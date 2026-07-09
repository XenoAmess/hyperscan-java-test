package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionExt;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualMode;
import com.xenoamess.hyperscan.smoke.dual.JavaCppAdapter;
import com.xenoamess.hyperscan.smoke.dual.PanamaAdapter;
import com.xenoamess.hyperscan.smoke.util.TestData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BadPatternsTest {

    private static final String[] BAD_PATTERNS = {
            "",
            "a?",
            "a*",
            "(foo)?",
            "(foo)*(bar)*",
            "^arg|(foo)*(bar)*",
            "foo|bar|",
            "a*(b?)*c*",
            "a{0,3}",
            "[\\s\\S]*",
            "[^\\s\\S]*",
            "(ewh|m?uit|f|snmv.g.gx[yofl]|.[^g][hbd])((.h|((y|vypfw|dfg{4}|x+|o.|y{8,}))+|k{9}t|cgp...gsk+)){17,}",
            "fooa{0}",
            "a{4,3}",
            "a{2,1}",
            "a++",
            "a+?+",
            "a??",
            "a?+",
            "?qa",
            "*abc",
            "+abc",
            "^?0",
            "^*0",
            "^+0",
            "^{1,3}0",
            "0$?",
            "0$*",
            "0$+",
            "0${1,3}",
            "[a-z]+(?=;)",
            ".((?!\\x00\\x00)..)*?foo",
            "(?<=bullock|donkey)",
            "(?<!foo)bar",
            "foo^bar",
            "foo$bar",
            "(foo^bar|other)",
            "(foo$bar|other)",
            "$test",
            "test^",
            "foo(a|^)bar",
            "a$^b",
            "(^foo)+bar",
            "foo(bar$)+",
            "a^{3}=",
            "foobar(?>.{3,})bar",
            "\\d++foo",
            "(abc|xyz){2,3}+",
            "^...\u0002.{10,522}([^\u0000])\\1{16}",
            "[]",
            "[]foobar",
            "[`-\\80",
            "[A-\\K]",
            "[[:foo:]]",
            "[[:1234:]]",
            "[[:f\\oo:]]",
            "[[: :]]",
            "[[:...:]]",
            "[[:l\\ower:]]",
            "[[:abc\\:]]",
            "[abc[:x\\]pqr:]]",
            "[[:a\\dz:]]",
            "foo\\g''bar",
            "foo\\g'45'bar",
            "foo\\g'hatstand'bar",
            "foo\\g<>bar",
            "foo\\g<45>bar",
            "foo\\g<teakettle>bar",
            "((?i)rah)\\s+\\1",
            "(?<p1>(?i)rah)\\s+\\k<p1>",
            "(?'p1'(?i)rah)\\s+\\k{p1}",
            "(?P<p1>(?i)rah)\\s+(?P=p1)",
            "(?<p1>(?i)rah)\\s+\\g{p1}",
            "((c(p|p)h{2,}bh.|p|((((cq|j|c|(\\b)|.[^nbgn]|(\\B)[qfh]a)){10,12}|ih|a|mnde[pa].|.g)){5,8})){21,29}",
            "(?(?=[^a-z]*[a-z])\\d{2}-[a-z]{3}-\\d{2}|\\d{2}-\\d{2}-\\d{2)}",
            "(foo",
            "foo)",
            "((foo)",
            "(foo))",
            "/foo(?#comment/",
            "A\\g",
            "A(.*)\\ga",
            "^(a)\\g",
            "^(a)\\g{3",
            "\\g{A",
            "[\\g6666666666]",
            "(^(a|b\\g<-1'c))",
            "^(?<name>a|b\\g'name'c)",
            "^(a|b\\g'1'c)",
            "^(a|b\\g'-1'c)",
            "A((?:A|B(*ACCEPT)|C)D)",
            "(*FAIL)",
            "(*F)",
            "a+(*COMMIT)b",
            "(*PRUNE)",
            "a+(*SKIP)b",
            "\\R",
            "foo\\Kbar",
            "\\Gfoo",
            "(?|(Sat)ur|(Sun))day",
            "foobar\\",
            "(?x)abc(#i)def"
    };

    private static final DualMode[] MODES = { DualMode.BLOCK, DualMode.STREAM };

    static Stream<Arguments> inlineBadPatternProvider() {
        Stream.Builder<Arguments> builder = Stream.builder();
        for (DualApi api : List.of(new JavaCppAdapter(), new PanamaAdapter())) {
            for (String pattern : BAD_PATTERNS) {
                for (DualMode mode : MODES) {
                    builder.add(Arguments.of(api, pattern, mode));
                }
            }
        }
        return builder.build();
    }

    static Stream<Arguments> fileBadPatternProvider() {
        List<ExpressionParser.ParsedExpression> cases = readBadPatternCases("bad_patterns.txt");
        Stream.Builder<Arguments> builder = Stream.builder();
        for (DualApi api : List.of(new JavaCppAdapter(), new PanamaAdapter())) {
            for (ExpressionParser.ParsedExpression parsed : cases) {
                for (DualMode mode : MODES) {
                    builder.add(Arguments.of(api, parsed, mode));
                }
            }
        }
        return builder.build();
    }

    static Stream<Arguments> fileBadPatternFastProvider() {
        List<ExpressionParser.ParsedExpression> cases = readBadPatternCases("bad_patterns_fast.txt");
        Stream.Builder<Arguments> builder = Stream.builder();
        for (DualApi api : List.of(new JavaCppAdapter(), new PanamaAdapter())) {
            for (ExpressionParser.ParsedExpression parsed : cases) {
                for (DualMode mode : MODES) {
                    builder.add(Arguments.of(api, parsed, mode));
                }
            }
        }
        return builder.build();
    }

    private static List<ExpressionParser.ParsedExpression> readBadPatternCases(String resource) {
        String text = TestData.readString(resource);
        List<ExpressionParser.ParsedExpression> result = new ArrayList<>();
        for (String line : text.split("\n")) {
            if (line.isEmpty() || line.charAt(0) == '#') {
                continue;
            }
            ExpressionParser.ParsedExpression parsed = ExpressionParser.parseLine(line);
            if (isUnsupportedCase(parsed)) {
                continue;
            }
            result.add(parsed);
        }
        return result;
    }

    private static boolean isUnsupportedCase(ExpressionParser.ParsedExpression parsed) {
        String error = parsed.expectedError();
        return error.startsWith("Expression is not valid UTF-8.")
                || error.contains("Nested character classes are not supported")
                || error.contains("Character class intersection/subtraction is not supported");
    }

    @ParameterizedTest
    @MethodSource("inlineBadPatternProvider")
    void inlineBadPatternCompileFails(DualApi api, String pattern, DualMode mode) {
        int modeBits = mode == DualMode.BLOCK ? api.modeBlock() : api.modeStream();
        DualCompileResult result = api.compileRaw(pattern, 0, modeBits);
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.database()).isNull();
        assertThat(result.message()).isNotNull();
        assertThat(result.message()).isNotEqualTo("An invalid flag was specified.");
        assertThat(result.message()).isNotEqualTo("Unable to allocate memory.");
        assertThat(result.message()).isNotEqualTo("Internal error.");
        assertThat(result.message()).isNotEqualTo("Match can be raised on EOD");
    }

    @ParameterizedTest
    @MethodSource("fileBadPatternProvider")
    void fileBadPatternCompileFails(DualApi api, ExpressionParser.ParsedExpression parsed, DualMode mode) {
        int modeBits = mode == DualMode.BLOCK ? api.modeBlock() : api.modeStream() | api.modeSomHorizonLarge();
        int flags = api.flagsToBits(parsed.flags());
        DualCompileResult result = api.compileExtRaw(parsed.pattern(), flags, parsed.ext(), modeBits);
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.database()).isNull();
        assertThat(result.message()).isNotNull();
        assertThat(result.message()).isEqualTo(parsed.expectedError());
    }

    @ParameterizedTest
    @MethodSource("fileBadPatternFastProvider")
    void fileBadPatternFastCompileFails(DualApi api, ExpressionParser.ParsedExpression parsed, DualMode mode) {
        int modeBits = mode == DualMode.BLOCK ? api.modeBlock() : api.modeStream() | api.modeSomHorizonLarge();
        int flags = api.flagsToBits(parsed.flags());
        DualCompileResult result = api.compileExtRaw(parsed.pattern(), flags, parsed.ext(), modeBits);
        assertThat(result.code()).isEqualTo(api.compilerError());
        assertThat(result.database()).isNull();
        assertThat(result.message()).isNotNull();
        assertThat(result.message()).isEqualTo(parsed.expectedError());
    }

    @Test
    void resourceLimitLongPattern() {
        for (DualApi api : List.of(new JavaCppAdapter(), new PanamaAdapter())) {
            char[] pattern = new char[16384];
            java.util.Arrays.fill(pattern, 'a');
            DualCompileResult result = api.compileRaw(new String(pattern), api.flagsToBits(EnumSet.of(DualExpressionFlag.DOTALL)), api.modeBlock());
            assertThat(result.code()).isEqualTo(api.compilerError());
            assertThat(result.database()).isNull();
            assertThat(result.message()).isEqualTo("Pattern length exceeds limit.");
        }
    }

    @Test
    void resourceLimitLongPatternInfo() {
        for (DualApi api : List.of(new JavaCppAdapter(), new PanamaAdapter())) {
            char[] pattern = new char[16384];
            java.util.Arrays.fill(pattern, 'a');
            var result = api.expressionInfoRaw(new String(pattern), EnumSet.of(DualExpressionFlag.DOTALL));
            assertThat(result.code()).isEqualTo(api.compilerError());
            assertThat(result.message()).isEqualTo("Pattern length exceeds limit.");
        }
    }

    @Test
    void resourceLimitLongLiteral() {
        for (DualApi api : List.of(new JavaCppAdapter(), new PanamaAdapter())) {
            DualCompileResult result = api.compileRaw("(abcd){4096}", api.flagsToBits(EnumSet.of(DualExpressionFlag.DOTALL)), api.modeBlock());
            assertThat(result.code()).isEqualTo(api.compilerError());
            assertThat(result.database()).isNull();
            assertThat(result.message()).isEqualTo("Resource limit exceeded.");
        }
    }
}
