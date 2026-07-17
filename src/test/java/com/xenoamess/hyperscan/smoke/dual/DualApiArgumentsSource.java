package com.xenoamess.hyperscan.smoke.dual;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class DualApiArgumentsSource implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        // UPSTREAM is a benchmark-only comparison target and must not
        // participate in dual-run functional tests.
        return Stream.of(DualImplementation.JAVACPP, DualImplementation.PANAMA)
                .map(impl -> Arguments.of(impl.createAdapter()));
    }
}
