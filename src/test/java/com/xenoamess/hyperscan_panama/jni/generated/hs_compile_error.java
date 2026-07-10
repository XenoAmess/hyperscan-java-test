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
            MH_LAYOUT = find("layout", GroupLayout.class);
            MH_MESSAGE_LAYOUT = find("message$layout", AddressLayout.class);
            MH_MESSAGE_OFFSET = find("message$offset", long.class);
            MH_MESSAGE = find("message", MemorySegment.class, MemorySegment.class);
            MH_MESSAGE_1 = find("message", void.class, MemorySegment.class, MemorySegment.class);
            MH_EXPRESSION_LAYOUT = find("expression$layout", ValueLayout.OfInt.class);
            MH_EXPRESSION_OFFSET = find("expression$offset", long.class);
            MH_EXPRESSION = find("expression", int.class, MemorySegment.class);
            MH_EXPRESSION_1 = find("expression", void.class, MemorySegment.class, int.class);
            MH_ASSLICE = find("asSlice", MemorySegment.class, MemorySegment.class, long.class);
            MH_SIZEOF = find("sizeof", long.class);
            MH_ALLOCATE = find("allocate", MemorySegment.class, SegmentAllocator.class);
            MH_ALLOCATEARRAY = find("allocateArray", MemorySegment.class, long.class, SegmentAllocator.class);
            MH_REINTERPRET = find("reinterpret", MemorySegment.class, MemorySegment.class, Arena.class, Consumer.class);
            MH_REINTERPRET_1 = find("reinterpret", MemorySegment.class, MemorySegment.class, long.class, Arena.class, Consumer.class);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hs_compile_error class: " + className, e);
        }
    }

    private hs_compile_error() {}

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

    public static AddressLayout message$layout() {
        try {
            return (AddressLayout) MH_MESSAGE_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long message$offset() {
        try {
            return (long) MH_MESSAGE_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment message(MemorySegment arg0) {
        try {
            return (MemorySegment) MH_MESSAGE.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void message(MemorySegment arg0, MemorySegment arg1) {
        try {
            MH_MESSAGE_1.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ValueLayout.OfInt expression$layout() {
        try {
            return (ValueLayout.OfInt) MH_EXPRESSION_LAYOUT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long expression$offset() {
        try {
            return (long) MH_EXPRESSION_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int expression(MemorySegment arg0) {
        try {
            return (int) MH_EXPRESSION.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void expression(MemorySegment arg0, int arg1) {
        try {
            MH_EXPRESSION_1.invokeExact(arg0, arg1);
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