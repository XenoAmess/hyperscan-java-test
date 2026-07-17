# 性能报告追加原版（Upstream）hyperscan-native 对比 — 设计方案

> 时间：2026-07-17
> 目标：在 `hyperscan-java-test` 的性能报告中追加第三个对比实现 —— 原版 `com.gliwka.hyperscan:native`，要求 7 档 CPU/平台矩阵每档都有对应对比，原版不支持的档位明确输出 `unsupported`。

---

## 一、调研结论（决定设计走向的 4 个硬事实）

1. **原版产物矩阵**（`com.gliwka.hyperscan:native:5.4.12-2.0.4`，2025-11-05 发布，vectorscan 5.4.12 源码）：仅 `linux-x86_64`、`linux-arm64`、`macosx-x86_64`、`macosx-arm64` 四个单档 classifier —— 无 Windows、无 ISA tier 变体。当前 7 档 CI 矩阵中：
   - `linux-x86_64-baseline` / `linux-x86_64-avx2` / `linux-x86_64`：可跑（原版同一 linux-x86_64 JAR 在各档 runner 上测，即"原版在该 CPU 档位的表现"）
   - `linux-arm64-baseline` / `linux-arm64`：可跑（同一 linux-arm64 JAR）
   - `windows-x86_64-baseline` / `windows-x86_64`：**unsupported**（无 classifier）
2. **类名冲突**：原版与 `com.xenoamess.hyperscan:native` 同为 `com.gliwka.hyperscan.jni.*`，不能同 classpath。用 Maven profile 互斥切换：`native-xenoamess`（activeByDefault）+ `native-gliwka`（`-Dnative.flavor=gliwka` 激活，activeByDefault 自动失效）。
3. **原版无 `HyperscanNativeLoader`**（tier 选择 loader 是 fork 私有类），`JavaCppAdapter.java:58` 直接调用它 → 原版 flavor 下需 `NoClassDefFoundError` 兜底（JavaCPP 生成类的 static init 自动 `Loader.load()`，原版依赖该机制）。原版 base JAR 类清单已核实含 `hs_alloc_t`/`hs_free_t`/`match_event_handler` 等全部所需类。
4. **报告脚本对 javacpp/panama 成对写死**（`build_platform_summary`、固定档表格、双条形图、per-platform details 循环），且 `BenchmarkRecorder` 不把 `implementation` 写进 JSON（靠文件名嗅探，`upstream` 嗅探不到会误判为 javacpp）—— 两处都要改。

## 二、命名与语义（用户已拍板）

- 版本：pin `upstream.version=5.4.12-2.0.4`（单一来源在 pom）
- 命名：JSON/文件名 `upstream`，报告列名 `Upstream`
- 胜出语义：三者全面对比 —— Faster=三者最优，Speedup=最优对次优；Executive Summary 的 best/worst 池纳入 upstream、排除 unsupported
- 范围：仅 benchmark，smoke 不跑原版
- unsupported 表示：marker JSON `{platform, implementation:"upstream", unsupported:true, reason, benchmarks:[]}`

## 三、执行计划（2 个 commit）

### Commit 1 — `feat(benchmarks): add upstream (gliwka) native as third comparison target`

**`pom.xml`**：
- 属性 `upstream.version=5.4.12-2.0.4`、`native.flavor=xenoamess`
- 4 个 `com.xenoamess.hyperscan:native` 依赖移入 profile `native-xenoamess`（activeByDefault）
- 新 profile `native-gliwka`（`native.flavor=gliwka` 激活）：原版 base jar + `${upstream.native.classifier}` classifier 依赖；OS profile 设 classifier（linux-x86_64 / linux-arm64）

**Java**：
- `DualImplementation.UPSTREAM` → `createAdapter() = new JavaCppAdapter()`（toString → `"upstream"`）
- `JavaCppAdapter` static init：`HyperscanNativeLoader.load()` 加 `NoClassDefFoundError` 兜底
- `BenchmarkRecorder`：`"implementation"` 字段写入 JSON

