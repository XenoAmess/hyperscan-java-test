package com.xenoamess.hyperscan.smoke.benchmarks.jmh.large;

import com.xenoamess.hyperscan.smoke.BenchmarkResult;
import com.xenoamess.hyperscan.smoke.benchmarks.jmh.BenchmarkData;
import com.xenoamess.hyperscan.smoke.benchmarks.jmh.BenchmarkResultConverter;
import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
public class ScanGigabytesBlockMatchBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        @Param({"0", "1", "2", "3", "4", "5", "6", "7"})
        public int caseIndex;

        public DualApi api;
        public DualDatabase database;
        public DualScanner scanner;
        public byte[] block;
        public long matches;

        @Setup(Level.Trial)
        public void setUp() {
            String impl = System.getProperty("hyperscan.benchmark.implementation", "JAVACPP");
            api = DualImplementation.valueOf(impl).createAdapter();
            BehaviourTest.HugeScanCase params = BehaviourTest.GIGABYTE_CASES.get(caseIndex);
            List<DualExpression> expressions = List.of(
                    api.createExpression(params.pattern(), params.flags(), 1)
            );
            database = api.compileDatabase(expressions);
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            byte[] preBlock = params.preBlock().getBytes(StandardCharsets.UTF_8);
            byte[] postBlock = params.postBlock().getBytes(StandardCharsets.UTF_8);
            block = new byte[1024 * 1024];
            Arrays.fill(block, (byte) 'X');
            System.arraycopy(preBlock, 0, block, 0, preBlock.length);
            System.arraycopy(postBlock, 0, block, block.length - postBlock.length, postBlock.length);
            long[] counter = new long[1];
            DualByteMatchHandler handler = (expr, from, to) -> {
                counter[0]++;
                return true;
            };
            api.scan(scanner, database, block, handler);
            matches = counter[0];
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
    public long scan(BenchmarkState state) {
        long[] counter = new long[1];
        DualByteMatchHandler handler = (expr, from, to) -> {
            counter[0]++;
            return true;
        };
        for (int i = 0; i < 1000; i++) {
            state.api.scan(state.scanner, state.database, state.block, handler);
        }
        return counter[0];
    }

    public static BenchmarkResult toBenchmarkResult(RunResult runResult) {
        BenchmarkState state = new BenchmarkState();
        state.caseIndex = Integer.parseInt(runResult.getParams().getParam("caseIndex"));
        state.setUp();
        BehaviourTest.HugeScanCase params = BehaviourTest.GIGABYTE_CASES.get(state.caseIndex);
        String name = "scanGigabytesBlockMatch_" + BenchmarkData.sanitizePattern(params.pattern());
        BenchmarkResult result = BenchmarkResultConverter.singleShotLarge(
                name, runResult.getPrimaryResult(), state.block.length, state.matches, 1000);
        state.tearDown();
        return result;
    }
}
