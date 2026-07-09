package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualScratchResult;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
import com.xenoamess.hyperscan.smoke.dual.DualStreamResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StreamOpTest {

    private static final DualByteMatchHandler DUMMY = (expr, from, to) -> true;

    private static final byte[] DATA1 = "barfoobar".getBytes(StandardCharsets.UTF_8);
    private static final byte[] DATA1_WITH_NULL = new byte[DATA1.length + 1];

    static {
        System.arraycopy(DATA1, 0, DATA1_WITH_NULL, 0, DATA1.length);
    }

    private record MatchRecord(int to, int id) {
    }

    private static final class MatchCollector implements DualByteMatchHandler {
        private final List<MatchRecord> matches = new ArrayList<>();
        private boolean halt = false;

        @Override
        public boolean onMatch(DualExpression expression, long from, long to) {
            matches.add(new MatchRecord((int) to, expression.id()));
            return !halt;
        }
    }

    private static DualDatabase compileStream(DualApi api, String pattern) {
        DualCompileResult result = api.compileRaw(pattern, 0, api.modeStream());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        return db;
    }

    private static DualScanner allocScratch(DualApi api, DualDatabase db) {
        DualScratchResult result = api.allocScratchRaw(db);
        assertThat(result.code()).isEqualTo(api.success());
        DualScanner scanner = result.scratch();
        assertThat(scanner).isNotNull();
        return scanner;
    }

    private static DualStream openStream(DualApi api, DualDatabase db) {
        DualStreamResult result = api.openStreamRaw(db);
        assertThat(result.code()).isEqualTo(api.success());
        DualStream stream = result.stream();
        assertThat(stream).isNotNull();
        return stream;
    }

    private static void closeStream(DualApi api, DualStream stream, DualScanner scanner) {
        assertThat(api.closeStreamRaw(stream, scanner, null)).isEqualTo(api.success());
    }

    private static DualStream copyStream(DualApi api, DualStream source) {
        DualStream[] out = new DualStream[1];
        assertThat(api.copyStreamRaw(out, source)).isEqualTo(api.success());
        assertThat(out[0]).isNotNull();
        return out[0];
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void reset1(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        DualStream stream = openStream(api, db);
        try {
            MatchCollector c = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(9, 0));
            c.matches.clear();
            assertThat(api.resetStreamRaw(stream, null, null)).isEqualTo(api.success());
            MatchCollector c2 = new MatchCollector();
            DualByteMatchHandler handler2 = (expr, from, to) -> {
                c2.matches.add(new MatchRecord((int) to + 1000, expr.id()));
                return true;
            };
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, handler2)).isEqualTo(api.success());
            assertThat(c.matches).isEmpty();
            assertThat(c2.matches).containsExactly(new MatchRecord(1009, 0));
        } finally {
            closeStream(api, stream, scanner);
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void reset2(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        DualStream stream = openStream(api, db);
        try {
            MatchCollector c = new MatchCollector();
            c.halt = true;
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.scanTerminated());
            assertThat(c.matches).containsExactly(new MatchRecord(9, 0));
            c.matches.clear();
            assertThat(api.resetStreamRaw(stream, null, null)).isEqualTo(api.success());
            MatchCollector c2 = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c2)).isEqualTo(api.success());
            assertThat(c.matches).isEmpty();
            assertThat(c2.matches).containsExactly(new MatchRecord(9, 0));
        } finally {
            closeStream(api, stream, scanner);
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void resetMatches(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar$");
        DualScanner scanner = allocScratch(api, db);
        DualStream stream = openStream(api, db);
        try {
            MatchCollector c = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, DATA1, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).isEmpty();
            assertThat(api.resetStreamRaw(stream, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(9, 0));
        } finally {
            closeStream(api, stream, scanner);
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void resetClose(DualApi api) {
        DualDatabase db = compileStream(api, "foo");
        DualScanner scanner = allocScratch(api, db);
        DualStream stream = openStream(api, db);
        try {
            MatchCollector c = new MatchCollector();
            byte[] part1 = "---f".getBytes(StandardCharsets.UTF_8);
            byte[] part2 = "oo--".getBytes(StandardCharsets.UTF_8);
            assertThat(api.scanStreamRaw(stream, part1, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).isEmpty();
            assertThat(api.scanStreamRaw(stream, part2, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(6, 0));
            assertThat(api.resetStreamRaw(stream, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(6, 0));
            assertThat(api.closeStreamRaw(stream, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(6, 0));
        } finally {
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void copy1(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        DualStream stream = openStream(api, db);
        try {
            MatchCollector c = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(9, 0));
            c.matches.clear();
            DualStream stream2 = copyStream(api, stream);
            try {
                assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.success());
                assertThat(c.matches).containsExactly(new MatchRecord(13, 0), new MatchRecord(19, 0));
                c.matches.clear();
                assertThat(api.scanStreamRaw(stream2, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.success());
                assertThat(c.matches).containsExactly(new MatchRecord(13, 0), new MatchRecord(19, 0));
            } finally {
                closeStream(api, stream2, scanner);
            }
        } finally {
            closeStream(api, stream, scanner);
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void copy2(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        DualStream stream = openStream(api, db);
        try {
            MatchCollector c = new MatchCollector();
            c.halt = true;
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.scanTerminated());
            assertThat(c.matches).containsExactly(new MatchRecord(9, 0));
            c.matches.clear();
            DualStream stream2 = copyStream(api, stream);
            try {
                assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.scanTerminated());
                assertThat(c.matches).isEmpty();
                assertThat(api.scanStreamRaw(stream2, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.scanTerminated());
                assertThat(c.matches).isEmpty();
            } finally {
                closeStream(api, stream2, scanner);
            }
        } finally {
            closeStream(api, stream, scanner);
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void copyReset1(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        DualStream stream = openStream(api, db);
        DualStream stream2 = openStream(api, db);
        try {
            MatchCollector c = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(9, 0));
            c.matches.clear();
            assertThat(api.resetAndCopyStreamRaw(stream, stream2, null, null)).isEqualTo(api.success());
            MatchCollector c2 = new MatchCollector();
            DualByteMatchHandler handler2 = (expr, from, to) -> {
                c2.matches.add(new MatchRecord((int) to + 1000, expr.id()));
                return true;
            };
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, handler2)).isEqualTo(api.success());
            assertThat(c.matches).isEmpty();
            assertThat(c2.matches).containsExactly(new MatchRecord(1009, 0));
        } finally {
            closeStream(api, stream, scanner);
            closeStream(api, stream2, scanner);
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void copyReset2(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        DualStream stream = openStream(api, db);
        DualStream stream2 = openStream(api, db);
        try {
            MatchCollector c = new MatchCollector();
            c.halt = true;
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.scanTerminated());
            assertThat(c.matches).containsExactly(new MatchRecord(9, 0));
            c.matches.clear();
            assertThat(api.resetAndCopyStreamRaw(stream, stream2, null, null)).isEqualTo(api.success());
            MatchCollector c2 = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c2)).isEqualTo(api.success());
            assertThat(c.matches).isEmpty();
            assertThat(c2.matches).containsExactly(new MatchRecord(9, 0));
        } finally {
            closeStream(api, stream, scanner);
            closeStream(api, stream2, scanner);
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void copyReset3(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        DualStream stream = openStream(api, db);
        DualStream stream2 = openStream(api, db);
        try {
            MatchCollector c = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(9, 0));
            c.matches.clear();
            assertThat(api.resetAndCopyStreamRaw(stream2, stream, null, null)).isEqualTo(api.success());
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(13, 0), new MatchRecord(19, 0));
            c.matches.clear();
            assertThat(api.scanStreamRaw(stream2, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(13, 0), new MatchRecord(19, 0));
        } finally {
            closeStream(api, stream, scanner);
            closeStream(api, stream2, scanner);
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void copyReset4(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        DualStream stream = openStream(api, db);
        DualStream stream2 = openStream(api, db);
        try {
            MatchCollector c = new MatchCollector();
            c.halt = true;
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.scanTerminated());
            assertThat(c.matches).containsExactly(new MatchRecord(9, 0));
            c.matches.clear();
            assertThat(api.resetAndCopyStreamRaw(stream2, stream, null, null)).isEqualTo(api.success());
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.scanTerminated());
            assertThat(c.matches).isEmpty();
            assertThat(api.scanStreamRaw(stream2, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.scanTerminated());
            assertThat(c.matches).isEmpty();
        } finally {
            closeStream(api, stream, scanner);
            closeStream(api, stream2, scanner);
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void copyReset5(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        DualStream stream = openStream(api, db);
        DualStream stream2 = openStream(api, db);
        try {
            MatchCollector c = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(9, 0));
            c.matches.clear();
            assertThat(api.scanStreamRaw(stream2, new byte[] { 'f', 'o', 'o' }, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).isEmpty();
            assertThat(api.resetAndCopyStreamRaw(stream2, stream, null, null)).isEqualTo(api.success());
            assertThat(api.scanStreamRaw(stream, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(13, 0), new MatchRecord(19, 0));
            c.matches.clear();
            assertThat(api.scanStreamRaw(stream2, DATA1_WITH_NULL, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(13, 0), new MatchRecord(19, 0));
        } finally {
            closeStream(api, stream, scanner);
            closeStream(api, stream2, scanner);
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void copyResetMatches(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar$");
        DualScanner scanner = allocScratch(api, db);
        DualStream stream = openStream(api, db);
        DualStream stream2 = openStream(api, db);
        try {
            MatchCollector c = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, DATA1, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).isEmpty();
            assertThat(api.resetAndCopyStreamRaw(stream, stream2, scanner, c)).isEqualTo(api.success());
            assertThat(c.matches).containsExactly(new MatchRecord(9, 0));
        } finally {
            closeStream(api, stream, scanner);
            closeStream(api, stream2, scanner);
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }
}