**`.github/workflows/smoke-test.yml`** benchmark job：
- Linux 档新增 `Run JMH benchmarks for UPSTREAM`：`BASE_PLATFORM` 去 `-avx2`/`-baseline` 尾档后传基档给 loader；`-Dnative.flavor=gliwka -Dhyperscan.benchmark.implementation=UPSTREAM -Dbenchmark.platform=${{ matrix.platform }}`；保留 cpu-check 门控（同机对比公平 + 缺特性时 retry 机制换机）
- Windows 档新增 marker 步骤：写 `benchmark-result-upstream.json`（`unsupported:true` + reason）到 `target/benchmark-results/`

### Commit 2 — `feat(report): render upstream implementation and unsupported rows`

- `generate-performance-report.py`：
  - `build_platform_summary` 纳入 upstream（含 unsupported 标记读取）
  - 固定档表格加 `Upstream (MB/s)` 列；unsupported 单元格显示 muted "unsupported"
  - Faster=三者最优，Speedup=最优/次优（跳过 unsupported）
  - 三系列条形图（Upstream 灰 #6a737d），unsupported 平台显示文字不画条
  - per-platform details 循环 3 实现，unsupported 显示原因行
  - Executive Summary best/worst 池排除 unsupported（否则 throughput=0 会被误判为 worst）
  - `implementation_for` 文件名嗅探兜底加 `'upstream'` 分支
- `generate-performance-svg.py`：同步第三系列 + unsupported 跳过
- README：What is tested 补 upstream 对比 + Windows unsupported 说明

## 四、验证

1. 本地 `-Dnative.flavor=gliwka` 功能子集：`WrapperSmokeTest,StreamAndVectorSmokeTest,DirectApiSmokeTest`（不含测 fork loader tier 的 PlatformSelectionTest）
2. 本地 `JmhBenchmarkRunner` UPSTREAM + `-Djmh.include` 单场景，检查 `benchmark-result-upstream.json`
3. 手工 unsupported marker + 样本目录跑两个 python 脚本，目检 HTML/SVG
4. push 后 CI 全量验证

## 五、风险

| 风险 | 对策 |
|---|---|
| 原版生成的 JNI 类与测试代码签名有出入 | 验证步骤 1 兜底；同 preset 血统 + 同 5.4.12 头文件，预期一致 |
| benchmark job 时长 +~40%（第三次 JMH 调用） | 接受；要控时可后续给 upstream 减 large 场景 |
| 原版 loader 不识别 `-avx2` 平台名 | 工作流 sed 去尾档传基档平台 |

## 六、进度追踪

- [x] Commit 1：pom profile + Java 适配 + CI workflow（`6cd3baa`）
- [x] 验证 1：gliwka flavor 功能子集（58 测试全绿）
- [x] 验证 2：UPSTREAM JMH 单场景（`benchmark-result-upstream.json` 含 `"implementation": "upstream"` 与原版自标签版本号）
- [x] Commit 2：报告脚本 + SVG + README（`db2c13d`）
- [x] 验证 3：样本目录目检 python 输出（三系列 + unsupported 渲染正确）

### 实施偏差记录

- **activeByDefault 失效**：`native-xenoamess` 用 `activeByDefault` 激活时会被常驻的 OS 激活 profile（panama-linux-*）连带停用；POM `<properties>` 默认值又不参与 property 激活判定。最终方案：`native-xenoamess` 以 `!native.flavor`（属性缺席）激活，不设置 POM 默认值；约定不要显式传 `-Dnative.flavor=xenoamess`。
- **编译期类缺失**：`HyperscanNativeLoader` 在原版 JAR 中不存在，`import` 直接编译失败；改为 `HyperscanTestHelper.loadNativeLibrary()` 反射调用（`Class.forName` 找不到时走 JavaCPP 自动加载）。`getPlatform()` 改用 `Loader.getPlatform()`，两种 flavor 语义均正确。
- **双跑测试防三跑**：`dual/DualApiArgumentsSource` 原遍历 `DualImplementation.values()`，新增 UPSTREAM 会导致全部双跑测试变三跑，已改为显式枚举 JAVACPP/PANAMA。
