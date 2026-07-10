package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualMode;
import com.xenoamess.hyperscan.smoke.dual.DualScratchResult;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
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
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LiteralsTest {

    private static final int SEED = 29785643;

    private static final DualMode[] MODES = {DualMode.BLOCK, DualMode.STREAM, DualMode.VECTORED};
    private static final EnumSet<DualExpressionFlag>[] ALL_FLAGS = new EnumSet[] {
            EnumSet.noneOf(DualExpressionFlag.class),
            EnumSet.of(DualExpressionFlag.SINGLEMATCH),
            EnumSet.of(DualExpressionFlag.SOM_LEFTMOST)
    };
    private static final int[] SIZES = {1, 10, 100};
    private static final int[][] BOUNDS = {{3, 10}, {10, 100}};
    private static final boolean[] ADD_NON_LITERALS = {false, true};

    private record Params(DualMode mode, EnumSet<DualExpressionFlag> allFlags, int size,
                          int minLen, int maxLen, boolean addNonLiteral) {
    }

    private static final class CountingHandler implements DualByteMatchHandler {
        private long count = 0;

        @Override
        public boolean onMatch(com.xenoamess.hyperscan.smoke.dual.DualExpression expression, long from, long to) {
            count++;
            return true;
        }

        public long count() {
            return count;
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LiteralArgumentsSource.class)
    void caseful(DualApi api, Params params) {
        runScenario(api, params, false, false);
    }

    @ParameterizedTest
    @ArgumentsSource(LiteralArgumentsSource.class)
    void caseless(DualApi api, Params params) {
        runScenario(api, params, true, false);
    }

    @ParameterizedTest
    @ArgumentsSource(LiteralArgumentsSource.class)
    void mixedCase(DualApi api, Params params) {
        runScenario(api, params, false, true);
    }

    private void runScenario(DualApi api, Params params, boolean caseless, boolean mixedCase) {
        Random rng = new Random(SEED);
        List<DualExpression> expressions = new ArrayList<>();
        List<byte[]> corpora = new ArrayList<>();
        for (int i = 0; i < params.size(); i++) {
            Literal literal = randomLiteral(rng, params.minLen(), params.maxLen());
            EnumSet<DualExpressionFlag> flags = EnumSet.copyOf(params.allFlags());
            if (caseless) {
                flags.add(DualExpressionFlag.CASELESS);
            } else if (mixedCase && i % 2 != 0) {
                flags.add(DualExpressionFlag.CASELESS);
            }
            expressions.add(api.createExpression(literal.pattern(), flags, i));
            corpora.add(literal.corpus());
        }
        if (params.addNonLiteral()) {
            expressions.add(api.createExpression("hatstand.*teakettle", EnumSet.noneOf(DualExpressionFlag.class), params.size()));
            corpora.add("hatstand teakettle".getBytes(StandardCharsets.UTF_8));
        }

        int modeBits = modeBits(api, params);
        DualCompileResult compileResult = api.compileRaw(expressions, modeBits);
        assertThat(compileResult.code())
                .withFailMessage(() -> String.format("compile failed for %s %s: %s (code %d)",
                        api.getClass().getSimpleName(), params, compileResult.message(),
                        compileResult.code()))
                .isEqualTo(api.success());
        DualDatabase db = compileResult.database();
        try {
            CountingHandler handler = new CountingHandler();
            if (params.mode() == DualMode.BLOCK) {
                DualScratchResult scratchResult = api.allocScratchRaw(db);
                assertThat(scratchResult.code()).isEqualTo(api.success());
                DualScanner rawScanner = scratchResult.scratch();
                try {
                    for (byte[] corpus : corpora) {
                        long before = handler.count();
                        int result = api.scanRaw(rawScanner, db, corpus, handler);
                        assertThat(result).isEqualTo(api.success());
                        assertThat(handler.count()).isGreaterThan(before);
                    }
                } finally {
                    api.closeScanner(rawScanner);
                }
            } else if (params.mode() == DualMode.STREAM) {
                for (byte[] corpus : corpora) {
                    long before = handler.count();
                    DualStream stream = api.openStream(db);
                    try {
                        api.scanStream(null, stream, corpus, handler);
                    } finally {
                        api.closeStream(null, stream, handler);
                    }
                    assertThat(handler.count()).isGreaterThan(before);
                }
            } else {
                for (byte[] corpus : corpora) {
                    long before = handler.count();
                    api.scanVector(null, db, new byte[][] {corpus}, handler);
                    assertThat(handler.count()).isGreaterThan(before);
                }
            }
        } finally {
            api.closeDatabase(db);
        }
    }

    private static int modeBits(DualApi api, Params params) {
        boolean hasSom = params.allFlags().contains(DualExpressionFlag.SOM_LEFTMOST);
        return switch (params.mode()) {
            case BLOCK -> api.modeBlock();
            case STREAM -> api.modeStream() | (hasSom ? api.modeSomHorizonLarge() : 0);
            case VECTORED -> api.modeVectored();
        };
    }

    private static Literal randomLiteral(Random rng, int minLen, int maxLen) {
        int len = minLen + rng.nextInt(maxLen - minLen + 1);
        StringBuilder pattern = new StringBuilder();
        StringBuilder corpus = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = (char) ('a' + rng.nextInt(26));
            pattern.append(String.format("\\x%02x", (int) c));
            corpus.append(c);
        }
        return new Literal(pattern.toString(), corpus.toString().getBytes(StandardCharsets.UTF_8));
    }

    private record Literal(String pattern, byte[] corpus) {
    }

    public static class LiteralArgumentsSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(DualImplementation.values())
                    .flatMap(impl -> Stream.of(MODES)
                            .flatMap(mode -> Stream.of(ALL_FLAGS)
                                    .flatMap(flags -> Arrays.stream(SIZES).boxed()
                                            .flatMap(size -> Arrays.stream(BOUNDS)
                                                    .flatMap(bounds -> IntStream.range(0, ADD_NON_LITERALS.length)
                                                            .mapToObj(i -> ADD_NON_LITERALS[i])
                                                            .map(addNonLiteral -> Arguments.of(
                                                                    impl.createAdapter(),
                                                                    new Params(mode, flags, size, bounds[0], bounds[1], addNonLiteral))))))));
        }
    }
}
