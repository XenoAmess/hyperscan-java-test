package com.xenoamess.hyperscan.smoke.benchmarks.jmh;

import com.xenoamess.hyperscan.smoke.BenchmarkRecorder;
import com.xenoamess.hyperscan.smoke.BenchmarkResult;
import com.xenoamess.hyperscan.smoke.annotation.Benchmark;
import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        boolean largeEnabled = Boolean.parseBoolean(System.getProperty("hyperscan.benchmarks.large.enabled", "true"));
        if (largeEnabled) {
            jvmArgs.add("-Dhyperscan.benchmarks.large.enabled=true");
        }

        ChainedOptionsBuilder builder = new OptionsBuilder()
                .include("com\\.xenoamess\\.hyperscan\\.smoke\\.benchmarks\\.jmh\\..*")
                .jvmArgsAppend(jvmArgs.toArray(new String[0]));
        if (!largeEnabled) {
            builder.exclude("com\\.xenoamess\\.hyperscan\\.smoke\\.benchmarks\\.jmh\\.large\\..*");
        }
        Options options = builder.build();

        Collection<RunResult> runResults = new Runner(options).run();

        List<BenchmarkResult> benchmarkResults = new ArrayList<>();
        for (RunResult runResult : runResults) {
            String benchmark = runResult.getParams().getBenchmark();
            String className = benchmark.substring(0, benchmark.lastIndexOf('.'));
            Class<?> benchmarkClass = Class.forName(className);
            Method converter = benchmarkClass.getMethod("toBenchmarkResult", RunResult.class);
            BenchmarkResult result = (BenchmarkResult) converter.invoke(null, runResult);
            benchmarkResults.add(result);
        }

        DualImplementation impl = DualImplementation.valueOf(IMPLEMENTATION);
        DualApi api = impl.createAdapter();
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
