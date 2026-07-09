package com.xenoamess.hyperscan.smoke.util;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualPatternFilter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

class PatternFilterTest {

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void readmeSample(DualApi api) throws Exception {
        List<Pattern> patterns = asList(
                Pattern.compile("The number is ([0-9]+)", Pattern.CASE_INSENSITIVE),
                Pattern.compile("The color is (blue|red|orange)")
        );

        try (DualPatternFilter filter = api.createPatternFilter(patterns)) {
            List<Matcher> matchers = filter.filter("The number is 7 the NUMber is 27");

            for (Matcher matcher : matchers) {
                while (matcher.find()) {
                    System.out.println(matcher.group(1));
                }
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void filterNotMatchingPatterns(DualApi api) throws Exception {
        List<Pattern> patterns = asList(
                Pattern.compile("The number is ([0-9]+)", Pattern.CASE_INSENSITIVE),
                Pattern.compile("The color is (blue|red|orange)")
        );

        try (DualPatternFilter filter = api.createPatternFilter(patterns)) {
            List<Matcher> matchers = filter.filter("The color is orange");

            assertHasPattern(patterns.get(1), matchers);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void handleFlagsProperly(DualApi api) throws Exception {
        List<Pattern> patterns = asList(
            Pattern.compile("The number is ([0-9]+)"),
            Pattern.compile("The number is ([0-9]+)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("^The color is (blue|red|orange)$"),
            Pattern.compile("^The color is (blue|red|orange)$", Pattern.MULTILINE),
            Pattern.compile("something.else"),
            Pattern.compile("something.else", Pattern.DOTALL),
            Pattern.compile("match.THIS", Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
        );

        try (DualPatternFilter filter = api.createPatternFilter(patterns)) {
            List<Matcher> matchers = filter.filter("tHE nuMBeR is 17");
            assertHasPattern(patterns.get(1), matchers);
            assertEquals(1, matchers.size());

            matchers = filter.filter("The number is 17");
            assertHasPattern(patterns.get(0), matchers);
            assertHasPattern(patterns.get(1), matchers);
            assertEquals(2, matchers.size());

            matchers = filter.filter("Some text\nThe color is red");
            assertHasPattern(patterns.get(3), matchers);
            assertEquals(1, matchers.size());

            matchers = filter.filter("something\nelse");
            assertHasPattern(patterns.get(5), matchers);
            assertEquals(1, matchers.size());

            matchers = filter.filter("match\nthiS");
            assertHasPattern(patterns.get(6), matchers);
            assertEquals(1, matchers.size());
        }
    }

    private void assertHasPattern(Pattern pattern, List<Matcher> matchers) {
        List<Pattern> filteredPatterns = matchers.stream().map(Matcher::pattern).collect(toList());
        assertTrue(filteredPatterns.contains(pattern));
    }
}
