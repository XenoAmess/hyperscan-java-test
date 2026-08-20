package com.xenoamess.hyperscan.smoke.benchmarks.jmh;

import com.xenoamess.hyperscan.smoke.BenchmarkResult;
import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@State(Scope.Thread)
public class FixedWorkloadCountingBenchmark {

    private static final long EXPECTED_MATCHES = 2773;

    @State(Scope.Thread)
    public static class BenchmarkState {
        public DualApi api;
        public DualDatabase database;
        public DualScanner scanner;
        public ByteBuffer input;
        public int inputBytes;
        public List<DualExpression> expressions;
        public long[] matchCounter;
        public DualByteMatchHandler handler;

        @Setup(Level.Trial)
        public void setUp() {
            String impl = System.getProperty("hyperscan.benchmark.implementation", "JAVACPP");
            api = DualImplementation.valueOf(impl).createAdapter();
            expressions = BenchmarkData.buildCrossPlatformExpressions(api, 500);
            byte[] bytes = BenchmarkData.buildCrossPlatformInput(20_000, 50)
                    .getBytes(StandardCharsets.UTF_8);
            inputBytes = bytes.length;
            input = ByteBuffer.allocateDirect(inputBytes);
            input.put(bytes).flip();
            database = api.compileDatabase(expressions);
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            matchCounter = new long[1];
            handler = (expression, from, to) -> {
                matchCounter[0]++;
                return true;
            };
            api.scan(scanner, database, input, handler);
            if (matchCounter[0] != EXPECTED_MATCHES) {
                throw new IllegalStateException("Fixed workload match count is invalid: expected "
                        + EXPECTED_MATCHES + ", got " + matchCounter[0]);
            }
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
        if (state.matchCounter[0] != EXPECTED_MATCHES) {
            throw new IllegalStateException("Fixed workload match count changed: expected "
                    + EXPECTED_MATCHES + ", got " + state.matchCounter[0]);
        }
    }

    public static BenchmarkResult toBenchmarkResult(RunResult runResult) {
        BenchmarkState state = new BenchmarkState();
        state.setUp();
        try {
            return BenchmarkResultConverter.averageTimeThroughput(
                    "ISA fixed workload (direct buffer)", runResult.getPrimaryResult(),
                    state.inputBytes, state.expressions.size(), EXPECTED_MATCHES);
        } finally {
            state.tearDown();
        }
    }
}
