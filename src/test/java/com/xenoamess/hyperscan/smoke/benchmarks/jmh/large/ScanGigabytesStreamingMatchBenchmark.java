package com.xenoamess.hyperscan.smoke.benchmarks.jmh.large;

import com.xenoamess.hyperscan.smoke.BenchmarkResult;
import com.xenoamess.hyperscan.smoke.benchmarks.jmh.BenchmarkData;
import com.xenoamess.hyperscan.smoke.benchmarks.jmh.BenchmarkResultConverter;
import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualMode;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
import com.xenoamess.hyperscan.smoke.vectorscan.BehaviourTest;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
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

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
public class ScanGigabytesStreamingMatchBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        @Param({"0", "1", "2", "3", "4", "5", "6", "7"})
        public int caseIndex;

        @Param({"1", "2"})
        public long gigabytes;

        public DualApi api;
        public DualDatabase database;
        public DualStream stream;
        public byte[] preBlock;
        public byte[] postBlock;
        public ByteBuffer chunk;
        public boolean closed;

        @Setup(Level.Iteration)
        public void setUp() {
            String impl = System.getProperty("hyperscan.benchmark.implementation", "JAVACPP");
            api = DualImplementation.valueOf(impl).createAdapter();
            BehaviourTest.HugeScanCase params = BehaviourTest.GIGABYTE_CASES.get(caseIndex);
            List<DualExpression> expressions = List.of(
                    api.createExpression(params.pattern(), params.flags(), 1)
            );
            database = api.compileDatabase(expressions, DualMode.STREAM);
            stream = api.openStream(database);
            preBlock = params.preBlock().getBytes(StandardCharsets.UTF_8);
            postBlock = params.postBlock().getBytes(StandardCharsets.UTF_8);
            chunk = ByteBuffer.allocateDirect(1024 * 1024);
            for (int i = 0; i < chunk.capacity(); i++) {
                chunk.put(i, (byte) 'X');
            }
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
    public long scan(BenchmarkState state) {
        long[] counter = new long[1];
        DualByteMatchHandler handler = (expr, from, to) -> {
            counter[0]++;
            return true;
        };
        state.api.scanStream(null, state.stream, state.preBlock, handler);
        long remaining = state.gigabytes * 1024;
        while (remaining-- > 0) {
            state.api.scanStream(null, state.stream, state.chunk, handler);
        }
        state.api.scanStream(null, state.stream, state.postBlock, handler);
        state.api.closeStream(null, state.stream, handler);
        state.closed = true;
        return counter[0];
    }

    public static BenchmarkResult toBenchmarkResult(RunResult runResult) {
        BenchmarkState state = new BenchmarkState();
        state.caseIndex = Integer.parseInt(runResult.getParams().getParam("caseIndex"));
        state.gigabytes = Long.parseLong(runResult.getParams().getParam("gigabytes"));
        state.setUp();
        BehaviourTest.HugeScanCase params = BehaviourTest.GIGABYTE_CASES.get(state.caseIndex);
        long totalBytes = state.preBlock.length + state.gigabytes * 1024L * 1024L * 1024L + state.postBlock.length;
        String name = "scanGigabytesStreamingMatch_"
                + BenchmarkData.sanitizePattern(params.pattern()) + "_"
                + state.gigabytes + "GB";
        BenchmarkResult result = BenchmarkResultConverter.singleShotLarge(
                name, runResult.getPrimaryResult(), totalBytes, 0, 1);
        state.tearDown();
        return result;
    }
}
