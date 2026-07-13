# Panama 性能优化执行计划（v2）

> 创建时间：2026-07-13
> 来源：`hyperscan-java-test` GitHub Pages 报告（2026-07-12 20:23 UTC，commit 7d91ebc8）
> 目标：缩小 Panama 绑定层相对于 JavaCPP 在高频 downcall 大文件扫描场景中的性能差距

---

## 一、最新报告量化结论

报告统计 35 benchmark × 6 平台 = 210 数据点：
- **88 项 Panama 落后**、112 项领先、10 项持平
- Panama 完全不输的 benchmark（9 项）：`ISA fixed workload`、`ISA granularity`、`compileLargeSet`、`hasMatchShortText`、`mixedPatternThroughput`、`scanByteBuffer`、`scanLongText`、`scanManyLiteralPatterns`、`scanShortText`
- Panama 在全部 6 平台落后（2 项）：`scanGigabytesStreamingMatch_foobar_z_1GB`、`scanGigabytesStreamingMatch_foobar_z_2GB`
- Panama 在 ≥4/6 平台落后：`compileSmallSet`（4/6，仅 −1~−6%）、`scanGigabytesBlockMatch_foobar_z`（5/6，最差 −61.5%）、`scanGigabytesStreamingMatch_hatstand__teakettle__badgerbrush_2GB`（5/6）、`scanGigabytesStreamingMatch_hatstand__teakettle__badgerbrush_z_1GB/2GB`（4/6、5/6）、`scanSeveralGigabytesNoMatch`（4/6，最差 −51.3%）

### 失败模式归类

1. **大块/流式扫描（`scanGigabytes*` / `scanSeveralGigabytesNoMatch`）**：Panama 几乎全输，落幅最大（最高 −61.5%）
2. **`_z` 后缀（无匹配 / 快速拒绝）变体最痛**：`foobar_z_1GB/2GB` 在 6/6 平台全输；native 几乎立即返回时，per-call 开销在总时长里占比最高
3. **`compileSmallSet`**：唯一非 gigabyte 落败项，但落幅仅 1–6%
4. **平台偏向**：`linux-x86_64-baseline` 与 `windows-x86_64-baseline` 是 Panama 黑洞（−40~−55% 常见）；`linux-x86_64-avx2` 多数接近持平

## 二、根因分析

所有输掉的 gigabyte benchmark 都是**小循环体里密集 downcall** 的工作负载：
- `ScanGigabytesBlockMatchBenchmark.scan()`：对同一 1 MB `ByteBuffer` 循环 1000 次 `api.scan(...)`
- `ScanGigabytesStreamingMatchBenchmark.scan()`：GB 数据以 1 MB 为单位循环 1024 次 `hs_scan_stream`

每次 native 调用处理 1 MB，native 端字面量扫描只几微秒，**总墙上时间几乎完全由 per-call 跨边界开销决定**。FFM downcall（Linker 参数封送 + `MemorySegment.ofBuffer().asSlice()` 视图 + 匿名 handler 分配）相比 JavaCPP tightly-tuned JNI vtable 调用更重，在 1024 次/GB 密度下被放大成可见差距。`_z` 变体 native 立即返回，开销占比最大，所以落后最惨。

## 三、仓库边界与责任划分

| 仓库 | 控制层 | 对 gigabyte 落差的因果 | 可改 |
|---|---|---|---|
| `hyperscan-java` | JavaCPP wrapper `Scanner.java`，每次 `hs_scan_stream` 的 JNI downcall + `new BytePointer` + 匿名 handler | JavaCPP per-call 开销来源 | **否（锁定）** |
| `hyperscan-java-native` | vectorscan `.so`/`.dll` 构建（`-march`/`BUILD_AVX2`/SIMD tier）+ JavaCPP preset + loader | 只影响 native 执行时间，不影响跨边界 per-call 开销 | 是，但与本次差距无关 |
| `hyperscan-java-panama` | FFM wrapper + jextract 绑定 + native 层 | Panama per-call 开销来源 | **是** |
| `hyperscan-java-test` | `PanamaAdapter` 测试 harness | 测试侧的 FFM 调用与 handler 包装 | 是 |

