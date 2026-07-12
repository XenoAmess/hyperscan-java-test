package com.xenoamess.hyperscan.smoke.benchmarks.jmh;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public final class BenchmarkData {

    private BenchmarkData() {
    }

    public static List<DualExpression> buildCrossPlatformExpressions(DualApi api, int count) {
        List<DualExpression> expressions = new ArrayList<>(count);
        expressions.add(api.createExpression("[0-9]{3}-[0-9]{2}-[0-9]{4}", DualExpressionFlag.SOM_LEFTMOST, 0));
        expressions.add(api.createExpression("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", DualExpressionFlag.SOM_LEFTMOST, 1));
        expressions.add(api.createExpression("https?://[^\\s]+", DualExpressionFlag.SOM_LEFTMOST, 2));
        expressions.add(api.createExpression("\\bERROR\\b", DualExpressionFlag.SOM_LEFTMOST, 3));
        expressions.add(api.createExpression("\\bWARNING\\b", DualExpressionFlag.SOM_LEFTMOST, 4));
        Random random = new Random(2027);
        for (int i = 5; i < count; i++) {
            String token = String.format("%08x", random.nextInt());
            expressions.add(api.createExpression(Pattern.quote("TOKEN_" + token), DualExpressionFlag.SOM_LEFTMOST, i));
        }
        return expressions;
    }

    public static String buildCrossPlatformInput(int size, int seedCount) {
        Random random = new Random(2026);
        Random tokenRandom = new Random(2027);
        StringBuilder sb = new StringBuilder(size);
        String[] fragments = {
                "Contact support@example.com for help. ",
                "SSN 123-45-6789 is fake. ",
                "Visit https://example.com/page for more. ",
                "ERROR: disk full. ",
                "WARNING: high latency. ",
        };
        while (sb.length() < size) {
            if (random.nextInt(10) == 0 && seedCount > 0) {
                sb.append("TOKEN_").append(String.format("%08x", tokenRandom.nextInt())).append(' ');
                seedCount--;
            } else {
                sb.append(fragments[random.nextInt(fragments.length)]);
            }
        }
        return sb.toString();
    }

    public static List<DualExpression> buildMixedExpressions(DualApi api, int count) {
        return buildCrossPlatformExpressions(api, count);
    }

    public static String buildMixedInput(int size, int seedCount) {
        return buildCrossPlatformInput(size, seedCount);
    }

    public static List<DualExpression> generateLiteralExpressions(DualApi api, int count) {
        List<DualExpression> expressions = new ArrayList<>(count);
        Random random = new Random(2028);
        for (int i = 0; i < count; i++) {
            String literal = "LIT_" + String.format("%08x", random.nextInt());
            expressions.add(api.createExpression(literal, DualExpressionFlag.SOM_LEFTMOST, i));
        }
        return expressions;
    }

    public static String buildHaystackWithMatches(List<DualExpression> expressions) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expressions.size(); i++) {
            if (i % 5 == 0) {
                sb.append(expressions.get(i).pattern()).append(' ');
            } else {
                sb.append("noise_").append(i).append(' ');
            }
        }
        return sb.toString();
    }

    public static String generateLongText(int length) {
        Random random = new Random(12345);
        StringBuilder sb = new StringBuilder(length);
        String[] words = {"hello", "world", "foo", "bar", "1234", "test", "data", "error", "https://example.com/page"};
        while (sb.length() < length) {
            sb.append(words[random.nextInt(words.length)]).append(' ');
            if (random.nextInt(20) == 0) {
                sb.append("user@example.com ");
            }
            if (random.nextInt(50) == 0) {
                sb.append("ERROR: disk full ");
            }
            if (random.nextInt(100) == 0) {
                sb.append("123-45-6789 ");
            }
        }
        return sb.toString();
    }

    public static String sanitizePattern(String pattern) {
        return pattern.replaceAll("[^A-Za-z0-9]", "_");
    }
}
