package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualByteMatchHandler;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualScratchResult;
import com.xenoamess.hyperscan.smoke.dual.DualStream;
import com.xenoamess.hyperscan.smoke.dual.DualStreamResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class ScratchOpTest {

    private static final DualByteMatchHandler DUMMY = (expr, from, to) -> true;

    private static final String LARGE_PATTERN = "(a.?b.?c.?d.?e.?f.?g)|(hatstand(..)+teakettle)";

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void tooSmallForDatabase(DualApi api) {
        DualCompileResult result1 = api.compileRaw("foobar", 0, api.modeBlock());
        assertThat(result1.code()).isEqualTo(api.success());
        DualDatabase db1 = result1.database();
        DualScratchResult scratchResult1 = api.allocScratchRaw(db1);
        assertThat(scratchResult1.code()).isEqualTo(api.success());
        DualScanner scanner = scratchResult1.scratch();
        api.closeDatabase(db1);
        DualCompileResult result2 = api.compileRaw(LARGE_PATTERN, 0, api.modeBlock());
        assertThat(result2.code()).isEqualTo(api.success());
        DualDatabase db2 = result2.database();
        try {
            byte[] data = "somedata".getBytes(StandardCharsets.UTF_8);
            int invalid = api.scanRaw(scanner, db2, data, DUMMY);
            assertThat(invalid).isEqualTo(api.invalid());
            DualScratchResult scratchResult2 = api.allocScratchRaw(db2, scanner);
            assertThat(scratchResult2.code()).isEqualTo(api.success());
            DualScanner scanner2 = scratchResult2.scratch();
            int success = api.scanRaw(scanner2, db2, data, DUMMY);
            assertThat(success).isEqualTo(api.success());
            assertThat(api.freeScratchRaw(scanner2)).isEqualTo(api.success());
        } finally {
            api.closeDatabase(db2);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void tooSmallForDatabase2(DualApi api) {
        DualCompileResult result1 = api.compileRaw("foobar", 0, api.modeStream());
        assertThat(result1.code()).isEqualTo(api.success());
        DualDatabase db1 = result1.database();
        DualScratchResult scratchResult1 = api.allocScratchRaw(db1);
        assertThat(scratchResult1.code()).isEqualTo(api.success());
        DualScanner scanner = scratchResult1.scratch();
        api.closeDatabase(db1);
        DualCompileResult result2 = api.compileRaw(LARGE_PATTERN, 0, api.modeStream());
        assertThat(result2.code()).isEqualTo(api.success());
        DualDatabase db2 = result2.database();
        try {
            DualStreamResult streamResult = api.openStreamRaw(db2);
            assertThat(streamResult.code()).isEqualTo(api.success());
            DualStream stream = streamResult.stream();
            assertThat(stream).isNotNull();
            try (stream) {
                byte[] data = "somedata".getBytes(StandardCharsets.UTF_8);
                int invalid = api.scanStreamRaw(stream, data, scanner, DUMMY);
                assertThat(invalid).isEqualTo(api.invalid());
                DualScratchResult scratchResult2 = api.allocScratchRaw(db2, scanner);
                assertThat(scratchResult2.code()).isEqualTo(api.success());
                DualScanner scanner2 = scratchResult2.scratch();
                int success = api.scanStreamRaw(stream, data, scanner2, DUMMY);
                assertThat(success).isEqualTo(api.success());
                assertThat(api.closeStreamRaw(stream, null, null)).isEqualTo(api.success());
                assertThat(api.freeScratchRaw(scanner2)).isEqualTo(api.success());
            }
        } finally {
            api.closeDatabase(db2);
        }
    }
}
