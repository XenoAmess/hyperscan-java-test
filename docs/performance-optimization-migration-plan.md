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
| **原生库编译参数调优** | `hyperscan-java-native` 的 `build.sh` / `pom.xml` | `hyperscan-java-native` + `hyperscan-java-panama/native` | **高** | 已核对：`build.sh` 中 Vectorscan 编译参数（`MARCH`、`BUILD_AVX*`、`FAT_RUNTIME`、`-O3` 等）在 Linux 下已完全一致；差异仅在脚本末尾的 Maven 调用方式。Windows 构建脚本存在差异（VS vs Ninja），但与当前 Linux `scanGigabytes*` 回归无直接关联。需进一步排查原生库本身或加载/variant 选择逻辑。 |
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

- 当前 `HyperscanJni` 接口未暴露自定义 allocator 路径（`hs_alloc_t` / `hs_free_t`），wrapper 中亦未使用。此项不适用，无需迁移。
- 若未来在 `HyperscanJni` 中增加自定义 allocator 支持，可复用 `MethodHandle.dropArguments` + `Linker.upcallStub` 模式。

#### 3. 同步 benchmark warm-up 与确定性数据生成

- 已检查 `hyperscan-java-panama/performance/BenchmarkSuiteTest.java`：该模块已实现 `@BeforeAll warmUp()`，且所有 `Random` 实例均使用固定 seed（2026/2027/2028/2029/42/2030 等），数据生成已确定性化。
- `hyperscan-java-test` 侧在 commit `cdf50fc` 也已统一 warm-up 与 seed；两边理念一致，无需额外同步。
- 后续可关注 `hyperscan-java-panama/performance` 是否补充与 `hyperscan-java-test` 等价的流式扫描（streaming / `scanGigabytes*`）基准。

---

### 第二阶段：`hyperscan-java-native` / 原生构建层优化

#### 4. 统一 JavaCPP 与 Panama 的 Vectorscan 编译参数

- 已对比 `hyperscan-java-native/build.sh` 与 `hyperscan-java-panama/native/build.sh`：
  - 两个脚本在 Linux/macOS 下的 Vectorscan 构建部分（`CMAKE_BUILD_TYPE=Release`、`MARCH`、`BUILD_AVX2/AVX512/AVX512VBMI/SVE/SVE2`、`FAT_RUNTIME=off`、`BUILD_SHARED_LIBS=on`、`-DBUILD_BENCHMARKS=false` 等）完全一致。
  - 唯一差异在脚本末尾：`hyperscan-java-native` 直接调用 `mvn ... -Dorg.bytedeco.javacpp.platform=$DETECTED_PLATFORM` 生成 JavaCPP 绑定；`hyperscan-java-panama/native/build.sh` 仅构建原生库，不生成 Java 绑定。
  - Windows 脚本差异较大（Visual Studio 2026 + MSBuild vs Ninja），但当前主要 Linux 回归与 Windows 构建无关。
- 结论：原生编译参数已在 Linux 对齐，剩余 `scanGigabytes*` 差距不宜再通过调整编译参数解决。
- 下一步建议：比对两个仓库发布的原生 `.so` 文件（objdump/二进制 diff）或检查 variant 选择/加载逻辑是否导致运行时选用了不同优化级别的库。

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
   - **状态**：已完成，commit `100ed5b`。
2. **同时或紧随其后做原生编译参数对齐**（解决剩余 `scanGigabytes*` 回归）。
   - **状态**：已核对，Linux 下参数已对齐；剩余回归需进一步排查原生库/加载逻辑。
3. 完成后重新跑 `hyperscan-java-test` CI，验证回归是否进一步缩小。
4. 若仍有差距，再进入低优先级项（Arena 复用、封装层开销、variant 选择逻辑）。

---

## 四、状态追踪

- [x] 创建本文档
- [x] `hyperscan-java-panama`：`allocateMatchEventHandler` 改为直接 upcall stub
- [x] `hyperscan-java-panama`：审计 `hs_alloc_t` / `hs_free_t` 是否同样可优化（结论：不适用）
- [x] `hyperscan-java-panama/performance`：同步 warm-up / 确定性数据生成（结论：已具备）
- [x] `hyperscan-java-native` / `hyperscan-java-panama/native`：对齐 Vectorscan 编译参数（结论：Linux 已对齐）
- [ ] 重新跑 `hyperscan-java-test` CI 验证（待 `hyperscan-java-panama` 新发布版本后触发）
- [ ] 可选：排查剩余 `scanGigabytes*` 回归（原生库/加载/variant 选择）
- [ ] 可选：Arena 复用审计、封装层开销优化
