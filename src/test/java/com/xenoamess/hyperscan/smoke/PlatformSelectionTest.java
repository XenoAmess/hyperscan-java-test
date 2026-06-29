package com.xenoamess.hyperscan.smoke;

import com.gliwka.hyperscan.jni.HyperscanNativeLoader;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlatformSelectionTest {

    @Test
    void platformIsValid() {
        assertThat(HyperscanNativeLoader.selectPlatform()).isNotBlank();
    }

    @Test
    void nativeLibraryLoads() {
        HyperscanNativeLoader.load();
        String actual = System.getProperty("org.bytedeco.javacpp.platform");
        assertThat(actual).isNotNull();
    }

    @Test
    void explicitPlatformOverridesSelection() {
        String explicit = System.getProperty("org.bytedeco.javacpp.platform");
        if (explicit != null) {
            HyperscanNativeLoader.load();
            String actual = System.getProperty("org.bytedeco.javacpp.platform");
            assertThat(actual).isEqualTo(explicit);
        }
    }
}
