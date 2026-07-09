package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualScratchResult;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LogicalCombinationTest {

    private record MatchRecord(int to, int id) {
    }

    private static final DualByteMatchHandler DUMMY = (expr, from, to) -> true;

    private static final class CollectingHandler implements DualByteMatchHandler {
        private final List<MatchRecord> matches = new ArrayList<>();

        @Override
        public boolean onMatch(DualExpression expression, long from, long to) {
            matches.add(new MatchRecord((int) to, expression.id()));
            return true;
        }
    }

    private static DualExpression expr(DualApi api, String pattern, int id, EnumSet<DualExpressionFlag> flags) {
        return api.createExpression(pattern, flags, id);
    }

    private static DualDatabase compileBlock(DualApi api, List<DualExpression> expressions) {
        DualCompileResult result = api.compileRaw(expressions, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        return db;
    }

    private static DualDatabase compileStream(DualApi api, List<DualExpression> expressions) {
        DualCompileResult result = api.compileRaw(expressions, api.modeStream());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        return db;
    }

    private static List<MatchRecord> scanBlock(DualApi api, DualDatabase db, String data) {
        DualScratchResult scratchResult = api.allocScratchRaw(db);
        assertThat(scratchResult.code()).isEqualTo(api.success());
        DualScanner scanner = scratchResult.scratch();
        try {
            CollectingHandler handler = new CollectingHandler();
            int result = api.scanRaw(scanner, db, data.getBytes(StandardCharsets.UTF_8), handler);
            assertThat(result).isEqualTo(api.success());
            return handler.matches;
        } finally {
            api.closeScanner(scanner);
        }
    }

    private static List<MatchRecord> scanStream(DualApi api, DualDatabase db, String[] chunks) {
        DualStream stream = api.openStream(db);
        try {
            CollectingHandler handler = new CollectingHandler();
            for (String chunk : chunks) {
                api.scanStream(null, stream, chunk.getBytes(StandardCharsets.UTF_8), handler);
            }
            api.closeStream(null, stream, DUMMY);
            return handler.matches;
        } finally {
            stream.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void singleComb1(DualApi api) {
        String data = "abcdefxxfoobarrrghabcxdefxteakettleeeeexxxxijklmxxdef";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 101, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "def", 102, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "foobar.*gh", 103, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "teakettle{4,10}", 104, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[mMn]", 105, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(101 & 102 & 103) | (104 & !105)", 1001, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(3, 101),
                    new MatchRecord(6, 102),
                    new MatchRecord(18, 103),
                    new MatchRecord(18, 1001),
                    new MatchRecord(21, 101),
                    new MatchRecord(21, 1001),
                    new MatchRecord(25, 102),
                    new MatchRecord(25, 1001),
                    new MatchRecord(38, 104),
                    new MatchRecord(38, 1001),
                    new MatchRecord(39, 104),
                    new MatchRecord(39, 1001),
                    new MatchRecord(48, 105),
                    new MatchRecord(48, 1001),
                    new MatchRecord(53, 102),
                    new MatchRecord(53, 1001));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void singleCombQuietSub1(DualApi api) {
        String data = "abcdefxxfoobarrrghabcxdefxteakettleeeeexxxxijklmxxdef";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 101, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "def", 102, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "foobar.*gh", 103, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "teakettle{4,10}", 104, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "ijkl[mMn]", 105, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(101 & 102 & 103) | (104 & !105)", 1001, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(18, 1001),
                    new MatchRecord(21, 1001),
                    new MatchRecord(25, 1001),
                    new MatchRecord(38, 1001),
                    new MatchRecord(39, 1001),
                    new MatchRecord(48, 105),
                    new MatchRecord(48, 1001),
                    new MatchRecord(53, 1001));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void multiCombQuietSub1(DualApi api) {
        String data = "abcdefxxfoobarrrghabcxdefxteakettleeeeexxxxijklmxxdef";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 101, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "def", 102, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "foobar.*gh", 103, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "teakettle{4,10}", 104, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "ijkl[mMn]", 105, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(101 & 102 & 103) | (104 & !105)", 1001, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "!101 & 102", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "!(!101 | 102)", 1003, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "101 & !102", 1004, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(3, 1003),
                    new MatchRecord(3, 1004),
                    new MatchRecord(18, 1001),
                    new MatchRecord(21, 1001),
                    new MatchRecord(25, 1001),
                    new MatchRecord(38, 1001),
                    new MatchRecord(39, 1001),
                    new MatchRecord(48, 105),
                    new MatchRecord(48, 1001),
                    new MatchRecord(53, 1001));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void multiHighlanderCombQuietSub1(DualApi api) {
        String data = "abcdefxxfoobarrrghabcxdefxteakettleeeeexxxxijklmxxdef";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 101, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "def", 102, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "foobar.*gh", 103, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "teakettle{4,10}", 104, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "ijkl[mMn]", 105, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(101 & 102 & 103) | (104 & !105)", 1001, EnumSet.of(DualExpressionFlag.COMBINATION, DualExpressionFlag.SINGLEMATCH)),
                expr(api, "!101 & 102", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "!(!101 | 102)", 1003, EnumSet.of(DualExpressionFlag.COMBINATION, DualExpressionFlag.SINGLEMATCH)),
                expr(api, "101 & !102", 1004, EnumSet.of(DualExpressionFlag.COMBINATION, DualExpressionFlag.SINGLEMATCH)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(3, 1003),
                    new MatchRecord(3, 1004),
                    new MatchRecord(18, 1001),
                    new MatchRecord(48, 105));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void multiQuietCombQuietSub1(DualApi api) {
        String data = "abcdefxxfoobarrrghabcxdefxteakettleeeeexxxxijklmxxdef";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 101, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "def", 102, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "foobar.*gh", 103, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "teakettle{4,10}", 104, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "ijkl[mMn]", 105, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(101 & 102 & 103) | (104 & !105)", 1001, EnumSet.of(DualExpressionFlag.COMBINATION, DualExpressionFlag.QUIET)),
                expr(api, "!101 & 102", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "!(!101 | 102)", 1003, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "101 & !102", 1004, EnumSet.of(DualExpressionFlag.COMBINATION, DualExpressionFlag.QUIET)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(3, 1003),
                    new MatchRecord(48, 105));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void singleComb2(DualApi api) {
        String data = "abbdefxxfoobarrrghabcxdefxteakettleeeeexxxxijklmxxdef";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 201, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "def", 202, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "foobar.*gh", 203, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "teakettle{4,10}", 204, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[mMn]", 205, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(201 | 202 & 203) & (!204 | 205)", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(6, 202),
                    new MatchRecord(18, 203),
                    new MatchRecord(18, 1002),
                    new MatchRecord(21, 201),
                    new MatchRecord(21, 1002),
                    new MatchRecord(25, 202),
                    new MatchRecord(25, 1002),
                    new MatchRecord(38, 204),
                    new MatchRecord(39, 204),
                    new MatchRecord(48, 205),
                    new MatchRecord(48, 1002),
                    new MatchRecord(53, 202),
                    new MatchRecord(53, 1002));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void singleCombQuietSub2(DualApi api) {
        String data = "abbdefxxfoobarrrghabcxdefxteakettleeeeexxxxijklmxxdef";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 201, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "def", 202, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "foobar.*gh", 203, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "teakettle{4,10}", 204, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[mMn]", 205, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "(201 | 202 & 203) & (!204 | 205)", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(18, 1002),
                    new MatchRecord(21, 201),
                    new MatchRecord(21, 1002),
                    new MatchRecord(25, 1002),
                    new MatchRecord(38, 204),
                    new MatchRecord(39, 204),
                    new MatchRecord(48, 1002),
                    new MatchRecord(53, 1002));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void singleComb3(DualApi api) {
        String data = "abcijklndefxxfoobarrrghabcxdefxteakettleeeeexxxxijklnxxdef";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 301, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "def", 302, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "foobar.*gh", 303, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "teakettle{4,10}", 304, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[mMn]", 305, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "((301 | 302) & 303) & (304 | 305)", 1003, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(3, 301),
                    new MatchRecord(8, 305),
                    new MatchRecord(11, 302),
                    new MatchRecord(23, 303),
                    new MatchRecord(23, 1003),
                    new MatchRecord(26, 301),
                    new MatchRecord(26, 1003),
                    new MatchRecord(30, 302),
                    new MatchRecord(30, 1003),
                    new MatchRecord(43, 304),
                    new MatchRecord(43, 1003),
                    new MatchRecord(44, 304),
                    new MatchRecord(44, 1003),
                    new MatchRecord(53, 305),
                    new MatchRecord(53, 1003),
                    new MatchRecord(58, 302),
                    new MatchRecord(58, 1003));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void singleCombQuietSub3(DualApi api) {
        String data = "abcijklndefxxfoobarrrghabcxdefxteakettleeeeexxxxijklnxxdef";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 301, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "def", 302, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "foobar.*gh", 303, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "teakettle{4,10}", 304, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "ijkl[mMn]", 305, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "((301 | 302) & 303) & (304 | 305)", 1003, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(23, 303),
                    new MatchRecord(23, 1003),
                    new MatchRecord(26, 1003),
                    new MatchRecord(30, 1003),
                    new MatchRecord(43, 1003),
                    new MatchRecord(44, 1003),
                    new MatchRecord(53, 1003),
                    new MatchRecord(58, 1003));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void multiCombDupSub4(DualApi api) {
        String data = "abbdefxxfoobarrrghabcxdefxteakettleeeeexxxxijklmxxdef";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 201, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "def", 202, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "foobar.*gh", 203, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "teakettle{4,10}", 204, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[mMn]", 205, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(201 & 202 & 203) | (204 & !205)", 1001, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "(201 | 202 & 203) & (!204 | 205)", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "((201 | 202) & 203) & (204 | 205)", 1003, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(6, 202),
                    new MatchRecord(18, 203),
                    new MatchRecord(18, 1002),
                    new MatchRecord(21, 201),
                    new MatchRecord(21, 1001),
                    new MatchRecord(21, 1002),
                    new MatchRecord(25, 202),
                    new MatchRecord(25, 1001),
                    new MatchRecord(25, 1002),
                    new MatchRecord(38, 204),
                    new MatchRecord(38, 1001),
                    new MatchRecord(38, 1003),
                    new MatchRecord(39, 204),
                    new MatchRecord(39, 1001),
                    new MatchRecord(39, 1003),
                    new MatchRecord(48, 205),
                    new MatchRecord(48, 1001),
                    new MatchRecord(48, 1002),
                    new MatchRecord(48, 1003),
                    new MatchRecord(53, 202),
                    new MatchRecord(53, 1001),
                    new MatchRecord(53, 1002),
                    new MatchRecord(53, 1003));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void multiCombQuietDupSub4(DualApi api) {
        String data = "abbdefxxfoobarrrghabcxdefxteakettleeeeexxxxijklmxxdef";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 201, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "def", 202, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "foobar.*gh", 203, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "teakettle{4,10}", 204, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[mMn]", 205, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "(201 & 202 & 203) | (204 & !205)", 1001, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "(201 | 202 & 203) & (!204 | 205)", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "((201 | 202) & 203) & (204 | 205)", 1003, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(18, 1002),
                    new MatchRecord(21, 1001),
                    new MatchRecord(21, 1002),
                    new MatchRecord(25, 1001),
                    new MatchRecord(25, 1002),
                    new MatchRecord(38, 204),
                    new MatchRecord(38, 1001),
                    new MatchRecord(38, 1003),
                    new MatchRecord(39, 204),
                    new MatchRecord(39, 1001),
                    new MatchRecord(39, 1003),
                    new MatchRecord(48, 1001),
                    new MatchRecord(48, 1002),
                    new MatchRecord(48, 1003),
                    new MatchRecord(53, 1001),
                    new MatchRecord(53, 1002),
                    new MatchRecord(53, 1003));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void multiCombUniSub5(DualApi api) {
        String data = "abcdefxxfoobarrrghabcxdefxteakettleeeeexxxxijklmxxdef"
                + "-----------------------------------------------"
                + "cbbfedxxgoogleeecncbaxfedxhaystacksssssxxxxijkloxxfed"
                + "-----------------------------------------------"
                + "cabijklRfeexxgoobarrrjpcabxfeexshockwaveeeeexxxxijklsxxfee"
                + "------------------------------------------";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 101, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "def", 102, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "foobar.*gh", 103, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "teakettle{4,10}", 104, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[mMn]", 105, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "cba", 201, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "fed", 202, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "google.*cn", 203, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "haystacks{4,8}", 204, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[oOp]", 205, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "cab", 301, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "fee", 302, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "goobar.*jp", 303, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "shockwave{4,6}", 304, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[rRs]", 305, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(101 & 102 & 103) | (104 & !105)", 1001, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "(201 | 202 & 203) & (!204 | 205)", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "((301 | 302) & 303) & (304 | 305)", 1003, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(3, 101),
                    new MatchRecord(6, 102),
                    new MatchRecord(18, 103),
                    new MatchRecord(18, 1001),
                    new MatchRecord(21, 101),
                    new MatchRecord(21, 1001),
                    new MatchRecord(25, 102),
                    new MatchRecord(25, 1001),
                    new MatchRecord(38, 104),
                    new MatchRecord(38, 1001),
                    new MatchRecord(39, 104),
                    new MatchRecord(39, 1001),
                    new MatchRecord(48, 105),
                    new MatchRecord(48, 1001),
                    new MatchRecord(53, 102),
                    new MatchRecord(53, 1001),
                    new MatchRecord(106, 202),
                    new MatchRecord(118, 203),
                    new MatchRecord(118, 1002),
                    new MatchRecord(121, 201),
                    new MatchRecord(121, 1002),
                    new MatchRecord(125, 202),
                    new MatchRecord(125, 1002),
                    new MatchRecord(138, 204),
                    new MatchRecord(139, 204),
                    new MatchRecord(148, 205),
                    new MatchRecord(148, 1002),
                    new MatchRecord(153, 202),
                    new MatchRecord(153, 1002),
                    new MatchRecord(203, 301),
                    new MatchRecord(208, 305),
                    new MatchRecord(211, 302),
                    new MatchRecord(223, 303),
                    new MatchRecord(223, 1003),
                    new MatchRecord(226, 301),
                    new MatchRecord(226, 1003),
                    new MatchRecord(230, 302),
                    new MatchRecord(230, 1003),
                    new MatchRecord(243, 304),
                    new MatchRecord(243, 1003),
                    new MatchRecord(244, 304),
                    new MatchRecord(244, 1003),
                    new MatchRecord(253, 305),
                    new MatchRecord(253, 1003),
                    new MatchRecord(258, 302),
                    new MatchRecord(258, 1003));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void multiCombQuietUniSub5(DualApi api) {
        String data = "abcdefxxfoobarrrghabcxdefxteakettleeeeexxxxijklmxxdef"
                + "-----------------------------------------------"
                + "cbbfedxxgoogleeecncbaxfedxhaystacksssssxxxxijkloxxfed"
                + "-----------------------------------------------"
                + "cabijklRfeexxgoobarrrjpcabxfeexshockwaveeeeexxxxijklsxxfee"
                + "------------------------------------------";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 101, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "def", 102, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "foobar.*gh", 103, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "teakettle{4,10}", 104, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "ijkl[mMn]", 105, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "cba", 201, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "fed", 202, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "google.*cn", 203, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "haystacks{4,8}", 204, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[oOp]", 205, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "cab", 301, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "fee", 302, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "goobar.*jp", 303, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "shockwave{4,6}", 304, EnumSet.of(DualExpressionFlag.QUIET)),
                expr(api, "ijkl[rRs]", 305, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(101 & 102 & 103) | (104 & !105)", 1001, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "(201 | 202 & 203) & (!204 | 205)", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "((301 | 302) & 303) & (304 | 305)", 1003, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(3, 101),
                    new MatchRecord(18, 1001),
                    new MatchRecord(21, 101),
                    new MatchRecord(21, 1001),
                    new MatchRecord(25, 1001),
                    new MatchRecord(38, 1001),
                    new MatchRecord(39, 1001),
                    new MatchRecord(48, 105),
                    new MatchRecord(48, 1001),
                    new MatchRecord(53, 1001),
                    new MatchRecord(106, 202),
                    new MatchRecord(118, 1002),
                    new MatchRecord(121, 1002),
                    new MatchRecord(125, 202),
                    new MatchRecord(125, 1002),
                    new MatchRecord(138, 204),
                    new MatchRecord(139, 204),
                    new MatchRecord(148, 1002),
                    new MatchRecord(153, 202),
                    new MatchRecord(153, 1002),
                    new MatchRecord(208, 305),
                    new MatchRecord(223, 303),
                    new MatchRecord(223, 1003),
                    new MatchRecord(226, 1003),
                    new MatchRecord(230, 1003),
                    new MatchRecord(243, 1003),
                    new MatchRecord(244, 1003),
                    new MatchRecord(253, 305),
                    new MatchRecord(253, 1003),
                    new MatchRecord(258, 1003));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void singleCombPurelyNegative6(DualApi api) {
        String data = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 201, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "def", 202, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "foobar.*gh", 203, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "teakettle{4,10}", 204, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[mMn]", 205, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(!201 | 202 & 203) & (!204 | 205)", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(53, 1002));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void singleCombQuietPurelyNegative6(DualApi api) {
        String data = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 201, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "def", 202, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "foobar.*gh", 203, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "teakettle{4,10}", 204, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[mMn]", 205, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(!201 | 202 & 203) & (!204 | 205)", 1002, EnumSet.of(DualExpressionFlag.COMBINATION, DualExpressionFlag.QUIET)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).isEmpty();
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void multiCombPurelyNegativeUniSub6(DualApi api) {
        String data = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                + "-----------------------------------------------"
                + "xxxfedxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                + "-----------------------------------------------"
                + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                + "------------------------------------------";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 101, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "def", 102, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "foobar.*gh", 103, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "teakettle{4,10}", 104, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[mMn]", 105, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "cba", 201, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "fed", 202, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "google.*cn", 203, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "haystacks{4,8}", 204, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[oOp]", 205, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "cab", 301, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "fee", 302, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "goobar.*jp", 303, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "shockwave{4,6}", 304, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[rRs]", 305, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(101 & 102 & 103) | (!104 & !105)", 1001, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "(!201 | 202 & 203) & (!204 | 205)", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "((301 | 302) & 303) & (304 | 305)", 1003, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(106, 202),
                    new MatchRecord(106, 1002),
                    new MatchRecord(300, 1001));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void multiCombPurelyNegativeUniSubEod6(DualApi api) {
        String data = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                + "-----------------------------------------------"
                + "xdefedxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                + "-----------------------------------------------"
                + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
                + "-------------------------------------defed";
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 101, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "defed", 102, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "foobar.*gh", 103, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "teakettle{4,10}", 104, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[mMn]", 105, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "cba", 201, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "fed", 202, EnumSet.of(DualExpressionFlag.MULTILINE)),
                expr(api, "google.*cn", 203, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "haystacks{4,8}", 204, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[oOp]", 205, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "cab", 301, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "fee", 302, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "goobar.*jp", 303, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "shockwave{4,6}", 304, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "ijkl[rRs]", 305, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "(101 & 102 & 103) | (!104 & !105)", 1001, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "(!201 | 202 & 203) & (!204 | 205)", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "((301 | 302) & 303) & (304 | 305)", 1003, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileBlock(api, patterns);
        try {
            assertThat(scanBlock(api, db, data)).containsExactly(
                    new MatchRecord(106, 102),
                    new MatchRecord(106, 202),
                    new MatchRecord(106, 1001),
                    new MatchRecord(106, 1002),
                    new MatchRecord(300, 102),
                    new MatchRecord(300, 202),
                    new MatchRecord(300, 1001),
                    new MatchRecord(300, 1002));
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(LogicalCombinationArgumentsSource.class)
    void multiCombStream1(DualApi api) {
        String[] data = {
                "xxxxxxxabcxxxxxxxdefxxxghixxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                "xxxxxxxxxxxxxxxxghixxxxxxxxxxxabcxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                "xxxxxxxxxxxxxxxxdefxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                "xxxxxxxxxxxxxxxxxyzxxxxxxxxxxxxxxxxxxxxxghixxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                "xxxxxghixxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxzxy",
                "z"};
        List<DualExpression> patterns = List.of(
                expr(api, "abc", 101, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "def", 102, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "xyz", 201, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "zxyz", 202, EnumSet.noneOf(DualExpressionFlag.class)),
                expr(api, "101 & 102", 1001, EnumSet.of(DualExpressionFlag.COMBINATION)),
                expr(api, "201 & !202", 1002, EnumSet.of(DualExpressionFlag.COMBINATION)));
        DualDatabase db = compileStream(api, patterns);
        try {
            assertThat(scanStream(api, db, data)).containsExactly(
                    new MatchRecord(10, 101),
                    new MatchRecord(20, 102),
                    new MatchRecord(20, 1001),
                    new MatchRecord(109, 101),
                    new MatchRecord(109, 1001),
                    new MatchRecord(171, 102),
                    new MatchRecord(171, 1001),
                    new MatchRecord(247, 201),
                    new MatchRecord(247, 1002),
                    new MatchRecord(761, 201),
                    new MatchRecord(761, 202));
        } finally {
            api.closeDatabase(db);
        }
    }

    public static class LogicalCombinationArgumentsSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(DualImplementation.values())
                    .map(impl -> Arguments.of(impl.createAdapter()));
        }
    }
}
