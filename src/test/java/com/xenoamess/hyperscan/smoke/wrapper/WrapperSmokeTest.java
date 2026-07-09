package com.xenoamess.hyperscan.smoke.wrapper;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualMatch;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static com.xenoamess.hyperscan.smoke.dual.DualArguments.withStrings;
import static org.assertj.core.api.Assertions.assertThat;

class WrapperSmokeTest {

    private DualScanner scanner;

    @AfterEach
    void tearDown() {
        try {
            if (scanner != null) {
                scanner.close();
            }
        } catch (Exception ignored) {
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void literalPatternMatches(DualApi api) {
        DualExpression expression = api.createExpression("needle", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST), 1);
        try (DualDatabase database = api.compileDatabase(expression)) {
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            List<DualMatch> matches = api.scan(scanner, database, "haystack needle haystack");
            assertThat(matches).hasSize(1);
            assertThat(matches.get(0)).satisfies(m -> {
                assertThat(m.id()).isEqualTo(1);
                assertThat(m.start()).isEqualTo(9);
                assertThat(m.end()).isEqualTo(14);
                assertThat(m.matchedString()).isEqualTo("needle");
            });
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void caselessFlagMatchesDifferentCases(DualApi api) {
        DualExpression expression = api.createExpression(
                "hyperscan",
                EnumSet.of(DualExpressionFlag.CASELESS, DualExpressionFlag.SOM_LEFTMOST),
                10
        );
        try (DualDatabase database = api.compileDatabase(expression)) {
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            List<DualMatch> matches = api.scan(scanner, database, "Hyperscan HYPERSCAN hyperscan");
            assertThat(matches).hasSize(3);
            assertThat(matches).allMatch(m -> m.id() == 10);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void multipleExpressionsReportCorrectIds(DualApi api) {
        List<DualExpression> expressions = Arrays.asList(
                api.createExpression("foo", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST), 1),
                api.createExpression("bar", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST), 2),
                api.createExpression("baz", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST), 3)
        );
        try (DualDatabase database = api.compileDatabase(expressions)) {
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            List<DualMatch> matches = api.scan(scanner, database, "foo bar baz bar");
            assertThat(matches).hasSize(4);

            assertThat(matches.stream().filter(m -> m.id() == 1)).hasSize(1);
            assertThat(matches.stream().filter(m -> m.id() == 2)).hasSize(2);
            assertThat(matches.stream().filter(m -> m.id() == 3)).hasSize(1);
            assertThat(matches).extracting(DualMatch::matchedString).containsExactlyInAnyOrder("foo", "bar", "baz", "bar");
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void utf8PatternMatchesMultibyteCharacters(DualApi api) {
        DualExpression expression = api.createExpression(
                "中文",
                EnumSet.of(DualExpressionFlag.UTF8, DualExpressionFlag.SOM_LEFTMOST),
                100
        );
        try (DualDatabase database = api.compileDatabase(expression)) {
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            List<DualMatch> matches = api.scan(scanner, database, "this is 中文 text");
            assertThat(matches).hasSize(1);
            assertThat(matches.get(0)).satisfies(m -> {
                assertThat(m.id()).isEqualTo(100);
                assertThat(m.start()).isEqualTo(8);
                assertThat(m.end()).isEqualTo(9);
                assertThat(m.matchedString()).isEqualTo("中文");
            });
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void noMatchReturnsEmptyList(DualApi api) {
        DualExpression expression = api.createExpression("nomatch", DualExpressionFlag.SOM_LEFTMOST, 1);
        try (DualDatabase database = api.compileDatabase(expression)) {
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            List<DualMatch> matches = api.scan(scanner, database, "something completely different");
            assertThat(matches).isEmpty();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void emptyInputProducesNoMatchForNonEmptyPattern(DualApi api) {
        DualExpression expression = api.createExpression("abc", DualExpressionFlag.SOM_LEFTMOST, 1);
        try (DualDatabase database = api.compileDatabase(expression)) {
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            List<DualMatch> matches = api.scan(scanner, database, "");
            assertThat(matches).isEmpty();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void allowEmptyPatternMatchesEmptyInput(DualApi api) {
        DualExpression expression = api.createExpression(".*", EnumSet.of(DualExpressionFlag.ALLOWEMPTY), 1);
        try (DualDatabase database = api.compileDatabase(expression)) {
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            List<DualMatch> matches = api.scan(scanner, database, "");
            assertThat(matches).hasSize(1);
            assertThat(matches.get(0).matchedString()).isEmpty();
        }
    }

    @ParameterizedTest
    @MethodSource("hasMatchReturnsTrueWhenPatternExistsArgs")
    void hasMatchReturnsTrueWhenPatternExists(DualApi api, String input) {
        DualExpression expression = api.createExpression(
                "password",
                EnumSet.of(DualExpressionFlag.CASELESS),
                1
        );
        try (DualDatabase database = api.compileDatabase(expression)) {
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            assertThat(api.hasMatch(scanner, database, input)).isTrue();
        }
    }

    static Stream<Arguments> hasMatchReturnsTrueWhenPatternExistsArgs() {
        return withStrings("password=secret", "the password is secret", "PASSWORD=SECRET");
    }

    @ParameterizedTest
    @MethodSource("hasMatchReturnsFalseWhenPatternAbsentArgs")
    void hasMatchReturnsFalseWhenPatternAbsent(DualApi api, String input) {
        DualExpression expression = api.createExpression("password", DualExpressionFlag.CASELESS, 1);
        try (DualDatabase database = api.compileDatabase(expression)) {
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            assertThat(api.hasMatch(scanner, database, input)).isFalse();
        }
    }

    static Stream<Arguments> hasMatchReturnsFalseWhenPatternAbsentArgs() {
        return withStrings("hello world", "no secret here", "");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void realWorldRegexPatternsSmoke(DualApi api) {
        List<DualExpression> expressions = Arrays.asList(
                api.createExpression("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}", DualExpressionFlag.SOM_LEFTMOST, 1),
                api.createExpression("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", DualExpressionFlag.SOM_LEFTMOST, 2),
                api.createExpression("https?://[^\\s]+", DualExpressionFlag.SOM_LEFTMOST, 3),
                api.createExpression("\\bERROR\\b", DualExpressionFlag.SOM_LEFTMOST, 4),
                api.createExpression("[0-9]{3}-[0-9]{2}-[0-9]{4}", DualExpressionFlag.SOM_LEFTMOST, 5)
        );
        try (DualDatabase database = api.compileDatabase(expressions)) {
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            String input = "Contact admin@example.com from 203.0.113.42. See https://example.com/page. SSN 123-45-6789. ERROR: disk full.";
            List<DualMatch> matches = api.scan(scanner, database, input);
            assertThat(matches).isNotEmpty();
            assertThat(matches.stream().anyMatch(m -> m.id() == 1)).isTrue();
            assertThat(matches.stream().anyMatch(m -> m.id() == 2)).isTrue();
            assertThat(matches.stream().anyMatch(m -> m.id() == 3)).isTrue();
            assertThat(matches.stream().anyMatch(m -> m.id() == 4)).isTrue();
            assertThat(matches.stream().anyMatch(m -> m.id() == 5)).isTrue();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void expressionValidationWorks(DualApi api) {
        DualExpression valid = api.createExpression("test[0-9]+", DualExpressionFlag.SOM_LEFTMOST);
        assertThat(api.validate(valid)).isTrue();

        DualExpression invalid = api.createExpression("test\\1", DualExpressionFlag.SOM_LEFTMOST);
        assertThat(api.validate(invalid)).isFalse();
        assertThat(api.getValidationError(invalid)).isNotEmpty();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void databaseSerializationRoundTrip(DualApi api) {
        List<DualExpression> expressions = Arrays.asList(
                api.createExpression("foo", DualExpressionFlag.SOM_LEFTMOST, 1),
                api.createExpression("bar", DualExpressionFlag.SOM_LEFTMOST, 2)
        );

        byte[] serialized;
        try (DualDatabase original = api.compileDatabase(expressions)) {
            serialized = api.serialize(original);
            assertThat(serialized.length).isGreaterThan(0);
        }

        try (DualDatabase loaded = api.deserialize(serialized)) {
            scanner = api.createScanner();
            api.allocScratch(scanner, loaded);
            List<DualMatch> matches = api.scan(scanner, loaded, "foo bar");
            assertThat(matches).hasSize(2);
            assertThat(matches).extracting(DualMatch::matchedString).containsExactlyInAnyOrder("foo", "bar");
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void anchoringAndDotallBehavesCorrectly(DualApi api) {
        DualExpression expression = api.createExpression(
                "^start.*end$",
                EnumSet.of(DualExpressionFlag.MULTILINE, DualExpressionFlag.DOTALL, DualExpressionFlag.SOM_LEFTMOST),
                1
        );
        try (DualDatabase database = api.compileDatabase(expression)) {
            scanner = api.createScanner();
            api.allocScratch(scanner, database);
            List<DualMatch> matches = api.scan(scanner, database, "start middle end\nstart another end");
            assertThat(matches).hasSize(2);
            assertThat(matches).allMatch(m -> m.id() == 1);
        }
    }
}
