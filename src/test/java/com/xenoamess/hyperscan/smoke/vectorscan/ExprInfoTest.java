package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionExt;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionInfo;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualResult;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.EnumSet;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ExprInfoTest {

    private static final long HS_EXT_FLAG_MIN_OFFSET = 1;
    private static final long HS_EXT_FLAG_MAX_OFFSET = 2;
    private static final long HS_EXT_FLAG_MIN_LENGTH = 4;
    private static final long HS_EXT_FLAG_EDIT_DISTANCE = 8;
    private static final long HS_EXT_FLAG_HAMMING_DISTANCE = 16;

    private static final long UINT_MAX = 0xFFFFFFFFL;

    private record ExpectedInfo(String pattern, DualExpressionExt ext, long min, long max,
                                boolean unorderedMatches, boolean matchesAtEod, boolean matchesOnlyAtEod) {
    }

    private static DualExpressionExt ext(long flags, long minOffset, long maxOffset, long minLength,
                                         int editDistance, int hammingDistance) {
        return new DualExpressionExt(flags, minOffset, maxOffset, minLength, editDistance, hammingDistance);
    }

    private static final ExpectedInfo[] EXPECTED_INFOS = new ExpectedInfo[] {
            new ExpectedInfo("abc", ext(0, 0, 0, 0, 0, 0), 3, 3, false, false, false),
            new ExpectedInfo("abc.*def", ext(0, 0, 0, 0, 0, 0), 6, UINT_MAX, false, false, false),
            new ExpectedInfo("abc|defghi", ext(0, 0, 0, 0, 0, 0), 3, 6, false, false, false),
            new ExpectedInfo("abc(def)?", ext(0, 0, 0, 0, 0, 0), 3, 6, false, false, false),
            new ExpectedInfo("abc(def){0,3}", ext(0, 0, 0, 0, 0, 0), 3, 12, false, false, false),
            new ExpectedInfo("abc(def){1,4}", ext(0, 0, 0, 0, 0, 0), 6, 15, false, false, false),
            new ExpectedInfo("", ext(0, 0, 0, 0, 0, 0), 0, 0, false, false, false),
            new ExpectedInfo("^", ext(0, 0, 0, 0, 0, 0), 0, 0, false, false, false),
            new ExpectedInfo("^\\b", ext(0, 0, 0, 0, 0, 0), 0, 0, true, false, false),
            new ExpectedInfo("\\b$", ext(0, 0, 0, 0, 0, 0), 0, 0, true, true, true),
            new ExpectedInfo("(?m)\\b$", ext(0, 0, 0, 0, 0, 0), 0, 0, true, true, false),
            new ExpectedInfo("\\A", ext(0, 0, 0, 0, 0, 0), 0, 0, false, false, false),
            new ExpectedInfo("\\z", ext(0, 0, 0, 0, 0, 0), 0, 0, false, true, true),
            new ExpectedInfo("\\Z", ext(0, 0, 0, 0, 0, 0), 0, 0, true, true, true),
            new ExpectedInfo("$", ext(0, 0, 0, 0, 0, 0), 0, 0, true, true, true),
            new ExpectedInfo("(?m)$", ext(0, 0, 0, 0, 0, 0), 0, 0, true, true, false),
            new ExpectedInfo("^foo", ext(0, 0, 0, 0, 0, 0), 3, 3, false, false, false),
            new ExpectedInfo("^foo.*bar", ext(0, 0, 0, 0, 0, 0), 6, UINT_MAX, false, false, false),
            new ExpectedInfo("^foo.*bar?", ext(0, 0, 0, 0, 0, 0), 5, UINT_MAX, false, false, false),
            new ExpectedInfo("^foo.*bar$", ext(0, 0, 0, 0, 0, 0), 6, UINT_MAX, true, true, true),
            new ExpectedInfo("^foobar$", ext(0, 0, 0, 0, 0, 0), 6, 6, true, true, true),
            new ExpectedInfo("foobar$", ext(0, 0, 0, 0, 0, 0), 6, 6, true, true, true),
            new ExpectedInfo("^.*foo", ext(0, 0, 0, 0, 0, 0), 3, UINT_MAX, false, false, false),
            new ExpectedInfo("foo\\b", ext(0, 0, 0, 0, 0, 0), 3, 3, true, true, false),
            new ExpectedInfo("foo.{1,13}bar", ext(0, 0, 0, 0, 0, 0), 7, 19, false, false, false),
            new ExpectedInfo("foo.{10,}bar", ext(0, 0, 0, 0, 0, 0), 16, UINT_MAX, false, false, false),
            new ExpectedInfo("foo.{0,10}bar", ext(0, 0, 0, 0, 0, 0), 6, 16, false, false, false),
            new ExpectedInfo("foo.{,10}bar", ext(0, 0, 0, 0, 0, 0), 12, 12, false, false, false),
            new ExpectedInfo("foo.{10}bar", ext(0, 0, 0, 0, 0, 0), 16, 16, false, false, false),
            new ExpectedInfo("(^|\n)foo", ext(0, 0, 0, 0, 0, 0), 3, 4, false, false, false),
            new ExpectedInfo("(^\n|)foo", ext(0, 0, 0, 0, 0, 0), 3, 4, false, false, false),
            new ExpectedInfo("(?m)^foo", ext(0, 0, 0, 0, 0, 0), 3, 3, false, false, false),
            new ExpectedInfo("\\bfoo", ext(0, 0, 0, 0, 0, 0), 3, 3, false, false, false),
            new ExpectedInfo("^\\bfoo", ext(0, 0, 0, 0, 0, 0), 3, 3, false, false, false),
            new ExpectedInfo("(?m)^\\bfoo", ext(0, 0, 0, 0, 0, 0), 3, 3, false, false, false),
            new ExpectedInfo("\\Bfoo", ext(0, 0, 0, 0, 0, 0), 3, 3, false, false, false),
            new ExpectedInfo("(foo|bar\\z)", ext(0, 0, 0, 0, 0, 0), 3, 3, false, true, false),
            new ExpectedInfo("(foo|bar)\\z", ext(0, 0, 0, 0, 0, 0), 3, 3, false, true, true),

            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_MAX_OFFSET, 0, 10, 0, 0, 0), 6, 10, false, false, false),
            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_MIN_LENGTH, 0, 0, 100, 0, 0), 100, UINT_MAX, false, false, false),
            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_MAX_OFFSET, 0, 10, 0, 0, 0), 6, 10, false, false, false),
            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_MIN_LENGTH, 0, 0, 100, 0, 0), 100, UINT_MAX, false, false, false),
            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_MIN_LENGTH, 0, 0, 5, 0, 0), 6, UINT_MAX, false, false, false),

            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_EDIT_DISTANCE, 0, 0, 0, 1, 0), 5, UINT_MAX, false, false, false),
            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_EDIT_DISTANCE, 0, 0, 0, 2, 0), 4, UINT_MAX, false, false, false),
            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_EDIT_DISTANCE | HS_EXT_FLAG_MIN_LENGTH, 0, 0, 10, 2, 0), 10, UINT_MAX, false, false, false),
            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_EDIT_DISTANCE | HS_EXT_FLAG_MIN_OFFSET, 6, 0, 0, 2, 0), 4, UINT_MAX, false, false, false),
            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_EDIT_DISTANCE | HS_EXT_FLAG_MAX_OFFSET, 0, 6, 0, 2, 0), 4, 6, false, false, false),

            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_EDIT_DISTANCE, 0, 0, 0, 1, 0), 5, UINT_MAX, false, false, false),
            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_EDIT_DISTANCE, 0, 0, 0, 2, 0), 4, UINT_MAX, false, false, false),
            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_EDIT_DISTANCE | HS_EXT_FLAG_MIN_LENGTH, 0, 0, 10, 2, 0), 10, UINT_MAX, false, false, false),
            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_EDIT_DISTANCE | HS_EXT_FLAG_MIN_OFFSET, 6, 0, 0, 2, 0), 4, UINT_MAX, false, false, false),
            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_EDIT_DISTANCE | HS_EXT_FLAG_MAX_OFFSET, 0, 6, 0, 2, 0), 4, 6, false, false, false),

            new ExpectedInfo("^abcdef", ext(HS_EXT_FLAG_EDIT_DISTANCE, 0, 0, 0, 1, 0), 5, 7, false, false, false),
            new ExpectedInfo("^abcdef", ext(HS_EXT_FLAG_EDIT_DISTANCE, 0, 0, 0, 2, 0), 4, 8, false, false, false),
            new ExpectedInfo("^abcdef", ext(HS_EXT_FLAG_EDIT_DISTANCE | HS_EXT_FLAG_MIN_LENGTH, 0, 0, 8, 2, 0), 8, 8, false, false, false),
            new ExpectedInfo("^abcdef", ext(HS_EXT_FLAG_EDIT_DISTANCE | HS_EXT_FLAG_MIN_OFFSET, 6, 0, 0, 2, 0), 4, 8, false, false, false),
            new ExpectedInfo("^abcdef", ext(HS_EXT_FLAG_EDIT_DISTANCE | HS_EXT_FLAG_MAX_OFFSET, 0, 6, 0, 2, 0), 4, 6, false, false, false),

            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_HAMMING_DISTANCE, 0, 0, 0, 0, 1), 6, UINT_MAX, false, false, false),
            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_HAMMING_DISTANCE, 0, 0, 0, 0, 2), 6, UINT_MAX, false, false, false),
            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_HAMMING_DISTANCE, 0, 0, 0, 0, 5), 6, UINT_MAX, false, false, false),
            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_HAMMING_DISTANCE | HS_EXT_FLAG_MIN_LENGTH, 0, 0, 10, 0, 2), 10, UINT_MAX, false, false, false),
            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_HAMMING_DISTANCE | HS_EXT_FLAG_MIN_OFFSET, 6, 0, 0, 0, 2), 6, UINT_MAX, false, false, false),
            new ExpectedInfo("abc.*def", ext(HS_EXT_FLAG_HAMMING_DISTANCE | HS_EXT_FLAG_MAX_OFFSET, 0, 6, 0, 0, 2), 6, 6, false, false, false),

            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_HAMMING_DISTANCE, 0, 0, 0, 0, 1), 6, UINT_MAX, false, false, false),
            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_HAMMING_DISTANCE, 0, 0, 0, 0, 2), 6, UINT_MAX, false, false, false),
            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_HAMMING_DISTANCE, 0, 0, 0, 0, 5), 6, UINT_MAX, false, false, false),
            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_HAMMING_DISTANCE | HS_EXT_FLAG_MIN_LENGTH, 0, 0, 10, 0, 2), 10, UINT_MAX, false, false, false),
            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_HAMMING_DISTANCE | HS_EXT_FLAG_MIN_OFFSET, 6, 0, 0, 0, 2), 6, UINT_MAX, false, false, false),
            new ExpectedInfo("^abc.*def", ext(HS_EXT_FLAG_HAMMING_DISTANCE | HS_EXT_FLAG_MAX_OFFSET, 0, 6, 0, 0, 2), 6, 6, false, false, false),

            new ExpectedInfo("^abcdef", ext(HS_EXT_FLAG_HAMMING_DISTANCE, 0, 0, 0, 0, 1), 6, 6, false, false, false),
            new ExpectedInfo("^abcdef", ext(HS_EXT_FLAG_HAMMING_DISTANCE, 0, 0, 0, 0, 2), 6, 6, false, false, false),
            new ExpectedInfo("^abcdef", ext(HS_EXT_FLAG_HAMMING_DISTANCE, 0, 0, 0, 0, 5), 6, 6, false, false, false),
            new ExpectedInfo("^abcdef", ext(HS_EXT_FLAG_HAMMING_DISTANCE | HS_EXT_FLAG_MIN_LENGTH, 0, 0, 6, 0, 2), 6, 6, false, false, false),
            new ExpectedInfo("^abcdef", ext(HS_EXT_FLAG_HAMMING_DISTANCE | HS_EXT_FLAG_MIN_OFFSET, 6, 0, 0, 0, 2), 6, 6, false, false, false),
            new ExpectedInfo("^abcdef", ext(HS_EXT_FLAG_HAMMING_DISTANCE | HS_EXT_FLAG_MAX_OFFSET, 0, 6, 0, 0, 2), 6, 6, false, false, false),
    };

    @ParameterizedTest
    @ArgumentsSource(ExprInfoArgumentsSource.class)
    void checkNoExt(DualApi api, ExpectedInfo info) {
        if (info.ext().flags() != 0) {
            return;
        }
        DualResult<DualExpressionInfo> result = api.expressionInfoDataRaw(info.pattern(), EnumSet.noneOf(DualExpressionFlag.class));
        assertThat(result.code()).isEqualTo(api.success());
        assertInfo(info, result.value());
    }

    @ParameterizedTest
    @ArgumentsSource(ExprInfoArgumentsSource.class)
    void checkExt(DualApi api, ExpectedInfo info) {
        DualResult<DualExpressionInfo> result = api.expressionExtInfoDataRaw(info.pattern(), EnumSet.noneOf(DualExpressionFlag.class), info.ext());
        assertThat(result.code()).isEqualTo(api.success());
        assertInfo(info, result.value());
    }

    @ParameterizedTest
    @ArgumentsSource(ExprInfoArgumentsSource.class)
    void checkExtNull(DualApi api, ExpectedInfo info) {
        if (info.ext().flags() != 0) {
            return;
        }
        DualResult<DualExpressionInfo> result = api.expressionExtInfoDataRaw(info.pattern(), EnumSet.noneOf(DualExpressionFlag.class), null);
        assertThat(result.code()).isEqualTo(api.success());
        assertInfo(info, result.value());
    }

    private static void assertInfo(ExpectedInfo expected, DualExpressionInfo actual) {
        assertThat(actual).isNotNull();
        assertThat(actual.minWidth()).isEqualTo(expected.min());
        assertThat(actual.maxWidth()).isEqualTo(expected.max());
        assertThat(actual.unorderedMatches()).isEqualTo(expected.unorderedMatches());
        assertThat(actual.matchesAtEod()).isEqualTo(expected.matchesAtEod());
        assertThat(actual.matchesOnlyAtEod()).isEqualTo(expected.matchesOnlyAtEod());
    }

    public static class ExprInfoArgumentsSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(DualImplementation.values())
                    .flatMap(impl -> Stream.of(EXPECTED_INFOS)
                            .map(info -> Arguments.of(impl.createAdapter(), info)));
        }
    }
}
