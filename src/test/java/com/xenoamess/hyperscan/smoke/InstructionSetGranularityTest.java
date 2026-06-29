package com.xenoamess.hyperscan.smoke;

import com.gliwka.hyperscan.jni.hs_database_t;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.gliwka.hyperscan.jni.hyperscan.HS_FLAG_SOM_LEFTMOST;
import static org.assertj.core.api.Assertions.assertThat;

class InstructionSetGranularityTest extends BaseSmokeTest {

    @Test
    void benchmarkReport() {
        String platform = System.getProperty("org.bytedeco.javacpp.platform");
        System.out.println("=== ISA granularity benchmark on platform: " + platform + " ===");

        String[] patterns = buildMixedPatterns(500);
        int[] ids = new int[patterns.length];
        int[] flags = new int[patterns.length];
        for (int i = 0; i < patterns.length; i++) {
            ids[i] = i;
            flags[i] = HS_FLAG_SOM_LEFTMOST;
        }

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            String input = buildMixedInput(20_000, 50);

            long start = System.nanoTime();
            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, input);
            long elapsed = System.nanoTime() - start;

            System.out.println("Patterns: " + patterns.length);
            System.out.println("Input bytes: " + input.length());
            System.out.println("Matches: " + matches.size());
            System.out.println("Elapsed ms: " + (elapsed / 1_000_000.0));
            System.out.println("Throughput MB/s: " + (input.length() * 1_000.0 / elapsed));

            assertThat(matches).isNotNull();
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }

    private String[] buildMixedPatterns(int count) {
        String[] patterns = new String[count];
        patterns[0] = "[0-9]{3}-[0-9]{2}-[0-9]{4}";
        patterns[1] = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
        patterns[2] = "https?://[^\\s]+";
        patterns[3] = "\\bERROR\\b";
        patterns[4] = "\\bWARNING\\b";
        for (int i = 5; i < count; i++) {
            String token = UUID.randomUUID().toString().substring(0, 8);
            patterns[i] = Pattern.quote("TOKEN_" + token);
        }
        return patterns;
    }

    private String buildMixedInput(int size, int seedCount) {
        Random random = new Random(2026);
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
                sb.append("TOKEN_").append(UUID.randomUUID().toString().substring(0, 8)).append(" ");
                seedCount--;
            } else {
                sb.append(fragments[random.nextInt(fragments.length)]);
            }
        }
        return sb.toString();
    }
}
