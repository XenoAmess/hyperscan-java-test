package com.xenoamess.hyperscan.smoke.benchmarks.jmh;

import com.xenoamess.hyperscan.smoke.BenchmarkRecorder;
import com.xenoamess.hyperscan.smoke.BenchmarkResult;
import com.xenoamess.hyperscan.smoke.annotation.Benchmark;
import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.util.Statistics;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Benchmark
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JmhBenchmarkRunner {

    private static final String IMPLEMENTATION = System.getProperty("hyperscan.benchmark.implementation", "JAVACPP");
    private static final String BENCHMARK_PLATFORM = System.getProperty("benchmark.platform", "unknown");
    private static final String RUNNER_OS = System.getProperty("os.name", "");
    private static final String RUNNER_ARCH = System.getProperty("os.arch", "");

    @Test
    public void runBenchmarks() throws Exception {
        String javacppPlatform = System.getProperty("javacpp.platform", "");
        String panamaPlatform = System.getProperty("com.xenoamess.hyperscan_panama.platform", "");
        List<String> jvmArgs = new ArrayList<>();
        jvmArgs.add("--enable-native-access=ALL-UNNAMED");
        jvmArgs.add("-Dhyperscan.benchmark.implementation=" + IMPLEMENTATION);
        if (!javacppPlatform.isEmpty()) {
            jvmArgs.add("-Djavacpp.platform=" + javacppPlatform);
        }
        if (!panamaPlatform.isEmpty()) {
            jvmArgs.add("-Dcom.xenoamess.hyperscan_panama.platform=" + panamaPlatform);
        }
        if (Boolean.parseBoolean(System.getProperty("hyperscan.benchmarks.large.enabled", "true"))) {
            jvmArgs.add("-Dhyperscan.benchmarks.large.enabled=true");
        }

        Options options = new OptionsBuilder()
                .include(CrossPlatformFixedWorkloadBenchmark.class.getSimpleName())
                .forks(2)
                .warmupIterations(5)
                .warmupTime(TimeValue.seconds(1))
                .measurementIterations(5)
                .measurementTime(TimeValue.seconds(1))
                .timeUnit(TimeUnit.MILLISECONDS)
                .mode(Mode.AverageTime)
                .jvmArgsAppend(jvmArgs.toArray(new String[0]))
                .build();

        Collection<RunResult> runResults = new Runner(options).run();

        CrossPlatformFixedWorkloadBenchmark.BenchmarkState state = new CrossPlatformFixedWorkloadBenchmark.BenchmarkState();
        state.setUp();
        int inputBytes = state.input.length();
        int matches = state.matches;
        int patterns = state.expressions.size();
        DualApi api = state.api;
        state.tearDown();

        List<BenchmarkResult> benchmarkResults = new ArrayList<>();
        for (RunResult runResult : runResults) {
            Result primary = runResult.getPrimaryResult();
            Statistics stats = primary.getStatistics();
            double elapsedMsAvg = stats.getMean();
            double elapsedMsMin = stats.getMin();
            double elapsedMsMax = stats.getMax();
            double throughputAvg = inputBytes * 1000.0 / elapsedMsAvg / 1024.0 / 1024.0;
            double throughputMin = inputBytes * 1000.0 / elapsedMsMax / 1024.0 / 1024.0;
            double throughputMax = inputBytes * 1000.0 / elapsedMsMin / 1024.0 / 1024.0;

            benchmarkResults.add(new BenchmarkResult("ISA granularity benchmark")
                    .metric("patterns", patterns)
                    .metric("inputBytes", inputBytes)
                    .metric("matches", matches)
                    .metric("iterations", stats.getN())
                    .metric("elapsedMsAvg", elapsedMsAvg)
                    .metric("elapsedMsMin", elapsedMsMin)
                    .metric("elapsedMsMax", elapsedMsMax)
                    .metric("throughputMBpsAvg", throughputAvg)
                    .metric("throughputMBpsMin", throughputMin)
                    .metric("throughputMBpsMax", throughputMax));
        }

        DualImplementation impl = DualImplementation.valueOf(IMPLEMENTATION);
        BenchmarkRecorder recorder = new BenchmarkRecorder(
                BENCHMARK_PLATFORM,
                api.getVersion(),
                System.getenv("GITHUB_SHA"),
                RUNNER_OS,
                RUNNER_ARCH,
                readCpuModel(),
                readCpuFlags(),
                impl.toString(),
                benchmarkResults
        );
        recorder.write();
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
}
