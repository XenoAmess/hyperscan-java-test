package com.xenoamess.hyperscan.smoke;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.assertj.core.api.Assertions.assertThat;

class PlatformSelectionTest {

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void platformIsValid(DualApi api) {
        assertThat(api.getPlatform()).isNotBlank();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void nativeLibraryLoads(DualApi api) {
        assertThat(api.getPlatform()).isNotBlank();
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void versionIsAvailable(DualApi api) {
        assertThat(api.getVersion()).isNotBlank().matches("\\d+\\.\\d+\\.\\d+.*");
    }
}
