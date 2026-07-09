package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualMode;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    private static final String DATA = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    private record Match(int id, long to) {
    }

    private record Case(String name, List<DualExpression> patterns, int[] expected) {
    }

    private static Case ordering1(DualApi api) {
        return new Case("ordering1", List.of(
                expr(api, "aa", 1),
                expr(api, "aa.", 2),
                expr(api, "aa..", 3),
                expr(api, "^.{0,4}aa..", 4),
                expr(api, "^.{0,4}aa", 5)
        ), new int[] {31, 30, 29, 5, 5});
    }

    private static Case ordering2(DualApi api) {
        return new Case("ordering2", List.of(
                expr(api, "aa.", 2),
                expr(api, "aa..", 3),
                expr(api, "^.{0,4}aa..", 4),
                expr(api, "^.{0,4}aa", 5)
        ), new int[] {0, 30, 29, 5, 5});
    }

    private static Case ordering3(DualApi api) {
        return new Case("ordering3", List.of(
                expr(api, "aa.", 2),
                expr(api, "aa..", 3),
                expr(api, "^.{0,4}aa..", 4)
        ), new int[] {0, 30, 29, 5, 0});
    }

    private static Case ordering4(DualApi api) {
        return new Case("ordering4", List.of(
                expr(api, "aa", 1),
                expr(api, "aa.", 2),
                expr(api, "aa..", 3)
        ), new int[] {31, 30, 29, 0, 0});
    }

    private static DualExpression expr(DualApi api, String pattern, int id) {
        return api.createExpression(pattern, EnumSet.of(DualExpressionFlag.DOTALL), id);
    }

    @ParameterizedTest
    @ArgumentsSource(OrderArgumentsSource.class)
    void block(DualApi api, Case c) {
        DualDatabase db = api.compileDatabase(c.patterns(), DualMode.BLOCK);
        DualScanner scanner = api.createScanner();
        try {
            api.allocScratch(scanner, db);
            List<Match> matches = new ArrayList<>();
            api.scan(scanner, db, DATA.getBytes(StandardCharsets.UTF_8), (expression, from, to) -> {
                matches.add(new Match(expression.id(), to));
                return true;
            });
            assertOrder(matches, c.expected());
        } finally {
            api.closeScanner(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(OrderArgumentsSource.class)
    void stream(DualApi api, Case c) {
        DualDatabase db = api.compileDatabase(c.patterns(), DualMode.STREAM);
        DualScanner scanner = api.createScanner();
        try {
            byte[] bytes = DATA.getBytes(StandardCharsets.UTF_8);
            for (int jump = 1; jump <= 8; jump++) {
                List<Match> matches = new ArrayList<>();
                DualStream stream = api.openStream(db);
                try {
                    for (int i = 0; i < bytes.length; i += jump) {
                        byte[] chunk = Arrays.copyOfRange(bytes, i, i + Math.min(jump, bytes.length - i));
                        api.scanStream(scanner, stream, chunk, (expression, from, to) -> {
                            matches.add(new Match(expression.id(), to));
                            return true;
                        });
                    }
                } finally {
                    api.closeStream(scanner, stream, (expression, from, to) -> {
                        matches.add(new Match(expression.id(), to));
                        return true;
                    });
                }
                assertOrder(matches, c.expected());
            }
        } finally {
            api.closeScanner(scanner);
            api.closeDatabase(db);
        }
    }

    private static void assertOrder(List<Match> matches, int[] expected) {
        for (int id = 1; id <= 5; id++) {
            int count = 0;
            for (Match m : matches) {
                if (m.id() == id) {
                    count++;
                }
            }
            assertThat(count).as("matches for id %d", id).isEqualTo(expected[id - 1]);
        }
        long last = 0;
        for (Match m : matches) {
            assertThat(m.to()).isGreaterThanOrEqualTo(last);
            last = m.to();
        }
    }

    public static class OrderArgumentsSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(DualImplementation.values())
                    .flatMap(impl -> {
                        DualApi api = impl.createAdapter();
                        List<Case> cases = List.of(ordering1(api), ordering2(api), ordering3(api), ordering4(api));
                        return cases.stream().map(c -> Arguments.of(api, c));
                    });
        }
    }
}
