# Panama 性能优化执行计划

> 创建时间：2026-07-12
> 来源：基于 `hyperscan-java-test` CI run `#29193405673` 的 benchmark 报告
> 目标：缩小 Panama 绑定层相对于 JavaCPP 在编译与大文件扫描场景中的性能差距

---

## 一、背景与目标

最新 benchmark 报告显示，Panama 在以下场景落后于 JavaCPP：

- **编译**：`compileSmallSet`（多平台）、`compileLargeSet`（x86_64-avx2、windows-x86_64-baseline）
- **大文件扫描**：`scanGigabytesBlockMatch`、`scanGigabytesStreamingMatch`、`scanSeveralGigabytesNoMatch`（arm64-baseline、x86_64-baseline、部分 x86_64-avx2）

**目标**：在 `hyperscan-java-panama` 绑定层做通用优化，再同步优化 `hyperscan-java-test` 的 Panama 适配器，最后用 JMH 验证差距是否收敛。

---

## 二、根因分析

差距并非来自 Vectorscan/Hyperscan 原生引擎本身（Linux 下原生编译参数已对齐），而是来自 **Panama 绑定层与 JavaCPP 的绑定/调用开销差异**：

1. **FFM downcall 开销**：JMH 无 match 场景（`scanSeveralGigabytesNoMatch`）仍落后，说明每次 `hs_scan`/`hs_scan_stream` 的 FFM 调用本身比 JavaCPP 的 JNI 调用更慢。
2. **FFM upcall 开销**：有 match 场景差距更大。Panama 的 `Scanner.MATCH_HANDLER` 是 `Linker.upcallStub`，每个 match 需要从 native 进入 Java，查 `ThreadLocal` 上下文，再查 `Database.getExpression(id)`。
3. **wrapper 层额外工作**：
   - `Database.compile` 为每个 pattern 单独创建 `NativeExpression`，产生 O(n) 次 native 分配。
   - `Database` 构造时无条件创建 `HashMap<Integer, Expression>`，即使 ID 连续密集。
   - 扫描时 `byte[]`/`String` 需要先拷贝到线程本地 buffer。
4. **适配器层额外开销**：`PanamaAdapter` 流式/vector 回调使用 `findExpressionById` 线性查找，复杂度 O(n) 每 match。
5. **平台差异**：baseline 平台（无 AVX/AVX2 SIMD）原生扫描更慢，上述固定开销占比更高，因此差距被放大。

---

## 三、优化策略

### 3.1 编译路径

- 把 `NativeExpressionCollection` 中 O(n) 次 `allocateFrom` 改为**一次批量 native 分配**，所有 pattern 字符串连续存放，指针数组指向偏移。
- 移除 `List<NativeExpression>` 这类一次性 wrapper 对象。
- `Database` 构造时只保留 `Expression[] expressionsById`；`HashMap<Integer, Expression>` 改为**延迟初始化**，仅在序列化或 `equals` 等需要时创建。

### 3.2 扫描回调

- 在 `Scanner.scanRaw` 前，把 `Database` 的 `Expression[]` 快照进 `CallbackContext`。
- FFM upcall 中直接用 `ctx.expressionsById[id]` 数组索引，替代 `ctx.db.getExpression(id)` 的数组→sparse map→HashMap 链路。
- 保留 `RawMatchEventHandler` 路径，让只关心 id 的调用方完全不查 `Expression`。

### 3.3 输入拷贝与直接内存

- 为 `Scanner` 增加公开重载：
  - `scan(Database, ByteBuffer, ByteMatchEventHandler)`
  - `scan(Database, MemorySegment, int, ByteMatchEventHandler)`
- direct `ByteBuffer` / `MemorySegment` 直接切片，不拷贝；`byte[]` 和 `String` 仍复用线程本地 buffer。
- benchmark 侧改用 direct buffer，消除每轮 `Unsafe.copyMemory` 开销。

### 3.4 适配器层

- `PanamaAdapter` 流式/vector 回调不再用 `findExpressionById` 线性查找，改为 open stream 前构建 `DualExpression[]` 按 id 索引，O(1) 查找。

### 3.5 Conservative Critical

- 仅对不回调、不阻塞、执行时间短的函数使用 `Linker.Option.critical`：
  - `hs_free_database`
  - `hs_free_scratch`
  - `hs_scratch_size`
  - `hs_database_size`
  - `hs_valid_platform`
  - `hs_version`
- **绝不**用于 `hs_scan`/`hs_scan_stream`（会触发 Java 回调）和 `hs_compile_multi`（耗时不定、大量内存分配）。

---

## 四、执行阶段

### Phase 1：JMH Profiling 支持

1. 文件：`hyperscan-java-test/src/test/java/com/xenoamess/hyperscan/smoke/benchmarks/jmh/JmhBenchmarkRunner.java`
2. 改动：读取 `-Djmh.profilers` 系统属性，解析后调用 `OptionsBuilder.addProfiler(...)`。
3. 支持 profiler：`perf:stack`、`async` 等。

### Phase 2：`hyperscan-java-panama` 绑定层优化

#### 2.1 编译路径