### `hyperscan-java-native` 范围结论

我列的优化全部落在 FFM/wrapper 层：
1. 缓存 `MemorySegment` 视图 → 仅 FFM → panama + test 仓库
2. 复用 final handler → JavaCPP 侧对应代码在 `hyperscan-java Scanner.java`（锁定）；Panama 侧可改
3. `Linker.Option.critical` → FFM 专属
4. sibling panama 修复 → panama 专属

`hyperscan-java-native` 既不产生 FFM 调用，也不产生 JavaCPP JNI 调用（在锁定的 `hyperscan-java`），这些优化无处落脚。JavaCPP 在 gigabyte benchmark 本来就**赢**，再优化 native 构建只会拉大差距。

唯一理论上能切中同一根因、且部分落在这个仓库的方向——新增 native 批量扫描入口（一次 downcall 扫 N 个 chunk）——也碰壁：JavaCPP 要调用它需在 `hyperscan-java` 改绑定 + wrapper（锁定）；Panama 路径应在 `hyperscan-java-panama/native` 实现而非 `hyperscan-java-native`。**故本轮优化不涉及 `hyperscan-java-native`。**

## 四、优化计划（按 ROI 排序）

### 4.1 高 ROI：缓存 per-thread `MemorySegment` 视图

**问题**：每次 `scan(ByteBuffer)` 调 `MemorySegment.ofBuffer(input).asSlice(position, length)` 创建新视图（Scanner.java:249, PanamaAdapter.java:601）。1024 次/GB 时分配流量显著。

**方案**：
- `PanamaAdapter`（test）：为 direct `ByteBuffer` 缓存 per-thread `MemorySegment`（按 buffer identity），direct 路径跳过 `ofBuffer().asSlice()`
- `hyperscan-java-panama Scanner.java`：direct buffer 路径同样缓存视图

### 4.2 高 ROI：复用 handler 实例，消除匿名内部类

**问题**：每次 scan 分配新匿名 `ByteMatchEventHandler`（PanamaAdapter.java:502, 514；Scanner.java:238-256）。叠加 1024 次/GB 在年轻代制造流量。

**方案**：
- `PanamaAdapter`：把匿名 handler 改为可复用 final 实例，state 通过 ThreadLocal 传入
- `Scanner.java`：同样复用 final handler adapter，把 `ctx.byteHandler` 设置到 `CallbackContext` 即可

### 4.3 中 ROI：`Linker.Option.critical(true)` fast-path

**问题**：对不回调、不阻塞的短函数，FFM 会在 downcall 前后状态保存/恢复，without critical，每次都分配 downcall。

**方案**（仅短函数，绝不用于 `hs_scan`/`hs_scan_stream`/`hs_compile_multi`）：
- `hs_free_database`、`hs_free_scratch`、`hs_scratch_size`、`hs_database_size`、`hs_valid_platform`、`hs_version`
- 落点：`hyperscan-java-panama/native/src/main/template/HyperscanJniImpl.java.template` 或重新生成

### 4.4 低 ROI：compileSmallSet 开销（落幅仅 1–6%）

**问题**：编译路径每次 confined Arena 开/关 + 多段分配。

**方案**（次要，不影响 gigabyte benchmark）：
- `Database.compile` 在 `NativeExpressionCollection` 批量分配 pattern 字符串
- 移除一次性 wrapper 对象
- `Expression[] expressionsById` 为主要索引；`HashMap<Integer, Expression>` 延迟初始化

### 4.5 测试 harness 同步改造

**问题**：`PanamaAdapter` 流式/vector 回调用 `buildExpressionLookup` + 数组索引已对，但每次仍 `STREAM_CALLBACK.set(newHandlerContext(...))` 新建 record。

