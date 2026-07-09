package com.xenoamess.hyperscan.smoke.dual;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public final class DualArguments {

    private DualArguments() {
    }

    public static Stream<Arguments> withStrings(String... values) {
        return Stream.of(DualImplementation.values())
                .flatMap(impl -> Stream.of(values)
                        .map(value -> Arguments.of(impl.createAdapter(), value)));
    }

    public static Stream<Arguments> dualApiOnly() {
        return Stream.of(DualImplementation.values())
                .map(impl -> Arguments.of(impl.createAdapter()));
    }
}
