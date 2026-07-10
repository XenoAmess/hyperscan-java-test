package com.xenoamess.hyperscan_panama.jni.generated;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/** Optimized compatibility facade for match_event_handler. */
public final class match_event_handler {

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
        String className = "com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.match_event_handler";
        try {
            DELEGATE = Class.forName(className);
            // no SYMBOL_LOOKUP/LIBRARY_ARENA in functional/struct classes
            PLATFORM_FUNCTION_CLASS = Class.forName("com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.match_event_handler$Function");
            MH_DESCRIPTOR = find("descriptor", FunctionDescriptor.class);
            MH_ALLOCATE = find("allocate", MemorySegment.class, PLATFORM_FUNCTION_CLASS, Arena.class);
            MH_INVOKE = find("invoke", int.class, MemorySegment.class, int.class, long.class, long.class, int.class, MemorySegment.class);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific match_event_handler class: " + className, e);
        }
    }

    private match_event_handler() {}

    private static MethodHandle find(String name, Class<?> rtype, Class<?>... ptypes) {
        try {
            return MethodHandles.publicLookup().findStatic(DELEGATE, name, MethodType.methodType(rtype, ptypes));
        } catch (Exception e) {
            throw new RuntimeException("Failed to find method " + name + " in " + DELEGATE.getName(), e);
        }
    }

    @FunctionalInterface
    public interface Function {
        int apply(int arg0, long arg1, long arg2, int arg3, MemorySegment arg4);
    }

    public static FunctionDescriptor descriptor() {
        try {
            return (FunctionDescriptor) MH_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment allocate(match_event_handler.Function arg0, Arena arg1) {
        try {
            MethodHandle mh = MethodHandles.lookup().findVirtual(Function.class, "apply", MethodType.methodType(int.class, int.class, long.class, long.class, int.class, MemorySegment.class)).bindTo(arg0);
            Object platformFi = MethodHandleProxies.asInterfaceInstance(PLATFORM_FUNCTION_CLASS, mh);
            return (MemorySegment) MH_ALLOCATE.invoke(platformFi, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int invoke(MemorySegment arg0, int arg1, long arg2, long arg3, int arg4, MemorySegment arg5) {
        try {
            return (int) MH_INVOKE.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}