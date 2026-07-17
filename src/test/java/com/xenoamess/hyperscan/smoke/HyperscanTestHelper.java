package com.xenoamess.hyperscan.smoke;

import com.gliwka.hyperscan.jni.hs_compile_error_t;
import com.gliwka.hyperscan.jni.hs_database_t;
import com.gliwka.hyperscan.jni.hs_scratch_t;
import com.gliwka.hyperscan.jni.match_event_handler;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.Cast;

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
        PointerPointer<BytePointer> expressionsPointer = new PointerPointer<>(patterns);
        IntPointer patternIds = new IntPointer(ids);
        IntPointer compileFlags = new IntPointer(flags);
        PointerPointer<hs_database_t> databasePtr = new PointerPointer<>(1);
        PointerPointer<hs_compile_error_t> errorPtr = new PointerPointer<>(1);

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
            String message = error.message() != null ? error.message().getString() : "unknown";
            throw new AssertionError("hs_compile_multi failed: " + result + " - " + message);
        }

        return databasePtr.get(hs_database_t.class);
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

        int scanResult = hs_scan(database, input, input.length(), 0, scratch, HANDLER, null);
        if (scanResult != 0) {
            throw new AssertionError("hs_scan failed: " + scanResult);
        }

        int freeResult = hs_free_scratch(scratch);
        if (freeResult != 0) {
            throw new AssertionError("hs_free_scratch failed: " + freeResult);
        }

        return new ArrayList<>(matches);
    }

    public static void freeDatabase(hs_database_t database) {
        hs_free_database(database);
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
