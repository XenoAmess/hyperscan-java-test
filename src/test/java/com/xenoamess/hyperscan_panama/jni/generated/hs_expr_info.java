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

/** Compatibility facade for hs_expr_info. */
public final class hs_expr_info {

    private static final Class<?> DELEGATE;
    private static final String PLATFORM_FAMILY;
    private static final ConcurrentHashMap<String, MethodHandle> HANDLES = new ConcurrentHashMap<>();

    static {
        String platform = System.getProperty("com.xenoamess.hyperscan_panama.platform");
        if (platform == null || platform.isEmpty()) {
            platform = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatform();
        }
        PLATFORM_FAMILY = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatformFamily(platform);
        String className = "com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.hs_expr_info";
        try {
            DELEGATE = Class.forName(className);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hs_expr_info class: " + className, e);
        }
    }

    private hs_expr_info() {}

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

    public static ValueLayout.OfInt min_width$layout() {
        try {
            return (ValueLayout.OfInt) find("min_width$layout", ValueLayout.OfInt.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long min_width$offset() {
        try {
            return (long) find("min_width$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int min_width(MemorySegment arg0) {
        try {
            return (int) find("min_width", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void min_width(MemorySegment arg0, int arg1) {
        try {
            find("min_width", void.class, MemorySegment.class, int.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfInt max_width$layout() {
        try {
            return (ValueLayout.OfInt) find("max_width$layout", ValueLayout.OfInt.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long max_width$offset() {
        try {
            return (long) find("max_width$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int max_width(MemorySegment arg0) {
        try {
            return (int) find("max_width", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void max_width(MemorySegment arg0, int arg1) {
        try {
            find("max_width", void.class, MemorySegment.class, int.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfByte unordered_matches$layout() {
        try {
            return (ValueLayout.OfByte) find("unordered_matches$layout", ValueLayout.OfByte.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long unordered_matches$offset() {
        try {
            return (long) find("unordered_matches$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte unordered_matches(MemorySegment arg0) {
        try {
            return (byte) find("unordered_matches", byte.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void unordered_matches(MemorySegment arg0, byte arg1) {
        try {
            find("unordered_matches", void.class, MemorySegment.class, byte.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfByte matches_at_eod$layout() {
        try {
            return (ValueLayout.OfByte) find("matches_at_eod$layout", ValueLayout.OfByte.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long matches_at_eod$offset() {
        try {
            return (long) find("matches_at_eod$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte matches_at_eod(MemorySegment arg0) {
        try {
            return (byte) find("matches_at_eod", byte.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void matches_at_eod(MemorySegment arg0, byte arg1) {
        try {
            find("matches_at_eod", void.class, MemorySegment.class, byte.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfByte matches_only_at_eod$layout() {
        try {
            return (ValueLayout.OfByte) find("matches_only_at_eod$layout", ValueLayout.OfByte.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long matches_only_at_eod$offset() {
        try {
            return (long) find("matches_only_at_eod$offset", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static byte matches_only_at_eod(MemorySegment arg0) {
        try {
            return (byte) find("matches_only_at_eod", byte.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void matches_only_at_eod(MemorySegment arg0, byte arg1) {
        try {
            find("matches_only_at_eod", void.class, MemorySegment.class, byte.class).invokeExact(arg0, arg1);
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