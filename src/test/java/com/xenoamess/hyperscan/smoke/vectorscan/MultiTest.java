package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualScratchResult;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MultiTest {

    private record Match(int to, int id) {
    }

    private static class CollectingHandler implements com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler {
        private final List<Match> matches = new ArrayList<>();
        private final boolean halt;

        CollectingHandler(boolean halt) {
            this.halt = halt;
        }

        @Override
        public boolean onMatch(com.xenoamess.hyperscan.smoke.dual.DualExpression expression, long from, long to) {
            matches.add(new Match((int) to, expression.id() != null ? expression.id() : 0));
            return !halt;
        }
    }

    private static DualExpression expr(DualApi api, String pattern, int id, EnumSet<DualExpressionFlag> flags) {
        return api.createExpression(pattern, flags, id);
    }

    private static DualExpression expr(DualApi api, String pattern, EnumSet<DualExpressionFlag> flags) {
        return api.createExpression(pattern, flags, null);
    }

    private static List<Match> scan(DualApi api, DualDatabase db, byte[] data, boolean halt) {
        DualScratchResult scratchResult = api.allocScratchRaw(db);
        assertThat(scratchResult.code()).isEqualTo(api.success());
        DualScanner scanner = scratchResult.scratch();
        try {
            CollectingHandler handler = new CollectingHandler(halt);
            int result = api.scanRaw(scanner, db, data, handler);
            if (halt) {
                assertThat(result).isEqualTo(api.scanTerminated());
            } else {
                assertThat(result).isEqualTo(api.success());
            }
            return handler.matches;
        } finally {
            api.closeScanner(scanner);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(MultiArgumentsSource.class)
    void normCont1(DualApi api) {
        List<DualExpression> patterns = List.of(
                expr(api, "aoo[A-K]", 30, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "bar[L-Z]", 31, EnumSet.noneOf(DualExpressionFlag.class)));
        DualDatabase db = compile(api, patterns);
        List<Match> matches = scan(api, db, "aooAaooAbarZ".getBytes(StandardCharsets.UTF_8), false);
        assertThat(matches).containsExactly(new Match(4, 30), new Match(8, 30), new Match(12, 31));
    }

    @ParameterizedTest
    @ArgumentsSource(MultiArgumentsSource.class)
    void normCont2(DualApi api) {
        List<DualExpression> patterns = List.of(
                expr(api, "aoo[A-K][^\n]{16}", 30, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "bar[L-Z][^\n]{16}", 31, EnumSet.noneOf(DualExpressionFlag.class)));
        DualDatabase db = compile(api, patterns);
        List<Match> matches = scan(api, db, "aooAaooAbarZ                      ".getBytes(StandardCharsets.UTF_8), false);
        assertThat(matches).containsExactly(new Match(20, 30), new Match(24, 30), new Match(28, 31));
    }

    @ParameterizedTest
    @ArgumentsSource(MultiArgumentsSource.class)
    void normHalt1(DualApi api) {
        List<DualExpression> patterns = List.of(
                expr(api, "aoo[A-K]", 30, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "bar[L-Z]", 31, EnumSet.noneOf(DualExpressionFlag.class)));
        DualDatabase db = compile(api, patterns);
        List<Match> matches = scan(api, db, "aooAaooAbarZ".getBytes(StandardCharsets.UTF_8), true);
        assertThat(matches).containsExactly(new Match(4, 30));
    }

    @ParameterizedTest
    @ArgumentsSource(MultiArgumentsSource.class)
    void normHalt2(DualApi api) {
        List<DualExpression> patterns = List.of(
                expr(api, "aoo[A-K][^\n]{16}", 30, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "bar[L-Z][^\n]{16}", 31, EnumSet.noneOf(DualExpressionFlag.class)));
        DualDatabase db = compile(api, patterns);
        List<Match> matches = scan(api, db, "aooAaooAbarZ                      ".getBytes(StandardCharsets.UTF_8), true);
        assertThat(matches).containsExactly(new Match(20, 30));
    }

    @ParameterizedTest
    @ArgumentsSource(MultiArgumentsSource.class)
    void highCont1(DualApi api) {
        List<DualExpression> patterns = List.of(
                expr(api, "aoo[A-K]", 30, EnumSet.of(DualExpressionFlag.SINGLEMATCH)),
                expr(api, "bar[L-Z]", 31, EnumSet.noneOf(DualExpressionFlag.class)));
        DualDatabase db = compile(api, patterns);
        List<Match> matches = scan(api, db, "aooAaooAbarZ".getBytes(StandardCharsets.UTF_8), false);
        assertThat(matches).containsExactly(new Match(4, 30), new Match(12, 31));
    }

    @ParameterizedTest
    @ArgumentsSource(MultiArgumentsSource.class)
    void highCont2(DualApi api) {
        List<DualExpression> patterns = List.of(
                expr(api, "aoo[A-K][^\n]{16}", 30, EnumSet.of(DualExpressionFlag.SINGLEMATCH)),
                expr(api, "bar[L-Z][^\n]{16}", 31, EnumSet.noneOf(DualExpressionFlag.class)));
        DualDatabase db = compile(api, patterns);
        List<Match> matches = scan(api, db, "aooAaooAbarZ                      ".getBytes(StandardCharsets.UTF_8), false);
        assertThat(matches).containsExactly(new Match(20, 30), new Match(28, 31));
    }

    @ParameterizedTest
    @ArgumentsSource(MultiArgumentsSource.class)
    void highHalt1(DualApi api) {
        List<DualExpression> patterns = List.of(
                expr(api, "aoo[A-K]", 30, EnumSet.of(DualExpressionFlag.SINGLEMATCH)),
                expr(api, "bar[L-Z]", 31, EnumSet.noneOf(DualExpressionFlag.class)));
        DualDatabase db = compile(api, patterns);
        List<Match> matches = scan(api, db, "aooAaooAbarZ".getBytes(StandardCharsets.UTF_8), true);
        assertThat(matches).containsExactly(new Match(4, 30));
    }

    @ParameterizedTest
    @ArgumentsSource(MultiArgumentsSource.class)
    void highHalt2(DualApi api) {
        List<DualExpression> patterns = List.of(
                expr(api, "aoo[A-K][^\n]{16}", 30, EnumSet.of(DualExpressionFlag.SINGLEMATCH)),
                expr(api, "bar[L-Z][^\n]{16}", 31, EnumSet.noneOf(DualExpressionFlag.class)));
        DualDatabase db = compile(api, patterns);
        List<Match> matches = scan(api, db, "aooAaooAbarZbarZaooA                      ".getBytes(StandardCharsets.UTF_8), true);
        assertThat(matches).hasSize(1);
        assertThat(matches.get(0)).isIn(new Match(20, 30), new Match(28, 31));
    }

    @ParameterizedTest
    @ArgumentsSource(MultiArgumentsSource.class)
    void ue2395(DualApi api) {
        List<DualExpression> patterns = List.of(
                expr(api, "^.{200}", 1, EnumSet.of(DualExpressionFlag.DOTALL)),
                expr(api, ".{40,}", 2, EnumSet.of(DualExpressionFlag.DOTALL)),
                expr(api, "aaa", 3, EnumSet.of(DualExpressionFlag.DOTALL)));
        DualDatabase db = compile(api, patterns);
        byte[] data = new byte[300];
        Arrays.fill(data, (byte) 'a');
        List<Match> matches = scan(api, db, data, false);
        int seen = 39;
        for (Match m : matches) {
            if (m.id == 2) {
                assertThat(m.to).isEqualTo(seen + 1);
                seen = m.to;
            } else if (m.id == 1) {
                assertThat(m.to).isEqualTo(200);
            }
        }
        assertThat(seen).isEqualTo(300);
    }

    @ParameterizedTest
    @ArgumentsSource(MultiArgumentsSource.class)
    void issue141(DualApi api) {
        List<DualExpression> patterns = List.of(
                expr(api, "/odezhda-dlya-bega/", 0, EnumSet.of(DualExpressionFlag.DOTALL, DualExpressionFlag.SINGLEMATCH)),
                expr(api, "kurtki-i-vetrovki-dlya-bega", 1, EnumSet.of(DualExpressionFlag.DOTALL, DualExpressionFlag.SINGLEMATCH)),
                expr(api, "futbolki-i-mayki-dlya-bega", 2, EnumSet.of(DualExpressionFlag.DOTALL, DualExpressionFlag.SINGLEMATCH)));
        DualDatabase db = compile(api, patterns);
        List<Match> matches = scan(api, db, "/odezhda-dlya-bega/".getBytes(StandardCharsets.UTF_8), false);
        assertThat(matches).containsExactly(new Match(19, 0));
    }

    private static DualDatabase compile(DualApi api, List<DualExpression> patterns) {
        DualCompileResult result = api.compileRaw(patterns, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        return db;
    }

    public static class MultiArgumentsSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(DualImplementation.values())
                    .map(impl -> Arguments.of(impl.createAdapter()));
        }
    }
}
