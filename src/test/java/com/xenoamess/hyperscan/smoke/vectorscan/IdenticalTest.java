package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualMatch;
import com.xenoamess.hyperscan.smoke.dual.DualMode;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualScratchResult;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
import com.xenoamess.hyperscan.smoke.dual.JavaCppAdapter;
import com.xenoamess.hyperscan.smoke.dual.PanamaAdapter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class IdenticalTest {

    private static final PatternInfo[] PATTERNS = {
            new PatternInfo("a", EnumSet.noneOf(DualExpressionFlag.class), "a", 1),
            new PatternInfo("a", EnumSet.of(DualExpressionFlag.SINGLEMATCH), "a", 1),
            new PatternInfo("handbasket", EnumSet.noneOf(DualExpressionFlag.class), "__handbasket__", 12),
            new PatternInfo("handbasket", EnumSet.of(DualExpressionFlag.SINGLEMATCH), "__handbasket__", 12),
            new PatternInfo("handbasket", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST), "__handbasket__", 12),
            new PatternInfo("foo.*bar", EnumSet.noneOf(DualExpressionFlag.class), "a foolish embarrassment", 15),
            new PatternInfo("foo.*bar", EnumSet.of(DualExpressionFlag.SINGLEMATCH), "a foolish embarrassment", 15),
            new PatternInfo("foo.*bar", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST), "a foolish embarrassment", 15),
            new PatternInfo("\\bword\\b(..)+\\d{3,7}", EnumSet.noneOf(DualExpressionFlag.class), "    word    012", 15),
            new PatternInfo("\\bword\\b(..)+\\d{3,7}", EnumSet.of(DualExpressionFlag.SINGLEMATCH), "    word    012", 15),
            new PatternInfo("\\bword\\b(..)+\\d{3,7}", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST), "    word    012", 15),
            new PatternInfo("eod\\z", EnumSet.noneOf(DualExpressionFlag.class), "eod", 3),
            new PatternInfo("eod\\z", EnumSet.of(DualExpressionFlag.SINGLEMATCH), "eod", 3),
            new PatternInfo("eod\\z", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST), "eod", 3),
    };

    private static final PatternInfo[] ACCEL_STREAM_SPLIT_PATTERNS = {
            new PatternInfo("[0-9]{1,2}c[a-f]*", EnumSet.noneOf(DualExpressionFlag.class), ".q3q..y1y.cz13czx0c31bc2 1z2bz1_qq2 12yebxz.yd.f.cy__ .ybezd112cz", 0),
            new PatternInfo("[b-d]{1,3}[cd]+\\d*", EnumSet.noneOf(DualExpressionFlag.class), "xf2a2b .3a21xbzz0 eq1qdxayyy20dadaa_ a22qbe..cxycx1 301.bd3", 0),
            new PatternInfo("[a-c]{1,3}[ac][ab]*", EnumSet.noneOf(DualExpressionFlag.class), "f1fzxq.qedb31dxa0d .2xc0 .3yy3_f01yxa01a3c ya1e. az.qdaa", 0),
            new PatternInfo("[ac]{1,3}[cd]+", EnumSet.noneOf(DualExpressionFlag.class), "f2z0c_xxzd23cabe_bdczd0xqeb0fcc", 0),
    };

    private static final DualMode[] MODES = { DualMode.BLOCK, DualMode.STREAM, DualMode.VECTORED };

    static Stream<Arguments> patternProvider() {
        Stream.Builder<Arguments> builder = Stream.builder();
        for (DualApi api : List.of(new JavaCppAdapter(), new PanamaAdapter())) {
            for (PatternInfo info : PATTERNS) {
                for (DualMode mode : MODES) {
                    builder.add(Arguments.of(api, info, mode));
                }
            }
        }
        return builder.build();
    }

    static Stream<Arguments> accelStreamSplitProvider() {
        Stream.Builder<Arguments> builder = Stream.builder();
        for (DualApi api : List.of(new JavaCppAdapter(), new PanamaAdapter())) {
            for (PatternInfo info : ACCEL_STREAM_SPLIT_PATTERNS) {
                builder.add(Arguments.of(api, info));
            }
        }
        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("patternProvider")
    void identicalExpressions(DualApi api, PatternInfo info, DualMode mode) {
        List<DualExpression> expressions = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            expressions.add(api.createExpression(info.expr(), info.flags(), i));
        }
        DualDatabase db = api.compileDatabase(expressions, mode);
        try {
            DualScanner scanner = api.createScanner();
            try {
                List<DualMatch> matches = scan(api, scanner, db, mode, info.corpus().getBytes(StandardCharsets.UTF_8));
                assertThat(matches).hasSize(expressions.size());
                Set<Integer> ids = new HashSet<>();
                for (DualMatch match : matches) {
                    assertThat(match.end()).isEqualTo(info.match());
                    ids.add(match.id());
                }
                assertThat(ids).hasSize(expressions.size());
                assertThat(ids).contains(0);
                assertThat(ids).contains(99);
            } finally {
                api.closeScanner(scanner);
            }
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @MethodSource("accelStreamSplitProvider")
    @Disabled("STREAMING_SPLIT")
    void doubleShuftiStreamingSplitInvariant(DualApi api, PatternInfo info) {
        DualExpression expr = api.createExpression(info.expr(), info.flags(), 0);
        DualDatabase blockDb = api.compileDatabase(expr, DualMode.BLOCK);
        DualDatabase streamDb = api.compileDatabase(expr, DualMode.STREAM);
        DualScanner scanner = api.createScanner();
        try {
            api.allocScratch(scanner, blockDb);
            CollectingHandler oracleHandler = new CollectingHandler();
            api.scan(scanner, blockDb, info.corpus().getBytes(StandardCharsets.UTF_8), oracleHandler);
            Set<Long> oracle = collectEndOffsets(oracleHandler.matches());
            byte[] corpus = info.corpus().getBytes(StandardCharsets.UTF_8);
            for (int k = 1; k < corpus.length; k++) {
                DualStream stream = api.openStream(streamDb);
                try {
                    CollectingHandler handler = new CollectingHandler();
                    api.scanStream(scanner, stream, Arrays.copyOfRange(corpus, 0, k), handler);
                    api.scanStream(scanner, stream, Arrays.copyOfRange(corpus, k, corpus.length), handler);
                    api.closeStream(scanner, stream, handler);
                    assertThat(collectEndOffsets(handler.matches())).isEqualTo(oracle);
                } finally {
                    api.closeStream(scanner, stream, (e, from, to) -> true);
                }
            }
        } finally {
            api.closeScanner(scanner);
            api.closeDatabase(blockDb);
            api.closeDatabase(streamDb);
        }
    }

    private static List<DualMatch> scan(DualApi api, DualScanner scanner, DualDatabase db, DualMode mode, byte[] data) {
        if (mode == DualMode.BLOCK) {
            api.allocScratch(scanner, db);
            CollectingHandler handler = new CollectingHandler();
            api.scan(scanner, db, data, handler);
            return handler.matches();
        } else if (mode == DualMode.STREAM) {
            DualStream stream = api.openStream(db);
            try {
                CollectingHandler handler = new CollectingHandler();
                api.scanStream(scanner, stream, data, handler);
                api.closeStream(scanner, stream, handler);
                return handler.matches();
            } finally {
                api.closeStream(scanner, stream, (e, from, to) -> true);
            }
        } else {
            CollectingHandler handler = new CollectingHandler();
            api.scanVector(scanner, db, new byte[][] { data }, handler);
            return handler.matches();
        }
    }

    private static Set<Long> collectEndOffsets(List<DualMatch> matches) {
        Set<Long> result = new HashSet<>();
        for (DualMatch match : matches) {
            result.add(match.end());
        }
        return result;
    }

    private record PatternInfo(String expr, EnumSet<DualExpressionFlag> flags, String corpus, long match) {
    }

    private static final class CollectingHandler implements DualByteMatchHandler {

        private final List<DualMatch> matches = new ArrayList<>();

        @Override
        public boolean onMatch(DualExpression expr, long from, long to) {
            matches.add(new DualMatch(expr, expr.id(), from, to, null));
            return true;
        }

        List<DualMatch> matches() {
            return matches;
        }
    }
}
