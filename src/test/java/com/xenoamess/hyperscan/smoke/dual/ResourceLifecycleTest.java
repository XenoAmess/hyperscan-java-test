package com.xenoamess.hyperscan.smoke.dual;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceLifecycleTest {

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void nativeResourcesCloseIdempotently(DualApi api) {
        try (DualDatabase database = api.compileDatabase(api.createExpression("foo"), DualMode.STREAM);
             DualStream stream = api.openStream(database);
             DualScanner borrowedScratch = api.getStreamScratch(stream)) {
            stream.close();
            stream.close();
            borrowedScratch.close();
            database.close();
            database.close();

            assertThatThrownBy(database::getSize).isInstanceOf(IllegalStateException.class);
            assertThatThrownBy(borrowedScratch::getSize).isInstanceOf(IllegalStateException.class);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void rawScratchCloseIsIdempotent(DualApi api) {
        DualCompileResult result = api.compileRaw("foo", 0, api.modeBlock());
        try (DualDatabase database = result.database()) {
            assertThat(result.code()).isEqualTo(api.success());
            DualScratchResult scratchResult = api.allocScratchRaw(database);
            try (DualScanner scratch = scratchResult.scratch()) {
                assertThat(scratchResult.code()).isEqualTo(api.success());
                scratch.close();
                scratch.close();
                database.close();
                database.close();

                assertThatThrownBy(scratch::getSize).isInstanceOf(IllegalStateException.class);
                assertThatThrownBy(database::getSize).isInstanceOf(IllegalStateException.class);
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void failedRawStreamCloseCanBeRetried(DualApi api) {
        DualCompileResult result = api.compileRaw("foo$", 0, api.modeStream());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase database = result.database();
        DualStreamResult streamResult = api.openStreamRaw(database);
        assertThat(streamResult.code()).isEqualTo(api.success());
        DualStream stream = streamResult.stream();

        try {
            assertThat(api.closeStreamRaw(stream, null, (expression, from, to) -> true))
                    .isEqualTo(api.invalid());
            assertThat(api.closeStreamRaw(stream, null, null)).isEqualTo(api.success());
            stream.close();
        } finally {
            stream.close();
            database.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void databaseCannotCloseWhileStreamIsOpen(DualApi api) {
        try (DualDatabase database = api.compileDatabase(api.createExpression("foo"), DualMode.STREAM);
             DualStream stream = api.openStream(database)) {
            assertThatThrownBy(database::close)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("open stream");
            assertThat(database.getSize()).isPositive();

            stream.close();
            database.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void copiedStreamRetainsDatabaseLease(DualApi api) {
        try (DualDatabase database = api.compileRaw("foo", 0, api.modeStream()).database();
             DualStream stream = api.openStreamRaw(database).stream()) {
            DualStream[] copyOut = new DualStream[1];
            assertThat(api.copyStreamRaw(copyOut, stream)).isEqualTo(api.success());
            try (DualStream copy = copyOut[0]) {
                assertThatThrownBy(database::close).isInstanceOf(IllegalStateException.class);
                stream.close();
                assertThatThrownBy(database::close).isInstanceOf(IllegalStateException.class);
                copy.close();
                database.close();

                assertThat(api.scanStreamRaw(stream, new byte[0], null, null)).isEqualTo(api.invalid());
                assertThat(api.resetStreamRaw(copy, null, null)).isEqualTo(api.invalid());
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void closedScannerCannotBeReused(DualApi api) {
        try (DualDatabase database = api.compileDatabase(api.createExpression("foo"), DualMode.BLOCK);
             DualScanner scanner = api.createScanner()) {
            scanner.close();
            scanner.close();

            assertThatThrownBy(scanner::getSize).isInstanceOf(IllegalStateException.class);
            assertThatThrownBy(() -> api.allocScratch(scanner, database))
                    .isInstanceOf(IllegalStateException.class);

            database.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void databaseCannotCloseFromBlockOrVectorCallback(DualApi api) {
        assertDatabaseCloseRejectedDuringScan(api, api.modeBlock(), false);
        assertDatabaseCloseRejectedDuringScan(api, api.modeVectored(), true);
    }

    private static void assertDatabaseCloseRejectedDuringScan(
            DualApi api, int mode, boolean vectored) {
        DualCompileResult compileResult = api.compileRaw("foo", 0, mode);
        try (DualDatabase database = compileResult.database()) {
            assertThat(compileResult.code()).isEqualTo(api.success());
            DualScratchResult scratchResult = api.allocScratchRaw(database);
            try (DualScanner scratch = scratchResult.scratch()) {
                assertThat(scratchResult.code()).isEqualTo(api.success());
                AtomicReference<Throwable> closeFailure = new AtomicReference<>();
                DualByteMatchHandler handler = (expression, from, to) -> {
                    try {
                        database.close();
                    } catch (Throwable failure) {
                        closeFailure.set(failure);
                    }
                    return true;
                };
                int result = vectored
                        ? api.scanVectorRaw(scratch, database, new byte[][]{"foo".getBytes()}, handler)
                        : api.scanRaw(scratch, database, "foo".getBytes(), handler);

                assertThat(result).isEqualTo(api.success());
                assertThat(closeFailure.get())
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("active operation");
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void streamCannotCloseFromOwnCallback(DualApi api) {
        DualCompileResult compileResult = api.compileRaw("foo", 0, api.modeStream());
        try (DualDatabase database = compileResult.database()) {
            assertThat(compileResult.code()).isEqualTo(api.success());
            DualScratchResult scratchResult = api.allocScratchRaw(database);
            DualStreamResult streamResult = api.openStreamRaw(database);
            try (DualScanner scratch = scratchResult.scratch();
                 DualStream stream = streamResult.stream()) {
                assertThat(scratchResult.code()).isEqualTo(api.success());
                assertThat(streamResult.code()).isEqualTo(api.success());
                AtomicInteger nestedCloseResult = new AtomicInteger(api.success());

                assertThat(api.scanStreamRaw(stream, "foo".getBytes(), scratch,
                        (expression, from, to) -> {
                            nestedCloseResult.set(api.closeStreamRaw(stream, scratch, null));
                            return true;
                        })).isEqualTo(api.success());
                assertThat(nestedCloseResult).hasValue(api.invalid());
                assertThat(api.closeStreamRaw(stream, scratch, null)).isEqualTo(api.success());
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void copiedStreamUsesSuppliedScratchInPublicApi(DualApi api) {
        DualCompileResult compileResult = api.compileRaw("foo$", 0, api.modeStream());
        try (DualDatabase database = compileResult.database()) {
            assertThat(compileResult.code()).isEqualTo(api.success());
            DualScratchResult scratchResult = api.allocScratchRaw(database);
            DualStreamResult streamResult = api.openStreamRaw(database);
            try (DualScanner scratch = scratchResult.scratch();
                 DualStream source = streamResult.stream()) {
                assertThat(scratchResult.code()).isEqualTo(api.success());
                assertThat(streamResult.code()).isEqualTo(api.success());
                DualStream[] copyOut = new DualStream[1];
                assertThat(api.copyStreamRaw(copyOut, source)).isEqualTo(api.success());
                try (DualStream copy = copyOut[0]) {
                    AtomicInteger matches = new AtomicInteger();
                    DualByteMatchHandler handler = (expression, from, to) -> {
                        matches.incrementAndGet();
                        return true;
                    };
                    api.scanStream(scratch, copy, "foo".getBytes(), handler);
                    api.closeStream(scratch, copy, handler);
                    assertThat(matches).hasValue(1);
                }
            }
        }
    }
}
