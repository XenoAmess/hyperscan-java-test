package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualMode;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
import com.xenoamess.hyperscan.smoke.dual.JavaCppAdapter;
import com.xenoamess.hyperscan.smoke.dual.PanamaAdapter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SingleTest {

    private static final DualByteMatchHandler DUMMY = (expr, from, to) -> true;

    private static final String[] PATTERNS = {
            "foobar",
            "abc[123]def",
            "[pqr]",
            "abc",
            "hatstand.*(teakettle|badgerbrush)"
    };

    @SuppressWarnings("unchecked")
    private static final EnumSet<DualExpressionFlag>[] FLAGS = new EnumSet[] {
            EnumSet.noneOf(DualExpressionFlag.class),
            EnumSet.of(DualExpressionFlag.CASELESS)
    };

    private static final DualMode[] MODES = { DualMode.BLOCK, DualMode.STREAM, DualMode.VECTORED };

    private static final byte[] LARGE_INPUT = new byte[2048];

    static {
        for (int i = 0; i < LARGE_INPUT.length; i++) {
            LARGE_INPUT[i] = (byte) 'X';
        }
    }

    static Stream<Arguments> comboProvider() {
        Stream.Builder<Arguments> builder = Stream.builder();
        for (DualApi api : List.of(new JavaCppAdapter(), new PanamaAdapter())) {
            for (String pattern : PATTERNS) {
                for (EnumSet<DualExpressionFlag> flags : FLAGS) {
                    for (DualMode mode : MODES) {
                        builder.add(Arguments.of(api, pattern, flags, mode));
                    }
                }
            }
        }
        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("comboProvider")
    void compileAndQuery(DualApi api, String pattern, EnumSet<DualExpressionFlag> flags, DualMode mode) {
        DualExpression expr = api.createExpression(pattern, flags, 1);
        DualDatabase db = api.compileDatabase(expr, mode);
        try {
            assertThat(api.getDatabaseSize(db)).isGreaterThan(0L);
            String info = api.getDatabaseInfo(db);
            assertThat(info).isNotNull().startsWith("Version:");
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @MethodSource("comboProvider")
    void allocateScratch(DualApi api, String pattern, EnumSet<DualExpressionFlag> flags, DualMode mode) {
        if (mode != DualMode.BLOCK) {
            return;
        }
        DualExpression expr = api.createExpression(pattern, flags, 1);
        DualDatabase db = api.compileDatabase(expr, mode);
        DualScanner scanner = api.createScanner();
        try {
            api.allocScratch(scanner, db);
            assertThat(api.getScannerSize(scanner)).isGreaterThan(0L);
        } finally {
            api.closeScanner(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @MethodSource("comboProvider")
    void zeroLengthScan(DualApi api, String pattern, EnumSet<DualExpressionFlag> flags, DualMode mode) {
        DualExpression expr = api.createExpression(pattern, flags, 1);
        DualDatabase db = api.compileDatabase(expr, mode);
        try {
            scanData(api, db, mode, new byte[0]);
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @MethodSource("comboProvider")
    void scanIgnoresMatches(DualApi api, String pattern, EnumSet<DualExpressionFlag> flags, DualMode mode) {
        DualExpression expr = api.createExpression(pattern, flags, 1);
        DualDatabase db = api.compileDatabase(expr, mode);
        try {
            scanData(api, db, mode, LARGE_INPUT);
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @MethodSource("comboProvider")
    void serializeAndDeserialize(DualApi api, String pattern, EnumSet<DualExpressionFlag> flags, DualMode mode) {
        DualExpression expr = api.createExpression(pattern, flags, 1);
        DualDatabase db = api.compileDatabase(expr, mode);
        try {
            long originalSize = api.getDatabaseSize(db);
            byte[] serialized = api.serialize(db);
            assertThat(serialized).isNotEmpty();
            DualDatabase db2 = api.deserialize(serialized);
            try {
                assertThat(api.getDatabaseSize(db2)).isEqualTo(originalSize);
                scanData(api, db2, mode, LARGE_INPUT);
            } finally {
                api.closeDatabase(db2);
            }
        } finally {
            api.closeDatabase(db);
        }
    }

    static Stream<Arguments> terminateProvider() {
        List<Arguments> cases = List.of(
                Arguments.of("foobar", "foobarfoobarfoobar"),
                Arguments.of("a", "a a a a a a a a a a a"),
                Arguments.of(".", "zzzzzzzzzzzzaaaaaaaaaaaaa"),
                Arguments.of("a", "   xAaAa"),
                Arguments.of("a|b|c|d", "a b c d a b c d a b c d")
        );
        Stream.Builder<Arguments> builder = Stream.builder();
        for (DualApi api : List.of(new JavaCppAdapter(), new PanamaAdapter())) {
            for (Arguments args : cases) {
                builder.add(Arguments.of(api, args.get()[0], args.get()[1]));
            }
        }
        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("terminateProvider")
    void blockMatchTerminates(DualApi api, String pattern, String corpus) {
        DualExpression expr = api.createExpression(pattern, 1);
        DualDatabase db = api.compileDatabase(expr, DualMode.BLOCK);
        DualScanner scanner = api.createScanner();
        try {
            api.allocScratch(scanner, db);
            CountingHandler handler = new CountingHandler(1);
            api.scan(scanner, db, corpus.getBytes(), handler);
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.closeScanner(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @MethodSource("terminateProvider")
    void streamMatchTerminates(DualApi api, String pattern, String corpus) {
        DualExpression expr = api.createExpression(pattern, 1);
        DualDatabase db = api.compileDatabase(expr, DualMode.STREAM);
        DualScanner scanner = api.createScanner();
        try {
            DualStream stream = api.openStream(db);
            try {
                CountingHandler handler = new CountingHandler(1);
                api.scanStream(scanner, stream, corpus.getBytes(), handler);
                api.closeStream(scanner, stream, handler);
                assertThat(handler.count).isEqualTo(1);
            } finally {
                api.closeStream(scanner, stream, DUMMY);
            }
        } finally {
            api.closeScanner(scanner);
            api.closeDatabase(db);
        }
    }

    @Test
    @Disabled("MMAP_REQUIRED")
    void outOfBoundReadMmap() {
    }

    private static void scanData(DualApi api, DualDatabase db, DualMode mode, byte[] data) {
        DualScanner scanner = api.createScanner();
        try {
            if (mode == DualMode.BLOCK) {
                api.allocScratch(scanner, db);
                api.scan(scanner, db, data, DUMMY);
            } else if (mode == DualMode.STREAM) {
                DualStream stream = api.openStream(db);
                try {
                    api.scanStream(scanner, stream, data, DUMMY);
                } finally {
                    api.closeStream(scanner, stream, DUMMY);
                }
            } else {
                api.scanVector(scanner, db, new byte[][] { data }, DUMMY);
            }
        } finally {
            api.closeScanner(scanner);
        }
    }

    private static final class CountingHandler implements DualByteMatchHandler {
        private final int haltAfter;
        private int count;

        CountingHandler(int haltAfter) {
            this.haltAfter = haltAfter;
        }

        @Override
        public boolean onMatch(com.xenoamess.hyperscan.smoke.dual.DualExpression expr, long from, long to) {
            count++;
            return count < haltAfter;
        }
    }
}
