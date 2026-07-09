package com.xenoamess.hyperscan.smoke.wrapper;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualMatch;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualStringMatchHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.xenoamess.hyperscan.smoke.dual.DualArguments.withStrings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ScannerTest {

    private DualApi api;
    private DualScanner scanner;
    private List<DualExpression> expressions;
    private DualDatabase database;

    void setUp(DualApi api) {
        this.api = api;
        scanner = api.createScanner();
        expressions = new ArrayList<>();
        expressions.add(api.createExpression("test", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST, DualExpressionFlag.CASELESS), 0));
        expressions.add(api.createExpression("test1", DualExpressionFlag.SOM_LEFTMOST, 1));
        expressions.add(api.createExpression("test3", DualExpressionFlag.SOM_LEFTMOST, 2));
        expressions.add(api.createExpression("world", DualExpressionFlag.SOM_LEFTMOST, 3));
        expressions.add(api.createExpression("你好", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST, DualExpressionFlag.UTF8), 4));

        try {
            database = api.compileDatabase(expressions);
            api.allocScratch(scanner, database);
        } catch (Exception e) {
            fail("Database compilation or scratch allocation failed", e);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            if (database != null) {
                api.closeDatabase(database);
            }
            if (scanner != null) {
                api.closeScanner(scanner);
            }
        } catch (Exception e) {
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_shouldFindMatches(DualApi api) {
        setUp(api);
        String text = "test test1 test test3";
        List<DualMatch> matches = api.scan(scanner, database, text);

        assertThat(matches).hasSize(6);

        assertThat(matches).anySatisfy(m -> assertMatch(m, 0, 3, 0));
        assertThat(matches).anySatisfy(m -> assertMatch(m, 5, 8, 0));
        assertThat(matches).anySatisfy(m -> assertMatch(m, 5, 9, 1));
        assertThat(matches).anySatisfy(m -> assertMatch(m, 11, 14, 0));
        assertThat(matches).anySatisfy(m -> assertMatch(m, 16, 19, 0));
        assertThat(matches).anySatisfy(m -> assertMatch(m, 16, 20, 2));
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_utf8_shouldFindMatch(DualApi api) {
        setUp(api);
        String text = "Say 你好 world";
        List<DualExpression> utfExpressions = Arrays.asList(expressions.get(3), expressions.get(4));
        try (DualDatabase utfDb = api.compileDatabase(utfExpressions)) {
            List<DualMatch> matches = api.scan(scanner, utfDb, text);

            assertThat(matches).hasSize(2);

            assertThat(matches).filteredOn(m -> m.id() == 4)
                    .hasSize(1)
                    .first()
                    .satisfies(m -> assertMatch(m, 4, 5, 4));

            assertThat(matches).filteredOn(m -> m.id() == 3)
                    .hasSize(1)
                    .first()
                    .satisfies(m -> assertMatch(m, 7, 11, 3));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_noMatches_shouldReturnEmptyList(DualApi api) {
        setUp(api);
        String text = "This text contains none of the patterns.";
        List<DualMatch> matches = api.scan(scanner, database, text);
        assertThat(matches).isEmpty();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_emptyInput_shouldReturnEmptyList(DualApi api) {
        setUp(api);
        String text = "";
        List<DualMatch> matches = api.scan(scanner, database, text);
        assertThat(matches).isEmpty();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanStringWithHandler_shouldInvokeHandler(DualApi api) {
        setUp(api);
        String text = "test test1 test test3";
        AtomicInteger matchCount = new AtomicInteger(0);
        final List<String> matchedExprs = new ArrayList<>();

        DualStringMatchHandler handler = (expression, from, to) -> {
            matchCount.incrementAndGet();
            matchedExprs.add(expression.pattern());
            return true;
        };

        api.scan(scanner, database, text, handler);

        assertThat(matchCount.get()).isEqualTo(6);
        assertThat(matchedExprs).containsExactlyInAnyOrder("test", "test", "test1", "test", "test", "test3");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanStringWithHandler_terminating_shouldStopEarly(DualApi api) {
        setUp(api);
        String text = "Test example789 world";
        AtomicInteger matchCount = new AtomicInteger(0);

        DualStringMatchHandler handler = (expression, from, to) -> {
            matchCount.incrementAndGet();
            return false;
        };

        api.scan(scanner, database, text, handler);

        assertThat(matchCount.get()).isEqualTo(1);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanBytesWithHandler_shouldInvokeHandler(DualApi api) {
        setUp(api);
        byte[] data = "test test1 test test3".getBytes(StandardCharsets.UTF_8);
        AtomicInteger matchCount = new AtomicInteger(0);
        final List<String> matchedExprs = new ArrayList<>();

        DualByteMatchHandler handler = (expression, from, to) -> {
            matchCount.incrementAndGet();
            matchedExprs.add(expression.pattern());
            return true;
        };

        api.scan(scanner, database, data, handler);

        assertThat(matchCount.get()).isEqualTo(6);
        assertThat(matchedExprs).containsExactlyInAnyOrder("test", "test", "test1", "test", "test", "test3");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanBytesWithHandler_utf8_shouldFindMatch(DualApi api) {
        setUp(api);
        byte[] data = "Say 你好 world".getBytes(StandardCharsets.UTF_8);
        AtomicInteger matchCount = new AtomicInteger(0);
        final List<Long> matchPositions = new ArrayList<>();

        List<DualExpression> utfExpressions = Arrays.asList(expressions.get(3), expressions.get(4));
        try (DualDatabase utfDb = api.compileDatabase(utfExpressions)) {
            DualByteMatchHandler handler = (expression, from, to) -> {
                matchCount.incrementAndGet();
                matchPositions.add(from);
                matchPositions.add(to);
                return true;
            };

            api.scan(scanner, utfDb, data, handler);

            assertThat(matchCount.get()).isEqualTo(2);
            assertThat(matchPositions).containsExactlyInAnyOrder(4L, 10L, 11L, 16L);
        }
    }

    @ParameterizedTest
    @MethodSource("hasMatch_shouldReturnTrueIfMatchExistsArgs")
    void hasMatch_shouldReturnTrueIfMatchExists(DualApi api, String input) {
        setUp(api);
        assertTrue(api.hasMatch(scanner, database, input.getBytes(StandardCharsets.UTF_8)));
    }

    static Stream<Arguments> hasMatch_shouldReturnTrueIfMatchExistsArgs() {
        return withStrings("Test", "A test string", "Another TEST");
    }

    @ParameterizedTest
    @MethodSource("hasMatch_shouldReturnFalseIfNoMatchExistsArgs")
    void hasMatch_shouldReturnFalseIfNoMatchExists(DualApi api, String input) {
        setUp(api);
        assertFalse(api.hasMatch(scanner, database, input.getBytes(StandardCharsets.UTF_8)));
    }

    static Stream<Arguments> hasMatch_shouldReturnFalseIfNoMatchExistsArgs() {
        return withStrings("Completely different", "No patterns here", "");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void hasMatch_utf8_shouldReturnTrue(DualApi api) {
        setUp(api);
        assertTrue(api.hasMatch(scanner, database, "你好 world".getBytes(StandardCharsets.UTF_8)));
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scan_afterClose_shouldThrowException(DualApi api) throws IOException {
        setUp(api);
        api.closeScanner(scanner);

        assertThrows(IllegalStateException.class, () -> {
            api.scan(scanner, database, "some text");
        });

        assertThrows(IllegalStateException.class, () -> {
            api.scan(scanner, database, "some text".getBytes(), (expr, from, to) -> true);
        });

        assertThrows(IllegalStateException.class, () -> {
            api.hasMatch(scanner, database, "some text".getBytes());
        });
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void allocScratch_afterClose_shouldThrowException(DualApi api) {
        setUp(api);
        api.closeScanner(scanner);
        assertThrows(IllegalStateException.class, () -> {
            api.allocScratch(scanner, database);
        });
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void doubleClose_shouldBeSafe(DualApi api) {
        setUp(api);
        api.closeScanner(scanner);
        api.closeScanner(scanner);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scan_recursiveCall_shouldThrowException(DualApi api) {
        setUp(api);
        String text = "test world";
        AtomicBoolean insideScan = new AtomicBoolean(false);

        DualStringMatchHandler handler = (expression, from, to) -> {
            if (insideScan.get()) {
                fail("Recursive call detected unexpectedly.");
            }
            insideScan.set(true);
            try {
                assertThrows(IllegalStateException.class, () -> {
                    api.scan(scanner, database, "another test");
                }, "Recursive scanning should be disallowed.");
            } finally {
                insideScan.set(false);
            }
            return true;
        };

        api.scan(scanner, database, text, handler);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_nonSOM_shouldReportStartZero(DualApi api) throws Exception {
        DualExpression nonSomExpr = api.createExpression("^start", EnumSet.noneOf(DualExpressionFlag.class), 5);
        DualDatabase nonSomDb = null;
        DualScanner nonSomScanner = api.createScanner();
        try {
            nonSomDb = api.compileDatabase(Collections.singletonList(nonSomExpr));
            api.allocScratch(nonSomScanner, nonSomDb);
            String text = "start middle end";

            List<DualMatch> matches = api.scan(nonSomScanner, nonSomDb, text);

            assertThat(matches).hasSize(1);
            assertThat(matches.get(0).start()).isEqualTo(0);
            assertThat(matches.get(0).end()).isEqualTo(4);
            assertThat(matches.get(0).id()).isEqualTo(5);
        } finally {
            if (nonSomDb != null) api.closeDatabase(nonSomDb);
            api.closeScanner(nonSomScanner);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void getVersion_shouldReturnString(DualApi api) {
        String version = api.getVersion();
        assertThat(version).isNotNull().isNotEmpty();
        assertThat(version).matches("\\d+\\.\\d+\\.\\d+.*");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void getIsValidPlatform_shouldReturnBoolean(DualApi api) {
        assertDoesNotThrow(() -> {
            String platform = api.getPlatform();
            assertThat(platform).isNotBlank();
        });
    }

    private void assertMatch(DualMatch m, long start, long end, int expressionIndex) {
        assertEquals(start, m.start(), "Start position mismatch");
        assertEquals(end, m.end(), "End position mismatch (inclusive)");
        assertEquals(expressions.get(expressionIndex), m.matchedExpression(), "Expression mismatch");
    }

    private void assertMatchById(DualMatch m, long start, long end, int expressionId) {
        assertEquals(start, m.start(), "Start position mismatch");
        assertEquals(end, m.end(), "End position mismatch (inclusive)");
        assertEquals(expressionId, m.id(), "Expression ID mismatch");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_singleExpression_shouldFindMatches(DualApi api) {
        setUp(api);
        DualExpression expression = api.createExpression("Te?st", EnumSet.of(DualExpressionFlag.CASELESS, DualExpressionFlag.SOM_LEFTMOST), 100);
        try (DualDatabase db = api.compileDatabase(expression)) {
            api.allocScratch(scanner, db);

            String text = "Dies ist ein Test tst.";
            List<DualMatch> matches = api.scan(scanner, db, text);

            assertThat(matches).hasSize(2);

            assertThat(matches.get(0)).satisfies(m -> assertMatchById(m, 13, 16, 100));
            assertThat(matches.get(0).matchedString()).isEqualTo("Test");

            assertThat(matches.get(1)).satisfies(m -> assertMatchById(m, 18, 20, 100));
            assertThat(matches.get(1).matchedString()).isEqualTo("tst");
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_emptyPatternAllowsEmptyMatch(DualApi api) {
        setUp(api);
        DualExpression expression = api.createExpression(".*", EnumSet.of(DualExpressionFlag.ALLOWEMPTY), 101);
        try (DualDatabase db = api.compileDatabase(expression)) {
            api.allocScratch(scanner, db);
            String text = "";
            List<DualMatch> matches = api.scan(scanner, db, text);
            assertThat(matches).hasSize(1);
            assertThat(matches.get(0)).satisfies(m -> {
                assertMatchById(m, 0, 0, 101);
                assertThat(m.matchedString()).isEqualTo("");
            });
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_nonEmptyPatternDisallowsEmptyMatch(DualApi api) {
        setUp(api);
        DualExpression expression = api.createExpression(".+", EnumSet.of(DualExpressionFlag.ALLOWEMPTY, DualExpressionFlag.SOM_LEFTMOST), 102);
        try (DualDatabase db = api.compileDatabase(expression)) {
            api.allocScratch(scanner, db);
            String text = "";
            List<DualMatch> matches = api.scan(scanner, db, text);
            assertThat(matches).isEmpty();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_multiExpression_shouldFindMatches(DualApi api) {
        setUp(api);
        List<DualExpression> multiExpressions = new ArrayList<>();
        EnumSet<DualExpressionFlag> flags = EnumSet.of(DualExpressionFlag.CASELESS, DualExpressionFlag.SOM_LEFTMOST);
        multiExpressions.add(api.createExpression("Te?st", flags, 200));
        multiExpressions.add(api.createExpression("ist", flags, 201));

        try (DualDatabase db = api.compileDatabase(multiExpressions)) {
            api.allocScratch(scanner, db);
            String text = "Dies ist ein Test tst.";
            List<DualMatch> matches = api.scan(scanner, db, text);

            assertThat(matches).hasSize(3);

            assertThat(matches).filteredOn(m -> m.id() == 201)
                    .hasSize(1)
                    .first()
                    .satisfies(m -> {
                        assertMatchById(m, 5, 7, 201);
                        assertThat(m.matchedString()).isEqualTo("ist");
                    });

            assertThat(matches).filteredOn(m -> m.id() == 200)
                    .hasSize(2);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_expressionWithId_shouldMatchCorrectId(DualApi api) {
        setUp(api);
        DualExpression expression = api.createExpression("test", DualExpressionFlag.SOM_LEFTMOST, 17);
        try (DualDatabase db = api.compileDatabase(expression)) {
            api.allocScratch(scanner, db);
            String text = "12345 test string";
            List<DualMatch> matches = api.scan(scanner, db, text);
            assertThat(matches).hasSize(1);
            assertThat(matches.get(0)).satisfies(m -> {
                assertMatchById(m, 6, 9, 17);
                assertThat(m.matchedString()).isEqualTo("test");
            });
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_utf8Digits_shouldMatchCorrectStringAndPosition(DualApi api) {
        setUp(api);
        DualExpression expr = api.createExpression("\\d{5}", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST, DualExpressionFlag.UTF8), 300);
        try (DualDatabase db = api.compileDatabase(expr)) {
            api.allocScratch(scanner, db);
            String text = "58744 78524 \uD83D\uDE00The quick brown fox ◌\uD804\uDD00 jumps 06840 over the lazy dog༼؈";

            List<DualMatch> matches = api.scan(scanner, db, text);
            assertThat(matches).hasSize(3);

            assertThat(matches.get(2)).satisfies(m -> {
                assertMatchById(m, 44, 48, 300);
                assertThat(m.matchedString()).isEqualTo("06840");
            });
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_unknownUtf8Character(DualApi api) {
        setUp(api);
        DualExpression expression = api.createExpression("Hallo", EnumSet.of(DualExpressionFlag.UTF8, DualExpressionFlag.SOM_LEFTMOST), 700);
        try (DualDatabase db = api.compileDatabase(expression)) {
            api.allocScratch(scanner, db);
            String text = "\uD83D\uDE00\uFFFDHallo";
            List<DualMatch> matches = api.scan(scanner, db, text);
            assertThat(matches).hasSize(1);
            assertThat(matches.get(0)).satisfies(m -> {
                assertMatchById(m, 3, 7, 700);
                assertThat(m.matchedString()).isEqualTo("Hallo");
            });
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanString_potentiallyInfinitePattern_shouldComplete(DualApi api) {
        setUp(api);
        DualExpression expression = api.createExpression("a|", EnumSet.of(DualExpressionFlag.ALLOWEMPTY), 400);
        try (DualDatabase db = api.compileDatabase(expression)) {
            api.allocScratch(scanner, db);
            String text = "bbbbbbb";
            assertThat(api.hasMatch(scanner, db, text)).isTrue();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void canCreateHighNumberOfScannersWithoutRunningOutOfNativeHandles(DualApi api) {
        for (int i = 0; i < 100_000; i++) {
            try (DualScanner s = api.createScanner();
                 DualDatabase db = api.compileDatabase(api.createExpression("a"))) {
                api.allocScratch(s, db);
                assertFalse(api.scan(s, db, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa").isEmpty());
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanWithOneScannerAndMultipleDatabases(DualApi api) {
        setUp(api);
        DualExpression exprA = api.createExpression("a", DualExpressionFlag.SOM_LEFTMOST, 500);
        DualExpression exprB = api.createExpression("b", DualExpressionFlag.SOM_LEFTMOST, 501);

        try (
                DualDatabase dbA = api.compileDatabase(exprA);
                DualDatabase dbB = api.compileDatabase(exprB)
        ) {
            api.allocScratch(scanner, dbA);
            List<DualMatch> matchesA = api.scan(scanner, dbA, "a");
            assertThat(matchesA).hasSize(1);
            assertThat(matchesA.get(0)).satisfies(m -> assertMatchById(m, 0, 0, 500));

            api.allocScratch(scanner, dbB);
            List<DualMatch> matchesB = api.scan(scanner, dbB, "b");
            assertThat(matchesB).hasSize(1);
            assertThat(matchesB.get(0)).satisfies(m -> assertMatchById(m, 0, 0, 501));
        }
    }
}
