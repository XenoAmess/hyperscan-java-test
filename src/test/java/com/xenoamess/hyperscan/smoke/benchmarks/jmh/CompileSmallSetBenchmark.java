package com.xenoamess.hyperscan.smoke.benchmarks.jmh;

import com.xenoamess.hyperscan.smoke.BenchmarkResult;
import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
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
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.results.RunResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@Warmup(iterations = 5)
@Measurement(iterations = 100)
public class CompileSmallSetBenchmark {

    @State(Scope.Thread)
    public static class BenchmarkState {
        public DualApi api;
        public List<DualExpression> expressions;

        @Setup(Level.Trial)
        public void setUp() {
            String impl = System.getProperty("hyperscan.benchmark.implementation", "JAVACPP");
            api = DualImplementation.valueOf(impl).createAdapter();
            expressions = List.of(
                    api.createExpression("password", DualExpressionFlag.CASELESS, 1),
                    api.createExpression("[0-9]{4,16}", DualExpressionFlag.SOM_LEFTMOST, 2),
                    api.createExpression("https?://[^\\s]+", DualExpressionFlag.SOM_LEFTMOST, 3)
            );
        }
    }

    @Benchmark
    public void compile(BenchmarkState state) {
        try (DualDatabase database = state.api.compileDatabase(state.expressions)) {
            // database compiled
        }
    }

    public static BenchmarkResult toBenchmarkResult(RunResult runResult) {
        return BenchmarkResultConverter.singleShotCompile("compileSmallSet", runResult.getPrimaryResult());
    }
}
