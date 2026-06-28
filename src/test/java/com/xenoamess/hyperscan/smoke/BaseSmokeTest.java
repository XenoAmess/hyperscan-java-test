package com.xenoamess.hyperscan.smoke;

import com.xenoamess.hyperscan.jni.HyperscanNativeLoader;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseSmokeTest {

    @BeforeAll
    static void loadNativeLibrary() {
        String hsPlatform = System.getProperty("hs.platform");
        if (hsPlatform == null || hsPlatform.isEmpty()) {
            HyperscanNativeLoader.load();
            return;
        }

        String originalPlatform = System.getProperty("org.bytedeco.javacpp.platform");
        if (originalPlatform == null) {
            originalPlatform = Loader.getPlatform();
            System.setProperty("org.bytedeco.javacpp.platform", originalPlatform);
        }

        try (BytePointer ignored = new BytePointer(1)) {
            // Trigger loading of the JavaCPP core native library using the base platform.
        }

        System.setProperty("org.bytedeco.javacpp.platform", hsPlatform);
        HyperscanNativeLoader.load();

        System.setProperty("org.bytedeco.javacpp.platform", hsPlatform);
    }
}
