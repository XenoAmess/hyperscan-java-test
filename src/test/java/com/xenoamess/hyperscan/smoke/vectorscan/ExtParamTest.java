package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionExt;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualScratchResult;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ExtParamTest {

    private static final byte[] HATSTAND = "hatstand".getBytes();
    private static final byte[] TEAKETTLE = "teakettle".getBytes();

    private record Match(int to, int id) {
    }

    private static class CollectingHandler implements com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler {
        private final List<Match> matches = new ArrayList<>();

        @Override
        public boolean onMatch(com.xenoamess.hyperscan.smoke.dual.DualExpression expression, long from, long to) {
            matches.add(new Match((int) to, expression.id() != null ? expression.id() : 0));
            return true;
        }
    }

    private static List<Match> scan(DualApi api, DualDatabase db, byte[] data) {
        DualScratchResult scratchResult = api.allocScratchRaw(db);
        assertThat(scratchResult.code()).isEqualTo(api.success());
        DualScanner scanner = scratchResult.scratch();
        try {
            CollectingHandler handler = new CollectingHandler();
            int result = api.scanRaw(scanner, db, data, handler);
            assertThat(result).isEqualTo(api.success());
            return handler.matches;
        } finally {
            api.closeScanner(scanner);
        }
    }

    private static DualDatabase compile(DualApi api, DualExpressionExt ext) {
        DualCompileResult result = api.compileExtRaw(
                "hatstand.*teakettle",
                api.flagsToBits(EnumSet.noneOf(DualExpressionFlag.class)),
                ext,
                api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        return db;
    }

    private static byte[] corpus(int underscoreCount) {
        byte[] data = new byte[8 + underscoreCount + 9];
        System.arraycopy(HATSTAND, 0, data, 0, 8);
        java.util.Arrays.fill(data, 8, 8 + underscoreCount, (byte) '_');
        System.arraycopy(TEAKETTLE, 0, data, 8 + underscoreCount, 9);
        return data;
    }

    private static byte[] corpusWithPrefix(int prefix, int underscoreCount) {
        byte[] data = new byte[prefix + 8 + underscoreCount + 9];
        java.util.Arrays.fill(data, 0, prefix, (byte) '_');
        System.arraycopy(HATSTAND, 0, data, prefix, 8);
        java.util.Arrays.fill(data, prefix + 8, prefix + 8 + underscoreCount, (byte) '_');
        System.arraycopy(TEAKETTLE, 0, data, prefix + 8 + underscoreCount, 9);
        return data;
    }

    @ParameterizedTest
    @ArgumentsSource(ExtParamArgumentsSource.class)
    void largeMinOffset(DualApi api) {
        DualExpressionExt ext = new DualExpressionExt(
                1L, 100000L, -1L, 0L, 0, 0);
        DualDatabase db = compile(api, ext);
        try {
            assertThat(scan(api, db, corpus(80000))).isEmpty();
            assertThat(scan(api, db, corpus(99983))).containsExactly(new Match(100000, 0));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ExtParamArgumentsSource.class)
    void largeExactOffset(DualApi api) {
        DualExpressionExt ext = new DualExpressionExt(
                1L | 2L, 200000L, 200000L, 0L, 0, 0);
        DualDatabase db = compile(api, ext);
        try {
            assertThat(scan(api, db, corpus(199982))).isEmpty();
            assertThat(scan(api, db, corpus(199983))).containsExactly(new Match(200000, 0));
            assertThat(scan(api, db, corpus(199984))).isEmpty();
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ExtParamArgumentsSource.class)
    void largeMinLength(DualApi api) {
        DualExpressionExt ext = new DualExpressionExt(
                4L, 0L, -1L, 100000L, 0, 0);
        DualDatabase db = compile(api, ext);
        try {
            assertThat(scan(api, db, corpusWithPrefix(10000, 80000))).isEmpty();
            assertThat(scan(api, db, corpusWithPrefix(10000, 99983))).containsExactly(new Match(110000, 0));
        } finally {
            api.closeDatabase(db);
        }
    }

    public static class ExtParamArgumentsSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(DualImplementation.values())
                    .map(impl -> Arguments.of(impl.createAdapter()));
        }
    }
}
