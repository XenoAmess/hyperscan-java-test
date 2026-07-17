package com.xenoamess.hyperscan.smoke;

import org.junit.jupiter.api.BeforeAll;

public abstract class BaseSmokeTest {

    @BeforeAll
    static void loadNativeLibrary() {
        HyperscanTestHelper.loadNativeLibrary();
    }
}
