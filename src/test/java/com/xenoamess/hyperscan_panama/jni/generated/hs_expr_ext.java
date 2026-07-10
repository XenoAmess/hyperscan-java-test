package com.xenoamess.hyperscan_panama.jni.generated;

import java.lang.foreign.Arena;
import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/** Compatibility facade for hs_expr_ext. */
public final class hs_expr_ext {

    private static final Class<?> DELEGATE;
    private static final String PLATFORM_FAMILY;
    private static final ConcurrentHashMap<String, MethodHandle> HANDLES = new ConcurrentHashMap<>();

    static {
        String platform = System.getProperty("com.xenoamess.hyperscan_panama.platform");
        if (platform == null || platform.isEmpty()) {
            platform = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatform();
        }
        PLATFORM_FAMILY = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatformFamily(platform);
        String className = "com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.hs_expr_ext";
        try {
            DELEGATE = Class.forName(className);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hs_expr_ext class: " + className, e);
        }
    }

    private hs_expr_ext() {}

    private static MethodHandle find(String name, Class<?> rtype, Class<?>... ptypes) {
        String key = name + ":" + rtype.getName() + ":" + Arrays.toString(ptypes);
        return HANDLES.computeIfAbsent(key, k -> {
            try {
                return MethodHandles.publicLookup().findStatic(DELEGATE, name, MethodType.methodType(rtype, ptypes));
            } catch (Exception e) {
                throw new RuntimeException("Failed to find method " + name + " in " + DELEGATE.getName(), e);
            }
        });
    }

    public static GroupLayout layout() {
        try {
            return (GroupLayout) find("layout", GroupLayout.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong flags$layout() {
        try {
            return (ValueLayout.OfLong) find("flags$layout", ValueLayout.OfLong.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long flags$offset() {
        try {
            return (long) find("flags$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long flags(MemorySegment arg0) {
        try {
            return (long) find("flags", long.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void flags(MemorySegment arg0, long arg1) {
        try {
            find("flags", void.class, MemorySegment.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong min_offset$layout() {
        try {
            return (ValueLayout.OfLong) find("min_offset$layout", ValueLayout.OfLong.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long min_offset$offset() {
        try {
            return (long) find("min_offset$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long min_offset(MemorySegment arg0) {
        try {
            return (long) find("min_offset", long.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void min_offset(MemorySegment arg0, long arg1) {
        try {
            find("min_offset", void.class, MemorySegment.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong max_offset$layout() {
        try {
            return (ValueLayout.OfLong) find("max_offset$layout", ValueLayout.OfLong.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long max_offset$offset() {
        try {
            return (long) find("max_offset$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long max_offset(MemorySegment arg0) {
        try {
            return (long) find("max_offset", long.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void max_offset(MemorySegment arg0, long arg1) {
        try {
            find("max_offset", void.class, MemorySegment.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong min_length$layout() {
        try {
            return (ValueLayout.OfLong) find("min_length$layout", ValueLayout.OfLong.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long min_length$offset() {
        try {
            return (long) find("min_length$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long min_length(MemorySegment arg0) {
        try {
            return (long) find("min_length", long.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void min_length(MemorySegment arg0, long arg1) {
        try {
            find("min_length", void.class, MemorySegment.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfInt edit_distance$layout() {
        try {
            return (ValueLayout.OfInt) find("edit_distance$layout", ValueLayout.OfInt.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long edit_distance$offset() {
        try {
            return (long) find("edit_distance$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int edit_distance(MemorySegment arg0) {
        try {
            return (int) find("edit_distance", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void edit_distance(MemorySegment arg0, int arg1) {
        try {
            find("edit_distance", void.class, MemorySegment.class, int.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfInt hamming_distance$layout() {
        try {
            return (ValueLayout.OfInt) find("hamming_distance$layout", ValueLayout.OfInt.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long hamming_distance$offset() {
        try {
            return (long) find("hamming_distance$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hamming_distance(MemorySegment arg0) {
        try {
            return (int) find("hamming_distance", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void hamming_distance(MemorySegment arg0, int arg1) {
        try {
            find("hamming_distance", void.class, MemorySegment.class, int.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment asSlice(MemorySegment arg0, long arg1) {
        try {
            return (MemorySegment) find("asSlice", MemorySegment.class, MemorySegment.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long sizeof() {
        try {
            return (long) find("sizeof", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment allocate(SegmentAllocator arg0) {
        try {
            return (MemorySegment) find("allocate", MemorySegment.class, SegmentAllocator.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment allocateArray(long arg0, SegmentAllocator arg1) {
        try {
            return (MemorySegment) find("allocateArray", MemorySegment.class, long.class, SegmentAllocator.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment reinterpret(MemorySegment arg0, Arena arg1, Consumer<MemorySegment> arg2) {
        try {
            return (MemorySegment) find("reinterpret", MemorySegment.class, MemorySegment.class, Arena.class, Consumer.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment reinterpret(MemorySegment arg0, long arg1, Arena arg2, Consumer<MemorySegment> arg3) {
        try {
            return (MemorySegment) find("reinterpret", MemorySegment.class, MemorySegment.class, long.class, Arena.class, Consumer.class).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}