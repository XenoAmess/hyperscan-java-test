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

/** Compatibility facade for hs_platform_info. */
public final class hs_platform_info {

    private static final Class<?> DELEGATE;
    private static final String PLATFORM_FAMILY;
    private static final ConcurrentHashMap<String, MethodHandle> HANDLES = new ConcurrentHashMap<>();

    static {
        String platform = System.getProperty("com.xenoamess.hyperscan_panama.platform");
        if (platform == null || platform.isEmpty()) {
            platform = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatform();
        }
        PLATFORM_FAMILY = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatformFamily(platform);
        String className = "com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.hs_platform_info";
        try {
            DELEGATE = Class.forName(className);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hs_platform_info class: " + className, e);
        }
    }

    private hs_platform_info() {}

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

    public static ValueLayout.OfInt tune$layout() {
        try {
            return (ValueLayout.OfInt) find("tune$layout", ValueLayout.OfInt.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long tune$offset() {
        try {
            return (long) find("tune$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int tune(MemorySegment arg0) {
        try {
            return (int) find("tune", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void tune(MemorySegment arg0, int arg1) {
        try {
            find("tune", void.class, MemorySegment.class, int.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong cpu_features$layout() {
        try {
            return (ValueLayout.OfLong) find("cpu_features$layout", ValueLayout.OfLong.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long cpu_features$offset() {
        try {
            return (long) find("cpu_features$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long cpu_features(MemorySegment arg0) {
        try {
            return (long) find("cpu_features", long.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void cpu_features(MemorySegment arg0, long arg1) {
        try {
            find("cpu_features", void.class, MemorySegment.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong reserved1$layout() {
        try {
            return (ValueLayout.OfLong) find("reserved1$layout", ValueLayout.OfLong.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long reserved1$offset() {
        try {
            return (long) find("reserved1$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long reserved1(MemorySegment arg0) {
        try {
            return (long) find("reserved1", long.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void reserved1(MemorySegment arg0, long arg1) {
        try {
            find("reserved1", void.class, MemorySegment.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfLong reserved2$layout() {
        try {
            return (ValueLayout.OfLong) find("reserved2$layout", ValueLayout.OfLong.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long reserved2$offset() {
        try {
            return (long) find("reserved2$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long reserved2(MemorySegment arg0) {
        try {
            return (long) find("reserved2", long.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void reserved2(MemorySegment arg0, long arg1) {
        try {
            find("reserved2", void.class, MemorySegment.class, long.class).invokeExact(arg0, arg1);
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