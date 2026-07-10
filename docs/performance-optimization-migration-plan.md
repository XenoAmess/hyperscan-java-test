# 性能优化跨仓库迁移计划

> 来源：`hyperscan-java-test` 近期对 Panama 适配层做的性能优化。
> 目标：判断这些优化是否可以/应该下沉到 `hyperscan-java-panama`（Java 绑定层）或 `hyperscan-java-native`（原生构建层），并在适用时两边同步实施。

---

## 一、`hyperscan-java-test` 中现有性能优化梳理

| 优化点 | 所在文件 / 提交 | 迁移目标 | 优先级 | 说明 |
|--------|-----------------|----------|--------|------|
| **MethodHandle 缓存** | `src/test/java/com/xenoamess/hyperscan_panama/jni/generated/*.java`<br>commits `fe5e999` / `8f8b356` | `hyperscan-java-panama` | 低 | jextract 生成的 `hyperscan` / `hs_*` 类本身已经是 `static final MethodHandle` 缓存。`HyperscanJniImpl` 只是薄封装，再做一层收益很小。 |
| **直接 `Linker.upcallStub` 回调** | `src/test/java/com/xenoamess/hyperscan/smoke/dual/PanamaAdapter.java`<br>commit `8373082` | `hyperscan-java-panama` | **高** | `HyperscanJniImpl.allocateMatchEventHandler` 现在仍走 `match_event_handler.allocate(...)` → `MethodHandleProxies` 代理。可直接用 `Linker.upcallStub` 省一层代理。 |
| **静态 `match_event_handler` + ThreadLocal 复用** | `src/test/java/com/xenoamess/hyperscan/smoke/HyperscanTestHelper.java` / `PanamaAdapter.java`<br>commit `cdf50fc` | `hyperscan-java-panama` | 已存在 | `hyperscan-java-panama/wrapper/Scanner.java` 里已经是 `CALLBACK_ARENA = Arena.global()` + `ThreadLocal<RawMatchEventHandler>` 模式，无需迁移。 |
| **Arena / Scratch 生命周期管理** | `PanamaAdapter.java` / `HyperscanTestHelper.java` | `hyperscan-java-panama` | 中低 | 可审计 wrapper 中大量 `try (Arena arena = Arena.ofConfined())` 是否能复用或避免重复创建。 |
| **原生库编译参数调优** | `hyperscan-java-native` 的 `build.sh` / `pom.xml` | `hyperscan-java-native` + `hyperscan-java-panama/native` | **高** | 剩余 `scanGigabytes*` 回归主要在大块输入扫描，差距来自原生库本身。需要统一/比较 JavaCPP 与 Panama 的 Vectorscan 编译参数（`MARCH`、`BUILD_AVX*`、`-O3`、LTO 等）。 |
| **全局 warm-up / 确定性数据生成** | `InstructionSetGranularityTest.java` / `BenchmarkSuiteTest`<br>commit `cdf50fc` | `hyperscan-java-panama/performance` | 中 | 若 `hyperscan-java-panama` 有独立性能测试模块，可同步 warm-up 逻辑与确定性 seed 数据生成。 |

---

## 二、迁移计划清单

### 第一阶段：`hyperscan-java-panama` 层可直接复用的优化

#### 1. 替换 `HyperscanJniImpl.allocateMatchEventHandler` 为直接 `Linker.upcallStub`

- **目标文件**：`hyperscan-java-panama/native/target/generated-sources/.../HyperscanJniImpl.java`
- **推荐做法**：改生成器模板 `hyperscan-java-panama/native/generate-hyperscan-jni-impl.sh`，在生成 `allocateMatchEventHandler` 时直接输出：
  ```java
  @Override
  public MemorySegment allocateMatchEventHandler(MatchEventCallback callback, Arena arena) {
      try {
          MethodHandle mh = MethodHandles.lookup()
                  .findVirtual(HyperscanJni.MatchEventCallback.class, "onMatch",
                          MethodType.methodType(int.class, int.class, long.class, long.class, int.class))
                  .bindTo(callback);
          return Linker.nativeLinker().upcallStub(
                  MethodHandles.dropArguments(mh, 4, MemorySegment.class),
                  match_event_handler.descriptor(),
                  arena);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
  }
  ```
