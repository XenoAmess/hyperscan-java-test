package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualExpressionExt;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;

import java.util.EnumSet;

public final class ExpressionParser {

    private ExpressionParser() {
    }

    public static ParsedExpression parseLine(String line) {
        int hash = line.indexOf('#');
        int colon = line.indexOf(':');
        if (hash < 0 || colon < 0 || hash == 0) {
            throw new IllegalArgumentException("Invalid line: " + line);
        }
        String expr = line.substring(colon + 1, hash).trim();
        String expectedError = line.substring(hash + 1);
        ExpressionSpec spec = parseExpression(expr);
        return new ParsedExpression(spec.pattern(), spec.flags(), spec.ext(), expectedError);
    }

    private static ExpressionSpec parseExpression(String expr) {
        if (expr.isEmpty() || expr.charAt(0) != '/') {
            throw new IllegalArgumentException("Invalid expression: " + expr);
        }
        int lastSlash = expr.lastIndexOf('/');
        if (lastSlash <= 0) {
            throw new IllegalArgumentException("Invalid expression: " + expr);
        }
        String pattern = expr.substring(1, lastSlash);
        String rest = expr.substring(lastSlash + 1);
        EnumSet<DualExpressionFlag> flags = EnumSet.noneOf(DualExpressionFlag.class);
        int i = 0;
        while (i < rest.length() && rest.charAt(i) != '{') {
            char c = rest.charAt(i);
            DualExpressionFlag flag = flagFromChar(c);
            if (flag != null) {
                flags.add(flag);
            }
            i++;
        }
        DualExpressionExt ext = DualExpressionExt.empty();
        if (i < rest.length() && rest.charAt(i) == '{') {
            int close = rest.lastIndexOf('}');
            if (close < i) {
                throw new IllegalArgumentException("Invalid expression: " + expr);
            }
            ext = parseExt(rest.substring(i + 1, close));
        }
        return new ExpressionSpec(pattern, flags, ext);
    }

    private static DualExpressionExt parseExt(String text) {
        long flags = 0;
        long minOffset = 0;
        long maxOffset = -1L;
        long minLength = 0;
        int editDistance = 0;
        int hammingDistance = 0;
        if (!text.isEmpty()) {
            for (String part : text.split(",")) {
                int eq = part.indexOf('=');
                if (eq < 0) {
                    throw new IllegalArgumentException("Invalid ext param: " + part);
                }
                String key = part.substring(0, eq).trim();
                String value = part.substring(eq + 1).trim();
                long num = Long.parseUnsignedLong(value);
                switch (key) {
                    case "min_offset" -> {
                        flags |= 1L;
                        minOffset = num;
                    }
                    case "max_offset" -> {
                        flags |= 2L;
                        maxOffset = num;
                    }
                    case "min_length" -> {
                        flags |= 4L;
                        minLength = num;
                    }
                    case "edit_distance" -> {
                        flags |= 8L;
                        editDistance = (int) num;
                    }
                    case "hamming_distance" -> {
                        flags |= 16L;
                        hammingDistance = (int) num;
                    }
                    default -> throw new IllegalArgumentException("Invalid ext param: " + key);
                }
            }
        }
        return new DualExpressionExt(flags, minOffset, maxOffset, minLength, editDistance, hammingDistance);
    }

    private static DualExpressionFlag flagFromChar(char c) {
        return switch (c) {
            case 'i' -> DualExpressionFlag.CASELESS;
            case 's' -> DualExpressionFlag.DOTALL;
            case 'm' -> DualExpressionFlag.MULTILINE;
            case 'H' -> DualExpressionFlag.SINGLEMATCH;
            case 'V' -> DualExpressionFlag.ALLOWEMPTY;
            case 'W' -> DualExpressionFlag.UCP;
            case '8' -> DualExpressionFlag.UTF8;
            case 'P' -> DualExpressionFlag.PREFILTER;
            case 'L' -> DualExpressionFlag.SOM_LEFTMOST;
            case 'C' -> DualExpressionFlag.COMBINATION;
            case 'Q' -> DualExpressionFlag.QUIET;
            case 'O' -> null;
            default -> throw new IllegalArgumentException("Invalid flag: " + c);
        };
    }

    public record ParsedExpression(String pattern, EnumSet<DualExpressionFlag> flags, DualExpressionExt ext, String expectedError) {
    }

    private record ExpressionSpec(String pattern, EnumSet<DualExpressionFlag> flags, DualExpressionExt ext) {
    }
}
