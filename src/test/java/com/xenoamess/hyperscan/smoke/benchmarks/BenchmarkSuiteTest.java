package com.xenoamess.hyperscan.smoke.benchmarks;

import com.xenoamess.hyperscan.smoke.BenchmarkRecorder;
import com.xenoamess.hyperscan.smoke.BenchmarkResult;
import com.xenoamess.hyperscan.smoke.annotation.Benchmark;
import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualMatch;
import com.xenoamess.hyperscan.smoke.dual.DualMode;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
import com.xenoamess.hyperscan.smoke.vectorscan.BehaviourTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@Benchmark
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BenchmarkSuiteTest {

    private static final String BENCHMARK_PLATFORM = System.getProperty("benchmark.platform", "unknown");
    private static final String RUNNER_OS = System.getProperty("os.name", "");
    private static final String RUNNER_ARCH = System.getProperty("os.arch", "");

    private final Map<DualImplementation, List<BenchmarkResult>> resultsByImplementation = new EnumMap<>(DualImplementation.class);
    private final Map<DualImplementation, String> nativeVersions = new EnumMap<>(DualImplementation.class);

    @BeforeAll
    void warmUp() throws Exception {
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            List<DualExpression> expressions = buildCrossPlatformExpressions(api, 500);
            String input = buildCrossPlatformInput(20_000, 50);
            try (DualDatabase database = api.compileDatabase(expressions);
                 DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, database);
                for (int i = 0; i < 20; i++) {
                    api.scan(scanner, database, input);
                }
            }
        }
    }

    @AfterAll
    void writeReports() throws Exception {
        for (DualImplementation impl : DualImplementation.values()) {
            List<BenchmarkResult> results = resultsByImplementation.get(impl);
            if (results == null || results.isEmpty()) {
                continue;
            }
            BenchmarkRecorder recorder = new BenchmarkRecorder(
                    BENCHMARK_PLATFORM,
                    nativeVersions.getOrDefault(impl, "unknown"),
                    System.getenv("GITHUB_SHA"),
                    RUNNER_OS,
                    RUNNER_ARCH,
                    readCpuModel(),
                    readCpuFlags(),
                    impl.toString(),
                    results
            );
            recorder.write();
        }
    }

    @Test
    void benchmarkCompileSmallSet() throws Exception {
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            List<DualExpression> expressions = Arrays.asList(
                    api.createExpression("password", DualExpressionFlag.CASELESS, 1),
                    api.createExpression("[0-9]{4,16}", DualExpressionFlag.SOM_LEFTMOST, 2),
                    api.createExpression("https?://[^\\s]+", DualExpressionFlag.SOM_LEFTMOST, 3)
            );
            runAndRecord(impl, api, "compileSmallSet", 100, () -> {
                try (DualDatabase database = api.compileDatabase(expressions)) {
                    assertThat(database.getSize()).isGreaterThan(0);
                }
                return 0L;
            });
        }
    }

    @Test
    void benchmarkCompileLargeSet() throws Exception {
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            List<DualExpression> expressions = generateLiteralExpressions(api, 1000);
            runAndRecord(impl, api, "compileLargeSet", 20, () -> {
                try (DualDatabase database = api.compileDatabase(expressions)) {
                    assertThat(database.getSize()).isGreaterThan(0);
                }
                return 0L;
            });
        }
    }

    @Test
    void benchmarkScanShortText() throws Exception {
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            List<DualExpression> expressions = Arrays.asList(
                    api.createExpression("password", DualExpressionFlag.CASELESS, 1),
                    api.createExpression("[0-9]{4,16}", DualExpressionFlag.SOM_LEFTMOST, 2),
                    api.createExpression("https?://[^\\s]+", DualExpressionFlag.SOM_LEFTMOST, 3)
            );
            try (DualDatabase database = api.compileDatabase(expressions);
                 DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, database);
                String input = "The password is 1234 and the link is https://example.com/path.";
                runAndRecord(impl, api, "scanShortText", 100_000, () -> {
                    List<DualMatch> matches = api.scan(scanner, database, input);
                    return (long) matches.size();
                });
            }
        }
    }

    @Test
    void benchmarkScanLongText() throws Exception {
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            List<DualExpression> expressions = Arrays.asList(
                    api.createExpression("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", DualExpressionFlag.SOM_LEFTMOST, 1),
                    api.createExpression("[0-9]{3}-[0-9]{2}-[0-9]{4}", DualExpressionFlag.SOM_LEFTMOST, 2),
                    api.createExpression("\\bERROR\\b", DualExpressionFlag.SOM_LEFTMOST, 3),
                    api.createExpression("https?://[^\\s]+", DualExpressionFlag.SOM_LEFTMOST, 4)
            );
            try (DualDatabase database = api.compileDatabase(expressions);
                 DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, database);
                String input = generateLongText(1_000_000);
                runAndRecord(impl, api, "scanLongText", 1_000, () -> {
                    List<DualMatch> matches = api.scan(scanner, database, input);
                    return (long) matches.size();
                });
            }
        }
    }

    @Test
    void benchmarkHasMatchShortText() throws Exception {
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            DualExpression expression = api.createExpression("password", DualExpressionFlag.CASELESS, 1);
            try (DualDatabase database = api.compileDatabase(expression);
                 DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, database);
                String input = "The password is secret.";
                runAndRecord(impl, api, "hasMatchShortText", 100_000, () -> api.hasMatch(scanner, database, input) ? 1L : 0L);
            }
        }
    }

    @Test
    void benchmarkScanManyLiteralPatterns() throws Exception {
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            List<DualExpression> expressions = generateLiteralExpressions(api, 500);
            try (DualDatabase database = api.compileDatabase(expressions);
                 DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, database);
                String input = buildHaystackWithMatches(expressions);
                runAndRecord(impl, api, "scanManyLiteralPatterns", 10_000, () -> {
                    List<DualMatch> matches = api.scan(scanner, database, input);
                    return (long) matches.size();
                });
            }
        }
    }

    @Test
    void benchmarkScanByteBuffer() throws Exception {
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            List<DualExpression> expressions = Arrays.asList(
                    api.createExpression("[0-9]{4,16}", DualExpressionFlag.SOM_LEFTMOST, 1),
                    api.createExpression("[A-Fa-f0-9]{32}", DualExpressionFlag.SOM_LEFTMOST, 2)
            );
            try (DualDatabase database = api.compileDatabase(expressions);
                 DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, database);
                byte[] input = generateLongText(100_000).getBytes(StandardCharsets.UTF_8);
                runAndRecord(impl, api, "scanByteBuffer", 10_000, () -> {
                    long[] matches = new long[1];
                    DualByteMatchHandler handler = (expression, fromByteIdx, toByteIdx) -> {
                        matches[0]++;
                        return true;
                    };
                    api.scan(scanner, database, input, handler);
                    return matches[0];
                });
            }
        }
    }

    @Test
    void benchmarkMixedPatternThroughput() throws Exception {
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            List<DualExpression> expressions = buildMixedExpressions(api, 500);
            String input = buildMixedInput(20_000, 50);
            try (DualDatabase database = api.compileDatabase(expressions);
                 DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, database);
                runAndRecordThroughput(impl, api, "mixedPatternThroughput", 2, 5, database, scanner, expressions.size(), input);
            }
        }
    }

    @Test
    void benchmarkCrossPlatformFixedWorkload() throws Exception {
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            List<DualExpression> expressions = buildCrossPlatformExpressions(api, 500);
            String input = buildCrossPlatformInput(20_000, 50);
            try (DualDatabase database = api.compileDatabase(expressions);
                 DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, database);
                runAndRecordThroughput(impl, api, "ISA granularity benchmark", 2, 5, database, scanner, expressions.size(), input);
            }
        }
    }

    @Test
    void benchmarkFixedWorkloadCounting() throws Exception {
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            List<DualExpression> expressions = buildCrossPlatformExpressions(api, 500);
            String input = buildCrossPlatformInput(20_000, 50);
            try (DualDatabase database = api.compileDatabase(expressions);
                 DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, database);
                int warmupIterations = 2;
                int measuredIterations = 5;

                for (int i = 0; i < warmupIterations; i++) {
                    api.scan(scanner, database, input, (expression, from, to) -> true);
                }

                double[] elapsedMs = new double[measuredIterations];
                double[] throughputMBps = new double[measuredIterations];
                long[] lastCount = new long[1];
                for (int i = 0; i < measuredIterations; i++) {
                    long[] count = new long[1];
                    long start = System.nanoTime();
                    api.scan(scanner, database, input, (expression, from, to) -> {
                        count[0]++;
                        return true;
                    });
                    long elapsed = System.nanoTime() - start;
                    elapsedMs[i] = elapsed / 1_000_000.0;
                    throughputMBps[i] = input.length() * 1_000.0 / elapsed;
                    lastCount[0] = count[0];
                }

                recordResult(impl, api, new BenchmarkResult("ISA fixed workload (counting only)")
                        .metric("patterns", expressions.size())
                        .metric("inputBytes", input.length())
                        .metric("matches", lastCount[0])
                        .metric("iterations", measuredIterations)
                        .metric("elapsedMsAvg", avg(elapsedMs))
                        .metric("elapsedMsMin", min(elapsedMs))
                        .metric("elapsedMsMax", max(elapsedMs))
                        .metric("throughputMBpsAvg", avg(throughputMBps))
                        .metric("throughputMBpsMin", min(throughputMBps))
                        .metric("throughputMBpsMax", max(throughputMBps)));
            }
        }
    }

    private static boolean isLargeBenchmarkEnabled() {
        return Boolean.parseBoolean(System.getProperty("hyperscan.benchmarks.large.enabled", "true"));
    }

    private static String sanitizePattern(String pattern) {
        return pattern.replaceAll("[^A-Za-z0-9]", "_");
    }

    @Test
    void benchmarkScanSeveralGigabytesNoMatch() throws Exception {
        if (!isLargeBenchmarkEnabled()) {
            return;
        }
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            List<DualExpression> expressions = List.of(
                    api.createExpression("hatstand.*teakettle.*badgerbrush", EnumSet.of(DualExpressionFlag.DOTALL), 1)
            );
            try (DualDatabase database = api.compileDatabase(expressions, DualMode.STREAM)) {
                DualStream stream = api.openStream(database);
                try {
                    DualByteMatchHandler handler = (expr, from, to) -> true;
                    byte[] chunk = new byte[1024 * 1024];
                    Arrays.fill(chunk, (byte) 'X');
                    long chunks = 5L * 1024;
                    long start = System.nanoTime();
                    while (chunks-- > 0) {
                        api.scanStream(null, stream, chunk, handler);
                    }
                    api.closeStream(null, stream, handler);
                    long elapsed = System.nanoTime() - start;
                    stream = null;
                    long totalBytes = 5L * 1024 * 1024 * 1024;
                    double throughput = (double) totalBytes * 1_000_000_000.0 / elapsed / 1024.0 / 1024.0;
                    recordResult(impl, api, new BenchmarkResult("scanSeveralGigabytesNoMatch")
                            .metric("inputBytes", totalBytes)
                            .metric("matches", 0)
                            .metric("elapsedMs", elapsed / 1_000_000.0)
                            .metric("throughputMBps", throughput));
                } finally {
                    if (stream != null) {
                        api.closeStream(null, stream, null);
                    }
                }
            }
        }
    }

    @Test
    void benchmarkScanGigabytesStreamingMatch() throws Exception {
        if (!isLargeBenchmarkEnabled()) {
            return;
        }
        byte[] chunk = new byte[1024 * 1024];
        Arrays.fill(chunk, (byte) 'X');
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            for (BehaviourTest.HugeScanCase params : BehaviourTest.GIGABYTE_CASES) {
                byte[] preBlock = params.preBlock().getBytes(StandardCharsets.UTF_8);
                byte[] postBlock = params.postBlock().getBytes(StandardCharsets.UTF_8);
                long[] gigabytes = {1, 2};
                for (long gb : gigabytes) {
                    List<DualExpression> expressions = List.of(
                            api.createExpression(params.pattern(), params.flags(), 1)
                    );
                    try (DualDatabase database = api.compileDatabase(expressions, DualMode.STREAM)) {
                        DualStream stream = api.openStream(database);
                        try {
                            long[] matches = new long[1];
                            DualByteMatchHandler handler = (expr, from, to) -> {
                                matches[0]++;
                                return true;
                            };
                            long start = System.nanoTime();
                            api.scanStream(null, stream, preBlock, handler);
                            long remaining = gb * 1024;
                            while (remaining-- > 0) {
                                api.scanStream(null, stream, chunk, handler);
                            }
                            api.scanStream(null, stream, postBlock, handler);
                            api.closeStream(null, stream, handler);
                            long elapsed = System.nanoTime() - start;
                            stream = null;
                            long totalBytes = preBlock.length + gb * 1024L * 1024L * 1024L + postBlock.length;
                            double throughput = (double) totalBytes * 1_000_000_000.0 / elapsed / 1024.0 / 1024.0;
                            recordResult(impl, api, new BenchmarkResult(
                                    "scanGigabytesStreamingMatch_" + sanitizePattern(params.pattern()) + "_" + gb + "GB")
                                    .metric("inputBytes", totalBytes)
                                    .metric("matches", matches[0])
                                    .metric("elapsedMs", elapsed / 1_000_000.0)
                                    .metric("throughputMBps", throughput));
                        } finally {
                            if (stream != null) {
                                api.closeStream(null, stream, null);
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    void benchmarkScanGigabytesBlockMatch() throws Exception {
        if (!isLargeBenchmarkEnabled()) {
            return;
        }
        for (DualImplementation impl : DualImplementation.values()) {
            DualApi api = impl.createAdapter();
            for (BehaviourTest.HugeScanCase params : BehaviourTest.GIGABYTE_CASES) {
                byte[] preBlock = params.preBlock().getBytes(StandardCharsets.UTF_8);
                byte[] postBlock = params.postBlock().getBytes(StandardCharsets.UTF_8);
                byte[] block = new byte[1024 * 1024];
                Arrays.fill(block, (byte) 'X');
                System.arraycopy(preBlock, 0, block, 0, preBlock.length);
                System.arraycopy(postBlock, 0, block, block.length - postBlock.length, postBlock.length);
                List<DualExpression> expressions = List.of(
                        api.createExpression(params.pattern(), params.flags(), 1)
                );
                try (DualDatabase database = api.compileDatabase(expressions);
                     DualScanner scanner = api.createScanner()) {
                    api.allocScratch(scanner, database);
                    int iterations = 10;
                    long[] matches = new long[1];
                    DualByteMatchHandler handler = (expr, from, to) -> {
                        matches[0]++;
                        return true;
                    };
                    long start = System.nanoTime();
                    for (int i = 0; i < iterations; i++) {
                        api.scan(scanner, database, block, handler);
                    }
                    long elapsed = System.nanoTime() - start;
                    double avgElapsed = (double) elapsed / iterations;
                    double throughput = (double) block.length * 1_000_000_000.0 / avgElapsed / 1024.0 / 1024.0;
                    recordResult(impl, api, new BenchmarkResult("scanGigabytesBlockMatch_" + sanitizePattern(params.pattern()))
                            .metric("inputBytes", block.length)
                            .metric("matches", matches[0])
                            .metric("iterations", iterations)
                            .metric("elapsedMs", avgElapsed / 1_000_000.0)
                            .metric("throughputMBps", throughput));
                }
            }
        }
    }

    private void runAndRecord(DualImplementation impl, DualApi api, String name, int iterations, ThrowingLongSupplier task) throws Exception {
        long totalMatches = 0;
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            totalMatches += task.getAsLong();
        }
        long elapsedNanos = System.nanoTime() - start;
        double elapsedMs = TimeUnit.NANOSECONDS.toMillis(elapsedNanos);
        double opsPerSecond = iterations / (elapsedNanos / 1_000_000_000.0);
        double nsPerOp = (double) elapsedNanos / iterations;

        recordResult(impl, api, new BenchmarkResult(name)
                .metric("iterations", iterations)
                .metric("elapsedMs", elapsedMs)
                .metric("opsPerSecond", opsPerSecond)
                .metric("nsPerOp", nsPerOp)
                .metric("totalMatches", totalMatches));
    }

    private void runAndRecordThroughput(DualImplementation impl, DualApi api, String name, int warmupIterations,
                                          int measuredIterations, DualDatabase database, DualScanner scanner,
                                          int patterns, String input) throws Exception {
        for (int i = 0; i < warmupIterations; i++) {
            api.scan(scanner, database, input);
        }

        double[] elapsedMs = new double[measuredIterations];
        double[] throughputMBps = new double[measuredIterations];
        List<DualMatch> lastMatches = null;
        for (int i = 0; i < measuredIterations; i++) {
            long start = System.nanoTime();
            List<DualMatch> matches = api.scan(scanner, database, input);
            long elapsed = System.nanoTime() - start;
            elapsedMs[i] = elapsed / 1_000_000.0;
            throughputMBps[i] = input.length() * 1_000.0 / elapsed;
            lastMatches = matches;
        }

        recordResult(impl, api, new BenchmarkResult(name)
                .metric("patterns", patterns)
                .metric("inputBytes", input.length())
                .metric("matches", lastMatches == null ? 0 : lastMatches.size())
                .metric("iterations", measuredIterations)
                .metric("elapsedMsAvg", avg(elapsedMs))
                .metric("elapsedMsMin", min(elapsedMs))
                .metric("elapsedMsMax", max(elapsedMs))
                .metric("throughputMBpsAvg", avg(throughputMBps))
                .metric("throughputMBpsMin", min(throughputMBps))
                .metric("throughputMBpsMax", max(throughputMBps)));
    }

    private void recordResult(DualImplementation impl, DualApi api, BenchmarkResult result) {
        resultsByImplementation.computeIfAbsent(impl, k -> new ArrayList<>()).add(result);
        nativeVersions.putIfAbsent(impl, api.getVersion());
    }

    private static String readCpuModel() {
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    Runtime.getRuntime().exec("cat /proc/cpuinfo").getInputStream(), StandardCharsets.UTF_8))) {
                String model = reader.lines()
                        .filter(line -> line.startsWith("model name"))
                        .map(line -> line.substring(line.indexOf(':') + 1).trim())
                        .findFirst()
                        .orElse("");
                if (!model.isEmpty()) {
                    return model;
                }
            } catch (Exception ignored) {
            }
        }
        String env = System.getenv("CPU_MODEL");
        return env == null ? "" : env.trim();
    }

    private static String readCpuFlags() {
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    Runtime.getRuntime().exec("cat /proc/cpuinfo").getInputStream(), StandardCharsets.UTF_8))) {
                String flags = reader.lines()
                        .filter(line -> line.startsWith("flags") || line.startsWith("Features"))
                        .map(line -> line.substring(line.indexOf(':') + 1).trim())
                        .findFirst()
                        .orElse("");
                if (!flags.isEmpty()) {
                    return flags;
                }
            } catch (Exception ignored) {
            }
        }
        String env = System.getenv("CPU_FLAGS");
        return env == null ? "" : env.trim();
    }

    private static List<DualExpression> buildCrossPlatformExpressions(DualApi api, int count) {
        List<DualExpression> expressions = new ArrayList<>(count);
        expressions.add(api.createExpression("[0-9]{3}-[0-9]{2}-[0-9]{4}", DualExpressionFlag.SOM_LEFTMOST, 0));
        expressions.add(api.createExpression("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", DualExpressionFlag.SOM_LEFTMOST, 1));
        expressions.add(api.createExpression("https?://[^\\s]+", DualExpressionFlag.SOM_LEFTMOST, 2));
        expressions.add(api.createExpression("\\bERROR\\b", DualExpressionFlag.SOM_LEFTMOST, 3));
        expressions.add(api.createExpression("\\bWARNING\\b", DualExpressionFlag.SOM_LEFTMOST, 4));
        Random random = new Random(2027);
        for (int i = 5; i < count; i++) {
            String token = String.format("%08x", random.nextInt());
            expressions.add(api.createExpression(Pattern.quote("TOKEN_" + token), DualExpressionFlag.SOM_LEFTMOST, i));
        }
        return expressions;
    }

    private static String buildCrossPlatformInput(int size, int seedCount) {
        Random random = new Random(2026);
        Random tokenRandom = new Random(2027);
        StringBuilder sb = new StringBuilder(size);
        String[] fragments = {
                "Contact support@example.com for help. ",
                "SSN 123-45-6789 is fake. ",
                "Visit https://example.com/page for more. ",
                "ERROR: disk full. ",
                "WARNING: high latency. ",
        };
        while (sb.length() < size) {
            if (random.nextInt(10) == 0 && seedCount > 0) {
                sb.append("TOKEN_").append(String.format("%08x", tokenRandom.nextInt())).append(" ");
                seedCount--;
            } else {
                sb.append(fragments[random.nextInt(fragments.length)]);
            }
        }
        return sb.toString();
    }

    private static List<DualExpression> buildMixedExpressions(DualApi api, int count) {
        List<DualExpression> expressions = new ArrayList<>(count);
        expressions.add(api.createExpression("[0-9]{3}-[0-9]{2}-[0-9]{4}", DualExpressionFlag.SOM_LEFTMOST, 0));
        expressions.add(api.createExpression("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", DualExpressionFlag.SOM_LEFTMOST, 1));
        expressions.add(api.createExpression("https?://[^\\s]+", DualExpressionFlag.SOM_LEFTMOST, 2));
        expressions.add(api.createExpression("\\bERROR\\b", DualExpressionFlag.SOM_LEFTMOST, 3));
        expressions.add(api.createExpression("\\bWARNING\\b", DualExpressionFlag.SOM_LEFTMOST, 4));
        Random random = new Random(2027);
        for (int i = 5; i < count; i++) {
            String token = String.format("%08x", random.nextInt());
            expressions.add(api.createExpression(Pattern.quote("TOKEN_" + token), DualExpressionFlag.SOM_LEFTMOST, i));
        }
        return expressions;
    }

    private static String buildMixedInput(int size, int seedCount) {
        return buildCrossPlatformInput(size, seedCount);
    }

    private static List<DualExpression> generateLiteralExpressions(DualApi api, int count) {
        List<DualExpression> expressions = new ArrayList<>(count);
        Random random = new Random(2028);
        for (int i = 0; i < count; i++) {
            String literal = "LIT_" + String.format("%08x", random.nextInt());
            expressions.add(api.createExpression(literal, DualExpressionFlag.SOM_LEFTMOST, i));
        }
        return expressions;
    }

    private static String buildHaystackWithMatches(List<DualExpression> expressions) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expressions.size(); i++) {
            if (i % 5 == 0) {
                sb.append(expressions.get(i).pattern()).append(' ');
            } else {
                sb.append("noise_").append(i).append(' ');
            }
        }
        return sb.toString();
    }

    private static String generateLongText(int length) {
        Random random = new Random(12345);
        StringBuilder sb = new StringBuilder(length);
        String[] words = {"hello", "world", "foo", "bar", "1234", "test", "data", "error", "https://example.com/page"};
        while (sb.length() < length) {
            sb.append(words[random.nextInt(words.length)]).append(' ');
            if (random.nextInt(20) == 0) {
                sb.append("user@example.com ");
            }
            if (random.nextInt(50) == 0) {
                sb.append("ERROR: disk full ");
            }
            if (random.nextInt(100) == 0) {
                sb.append("123-45-6789 ");
            }
        }
        return sb.toString();
    }

    private static double avg(double[] values) {
        double sum = 0.0;
        for (double v : values) {
            sum += v;
        }
        return sum / values.length;
    }

    private static double min(double[] values) {
        double min = Double.MAX_VALUE;
        for (double v : values) {
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    private static double max(double[] values) {
        double max = -Double.MAX_VALUE;
        for (double v : values) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    @FunctionalInterface
    private interface ThrowingLongSupplier {
        long getAsLong() throws Exception;
    }
}