- **同步验证**：`hyperscan-java-panama/wrapper` 单元测试 + `hyperscan-java-test` 全量 CI。
- **预期收益**：减少流式/匹配密集型场景的一层 `MethodHandleProxies` 动态代理开销。

#### 2. 审计并复用 `hs_alloc_t` / `hs_free_t` 的直接 upcall stub

- 若 `hyperscan-java-panama` 的自定义 allocator 路径也经过 `hs_alloc_t.allocate` / `hs_free_t.allocate`，同样可改为 `Linker.upcallStub`。
- 目标文件：`HyperscanJniImpl.java` 或 wrapper 中自定义 allocator 相关代码。

#### 3. 同步 benchmark warm-up 与确定性数据生成

- 检查 `hyperscan-java-panama/performance` 是否有类似 `InstructionSetGranularityTest` 的随机/非确定性数据生成。
- 统一 warm-up 次数、确定性 seed、scratch 释放等 benchmark 基础设施。

---

### 第二阶段：`hyperscan-java-native` / 原生构建层优化

#### 4. 统一 JavaCPP 与 Panama 的 Vectorscan 编译参数

- 对比 `hyperscan-java-native/build.sh` 与 `hyperscan-java-panama/native/build.sh`：
  - `CMAKE_BUILD_TYPE=Release`
  - `MARCH` / `BUILD_AVX2` / `BUILD_AVX512` / `BUILD_AVX512VBMI`
  - `FAT_RUNTIME=off`
  - 是否开启 LTO、PGO、`-O3`、`-DNDEBUG`
- 确保两个仓库对同一 platform variant（如 `linux-x86_64-baseline`）使用等价的编译优化级别。
- **预期收益**：消除 `scanGigabytes*` 这类原生主导型基准的差距。

#### 5. 评估原生库选择 / 向量扫描 fork

- 当前两边都用 `VectorCamp/vectorscan`。若 JavaCPP 侧仍用原版 Intel Hyperscan，需确认是否因 fork 差异导致性能差异。
- 考虑针对 baseline / AVX2 / AVX512 分 variant 的精细化编译标志。

---

### 第三阶段：低优先级 / 可选

#### 6. 减少 `HyperscanJniImpl` 封装层开销

- 若 profiling 证明 `HyperscanJniImpl.hsScan` 等方法的接口转发有开销，可考虑让 wrapper 直接调用生成的 `hyperscan` 类，或让生成器内联。
- 当前 jextract 已缓存 MethodHandle，收益预计很小。

#### 7. Arena 复用审计

- 扫描 `hyperscan-java-panama/wrapper` 中高频调用路径（`Scanner.scan`、`Database.compile`）是否重复创建 `Arena.ofConfined()`。
- 对可安全复用的场景改用长期 arena 或线程局部 arena。

---

## 三、执行顺序

1. **先做 `hyperscan-java-panama` 的 direct upcall stub 优化**（高优先级、影响明确、可复用回调模式）。
2. **同时或紧随其后做原生编译参数对齐**（解决剩余 `scanGigabytes*` 回归）。
3. 完成后重新跑 `hyperscan-java-test` CI，验证回归是否进一步缩小。
4. 若仍有差距，再进入低优先级项（Arena 复用、封装层开销）。

---

## 四、状态追踪

- [ ] 创建本文档
- [ ] `hyperscan-java-panama`：`allocateMatchEventHandler` 改为直接 upcall stub
- [ ] `hyperscan-java-panama`：审计 `hs_alloc_t` / `hs_free_t` 是否同样可优化
- [ ] `hyperscan-java-panama/performance`：同步 warm-up / 确定性数据生成
- [ ] `hyperscan-java-native` / `hyperscan-java-panama/native`：对齐 Vectorscan 编译参数
- [ ] 重新跑 `hyperscan-java-test` CI 验证
- [ ] 可选：Arena 复用审计、封装层开销优化
