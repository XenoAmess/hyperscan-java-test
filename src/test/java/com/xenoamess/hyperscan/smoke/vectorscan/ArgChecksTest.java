package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualPlatformInfo;
import com.xenoamess.hyperscan.smoke.dual.DualResult;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualScratchResult;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
import com.xenoamess.hyperscan.smoke.dual.DualStreamResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArgChecksTest {

    private static final DualByteMatchHandler DUMMY = (expr, from, to) -> true;

    private static final byte[] DATA = "data".getBytes();

    private static final byte[][] VECTOR_DATA = { "data".getBytes(), "data".getBytes() };

    private static final byte[] DATA_4 = new byte[4];

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void validPlatform(DualApi api) {
        assertThat(api.validPlatformRaw()).isEqualTo(api.success());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void version(DualApi api) {
        String version = api.getVersion();
        assertThat(version).isNotNull();
        assertThat(version.charAt(0)).isBetween('0', '9');
        assertThat(version.charAt(1)).isEqualTo('.');
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void singleCompileBlockNoPattern(DualApi api) {
        DualCompileResult result = api.compileRaw(null, 0, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.database()).isNull();
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void singleCompileStreamingNoPattern(DualApi api) {
        DualCompileResult result = api.compileRaw(null, 0, api.modeStream());
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.database()).isNull();
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void singleCompileBlockNoDatabase(DualApi api) {
        DualCompileResult result = api.compileNullOutputRaw("foobar", 0, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void singleCompileStreamingNoDatabase(DualApi api) {
        DualCompileResult result = api.compileNullOutputRaw("foobar", 0, api.modeStream());
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void singleCompileNoMode(DualApi api) {
        DualCompileResult result = api.compileRaw("foobar", 0, 0);
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void singleCompileSeveralModes(DualApi api) {
        DualCompileResult result = api.compileRaw("foobar", 0, api.modeStream() | api.modeBlock());
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void singleCompileBogusFlags(DualApi api) {
        DualCompileResult result = api.compileRaw("foobar", 0xdeadbeef, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isEqualTo("only HS_FLAG_QUIET and HS_FLAG_SINGLEMATCH are supported in combination with HS_FLAG_COMBINATION.");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void singleCompileBogusMode(DualApi api) {
        DualCompileResult result = api.compileRaw("foobar", 0, api.modeStream() | (1 << 30));
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isEqualTo("Invalid parameter: unrecognised mode flags.");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void singleCompileBadTune(DualApi api) {
        DualCompileResult result = api.compileRaw("foobar", 0, api.modeBlock(), new DualPlatformInfo(42, 0));
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isEqualTo("Invalid tuning value specified in the platform information.");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void singleCompileBadFeatures(DualApi api) {
        DualCompileResult result = api.compileRaw("foobar", 0, api.modeBlock(), new DualPlatformInfo(0, 42));
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isEqualTo("Invalid cpu features specified in the platform information.");
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void singleCompileSomFlag(DualApi api) {
        int flags = api.flagsToBits(EnumSet.of(DualExpressionFlag.SOM_LEFTMOST));
        DualCompileResult result = api.compileRaw("foobar", flags, api.modeStream());
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void singleCompileSomModes(DualApi api) {
        int flags = api.flagsToBits(EnumSet.of(DualExpressionFlag.SOM_LEFTMOST));
        int[] somModes = {
                api.modeSomHorizonLarge(),
                api.modeSomHorizonMedium(),
                api.modeSomHorizonSmall()
        };
        for (int somMode : somModes) {
            DualCompileResult result = api.compileRaw("foobar", flags, api.modeStream() | somMode);
            assertThat(result.code()).isEqualTo(api.success());
            result.database().close();
        }
        for (int i = 0; i < somModes.length; i++) {
            for (int j = i + 1; j < somModes.length; j++) {
                DualCompileResult result = api.compileRaw("foobar", flags, api.modeStream() | somModes[i] | somModes[j]);
                assertThat(result.code()).isEqualTo(api.compilerError());
                assertThat(result.message()).isNotNull();
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void multiCompileBlockNoPattern(DualApi api) {
        DualCompileResult result = api.compileRaw(null, null, null, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.database()).isNull();
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void multiCompileStreamingNoPattern(DualApi api) {
        DualCompileResult result = api.compileRaw(null, null, null, api.modeStream());
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.database()).isNull();
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void multiCompileZeroPatterns(DualApi api) {
        DualCompileResult result = api.compileRaw(List.of(), api.modeBlock());
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.database()).isNull();
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void multiCompileBlockNoDatabase(DualApi api) {
        DualCompileResult result = api.compileMultiNullOutputRaw(new String[] { "foobar" }, null, null, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void multiCompileStreamingNoDatabase(DualApi api) {
        DualCompileResult result = api.compileMultiNullOutputRaw(new String[] { "foobar" }, null, null, api.modeStream());
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void openStreamNoDatabase(DualApi api) {
        DualStreamResult result = api.openStreamRaw(null);
        assertThat(result.code()).isNotEqualTo(api.success());
        assertThat(result.stream()).isNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void openStreamNoStreamId(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        try {
            int code = api.openStreamNullOutput(db);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void openStreamWithBlockDatabase(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeBlock()).database();
        try {
            DualStreamResult result = api.openStreamRaw(db);
            assertThat(result.code()).isNotEqualTo(api.success());
            assertThat(result.stream()).isNull();
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void openStreamWithBrokenDatabaseBytecode(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanStreamNoStreamId(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanStreamRaw(null, DATA, scanner, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanStreamNoData(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        DualStreamResult streamResult = api.openStreamRaw(db);
        DualStream stream = streamResult.stream();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanStreamRaw(stream, null, scanner, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            stream.close();
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanStreamNoScratch(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        DualStreamResult streamResult = api.openStreamRaw(db);
        DualStream stream = streamResult.stream();
        try {
            int code = api.scanStreamRaw(stream, DATA, null, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            stream.close();
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void closeStreamNoStream(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.closeStreamRaw(null, scanner, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void closeStreamNoScratch(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        DualStreamResult streamResult = api.openStreamRaw(db);
        DualStream stream = streamResult.stream();
        try {
            int code = api.closeStreamRaw(stream, null, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void closeStreamNoScratchNoCallback(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        DualStreamResult streamResult = api.openStreamRaw(db);
        DualStream stream = streamResult.stream();
        try {
            int code = api.closeStreamRaw(stream, null, null);
            assertThat(code).isEqualTo(api.success());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void closeStreamNoMatchNoStream(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.closeStreamRaw(null, scanner, null);
            assertThat(code).isNotEqualTo(api.success());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void changeStreamContext(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void resetStreamNoId(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.resetStreamRaw(null, scanner, DUMMY);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void resetStreamNoScratch(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        DualStreamResult streamResult = api.openStreamRaw(db);
        DualStream stream = streamResult.stream();
        try {
            int code = api.resetStreamRaw(stream, null, DUMMY);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            stream.close();
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void copyStreamNoFromId(DualApi api) {
        DualStream[] to = new DualStream[1];
        int code = api.copyStreamRaw(to, null);
        assertThat(code).isEqualTo(api.invalid());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void copyStreamNoToId(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        DualStreamResult streamResult = api.openStreamRaw(db);
        DualStream stream = streamResult.stream();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.copyStreamRaw(null, stream);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            stream.close();
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void resetAndCopyStreamNoToId(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        DualStreamResult streamResult = api.openStreamRaw(db);
        DualStream stream = streamResult.stream();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.resetAndCopyStreamRaw(null, stream, scanner, null);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            stream.close();
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void resetAndCopyStreamNoFromId(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        DualStreamResult streamResult = api.openStreamRaw(db);
        DualStream stream = streamResult.stream();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.resetAndCopyStreamRaw(stream, null, scanner, null);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            stream.close();
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void resetAndCopyStreamSameToId(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        DualStreamResult streamResult = api.openStreamRaw(db);
        DualStream stream = streamResult.stream();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.resetAndCopyStreamRaw(stream, stream, scanner, null);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            stream.close();
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void resetAndCopyStreamNoCallbackOrScratch(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        DualStreamResult stream1Result = api.openStreamRaw(db);
        DualStream stream1 = stream1Result.stream();
        DualStreamResult stream2Result = api.openStreamRaw(db);
        DualStream stream2 = stream2Result.stream();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.resetAndCopyStreamRaw(stream2, stream1, null, null);
            assertThat(code).isEqualTo(api.success());
            api.closeStreamRaw(stream2, scanner, null);
            api.closeStreamRaw(stream1, scanner, null);
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void resetAndCopyStreamNoScratch(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        DualStreamResult stream1Result = api.openStreamRaw(db);
        DualStream stream1 = stream1Result.stream();
        DualStreamResult stream2Result = api.openStreamRaw(db);
        DualStream stream2 = stream2Result.stream();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.resetAndCopyStreamRaw(stream2, stream1, null, DUMMY);
            assertThat(code).isEqualTo(api.invalid());
            api.closeStreamRaw(stream2, scanner, null);
            api.closeStreamRaw(stream1, scanner, null);
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void resetAndCopyStreamDiffDb(DualApi api) {
        DualDatabase db1 = api.compileRaw("foobar", 0, api.modeStream()).database();
        DualDatabase db2 = api.compileRaw("barfoo", 0, api.modeStream()).database();
        DualStreamResult stream1Result = api.openStreamRaw(db1);
        DualStream stream1 = stream1Result.stream();
        DualStreamResult stream2Result = api.openStreamRaw(db2);
        DualStream stream2 = stream2Result.stream();
        try (DualScanner scanner = api.allocScratchRaw(db1).scratch()) {
            int code = api.resetAndCopyStreamRaw(stream2, stream1, scanner, null);
            assertThat(code).isEqualTo(api.invalid());
            api.closeStreamRaw(stream2, scanner, null);
            api.closeStreamRaw(stream1, scanner, null);
        } finally {
            db1.close();
            db2.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanBlockNoDatabase(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeBlock()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanRaw(scanner, null, DATA, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void scanBlockBrokenDatabaseMagic(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void scanBlockBrokenDatabaseVersion(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void scanBlockBrokenDatabaseBytecode(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanBlockStreamingDatabase(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanRaw(scanner, db, DATA, DUMMY);
            assertThat(code).isEqualTo(api.dbModeError());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanBlockVectoredDatabase(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeVectored()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanRaw(scanner, db, DATA, DUMMY);
            assertThat(code).isEqualTo(api.dbModeError());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanBlockNoData(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeBlock()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanRaw(scanner, db, null, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanBlockNoScratch(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeBlock()).database();
        try {
            int code = api.scanRaw(null, db, DATA, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanBlockNoHandler(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeBlock()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanRaw(scanner, db, DATA, null);
            assertThat(code).isEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanVectorNoDatabase(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeVectored()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanVectorRaw(scanner, null, VECTOR_DATA, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void scanVectorBrokenDatabaseMagic(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void scanVectorBrokenDatabaseVersion(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void scanVectorBrokenDatabaseBytecode(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanVectorStreamingDatabase(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanVectorRaw(scanner, db, VECTOR_DATA, DUMMY);
            assertThat(code).isEqualTo(api.dbModeError());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanVectorBlockDatabase(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeBlock()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanVectorRaw(scanner, db, VECTOR_DATA, DUMMY);
            assertThat(code).isEqualTo(api.dbModeError());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanVectorNoDataArray(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeVectored()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanVectorRaw(scanner, db, null, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanVectorNoDataBlock(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeVectored()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanVectorRaw(scanner, db, new byte[][] { DATA, null }, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanVectorNoLenArray(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeVectored()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanVectorNoLenArrayRaw(scanner, db, VECTOR_DATA, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanVectorNoScratch(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeVectored()).database();
        try {
            int code = api.scanVectorRaw(null, db, VECTOR_DATA, DUMMY);
            assertThat(code).isNotEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanVectorNoHandler(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeVectored()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scanVectorRaw(scanner, db, VECTOR_DATA, null);
            assertThat(code).isEqualTo(api.success());
            assertThat(code).isNotEqualTo(api.scanTerminated());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void allocScratchNoDatabase(DualApi api) {
        DualScratchResult result = api.allocScratchRaw(null);
        assertThat(result.code()).isNotEqualTo(api.success());
        assertThat(result.scratch()).isNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void allocScratchNullScratchPtr(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeBlock()).database();
        try {
            int code = api.allocScratchNullOutput(db);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void allocScratchBogusScratch(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void allocScratchBadDatabaseMagic(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void allocScratchBadDatabaseVersion(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void allocScratchBadDatabasePlatform(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void allocScratchBadDatabaseBytecode(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void allocScratchBadDatabaseCrc(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void cloneScratchNoSource(DualApi api) {
        DualScratchResult result = api.cloneScratchRaw(null);
        assertThat(result.code()).isNotEqualTo(api.success());
        assertThat(result.scratch()).isNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void serializeNoDatabase(DualApi api) {
        DualResult<byte[]> result = api.serializeRaw(null);
        assertThat(result.code()).isNotEqualTo(api.success());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void serializeNoBuffer(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeBlock()).database();
        try {
            int code = api.serializeNoBufferRaw(db);
            assertThat(code).isNotEqualTo(api.success());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void serializeNoLength(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeBlock()).database();
        try {
            int code = api.serializeNoLengthRaw(db);
            assertThat(code).isNotEqualTo(api.success());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void streamSizeNoDatabase(DualApi api) {
        DualResult<Long> result = api.getStreamSizeRaw(null);
        assertThat(result.code()).isEqualTo(api.invalid());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void streamSizeBogusDatabase(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void streamSizeBlockDatabase(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeBlock()).database();
        try {
            DualResult<Long> result = api.getStreamSizeRaw(db);
            assertThat(result.code()).isEqualTo(api.dbModeError());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void streamSizeVectoredDatabase(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeVectored()).database();
        try {
            DualResult<Long> result = api.getStreamSizeRaw(db);
            assertThat(result.code()).isEqualTo(api.dbModeError());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void streamSizeRealDatabase(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        try {
            DualResult<Long> result = api.getStreamSizeRaw(db);
            assertThat(result.code()).isEqualTo(api.success());
            assertThat(result.value()).isGreaterThan(0L);
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void streamSizeNoSize(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        try {
            int code = api.streamSizeNullOutput(db);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void databaseSizeNoDatabase(DualApi api) {
        DualResult<Long> result = api.getDatabaseSizeRaw(null);
        assertThat(result.code()).isEqualTo(api.invalid());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void databaseSizeNoSize(DualApi api) {
        DualDatabase db = api.compileRaw("foobar", 0, api.modeStream()).database();
        try {
            int code = api.databaseSizeNullOutput(db);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void databaseSizeBadDb(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void databaseInfoBadDb(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void databaseInfoNullDb(DualApi api) {
        DualResult<String> result = api.getDatabaseInfoRaw(null);
        assertThat(result.code()).isEqualTo(api.invalid());
        assertThat(result.value()).isNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void databaseInfoNullInfo(DualApi api) {
        DualDatabase db = api.compileRaw("hatstand.*(badgerbrush|teakettle)", 0, api.modeBlock()).database();
        try {
            int code = api.databaseInfoNullOutput(db);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void serializedDatabaseSizeBadLen(DualApi api) {
        DualDatabase db = api.compileRaw("hatstand.*(badgerbrush|teakettle)", 0, api.modeBlock()).database();
        try {
            byte[] serialized = api.serializeRaw(db).value();
            DualResult<Long> result = api.getSerializedDatabaseSizeRaw(truncate(serialized, 16));
            assertThat(result.code()).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void serializedDatabaseSizeNoSize(DualApi api) {
        DualDatabase db = api.compileRaw("hatstand.*(badgerbrush|teakettle)", 0, api.modeBlock()).database();
        try {
            byte[] serialized = api.serializeRaw(db).value();
            int code = api.serializedDatabaseSizeNullOutput(serialized);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void serializedDatabaseSizeNoBytes(DualApi api) {
        DualResult<Long> result = api.getSerializedDatabaseSizeRaw(null);
        assertThat(result.code()).isEqualTo(api.invalid());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void serializedDatabaseInfoBadLen(DualApi api) {
        DualDatabase db = api.compileRaw("hatstand.*(badgerbrush|teakettle)", 0, api.modeBlock()).database();
        try {
            byte[] serialized = api.serializeRaw(db).value();
            DualResult<String> result = api.getSerializedDatabaseInfoRaw(truncate(serialized, 16));
            assertThat(result.code()).isEqualTo(api.invalid());
            assertThat(result.value()).isNull();
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void serializedDatabaseInfoNoInfo(DualApi api) {
        DualDatabase db = api.compileRaw("hatstand.*(badgerbrush|teakettle)", 0, api.modeBlock()).database();
        try {
            byte[] serialized = api.serializeRaw(db).value();
            int code = api.serializedDatabaseInfoNullOutput(serialized);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void serializedDatabaseInfoNoBytes(DualApi api) {
        DualResult<String> result = api.getSerializedDatabaseInfoRaw(null);
        assertThat(result.code()).isEqualTo(api.invalid());
        assertThat(result.value()).isNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void deserializeDatabaseNoBytes(DualApi api) {
        DualResult<DualDatabase> result = api.deserializeRaw(null);
        assertThat(result.code()).isEqualTo(api.invalid());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void deserializeDatabaseAtNoBytes(DualApi api) {
        int code = api.deserializeAtRaw(null, null);
        assertThat(code).isEqualTo(api.invalid());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void deserializeDatabaseNoDb(DualApi api) {
        DualDatabase db = api.compileRaw("(foo.*bar){3,}", 0, api.modeBlock()).database();
        try {
            byte[] serialized = api.serializeRaw(db).value();
            DualResult<DualDatabase> result = api.deserializeNullOutputRaw(serialized);
            assertThat(result.code()).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void deserializeDatabaseBadLen(DualApi api) {
        DualDatabase db = api.compileRaw("(foo.*bar){3,}", 0, api.modeBlock()).database();
        try {
            byte[] serialized = api.serializeRaw(db).value();
            DualResult<DualDatabase> result = api.deserializeRaw(truncate(serialized, serialized.length - 1));
            assertThat(result.code()).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void deserializeDatabaseBadLen2(DualApi api) {
        DualDatabase db = api.compileRaw("(foo.*bar){3,}", 0, api.modeBlock()).database();
        try {
            byte[] serialized = api.serializeRaw(db).value();
            byte[] extended = new byte[serialized.length + 1];
            System.arraycopy(serialized, 0, extended, 0, serialized.length);
            DualResult<DualDatabase> result = api.deserializeRaw(extended);
            assertThat(result.code()).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void deserializeDatabaseBadBytes(DualApi api) {
        DualDatabase db = api.compileRaw("(foo.*bar){3,}", 0, api.modeBlock()).database();
        try {
            byte[] serialized = api.serializeRaw(db).value();
            byte[] scribbled = new byte[serialized.length];
            for (int i = 0; i < scribbled.length; i++) {
                scribbled[i] = (byte) 0xff;
            }
            DualResult<DualDatabase> result = api.deserializeRaw(scribbled);
            assertThat(result.code()).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void deserializeDatabaseBadBytes2(DualApi api) {
        DualDatabase db = api.compileRaw("(foo.*bar){3,}", 0, api.modeBlock()).database();
        try {
            byte[] serialized = api.serializeRaw(db).value();
            byte[] scribbled = new byte[serialized.length];
            DualResult<DualDatabase> result = api.deserializeRaw(scribbled);
            assertThat(result.code()).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void deserializeDatabaseAtNoDb(DualApi api) {
        DualDatabase db = api.compileRaw("(foo.*bar){3,}", 0, api.modeBlock()).database();
        try {
            byte[] serialized = api.serializeRaw(db).value();
            int code = api.deserializeAtRaw(serialized, null);
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void deserializeDatabaseAtBadLen(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void deserializeDatabaseAtBadLen2(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void deserializeDatabaseAtBadBytes(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void deserializeDatabaseAtBadBytes2(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scratchSizeNoSize(DualApi api) {
        DualDatabase db = api.compileRaw("foo.*bar$", 0, api.modeStream()).database();
        try (DualScanner scanner = api.allocScratchRaw(db).scratch()) {
            int code = api.scratchSizeNullOutput();
            assertThat(code).isEqualTo(api.invalid());
        } finally {
            db.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scratchSizeNoScratch(DualApi api) {
        DualResult<Long> result = api.getScratchSizeRaw(null);
        assertThat(result.code()).isEqualTo(api.invalid());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void scratchSizeBadScratch(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void cloneBadScratch(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void scanBadScratch(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void scanStreamBadScratch(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void resetStreamBadScratch(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void scanVectorBadScratch(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void scanFreedScratch(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void exprInfoNullExpression(DualApi api) {
        DualResult<String> result = api.expressionInfoRaw(null, EnumSet.noneOf(DualExpressionFlag.class));
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.value()).isNull();
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void exprInfoNullInfoPtr(DualApi api) {
        DualResult<String> result = api.expressionInfoNullInfoRaw("foobar", EnumSet.noneOf(DualExpressionFlag.class));
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void exprInfoNullErrPtr(DualApi api) {
        DualResult<String> result = api.expressionInfoNullErrRaw("foobar", EnumSet.noneOf(DualExpressionFlag.class));
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.value()).isNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void exprExtInfoNullExpression(DualApi api) {
        DualResult<String> result = api.expressionExtInfoRaw(null, EnumSet.noneOf(DualExpressionFlag.class));
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.value()).isNull();
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void exprExtInfoNullInfoPtr(DualApi api) {
        DualResult<String> result = api.expressionExtInfoNullInfoRaw("foobar", EnumSet.noneOf(DualExpressionFlag.class));
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.message()).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void exprExtInfoNullErrPtr(DualApi api) {
        DualResult<String> result = api.expressionExtInfoNullErrRaw("foobar", EnumSet.noneOf(DualExpressionFlag.class));
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.value()).isNull();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void freeDatabaseNull(DualApi api) {
        assertThat(api.freeDatabaseRaw(null)).isEqualTo(api.success());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void freeDatabaseGarbage(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void freeScratchNull(DualApi api) {
        assertThat(api.freeScratchRaw(null)).isEqualTo(api.success());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("CPP_INTERNAL")
    void freeScratchGarbage(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void freeCompileErrorNull(DualApi api) {
        assertThat(api.freeCompileErrorRaw(null)).isEqualTo(api.success());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void multicompileMixHighlander1(DualApi api) {
        int[] flags = {
                api.flagsToBits(EnumSet.of(DualExpressionFlag.SINGLEMATCH)),
                0
        };
        int[] ids = { 30, 30 };
        DualCompileResult result = api.compileRaw(new String[] { "aoo[A-K]", "bar[L-Z]" }, flags, ids, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.compilerError());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void multicompileMixHighlander2(DualApi api) {
        int[] flags = {
                0,
                api.flagsToBits(EnumSet.of(DualExpressionFlag.SINGLEMATCH))
        };
        int[] ids = { 30, 30 };
        DualCompileResult result = api.compileRaw(new String[] { "aoo[A-K]", "bar[L-Z]" }, flags, ids, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.compilerError());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void multicompileNoMixHighlander1(DualApi api) {
        int[] flags = {
                api.flagsToBits(EnumSet.of(DualExpressionFlag.SINGLEMATCH)),
                api.flagsToBits(EnumSet.of(DualExpressionFlag.SINGLEMATCH))
        };
        int[] ids = { 30, 30 };
        DualCompileResult result = api.compileRaw(new String[] { "aoo[A-K]", "bar[L-Z]" }, flags, ids, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        result.database().close();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void multicompileNoMixHighlander2(DualApi api) {
        int[] flags = { 0, 0 };
        int[] ids = { 30, 30 };
        DualCompileResult result = api.compileRaw(new String[] { "aoo[A-K]", "bar[L-Z]" }, flags, ids, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        result.database().close();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void populatePlatformNull(DualApi api) {
        assertThat(api.populatePlatformRaw()).isEqualTo(api.invalid());
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void badModes(DualApi api) {
        int[] badModes = {
                api.modeBlock() | api.modeStream() | api.modeVectored(),
                api.modeBlock() | api.modeStream(),
                api.modeBlock() | api.modeVectored(),
                api.modeStream() | api.modeVectored(),
                api.modeBlock() | api.modeSomHorizonLarge(),
                api.modeBlock() | api.modeSomHorizonMedium(),
                api.modeBlock() | api.modeSomHorizonSmall(),
                api.modeVectored() | api.modeSomHorizonLarge(),
                api.modeVectored() | api.modeSomHorizonMedium(),
                api.modeVectored() | api.modeSomHorizonSmall(),
                api.modeStream() | api.modeSomHorizonLarge() | api.modeSomHorizonMedium() | api.modeSomHorizonSmall(),
                api.modeStream() | api.modeSomHorizonLarge() | api.modeSomHorizonSmall(),
                api.modeStream() | api.modeSomHorizonLarge() | api.modeSomHorizonMedium(),
                api.modeStream() | api.modeSomHorizonMedium() | api.modeSomHorizonSmall()
        };
        for (int mode : badModes) {
            DualCompileResult result = api.compileRaw("foo", 0, mode);
            assertThat(result.code()).isEqualTo(api.compilerError());
            assertThat(result.message()).isNotNull();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("STREAM_COMPRESS")
    void compressStreamNoStream(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("STREAM_COMPRESS")
    void compressStreamNoUsed(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("STREAM_COMPRESS")
    void compressStreamNoBuf(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("STREAM_COMPRESS")
    void compressStreamSmallBuff(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("STREAM_COMPRESS")
    void expandNoDb(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("STREAM_COMPRESS")
    void expandNoTo(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("STREAM_COMPRESS")
    void expandNoBuf(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("STREAM_COMPRESS")
    void expandSmallBuf(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("STREAM_COMPRESS")
    void resetAndExpandNoStream(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("STREAM_COMPRESS")
    void resetAndExpandNoBuf(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("STREAM_COMPRESS")
    void resetAndExpandSmallBuf(DualApi api) {
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Disabled("STREAM_COMPRESS")
    void resetAndExpandNoScratch(DualApi api) {
    }

    private static byte[] truncate(byte[] data, int length) {
        byte[] out = new byte[length];
        System.arraycopy(data, 0, out, 0, Math.min(length, data.length));
        return out;
    }
}
