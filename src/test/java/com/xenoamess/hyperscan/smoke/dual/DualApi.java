package com.xenoamess.hyperscan.smoke.dual;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.regex.Pattern;

public interface DualApi {

    @FunctionalInterface
    interface DualAllocator {
        long allocate(long size);
    }

    @FunctionalInterface
    interface DualFree {
        void free(long address);
    }

    void setAllocator(DualAllocator alloc, DualFree free);

    void setDatabaseAllocator(DualAllocator alloc, DualFree free);

    void setMiscAllocator(DualAllocator alloc, DualFree free);

    void setScratchAllocator(DualAllocator alloc, DualFree free);

    void setStreamAllocator(DualAllocator alloc, DualFree free);

    default void resetAllocators() {
        setAllocator(null, null);
        setDatabaseAllocator(null, null);
        setMiscAllocator(null, null);
        setScratchAllocator(null, null);
        setStreamAllocator(null, null);
    }

    DualExpression createExpression(String pattern, java.util.EnumSet<DualExpressionFlag> flags, Integer id);

    default DualExpression createExpression(String pattern, java.util.EnumSet<DualExpressionFlag> flags) {
        return createExpression(pattern, flags, null);
    }

    default DualExpression createExpression(String pattern, DualExpressionFlag flag, Integer id) {
        return createExpression(pattern, java.util.EnumSet.of(flag), id);
    }

    default DualExpression createExpression(String pattern, Integer id) {
        return createExpression(pattern, java.util.EnumSet.noneOf(DualExpressionFlag.class), id);
    }

    default DualExpression createExpression(String pattern, DualExpressionFlag flag) {
        return createExpression(pattern, java.util.EnumSet.of(flag), null);
    }

    default DualExpression createExpression(String pattern) {
        return createExpression(pattern, java.util.EnumSet.noneOf(DualExpressionFlag.class), null);
    }

    default DualDatabase compileDatabase(List<DualExpression> expressions) {
        return compileDatabase(expressions, DualMode.BLOCK);
    }

    DualDatabase compileDatabase(List<DualExpression> expressions, DualMode mode);

    DualDatabase compileDatabase(DualExpression expression, DualMode mode);

    default DualDatabase compileDatabase(DualExpression expression) {
        return compileDatabase(expression, DualMode.BLOCK);
    }

    DualDatabase compileDatabase(DualExpression[] expressions, DualMode mode);

    default DualDatabase compileDatabase(DualExpression... expressions) {
        return compileDatabase(expressions, DualMode.BLOCK);
    }

    DualExpression getExpression(DualDatabase database, int id);

    DualScanner createScanner();

    void allocScratch(DualScanner scanner, DualDatabase database);

    List<DualMatch> scan(DualScanner scanner, DualDatabase database, String input);

    void scan(DualScanner scanner, DualDatabase database, String input, DualStringMatchHandler handler);

    void scan(DualScanner scanner, DualDatabase database, byte[] input, DualByteMatchHandler handler);

    void scan(DualScanner scanner, DualDatabase database, ByteBuffer input, DualByteMatchHandler handler);

    boolean hasMatch(DualScanner scanner, DualDatabase database, String input);

    boolean hasMatch(DualScanner scanner, DualDatabase database, byte[] input);

    DualStream openStream(DualDatabase database);

    void scanStream(DualScanner scanner, DualStream stream, byte[] input, DualByteMatchHandler handler);

    void scanStream(DualScanner scanner, DualStream stream, ByteBuffer input, DualByteMatchHandler handler);

    void closeStream(DualScanner scanner, DualStream stream, DualByteMatchHandler handler);

    void scanVector(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler);

    String getDatabaseInfo(DualDatabase database);

    String getSerializedDatabaseInfo(byte[] data);

    byte[] serialize(DualDatabase database);

    DualDatabase deserialize(byte[] data);

    void closeScanner(DualScanner scanner);

    void closeDatabase(DualDatabase database);

    long getDatabaseSize(DualDatabase database);

    long getScannerSize(DualScanner scanner);

    String getVersion();

    String getPlatform();

    boolean validate(DualExpression expression);

    String getValidationError(DualExpression expression);

    DualPatternFilter createPatternFilter(List<Pattern> patterns);

    int flagsToBits(java.util.EnumSet<DualExpressionFlag> flags);

    int success();

    int invalid();

    int noMem();

    int badAlloc();

    int compilerError();

    int dbVersionError();

    int dbModeError();

    int dbPlatformError();

    int insufficientSpace();

    int scanTerminated();

    int scratchInUse();

    int validPlatformRaw();

    int badAlign();

    int modeBlock();

    int modeStream();

    int modeVectored();

    int modeSomHorizonLarge();

    int modeSomHorizonMedium();

    int modeSomHorizonSmall();

    long offsetPastHorizon();

    DualCompileResult compileRaw(String pattern, int flags, int mode);

