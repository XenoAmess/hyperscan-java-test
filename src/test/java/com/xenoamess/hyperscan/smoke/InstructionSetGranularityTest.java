package com.xenoamess.hyperscan.smoke;

import com.gliwka.hyperscan.jni.hs_database_t;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import static com.gliwka.hyperscan.jni.hyperscan.HS_FLAG_SOM_LEFTMOST;
import static org.assertj.core.api.Assertions.assertThat;

class InstructionSetGranularityTest extends BaseSmokeTest {

    private static final int WARMUP_ITERATIONS = 2;
    private static final int MEASURED_ITERATIONS = 5;
    private static final int GLOBAL_WARMUP_ITERATIONS = 20;

    @BeforeAll
    static void globalWarmUp() throws Exception {
        String[] patterns = buildMixedPatterns(500);
        int[] ids = new int[patterns.length];
        int[] flags = new int[patterns.length];
        for (int i = 0; i < patterns.length; i++) {
            ids[i] = i;
            flags[i] = HS_FLAG_SOM_LEFTMOST;
        }
        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            String input = buildMixedInput(20_000, 50);
            int matches = 0;
            for (int i = 0; i < GLOBAL_WARMUP_ITERATIONS; i++) {
                matches = HyperscanTestHelper.hsScan(db, input).size();
            }
            System.out.println("Warm-up matches: " + matches);
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }

    @Test
    void benchmarkReport() throws Exception {
        String platform = System.getProperty("org.bytedeco.javacpp.platform");
        System.out.println("=== ISA granularity benchmark on platform: " + platform + " ===");

        String[] patterns = buildMixedPatterns(500);
        int[] ids = new int[patterns.length];
        int[] flags = new int[patterns.length];
        for (int i = 0; i < patterns.length; i++) {
            ids[i] = i;
            flags[i] = HS_FLAG_SOM_LEFTMOST;
        }

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            String input = buildMixedInput(20_000, 50);

            for (int i = 0; i < WARMUP_ITERATIONS; i++) {
                HyperscanTestHelper.hsScan(db, input);
            }

            double[] throughputs = new double[MEASURED_ITERATIONS];
            double[] elapsedMs = new double[MEASURED_ITERATIONS];
            List<HyperscanTestHelper.Match> matches = null;
            for (int i = 0; i < MEASURED_ITERATIONS; i++) {
                long start = System.nanoTime();
                matches = HyperscanTestHelper.hsScan(db, input);
                long elapsed = System.nanoTime() - start;
                elapsedMs[i] = elapsed / 1_000_000.0;
                throughputs[i] = input.length() * 1_000.0 / elapsed;
            }

            double avgElapsedMs = avg(elapsedMs);
            double minElapsedMs = min(elapsedMs);
            double maxElapsedMs = max(elapsedMs);
            double avgThroughput = avg(throughputs);
            double minThroughput = min(throughputs);
            double maxThroughput = max(throughputs);

            System.out.println("Patterns: " + patterns.length);
            System.out.println("Input bytes: " + input.length());
            System.out.println("Matches: " + matches.size());
            System.out.println("Elapsed ms (avg/min/max): " + avgElapsedMs + "/" + minElapsedMs + "/" + maxElapsedMs);
            System.out.println("Throughput MB/s (avg/min/max): " + avgThroughput + "/" + minThroughput + "/" + maxThroughput);

            assertThat(matches).isNotNull();

            BenchmarkResult result = new BenchmarkResult("ISA granularity benchmark")
                    .metric("patterns", patterns.length)
                    .metric("inputBytes", input.length())
                    .metric("matches", matches.size())
                    .metric("iterations", MEASURED_ITERATIONS)
                    .metric("elapsedMsAvg", avgElapsedMs)
                    .metric("elapsedMsMin", minElapsedMs)
                    .metric("elapsedMsMax", maxElapsedMs)
                    .metric("throughputMBpsAvg", avgThroughput)
                    .metric("throughputMBpsMin", minThroughput)
                    .metric("throughputMBpsMax", maxThroughput);

            BenchmarkRecorder recorder = new BenchmarkRecorder(
                    platform,
                    env("NATIVE_VERSION", "unknown"),
                    env("GITHUB_SHA", "unknown"),
                    env("RUNNER_OS", "unknown"),
                    env("RUNNER_ARCH", "unknown"),
                    env("CPU_MODEL", "unknown"),
                    env("CPU_FLAGS", "unknown"),
                    java.util.Collections.singletonList(result)
            );
            recorder.write();
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }

    private static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null ? defaultValue : value;
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

    private static String[] buildMixedPatterns(int count) {
        String[] patterns = new String[count];
        patterns[0] = "[0-9]{3}-[0-9]{2}-[0-9]{4}";
        patterns[1] = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
        patterns[2] = "https?://[^\\s]+";
        patterns[3] = "\\bERROR\\b";
        patterns[4] = "\\bWARNING\\b";
        Random random = new Random(2027);
        for (int i = 5; i < count; i++) {
            String token = String.format("%08x", random.nextInt());
            patterns[i] = Pattern.quote("TOKEN_" + token);
        }
        return patterns;
    }

    private static String buildMixedInput(int size, int seedCount) {
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
}
