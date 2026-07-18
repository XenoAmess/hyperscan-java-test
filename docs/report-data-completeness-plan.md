# 性能报告数据完整性修复方案

> 时间：2026-07-17
> 问题：性能报告（https://xenoamess.github.io/hyperscan-java-test/）数据缺失概率高。实证：当前线上报告缺 `linux-x86_64`（AVX-512 档）整行；最近 4 次 run 中 AVX-512 benchmark 步骤 3 次因 runner 无 AVX-512 被整体跳过，仅 1 次真正执行。

---

## 一、根因分析

1. **Runner 抽奖**：AVX-512 / SVE2 档位依赖特定 CPU 特性，GitHub runner 池命中不稳定。抽不到时该 cell 的 smoke/benchmark 步骤全部 skip，当轮该档位数据为空。
2. **报告无记忆**：report job 只用当次 run 的 benchmark-raw artifacts 重建报告 → 缺失档位当轮从报告消失。
3. **互相覆盖**：retry-dispatch 重跑整个 workflow 且同样抽奖；新 run 的报告覆盖上一轮完整报告（实证：07:46 run 产出完整报告，07:48 run 又漏 AVX-512 并覆盖）。
4. **flake 放大**：任一 benchmark cell 失败（依赖解析 / TLS flake）→ 整个 benchmark 矩阵 failure → report job 跳过 → 报告停更。实现数从 2 增至 3 后，单 cell 失败概率进一步放大。

## 二、方案（A+B+C+D，2 个 commit）

### Commit 1 — `ci(report): merge benchmark results with previous run and mark stale cells`

**A. 跨 run 数据合并（核心）**
- 新增 `.github/scripts/merge-benchmark-results.py <current> <previous> <out>`：
  - 按 `(platform, implementation)` 键合并；当次数据优先；缺失格 fallback 到上一次成功 run 的数据并注入 `"stale": true`（保留原 timestamp；unsupported marker 无 timestamp 时脚注显示 "earlier run"）。
  - previous 不存在/为空（首轮）→ 直接透传 current。
  - 不做版本一致性校验（用户拍板）；合并深度=1，由 retry-dispatch 保证最终新鲜。
- report job 新增"下载上一次成功 run 的 benchmark-raw"步骤：`dawidd6/action-download-artifact`，`workflow_conclusion: success`，`name_is_regexp: true` 匹配 `benchmark-raw-*`，`continue-on-error: true`；两个 generator 改读合并目录。

**B. report 门禁放宽**
- 原：`needs.smoke.result == 'success' && needs.benchmark.result == 'success'` 才发报告。
- 改：`needs.smoke.result` ∈ {success, skipped}（skipped 仅供 D 的部分重跑）；`needs.benchmark.result` 不再进门禁。smoke 真失败仍不发报告（功能正确性门槛保留）。

**stale 渲染（`†` + 日期，用户拍板）**
- `is_stale(result)` helper 加入两个 generator。
- HTML：固定档表格、场景表格、per-platform details 中 stale 格/行加 `†`，对应节后加脚注 `† data from <timestamp>`。
- SVG：stale 行平台名后加 `†`，footer 加说明行。

### Commit 2 — `ci(benchmark): retry JMH invocations and re-dispatch only missing platforms`

**C. JMH 调用重试（抗 flake）**
- 3 个 JMH mvn 调用各包 bash 重试循环：最多 3 次、间隔 30s；最后一次仍失败则步骤失败。

**D. retry-dispatch 精细化**
- `workflow_dispatch` 新增 `platforms` input（逗号分隔子集，空=全量）。
- 新增 `setup` job：按 input 输出 matrix JSON（全量 7 档或按 platform 字段过滤子集）；smoke/benchmark 两 job 的 matrix 改为 `fromJSON(needs.setup.outputs.matrix)`，`needs: setup`。
- `retry-dispatch`：从 jobs API 提取被 skip 步骤名中的平台名（`Run ... on <platform>` 后缀，去重）→ `gh workflow run -f platforms=<csv>` 只重跑缺失 cell；INFLIGHT≤4 防死循环保留。
- cpu-miss 的 cell 其 smoke 同样从未跑过 → 部分重跑同时补 smoke + benchmark，语义自洽。
- 闭环：全量 run 漏档 → 报告 merge 旧数据（stale）→ 精准重跑该档 → 下轮报告该档恢复新鲜。

## 三、验证

1. 本地造 current/previous 样本目录跑 merge 脚本 + 两个 generator，目检 stale 渲染（`†` + 脚注日期）。
2. `setup` job 的 matrix 过滤逻辑本地模拟验证（全量 / 子集 / 非法平台名）。
3. YAML 解析检查；push 后观察一轮 CI（含自然发生的 cpu-miss → 精准重跑链路）。

## 四、进度追踪

- [x] Commit 1：merge 脚本 + report job + stale 渲染（`68058e2`）
- [x] 验证 1：样本目检（merge 计数、HTML/SVG `†` 与脚注正确）
- [x] Commit 2：重试 + setup matrix + retry-dispatch 精细化（`076c069`）
- [x] 验证 2：matrix 过滤模拟（全量/子集/非法名）、jq 提取 mock 验证、CI 观察

### 实施偏差与 CI 实证记录（2026-07-17）

