# Panama 性能优化执行计划 v3 — 全仓库审计与实施

> 时间：2026-07-14
> 范围：`hyperscan-java-panama` + `hyperscan-java-test`（`hyperscan-java` + `hyperscan-java-native` 已锁定）
> 前序：v2 计划已落地（HandlerContext 复用、expressionById 缓存、direct buffer 缓存）
> 基础：跨 4 仓库性能审计结果

---

## 一、现状

v2 优化对 gigabyte benchmark 的 FFM per-call 开销有了显著改善（Panama 胜率 39.7% → 60.9%）。但审计发现了多个未优化剩余的 FFM 路径以及全新的优化机会。

## 二、审计发现的优化点（按优先级）

### P0：PanamaAdapter scanStream(ByteBuffer) 未使用 directBufferSegment

**文件** `hyperscan-java-test/src/test/java/.../PanamaAdapter.java:601`

v2 添加了 `DirectBufferCache` 和 `directBufferSegment()` 辅助函数，但 `scanStream(ByteBuffer)` 路径仍使用原始 `MemorySegment.ofBuffer(input).asSlice(...)` —— 未受益于缓存。gigabyte streaming benchmark 每次 1MB chunk 仍创建新 MemorySegment 视图。

### P1：匿名 ByteMatchEventHandler → ThreadLocal 复用

**文件** `hyperscan-java-test/src/test/java/.../PanamaAdapter.java:502,514,543`

```java
// 每次 scan 调用分配匿名 handler
s.scanner().scan(db.database(), input, new ByteMatchEventHandler() {
    @Override
    public boolean onMatch(Expression expression, long from, long to) {
        return handler.onMatch(toDualExpression(expression), from, to);
    }
});
```

改为 ThreadLocal 缓存的可复用实例，state 通过 ThreadLocal 传入。block benchmark 每次 1000-iteration 循环少 1000 次 alloc。

### P1：短函数 Linker.Option.critical(true)

**文件** `hyperscan-java-panama/native/src/main/template/HyperscanJniImpl.java.template`

`hs_free_database`、`hs_free_scratch`、`hs_database_size`、`hs_scratch_size`、`hs_valid_platform`、`hs_version`、`hs_serialize_database`、`hs_deserialize_database`、`hs_free_compile_error` —— 这些函数不回调、不阻塞、不大量发内存。可用 `Linker.Option.critical(true)` 降低 ~20% 每调用固定开销。

**绝不用于** `hs_scan`/`hs_scan_stream`（会触发 Java upcall）和 `hs_compile_multi`（长时间、大量分配）。

### P2：Native 构建 LTO

**文件** `hyperscan-java-panama/native/build.sh`

```bash
-DCMAKE_C_FLAGS="-march=$MARCH"
-DCMAKE_CXX_FLAGS="-march=$MARCH"
```
→
```bash
-DCMAKE_C_FLAGS="-march=$MARCH -O3 -flto=auto"
-DCMAKE_CXX_FLAGS="-march=$MARCH -O3 -flto=auto"
```

vectorscan native 引擎吞吐提升 2~5%，对 Panama 和 JavaCPP 同比例受益。

### P3：allocateMatchEventHandler MethodHandle 提升为 static final

**文件** `hyperscan-java-panama/native/src/main/template/HyperscanJniImpl.java.template:200-213`

每次分配 upcall stub 时做 `MethodHandles.lookup().findVirtual()` 反射。`MethodHandle` 应提升为 class-level static final 字段（只在 compile 路径调用一次，影响极微）。

### P3：bitmaskToFlag static final

**文件** `hyperscan-java-panama/wrapper/.../Database.java:438-440`

每次 `load()` 重建 `bitmaskToFlag` HashMap。应改为 static final。

### P3：getNativeDatabaseHandle MethodHandle 缓存

**文件** `hyperscan-java-test/src/test/java/.../PanamaAdapter.java:1884-1892`

每次调用用反射找 `getDatabase()`。应缓存 `MethodHandle`。

## 三、测试补充

### 新增 3 个 ParameterizedTest 在 StreamAndVectorSmokeTest

1. **`streamingScanWithDirectByteBufferFindsMatches`** — 验证 `scanStream(ByteBuffer)` 路径（覆盖 P0 修复）
2. **`blockScanWithDirectByteBufferFindsMatches`** — 验证 `scan(ByteBuffer, handler)` 路径
3. **`blockScanWithSameBufferDifferentPositionFindsCorrectMatches`** — 验证 `directBufferSegment` 缓存正确性（同 buffer 不同 position/limit）

全部用 `@MethodSource("adapters")` 测试 JavaCppAdapter + PanamaAdapter。

## 四、执行流程

1. 写计划到 docs/
2. 加 3 个 functional tests
3. 按 P0 → P1 → P2 → P3 顺序实施优化
4. 每个仓库的功能测试验证
5. Push，观察 CI
6. 若 CI 全绿：bump panama → 5.4.12-rc9 → release workflow → Maven Central → bump test pom → 重跑 benchmark

## 五、风险

| 风险 | 应对 |
|---|---|
| `Linker.Option.critical` 阻塞 GC | 只用于不回调的短函数 |
| `native/build.sh` 改 LTO 后 CI native 编译失败 | 先本地验证，CI 交构建 |
| 测试覆盖不足导致回归 | 新增 3 个测试覆盖 ByteBuffer 路径 |

## 六、参考

- 审计基础：跨 4 仓库性能审计（`hyperscan-java` + `hyperscan-java-native` 锁定）
- 测试文件：`StreamAndVectorSmokeTest.java`、`PanamaAdapter.java`、`HyperscanJniImpl.java.template`、`Database.java`、`Scanner.java`、`build.sh`
