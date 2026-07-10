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

/** Optimized compatibility facade for hs_platform_info. */
public final class hs_platform_info {

    private static final Class<?> DELEGATE;
    private static final String PLATFORM_FAMILY;

    private static final MethodHandle MH_LAYOUT;
    private static final MethodHandle MH_TUNE_LAYOUT;
    private static final MethodHandle MH_TUNE_OFFSET;
    private static final MethodHandle MH_TUNE;
    private static final MethodHandle MH_TUNE_1;
    private static final MethodHandle MH_CPU_FEATURES_LAYOUT;
    private static final MethodHandle MH_CPU_FEATURES_OFFSET;
    private static final MethodHandle MH_CPU_FEATURES;
    private static final MethodHandle MH_CPU_FEATURES_1;
    private static final MethodHandle MH_RESERVED1_LAYOUT;
    private static final MethodHandle MH_RESERVED1_OFFSET;
    private static final MethodHandle MH_RESERVED1;
    private static final MethodHandle MH_RESERVED1_1;
    private static final MethodHandle MH_RESERVED2_LAYOUT;
    private static final MethodHandle MH_RESERVED2_OFFSET;
    private static final MethodHandle MH_RESERVED2;
    private static final MethodHandle MH_RESERVED2_1;
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
        String className = "com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.hs_platform_info";
        try {
            DELEGATE = Class.forName(className);
            // no SYMBOL_LOOKUP/LIBRARY_ARENA in functional/struct classes
            MH_LAYOUT = find("layout", GroupLayout.class);
            MH_TUNE_LAYOUT = find("tune$layout", ValueLayout.OfInt.class);
            MH_TUNE_OFFSET = find("tune$offset", long.class);
            MH_TUNE = find("tune", int.class, MemorySegment.class);
            MH_TUNE_1 = find("tune", void.class, MemorySegment.class, int.class);
            MH_CPU_FEATURES_LAYOUT = find("cpu_features$layout", ValueLayout.OfLong.class);
            MH_CPU_FEATURES_OFFSET = find("cpu_features$offset", long.class);
            MH_CPU_FEATURES = find("cpu_features", long.class, MemorySegment.class);
            MH_CPU_FEATURES_1 = find("cpu_features", void.class, MemorySegment.class, long.class);
            MH_RESERVED1_LAYOUT = find("reserved1$layout", ValueLayout.OfLong.class);
            MH_RESERVED1_OFFSET = find("reserved1$offset", long.class);
            MH_RESERVED1 = find("reserved1", long.class, MemorySegment.class);
            MH_RESERVED1_1 = find("reserved1", void.class, MemorySegment.class, long.class);
            MH_RESERVED2_LAYOUT = find("reserved2$layout", ValueLayout.OfLong.class);
            MH_RESERVED2_OFFSET = find("reserved2$offset", long.class);
            MH_RESERVED2 = find("reserved2", long.class, MemorySegment.class);
            MH_RESERVED2_1 = find("reserved2", void.class, MemorySegment.class, long.class);
            MH_ASSLICE = find("asSlice", MemorySegment.class, MemorySegment.class, long.class);
            MH_SIZEOF = find("sizeof", long.class);
            MH_ALLOCATE = find("allocate", MemorySegment.class, SegmentAllocator.class);
            MH_ALLOCATEARRAY = find("allocateArray", MemorySegment.class, long.class, SegmentAllocator.class);
            MH_REINTERPRET = find("reinterpret", MemorySegment.class, MemorySegment.class, Arena.class, Consumer.class);
            MH_REINTERPRET_1 = find("reinterpret", MemorySegment.class, MemorySegment.class, long.class, Arena.class, Consumer.class);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hs_platform_info class: " + className, e);
        }
    }

    private hs_platform_info() {}

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

    public static ValueLayout.OfInt tune$layout() {
        try {
            return (ValueLayout.OfInt) MH_TUNE_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long tune$offset() {
        try {
            return (long) MH_TUNE_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int tune(MemorySegment arg0) {
        try {
            return (int) MH_TUNE.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void tune(MemorySegment arg0, int arg1) {
        try {
            MH_TUNE_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong cpu_features$layout() {
        try {
            return (ValueLayout.OfLong) MH_CPU_FEATURES_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long cpu_features$offset() {
        try {
            return (long) MH_CPU_FEATURES_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long cpu_features(MemorySegment arg0) {
        try {
            return (long) MH_CPU_FEATURES.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void cpu_features(MemorySegment arg0, long arg1) {
        try {
            MH_CPU_FEATURES_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong reserved1$layout() {
        try {
            return (ValueLayout.OfLong) MH_RESERVED1_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long reserved1$offset() {
        try {
            return (long) MH_RESERVED1_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long reserved1(MemorySegment arg0) {
        try {
            return (long) MH_RESERVED1.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void reserved1(MemorySegment arg0, long arg1) {
        try {
            MH_RESERVED1_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong reserved2$layout() {
        try {
            return (ValueLayout.OfLong) MH_RESERVED2_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long reserved2$offset() {
        try {
            return (long) MH_RESERVED2_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long reserved2(MemorySegment arg0) {
        try {
            return (long) MH_RESERVED2.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void reserved2(MemorySegment arg0, long arg1) {
        try {
            MH_RESERVED2_1.invokeExact(arg0, arg1);
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