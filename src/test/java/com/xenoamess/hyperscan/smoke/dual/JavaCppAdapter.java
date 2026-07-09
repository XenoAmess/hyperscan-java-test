package com.xenoamess.hyperscan.smoke.dual;

import com.gliwka.hyperscan.jni.HyperscanNativeLoader;
import com.gliwka.hyperscan.util.PatternFilter;
import com.gliwka.hyperscan.wrapper.ByteMatchEventHandler;
import com.gliwka.hyperscan.wrapper.CompileErrorException;
import com.gliwka.hyperscan.wrapper.Expression;
import com.gliwka.hyperscan.wrapper.ExpressionFlag;
import com.gliwka.hyperscan.wrapper.Match;
import com.gliwka.hyperscan.wrapper.StringMatchEventHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaCppAdapter implements DualApi {

    static {
        HyperscanNativeLoader.load();
    }

    @Override
    public DualExpression createExpression(String pattern, EnumSet<DualExpressionFlag> flags, Integer id) {
        return new DualExpression(pattern, flags, id);
    }

    @Override
    public DualDatabase compileDatabase(List<DualExpression> expressions) {
        List<Expression> javaCppExpressions = new ArrayList<>();
        for (DualExpression expr : expressions) {
            javaCppExpressions.add(toJavaCppExpression(expr));
        }
        try {
            return new JavaCppDatabase(com.gliwka.hyperscan.wrapper.Database.compile(javaCppExpressions));
        } catch (CompileErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualDatabase compileDatabase(DualExpression expression) {
        try {
            return new JavaCppDatabase(com.gliwka.hyperscan.wrapper.Database.compile(toJavaCppExpression(expression)));
        } catch (CompileErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualDatabase compileDatabase(DualExpression... expressions) {
        try {
            List<Expression> javaCppExpressions = new ArrayList<>();
            for (DualExpression expr : expressions) {
                javaCppExpressions.add(toJavaCppExpression(expr));
            }
            return new JavaCppDatabase(com.gliwka.hyperscan.wrapper.Database.compile(javaCppExpressions));
        } catch (CompileErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualExpression getExpression(DualDatabase database, int id) {
        JavaCppDatabase db = (JavaCppDatabase) database;
        try {
            Method method = com.gliwka.hyperscan.wrapper.Database.class.getDeclaredMethod("getExpression", int.class);
            method.setAccessible(true);
            Expression expr = (Expression) method.invoke(db.database, id);
            return expr == null ? null : toDualExpression(expr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualScanner createScanner() {
        return new JavaCppScanner(new com.gliwka.hyperscan.wrapper.Scanner());
    }

    @Override
    public void allocScratch(DualScanner scanner, DualDatabase database) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        JavaCppDatabase db = (JavaCppDatabase) database;
        s.scanner.allocScratch(db.database);
    }

    @Override
    public List<DualMatch> scan(DualScanner scanner, DualDatabase database, String input) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        JavaCppDatabase db = (JavaCppDatabase) database;
        List<Match> matches = s.scanner.scan(db.database, input);
        List<DualMatch> result = new ArrayList<>();
        for (Match m : matches) {
            result.add(new DualMatch(
                    toDualExpression(m.getMatchedExpression()),
                    m.getMatchedExpression().getId(),
                    m.getStartPosition(),
                    m.getEndPosition(),
                    m.getMatchedString()
            ));
        }
        return result;
    }

    @Override
    public void scan(DualScanner scanner, DualDatabase database, String input, DualStringMatchHandler handler) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        JavaCppDatabase db = (JavaCppDatabase) database;
        s.scanner.scan(db.database, input, new StringMatchEventHandler() {
            @Override
            public boolean onMatch(Expression expression, long from, long to) {
                return handler.onMatch(toDualExpression(expression), from, to);
            }
        });
    }

    @Override
    public void scan(DualScanner scanner, DualDatabase database, byte[] input, DualByteMatchHandler handler) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        JavaCppDatabase db = (JavaCppDatabase) database;
        s.scanner.scan(db.database, input, new ByteMatchEventHandler() {
            @Override
            public boolean onMatch(Expression expression, long from, long to) {
                return handler.onMatch(toDualExpression(expression), from, to);
            }
        });
    }

    @Override
    public boolean hasMatch(DualScanner scanner, DualDatabase database, String input) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        JavaCppDatabase db = (JavaCppDatabase) database;
        return s.scanner.hasMatch(db.database, input);
    }

    @Override
    public boolean hasMatch(DualScanner scanner, DualDatabase database, byte[] input) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        JavaCppDatabase db = (JavaCppDatabase) database;
        return s.scanner.hasMatch(db.database, input);
    }

    @Override
    public byte[] serialize(DualDatabase database) {
        JavaCppDatabase db = (JavaCppDatabase) database;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            db.database.save(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

    @Override
    public DualDatabase deserialize(byte[] data) {
        try {
            return new JavaCppDatabase(com.gliwka.hyperscan.wrapper.Database.load(new ByteArrayInputStream(data)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeScanner(DualScanner scanner) {
        try {
            ((JavaCppScanner) scanner).scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeDatabase(DualDatabase database) {
        ((JavaCppDatabase) database).database.close();
    }

    @Override
    public long getDatabaseSize(DualDatabase database) {
        return ((JavaCppDatabase) database).database.getSize();
    }

    @Override
    public long getScannerSize(DualScanner scanner) {
        return ((JavaCppScanner) scanner).scanner.getSize();
    }

    @Override
    public String getVersion() {
        return com.gliwka.hyperscan.wrapper.Scanner.getVersion();
    }

    @Override
    public String getPlatform() {
        return HyperscanNativeLoader.selectPlatform();
    }

    @Override
    public boolean validate(DualExpression expression) {
        return toJavaCppExpression(expression).validate().isValid();
    }

    @Override
    public String getValidationError(DualExpression expression) {
        return toJavaCppExpression(expression).validate().getErrorMessage();
    }

    @Override
    public DualPatternFilter createPatternFilter(List<Pattern> patterns) {
        try {
            return new JavaCppPatternFilter(new PatternFilter(patterns));
        } catch (CompileErrorException e) {
            throw new RuntimeException(e);
        }
    }

    private static Expression toJavaCppExpression(DualExpression expr) {
        return new Expression(expr.pattern(), toJavaCppFlags(expr.flags()), expr.id());
    }

    private static EnumSet<ExpressionFlag> toJavaCppFlags(EnumSet<DualExpressionFlag> flags) {
        EnumSet<ExpressionFlag> result = EnumSet.noneOf(ExpressionFlag.class);
        for (DualExpressionFlag flag : flags) {
            result.add(toJavaCppFlag(flag));
        }
        return result;
    }

    private static ExpressionFlag toJavaCppFlag(DualExpressionFlag flag) {
        return switch (flag) {
            case CASELESS -> ExpressionFlag.CASELESS;
            case DOTALL -> ExpressionFlag.DOTALL;
            case MULTILINE -> ExpressionFlag.MULTILINE;
            case SINGLEMATCH -> ExpressionFlag.SINGLEMATCH;
            case ALLOWEMPTY -> ExpressionFlag.ALLOWEMPTY;
            case UTF8 -> ExpressionFlag.UTF8;
            case PREFILTER -> ExpressionFlag.PREFILTER;
            case SOM_LEFTMOST -> ExpressionFlag.SOM_LEFTMOST;
        };
    }

    private static DualExpression toDualExpression(Expression expr) {
        EnumSet<DualExpressionFlag> flags = EnumSet.noneOf(DualExpressionFlag.class);
        for (ExpressionFlag flag : expr.getFlags()) {
            if (flag == ExpressionFlag.NO_FLAG) {
                continue;
            }
            flags.add(fromJavaCppFlag(flag));
        }
        return new DualExpression(expr.getExpression(), flags, expr.getId());
    }

    private static DualExpressionFlag fromJavaCppFlag(ExpressionFlag flag) {
        return switch (flag) {
            case CASELESS -> DualExpressionFlag.CASELESS;
            case DOTALL -> DualExpressionFlag.DOTALL;
            case MULTILINE -> DualExpressionFlag.MULTILINE;
            case SINGLEMATCH -> DualExpressionFlag.SINGLEMATCH;
            case ALLOWEMPTY -> DualExpressionFlag.ALLOWEMPTY;
            case UTF8 -> DualExpressionFlag.UTF8;
            case PREFILTER -> DualExpressionFlag.PREFILTER;
            case SOM_LEFTMOST -> DualExpressionFlag.SOM_LEFTMOST;
            default -> throw new IllegalArgumentException("Unsupported flag: " + flag);
        };
    }

    private record JavaCppDatabase(com.gliwka.hyperscan.wrapper.Database database) implements DualDatabase {
        @Override
        public long getSize() {
            return database.getSize();
        }

        @Override
        public void close() {
            database.close();
        }
    }

    private record JavaCppScanner(com.gliwka.hyperscan.wrapper.Scanner scanner) implements DualScanner {
        @Override
        public long getSize() {
            return scanner.getSize();
        }

        @Override
        public void close() {
            try {
                scanner.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private record JavaCppPatternFilter(PatternFilter filter) implements DualPatternFilter {
        @Override
        public List<Matcher> filter(String input) {
            return filter.filter(input);
        }

        @Override
        public void close() {
            try {
                filter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
