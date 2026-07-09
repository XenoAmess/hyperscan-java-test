package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
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

class SomTest {

    private static final int MAX_CHUNK_SIZE = 16 * 1024 * 1024;
    private static final int MIN_CHUNK_SIZE = 4096;

    private record Match(long from, long to, int id) {
    }

    private static final class CollectingHandler implements DualByteMatchHandler {
        private final List<Match> matches = new ArrayList<>();

        @Override
        public boolean onMatch(DualExpression expression, long from, long to) {
            matches.add(new Match(from, to, expression.id()));
            return true;
        }
    }

    private static int chunkSizeForHorizon(long horizon) {
        int chunk = (int) (horizon / 256);
        return Math.min(Math.max(chunk, MIN_CHUNK_SIZE), MAX_CHUNK_SIZE);
    }

    private static DualExpression expr(DualApi api, String pattern, int id, EnumSet<DualExpressionFlag> flags) {
        return api.createExpression(pattern, flags, id);
    }

    private static DualDatabase compileStream(DualApi api, DualExpression expression, int somMode) {
        DualCompileResult result = api.compileRaw(expression, api.modeStream() | somMode);
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        return db;
    }

    @ParameterizedTest
    @ArgumentsSource(SomArgumentsSource.class)
    void pastHorizon(DualApi api, int somMode, long horizon) {
        DualExpression expression = expr(api, "foo.*bar", 1000, EnumSet.of(DualExpressionFlag.SOM_LEFTMOST));
        DualDatabase db = compileStream(api, expression, somMode);
        try (DualStream stream = api.openStream(db)) {
            CollectingHandler handler = new CollectingHandler();
            String prefix = " foo";
            String suffix = "bar";
            int chunkSize = chunkSizeForHorizon(horizon);
            byte[] filler = new byte[chunkSize];
            Arrays.fill(filler, (byte) 'X');
            long scannedLen = 0;
            api.scanStream(null, stream, prefix.getBytes(StandardCharsets.UTF_8), handler);
            scannedLen += prefix.length();
            long blocks = horizon / chunkSize;
            for (long i = 0; i < blocks; i++) {
                api.scanStream(null, stream, filler, handler);
                scannedLen += filler.length;
            }
            api.scanStream(null, stream, suffix.getBytes(StandardCharsets.UTF_8), handler);
            scannedLen += suffix.length();
            api.closeStream(null, stream, handler);
            assertThat(handler.matches).containsExactly(
                    new Match(api.offsetPastHorizon(), scannedLen, 1000));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(SomArgumentsSource.class)
    void nearHorizon(DualApi api, int somMode, long horizon) {
        DualExpression expression = expr(api, "foo.*bar", 1000, EnumSet.of(DualExpressionFlag.SOM_LEFTMOST));
        DualDatabase db = compileStream(api, expression, somMode);
        try (DualStream stream = api.openStream(db)) {
            CollectingHandler handler = new CollectingHandler();
            String prefix = " foo";
            String suffix = "bar";
            int chunkSize = chunkSizeForHorizon(horizon);
            byte[] filler = new byte[chunkSize];
            Arrays.fill(filler, (byte) 'X');
            long scannedLen = 0;
            api.scanStream(null, stream, prefix.getBytes(StandardCharsets.UTF_8), handler);
            scannedLen += prefix.length();
            long blocks = horizon / chunkSize - 1;
            for (long i = 0; i < blocks; i++) {
                api.scanStream(null, stream, filler, handler);
                scannedLen += filler.length;
            }
            api.scanStream(null, stream, suffix.getBytes(StandardCharsets.UTF_8), handler);
            scannedLen += suffix.length();
            api.closeStream(null, stream, handler);
            assertThat(handler.matches).containsExactly(
                    new Match(1L, scannedLen, 1000));
        } finally {
            api.closeDatabase(db);
        }
    }

    public static class SomArgumentsSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(DualImplementation.values())
                    .flatMap(impl -> {
                        DualApi api = impl.createAdapter();
                        return Stream.of(
                                Arguments.of(api, api.modeSomHorizonSmall(), 1L << 16),
                                Arguments.of(api, api.modeSomHorizonMedium(), 1L << 32)
                        );
                    });
        }
    }
}
