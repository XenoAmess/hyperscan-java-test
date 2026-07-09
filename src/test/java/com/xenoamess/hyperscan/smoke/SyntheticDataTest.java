package com.xenoamess.hyperscan.smoke;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualMatch;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class SyntheticDataTest {

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void manyRandomLiteralPatterns(DualApi api) {
        int count = 200;
        List<DualExpression> expressions = new ArrayList<>();
        List<String> expectedMatches = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String literal = "LIT_" + UUID.randomUUID().toString().replace("-", "");
            expressions.add(api.createExpression(Pattern.quote(literal), DualExpressionFlag.SOM_LEFTMOST, i));
            if (i % 7 == 0) {
                expectedMatches.add(literal);
            }
        }

        try (DualDatabase db = api.compileDatabase(expressions)) {
            StringBuilder input = new StringBuilder();
            input.append("noise_");
            for (String literal : expectedMatches) {
                input.append(literal).append("_sep_");
            }
            input.append("trailing");

            try (DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, db);
                List<DualMatch> matches = api.scan(scanner, db, input.toString());
                assertThat(matches).hasSize(expectedMatches.size());
                for (DualMatch match : matches) {
                    assertThat(match.id() % 7).isEqualTo(0);
                }
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void characterClassPatternsOnRandomAlphanumericInput(DualApi api) {
        List<DualExpression> expressions = Arrays.asList(
                api.createExpression("[0-9]{5,}", DualExpressionFlag.SOM_LEFTMOST, 1),
                api.createExpression("[A-Z]{4,}", DualExpressionFlag.SOM_LEFTMOST, 2),
                api.createExpression("[a-z]{6,}", DualExpressionFlag.SOM_LEFTMOST, 3),
                api.createExpression("[0-9a-f]{32}", DualExpressionFlag.SOM_LEFTMOST, 4)
        );

        try (DualDatabase db = api.compileDatabase(expressions)) {
            Random random = new Random(42);
            StringBuilder input = new StringBuilder();
            for (int i = 0; i < 5000; i++) {
                input.append((char) ('a' + random.nextInt(26)));
            }
            input.append(" 12345 ABCDE lowercase ");
            input.append(UUID.randomUUID().toString().replace("-", ""));

            try (DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, db);
                List<DualMatch> matches = api.scan(scanner, db, input.toString());
                assertThat(matches).isNotEmpty();
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void longInputScanDoesNotCrash(DualApi api) {
        List<DualExpression> expressions = Arrays.asList(
                api.createExpression("needle", DualExpressionFlag.SOM_LEFTMOST, 1)
        );

        try (DualDatabase db = api.compileDatabase(expressions)) {
            StringBuilder input = new StringBuilder();
            for (int i = 0; i < 100_000; i++) {
                input.append("haystack ");
            }
            input.append("needle");
            for (int i = 0; i < 10_000; i++) {
                input.append(" tail");
            }

            try (DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, db);
                List<DualMatch> matches = api.scan(scanner, db, input.toString());
                assertThat(matches).hasSize(1);
                assertThat(matches.get(0).id()).isEqualTo(1);
            }
        }
    }
}
