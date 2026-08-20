package com.xenoamess.hyperscan.smoke.benchmarks.jmh;

import com.xenoamess.hyperscan.smoke.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.util.Statistics;

public final class BenchmarkResultConverter {

    private BenchmarkResultConverter() {
    }

    public static BenchmarkResult averageTimeThroughput(String name, Result result, long inputBytes,
                                                        int patterns, Long matchesPerOperation) {
        Statistics stats = result.getStatistics();
        double elapsedMsAvg = stats.getMean();
        double elapsedMsMin = stats.getMin();
        double elapsedMsMax = stats.getMax();
        double throughputAvg = inputBytes * 1000.0 / elapsedMsAvg / 1024.0 / 1024.0;
        double throughputMin = inputBytes * 1000.0 / elapsedMsMax / 1024.0 / 1024.0;
        double throughputMax = inputBytes * 1000.0 / elapsedMsMin / 1024.0 / 1024.0;
        BenchmarkResult benchmark = new BenchmarkResult(name)
                .metric("patterns", patterns)
                .metric("inputBytes", inputBytes)
                .metric("measurementSamples", stats.getN())
                .metric("elapsedMsAvg", elapsedMsAvg)
                .metric("elapsedMsMin", elapsedMsMin)
                .metric("elapsedMsMax", elapsedMsMax)
                .metric("scoreError", result.getScoreError())
                .metric("scoreUnit", result.getScoreUnit())
                .metric("throughputMiBpsAvg", throughputAvg)
                .metric("throughputMiBpsMin", throughputMin)
                .metric("throughputMiBpsMax", throughputMax);
        if (matchesPerOperation != null) {
            benchmark.metric("matchesPerOperation", matchesPerOperation);
        }
        return benchmark;
    }

    public static BenchmarkResult singleShotOps(String name, Result result, Long matchesPerOperation) {
        Statistics stats = result.getStatistics();
        double elapsedMsPerOp = stats.getMean();
        double opsPerSecond = 1000.0 / elapsedMsPerOp;
        double nsPerOp = elapsedMsPerOp * 1_000_000.0;
        BenchmarkResult benchmark = new BenchmarkResult(name)
                .metric("measurementSamples", stats.getN())
                .metric("elapsedMsPerOperation", elapsedMsPerOp)
                .metric("opsPerSecond", opsPerSecond)
                .metric("nsPerOp", nsPerOp)
                .metric("scoreError", result.getScoreError())
                .metric("scoreUnit", result.getScoreUnit());
        if (matchesPerOperation != null) {
            benchmark.metric("matchesPerOperation", matchesPerOperation)
                    .metric("measuredMatches", matchesPerOperation * stats.getN());
        }
        return benchmark;
    }

    public static BenchmarkResult singleShotCompile(String name, Result result) {
        return singleShotOps(name, result, null);
    }

    public static BenchmarkResult singleShotLarge(String name, Result result, long inputBytes,
                                                   Long matchesPerOperation,
                                                   int operationsPerInvocation) {
        Statistics stats = result.getStatistics();
        double elapsedMsPerOperation = stats.getMean();
        double throughput = inputBytes * 1000.0 / elapsedMsPerOperation / 1024.0 / 1024.0;
        BenchmarkResult benchmark = new BenchmarkResult(name)
                .metric("inputBytes", inputBytes)
                .metric("measurementSamples", stats.getN())
                .metric("operationsPerInvocation", operationsPerInvocation)
                .metric("elapsedMsPerOperation", elapsedMsPerOperation)
                .metric("scoreError", result.getScoreError())
                .metric("scoreUnit", result.getScoreUnit())
                .metric("throughputMiBps", throughput);
        if (matchesPerOperation != null) {
            benchmark.metric("matchesPerOperation", matchesPerOperation);
        }
        return benchmark;
    }
}
