package com.xenoamess.hyperscan.smoke.benchmarks.jmh;

import com.xenoamess.hyperscan.smoke.BenchmarkResult;
import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualStringMatchHandler;
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
import org.openjdk.jmh.results.RunResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@State(Scope.Thread)
public class FixedWorkloadCountingBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        public DualApi api;
        public DualDatabase database;
        public DualScanner scanner;
        public String input;
        public List<DualExpression> expressions;
        public long[] matchCounter;
        public DualStringMatchHandler handler;
        public long matches;

        @Setup(Level.Trial)
        public void setUp() {
            String impl = System.getProperty("hyperscan.benchmark.implementation", "JAVACPP");
            api = DualImplementation.valueOf(impl).createAdapter();
            expressions = BenchmarkData.buildCrossPlatformExpressions(api, 500);
            input = BenchmarkData.buildCrossPlatformInput(20_000, 50);
            database = api.compileDatabase(expressions);
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            matchCounter = new long[1];
            handler = (expression, from, to) -> {
                matchCounter[0]++;
                return true;
            };
            api.scan(scanner, database, input, handler);
            matches = matchCounter[0];
            matchCounter[0] = 0;
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
    public void scan(BenchmarkState state) {
        state.matchCounter[0] = 0;
        state.api.scan(state.scanner, state.database, state.input, state.handler);
    }

    public static BenchmarkResult toBenchmarkResult(RunResult runResult) {
        BenchmarkState state = new BenchmarkState();
        state.setUp();
        BenchmarkResult result = BenchmarkResultConverter.averageTimeThroughput(
                "ISA fixed workload (counting only)", runResult.getPrimaryResult(), state.input, state.expressions, (int) state.matches);
        state.tearDown();
        return result;
    }
}
