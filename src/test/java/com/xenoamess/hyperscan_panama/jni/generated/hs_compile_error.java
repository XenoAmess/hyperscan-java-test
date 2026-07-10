package com.xenoamess.hyperscan_panama.jni.generated;

import java.lang.foreign.AddressLayout;
import java.lang.foreign.Arena;
import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Consumer;

/** Optimized compatibility facade for hs_compile_error. */
public final class hs_compile_error {

    private static final Class<?> DELEGATE;
    private static final String PLATFORM_FAMILY;

    private static final MethodHandle MH_LAYOUT;
    private static final MethodHandle MH_MESSAGE_LAYOUT;
    private static final MethodHandle MH_MESSAGE_OFFSET;
    private static final MethodHandle MH_MESSAGE;
    private static final MethodHandle MH_MESSAGE_1;
    private static final MethodHandle MH_EXPRESSION_LAYOUT;
    private static final MethodHandle MH_EXPRESSION_OFFSET;
    private static final MethodHandle MH_EXPRESSION;
    private static final MethodHandle MH_EXPRESSION_1;
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
        String className = "com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.hs_compile_error";
        try {
            DELEGATE = Class.forName(className);
            // no SYMBOL_LOOKUP/LIBRARY_ARENA in functional/struct classes
            MH_LAYOUT = findOrNull("layout", GroupLayout.class);
            MH_MESSAGE_LAYOUT = findOrNull("message$layout", AddressLayout.class);
            MH_MESSAGE_OFFSET = findOrNull("message$offset", long.class);
            MH_MESSAGE = findOrNull("message", MemorySegment.class, MemorySegment.class);
            MH_MESSAGE_1 = findOrNull("message", void.class, MemorySegment.class, MemorySegment.class);
            MH_EXPRESSION_LAYOUT = findOrNull("expression$layout", ValueLayout.OfInt.class);
            MH_EXPRESSION_OFFSET = findOrNull("expression$offset", long.class);
            MH_EXPRESSION = findOrNull("expression", int.class, MemorySegment.class);
            MH_EXPRESSION_1 = findOrNull("expression", void.class, MemorySegment.class, int.class);
            MH_ASSLICE = findOrNull("asSlice", MemorySegment.class, MemorySegment.class, long.class);
            MH_SIZEOF = findOrNull("sizeof", long.class);
            MH_ALLOCATE = findOrNull("allocate", MemorySegment.class, SegmentAllocator.class);
            MH_ALLOCATEARRAY = findOrNull("allocateArray", MemorySegment.class, long.class, SegmentAllocator.class);
            MH_REINTERPRET = findOrNull("reinterpret", MemorySegment.class, MemorySegment.class, Arena.class, Consumer.class);
            MH_REINTERPRET_1 = findOrNull("reinterpret", MemorySegment.class, MemorySegment.class, long.class, Arena.class, Consumer.class);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hs_compile_error class: " + className, e);
        }
    }

    private hs_compile_error() {}

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

    public static AddressLayout message$layout() {
        try {
            return (AddressLayout) require("message$layout", MH_MESSAGE_LAYOUT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long message$offset() {
        try {
            return (long) require("message$offset", MH_MESSAGE_OFFSET).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment message(MemorySegment arg0) {
        try {
            return (MemorySegment) require("message", MH_MESSAGE).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void message(MemorySegment arg0, MemorySegment arg1) {
        try {
            require("message", MH_MESSAGE_1).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfInt expression$layout() {
        try {
            return (ValueLayout.OfInt) require("expression$layout", MH_EXPRESSION_LAYOUT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long expression$offset() {
        try {
            return (long) require("expression$offset", MH_EXPRESSION_OFFSET).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int expression(MemorySegment arg0) {
        try {
            return (int) require("expression", MH_EXPRESSION).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void expression(MemorySegment arg0, int arg1) {
        try {
            require("expression", MH_EXPRESSION_1).invokeExact(arg0, arg1);
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