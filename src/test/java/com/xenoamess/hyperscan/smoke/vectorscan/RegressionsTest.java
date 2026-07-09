package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualScratchResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class RegressionsTest {

    private static byte[] readResource(String name) throws IOException {
        try (InputStream in = RegressionsTest.class.getResourceAsStream(name)) {
            assertThat(in).isNotNull();
            return in.readAllBytes();
        }
    }

    private static byte[] utf8LossyDecode(byte[] input) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(input.length);
        for (int i = 0; i < input.length; i++) {
            int b = input[i] & 0xFF;
            if (b < 0x80) {
                out.write(b);
            } else if (b < 0xC0) {
                out.write(0xEF);
                out.write(0xBF);
                out.write(0xBD);
            } else if (b < 0xE0) {
                if (i + 1 < input.length && (input[i + 1] & 0xC0) == 0x80) {
                    out.write(b);
                    out.write(input[i + 1]);
                    i++;
                } else {
                    out.write(0xEF);
                    out.write(0xBF);
                    out.write(0xBD);
                }
            } else if (b < 0xF0) {
                if (i + 2 < input.length && (input[i + 1] & 0xC0) == 0x80
                        && (input[i + 2] & 0xC0) == 0x80) {
                    out.write(b);
                    out.write(input[i + 1]);
                    out.write(input[i + 2]);
                    i += 2;
                } else {
                    out.write(0xEF);
                    out.write(0xBF);
                    out.write(0xBD);
                }
            } else {
                out.write(0xEF);
                out.write(0xBF);
                out.write(0xBD);
            }
        }
        return out.toByteArray();
    }

    private static int countMatches(DualApi api, DualScanner scanner, DualDatabase db, byte[] data) {
        AtomicInteger counter = new AtomicInteger();
        int result = api.scanRaw(scanner, db, data, (expr, from, to) -> {
            counter.incrementAndGet();
            return true;
        });
        assertThat(result).isEqualTo(api.success());
        return counter.get();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void bug317(DualApi api) {
        EnumSet<DualExpressionFlag> flags = EnumSet.of(
                DualExpressionFlag.SINGLEMATCH,
                DualExpressionFlag.ALLOWEMPTY,
                DualExpressionFlag.UTF8,
                DualExpressionFlag.PREFILTER);
        List<DualExpression> expressions = List.of(
                api.createExpression("^muvoy-nyemcynjywynamlahi/nyzye/khjdrehko-(qjhn|lyol)-.*/0$", flags, 0),
                api.createExpression("^cop/devel/workflows-(prod|test)-.*/[0-9]+$", flags, 1));
        DualCompileResult result = api.compileRaw(expressions, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        DualScratchResult scratchResult = api.allocScratchRaw(db);
        assertThat(scratchResult.code()).isEqualTo(api.success());
        DualScanner scanner = scratchResult.scratch();
        try {
            AtomicBoolean matchFound = new AtomicBoolean();
            int scanResult = api.scanRaw(scanner, db, "cop/devel/workflows-prod-build-cop-cop-ingestor/0".getBytes(),
                    (expr, from, to) -> {
                        matchFound.set(true);
                        return true;
                    });
            assertThat(scanResult).isEqualTo(api.success());
            assertThat(matchFound).isTrue();
        } finally {
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Tag("large-input")
    void leipzigMathSymbolsCount(DualApi api) throws IOException {
        EnumSet<DualExpressionFlag> flags = EnumSet.of(DualExpressionFlag.UCP, DualExpressionFlag.UTF8);
        DualCompileResult result = api.compileRaw("\\p{Sm}", api.flagsToBits(flags), api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        DualScratchResult scratchResult = api.allocScratchRaw(db);
        assertThat(scratchResult.code()).isEqualTo(api.success());
        DualScanner scanner = scratchResult.scratch();
        try {
            byte[] data = readResource("/vectorscan/datafiles/leipzig-3200.txt");
            assertThat(countMatches(api, scanner, db, data)).isEqualTo(69);
        } finally {
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Tag("large-input")
    void lh3lh3RebUriOrEmailGrep(DualApi api) throws IOException {
        DualCompileResult result = api.compileRaw(
                "([a-zA-Z][a-zA-Z0-9]*)://([^ /]+)(/[^ ]*)?|([^ @]+)@([^ @]+)",
                0, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        DualScratchResult scratchResult = api.allocScratchRaw(db);
        assertThat(scratchResult.code()).isEqualTo(api.success());
        DualScanner scanner = scratchResult.scratch();
        try {
            byte[] raw = readResource("/vectorscan/datafiles/lh3lh3-reb-howto.txt");
            byte[] data = utf8LossyDecode(raw);
            assertThat(countMatches(api, scanner, db, data)).isEqualTo(888987);
        } finally {
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Tag("large-input")
    void lh3lh3RebEmailGrep(DualApi api) throws IOException {
        DualCompileResult result = api.compileRaw("([^ @]+)@([^ @]+)", 0, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        DualScratchResult scratchResult = api.allocScratchRaw(db);
        assertThat(scratchResult.code()).isEqualTo(api.success());
        DualScanner scanner = scratchResult.scratch();
        try {
            byte[] raw = readResource("/vectorscan/datafiles/lh3lh3-reb-howto.txt");
            byte[] data = utf8LossyDecode(raw);
            assertThat(countMatches(api, scanner, db, data)).isEqualTo(232354);
        } finally {
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    @Tag("large-input")
    void lh3lh3RebDateGrep(DualApi api) throws IOException {
        DualCompileResult result = api.compileRaw(
                "([0-9][0-9]?)/([0-9][0-9]?)/([0-9][0-9]([0-9][0-9])?)",
                0, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        DualDatabase db = result.database();
        assertThat(db).isNotNull();
        DualScratchResult scratchResult = api.allocScratchRaw(db);
        assertThat(scratchResult.code()).isEqualTo(api.success());
        DualScanner scanner = scratchResult.scratch();
        try {
            byte[] raw = readResource("/vectorscan/datafiles/lh3lh3-reb-howto.txt");
            byte[] data = utf8LossyDecode(raw);
            assertThat(countMatches(api, scanner, db, data)).isEqualTo(819);
        } finally {
            assertThat(api.freeScratchRaw(scanner)).isEqualTo(api.success());
            api.closeDatabase(db);
        }
    }
}
