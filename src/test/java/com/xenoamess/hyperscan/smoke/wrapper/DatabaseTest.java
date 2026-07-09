package com.xenoamess.hyperscan.smoke.wrapper;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualMatch;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    private List<DualExpression> expressions;
    private Path tempDbFile;

    @BeforeEach
    void setUp() throws IOException {
        expressions = new ArrayList<>();
        expressions.add(new DualExpression("test", EnumSet.of(DualExpressionFlag.CASELESS)));
        expressions.add(new DualExpression("[0-9]+"));
        tempDbFile = Files.createTempFile("hyperscan-db-test-", ".db");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempDbFile);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void compileSingleExpression(DualApi api) {
        DualExpression expression = new DualExpression("test", EnumSet.of(DualExpressionFlag.CASELESS));
        try (DualDatabase db = api.compileDatabase(expression)) {
            assertNotNull(db);
            assertThat(api.getDatabaseSize(db)).isGreaterThan(0);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void compileMultipleExpressions(DualApi api) {
        try (DualDatabase db = api.compileDatabase(expressions)) {
            assertNotNull(db);
            assertThat(api.getDatabaseSize(db)).isGreaterThan(0);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void compileInvalidExpressionShouldThrow(DualApi api) {
        DualExpression invalidExpression = new DualExpression("test(");
        Exception exception = assertThrows(Exception.class, () -> {
            api.compileDatabase(invalidExpression);
        });
        assertThat(exception.getMessage()).contains("Missing close parenthesis");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void compileWithNullExpressionShouldThrow(DualApi api) {
        assertThrows(NullPointerException.class, () -> api.compileDatabase((DualExpression) null));
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void compileWithNullExpressionListShouldThrow(DualApi api) {
        assertThrows(NullPointerException.class, () -> api.compileDatabase((List<DualExpression>) null));
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void compileWithEmptyExpressionListShouldThrow(DualApi api) {
        assertThrows(Exception.class, () -> api.compileDatabase(Collections.emptyList()));
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void databaseClose(DualApi api) {
        DualDatabase db = api.compileDatabase(expressions);
        assertDoesNotThrow(() -> api.closeDatabase(db));
        assertDoesNotThrow(() -> api.closeDatabase(db));
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void compileWithDuplicateExpressionIdsShouldThrow(DualApi api) {
        List<DualExpression> duplicateIdExpressions = Arrays.asList(
                new DualExpression("pattern1", EnumSet.noneOf(DualExpressionFlag.class), 1),
                new DualExpression("pattern2", EnumSet.noneOf(DualExpressionFlag.class), 1)
        );
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            api.compileDatabase(duplicateIdExpressions);
        });
        assertThat(exception.getMessage()).contains("Expression ID must be unique");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void compileWithMixedIdPresenceShouldThrow(DualApi api) {
        List<DualExpression> mixedIds = Arrays.asList(
                new DualExpression("pattern1"),
                new DualExpression("pattern2", EnumSet.noneOf(DualExpressionFlag.class), 1)
        );

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            api.compileDatabase(mixedIds);
        });
        assertThat(exception.getMessage()).contains("You can't mix expressions with and without id's");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void getExpressionById(DualApi api) {
        List<DualExpression> expressionsWithIds = Arrays.asList(
                new DualExpression("abc", EnumSet.noneOf(DualExpressionFlag.class), 10),
                new DualExpression("def", EnumSet.noneOf(DualExpressionFlag.class), 20)
        );
        try (DualDatabase db = api.compileDatabase(expressionsWithIds)) {
            DualExpression expr10 = api.getExpression(db, 10);
            DualExpression expr20 = api.getExpression(db, 20);
            DualExpression exprMissing = api.getExpression(db, 99);

            assertNotNull(expr10);
            assertEquals("abc", expr10.pattern());
            assertEquals(Integer.valueOf(10), expr10.id());

            assertNotNull(expr20);
            assertEquals("def", expr20.pattern());
            assertEquals(Integer.valueOf(20), expr20.id());

            assertNull(exprMissing, "Should return null for non-existent ID");
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void getExpressionByIndexWhenNoIdsProvided(DualApi api) {
        try (DualDatabase db = api.compileDatabase(expressions)) {
            DualExpression expr0 = api.getExpression(db, 0);
            DualExpression expr1 = api.getExpression(db, 1);
            DualExpression exprMissing = api.getExpression(db, 2);

            assertNotNull(expr0);
            assertEquals("test", expr0.pattern());
            assertNull(expr0.id());

            assertNotNull(expr1);
            assertEquals("[0-9]+", expr1.pattern());
            assertNull(expr1.id());

            assertNull(exprMissing, "Should return null for out-of-bounds index");
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void getSizeAfterCloseShouldThrow(DualApi api) {
        DualDatabase db = api.compileDatabase(expressions);
        api.closeDatabase(db);
        assertThrows(IllegalStateException.class, () -> api.getDatabaseSize(db),
                "getSize should throw IllegalStateException after close");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void testSerializationDeserializationRoundtrip(DualApi api) {
        List<DualExpression> expressionsWithIds = Arrays.asList(
                new DualExpression("Word\\d+", EnumSet.of(DualExpressionFlag.CASELESS, DualExpressionFlag.SOM_LEFTMOST), 100),
                new DualExpression("\\d{3}-\\d{2}-\\d{4}", EnumSet.noneOf(DualExpressionFlag.class), 200)
        );
        String inputText = "Test Word1 123-45-6789 Word2 end";

        DualDatabase originalDb = null;
        DualDatabase deserializedDb = null;
        List<DualMatch> expectedMatches;

        try {
            originalDb = api.compileDatabase(expressionsWithIds);
            try (DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, originalDb);
                expectedMatches = api.scan(scanner, originalDb, inputText);
                assertThat(expectedMatches).hasSize(3);
            }

            byte[] serializedBytes = api.serialize(originalDb);
            assertThat(serializedBytes.length).isGreaterThan(0);

            deserializedDb = api.deserialize(serializedBytes);
            assertThat(deserializedDb).isNotNull();

            assertThat(api.getDatabaseSize(deserializedDb)).isEqualTo(api.getDatabaseSize(originalDb));

            DualExpression expr100 = api.getExpression(deserializedDb, 100);
            assertNotNull(expr100);
            assertEquals("Word\\d+", expr100.pattern());
            assertEquals(Integer.valueOf(100), expr100.id());
            assertThat(expr100.flags()).containsExactlyInAnyOrder(DualExpressionFlag.CASELESS, DualExpressionFlag.SOM_LEFTMOST);

            DualExpression expr200 = api.getExpression(deserializedDb, 200);
            assertNotNull(expr200);
            assertEquals("\\d{3}-\\d{2}-\\d{4}", expr200.pattern());
            assertEquals(Integer.valueOf(200), expr200.id());
            assertThat(expr200.flags()).isEmpty();

            try (DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, deserializedDb);
                List<DualMatch> actualMatches = api.scan(scanner, deserializedDb, inputText);
                assertThat(actualMatches).usingRecursiveComparison().isEqualTo(expectedMatches);
            }
        } finally {
            if (originalDb != null) {
                api.closeDatabase(originalDb);
            }
            if (deserializedDb != null) {
                api.closeDatabase(deserializedDb);
            }
        }
    }
}
