package com.xenoamess.hyperscan.smoke;

import com.gliwka.hyperscan.jni.hs_compile_error_t;
import com.gliwka.hyperscan.jni.hs_database_t;
import com.gliwka.hyperscan.jni.hs_scratch_t;
import com.gliwka.hyperscan.jni.match_event_handler;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.Cast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.gliwka.hyperscan.jni.hyperscan.*;

public final class HyperscanTestHelper {

    private HyperscanTestHelper() {
    }

    /**
     * Loads the JavaCPP native library, using the fork's tier-selecting
     * HyperscanNativeLoader when present. The upstream (gliwka) native
     * artifact has no such class; there JavaCPP loads the library via the
     * generated class's static initializer.
     */
    public static void loadNativeLibrary() {
        String requestedPlatform = System.getProperty("javacpp.platform");
        if (requestedPlatform != null && !requestedPlatform.isBlank()) {
            System.setProperty("org.bytedeco.javacpp.platform", requestedPlatform);
        }
        try {
            Class.forName("com.gliwka.hyperscan.jni.HyperscanNativeLoader")
                    .getMethod("load")
                    .invoke(null);
        } catch (ClassNotFoundException e) {
            // Upstream (gliwka) native: fall back to JavaCPP auto-loading.
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static hs_database_t hsCompileMulti(String[] patterns, int[] ids, int[] flags) {
        BytePointer[] expressions = new BytePointer[patterns.length];
        try {
            for (int i = 0; i < patterns.length; i++) {
                expressions[i] = new BytePointer(patterns[i], StandardCharsets.UTF_8);
            }
            try (PointerPointer<BytePointer> expressionsPointer = new PointerPointer<>(expressions);
                 IntPointer patternIds = new IntPointer(ids);
                 IntPointer compileFlags = new IntPointer(flags);
                 PointerPointer<hs_database_t> databasePtr = new PointerPointer<>(1);
                 PointerPointer<hs_compile_error_t> errorPtr = new PointerPointer<>(1)) {
                int result = hs_compile_multi(
                        expressionsPointer,
                        compileFlags,
                        patternIds,
                        patterns.length,
                        HS_MODE_BLOCK,
                        null,
                        databasePtr,
                        errorPtr
                );

                if (result != 0) {
                    hs_compile_error_t error = new hs_compile_error_t(errorPtr.get(0));
                    try {
                        String message = error.message() != null
                                ? error.message().getString(StandardCharsets.UTF_8)
                                : "unknown";
                        throw new AssertionError("hs_compile_multi failed: " + result + " - " + message);
                    } finally {
                        hs_free_compile_error(error);
                    }
                }

                return databasePtr.get(hs_database_t.class);
            }
        } finally {
            for (BytePointer expression : expressions) {
                if (expression != null) {
                    expression.close();
                }
            }
        }
    }

    private static final ThreadLocal<List<Match>> CURRENT_MATCHES = ThreadLocal.withInitial(ArrayList::new);

    private static final match_event_handler HANDLER = new match_event_handler() {
        @Override
        public int call(@Cast("unsigned int") int id,
                        @Cast("unsigned long long") long from,
                        @Cast("unsigned long long") long to,
                        @Cast("unsigned int") int flags,
                        Pointer context) {
            CURRENT_MATCHES.get().add(new Match(id, from, to));
            return 0;
        }
    };

    public static List<Match> hsScan(hs_database_t database, String input) {
        hs_scratch_t scratch = new hs_scratch_t();
        int allocResult = hs_alloc_scratch(database, scratch);
        if (allocResult != 0) {
            throw new AssertionError("hs_alloc_scratch failed: " + allocResult);
        }

        List<Match> matches = CURRENT_MATCHES.get();
        matches.clear();

        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        try (BytePointer inputPointer = new BytePointer(inputBytes)) {
            int scanResult = hs_scan(database, inputPointer, inputBytes.length, 0, scratch, HANDLER, null);
            if (scanResult != 0) {
                throw new AssertionError("hs_scan failed: " + scanResult);
            }

            return new ArrayList<>(matches);
        } finally {
            int freeResult = hs_free_scratch(scratch);
            scratch.close();
            if (freeResult != 0) {
                throw new AssertionError("hs_free_scratch failed: " + freeResult);
            }
        }
    }

    public static void freeDatabase(hs_database_t database) {
        int result = hs_free_database(database);
        database.close();
        if (result != 0) {
            throw new AssertionError("hs_free_database failed: " + result);
        }
    }

    public static final class Match {
        public final int id;
        public final long from;
        public final long to;

        public Match(int id, long from, long to) {
            this.id = id;
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "Match{id=" + id + ", from=" + from + ", to=" + to + '}';
        }
    }
}