**方案**：
- 复用 `HandlerContext`（mutable 字段），避免每次 scan 新建 record
- gigabyte benchmark 侧（`ScanGigabytes*`）显式喂 direct `ByteBuffer`，避免 heap buffer 拷贝路径

## 五、执行阶段

### Phase 1：`hyperscan-java-panama` wrapper 优化

| 文件 | 关键改动 |
|---|---|
| `wrapper/.../Scanner.java` | direct buffer 缓存视图；handler 实例复用；`CallbackContext` 复用 |
| `wrapper/.../Database.java` | `Expression[]` 为主索引；`HashMap` 延迟初始化 |
| `native/src/main/template/HyperscanJniImpl.java.template` | 短函数加 `critical(true)` |

### Phase 2：`hyperscan-java-test` 适配器与 benchmark 优化

| 文件 | 关键改动 |
|---|---|
| `src/test/.../dual/PanamaAdapter.java` | `HandlerContext` 改为可复用 mutable；direct `ByteBuffer` 缓存 `MemorySegment` |
| `src/test/.../benchmarks/jmh/large/ScanGigabytes*Benchmark.java` | 确认已用 direct buffer（已是）；走新缓存路径 |

### Phase 3：验证

1. `hyperscan-java-panama` 构建：`mvn -B install -DskipTests`
2. `hyperscan-java-test` 功能回归：`mvn -B -U test -Dtest=!*Benchmark*`（视实际 surefire 配置）
3. JMH 对比：`mvn -B -U test -Dtest=JmhBenchmarkRunner ... -Dhyperscan.benchmark.implementation=PANAMA`，同样跑 `JAVACPP` 作基准
4. 对比新报告与 7d91ebc8 报告，确认 gigabyte 差距是否收敛

## 六、风险与决策

| 风险 | 应对 |
|---|---|
| `Linker.Option.critical` 滥用阻塞 GC 安全点 | 只用于不回调、不阻塞短函数；`hs_scan`/`hs_scan_stream`/`hs_compile_multi` 不加 |
| `MemorySegment.ofBuffer` 缓存与 buffer 位置突变 | 仅在 direct buffer 且 position/limit 不变时复用；否则重建视图 |
| `Database` 的 `equals`/`hashCode` 依赖 `HashMap` | 延迟初始化 `HashMap`，行为一致 |
| 直接内存视图泄漏 | 缓存随 ThreadLocal 生命周期管理，不持有 arena 资源 |
| 测试 harness mutable HandlerContext 线程安全 | ThreadLocal 隔离，scan 单线程内使用 |

## 七、进度追踪

- [x] 输出本文档到 `docs/performance-optimization-execution-plan.md`
- [ ] Phase 1.1：`Scanner.java` direct buffer 缓存视图
- [ ] Phase 1.2：`Scanner.java` handler 实例复用
- [ ] Phase 1.3：`Database.java` 数组主索引 + HashMap 延迟
- [ ] Phase 1.4：短函数 `critical(true)`
- [ ] Phase 2.1：`PanamaAdapter` HandlerContext 可复用
- [ ] Phase 2.2：`PanamaAdapter` direct buffer MemorySegment 缓存
- [ ] Phase 3：构建、测试、JMH 对比

## 八、参考

- 报告来源：https://xenoamess.github.io/hyperscan-java-test/
- 相关代码：
  - `hyperscan-java-panama/wrapper/src/main/java/com/xenoamess/hyperscan_panama/wrapper/Scanner.java:238-362`
  - `hyperscan-java-panama/wrapper/src/main/java/com/xenoamess/hyperscan_panama/wrapper/Database.java`
  - `hyperscan-java-test/src/test/java/com/xenoamess/hyperscan/smoke/dual/PanamaAdapter.java:563-636`
  - `hyperscan-java-test/src/test/java/com/xenoamess/hyperscan/smoke/benchmarks/jmh/large/ScanGigabytesStreamingMatchBenchmark.java:95-98`