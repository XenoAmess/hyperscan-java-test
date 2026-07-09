package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualPlatformInfo;
import com.xenoamess.hyperscan.smoke.dual.DualResult;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SerializeTest {

    private static final int MAX_ALIGN = 16;

    private record PatternCase(String pattern, EnumSet<DualExpressionFlag> flags, int id) {
    }

    private static final List<PatternCase> PATTERNS = List.of(
            new PatternCase("hatstand.*teakettle.*badgerbrush", EnumSet.of(DualExpressionFlag.CASELESS), 1000),
            new PatternCase("hatstand.*teakettle.*badgerbrush", EnumSet.of(DualExpressionFlag.DOTALL), 1001),
            new PatternCase("hatstand|teakettle|badgerbrush", EnumSet.noneOf(DualExpressionFlag.class), 1002),
            new PatternCase("^hatstand|teakettle|badgerbrush$", EnumSet.noneOf(DualExpressionFlag.class), 1003),
            new PatternCase("foobar.{10,1000}xyzzy", EnumSet.of(DualExpressionFlag.DOTALL), 1004),
            new PatternCase("foobar.{2,501}roobar", EnumSet.noneOf(DualExpressionFlag.class), 1005),
            new PatternCase("abc.*def.*ghi", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST), 1006),
            new PatternCase("(\\p{L}){4}", EnumSet.of(DualExpressionFlag.UTF8, DualExpressionFlag.UCP), 1007),
            new PatternCase("\\.(exe|pdf|gif|jpg|png|wav|riff|mp4)\\z", EnumSet.noneOf(DualExpressionFlag.class), 1008));

    private static int[] validModes(DualApi api) {
        return new int[] { api.modeBlock(), api.modeStream() | api.modeSomHorizonLarge(), api.modeVectored() };
    }

    private static DualDatabase compile(DualApi api, PatternCase patternCase, int mode) {
        DualExpression expression = api.createExpression(patternCase.pattern, patternCase.flags, patternCase.id);
        DualCompileResult result = api.compileRaw(expression, mode);
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        return db;
    }

    @ParameterizedTest
    @ArgumentsSource(SerializeArgumentsSource.class)
    void deserializeFromAnyAlignment(DualApi api, int mode, PatternCase patternCase) {
        DualDatabase db = compile(api, patternCase, mode);
        try {
            DualResult<String> originalInfoResult = api.getDatabaseInfoRaw(db);
            assertThat(originalInfoResult.code()).isEqualTo(api.success());
            String originalInfo = originalInfoResult.value();
            assertThat(originalInfo).isNotNull();
            assertThat(originalInfo.startsWith("Version:")).isTrue();
            DualResult<byte[]> serializedResult = api.serializeRaw(db);
            assertThat(serializedResult.code()).isEqualTo(api.success());
            byte[] serialized = serializedResult.value();
            assertThat(serialized).isNotNull();
            assertThat(serialized.length).isGreaterThan(0);
            byte[] copy = new byte[serialized.length + MAX_ALIGN];
            for (int i = 0; i < MAX_ALIGN; i++) {
                Arrays.fill(copy, (byte) 0);
                System.arraycopy(serialized, 0, copy, i, serialized.length);
                byte[] aligned = Arrays.copyOfRange(copy, i, i + serialized.length);
                DualResult<String> infoResult = api.getSerializedDatabaseInfoRaw(aligned);
                assertThat(infoResult.code()).isEqualTo(api.success());
                assertThat(infoResult.value()).isEqualTo(originalInfo);
                DualResult<DualDatabase> dbResult = api.deserializeRaw(aligned);
                assertThat(dbResult.code()).isEqualTo(api.success());
                DualDatabase db2 = dbResult.value();
                assertThat(db2).isNotNull();
                try {
                    DualResult<String> dbInfoResult = api.getDatabaseInfoRaw(db2);
                    assertThat(dbInfoResult.code()).isEqualTo(api.success());
                    assertThat(dbInfoResult.value()).isEqualTo(originalInfo);
                } finally {
                    api.closeDatabase(db2);
                }
            }
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(SerializeArgumentsSource.class)
    void deserializeAtFromAnyAlignment(DualApi api, int mode, PatternCase patternCase) {
        DualDatabase db = compile(api, patternCase, mode);
        try {
            DualResult<String> originalInfoResult = api.getDatabaseInfoRaw(db);
            assertThat(originalInfoResult.code()).isEqualTo(api.success());
            String originalInfo = originalInfoResult.value();
            DualResult<byte[]> serializedResult = api.serializeRaw(db);
            assertThat(serializedResult.code()).isEqualTo(api.success());
            byte[] serialized = serializedResult.value();
            DualResult<Long> serializedSizeResult = api.getSerializedDatabaseSizeRaw(serialized);
            assertThat(serializedSizeResult.code()).isEqualTo(api.success());
            long serializedSize = serializedSizeResult.value();
            byte[] copy = new byte[serialized.length + MAX_ALIGN];
            for (int i = 0; i < MAX_ALIGN; i++) {
                Arrays.fill(copy, (byte) 0);
                System.arraycopy(serialized, 0, copy, i, serialized.length);
                byte[] aligned = Arrays.copyOfRange(copy, i, i + serialized.length);
                DualResult<String> infoResult = api.getSerializedDatabaseInfoRaw(aligned);
                assertThat(infoResult.code()).isEqualTo(api.success());
                assertThat(infoResult.value()).isEqualTo(originalInfo);
                DualResult<DualDatabase> dbResult = api.deserializeRaw(serialized);
                assertThat(dbResult.code()).isEqualTo(api.success());
                DualDatabase db2 = dbResult.value();
                assertThat(db2).isNotNull();
                try {
                    assertThat(api.deserializeAtRaw(aligned, db2)).isEqualTo(api.success());
                    DualResult<String> dbInfoResult = api.getDatabaseInfoRaw(db2);
                    assertThat(dbInfoResult.code()).isEqualTo(api.success());
                    assertThat(dbInfoResult.value()).isEqualTo(originalInfo);
                } finally {
                    api.closeDatabase(db2);
                }
            }
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void crossCompileSom(DualApi api) {
        EnumSet<DualExpressionFlag> flags = EnumSet.of(DualExpressionFlag.SOM_LEFTMOST);
        DualPlatformInfo platform = new DualPlatformInfo(0, 0);
        DualCompileResult result = api.compileRaw(
                "hatstand.*(badgerbrush|teakettle)",
                api.flagsToBits(flags),
                api.modeStream() | api.modeSomHorizonLarge(),
                platform);
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        try {
            DualResult<Long> sizeResult = api.getDatabaseSizeRaw(db);
            assertThat(sizeResult.code()).isEqualTo(api.success());
            assertThat(sizeResult.value()).isGreaterThan(0);
            DualResult<byte[]> serializedResult = api.serializeRaw(db);
            assertThat(serializedResult.code()).isEqualTo(api.success());
            byte[] serialized = serializedResult.value();
            assertThat(serialized).isNotNull();
            assertThat(serialized.length).isGreaterThan(0);
            DualResult<Long> serializedSizeResult = api.getSerializedDatabaseSizeRaw(serialized);
            assertThat(serializedSizeResult.code()).isEqualTo(api.success());
            assertThat(serializedSizeResult.value()).isEqualTo(sizeResult.value());
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void deserializeGarbage(DualApi api) {
        DualCompileResult result = api.compileRaw(
                "hatstand.*(badgerbrush|teakettle)", 0, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        try {
            DualResult<Long> sizeResult = api.getDatabaseSizeRaw(db);
            assertThat(sizeResult.code()).isEqualTo(api.success());
            assertThat(sizeResult.value()).isGreaterThan(0);
            DualResult<byte[]> serializedResult = api.serializeRaw(db);
            assertThat(serializedResult.code()).isEqualTo(api.success());
            byte[] serialized = serializedResult.value();
            byte[] extended = Arrays.copyOf(serialized, serialized.length + 1);
            byte[][] invalidArgs = {
                    Arrays.copyOfRange(extended, 1, 1 + serialized.length),
                    Arrays.copyOfRange(extended, 1, serialized.length),
                    Arrays.copyOf(serialized, serialized.length - 1),
                    Arrays.copyOf(extended, serialized.length + 1)
            };
            for (byte[] arg : invalidArgs) {
                assertThat(api.deserializeRaw(arg).code()).isNotEqualTo(api.success());
                DualResult<DualDatabase> dbResult = api.deserializeRaw(serialized);
                assertThat(dbResult.code()).isEqualTo(api.success());
                DualDatabase target = dbResult.value();
                assertThat(target).isNotNull();
                try {
                    assertThat(api.deserializeAtRaw(arg, target)).isNotEqualTo(api.success());
                } finally {
                    api.closeDatabase(target);
                }
                assertThat(api.getSerializedDatabaseInfoRaw(arg).code()).isNotEqualTo(api.success());
                assertThat(api.getSerializedDatabaseSizeRaw(arg).code()).isNotEqualTo(api.success());
            }
        } finally {
            api.closeDatabase(db);
        }
    }

    public static class SerializeArgumentsSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(DualImplementation.values())
                    .flatMap(impl -> {
                        DualApi api = impl.createAdapter();
                        return Arrays.stream(validModes(api))
                                .boxed()
                                .flatMap(mode -> PATTERNS.stream()
                                        .map(patternCase -> Arguments.of(api, mode, patternCase)));
                    });
        }
    }
}
