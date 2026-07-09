package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualResult;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class BehaviourTest {

    private record MatchRecord(int to, int id) {
    }

    private record CallbackStopCase(String pattern, EnumSet<DualExpressionFlag> flags, String corpus) {
    }

    private record HugeScanCase(String pattern, EnumSet<DualExpressionFlag> flags, String preBlock, String postBlock) {
    }

    private static final class CountingHandler implements DualByteMatchHandler {
        private final int haltAfter;
        private int count;

        CountingHandler(int haltAfter) {
            this.haltAfter = haltAfter;
        }

        @Override
        public boolean onMatch(DualExpression expression, long from, long to) {
            count++;
            return count < haltAfter;
        }
    }

    private static final class MatchCollector implements DualByteMatchHandler {
        private final List<MatchRecord> matches = new ArrayList<>();
        private boolean halt;

        @Override
        public boolean onMatch(DualExpression expression, long from, long to) {
            matches.add(new MatchRecord((int) to, expression.id()));
            return !halt;
        }
    }

    private static final class RecordingHandler implements DualByteMatchHandler {
        private long lastTo;
        private long lastFrom;
        private int lastId;
        private boolean matched;

        void reset() {
            lastTo = 0;
            lastFrom = 0;
            lastId = 0;
            matched = false;
        }

        @Override
        public boolean onMatch(DualExpression expression, long from, long to) {
            lastTo = to;
            lastFrom = from;
            lastId = expression.id();
            matched = true;
            return true;
        }
    }

    private static final class NoOpHandler implements DualByteMatchHandler {
        @Override
        public boolean onMatch(DualExpression expression, long from, long to) {
            return true;
        }
    }

    private static final int[] LITERAL_LENGTHS = {1, 2, 3, 4, 8, 16, 17, 32, 100, 200, 400, 1000, 4096, 8192, 15000, 15999};

    private static final List<CallbackStopCase> CALLBACK_STOP_CASES = List.of(
            new CallbackStopCase("foobar", EnumSet.noneOf(DualExpressionFlag.class), "xxxfoobarxxxfoobarxxxfoobar"),
            new CallbackStopCase("a", EnumSet.noneOf(DualExpressionFlag.class), "xxxaaaaaaaaaaaaaaaaaaa"),
            new CallbackStopCase("a", EnumSet.of(DualExpressionFlag.CASELESS), "xxxAaAaAaAa"),
            new CallbackStopCase(".", EnumSet.noneOf(DualExpressionFlag.class), "acbdef"),
            new CallbackStopCase(".", EnumSet.of(DualExpressionFlag.DOTALL), "abcdef"),
            new CallbackStopCase("foo.*bar.*foo", EnumSet.of(DualExpressionFlag.DOTALL), "foobarfoobarfoobarfoobarfoo"),
            new CallbackStopCase("(badger.*){3,}", EnumSet.of(DualExpressionFlag.DOTALL), "badger badger badger badger badger badger badger badger"),
            new CallbackStopCase("foobar.*", EnumSet.noneOf(DualExpressionFlag.class), "foobarxxx"),
            new CallbackStopCase("[012]{3,7}.*abba", EnumSet.noneOf(DualExpressionFlag.class), "0120120120abbaabba")
    );

    private static final List<HugeScanCase> GIGABYTE_CASES = List.of(
            new HugeScanCase("foobar", EnumSet.noneOf(DualExpressionFlag.class), "flibble", "foobar"),
            new HugeScanCase("longliteralislongerthanlong", EnumSet.noneOf(DualExpressionFlag.class), "precursor", "longliteralislongerthanlong"),
            new HugeScanCase("foobar\\z", EnumSet.noneOf(DualExpressionFlag.class), "flibble", "foobar"),
            new HugeScanCase("hatstand.*teakettle.*badgerbrush", EnumSet.of(DualExpressionFlag.DOTALL), "hatstand teakettle", "_badgerbrush"),
            new HugeScanCase("hatstand.*teakettle.*badgerbrush\\z", EnumSet.of(DualExpressionFlag.DOTALL), "hatstand teakettle", "_badgerbrush"),
            new HugeScanCase("a.*(([0123][56789]){3,6}|flibble|xyz{1,2}y)", EnumSet.noneOf(DualExpressionFlag.class), "a", "051629"),
            new HugeScanCase("^a.*(([0123][56789]){3,6}|flibble|xyz{1,2}y)", EnumSet.noneOf(DualExpressionFlag.class), "a", "051629"),
            new HugeScanCase("(badger.*){3,}mushroom.*mushroom", EnumSet.of(DualExpressionFlag.DOTALL), "badger badger badger", "mushroom! mushroom"),
            new HugeScanCase("(badger.*){3,}mushroom.*mushroom$", EnumSet.of(DualExpressionFlag.DOTALL), "badger badger badger", "mushroom! mushroom"),
            new HugeScanCase("foo[^X]{16}", EnumSet.of(DualExpressionFlag.SINGLEMATCH), "preblock", "foo0123456789abcdef"),
            new HugeScanCase("foobar.*bazbaz[^X]{16}", EnumSet.of(DualExpressionFlag.DOTALL, DualExpressionFlag.SINGLEMATCH), "foobar", "bazbaz0123456789abcdef"),
            new HugeScanCase("t?((w.(u|t|.)){6,10}|[dqnt]|e|va)", EnumSet.noneOf(DualExpressionFlag.class), "~~~~", "tva"),
            new HugeScanCase("HTTP.*foobar.*blah", EnumSet.of(DualExpressionFlag.DOTALL), "bing", "HTTP foobar blah")
    );

    static Stream<Arguments> literalLengthProvider() {
        return Stream.of(new com.xenoamess.hyperscan.smoke.dual.JavaCppAdapter(), new com.xenoamess.hyperscan.smoke.dual.PanamaAdapter())
                .flatMap(api -> Arrays.stream(LITERAL_LENGTHS).boxed().map(len -> Arguments.of(api, len)));
    }

    static Stream<Arguments> callbackStopProvider() {
        return Stream.of(new com.xenoamess.hyperscan.smoke.dual.JavaCppAdapter(), new com.xenoamess.hyperscan.smoke.dual.PanamaAdapter())
                .flatMap(api -> CALLBACK_STOP_CASES.stream().map(c -> Arguments.of(api, c)));
    }

    static Stream<Arguments> gigabyteMatchProvider() {
        return Stream.of(new com.xenoamess.hyperscan.smoke.dual.JavaCppAdapter(), new com.xenoamess.hyperscan.smoke.dual.PanamaAdapter())
                .flatMap(api -> GIGABYTE_CASES.stream().map(c -> Arguments.of(api, c)));
    }

    private static DualDatabase compileSingle(DualApi api, String pattern, EnumSet<DualExpressionFlag> flags, int mode) {
        DualCompileResult result = api.compileRaw(pattern, api.flagsToBits(flags), mode);
        assertThat(result.code()).isEqualTo(api.success());
        assertThat(result.database()).isNotNull();
        return result.database();
    }

    private static DualDatabase compileMulti(DualApi api, List<DualExpression> expressions, int mode) {
        DualCompileResult result = api.compileRaw(expressions, mode);
        assertThat(result.code()).isEqualTo(api.success());
        assertThat(result.database()).isNotNull();
        return result.database();
    }

    @Tag("large-data")
    @Timeout(value = 5, unit = TimeUnit.MINUTES)
    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void scanSeveralGigabytesNoMatch(DualApi api) {
        DualDatabase db = compileSingle(api, "hatstand.*teakettle.*badgerbrush", EnumSet.of(DualExpressionFlag.DOTALL), api.modeStream());
        DualStream stream = api.openStreamRaw(db).stream();
        DualScanner scratch = api.getStreamScratch(stream);
        try {
            NoOpHandler handler = new NoOpHandler();
            byte[] data = new byte[1024 * 1024];
            Arrays.fill(data, (byte) 'X');
            long megabytes = 5L * 1024;
            while (megabytes-- > 0) {
                assertThat(api.scanStreamRaw(stream, data, scratch, handler)).isEqualTo(api.success());
            }
            assertThat(api.closeStreamRaw(stream, scratch, handler)).isEqualTo(api.success());
            stream = null;
            scratch = null;
        } finally {
            if (stream != null) {
                api.closeStreamRaw(stream, scratch, null);
            }
            api.closeDatabase(db);
        }
    }

    @Tag("large-data")
    @Timeout(value = 5, unit = TimeUnit.MINUTES)
    @ParameterizedTest
    @MethodSource("gigabyteMatchProvider")
    void scanGigabytesStreamingMatch(DualApi api, HugeScanCase params) {
        DualDatabase db = compileSingle(api, params.pattern, params.flags, api.modeStream());
        try {
            byte[] preBlock = params.preBlock.getBytes(StandardCharsets.UTF_8);
            byte[] postBlock = params.postBlock.getBytes(StandardCharsets.UTF_8);
            byte[] data = new byte[1024 * 1024];
            Arrays.fill(data, (byte) 'X');
            long[] gigabytes = {1, 2};
            for (long gb : gigabytes) {
                DualStream stream = api.openStreamRaw(db).stream();
                DualScanner scratch = api.getStreamScratch(stream);
                try {
                    RecordingHandler handler = new RecordingHandler();
                    assertThat(api.scanStreamRaw(stream, preBlock, scratch, handler)).isEqualTo(api.success());
                    assertThat(handler.matched).isFalse();
                    handler.reset();
                    long remaining = gb * 1024;
                    while (remaining-- > 0) {
                        assertThat(api.scanStreamRaw(stream, data, scratch, handler)).isEqualTo(api.success());
                        assertThat(handler.matched).isFalse();
                    }
                    assertThat(api.scanStreamRaw(stream, postBlock, scratch, handler)).isEqualTo(api.success());
                    assertThat(api.closeStreamRaw(stream, scratch, handler)).isEqualTo(api.success());
                    long totalLen = preBlock.length + gb * 1024L * 1024L * 1024L + postBlock.length;
                    assertThat(handler.matched).isTrue();
                    assertThat(handler.lastTo).isEqualTo(totalLen);
                } finally {
                    api.closeStreamRaw(stream, scratch, null);
                }
            }
        } finally {
            api.closeDatabase(db);
        }
    }

    @Tag("large-data")
    @Timeout(value = 5, unit = TimeUnit.MINUTES)
    @ParameterizedTest
    @MethodSource("gigabyteMatchProvider")
    void scanGigabytesBlockMatch(DualApi api, HugeScanCase params) {
        DualDatabase db = compileSingle(api, params.pattern, params.flags, api.modeBlock());
        DualScanner scanner = api.allocScratchRaw(db).scratch();
        try {
            byte[] preBlock = params.preBlock.getBytes(StandardCharsets.UTF_8);
            byte[] postBlock = params.postBlock.getBytes(StandardCharsets.UTF_8);
            int[] blockSizes = {1, 4, 8, 16, 32, 64, 128, 256, 512, 1024}; // KB
            for (int sizeKb : blockSizes) {
                int base = sizeKb * 1024;
                int[] variants = {base, base + 4, base + postBlock.length};
                for (int len : variants) {
                    RecordingHandler handler = new RecordingHandler();
                    byte[] block = new byte[len];
                    Arrays.fill(block, (byte) 'X');
                    System.arraycopy(preBlock, 0, block, 0, preBlock.length);
                    System.arraycopy(postBlock, 0, block, len - postBlock.length, postBlock.length);
                    assertThat(api.scanRaw(scanner, db, block, handler)).isEqualTo(api.success());
                    assertThat(handler.lastTo).isEqualTo(len);
                }
            }
        } finally {
            api.freeScratchRaw(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void streamingThereCanBeOnlyOne(DualApi api) {
        DualDatabase db = compileSingle(api, ".ck", EnumSet.of(DualExpressionFlag.CASELESS, DualExpressionFlag.SINGLEMATCH), api.modeStream());
        DualStream stream = api.openStreamRaw(db).stream();
        DualScanner scratch = api.getStreamScratch(stream);
        try {
            CountingHandler handler = new CountingHandler(100);
            assertThat(api.scanStreamRaw(stream, "hackhackHACKhackHACkhAcK".getBytes(StandardCharsets.UTF_8), scratch, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(1);
            assertThat(api.scanStreamRaw(stream, "KHACKhackHACKhaCkhAcKhaaaaaaaaaaaaaaaaaaaackk".getBytes(StandardCharsets.UTF_8), scratch, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(1);
            assertThat(api.closeStreamRaw(stream, scratch, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(1);
            stream = null;
            scratch = null;
        } finally {
            if (stream != null) {
                api.closeStreamRaw(stream, scratch, null);
            }
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void blockThereCanBeOnlyOne(DualApi api) {
        DualDatabase db = compileSingle(api, ".ck", EnumSet.of(DualExpressionFlag.CASELESS, DualExpressionFlag.SINGLEMATCH), api.modeBlock());
        DualScanner scanner = api.allocScratchRaw(db).scratch();
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[] data = ("hackhackHACKhackHACkhAcKzofunvuynlslijlnikshvb ar yhtkubq45ytvb iuyh "
                    + "ackeruniou viytdfjhg nvldkrjgnal").getBytes(StandardCharsets.UTF_8);
            assertThat(api.scanRaw(scanner, db, data, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.freeScratchRaw(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @MethodSource("literalLengthProvider")
    void floatingLiteralLength(DualApi api, int literalLen) {
        String pattern = String.valueOf('a').repeat(literalLen);
        byte[] data = String.valueOf('a').repeat(literalLen + 4).getBytes(StandardCharsets.UTF_8);
        byte[] tail = new byte[data.length - 5];
        System.arraycopy(data, 5, tail, 0, tail.length);
        DualDatabase db = compileSingle(api, pattern, EnumSet.noneOf(DualExpressionFlag.class), api.modeBlock());
        DualScanner scanner = api.allocScratchRaw(db).scratch();
        try {
            CountingHandler handler = new CountingHandler(100);
            assertThat(api.scanRaw(scanner, db, data, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(5);
            handler = new CountingHandler(100);
            assertThat(api.scanRaw(scanner, db, tail, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(0);
        } finally {
            api.freeScratchRaw(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @MethodSource("literalLengthProvider")
    void anchoredLiteralLength(DualApi api, int literalLen) {
        String pattern = "^" + String.valueOf('a').repeat(literalLen);
        byte[] data = String.valueOf('a').repeat(literalLen + 4).getBytes(StandardCharsets.UTF_8);
        byte[] tail = new byte[data.length - 5];
        System.arraycopy(data, 5, tail, 0, tail.length);
        DualDatabase db = compileSingle(api, pattern, EnumSet.noneOf(DualExpressionFlag.class), api.modeBlock());
        DualScanner scanner = api.allocScratchRaw(db).scratch();
        try {
            CountingHandler handler = new CountingHandler(100);
            assertThat(api.scanRaw(scanner, db, data, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(1);
            handler = new CountingHandler(100);
            assertThat(api.scanRaw(scanner, db, tail, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(0);
        } finally {
            api.freeScratchRaw(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @MethodSource("callbackStopProvider")
    void callbackStopBlock(DualApi api, CallbackStopCase params) {
        DualDatabase db = compileSingle(api, params.pattern, params.flags, api.modeBlock());
        DualScanner scanner = api.allocScratchRaw(db).scratch();
        try {
            CountingHandler handler = new CountingHandler(1);
            assertThat(api.scanRaw(scanner, db, params.corpus.getBytes(StandardCharsets.UTF_8), handler)).isEqualTo(api.scanTerminated());
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.freeScratchRaw(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @MethodSource("callbackStopProvider")
    void callbackStopStreaming(DualApi api, CallbackStopCase params) {
        DualDatabase db = compileSingle(api, params.pattern, params.flags, api.modeStream());
        DualStream stream = api.openStreamRaw(db).stream();
        DualScanner scratch = api.getStreamScratch(stream);
        try {
            CountingHandler handler = new CountingHandler(1);
            assertThat(api.scanStreamRaw(stream, params.corpus.getBytes(StandardCharsets.UTF_8), scratch, handler)).isEqualTo(api.scanTerminated());
            assertThat(handler.count).isEqualTo(1);
            assertThat(api.closeStreamRaw(stream, scratch, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(1);
            stream = null;
            scratch = null;
        } finally {
            if (stream != null) {
                api.closeStreamRaw(stream, scratch, null);
            }
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @MethodSource("callbackStopProvider")
    void callbackStopVectored(DualApi api, CallbackStopCase params) {
        DualDatabase db = compileSingle(api, params.pattern, params.flags, api.modeVectored());
        DualScanner scanner = api.allocScratchRaw(db).scratch();
        try {
            CountingHandler handler = new CountingHandler(1);
            byte[][] input = {params.corpus.getBytes(StandardCharsets.UTF_8)};
            assertThat(api.scanVectorRaw(scanner, db, input, handler)).isEqualTo(api.scanTerminated());
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.freeScratchRaw(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void serializedDogfood1(DualApi api) {
        DualDatabase db = compileSingle(api, "puppy treats!", EnumSet.noneOf(DualExpressionFlag.class), api.modeBlock());
        DualResult<Long> sizeResult = api.getDatabaseSizeRaw(db);
        assertThat(sizeResult.code()).isEqualTo(api.success());
        long origSize = sizeResult.value();
        DualResult<byte[]> serializedResult = api.serializeRaw(db);
        assertThat(serializedResult.code()).isEqualTo(api.success());
        byte[] bytes = serializedResult.value();
        assertThat(bytes).isNotNull();
        assertThat(bytes.length).isGreaterThan(0);
        api.closeDatabase(db);
        DualResult<DualDatabase> dbResult = api.deserializeRaw(bytes);
        assertThat(dbResult.code()).isEqualTo(api.success());
        DualDatabase db2 = dbResult.value();
        assertThat(db2).isNotNull();
        try {
            DualResult<Long> size2 = api.getDatabaseSizeRaw(db2);
            assertThat(size2.code()).isEqualTo(api.success());
            assertThat(size2.value()).isEqualTo(origSize);
            DualScanner scanner = api.allocScratchRaw(db2).scratch();
            try {
                RecordingHandler handler = new RecordingHandler();
                byte[] data = "delicious puppy treats!".getBytes(StandardCharsets.UTF_8);
                assertThat(api.scanRaw(scanner, db2, data, handler)).isEqualTo(api.success());
                assertThat(handler.lastTo).isEqualTo(data.length);
            } finally {
                api.freeScratchRaw(scanner);
            }
        } finally {
            api.closeDatabase(db2);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void serializedDogfood2(DualApi api) {
        List<DualExpression> expressions = List.of(
                api.createExpression("puppy treats!", EnumSet.noneOf(DualExpressionFlag.class), 1000),
                api.createExpression("foobar", EnumSet.noneOf(DualExpressionFlag.class), 1001),
                api.createExpression("foo.*bar.*foo", EnumSet.of(DualExpressionFlag.DOTALL), 1002),
                api.createExpression("(badger.*){3,}", EnumSet.of(DualExpressionFlag.DOTALL), 1003),
                api.createExpression("[012]{3,7}.*abba", EnumSet.noneOf(DualExpressionFlag.class), 1004)
        );
        DualDatabase db = compileMulti(api, expressions, api.modeBlock());
        DualResult<Long> sizeResult = api.getDatabaseSizeRaw(db);
        assertThat(sizeResult.code()).isEqualTo(api.success());
        long origSize = sizeResult.value();
        DualResult<byte[]> serializedResult = api.serializeRaw(db);
        assertThat(serializedResult.code()).isEqualTo(api.success());
        byte[] bytes = serializedResult.value();
        assertThat(bytes).isNotNull();
        assertThat(bytes.length).isGreaterThan(0);
        api.closeDatabase(db);
        DualResult<DualDatabase> dbResult = api.deserializeRaw(bytes);
        assertThat(dbResult.code()).isEqualTo(api.success());
        DualDatabase db2 = dbResult.value();
        assertThat(db2).isNotNull();
        try {
            DualResult<Long> size2 = api.getDatabaseSizeRaw(db2);
            assertThat(size2.code()).isEqualTo(api.success());
            assertThat(size2.value()).isEqualTo(origSize);
            DualScanner scanner = api.allocScratchRaw(db2).scratch();
            try {
                RecordingHandler handler = new RecordingHandler();
                byte[] data = "delicious puppy treats!".getBytes(StandardCharsets.UTF_8);
                assertThat(api.scanRaw(scanner, db2, data, handler)).isEqualTo(api.success());
                assertThat(handler.lastTo).isEqualTo(data.length);
                assertThat(handler.lastId).isEqualTo(1000);
                handler.reset();
                byte[] data2 = "badger badger badger".getBytes(StandardCharsets.UTF_8);
                assertThat(api.scanRaw(scanner, db2, data2, handler)).isEqualTo(api.success());
                assertThat(handler.lastTo).isEqualTo(data2.length);
                assertThat(handler.lastId).isEqualTo(1003);
            } finally {
                api.freeScratchRaw(scanner);
            }
        } finally {
            api.closeDatabase(db2);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void serializedDogfood3(DualApi api) {
        DualDatabase db = compileSingle(api, "puppy treats!", EnumSet.noneOf(DualExpressionFlag.class), api.modeBlock());
        DualResult<Long> sizeResult = api.getDatabaseSizeRaw(db);
        assertThat(sizeResult.code()).isEqualTo(api.success());
        long origSize = sizeResult.value();
        DualResult<byte[]> serializedResult = api.serializeRaw(db);
        assertThat(serializedResult.code()).isEqualTo(api.success());
        byte[] bytes = serializedResult.value();
        assertThat(bytes).isNotNull();
        assertThat(bytes.length).isGreaterThan(0);
        api.closeDatabase(db);
        DualResult<Long> slengthResult = api.getSerializedDatabaseSizeRaw(bytes);
        assertThat(slengthResult.code()).isEqualTo(api.success());
        long slength = slengthResult.value();
        assertThat(slength).isEqualTo(origSize);
        DualDatabase mem = api.allocateRawDatabase(slength + 16);
        try {
            DualDatabase mem1 = api.offsetRawDatabase(mem, 1);
            DualDatabase mem4 = api.offsetRawDatabase(mem, 4);
            assertThat(api.deserializeAtRaw(bytes, mem1)).isEqualTo(api.badAlign());
            assertThat(api.deserializeAtRaw(bytes, mem4)).isEqualTo(api.badAlign());
            assertThat(api.deserializeAtRaw(bytes, mem)).isEqualTo(api.success());
            DualResult<Long> sizeMem = api.getDatabaseSizeRaw(mem);
            assertThat(sizeMem.code()).isEqualTo(api.success());
            assertThat(sizeMem.value()).isEqualTo(origSize);
            DualScanner scanner = api.allocScratchRaw(mem).scratch();
            try {
                RecordingHandler handler = new RecordingHandler();
                byte[] data = "delicious puppy treats!".getBytes(StandardCharsets.UTF_8);
                assertThat(api.scanRaw(scanner, mem, data, handler)).isEqualTo(api.success());
                assertThat(handler.lastTo).isEqualTo(data.length);
            } finally {
                api.freeScratchRaw(scanner);
            }
            api.closeDatabase(mem1);
            api.closeDatabase(mem4);
        } finally {
            api.closeDatabase(mem);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void closeStreamMatch(DualApi api) {
        DualDatabase db = compileSingle(api, "foo.*bar$", EnumSet.noneOf(DualExpressionFlag.class), api.modeStream());
        DualStream stream = api.openStream(db);
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[] data = "foo        bar".getBytes(StandardCharsets.UTF_8);
            api.scanStream(null, stream, data, handler);
            assertThat(handler.count).isEqualTo(0);
            api.closeStream(null, stream, handler);
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.closeStream(null, stream, null);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void noMainCallback(DualApi api) {
        DualDatabase db = compileSingle(api, "foo|foo.*bar$", EnumSet.noneOf(DualExpressionFlag.class), api.modeStream());
        DualStream stream = api.openStream(db);
        try {
            byte[] data = "foo        bar".getBytes(StandardCharsets.UTF_8);
            api.scanStream(null, stream, data, null);
            CountingHandler handler = new CountingHandler(100);
            api.closeStream(null, stream, handler);
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.closeStream(null, stream, null);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void closeStreamNoMatch(DualApi api) {
        DualDatabase db = compileSingle(api, "foo.*bar$", EnumSet.noneOf(DualExpressionFlag.class), api.modeStream());
        DualStream stream = api.openStream(db);
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[] data = "foo        bar".getBytes(StandardCharsets.UTF_8);
            api.scanStream(null, stream, data, handler);
            assertThat(handler.count).isEqualTo(0);
            api.closeStream(null, stream, null);
            assertThat(handler.count).isEqualTo(0);
        } finally {
            api.closeStream(null, stream, null);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void closeStreamAfterTermination(DualApi api) {
        DualDatabase db = compileSingle(api, "foo.*(bar|baz\\z)", EnumSet.noneOf(DualExpressionFlag.class), api.modeStream());
        DualStream stream = api.openStream(db);
        try {
            CountingHandler handler = new CountingHandler(1);
            byte[] data = "foo        bar     baz".getBytes(StandardCharsets.UTF_8);
            api.scanStream(null, stream, data, handler);
            assertThat(handler.count).isEqualTo(1);
            api.closeStream(null, stream, handler);
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.closeStream(null, stream, null);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void vectored1(DualApi api) {
        DualDatabase db = compileSingle(api, "^foo.*bar", EnumSet.of(DualExpressionFlag.DOTALL), api.modeVectored());
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[][] input = {
                    "foo".getBytes(StandardCharsets.UTF_8),
                    "   ".getBytes(StandardCharsets.UTF_8),
                    "bar".getBytes(StandardCharsets.UTF_8)
            };
            api.scanVector(null, db, input, handler);
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void vectored2(DualApi api) {
        DualDatabase db = compileSingle(api, "^foo.*bar", EnumSet.of(DualExpressionFlag.DOTALL), api.modeVectored());
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[][] input = {
                    "".getBytes(StandardCharsets.UTF_8),
                    "foo".getBytes(StandardCharsets.UTF_8),
                    "   ".getBytes(StandardCharsets.UTF_8),
                    "bar".getBytes(StandardCharsets.UTF_8)
            };
            api.scanVector(null, db, input, handler);
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void vectored3(DualApi api) {
        DualDatabase db = compileSingle(api, "^foo.*bar", EnumSet.of(DualExpressionFlag.DOTALL), api.modeVectored());
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[][] input = {
                    "foo".getBytes(StandardCharsets.UTF_8),
                    "   ".getBytes(StandardCharsets.UTF_8),
                    "".getBytes(StandardCharsets.UTF_8),
                    "bar".getBytes(StandardCharsets.UTF_8)
            };
            api.scanVector(null, db, input, handler);
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void vectored4(DualApi api) {
        DualDatabase db = compileSingle(api, "^foo.*bar", EnumSet.of(DualExpressionFlag.DOTALL), api.modeVectored());
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[][] input = {
                    "foo".getBytes(StandardCharsets.UTF_8),
                    "   ".getBytes(StandardCharsets.UTF_8),
                    "bar".getBytes(StandardCharsets.UTF_8),
                    "".getBytes(StandardCharsets.UTF_8)
            };
            api.scanVector(null, db, input, handler);
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void vectored5(DualApi api) {
        DualDatabase db = compileSingle(api, "^foo.*bar", EnumSet.of(DualExpressionFlag.DOTALL), api.modeVectored());
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[][] input = new byte[0][0];
            api.scanVector(null, db, input, handler);
            assertThat(handler.count).isEqualTo(0);
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void vectored6(DualApi api) {
        DualDatabase db = compileSingle(api, "^", EnumSet.of(DualExpressionFlag.DOTALL, DualExpressionFlag.ALLOWEMPTY), api.modeVectored());
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[][] input = new byte[0][0];
            api.scanVector(null, db, input, handler);
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void vectored7(DualApi api) {
        DualDatabase db = compileSingle(api, "$", EnumSet.of(DualExpressionFlag.DOTALL, DualExpressionFlag.ALLOWEMPTY), api.modeVectored());
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[][] input = new byte[0][0];
            api.scanVector(null, db, input, handler);
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void multiStream1(DualApi api) {
        DualDatabase db = compileSingle(api, "foo.*bar.*\\b", EnumSet.noneOf(DualExpressionFlag.class), api.modeStream());
        DualStream stream = api.openStream(db);
        DualStream stream2 = api.openStream(db);
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[] data = "foo        bara".getBytes(StandardCharsets.UTF_8);
            byte[] data2 = "foo        bar ".getBytes(StandardCharsets.UTF_8);
            api.scanStream(null, stream, data, handler);
            assertThat(handler.count).isEqualTo(0);
            api.scanStream(null, stream2, data2, null);
            assertThat(handler.count).isEqualTo(0);
            api.closeStream(null, stream, handler);
            assertThat(handler.count).isEqualTo(1);
            api.closeStream(null, stream2, handler);
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.closeStream(null, stream, null);
            api.closeStream(null, stream2, null);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void multiStream2(DualApi api) {
        DualDatabase db = compileSingle(api, "foo.*bar.*\\b", EnumSet.noneOf(DualExpressionFlag.class), api.modeStream());
        DualStream stream = api.openStream(db);
        DualStream stream2 = api.openStream(db);
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[] data = "foo        bara".getBytes(StandardCharsets.UTF_8);
            byte[] data2 = "foo        bar ".getBytes(StandardCharsets.UTF_8);
            api.scanStream(null, stream2, data2, null);
            assertThat(handler.count).isEqualTo(0);
            api.scanStream(null, stream, data, handler);
            assertThat(handler.count).isEqualTo(0);
            api.closeStream(null, stream2, handler);
            assertThat(handler.count).isEqualTo(0);
            api.closeStream(null, stream, handler);
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.closeStream(null, stream, null);
            api.closeStream(null, stream2, null);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void regressionUe1005(DualApi api) {
        List<DualExpression> expressions = List.of(
                api.createExpression("match[^Z]*", EnumSet.of(DualExpressionFlag.DOTALL, DualExpressionFlag.SINGLEMATCH), 1),
                api.createExpression("[^Z]+\\z", EnumSet.of(DualExpressionFlag.DOTALL, DualExpressionFlag.SINGLEMATCH), 2),
                api.createExpression("[^Y]+\\z", EnumSet.of(DualExpressionFlag.DOTALL, DualExpressionFlag.SINGLEMATCH), 3)
        );
        DualDatabase db = compileMulti(api, expressions, api.modeStream());
        DualStream stream = api.openStreamRaw(db).stream();
        DualScanner scratch = api.getStreamScratch(stream);
        try {
            MatchCollector collector = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, "match".getBytes(StandardCharsets.UTF_8), scratch, collector)).isEqualTo(api.success());
            assertThat(api.closeStreamRaw(stream, scratch, collector)).isEqualTo(api.success());
            assertThat(collector.matches).containsExactlyInAnyOrder(
                    new MatchRecord(5, 1), new MatchRecord(5, 2), new MatchRecord(5, 3));
            stream = null;
            scratch = null;
        } finally {
            if (stream != null) {
                api.closeStreamRaw(stream, scratch, null);
            }
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void regressionUe2425(DualApi api) {
        String pattern = "(b|[cd](\\B|a){14}|[ba]cd.[^ece]b.[da]cbe|d[cad]cb.[da](cd|[abedc])|\\ba.edbac){18}";
        EnumSet<DualExpressionFlag> flags = EnumSet.of(DualExpressionFlag.DOTALL, DualExpressionFlag.CASELESS,
                DualExpressionFlag.SINGLEMATCH, DualExpressionFlag.UTF8, DualExpressionFlag.PREFILTER);
        DualDatabase db = compileSingle(api, pattern, flags, api.modeStream());
        api.closeDatabase(db);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void regressionUe2485(DualApi api) {
        String pattern = "(?:(.EeEa|((a{2}BD[bc]Bd[eae]|[DCd]|c|ebCa|d)){7,21})(E{5,}A{4,}[Cc].cc{3,6}|eCec|e+CaBEd|[Bb])){10}DB(a|[AAda])..A?DE?E";
        EnumSet<DualExpressionFlag> flags = EnumSet.of(DualExpressionFlag.DOTALL, DualExpressionFlag.CASELESS,
                DualExpressionFlag.UTF8, DualExpressionFlag.PREFILTER);
        DualDatabase db = compileSingle(api, pattern, flags, api.modeBlock());
        api.closeDatabase(db);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void regressionUe2452(DualApi api) {
        String pattern = "/ab.b[bca]{2,}ca((?:c|(abc(?sxmi-xm)){10,14}|c|b|[abcb])){4,23}acbcbb*ba((?:(a|.{4,}|.|[acba])){3,16}a)+";
        EnumSet<DualExpressionFlag> flags = EnumSet.of(DualExpressionFlag.MULTILINE, DualExpressionFlag.CASELESS,
                DualExpressionFlag.UTF8, DualExpressionFlag.UCP, DualExpressionFlag.PREFILTER);
        DualDatabase db = compileSingle(api, pattern, flags, api.modeStream());
        api.closeDatabase(db);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void regressionUe2595(DualApi api) {
        String pattern = "(?:(?:acAa|c[EAA]aEb|((?:CC[bdd].cE((?x-msix)BE){32}(?:\\B)){16,19}CdD.E(E|E|B)){3,6}|E(a|d|.)(?:(?xs-isxm)|b|.|C))){17,}";
        EnumSet<DualExpressionFlag> flags = EnumSet.of(DualExpressionFlag.MULTILINE, DualExpressionFlag.CASELESS,
                DualExpressionFlag.SINGLEMATCH, DualExpressionFlag.PREFILTER);
        DualDatabase db = compileSingle(api, pattern, flags, api.modeStream());
        api.closeDatabase(db);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void regressionUe2762(DualApi api) {
        List<DualExpression> expressions = List.of(
                api.createExpression("\\Aa\\z", EnumSet.of(DualExpressionFlag.MULTILINE), 1),
                api.createExpression("^a", EnumSet.of(DualExpressionFlag.MULTILINE), 2),
                api.createExpression("a|^a", EnumSet.of(DualExpressionFlag.MULTILINE, DualExpressionFlag.SOM_LEFTMOST), 3)
        );
        DualDatabase db = compileMulti(api, expressions, api.modeStream() | api.modeSomHorizonLarge());
        DualStream stream = api.openStreamRaw(db).stream();
        DualScanner scratch = api.getStreamScratch(stream);
        try {
            MatchCollector collector = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, "a".getBytes(StandardCharsets.UTF_8), scratch, collector)).isEqualTo(api.success());
            assertThat(api.closeStreamRaw(stream, scratch, collector)).isEqualTo(api.success());
            assertThat(collector.matches).containsExactlyInAnyOrder(
                    new MatchRecord(1, 1), new MatchRecord(1, 2), new MatchRecord(1, 3));
            stream = null;
            scratch = null;
        } finally {
            if (stream != null) {
                api.closeStreamRaw(stream, scratch, null);
            }
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void regressionUe2763(DualApi api) {
        List<DualExpression> expressions = List.of(
                api.createExpression("aaa.a+$", EnumSet.of(DualExpressionFlag.CASELESS), 1),
                api.createExpression("aaa.a", EnumSet.of(DualExpressionFlag.CASELESS, DualExpressionFlag.SINGLEMATCH, DualExpressionFlag.UTF8), 2)
        );
        DualDatabase db = compileMulti(api, expressions, api.modeStream());
        DualStream stream = api.openStreamRaw(db).stream();
        DualScanner scratch = api.getStreamScratch(stream);
        try {
            MatchCollector collector = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, "aAasaA".getBytes(StandardCharsets.UTF_8), scratch, collector)).isEqualTo(api.success());
            assertThat(api.closeStreamRaw(stream, scratch, collector)).isEqualTo(api.success());
            assertThat(collector.matches).containsExactlyInAnyOrder(
                    new MatchRecord(6, 1), new MatchRecord(5, 2));
            stream = null;
            scratch = null;
        } finally {
            if (stream != null) {
                api.closeStreamRaw(stream, scratch, null);
            }
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void regressionUe2798(DualApi api) {
        List<DualExpression> expressions = List.of(
                api.createExpression("([ab]b|aab+)$", EnumSet.of(DualExpressionFlag.DOTALL), 1),
                api.createExpression("ab+", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST), 2),
                api.createExpression("a(b.)?ba+b", EnumSet.noneOf(DualExpressionFlag.class), 3)
        );
        DualDatabase db = compileMulti(api, expressions, api.modeStream() | api.modeSomHorizonLarge());
        DualStream stream = api.openStreamRaw(db).stream();
        DualScanner scratch = api.getStreamScratch(stream);
        try {
            MatchCollector collector = new MatchCollector();
            assertThat(api.scanStreamRaw(stream, "ab_baab\n".getBytes(StandardCharsets.UTF_8), scratch, collector)).isEqualTo(api.success());
            assertThat(api.closeStreamRaw(stream, scratch, collector)).isEqualTo(api.success());
            assertThat(collector.matches).containsExactlyInAnyOrder(
                    new MatchRecord(7, 1), new MatchRecord(2, 2), new MatchRecord(7, 2), new MatchRecord(7, 3));
            stream = null;
            scratch = null;
        } finally {
            if (stream != null) {
                api.closeStreamRaw(stream, scratch, null);
            }
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void pcreSpaceNewPcre(DualApi api) {
        byte[] data = new byte[] {0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x20};
        DualDatabase db = compileSingle(api, "\\s", EnumSet.noneOf(DualExpressionFlag.class), api.modeBlock());
        DualScanner scanner = api.allocScratchRaw(db).scratch();
        try {
            CountingHandler handler = new CountingHandler(100);
            assertThat(api.scanRaw(scanner, db, data, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(data.length);
        } finally {
            api.freeScratchRaw(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void pcreSpaceNewPcreClass(DualApi api) {
        byte[] data = new byte[] {0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x20};
        DualDatabase db = compileSingle(api, "[\\s]", EnumSet.noneOf(DualExpressionFlag.class), api.modeBlock());
        DualScanner scanner = api.allocScratchRaw(db).scratch();
        try {
            CountingHandler handler = new CountingHandler(100);
            assertThat(api.scanRaw(scanner, db, data, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(data.length);
        } finally {
            api.freeScratchRaw(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void pcreSpaceNewPcreNeg(DualApi api) {
        byte[] data = new byte[] {0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x20};
        DualDatabase db = compileSingle(api, "\\S", EnumSet.noneOf(DualExpressionFlag.class), api.modeBlock());
        DualScanner scanner = api.allocScratchRaw(db).scratch();
        try {
            CountingHandler handler = new CountingHandler(100);
            assertThat(api.scanRaw(scanner, db, data, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(0);
        } finally {
            api.freeScratchRaw(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void pcreSpaceNewPcreClassNeg(DualApi api) {
        byte[] data = new byte[] {0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x20};
        DualDatabase db = compileSingle(api, "[\\S]", EnumSet.noneOf(DualExpressionFlag.class), api.modeBlock());
        DualScanner scanner = api.allocScratchRaw(db).scratch();
        try {
            CountingHandler handler = new CountingHandler(100);
            assertThat(api.scanRaw(scanner, db, data, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(0);
        } finally {
            api.freeScratchRaw(scanner);
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void parserNewlineTerminatedComment(DualApi api) {
        String pattern = "(?x)foo # initial comment \n bar # second comment \n baz";
        DualDatabase db = compileSingle(api, pattern, EnumSet.noneOf(DualExpressionFlag.class), api.modeBlock());
        DualScanner scanner = api.allocScratchRaw(db).scratch();
        try {
            CountingHandler handler = new CountingHandler(100);
            byte[] data = "foobarbaz".getBytes(StandardCharsets.UTF_8);
            assertThat(api.scanRaw(scanner, db, data, handler)).isEqualTo(api.success());
            assertThat(handler.count).isEqualTo(1);
        } finally {
            api.freeScratchRaw(scanner);
            api.closeDatabase(db);
        }
    }
}
