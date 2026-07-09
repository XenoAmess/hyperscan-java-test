package com.xenoamess.hyperscan.smoke.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class TestData {

    private static final String BASE = "/vectorscan/datafiles/";

    public static final String LEIPZIG_3200 = "leipzig-3200.txt";
    public static final String LH3LH3_REB_HOWTO = "lh3lh3-reb-howto.txt";

    private TestData() {
    }

    public static InputStream stream(String name) {
        InputStream in = TestData.class.getResourceAsStream(BASE + name);
        if (in == null) {
            throw new IllegalArgumentException("Test data not found: " + BASE + name);
        }
        return in;
    }

    public static byte[] readBytes(String name) {
        try (InputStream in = stream(name)) {
            return in.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read test data: " + name, e);
        }
    }

    public static String readString(String name) {
        return new String(readBytes(name), StandardCharsets.UTF_8);
    }
}
