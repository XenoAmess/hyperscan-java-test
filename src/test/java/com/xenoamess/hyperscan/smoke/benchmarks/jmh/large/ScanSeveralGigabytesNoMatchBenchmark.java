package com.xenoamess.hyperscan.smoke.benchmarks.jmh.large;

import com.xenoamess.hyperscan.smoke.BenchmarkResult;
import com.xenoamess.hyperscan.smoke.benchmarks.jmh.BenchmarkResultConverter;
import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualMode;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
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
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
public class ScanSeveralGigabytesNoMatchBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        public DualApi api;
        public DualDatabase database;
        public DualStream stream;
        public ByteBuffer chunk;
        public long chunks;
        public boolean closed;

        @Setup(Level.Iteration)
        public void setUp() {
            String impl = System.getProperty("hyperscan.benchmark.implementation", "JAVACPP");
            api = DualImplementation.valueOf(impl).createAdapter();
            List<DualExpression> expressions = List.of(
                    api.createExpression("hatstand.*teakettle.*badgerbrush", EnumSet.of(DualExpressionFlag.DOTALL), 1)
            );
            database = api.compileDatabase(expressions, DualMode.STREAM);
            stream = api.openStream(database);
            chunk = ByteBuffer.allocateDirect(1024 * 1024);
            for (int i = 0; i < chunk.capacity(); i++) {
                chunk.put(i, (byte) 'X');
            }
            chunks = 5L * 1024;
            closed = false;
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            if (!closed && stream != null) {
                api.closeStream(null, stream, null);
            }
            if (database != null) {
                database.close();
            }
        }
    }

    @Benchmark
    public void scan(BenchmarkState state) {
        DualByteMatchHandler handler = (expr, from, to) -> true;
        long remaining = state.chunks;
        while (remaining-- > 0) {
            state.api.scanStream(null, state.stream, state.chunk, handler);
        }
        state.api.closeStream(null, state.stream, handler);
        state.closed = true;
    }

    public static BenchmarkResult toBenchmarkResult(RunResult runResult) {
        long totalBytes = 5L * 1024 * 1024 * 1024;
        return BenchmarkResultConverter.singleShotLarge(
                "scanSeveralGigabytesNoMatch", runResult.getPrimaryResult(), totalBytes, 0, 1);
    }
}