    default DualCompileResult compileRaw(String pattern, java.util.EnumSet<DualExpressionFlag> flags, int mode) {
        return compileRaw(pattern, flagsToBits(flags), mode);
    }

    DualCompileResult compileRaw(String pattern, int flags, int mode, DualPlatformInfo platform);

    DualCompileResult compileExtRaw(String pattern, int flags, DualExpressionExt ext, int mode);

    default DualCompileResult compileExtRaw(String pattern, java.util.EnumSet<DualExpressionFlag> flags, DualExpressionExt ext, int mode) {
        return compileExtRaw(pattern, flagsToBits(flags), ext, mode);
    }

    DualCompileResult compileRaw(DualExpression expression, int mode);

    DualCompileResult compileRaw(DualExpression[] expressions, int mode);

    DualCompileResult compileRaw(List<DualExpression> expressions, int mode);

    DualCompileResult compileRaw(String[] patterns, int[] flags, int[] ids, int mode);

    DualCompileResult compileNullOutputRaw(String pattern, int flags, int mode);

    DualCompileResult compileMultiNullOutputRaw(String[] patterns, int[] flags, int[] ids, int mode);

    DualResult<DualDatabase> deserializeRaw(byte[] data);

    DualResult<DualDatabase> deserializeNullOutputRaw(byte[] data);

    int deserializeAtRaw(byte[] data, DualDatabase database);

    DualDatabase allocateRawDatabase(long size);

    DualDatabase offsetRawDatabase(DualDatabase database, long offset);

    DualResult<byte[]> serializeRaw(DualDatabase database);

    int serializeNoBufferRaw(DualDatabase database);

    int serializeNoLengthRaw(DualDatabase database);

    DualResult<String> getDatabaseInfoRaw(DualDatabase database);

    DualResult<String> getSerializedDatabaseInfoRaw(byte[] data);

    DualResult<Long> getDatabaseSizeRaw(DualDatabase database);

    DualResult<Long> getSerializedDatabaseSizeRaw(byte[] data);

    int serializedDatabaseSizeNullOutput(byte[] data);

    int serializedDatabaseInfoNullOutput(byte[] data);

    DualResult<Long> getStreamSizeRaw(DualDatabase database);

    int streamSizeNullOutput(DualDatabase database);

    DualResult<Long> getScratchSizeRaw(DualScanner scanner);

    DualScratchResult allocScratchRaw(DualDatabase database);

    DualScratchResult allocScratchRaw(DualDatabase database, DualScanner existingScratch);

    int allocScratchNullOutput(DualDatabase database);

    int scratchSizeNullOutput();

    DualScratchResult cloneScratchRaw(DualScanner source);

    DualStreamResult openStreamRaw(DualDatabase database);

    DualScanner getStreamScratch(DualStream stream);

    int openStreamNullOutput(DualDatabase database);

    int scanStreamRaw(DualStream stream, byte[] input, DualScanner scanner, DualByteMatchHandler handler);

    int closeStreamRaw(DualStream stream, DualScanner scanner, DualByteMatchHandler handler);

    int resetStreamRaw(DualStream stream, DualScanner scanner, DualByteMatchHandler handler);

    int copyStreamRaw(DualStream[] to, DualStream from);

    int resetAndCopyStreamRaw(DualStream to, DualStream from, DualScanner scanner, DualByteMatchHandler handler);

    int scanRaw(DualScanner scanner, DualDatabase database, byte[] input, DualByteMatchHandler handler);

    int scanVectorRaw(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler);

    int scanVectorNoLenArrayRaw(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler);

    int databaseSizeNullOutput(DualDatabase database);

    int databaseInfoNullOutput(DualDatabase database);

    int freeDatabaseRaw(DualDatabase database);

    int freeScratchRaw(DualScanner scanner);

    int freeCompileErrorRaw(Object compileError);

    DualResult<String> expressionInfoRaw(String pattern, java.util.EnumSet<DualExpressionFlag> flags);

    DualResult<DualExpressionInfo> expressionInfoDataRaw(String pattern, java.util.EnumSet<DualExpressionFlag> flags);

    DualResult<String> expressionInfoNullInfoRaw(String pattern, java.util.EnumSet<DualExpressionFlag> flags);

    DualResult<String> expressionInfoNullErrRaw(String pattern, java.util.EnumSet<DualExpressionFlag> flags);

    DualResult<String> expressionExtInfoRaw(String pattern, java.util.EnumSet<DualExpressionFlag> flags);

    DualResult<DualExpressionInfo> expressionExtInfoDataRaw(String pattern, java.util.EnumSet<DualExpressionFlag> flags, DualExpressionExt ext);

    DualResult<String> expressionExtInfoNullInfoRaw(String pattern, java.util.EnumSet<DualExpressionFlag> flags);

    DualResult<String> expressionExtInfoNullErrRaw(String pattern, java.util.EnumSet<DualExpressionFlag> flags);

    int populatePlatformRaw();
}
