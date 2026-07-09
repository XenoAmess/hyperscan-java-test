# Vectorscan 测试移植与性能报告改进计划

## 目标

将 VectorCamp/vectorscan (`master`) 的功能测试和性能基准移植到 `hyperscan-java-test` 项目中，使用同一套参数化测试同时验证 JavaCPP 和 Panama 两种实现，并生成与 `hyperscan-java-panama` 项目结构一致的 per-scenario HTML 性能报告。

## 假设与约束

- 不修改 `vectorscan` 源码本身，只借用它的测试逻辑和数据文件。
- 保留现有 `DualApi` 的 wrapper 路径，新增直接调用 native 的路径作为扩展。
- 最低 Java 版本保持 `25`（`pom.xml` 中已配置）。
- 大文件（`leipzig-3200.txt`、`lh3lh3-reb-howto.txt`）用 Git LFS 管理。
- 性能测试默认开启，可通过 `-Dhyperscan.benchmarks.enabled=false` 关闭。
- 所有能同时跑 JavaCPP 和 Panama 的测试都要双跑。

## 计划

### Phase 1 — 对齐并改进性能报告

- **1.1 统一 JSON 输出格式**
  - 让 `BenchmarkRecorder` / `BenchmarkResult` 输出与 `hyperscan-java-panama` 一致的 JSON 顶层结构：`platform`、`nativeVersion`、`commitSha`、`runnerOs`、`runnerArch`、`cpuModel`、`cpuFlags`、`timestamp`、`benchmarks`。
  - 保留 `implementation` 字段用于本地聚合（文件名仍用 `benchmark-result-${implementation}.json`）。
  - 支持每个 benchmark 结果包含 `name` 和 `metrics`。

- **1.2 添加 benchmark 开关控制**
  - 给性能测试类加 `@Tag("benchmark")`。
  - 新增自定义 JUnit 条件：默认开启，当 `-Dhyperscan.benchmarks.enabled=false` 时跳过。

- **1.3 创建 `BenchmarkSuiteTest`**
  - 参考 `hyperscan-java-panama` 的 `BenchmarkSuiteTest`，复现以下 scenario：
    - `ISA granularity benchmark`（固定 500 patterns / ~20 KB）
    - `ISA fixed workload (counting only)`
    - `compileSmallSet`
    - `compileLargeSet`
    - `scanShortText`
    - `scanLongText`
    - `hasMatchShortText`
    - `scanManyLiteralPatterns`
    - `scanByteBuffer`
    - `mixedPatternThroughput`
  - 每个 scenario 同时用 JavaCPP 和 Panama 运行，各自写入 `BenchmarkResult`。

- **1.4 扩展 `generate-performance-report.py`**
  - 按 `benchmarks[].name` 分组，不再只读取 `benchmarks[0]`。
  - 保持现有：
    - Executive Summary
    - Fixed Workload Cross-Platform Comparison
    - Throughput Comparison
    - Per-Platform Details
    - Raw Data
  - 新增：
    - **Per-Benchmark Cross-Platform Comparison**：每个 scenario 一个表格，列出 platform、implementation、iterations、elapsed、throughput、ops/second、ns/op、totalMatches。
    - Per-Platform Details 中列出所有 scenario。

- **1.5 扩展 `generate-performance-svg.py`**
  - 默认绘制 overall fixed workload 的吞吐量对比图。
  - 支持通过命令行参数指定 scenario。

### Phase 2 — 扩展 `DualApi`（保留 wrapper fallback）

**状态：已完成。**

- 新增 `DualMode`：BLOCK、STREAM、VECTORED。
- 新增 `DualStream` 资源句柄。
- 在 `DualApi` 中新增方法：
  - `compileDatabase(List<DualExpression>, DualMode)` / `compileDatabase(DualExpression, DualMode)` / `compileDatabase(DualExpression[], DualMode)`
  - `openStream(DualDatabase)`
  - `scanStream(DualScanner, DualStream, byte[], handler)`
  - `closeStream(DualScanner, DualStream, handler)`
  - `scanVector(DualScanner, DualDatabase, byte[][], handler)`
  - `getDatabaseInfo(DualDatabase)` / `getSerializedDatabaseInfo(byte[])`
- 补全 `DualExpressionFlag`：UCP、COMBINATION、QUIET。
- 实现 `JavaCppAdapter`：直接调用 `com.gliwka.hyperscan.jni.hyperscan`。
- 实现 `PanamaAdapter`：直接调用 `com.xenoamess.hyperscan_panama.jni.generated.hyperscan`。
- 新增 `StreamAndVectorSmokeTest` 覆盖 streaming、vectored、database info、serialization，两个 adapter 都参与。

### Phase 3 — 测试数据基础设施

**状态：已完成。**

- 复制 `vectorscan/unit/hyperscan/datafiles/leipzig-3200.txt` 和 `lh3lh3-reb-howto.txt` 到 `src/test/resources/vectorscan/datafiles/`。
- 在 `.gitattributes` 中为这些文件配置 Git LFS：`src/test/resources/vectorscan/datafiles/*.txt filter=lfs diff=lfs merge=lfs -text`。
- 新增 `com.xenoamess.hyperscan.smoke.util.TestData` 工具，提供 `readBytes(String)` / `readString(String)` / `stream(String)`，并定义 `LEIPZIG_3200`、`LH3LH3_REB_HOWTO` 常量。
- 新增 `TestDataTest` 验证两个文件可从 classpath 读取。

### Phase 4 — 移植功能测试

- 在 `com.xenoamess.hyperscan.smoke.vectorscan` 包下，为 `unit/hyperscan/` 的 19 个 C++ 测试文件各创建一个 JUnit 5 参数化测试类。
- 每个测试使用 `@ArgumentsSource(DualApiArgumentsSource.class)`，同时跑 JavaCPP 和 Panama。
- 不兼容的测试用 `@Disabled` 加标准化原因码：
  - `MMAP_REQUIRED`
  - `CUSTOM_ALLOCATOR`
  - `LARGE_INPUT`
  - `CPP_INTERNAL`
  - `CHIMERA`
- 大输入测试用 `@Tag("large-input")`，默认不跑，可通过属性或 JUnit tag 启用。
- 创建 `docs/vectorscan-port-status.md` 记录移植状态、禁用原因和迁移说明。

### Phase 5 — 移植性能基准

- 将 `vectorscan/benchmarks/benchmarks.cpp` 移植为 `com.xenoamess.hyperscan.smoke.benchmarks.VectorscanBenchmarksTest`。
- 使用扩展后的 `DualApi` 和新的 benchmark 记录器，每个 scenario 双跑并记录。
- 复用 `BenchmarkSuiteTest` 的固定 workload，确保与 `hyperscan-java-panama` 可横向对比。

### Phase 6 — 验证与文档

- 运行 `mvn test`。
- 运行 `mvn test -Dhyperscan.benchmarks.enabled=false` 验证 benchmark 关闭。
- 检查 `target/benchmark-results/` 和 `target/performance-report/` 输出。
- 检查 Git LFS 跟踪是否正确。
- 更新 `docs/vectorscan-port-status.md` 和 README 中的相关说明。

## 验收标准

- `mvn test` 在本地通过，且性能测试结果写入 JSON。
- `.github/scripts/generate-performance-report.py` 生成的 HTML 包含 per-scenario 表格。
- `generate-performance-svg.py` 能生成 SVG 摘要。
- 所有能双跑的功能/性能测试都使用 `DualApiArgumentsSource` 双跑。
- `docs/vectorscan-port-status.md` 完整记录禁用项和原因。
