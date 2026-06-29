package com.xenoamess.hyperscan.smoke;

import com.gliwka.hyperscan.jni.hs_database_t;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.gliwka.hyperscan.jni.hyperscan.HS_FLAG_SOM_LEFTMOST;
import static org.assertj.core.api.Assertions.assertThat;

class SyntheticDataTest extends BaseSmokeTest {

    @Test
    void manyRandomLiteralPatterns() {
        int count = 200;
        String[] patterns = new String[count];
        int[] ids = new int[count];
        int[] flags = new int[count];

        List<String> expectedMatches = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String literal = "LIT_" + UUID.randomUUID().toString().replace("-", "");
            patterns[i] = Pattern.quote(literal);
            ids[i] = i;
            flags[i] = HS_FLAG_SOM_LEFTMOST;
            if (i % 7 == 0) {
                expectedMatches.add(literal);
            }
        }

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            StringBuilder input = new StringBuilder();
            input.append("noise_");
            for (String literal : expectedMatches) {
                input.append(literal).append("_sep_");
            }
            input.append("trailing");

            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, input.toString());
            assertThat(matches).hasSize(expectedMatches.size());
            for (HyperscanTestHelper.Match match : matches) {
                assertThat(match.id % 7).isEqualTo(0);
            }
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }

    @Test
    void characterClassPatternsOnRandomAlphanumericInput() {
        String[] patterns = {
                "[0-9]{5,}",
                "[A-Z]{4,}",
                "[a-z]{6,}",
                "[0-9a-f]{32}",
        };
        int[] ids = {1, 2, 3, 4};
        int[] flags = {HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST};

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            Random random = new Random(42);
            StringBuilder input = new StringBuilder();
            for (int i = 0; i < 5000; i++) {
                input.append((char) ('a' + random.nextInt(26)));
            }
            input.append(" 12345 ABCDE lowercase ");
            input.append(UUID.randomUUID().toString().replace("-", ""));

            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, input.toString());
            assertThat(matches).isNotEmpty();
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }

    @Test
    void longInputScanDoesNotCrash() {
        String[] patterns = {"needle"};
        int[] ids = {1};
        int[] flags = {HS_FLAG_SOM_LEFTMOST};

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            StringBuilder input = new StringBuilder();
            for (int i = 0; i < 100_000; i++) {
                input.append("haystack ");
            }
            input.append("needle");
            for (int i = 0; i < 10_000; i++) {
                input.append(" tail");
            }

            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, input.toString());
            assertThat(matches).hasSize(1);
            assertThat(matches.get(0).id).isEqualTo(1);
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }

    @Test
    void repeatedPatternIdsAreAccepted() {
        String[] patterns = {"foo", "bar", "foo"};
        int[] ids = {1, 2, 1};
        int[] flags = {HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST};

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, "foo bar foo");
            assertThat(matches).hasSize(3);
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }
}
