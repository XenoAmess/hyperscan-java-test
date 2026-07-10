package com.xenoamess.hyperscan_panama.jni.generated;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/** Optimized compatibility facade for hs_alloc_t. */
public final class hs_alloc_t {

    private static final Class<?> DELEGATE;
    private static final String PLATFORM_FAMILY;
    private static final Class<?> PLATFORM_FUNCTION_CLASS;

    private static final MethodHandle MH_DESCRIPTOR;
    private static final MethodHandle MH_ALLOCATE;
    private static final MethodHandle MH_INVOKE;

    static {
        String platform = System.getProperty("com.xenoamess.hyperscan_panama.platform");
        if (platform == null || platform.isEmpty()) {
            platform = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatform();
        }
        PLATFORM_FAMILY = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatformFamily(platform);
        String className = "com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.hs_alloc_t";
        try {
            DELEGATE = Class.forName(className);
            // no SYMBOL_LOOKUP/LIBRARY_ARENA in functional/struct classes
            PLATFORM_FUNCTION_CLASS = Class.forName("com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.hs_alloc_t$Function");
            MH_DESCRIPTOR = findOrNull("descriptor", FunctionDescriptor.class);
            MH_ALLOCATE = findOrNull("allocate", MemorySegment.class, PLATFORM_FUNCTION_CLASS, Arena.class);
            MH_INVOKE = findOrNull("invoke", MemorySegment.class, MemorySegment.class, long.class);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hs_alloc_t class: " + className, e);
        }
    }

    private hs_alloc_t() {}

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

    @FunctionalInterface
    public interface Function {
        MemorySegment apply(long arg0);
    }

    public static FunctionDescriptor descriptor() {
        try {
            return (FunctionDescriptor) require("descriptor", MH_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment allocate(hs_alloc_t.Function arg0, Arena arg1) {
        try {
            MethodHandle mh = MethodHandles.lookup().findVirtual(Function.class, "apply", MethodType.methodType(MemorySegment.class, long.class)).bindTo(arg0);
            Object platformFi = MethodHandleProxies.asInterfaceInstance(PLATFORM_FUNCTION_CLASS, mh);
            return (MemorySegment) require("allocate", MH_ALLOCATE).invoke(platformFi, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment invoke(MemorySegment arg0, long arg1) {
        try {
            return (MemorySegment) require("invoke", MH_INVOKE).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}