package com.xenoamess.hyperscan.smoke;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.JavaCppAdapter;
import org.bytedeco.javacpp.Loader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class PlatformSelectionTest {

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void platformIsValid(DualApi api) {
        assertThat(api.getPlatform()).isNotBlank();
        String requested = System.getProperty("javacpp.platform");
        if (requested != null && !requested.isBlank()) {
            assertThat(api.getPlatform()).isEqualTo(requested);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void nativeLibraryLoads(DualApi api) {
        assertThat(api.getPlatform()).isNotBlank();
        if (api instanceof JavaCppAdapter) {
            String expectedPathPart = "/" + api.getPlatform() + "/";
            assertThat(Loader.getLoadedLibraries().values())
                    .anyMatch(path -> path != null
                            && path.replace(File.separatorChar, '/').contains(expectedPathPart));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void versionIsAvailable(DualApi api) {
        assertThat(api.getVersion()).isNotBlank().matches("\\d+\\.\\d+\\.\\d+.*");
    }
}
