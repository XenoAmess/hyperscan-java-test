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

/** Optimized compatibility facade for hs_expr_ext. */
public final class hs_expr_ext {

    private static final Class<?> DELEGATE;
    private static final String PLATFORM_FAMILY;

    private static final MethodHandle MH_LAYOUT;
    private static final MethodHandle MH_FLAGS_LAYOUT;
    private static final MethodHandle MH_FLAGS_OFFSET;
    private static final MethodHandle MH_FLAGS;
    private static final MethodHandle MH_FLAGS_1;
    private static final MethodHandle MH_MIN_OFFSET_LAYOUT;
    private static final MethodHandle MH_MIN_OFFSET_OFFSET;
    private static final MethodHandle MH_MIN_OFFSET;
    private static final MethodHandle MH_MIN_OFFSET_1;
    private static final MethodHandle MH_MAX_OFFSET_LAYOUT;
    private static final MethodHandle MH_MAX_OFFSET_OFFSET;
    private static final MethodHandle MH_MAX_OFFSET;
    private static final MethodHandle MH_MAX_OFFSET_1;
    private static final MethodHandle MH_MIN_LENGTH_LAYOUT;
    private static final MethodHandle MH_MIN_LENGTH_OFFSET;
    private static final MethodHandle MH_MIN_LENGTH;
    private static final MethodHandle MH_MIN_LENGTH_1;
    private static final MethodHandle MH_EDIT_DISTANCE_LAYOUT;
    private static final MethodHandle MH_EDIT_DISTANCE_OFFSET;
    private static final MethodHandle MH_EDIT_DISTANCE;
    private static final MethodHandle MH_EDIT_DISTANCE_1;
    private static final MethodHandle MH_HAMMING_DISTANCE_LAYOUT;
    private static final MethodHandle MH_HAMMING_DISTANCE_OFFSET;
    private static final MethodHandle MH_HAMMING_DISTANCE;
    private static final MethodHandle MH_HAMMING_DISTANCE_1;
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
        String className = "com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.hs_expr_ext";
        try {
            DELEGATE = Class.forName(className);
            // no SYMBOL_LOOKUP/LIBRARY_ARENA in functional/struct classes
            MH_LAYOUT = find("layout", GroupLayout.class);
            MH_FLAGS_LAYOUT = find("flags$layout", ValueLayout.OfLong.class);
            MH_FLAGS_OFFSET = find("flags$offset", long.class);
            MH_FLAGS = find("flags", long.class, MemorySegment.class);
            MH_FLAGS_1 = find("flags", void.class, MemorySegment.class, long.class);
            MH_MIN_OFFSET_LAYOUT = find("min_offset$layout", ValueLayout.OfLong.class);
            MH_MIN_OFFSET_OFFSET = find("min_offset$offset", long.class);
            MH_MIN_OFFSET = find("min_offset", long.class, MemorySegment.class);
            MH_MIN_OFFSET_1 = find("min_offset", void.class, MemorySegment.class, long.class);
            MH_MAX_OFFSET_LAYOUT = find("max_offset$layout", ValueLayout.OfLong.class);
            MH_MAX_OFFSET_OFFSET = find("max_offset$offset", long.class);
            MH_MAX_OFFSET = find("max_offset", long.class, MemorySegment.class);
            MH_MAX_OFFSET_1 = find("max_offset", void.class, MemorySegment.class, long.class);
            MH_MIN_LENGTH_LAYOUT = find("min_length$layout", ValueLayout.OfLong.class);
            MH_MIN_LENGTH_OFFSET = find("min_length$offset", long.class);
            MH_MIN_LENGTH = find("min_length", long.class, MemorySegment.class);
            MH_MIN_LENGTH_1 = find("min_length", void.class, MemorySegment.class, long.class);
            MH_EDIT_DISTANCE_LAYOUT = find("edit_distance$layout", ValueLayout.OfInt.class);
            MH_EDIT_DISTANCE_OFFSET = find("edit_distance$offset", long.class);
            MH_EDIT_DISTANCE = find("edit_distance", int.class, MemorySegment.class);
            MH_EDIT_DISTANCE_1 = find("edit_distance", void.class, MemorySegment.class, int.class);
            MH_HAMMING_DISTANCE_LAYOUT = find("hamming_distance$layout", ValueLayout.OfInt.class);
            MH_HAMMING_DISTANCE_OFFSET = find("hamming_distance$offset", long.class);
            MH_HAMMING_DISTANCE = find("hamming_distance", int.class, MemorySegment.class);
            MH_HAMMING_DISTANCE_1 = find("hamming_distance", void.class, MemorySegment.class, int.class);
            MH_ASSLICE = find("asSlice", MemorySegment.class, MemorySegment.class, long.class);
            MH_SIZEOF = find("sizeof", long.class);
            MH_ALLOCATE = find("allocate", MemorySegment.class, SegmentAllocator.class);
            MH_ALLOCATEARRAY = find("allocateArray", MemorySegment.class, long.class, SegmentAllocator.class);
            MH_REINTERPRET = find("reinterpret", MemorySegment.class, MemorySegment.class, Arena.class, Consumer.class);
            MH_REINTERPRET_1 = find("reinterpret", MemorySegment.class, MemorySegment.class, long.class, Arena.class, Consumer.class);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hs_expr_ext class: " + className, e);
        }
    }

    private hs_expr_ext() {}

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

    public static ValueLayout.OfLong flags$layout() {
        try {
            return (ValueLayout.OfLong) MH_FLAGS_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long flags$offset() {
        try {
            return (long) MH_FLAGS_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long flags(MemorySegment arg0) {
        try {
            return (long) MH_FLAGS.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void flags(MemorySegment arg0, long arg1) {
        try {
            MH_FLAGS_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong min_offset$layout() {
        try {
            return (ValueLayout.OfLong) MH_MIN_OFFSET_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long min_offset$offset() {
        try {
            return (long) MH_MIN_OFFSET_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long min_offset(MemorySegment arg0) {
        try {
            return (long) MH_MIN_OFFSET.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void min_offset(MemorySegment arg0, long arg1) {
        try {
            MH_MIN_OFFSET_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong max_offset$layout() {
        try {
            return (ValueLayout.OfLong) MH_MAX_OFFSET_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long max_offset$offset() {
        try {
            return (long) MH_MAX_OFFSET_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long max_offset(MemorySegment arg0) {
        try {
            return (long) MH_MAX_OFFSET.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void max_offset(MemorySegment arg0, long arg1) {
        try {
            MH_MAX_OFFSET_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong min_length$layout() {
        try {
            return (ValueLayout.OfLong) MH_MIN_LENGTH_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long min_length$offset() {
        try {
            return (long) MH_MIN_LENGTH_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long min_length(MemorySegment arg0) {
        try {
            return (long) MH_MIN_LENGTH.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void min_length(MemorySegment arg0, long arg1) {
        try {
            MH_MIN_LENGTH_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfInt edit_distance$layout() {
        try {
            return (ValueLayout.OfInt) MH_EDIT_DISTANCE_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long edit_distance$offset() {
        try {
            return (long) MH_EDIT_DISTANCE_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int edit_distance(MemorySegment arg0) {
        try {
            return (int) MH_EDIT_DISTANCE.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void edit_distance(MemorySegment arg0, int arg1) {
        try {
            MH_EDIT_DISTANCE_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfInt hamming_distance$layout() {
        try {
            return (ValueLayout.OfInt) MH_HAMMING_DISTANCE_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long hamming_distance$offset() {
        try {
            return (long) MH_HAMMING_DISTANCE_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hamming_distance(MemorySegment arg0) {
        try {
            return (int) MH_HAMMING_DISTANCE.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void hamming_distance(MemorySegment arg0, int arg1) {
        try {
            MH_HAMMING_DISTANCE_1.invokeExact(arg0, arg1);
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