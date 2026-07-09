package com.xenoamess.hyperscan.smoke.util;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualMatch;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.EnumSet;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class Utf8EncoderTest {

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void testAsciiEncoding(DualApi api) {
        String input = "Hello, world!";
        assertLiteralMatch(api, input, input, 0, 12, input);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void testThreeByteEncoding(DualApi api) {
        String input = "あいう";
        assertLiteralMatch(api, input, input, 0, 2, input);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void testValidSurrogatePair(DualApi api) {
        String input = "𝄞";
        assertLiteralMatch(api, input, input, 0, 1, input);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void testLonelyHighSurrogateInMiddle(DualApi api) {
        String input = "A" + new String(new char[]{0xD800}) + "B";
        assertLiteralMatch(api, input, "B", 2, 2, "B");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void testLonelyLowSurrogateInMiddle(DualApi api) {
        String input = "A" + new String(new char[]{0xDC00}) + "B";
        assertLiteralMatch(api, input, "B", 2, 2, "B");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void testLonelySurrogateAtEnd(DualApi api) {
        String input = "ABC" + new String(new char[]{0xD800});
        assertLiteralMatch(api, input, "?", 3, 3, new String(new char[]{0xD800}));
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void testTwoByteEncoding(DualApi api) {
        String input = "ñáé";
        assertLiteralMatch(api, input, input, 0, 2, input);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void testCombinedString(DualApi api) {
        String input = "Hi 世𝄞" + new String(new char[]{0xD800}) + " " +
                       new String(new char[]{0xDC00}) + " End";
        assertLiteralMatch(api, input, "End", 10, 12, "End");
        assertLiteralMatch(api, input, "Hi", 0, 1, "Hi");
        assertLiteralMatch(api, input, "世", 3, 3, "世");
    }

    private static void assertLiteralMatch(DualApi api, String input, String literal, long expectedStart, long expectedEnd, String expectedMatch) {
        DualExpression expression = api.createExpression(Pattern.quote(literal),
                EnumSet.of(DualExpressionFlag.UTF8, DualExpressionFlag.SOM_LEFTMOST), 1);
        try (DualDatabase db = api.compileDatabase(expression); DualScanner scanner = api.createScanner()) {
            api.allocScratch(scanner, db);
            java.util.List<DualMatch> matches = api.scan(scanner, db, input);
            assertThat(matches).hasSize(1);
            DualMatch m = matches.get(0);
            assertThat(m.start()).isEqualTo(expectedStart);
            assertThat(m.end()).isEqualTo(expectedEnd);
            assertThat(m.matchedString()).isEqualTo(expectedMatch);
        }
    }
}
