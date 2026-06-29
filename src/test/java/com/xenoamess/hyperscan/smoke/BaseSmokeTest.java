package com.xenoamess.hyperscan.smoke;

import com.gliwka.hyperscan.jni.HyperscanNativeLoader;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseSmokeTest {

    @BeforeAll
    static void loadNativeLibrary() {
        HyperscanNativeLoader.load();
    }
}