| 文件 | 关键改动 |
|---|---|
| `wrapper/src/main/java/com/xenoamess/hyperscan_panama/wrapper/NativeExpressionCollection.java` | 批量分配 pattern 字符串内存；移除 `List<NativeExpression>` |
| `wrapper/src/main/java/com/xenoamess/hyperscan_panama/wrapper/Database.java` | 延迟初始化 `HashMap`；密集 ID 时只用 `Expression[]` |

#### 2.2 扫描回调

| 文件 | 关键改动 |
|---|---|
| `wrapper/src/main/java/com/xenoamess/hyperscan_panama/wrapper/Scanner.java` | `CallbackContext` 增加 `Expression[]` 快照；upcall 中数组索引替代 `db.getExpression(id)` |
| `wrapper/src/main/java/com/xenoamess/hyperscan_panama/wrapper/ByteMatchEventHandler.java` / `RawMatchEventHandler.java` | 保持接口不变；raw 路径不查 Expression |

#### 2.3 直接内存

| 文件 | 关键改动 |
|---|---|
| `wrapper/src/main/java/com/xenoamess/hyperscan_panama/wrapper/Scanner.java` | 新增 `scan(Database, ByteBuffer, ...)` 和 `scan(Database, MemorySegment, int, ...)` 公开重载 |
| `wrapper/src/main/java/com/xenoamess/hyperscan_panama/wrapper/Scanner.java` | direct buffer 直接切片，heap buffer 复用线程本地 buffer |

#### 2.4 Conservative Critical

| 文件 | 关键改动 |
|---|---|
| `native/src/main/template/HyperscanJniImpl.java.template` | 对 free/size/info/version/valid_platform 类 downcall 加 `Linker.Option.critical` |
| 生成后的 `native/target/generated-sources/.../generated/hyperscan.java` | 重新生成或同步修改（若模板控制生成） |

### Phase 3：`hyperscan-java-test` 适配器优化

| 文件 | 关键改动 |
|---|---|
| `src/test/java/com/xenoamess/hyperscan/smoke/dual/PanamaAdapter.java` | 流式/vector 回调改为 `DualExpression[]` 索引查找；移除 `findExpressionById` 线性扫描 |
| `src/test/java/com/xenoamess/hyperscan/smoke/benchmarks/jmh/ScanGigabytesStreamingMatchBenchmark.java` 等 | 大文件 benchmark 使用 direct `ByteBuffer`/`MemorySegment`，通过新增重载喂给 Panama |

### Phase 4：验证与对比

1. 在 `hyperscan-java-panama` 仓库：
   - `mvn -B -pl wrapper test`
   - `mvn -B install -DskipTests`
2. 在 `hyperscan-java-test` 仓库：
   - `mvn -B -U test`（功能回归）
   - `mvn -B -U test -Dtest=JmhBenchmarkRunner ... -Dhyperscan.benchmark.implementation=PANAMA`
   - 同样跑 `JAVACPP` 作为基准
3. 对比新报告与 run `#29193405673`，确认差距是否收敛。

---

## 五、风险与决策

| 风险 | 应对策略 |
|---|---|
| `Linker.Option.critical` 滥用阻塞 GC/安全点 | 只用于不回调、不阻塞的短函数；`hs_scan`/`hs_compile_multi` 不加 |
| `Database` 的 `equals`/`hashCode` 依赖 `HashMap` | 延迟初始化 `HashMap`，保证行为一致 |
| 直接内存重载暴露 `MemorySegment` API | 仅作为新重载，保留原有 `byte[]`/`String` API，不破坏兼容 |
| 流式 API 不在 wrapper 中 | 本次先优化 `PanamaAdapter` 内的流式回调；wrapper 层流式 API 作为后续可选 |
| 修改后行为回归 | 先跑 `wrapper` 单元测试，再跑 `hyperscan-java-test` 全量 smoke 测试 |

---

## 六、进度追踪

- [ ] 输出本文档到 `docs/performance-optimization-execution-plan.md`
- [ ] 为 `JmhBenchmarkRunner` 增加 profiler 支持
- [ ] 优化 `hyperscan-java-panama` 编译路径
- [ ] 优化 `hyperscan-java-panama` 扫描回调
- [ ] 优化 `hyperscan-java-panama` 直接内存/输入拷贝
- [ ] 优化 `PanamaAdapter` 流式/vector 回调
- [ ] 构建、测试并跑 JMH 对比验证

---

## 七、参考

- `hyperscan-java-test` 最新 benchmark 报告来源：CI run `#29193405673`
- 相关代码：
  - `hyperscan-java-panama/wrapper/src/main/java/com/xenoamess/hyperscan_panama/wrapper/Database.java`
  - `hyperscan-java-panama/wrapper/src/main/java/com/xenoamess/hyperscan_panama/wrapper/NativeExpressionCollection.java`
  - `hyperscan-java-panama/wrapper/src/main/java/com/xenoamess/hyperscan_panama/wrapper/Scanner.java`
  - `hyperscan-java-test/src/test/java/com/xenoamess/hyperscan/smoke/dual/PanamaAdapter.java`
  - `hyperscan-java-test/src/test/java/com/xenoamess/hyperscan/smoke/benchmarks/jmh/JmhBenchmarkRunner.java`
