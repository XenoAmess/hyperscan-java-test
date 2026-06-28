package com.xenoamess.hyperscan.smoke;

import com.xenoamess.hyperscan.jni.HyperscanNativeLoader;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseSmokeTest {

    @BeforeAll
    static void loadNativeLibrary() {
        String hsPlatform = System.getProperty("hs.platform");
        System.out.println("BaseSmokeTest hs.platform=" + hsPlatform);
        System.out.println("BaseSmokeTest org.bytedeco.javacpp.platform before=" + System.getProperty("org.bytedeco.javacpp.platform"));
        if (hsPlatform == null || hsPlatform.isEmpty()) {
            HyperscanNativeLoader.load();
            System.out.println("BaseSmokeTest loaded default, platform after=" + System.getProperty("org.bytedeco.javacpp.platform"));
            return;
        }

        String originalPlatform = System.getProperty("org.bytedeco.javacpp.platform");
        if (originalPlatform == null) {
            originalPlatform = Loader.getPlatform();
            System.setProperty("org.bytedeco.javacpp.platform", originalPlatform);
        }
        System.out.println("BaseSmokeTest originalPlatform=" + originalPlatform);

        try (BytePointer ignored = new BytePointer(1)) {
            // Trigger loading of the JavaCPP core native library using the base platform.
        }

        System.setProperty("org.bytedeco.javacpp.platform", hsPlatform);
        HyperscanNativeLoader.load();
        System.setProperty("org.bytedeco.javacpp.platform", hsPlatform);
        System.out.println("BaseSmokeTest loaded variant, platform after=" + System.getProperty("org.bytedeco.javacpp.platform"));
    }
}
