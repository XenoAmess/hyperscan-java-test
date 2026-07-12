package com.xenoamess.hyperscan.smoke.benchmarks.jmh;

import com.xenoamess.hyperscan.smoke.BenchmarkResult;
import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualMatch;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.RunResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@State(Scope.Thread)
public class MixedPatternThroughputBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        public DualApi api;
        public DualDatabase database;
        public DualScanner scanner;
        public String input;
        public List<DualExpression> expressions;
        public int matches;

        @Setup(Level.Trial)
        public void setUp() {
            String impl = System.getProperty("hyperscan.benchmark.implementation", "JAVACPP");
            api = DualImplementation.valueOf(impl).createAdapter();
            expressions = BenchmarkData.buildMixedExpressions(api, 500);
            input = BenchmarkData.buildMixedInput(20_000, 50);
            database = api.compileDatabase(expressions);
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            matches = api.scan(scanner, database, input).size();
        }

        @TearDown(Level.Trial)
        public void tearDown() {
            if (database != null) {
                database.close();
            }
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    @Benchmark
    public void scan(BenchmarkState state, Blackhole blackhole) {
        List<DualMatch> matches = state.api.scan(state.scanner, state.database, state.input);
        blackhole.consume(matches);
    }

    public static BenchmarkResult toBenchmarkResult(RunResult runResult) {
        BenchmarkState state = new BenchmarkState();
        state.setUp();
        BenchmarkResult result = BenchmarkResultConverter.averageTimeThroughput(
                "mixedPatternThroughput", runResult.getPrimaryResult(), state.input, state.expressions, state.matches);
        state.tearDown();
        return result;
    }
}
