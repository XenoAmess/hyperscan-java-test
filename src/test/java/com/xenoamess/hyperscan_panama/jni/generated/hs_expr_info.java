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
            MH_LAYOUT = find("layout", GroupLayout.class);
            MH_MIN_WIDTH_LAYOUT = find("min_width$layout", ValueLayout.OfInt.class);
            MH_MIN_WIDTH_OFFSET = find("min_width$offset", long.class);
            MH_MIN_WIDTH = find("min_width", int.class, MemorySegment.class);
            MH_MIN_WIDTH_1 = find("min_width", void.class, MemorySegment.class, int.class);
            MH_MAX_WIDTH_LAYOUT = find("max_width$layout", ValueLayout.OfInt.class);
            MH_MAX_WIDTH_OFFSET = find("max_width$offset", long.class);
            MH_MAX_WIDTH = find("max_width", int.class, MemorySegment.class);
            MH_MAX_WIDTH_1 = find("max_width", void.class, MemorySegment.class, int.class);
            MH_UNORDERED_MATCHES_LAYOUT = find("unordered_matches$layout", ValueLayout.OfByte.class);
            MH_UNORDERED_MATCHES_OFFSET = find("unordered_matches$offset", long.class);
            MH_UNORDERED_MATCHES = find("unordered_matches", byte.class, MemorySegment.class);
            MH_UNORDERED_MATCHES_1 = find("unordered_matches", void.class, MemorySegment.class, byte.class);
            MH_MATCHES_AT_EOD_LAYOUT = find("matches_at_eod$layout", ValueLayout.OfByte.class);
            MH_MATCHES_AT_EOD_OFFSET = find("matches_at_eod$offset", long.class);
            MH_MATCHES_AT_EOD = find("matches_at_eod", byte.class, MemorySegment.class);
            MH_MATCHES_AT_EOD_1 = find("matches_at_eod", void.class, MemorySegment.class, byte.class);
            MH_MATCHES_ONLY_AT_EOD_LAYOUT = find("matches_only_at_eod$layout", ValueLayout.OfByte.class);
            MH_MATCHES_ONLY_AT_EOD_OFFSET = find("matches_only_at_eod$offset", long.class);
            MH_MATCHES_ONLY_AT_EOD = find("matches_only_at_eod", byte.class, MemorySegment.class);
            MH_MATCHES_ONLY_AT_EOD_1 = find("matches_only_at_eod", void.class, MemorySegment.class, byte.class);
            MH_ASSLICE = find("asSlice", MemorySegment.class, MemorySegment.class, long.class);
            MH_SIZEOF = find("sizeof", long.class);
            MH_ALLOCATE = find("allocate", MemorySegment.class, SegmentAllocator.class);
            MH_ALLOCATEARRAY = find("allocateArray", MemorySegment.class, long.class, SegmentAllocator.class);
            MH_REINTERPRET = find("reinterpret", MemorySegment.class, MemorySegment.class, Arena.class, Consumer.class);
            MH_REINTERPRET_1 = find("reinterpret", MemorySegment.class, MemorySegment.class, long.class, Arena.class, Consumer.class);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hs_expr_info class: " + className, e);
        }
    }

    private hs_expr_info() {}

    private static MethodHandle find(String name, Class<?> rtype, Class<?>... ptypes) {
        try {
            return MethodHandles.publicLookup().findStatic(DELEGATE, name, MethodType.methodType(rtype, ptypes));
        } catch (Exception e) {
            throw new RuntimeException("Failed to find method " + name + " in " + DELEGATE.getName(), e);
        }
    }

    public static GroupLayout layout() {
        try {
            return (GroupLayout) MH_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfInt min_width$layout() {
        try {
            return (ValueLayout.OfInt) MH_MIN_WIDTH_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long min_width$offset() {
        try {
            return (long) MH_MIN_WIDTH_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int min_width(MemorySegment arg0) {
        try {
            return (int) MH_MIN_WIDTH.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void min_width(MemorySegment arg0, int arg1) {
        try {
            MH_MIN_WIDTH_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfInt max_width$layout() {
        try {
            return (ValueLayout.OfInt) MH_MAX_WIDTH_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long max_width$offset() {
        try {
            return (long) MH_MAX_WIDTH_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int max_width(MemorySegment arg0) {
        try {
            return (int) MH_MAX_WIDTH.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void max_width(MemorySegment arg0, int arg1) {
        try {
            MH_MAX_WIDTH_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfByte unordered_matches$layout() {
        try {
            return (ValueLayout.OfByte) MH_UNORDERED_MATCHES_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long unordered_matches$offset() {
        try {
            return (long) MH_UNORDERED_MATCHES_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte unordered_matches(MemorySegment arg0) {
        try {
            return (byte) MH_UNORDERED_MATCHES.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void unordered_matches(MemorySegment arg0, byte arg1) {
        try {
            MH_UNORDERED_MATCHES_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfByte matches_at_eod$layout() {
        try {
            return (ValueLayout.OfByte) MH_MATCHES_AT_EOD_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long matches_at_eod$offset() {
        try {
            return (long) MH_MATCHES_AT_EOD_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte matches_at_eod(MemorySegment arg0) {
        try {
            return (byte) MH_MATCHES_AT_EOD.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void matches_at_eod(MemorySegment arg0, byte arg1) {
        try {
            MH_MATCHES_AT_EOD_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfByte matches_only_at_eod$layout() {
        try {
            return (ValueLayout.OfByte) MH_MATCHES_ONLY_AT_EOD_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long matches_only_at_eod$offset() {
        try {
            return (long) MH_MATCHES_ONLY_AT_EOD_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte matches_only_at_eod(MemorySegment arg0) {
        try {
            return (byte) MH_MATCHES_ONLY_AT_EOD.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void matches_only_at_eod(MemorySegment arg0, byte arg1) {
        try {
            MH_MATCHES_ONLY_AT_EOD_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment asSlice(MemorySegment arg0, long arg1) {
        try {
            return (MemorySegment) MH_ASSLICE.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long sizeof() {
        try {
            return (long) MH_SIZEOF.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment allocate(SegmentAllocator arg0) {
        try {
            return (MemorySegment) MH_ALLOCATE.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment allocateArray(long arg0, SegmentAllocator arg1) {
        try {
            return (MemorySegment) MH_ALLOCATEARRAY.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment reinterpret(MemorySegment arg0, Arena arg1, Consumer<MemorySegment> arg2) {
        try {
            return (MemorySegment) MH_REINTERPRET.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment reinterpret(MemorySegment arg0, long arg1, Arena arg2, Consumer<MemorySegment> arg3) {
        try {
            return (MemorySegment) MH_REINTERPRET_1.invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}