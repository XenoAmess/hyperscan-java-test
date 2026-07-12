package com.xenoamess.hyperscan.smoke.benchmarks.jmh;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualMatch;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@State(Scope.Thread)
public class CrossPlatformFixedWorkloadBenchmark {

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
            expressions = buildCrossPlatformExpressions(api, 500);
            input = buildCrossPlatformInput(20_000, 50);
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
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void scan(BenchmarkState state, Blackhole blackhole) {
        List<DualMatch> matches = state.api.scan(state.scanner, state.database, state.input);
        blackhole.consume(matches);
    }

    private static List<DualExpression> buildCrossPlatformExpressions(DualApi api, int count) {
        List<DualExpression> expressions = new ArrayList<>(count);
        expressions.add(api.createExpression("[0-9]{3}-[0-9]{2}-[0-9]{4}", DualExpressionFlag.SOM_LEFTMOST, 0));
        expressions.add(api.createExpression("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", DualExpressionFlag.SOM_LEFTMOST, 1));
        expressions.add(api.createExpression("https?://[^\\s]+", DualExpressionFlag.SOM_LEFTMOST, 2));
        expressions.add(api.createExpression("\\bERROR\\b", DualExpressionFlag.SOM_LEFTMOST, 3));
        expressions.add(api.createExpression("\\bWARNING\\b", DualExpressionFlag.SOM_LEFTMOST, 4));
        Random random = new Random(2027);
        for (int i = 5; i < count; i++) {
            String token = String.format("%08x", random.nextInt());
            expressions.add(api.createExpression(Pattern.quote("TOKEN_" + token), DualExpressionFlag.SOM_LEFTMOST, i));
        }
        return expressions;
    }

    private static String buildCrossPlatformInput(int size, int seedCount) {
        Random random = new Random(2026);
        Random tokenRandom = new Random(2027);
        StringBuilder sb = new StringBuilder(size);
        String[] fragments = {
                "Contact support@example.com for help. ",
                "SSN 123-45-6789 is fake. ",
                "Visit https://example.com/page for more. ",
                "ERROR: disk full. ",
                "WARNING: high latency. ",
        };
        while (sb.length() < size) {
            if (random.nextInt(10) == 0 && seedCount > 0) {
                sb.append("TOKEN_").append(String.format("%08x", tokenRandom.nextInt())).append(' ');
                seedCount--;
            } else {
                sb.append(fragments[random.nextInt(fragments.length)]);
            }
        }
        return sb.toString();
    }
}
