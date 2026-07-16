# 性能优化执行计划 v4 — wrapper 层收口 + stream/vectored API

> 时间：2026-07-17
> 范围：`hyperscan-java`（wrapper）、`hyperscan-java-native`（preset）、`hyperscan-java-panama`（wrapper + native 生成层）
> 前序：v2/v3 已落地（HandlerContext 复用、expressionById 缓存、direct buffer 缓存、LTO、直接 upcall stub）
> 明确排除：Windows vectorscan 迁移（vectorscan 5.4.12 不支持 Windows，永久不做）；P7（SCAN_BUFFER 只增不减）仅记录不动手

---

## 一、分析结论（现状审计）

### hyperscan-java（wrapper，分支 `perf/optimize-wrapper`）

commit `0140f60` 已落地静态 `matchHandler` + ThreadLocal 回调、String/hasMatch 路径 ThreadLocal direct buffer。剩余问题：

1. `Scanner.java:201` `scanRaw` 每次 `new BytePointer(input)` —— 每次 scan 都 native malloc + 全量 memcpy + 注册 deallocator；同类 `hasMatch(byte[])`（274-286）已走 ThreadLocal buffer，两路径不一致
2. `Scanner.java:221` 零拷贝 `scan(db, ByteBuffer, handler)` 是 private，公开入口只有 String/byte[]
3. `Database.java:21,141` 每次 match 回调 `HashMap.get`
4. `Utf8Encoder.java:47` 每次 String scan 按 `buffer.capacity()`（历史峰值 ×4）分配映射表；ASCII 场景映射恒等却照分配；回调把 `mappingSize==0` 当"空输入"语义（改快路径需同步调整）
5. `Database.java:253` `bitmaskToFlag` 每次 `load()` 重建
6. 缺 stream/vectored 公开 API（`RawMatchEventHandler` 是 package-private，无 hs_open_stream 封装）

### hyperscan-java-native（preset）

- Linux 构建已与 panama 对齐（clang + `-flto=thin`），无可挖
- `JavaCppPreset.java` 未使用 `@Critical`：短无回调函数可走 critical native 降 JNI 开销

### hyperscan-java-panama（wrapper + native 生成层）

v2/v3 已彻底落地（LTO、直接 upcall stub、handler 复用、表达式数组+稀疏 map、ASCII 快路径、direct buffer 缓存、Unsafe 拷贝、compile 批量封送、`BITMASK_TO_FLAG` static final、lazy HashMap）。剩余：

- P1 生成的 `hyperscan.java` 137 个 downcall handle 全部裸 `downcallHandle(ADDR, DESC)`，短函数未加 `Linker.Option.critical(true)`（v3 P1 遗留）
- P2 `Scanner.java:274-277,346-353` heap ByteBuffer 扫描每次 `Arena.ofConfined()` 开关 + 段拷贝
- P3 `getNonAsciiBuffer`（325-335）先 `slice` 建视图，`scanRaw` 又 `ofBuffer().asSlice` 再建一个
- P4 非 ASCII 路径映射表仍按 `buffer.capacity()` 分配（ASCII 已走快路径，只剩定长一半收益）
- P5 `isAscii`（387-404）逐字节 early-exit 循环，无法向量化
- P6 Windows 构建同为 Intel 5.4.2 —— **排除，永久不做**
- P7 `SCAN_BUFFER`（297-299）global arena 按历史最大输入永久保留 —— **只记录**
- P8 `Database.compile`（236-239）双 confined arena 可合一
- 缺 stream/vectored 公开 API（生成绑定已含 hs_open_stream 等 75 处引用，wrapper 未暴露）

## 二、执行计划（15 个 commit，每个立即 push）

### A. hyperscan-java（`perf/optimize-wrapper`，6 commits，每步 `mvn test`）

| # | Commit | 要点 |
|---|---|---|
| 1 | `perf(scanner): route byte[] scan through thread-local direct buffer` | `scanRaw` 非空输入走新增 `rawScanBuffer`（ThreadLocal direct buffer，`put(input)`+flip，JDK 内部即 Unsafe 拷贝），再调现有 private `scan(db, ByteBuffer, handler)`；null/空保持原 `BytePointer` 路径，零行为变化 |
| 2 | `feat(scanner): add public zero-copy scan for ByteBuffer` | 新增 `public void scan(Database, ByteBuffer, ByteMatchEventHandler)`：direct 零拷贝直通；heap 拷入 `rawScanBuffer`；不改 position/limit；`ScannerTest` 补 direct/heap 用例（同 commit） |
| 3 | `perf(database): array-indexed expression lookup` | 构造时 maxId ≤ `max(4×count, 1024)` 建 `Expression[] expressionsById` 主索引，否则回退 HashMap；`getExpression` 先数组后 map；map 保留给 equals/hashCode/save |
| 4 | `perf(scanner): skip UTF-8 mapping for ASCII input` | 单趟编码，首个非 ASCII 字符才分配映射表（大小 `w + 3×剩余字符数` 上界，回填恒等段）；`ByteCharMapping` 加共享 identity 实例（`getCharIndex` 恒等、`getMappingSize` 返回 0）；`Scanner` 回调去 `mappingSize>0` 分支恒走 `getCharIndex`（identity 下 byte idx == char idx，空输入仍得 0，语义不变）；`Utf8EncoderTest` 补 ASCII/非 ASCII 用例 |
| 5 | `perf(database): hoist bitmaskToFlag to static final` | 不可变静态常量 |
| 6 | `feat(wrapper): add stream and vectored scan APIs` | 新增 `Stream implements Closeable`（`Scanner.openStream(db)` / `stream.scan(byte[]\|ByteBuffer, handler)` / `close(handler)`，close 可能触发尾部 match 回调故带 handler）+ `Scanner.scanVector(db, byte[][]\|ByteBuffer[], handler)`；复用静态 `matchHandler` + ThreadLocal 回调模式；byte[] 走 commit 1 的复用 buffer；含跨块匹配、vectored 多段、closeStream 尾部回调测试 |

