package com.xenoamess.hyperscan_panama.jni.generated;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/** Compatibility facade for hs_alloc_t. */
public final class hs_alloc_t {

    private static final Class<?> DELEGATE;
    private static final String PLATFORM_FAMILY;
    private static final ConcurrentHashMap<String, MethodHandle> HANDLES = new ConcurrentHashMap<>();

    static {
        String platform = System.getProperty("com.xenoamess.hyperscan_panama.platform");
        if (platform == null || platform.isEmpty()) {
            platform = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatform();
        }
        PLATFORM_FAMILY = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatformFamily(platform);
        String className = "com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.hs_alloc_t";
        try {
            DELEGATE = Class.forName(className);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hs_alloc_t class: " + className, e);
        }
    }

    private hs_alloc_t() {}

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

    @FunctionalInterface
    public interface Function {
        MemorySegment apply(long arg0);
    }

    public static FunctionDescriptor descriptor() {
        try {
            return (FunctionDescriptor) find("descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment allocate(hs_alloc_t.Function arg0, Arena arg1) {
        try {
            Class<?> platformFunc = Class.forName("com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.hs_alloc_t$Function");
            MethodHandle mh = MethodHandles.lookup().findVirtual(Function.class, "apply", MethodType.methodType(MemorySegment.class, long.class)).bindTo(arg0);
            Object platformFi = MethodHandleProxies.asInterfaceInstance(platformFunc, mh);
            return (MemorySegment) find("allocate", MemorySegment.class, platformFunc, Arena.class).invoke(platformFi, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment invoke(MemorySegment arg0, long arg1) {
        try {
            return (MemorySegment) find("invoke", MemorySegment.class, MemorySegment.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}