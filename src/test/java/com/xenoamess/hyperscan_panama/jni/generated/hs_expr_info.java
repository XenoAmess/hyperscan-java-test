package com.xenoamess.hyperscan_panama.jni.generated;

import java.lang.foreign.Arena;
import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Consumer;

/** Optimized compatibility facade for hs_expr_info. */
public final class hs_expr_info {

    private static final Class<?> DELEGATE;
    private static final String PLATFORM_FAMILY;

    private static final MethodHandle MH_LAYOUT;
    private static final MethodHandle MH_MIN_WIDTH_LAYOUT;
    private static final MethodHandle MH_MIN_WIDTH_OFFSET;
    private static final MethodHandle MH_MIN_WIDTH;
    private static final MethodHandle MH_MIN_WIDTH_1;
    private static final MethodHandle MH_MAX_WIDTH_LAYOUT;
    private static final MethodHandle MH_MAX_WIDTH_OFFSET;
    private static final MethodHandle MH_MAX_WIDTH;
    private static final MethodHandle MH_MAX_WIDTH_1;
    private static final MethodHandle MH_UNORDERED_MATCHES_LAYOUT;
    private static final MethodHandle MH_UNORDERED_MATCHES_OFFSET;
    private static final MethodHandle MH_UNORDERED_MATCHES;
    private static final MethodHandle MH_UNORDERED_MATCHES_1;
    private static final MethodHandle MH_MATCHES_AT_EOD_LAYOUT;
    private static final MethodHandle MH_MATCHES_AT_EOD_OFFSET;
    private static final MethodHandle MH_MATCHES_AT_EOD;
    private static final MethodHandle MH_MATCHES_AT_EOD_1;
    private static final MethodHandle MH_MATCHES_ONLY_AT_EOD_LAYOUT;
    private static final MethodHandle MH_MATCHES_ONLY_AT_EOD_OFFSET;
    private static final MethodHandle MH_MATCHES_ONLY_AT_EOD;
    private static final MethodHandle MH_MATCHES_ONLY_AT_EOD_1;
    private static final MethodHandle MH_ASSLICE;
    private static final MethodHandle MH_SIZEOF;
    private static final MethodHandle MH_ALLOCATE;
    private static final MethodHandle MH_ALLOCATEARRAY;
    private static final MethodHandle MH_REINTERPRET;
    private static final MethodHandle MH_REINTERPRET_1;

    static {
        String platform = System.getProperty("com.xenoamess.hyperscan_panama.platform");
        if (platform == null || platform.isEmpty()) {
            platform = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatform();
        }
        PLATFORM_FAMILY = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatformFamily(platform);
        String className = "com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.hs_expr_info";
        try {
            DELEGATE = Class.forName(className);
            // no SYMBOL_LOOKUP/LIBRARY_ARENA in functional/struct classes
            MH_LAYOUT = findOrNull("layout", GroupLayout.class);
            MH_MIN_WIDTH_LAYOUT = findOrNull("min_width$layout", ValueLayout.OfInt.class);
            MH_MIN_WIDTH_OFFSET = findOrNull("min_width$offset", long.class);
            MH_MIN_WIDTH = findOrNull("min_width", int.class, MemorySegment.class);
            MH_MIN_WIDTH_1 = findOrNull("min_width", void.class, MemorySegment.class, int.class);
            MH_MAX_WIDTH_LAYOUT = findOrNull("max_width$layout", ValueLayout.OfInt.class);
            MH_MAX_WIDTH_OFFSET = findOrNull("max_width$offset", long.class);
            MH_MAX_WIDTH = findOrNull("max_width", int.class, MemorySegment.class);
            MH_MAX_WIDTH_1 = findOrNull("max_width", void.class, MemorySegment.class, int.class);
            MH_UNORDERED_MATCHES_LAYOUT = findOrNull("unordered_matches$layout", ValueLayout.OfByte.class);
            MH_UNORDERED_MATCHES_OFFSET = findOrNull("unordered_matches$offset", long.class);
            MH_UNORDERED_MATCHES = findOrNull("unordered_matches", byte.class, MemorySegment.class);
            MH_UNORDERED_MATCHES_1 = findOrNull("unordered_matches", void.class, MemorySegment.class, byte.class);
            MH_MATCHES_AT_EOD_LAYOUT = findOrNull("matches_at_eod$layout", ValueLayout.OfByte.class);
            MH_MATCHES_AT_EOD_OFFSET = findOrNull("matches_at_eod$offset", long.class);
            MH_MATCHES_AT_EOD = findOrNull("matches_at_eod", byte.class, MemorySegment.class);
            MH_MATCHES_AT_EOD_1 = findOrNull("matches_at_eod", void.class, MemorySegment.class, byte.class);
            MH_MATCHES_ONLY_AT_EOD_LAYOUT = findOrNull("matches_only_at_eod$layout", ValueLayout.OfByte.class);
            MH_MATCHES_ONLY_AT_EOD_OFFSET = findOrNull("matches_only_at_eod$offset", long.class);
            MH_MATCHES_ONLY_AT_EOD = findOrNull("matches_only_at_eod", byte.class, MemorySegment.class);
            MH_MATCHES_ONLY_AT_EOD_1 = findOrNull("matches_only_at_eod", void.class, MemorySegment.class, byte.class);
            MH_ASSLICE = findOrNull("asSlice", MemorySegment.class, MemorySegment.class, long.class);
            MH_SIZEOF = findOrNull("sizeof", long.class);
            MH_ALLOCATE = findOrNull("allocate", MemorySegment.class, SegmentAllocator.class);
            MH_ALLOCATEARRAY = findOrNull("allocateArray", MemorySegment.class, long.class, SegmentAllocator.class);
            MH_REINTERPRET = findOrNull("reinterpret", MemorySegment.class, MemorySegment.class, Arena.class, Consumer.class);
            MH_REINTERPRET_1 = findOrNull("reinterpret", MemorySegment.class, MemorySegment.class, long.class, Arena.class, Consumer.class);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hs_expr_info class: " + className, e);
        }
    }