### B. hyperscan-java-native（`main`，1 commit）

| # | Commit | 要点 |
|---|---|---|
| 7 | `perf(preset): mark short no-callback JNI functions @Critical` | InfoMap 注解 `hs_version`/`hs_valid_platform`/`hs_database_size`/`hs_scratch_size`/`hs_stream_size`/`hs_database_info`/`hs_serialized_database_info`/`hs_free_database`/`hs_free_scratch`/`hs_free_compile_error`；排除 `hs_scan*`（upcall）、`hs_close_stream`（可能触发回调）、`hs_compile*`（长调用）；验证生成的 `hyperscan.java` 含 `@Critical` 且编译通过 |

### C. hyperscan-java-panama（`master`，8 commits，每步按该仓 AGENTS.md 验证）

| # | Commit | 要点 |
|---|---|---|
| 8 | `perf(native): critical(true) for short no-callback downcalls` | 生成层（`run-jextract.sh` 后处理）为 #7 同名单函数注入 `Linker.Option.critical(true)`；先 `mvn install -pl native` 重建后验证 |
| 9 | `perf(scanner): avoid per-call Arena for heap ByteBuffer scans` | heap buffer 改走 `SCAN_BUFFER`+`Unsafe.copyMemory`（P2） |
| 10 | `perf(scanner): eliminate double view allocation in non-ASCII String scan` | `getNonAsciiBuffer` 不再返回 slice，直传 buffer+length（P3） |
| 11 | `perf(scanner): size UTF-8 mapping by encoded length` | 非 ASCII 映射表按实际编码长度分配（P4） |
| 12 | `perf(scanner): word-at-a-time ASCII check` | `getLong` + `0x8080..80` 掩码（P5） |
| 13 | `perf(database): merge compile-time arenas` | compile 路径单 confined arena（P8） |
| 14 | `feat(wrapper): add stream and vectored scan APIs` | `HyperscanJni` + `HyperscanJniImpl.java.template` 加 `hsOpenStream`/`hsScanStream`/`hsCloseStream`/`hsResetStream`/`hsScanVector` 转发（生成绑定已含）；wrapper 层 `Stream`/`scanVector` 与 #6 对称；byte[][] 合并进单个 native block；含同类测试 |
| 15 | `docs(knowledge-base): document SCAN_BUFFER growth tradeoff` | P7 记录：以空间换时间策略、无上限原因与应对建议（遵守该仓 knowledge-base 规则） |

## 三、风险与对策

| 风险 | 对策 |
|---|---|
| #4 回调语义调整是行为敏感项 | identity 映射下 byte idx == char idx，空输入仍得 0；`Utf8EncoderTest`/`ScannerTest` 兜底 + 补 ASCII 用例 |
| #8/#14 动 native 生成层 | 本地重建 classifier JAR 验证后才 push |
| `@Critical`/`critical(true)` 误用阻塞 GC 或回调崩溃 | 严格限定无回调、非阻塞短函数名单 |
| 每仓库 push 触发 CI 资源消耗 | 用户已确认每个 commit 立即 push |

## 四、发布（用户决定，不在本批）

- hyperscan-java MINOR bump（API 新增）
- hyperscan-java-native x11
- hyperscan-java-panama rc13
- test 仓 pom bump 后用 `JmhBenchmarkRunner` 量化全部收益

## 五、进度追踪

- [ ] #1 wrapper byte[] scan ThreadLocal buffer
- [ ] #2 wrapper public ByteBuffer scan
- [ ] #3 wrapper expressionsById 数组索引
- [ ] #4 wrapper ASCII 跳过映射分配
- [ ] #5 wrapper bitmaskToFlag static final
- [ ] #6 wrapper stream/vectored API
- [ ] #7 native preset @Critical
- [ ] #8 panama critical(true)
- [ ] #9 panama heap ByteBuffer SCAN_BUFFER
- [ ] #10 panama 非 ASCII 双重视图
- [ ] #11 panama 映射表定长
- [ ] #12 panama isAscii 字长
- [ ] #13 panama compile 单 arena
- [ ] #14 panama stream/vectored API
- [ ] #15 panama knowledge-base P7 记录
