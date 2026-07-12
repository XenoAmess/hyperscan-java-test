package com.xenoamess.hyperscan.smoke.benchmarks.jmh;

import com.xenoamess.hyperscan.smoke.BenchmarkResult;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.util.Statistics;

import java.util.List;

public final class BenchmarkResultConverter {

    private BenchmarkResultConverter() {
    }

    public static BenchmarkResult averageTimeThroughput(String name, Result result, String input,
                                                        List<DualExpression> expressions, int matches) {
        Statistics stats = result.getStatistics();
        double elapsedMsAvg = stats.getMean();
        double elapsedMsMin = stats.getMin();
        double elapsedMsMax = stats.getMax();
        int inputBytes = input.length();
        double throughputAvg = inputBytes * 1000.0 / elapsedMsAvg / 1024.0 / 1024.0;
        double throughputMin = inputBytes * 1000.0 / elapsedMsMax / 1024.0 / 1024.0;
        double throughputMax = inputBytes * 1000.0 / elapsedMsMin / 1024.0 / 1024.0;
        return new BenchmarkResult(name)
                .metric("patterns", expressions.size())
                .metric("inputBytes", inputBytes)
                .metric("matches", matches)
                .metric("iterations", stats.getN())
                .metric("elapsedMsAvg", elapsedMsAvg)
                .metric("elapsedMsMin", elapsedMsMin)
                .metric("elapsedMsMax", elapsedMsMax)
                .metric("throughputMBpsAvg", throughputAvg)
                .metric("throughputMBpsMin", throughputMin)
                .metric("throughputMBpsMax", throughputMax);
    }

    public static BenchmarkResult singleShotOps(String name, Result result, long totalMatches) {
        Statistics stats = result.getStatistics();
        double elapsedMsPerOp = stats.getMean();
        long iterations = stats.getN();
        double totalElapsedMs = elapsedMsPerOp * iterations;
        double opsPerSecond = 1000.0 / elapsedMsPerOp;
        double nsPerOp = elapsedMsPerOp * 1_000_000.0;
        return new BenchmarkResult(name)
                .metric("iterations", iterations)
                .metric("elapsedMs", totalElapsedMs)
                .metric("opsPerSecond", opsPerSecond)
                .metric("nsPerOp", nsPerOp)
                .metric("totalMatches", totalMatches);
    }

    public static BenchmarkResult singleShotCompile(String name, Result result) {
        return singleShotOps(name, result, 0);
    }

    public static BenchmarkResult singleShotLarge(String name, Result result, long inputBytes,
                                                   long matches, int iterations) {
        Statistics stats = result.getStatistics();
        double elapsedMsTotal = stats.getMean();
        double elapsedMsPerIteration = elapsedMsTotal / iterations;
        double throughput = inputBytes * iterations * 1000.0 / elapsedMsTotal / 1024.0 / 1024.0;
        return new BenchmarkResult(name)
                .metric("inputBytes", inputBytes)
                .metric("matches", matches)
                .metric("iterations", iterations)
                .metric("elapsedMs", elapsedMsPerIteration)
                .metric("throughputMBps", throughput);
    }
}
