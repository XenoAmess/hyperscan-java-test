package com.xenoamess.hyperscan.smoke.dual;

import com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader;
import com.xenoamess.hyperscan_panama.util.PatternFilter;
import com.xenoamess.hyperscan_panama.wrapper.ByteMatchEventHandler;
import com.xenoamess.hyperscan_panama.wrapper.CompileErrorException;
import com.xenoamess.hyperscan_panama.wrapper.Expression;
import com.xenoamess.hyperscan_panama.wrapper.ExpressionFlag;
import com.xenoamess.hyperscan_panama.wrapper.Match;
import com.xenoamess.hyperscan_panama.wrapper.StringMatchEventHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PanamaAdapter implements DualApi {

    static {
        HyperscanNativeLoader.load();
    }

    @Override
    public DualExpression createExpression(String pattern, EnumSet<DualExpressionFlag> flags, Integer id) {
        return new DualExpression(pattern, flags, id);
    }

    @Override
    public DualDatabase compileDatabase(List<DualExpression> expressions) {
        List<Expression> panamaExpressions = new ArrayList<>();
        for (DualExpression expr : expressions) {
            panamaExpressions.add(toPanamaExpression(expr));
        }
        try {
            return new PanamaDatabase(com.xenoamess.hyperscan_panama.wrapper.Database.compile(panamaExpressions));
        } catch (CompileErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualDatabase compileDatabase(DualExpression expression) {
        try {
            return new PanamaDatabase(com.xenoamess.hyperscan_panama.wrapper.Database.compile(toPanamaExpression(expression)));
        } catch (CompileErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualDatabase compileDatabase(DualExpression... expressions) {
        try {
            List<Expression> panamaExpressions = new ArrayList<>();
            for (DualExpression expr : expressions) {
                panamaExpressions.add(toPanamaExpression(expr));
            }
            return new PanamaDatabase(com.xenoamess.hyperscan_panama.wrapper.Database.compile(panamaExpressions));
        } catch (CompileErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualExpression getExpression(DualDatabase database, int id) {
        PanamaDatabase db = (PanamaDatabase) database;
        try {
            Method method = com.xenoamess.hyperscan_panama.wrapper.Database.class.getDeclaredMethod("getExpression", int.class);
            method.setAccessible(true);
            Expression expr = (Expression) method.invoke(db.database, id);
            return expr == null ? null : toDualExpression(expr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualScanner createScanner() {
        return new PanamaScanner(new com.xenoamess.hyperscan_panama.wrapper.Scanner());
    }

    @Override
    public void allocScratch(DualScanner scanner, DualDatabase database) {
        PanamaScanner s = (PanamaScanner) scanner;
        PanamaDatabase db = (PanamaDatabase) database;
        s.scanner.allocScratch(db.database);
    }

    @Override
    public List<DualMatch> scan(DualScanner scanner, DualDatabase database, String input) {
        PanamaScanner s = (PanamaScanner) scanner;
        PanamaDatabase db = (PanamaDatabase) database;
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
        PanamaScanner s = (PanamaScanner) scanner;
        PanamaDatabase db = (PanamaDatabase) database;
        s.scanner.scan(db.database, input, new StringMatchEventHandler() {
            @Override
            public boolean onMatch(Expression expression, long from, long to) {
                return handler.onMatch(toDualExpression(expression), from, to);
            }
        });
    }

    @Override
    public void scan(DualScanner scanner, DualDatabase database, byte[] input, DualByteMatchHandler handler) {
        PanamaScanner s = (PanamaScanner) scanner;
        PanamaDatabase db = (PanamaDatabase) database;
        s.scanner.scan(db.database, input, new ByteMatchEventHandler() {
            @Override
            public boolean onMatch(Expression expression, long from, long to) {
                return handler.onMatch(toDualExpression(expression), from, to);
            }
        });
    }

    @Override
    public boolean hasMatch(DualScanner scanner, DualDatabase database, String input) {
        PanamaScanner s = (PanamaScanner) scanner;
        PanamaDatabase db = (PanamaDatabase) database;
        return s.scanner.hasMatch(db.database, input);
    }

    @Override
    public boolean hasMatch(DualScanner scanner, DualDatabase database, byte[] input) {
        PanamaScanner s = (PanamaScanner) scanner;
        PanamaDatabase db = (PanamaDatabase) database;
        return s.scanner.hasMatch(db.database, input);
    }

    @Override
    public byte[] serialize(DualDatabase database) {
        PanamaDatabase db = (PanamaDatabase) database;
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
            return new PanamaDatabase(com.xenoamess.hyperscan_panama.wrapper.Database.load(new ByteArrayInputStream(data)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeScanner(DualScanner scanner) {
        try {
            ((PanamaScanner) scanner).scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeDatabase(DualDatabase database) {
        ((PanamaDatabase) database).database.close();
    }

    @Override
    public long getDatabaseSize(DualDatabase database) {
        return ((PanamaDatabase) database).database.getSize();
    }

    @Override
    public long getScannerSize(DualScanner scanner) {
        return ((PanamaScanner) scanner).scanner.getSize();
    }

    @Override
    public String getVersion() {
        return com.xenoamess.hyperscan_panama.wrapper.Scanner.getVersion();
    }

    @Override
    public String getPlatform() {
        return HyperscanNativeLoader.selectPlatform();
    }

    @Override
    public boolean validate(DualExpression expression) {
        return toPanamaExpression(expression).validate().isValid();
    }

    @Override
    public String getValidationError(DualExpression expression) {
        return toPanamaExpression(expression).validate().getErrorMessage();
    }

    @Override
    public DualPatternFilter createPatternFilter(List<Pattern> patterns) {
        try {
            return new PanamaPatternFilter(new PatternFilter(patterns));
        } catch (CompileErrorException e) {
            throw new RuntimeException(e);
        }
    }

    private static Expression toPanamaExpression(DualExpression expr) {
        return new Expression(expr.pattern(), toPanamaFlags(expr.flags()), expr.id());
    }

    private static EnumSet<ExpressionFlag> toPanamaFlags(EnumSet<DualExpressionFlag> flags) {
        EnumSet<ExpressionFlag> result = EnumSet.noneOf(ExpressionFlag.class);
        for (DualExpressionFlag flag : flags) {
            result.add(toPanamaFlag(flag));
        }
        return result;
    }

    private static ExpressionFlag toPanamaFlag(DualExpressionFlag flag) {
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
            flags.add(fromPanamaFlag(flag));
        }
        return new DualExpression(expr.getExpression(), flags, expr.getId());
    }

    private static DualExpressionFlag fromPanamaFlag(ExpressionFlag flag) {
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

    private record PanamaDatabase(com.xenoamess.hyperscan_panama.wrapper.Database database) implements DualDatabase {
        @Override
        public long getSize() {
            return database.getSize();
        }

        @Override
        public void close() {
            database.close();
        }
    }

    private record PanamaScanner(com.xenoamess.hyperscan_panama.wrapper.Scanner scanner) implements DualScanner {
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

    private record PanamaPatternFilter(PatternFilter filter) implements DualPatternFilter {
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