- 首轮新链路 run（29567087388）：AVX-512 再次 cpu-miss，merge 输出 18 current + 0 stale（上一次成功 run 同样缺该档，深度 1 无法回补）→ retry-dispatch 精准触发 workflow_dispatch run（29567545024），仅含缺失 cell ✓
- **发现并修复误报**：retry-dispatch 的提取正则匹配到 `Run JMH benchmarks for UPSTREAM on windows-*` —— 该步骤在 Windows 上是**故意** skip（原版无 Windows 构建），被误判为 CPU miss 导致 Windows cell 被无谓重跑。修复：检测限定 `(JAVACPP|PANAMA)`（`8bf4dbf`）。Linux 上 UPSTREAM skip 与 JAVACPP/PANAMA 同源（cpu-check），不含 UPSTREAM 不会漏判。
- **深度 1 不足**：实测 AVX-512 连续 4 轮 cpu-miss，最近成功 run 同样缺档 → 追加多级回收：report job 从最近 5 个成功 run 的 benchmark-raw 回收缺失格（取最新副本，打 stale）（`1251b95`）。
- **端到端闭环验证**：精准重跑（29569434328，子集 linux-x86_64）最终命中 AVX-512 机器产出新鲜数据；其余 cell 由最近成功 run 回收（stale 带时间戳）——线上报告 7 档完整，新鲜度如实标注。

## 五、追加事故：报告 0.00 空洞与 libhs SIGILL（2026-07-17 下午）

用户复查发现报告仍有大量 0.00 缺失条目，深挖出三层叠加问题：

1. **空结果污染**：个别 run 的 JAVACPP JMH 步骤"成功"但写出 `benchmarks:[]`（JMH forked JVM 崩溃后静默返回空）。merge 的空值守卫（跳过空结果）+ `JmhBenchmarkRunner` 空结果即抛错（`948a800`）。
2. **backfill 顺序 bug**：report job 用 `ls -d benchmark-raw-prev-*` 重建目录列表，字母序破坏了 recency 顺序，导致回收了**最旧**（恰为空的）副本而非最新健康副本。改为按 RUN_IDS 顺序累积 PREV_DIRS（`948a800`）。
3. **根因 —— libhs SIGILL（`736389a`）**：hs_err 实锤：JMH forked JVM 加载了 `jni/linux-x86_64/libhs.so`（**AVX-512 构建**）在非 AVX-512 的 Zen3 runner 上于 `hs_compile_multi` 内 SIGILL。机制：JmhBenchmarkRunner 只转发 `-Djavacpp.platform`，而 fork loader 读的是 `org.bytedeco.javacpp.platform`；该属性缺失时裸平台被自动探测/缓存（loader 运行前），AVX-512 变体被加载。修复：同时转发 `-Dorg.bytedeco.javacpp.platform`，变体选择显式化，竞态消除。
4. 起爆时间线：~09:00 起集中爆发，与 ubuntu-24.04 runner 镜像（20260707.563）滚动更新导致的 JVM 启动顺序变化相符；此前同一代码路径健康运行数周属侥幸。
5. 结果：修复后 run 全绿，线上报告 7 档全为新鲜真实数据，无 0.00 无 †。

诊断辅助：`ci(benchmark)` 步骤新增失败时上传 hs_err + dumpstream（`169605f`），本轮正是靠它拿到实锤。

## 六、陈旧数据可追溯性（2026-07-18）

用户问"从报告中怎么知道陈旧数据是什么时候、什么版本产生的"。彼时：时间戳仅在固定档表脚注可见，版本完全不可知（JSON 的 `nativeVersion` 只是 hs 库 `hs_version()` 字符串，不含产物版本号）。补全：

- `d11ab67` `feat(benchmarks)`：`BenchmarkRecorder` 新增 `artifactVersion` 字段；`JmhBenchmarkRunner` 按实现从 jar 内 `META-INF/maven/<groupId>/<artifactId>/pom.properties` 解析版本（注意 jar 内是**点分** groupId，非仓库斜杠布局——首版误用斜杠导致 unknown，已修正并记录）。
- `d665d7d` `feat(report)`：stale 脚注升级为 `platform / impl (timestamp · artifactVersion · commit sha)`；Raw Data 节每条列出 artifactVersion + timestamp（新旧格式一视同仁）；SVG stale 行平台名追加 `†MM-DD` 短日期。旧格式（无 artifactVersion）降级为仅时间戳，不炸。

## 七、追加事故：报告全是陈旧标记（2026-07-18）

用户发现报告突然全是 †。根因：连续 3 个精准重跑（子集 run）的 AVX-512 cell 全部再次 cpu-miss，每个子集 run 都产出"0 新鲜 + 全 backfill"的报告并**覆盖**了此前健康的报告；同时 AVX-512 连续 5+ 轮缺席导致 depth-5 回收也失效（整行消失）。修复：

- `aa854da` `ci(report)`：report job 统计当次新鲜 benchmark JSON 数；为 0 时**不发布**（生成/上传/Pages 部署全部跳过，线上保留上一份健康报告），回收深度 5 → 10。
- `d9f0db2` `ci(report)`：修复上一提交自身引入的回归——全部 cell 缺席时 benchmark-raw 目录不存在，`find` 在 errexit 下炸掉整个 report job；加目录存在性判断。
- 验证：修复轮发布，线上报告 7 档全部新鲜值、0 个 †。
