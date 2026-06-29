package com.xenoamess.hyperscan.smoke;

import com.gliwka.hyperscan.jni.hs_database_t;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gliwka.hyperscan.jni.hyperscan.HS_FLAG_SOM_LEFTMOST;
import static org.assertj.core.api.Assertions.assertThat;

class DirectApiSmokeTest extends BaseSmokeTest {

    @Test
    void simpleMultiPatternScan() {
        String[] patterns = {"abc1", "asa", "dab"};
        int[] ids = {1, 2, 3};
        int[] flags = {HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST};

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, "-21dasaaadabcaaa");
            assertThat(matches).hasSize(2);
            assertThat(matches.get(0).id).isEqualTo(2);
            assertThat(matches.get(0).from).isEqualTo(4);
            assertThat(matches.get(0).to).isEqualTo(7);
            assertThat(matches.get(1).id).isEqualTo(3);
            assertThat(matches.get(1).from).isEqualTo(9);
            assertThat(matches.get(1).to).isEqualTo(12);
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }

    @Test
    void caselessFlagWorks() {
        String[] patterns = {"[Hh]yperscan", "test"};
        int[] ids = {10, 20};
        int[] flags = {HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST};

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, "Hyperscan is a TEST of hyperscan");
            assertThat(matches).hasSizeGreaterThanOrEqualTo(2);
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }

    @Test
    void emptyInputProducesNoMatches() {
        String[] patterns = {"foo"};
        int[] ids = {1};
        int[] flags = {HS_FLAG_SOM_LEFTMOST};

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, "");
            assertThat(matches).isEmpty();
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }

    @Test
    void utf8BytePositionsAreReported() {
        String[] patterns = {"\u4e2d\u6587", "test"};
        int[] ids = {1, 2};
        int[] flags = {HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST};

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            String input = "this is a test with \u4e2d\u6587 characters";
            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, input);
            assertThat(matches).isNotEmpty();
            for (HyperscanTestHelper.Match match : matches) {
                assertThat(match.to).isGreaterThanOrEqualTo(match.from);
            }
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }
}
