package com.xenoamess.hyperscan.smoke.dual;

import java.util.List;
import java.util.regex.Pattern;

public interface DualApi {

    DualExpression createExpression(String pattern, java.util.EnumSet<DualExpressionFlag> flags, Integer id);

    default DualExpression createExpression(String pattern, java.util.EnumSet<DualExpressionFlag> flags) {
        return createExpression(pattern, flags, null);
    }

    default DualExpression createExpression(String pattern, DualExpressionFlag flag, Integer id) {
        return createExpression(pattern, java.util.EnumSet.of(flag), id);
    }

    default DualExpression createExpression(String pattern, DualExpressionFlag flag) {
        return createExpression(pattern, java.util.EnumSet.of(flag), null);
    }

    default DualExpression createExpression(String pattern) {
        return createExpression(pattern, java.util.EnumSet.noneOf(DualExpressionFlag.class), null);
    }

    DualDatabase compileDatabase(List<DualExpression> expressions);

    DualDatabase compileDatabase(DualExpression expression);

    DualDatabase compileDatabase(DualExpression... expressions);

    DualExpression getExpression(DualDatabase database, int id);

    DualScanner createScanner();

    void allocScratch(DualScanner scanner, DualDatabase database);

    List<DualMatch> scan(DualScanner scanner, DualDatabase database, String input);

    void scan(DualScanner scanner, DualDatabase database, String input, DualStringMatchHandler handler);

    void scan(DualScanner scanner, DualDatabase database, byte[] input, DualByteMatchHandler handler);

    boolean hasMatch(DualScanner scanner, DualDatabase database, String input);

    boolean hasMatch(DualScanner scanner, DualDatabase database, byte[] input);

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
}
