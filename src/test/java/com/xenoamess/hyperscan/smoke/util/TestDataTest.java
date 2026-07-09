package com.xenoamess.hyperscan.smoke.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestDataTest {

    @Test
    void leipzig3200IsReadable() {
        byte[] bytes = TestData.readBytes(TestData.LEIPZIG_3200);
        assertThat(bytes).isNotEmpty();
        assertThat(bytes.length).isGreaterThan(1_000_000);
    }

    @Test
    void lh3lh3RebHowtoIsReadable() {
        byte[] bytes = TestData.readBytes(TestData.LH3LH3_REB_HOWTO);
        assertThat(bytes).isNotEmpty();
        assertThat(bytes.length).isGreaterThan(1_000_000);
    }
}
