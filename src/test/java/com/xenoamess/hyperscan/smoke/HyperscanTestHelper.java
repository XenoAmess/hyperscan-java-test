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

    public static List<Match> hsScan(hs_database_t database, String input) {
        hs_scratch_t scratch = new hs_scratch_t();
        int allocResult = hs_alloc_scratch(database, scratch);
        if (allocResult != 0) {
            throw new AssertionError("hs_alloc_scratch failed: " + allocResult);
        }

        List<Match> matches = new ArrayList<>();
        match_event_handler handler = new match_event_handler() {
            @Override
            public int call(@Cast("unsigned int") int id,
                            @Cast("unsigned long long") long from,
                            @Cast("unsigned long long") long to,
                            @Cast("unsigned int") int flags,
                            Pointer context) {
                matches.add(new Match(id, from, to));
                return 0;
            }
        };

        int scanResult = hs_scan(database, input, input.length(), 0, scratch, handler, null);
        if (scanResult != 0) {
            throw new AssertionError("hs_scan failed: " + scanResult);
        }

        return matches;
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