    private hs_expr_info() {}

    private static MethodHandle findOrNull(String name, Class<?> rtype, Class<?>... ptypes) {
        try {
            return MethodHandles.publicLookup().findStatic(DELEGATE, name, MethodType.methodType(rtype, ptypes));
        } catch (Exception e) {
            return null;
        }
    }

    private static MethodHandle require(String name, MethodHandle mh) {
        if (mh == null) {
            throw new RuntimeException("Method not available on this platform: " + name);
        }
        return mh;
    }

    public static GroupLayout layout() {
        try {
            return (GroupLayout) require("layout", MH_LAYOUT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfInt min_width$layout() {
        try {
            return (ValueLayout.OfInt) require("min_width$layout", MH_MIN_WIDTH_LAYOUT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long min_width$offset() {
        try {
            return (long) require("min_width$offset", MH_MIN_WIDTH_OFFSET).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int min_width(MemorySegment arg0) {
        try {
            return (int) require("min_width", MH_MIN_WIDTH).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void min_width(MemorySegment arg0, int arg1) {
        try {
            require("min_width", MH_MIN_WIDTH_1).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfInt max_width$layout() {
        try {
            return (ValueLayout.OfInt) require("max_width$layout", MH_MAX_WIDTH_LAYOUT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long max_width$offset() {
        try {
            return (long) require("max_width$offset", MH_MAX_WIDTH_OFFSET).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int max_width(MemorySegment arg0) {
        try {
            return (int) require("max_width", MH_MAX_WIDTH).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void max_width(MemorySegment arg0, int arg1) {
        try {
            require("max_width", MH_MAX_WIDTH_1).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfByte unordered_matches$layout() {
        try {
            return (ValueLayout.OfByte) require("unordered_matches$layout", MH_UNORDERED_MATCHES_LAYOUT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long unordered_matches$offset() {
        try {
            return (long) require("unordered_matches$offset", MH_UNORDERED_MATCHES_OFFSET).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte unordered_matches(MemorySegment arg0) {
        try {
            return (byte) require("unordered_matches", MH_UNORDERED_MATCHES).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void unordered_matches(MemorySegment arg0, byte arg1) {
        try {
            require("unordered_matches", MH_UNORDERED_MATCHES_1).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfByte matches_at_eod$layout() {
        try {
            return (ValueLayout.OfByte) require("matches_at_eod$layout", MH_MATCHES_AT_EOD_LAYOUT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long matches_at_eod$offset() {
        try {
            return (long) require("matches_at_eod$offset", MH_MATCHES_AT_EOD_OFFSET).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte matches_at_eod(MemorySegment arg0) {
        try {
            return (byte) require("matches_at_eod", MH_MATCHES_AT_EOD).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void matches_at_eod(MemorySegment arg0, byte arg1) {
        try {
            require("matches_at_eod", MH_MATCHES_AT_EOD_1).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfByte matches_only_at_eod$layout() {
        try {
            return (ValueLayout.OfByte) require("matches_only_at_eod$layout", MH_MATCHES_ONLY_AT_EOD_LAYOUT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long matches_only_at_eod$offset() {
        try {
            return (long) require("matches_only_at_eod$offset", MH_MATCHES_ONLY_AT_EOD_OFFSET).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte matches_only_at_eod(MemorySegment arg0) {
        try {
            return (byte) require("matches_only_at_eod", MH_MATCHES_ONLY_AT_EOD).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void matches_only_at_eod(MemorySegment arg0, byte arg1) {
        try {
            require("matches_only_at_eod", MH_MATCHES_ONLY_AT_EOD_1).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment asSlice(MemorySegment arg0, long arg1) {
        try {
            return (MemorySegment) require("asSlice", MH_ASSLICE).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long sizeof() {
        try {
            return (long) require("sizeof", MH_SIZEOF).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment allocate(SegmentAllocator arg0) {
        try {
            return (MemorySegment) require("allocate", MH_ALLOCATE).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment allocateArray(long arg0, SegmentAllocator arg1) {
        try {
            return (MemorySegment) require("allocateArray", MH_ALLOCATEARRAY).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment reinterpret(MemorySegment arg0, Arena arg1, Consumer<MemorySegment> arg2) {
        try {
            return (MemorySegment) require("reinterpret", MH_REINTERPRET).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment reinterpret(MemorySegment arg0, long arg1, Arena arg2, Consumer<MemorySegment> arg3) {
        try {
            return (MemorySegment) require("reinterpret", MH_REINTERPRET_1).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}