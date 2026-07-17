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
- 29567545024 的 AVX-512 再次抽不到机器 → 按设计不再连环重跑（仅 push 事件触发 retry），缺失格由 merge 旧数据 + `†` 标记兜底。
