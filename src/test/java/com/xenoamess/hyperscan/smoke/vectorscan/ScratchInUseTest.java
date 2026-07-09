package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualImplementation;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualScratchResult;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
import com.xenoamess.hyperscan.smoke.dual.DualStreamResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class ScratchInUseTest {

    private static final DualByteMatchHandler DUMMY = (expr, from, to) -> true;

    private static DualDatabase compileBlock(DualApi api, String pattern) {
        DualCompileResult result = api.compileRaw(pattern, 0, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        return db;
    }

    private static DualDatabase compileStream(DualApi api, String pattern) {
        DualCompileResult result = api.compileRaw(pattern, 0, api.modeStream());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        return db;
    }

    private static DualDatabase compileVectored(DualApi api, String pattern) {
        DualCompileResult result = api.compileRaw(pattern, 0, api.modeVectored());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        return db;
    }

    private static DualScanner allocScratch(DualApi api, DualDatabase db) {
        DualScratchResult result = api.allocScratchRaw(db);
        assertThat(result.code()).isEqualTo(api.success());
        DualScanner scanner = result.scratch();
        assertThat(scanner).isNotNull();
        return scanner;
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void block(DualApi api) {
        DualDatabase db = compileBlock(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        try {
            byte[] data = "___foo___bar_".getBytes(StandardCharsets.UTF_8);
            AtomicInteger matches = new AtomicInteger();
            DualByteMatchHandler handler = (expr, from, to) -> {
                matches.incrementAndGet();
                int reentrant = api.scanRaw(scanner, db, data, DUMMY);
                assertThat(reentrant).isEqualTo(api.scratchInUse());
                return true;
            };
            int result = api.scanRaw(scanner, db, data, handler);
            assertThat(result).isEqualTo(api.success());
            assertThat(matches.get()).isEqualTo(1);
        } finally {
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void streaming(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        DualStreamResult streamResult = api.openStreamRaw(db);
        assertThat(streamResult.code()).isEqualTo(api.success());
        try (DualStream stream = streamResult.stream()) {
            assertThat(stream).isNotNull();
            byte[] data = "___foo___bar_".getBytes(StandardCharsets.UTF_8);
            AtomicInteger matches = new AtomicInteger();
            DualByteMatchHandler handler = (expr, from, to) -> {
                matches.incrementAndGet();
                DualStreamResult newStreamResult = api.openStreamRaw(db);
                assertThat(newStreamResult.code()).isEqualTo(api.success());
                try (DualStream newStream = newStreamResult.stream()) {
                    assertThat(newStream).isNotNull();
                    int reentrant = api.scanStreamRaw(newStream, data, scanner, DUMMY);
                    assertThat(reentrant).isEqualTo(api.scratchInUse());
                }
                return true;
            };
            int result = api.scanStreamRaw(stream, data, scanner, handler);
            assertThat(result).isEqualTo(api.success());
            assertThat(matches.get()).isEqualTo(1);
        } finally {
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void vectored(DualApi api) {
        DualDatabase db = compileVectored(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        try {
            byte[] data1 = "___foo_".getBytes(StandardCharsets.UTF_8);
            byte[] data2 = "bar_".getBytes(StandardCharsets.UTF_8);
            byte[][] data = new byte[][] { data1, data2 };
            AtomicInteger matches = new AtomicInteger();
            DualByteMatchHandler handler = (expr, from, to) -> {
                matches.incrementAndGet();
                int reentrant = api.scanVectorRaw(scanner, db, data, DUMMY);
                assertThat(reentrant).isEqualTo(api.scratchInUse());
                return true;
            };
            int result = api.scanVectorRaw(scanner, db, data, handler);
            assertThat(result).isEqualTo(api.success());
            assertThat(matches.get()).isEqualTo(1);
        } finally {
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void reallocScratchBlock(DualApi api) {
        DualDatabase db = compileBlock(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        try {
            byte[] data = "___foo___bar_".getBytes(StandardCharsets.UTF_8);
            AtomicInteger matches = new AtomicInteger();
            DualByteMatchHandler handler = (expr, from, to) -> {
                matches.incrementAndGet();
                int reentrant = api.allocScratchRaw(db, scanner).code();
                assertThat(reentrant).isEqualTo(api.scratchInUse());
                return true;
            };
            int result = api.scanRaw(scanner, db, data, handler);
            assertThat(result).isEqualTo(api.success());
            assertThat(matches.get()).isEqualTo(1);
        } finally {
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void reallocScratchStreaming(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        DualStreamResult streamResult = api.openStreamRaw(db);
        assertThat(streamResult.code()).isEqualTo(api.success());
        try (DualStream stream = streamResult.stream()) {
            assertThat(stream).isNotNull();
            byte[] data = "___foo___bar_".getBytes(StandardCharsets.UTF_8);
            AtomicInteger matches = new AtomicInteger();
            DualByteMatchHandler handler = (expr, from, to) -> {
                matches.incrementAndGet();
                int reentrant = api.allocScratchRaw(db, scanner).code();
                assertThat(reentrant).isEqualTo(api.scratchInUse());
                return true;
            };
            int result = api.scanStreamRaw(stream, data, scanner, handler);
            assertThat(result).isEqualTo(api.success());
            assertThat(matches.get()).isEqualTo(1);
        } finally {
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void reallocScratchVector(DualApi api) {
        DualDatabase db = compileVectored(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        try {
            byte[] data1 = "___foo_".getBytes(StandardCharsets.UTF_8);
            byte[] data2 = "bar_".getBytes(StandardCharsets.UTF_8);
            byte[][] data = new byte[][] { data1, data2 };
            AtomicInteger matches = new AtomicInteger();
            DualByteMatchHandler handler = (expr, from, to) -> {
                matches.incrementAndGet();
                int reentrant = api.allocScratchRaw(db, scanner).code();
                assertThat(reentrant).isEqualTo(api.scratchInUse());
                return true;
            };
            int result = api.scanVectorRaw(scanner, db, data, handler);
            assertThat(result).isEqualTo(api.success());
            assertThat(matches.get()).isEqualTo(1);
        } finally {
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void freeScratchBlock(DualApi api) {
        DualDatabase db = compileBlock(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        try {
            byte[] data = "___foo___bar_".getBytes(StandardCharsets.UTF_8);
            AtomicInteger matches = new AtomicInteger();
            DualByteMatchHandler handler = (expr, from, to) -> {
                matches.incrementAndGet();
                int reentrant = api.freeScratchRaw(scanner);
                assertThat(reentrant).isEqualTo(api.scratchInUse());
                return true;
            };
            int result = api.scanRaw(scanner, db, data, handler);
            assertThat(result).isEqualTo(api.success());
            assertThat(matches.get()).isEqualTo(1);
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void freeScratchStreaming(DualApi api) {
        DualDatabase db = compileStream(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        DualStreamResult streamResult = api.openStreamRaw(db);
        assertThat(streamResult.code()).isEqualTo(api.success());
        try (DualStream stream = streamResult.stream()) {
            assertThat(stream).isNotNull();
            byte[] data = "___foo___bar_".getBytes(StandardCharsets.UTF_8);
            AtomicInteger matches = new AtomicInteger();
            DualByteMatchHandler handler = (expr, from, to) -> {
                matches.incrementAndGet();
                int reentrant = api.freeScratchRaw(scanner);
                assertThat(reentrant).isEqualTo(api.scratchInUse());
                return true;
            };
            int result = api.scanStreamRaw(stream, data, scanner, handler);
            assertThat(result).isEqualTo(api.success());
            assertThat(matches.get()).isEqualTo(1);
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void freeScratchVector(DualApi api) {
        DualDatabase db = compileVectored(api, "foo.*bar");
        DualScanner scanner = allocScratch(api, db);
        try {
            byte[] data1 = "___foo_".getBytes(StandardCharsets.UTF_8);
            byte[] data2 = "bar_".getBytes(StandardCharsets.UTF_8);
            byte[][] data = new byte[][] { data1, data2 };
            AtomicInteger matches = new AtomicInteger();
            DualByteMatchHandler handler = (expr, from, to) -> {
                matches.incrementAndGet();
                int reentrant = api.freeScratchRaw(scanner);
                assertThat(reentrant).isEqualTo(api.scratchInUse());
                return true;
            };
            int result = api.scanVectorRaw(scanner, db, data, handler);
            assertThat(result).isEqualTo(api.success());
            assertThat(matches.get()).isEqualTo(1);
        } finally {
            api.closeDatabase(db);
        }
    }
}
