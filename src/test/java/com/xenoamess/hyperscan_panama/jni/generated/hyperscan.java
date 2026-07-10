package com.xenoamess.hyperscan_panama.jni.generated;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/** Compatibility facade for hyperscan. */
public class hyperscan {

    private static final Class<?> DELEGATE;
    private static final String PLATFORM_FAMILY;
    public static final java.lang.foreign.SymbolLookup SYMBOL_LOOKUP;
    public static final java.lang.foreign.Arena LIBRARY_ARENA;
    private static final ConcurrentHashMap<String, MethodHandle> HANDLES = new ConcurrentHashMap<>();

    static {
        String platform = System.getProperty("com.xenoamess.hyperscan_panama.platform");
        if (platform == null || platform.isEmpty()) {
            platform = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatform();
        }
        PLATFORM_FAMILY = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatformFamily(platform);
        String className = "com.xenoamess.hyperscan_panama.jni." + PLATFORM_FAMILY.replace('-', '_') + ".generated.hyperscan";
        try {
            DELEGATE = Class.forName(className);
            java.lang.reflect.Field slField = DELEGATE.getDeclaredField("SYMBOL_LOOKUP");
            slField.setAccessible(true);
            SYMBOL_LOOKUP = (java.lang.foreign.SymbolLookup) slField.get(null);
            java.lang.reflect.Field laField = DELEGATE.getDeclaredField("LIBRARY_ARENA");
            laField.setAccessible(true);
            LIBRARY_ARENA = (java.lang.foreign.Arena) laField.get(null);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hyperscan class: " + className, e);
        }
    }

    private hyperscan() {}

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

    public static int _FEATURES_H() {
        try {
            return (int) find("_FEATURES_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _DEFAULT_SOURCE() {
        try {
            return (int) find("_DEFAULT_SOURCE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_ISOC2X() {
        try {
            return (int) find("__GLIBC_USE_ISOC2X", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_ISOC11() {
        try {
            return (int) find("__USE_ISOC11", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_ISOC99() {
        try {
            return (int) find("__USE_ISOC99", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_ISOC95() {
        try {
            return (int) find("__USE_ISOC95", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX_IMPLICITLY() {
        try {
            return (int) find("__USE_POSIX_IMPLICITLY", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _POSIX_SOURCE() {
        try {
            return (int) find("_POSIX_SOURCE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX() {
        try {
            return (int) find("__USE_POSIX", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX2() {
        try {
            return (int) find("__USE_POSIX2", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX199309() {
        try {
            return (int) find("__USE_POSIX199309", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX199506() {
        try {
            return (int) find("__USE_POSIX199506", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_XOPEN2K() {
        try {
            return (int) find("__USE_XOPEN2K", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_XOPEN2K8() {
        try {
            return (int) find("__USE_XOPEN2K8", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _ATFILE_SOURCE() {
        try {
            return (int) find("_ATFILE_SOURCE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WORDSIZE() {
        try {
            return (int) find("__WORDSIZE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WORDSIZE_TIME64_COMPAT32() {
        try {
            return (int) find("__WORDSIZE_TIME64_COMPAT32", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SYSCALL_WORDSIZE() {
        try {
            return (int) find("__SYSCALL_WORDSIZE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_MISC() {
        try {
            return (int) find("__USE_MISC", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_ATFILE() {
        try {
            return (int) find("__USE_ATFILE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_FORTIFY_LEVEL() {
        try {
            return (int) find("__USE_FORTIFY_LEVEL", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_DEPRECATED_GETS() {
        try {
            return (int) find("__GLIBC_USE_DEPRECATED_GETS", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_DEPRECATED_SCANF() {
        try {
            return (int) find("__GLIBC_USE_DEPRECATED_SCANF", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_C2X_STRTOL() {
        try {
            return (int) find("__GLIBC_USE_C2X_STRTOL", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _STDC_PREDEF_H() {
        try {
            return (int) find("_STDC_PREDEF_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __STDC_IEC_559__() {
        try {
            return (int) find("__STDC_IEC_559__", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __STDC_IEC_559_COMPLEX__() {
        try {
            return (int) find("__STDC_IEC_559_COMPLEX__", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GNU_LIBRARY__() {
        try {
            return (int) find("__GNU_LIBRARY__", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC__() {
        try {
            return (int) find("__GLIBC__", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_MINOR__() {
        try {
            return (int) find("__GLIBC_MINOR__", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _SYS_CDEFS_H() {
        try {
            return (int) find("_SYS_CDEFS_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __glibc_c99_flexarr_available() {
        try {
            return (int) find("__glibc_c99_flexarr_available", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __LDOUBLE_REDIRECTS_TO_FLOAT128_ABI() {
        try {
            return (int) find("__LDOUBLE_REDIRECTS_TO_FLOAT128_ABI", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_GENERIC_SELECTION() {
        try {
            return (int) find("__HAVE_GENERIC_SELECTION", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_LIB_EXT2() {
        try {
            return (int) find("__GLIBC_USE_LIB_EXT2", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_BFP_EXT() {
        try {
            return (int) find("__GLIBC_USE_IEC_60559_BFP_EXT", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_BFP_EXT_C2X() {
        try {
            return (int) find("__GLIBC_USE_IEC_60559_BFP_EXT_C2X", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_EXT() {
        try {
            return (int) find("__GLIBC_USE_IEC_60559_EXT", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_FUNCS_EXT() {
        try {
            return (int) find("__GLIBC_USE_IEC_60559_FUNCS_EXT", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_FUNCS_EXT_C2X() {
        try {
            return (int) find("__GLIBC_USE_IEC_60559_FUNCS_EXT_C2X", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_TYPES_EXT() {
        try {
            return (int) find("__GLIBC_USE_IEC_60559_TYPES_EXT", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _STDLIB_H() {
        try {
            return (int) find("_STDLIB_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WNOHANG() {
        try {
            return (int) find("WNOHANG", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WUNTRACED() {
        try {
            return (int) find("WUNTRACED", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WSTOPPED() {
        try {
            return (int) find("WSTOPPED", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WEXITED() {
        try {
            return (int) find("WEXITED", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WCONTINUED() {
        try {
            return (int) find("WCONTINUED", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WNOWAIT() {
        try {
            return (int) find("WNOWAIT", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WNOTHREAD() {
        try {
            return (int) find("__WNOTHREAD", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WALL() {
        try {
            return (int) find("__WALL", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __W_CONTINUED() {
        try {
            return (int) find("__W_CONTINUED", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WCOREFLAG() {
        try {
            return (int) find("__WCOREFLAG", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT128() {
        try {
            return (int) find("__HAVE_FLOAT128", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT128() {
        try {
            return (int) find("__HAVE_DISTINCT_FLOAT128", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT64X() {
        try {
            return (int) find("__HAVE_FLOAT64X", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT64X_LONG_DOUBLE() {
        try {
            return (int) find("__HAVE_FLOAT64X_LONG_DOUBLE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT16() {
        try {
            return (int) find("__HAVE_FLOAT16", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT32() {
        try {
            return (int) find("__HAVE_FLOAT32", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT64() {
        try {
            return (int) find("__HAVE_FLOAT64", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT32X() {
        try {
            return (int) find("__HAVE_FLOAT32X", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT128X() {
        try {
            return (int) find("__HAVE_FLOAT128X", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT32() {
        try {
            return (int) find("__HAVE_DISTINCT_FLOAT32", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT64() {
        try {
            return (int) find("__HAVE_DISTINCT_FLOAT64", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT32X() {
        try {
            return (int) find("__HAVE_DISTINCT_FLOAT32X", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT64X() {
        try {
            return (int) find("__HAVE_DISTINCT_FLOAT64X", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOATN_NOT_TYPEDEF() {
        try {
            return (int) find("__HAVE_FLOATN_NOT_TYPEDEF", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __ldiv_t_defined() {
        try {
            return (int) find("__ldiv_t_defined", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __lldiv_t_defined() {
        try {
            return (int) find("__lldiv_t_defined", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int RAND_MAX() {
        try {
            return (int) find("RAND_MAX", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int EXIT_FAILURE() {
        try {
            return (int) find("EXIT_FAILURE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int EXIT_SUCCESS() {
        try {
            return (int) find("EXIT_SUCCESS", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _SYS_TYPES_H() {
        try {
            return (int) find("_SYS_TYPES_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_TYPES_H() {
        try {
            return (int) find("_BITS_TYPES_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_TYPESIZES_H() {
        try {
            return (int) find("_BITS_TYPESIZES_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __OFF_T_MATCHES_OFF64_T() {
        try {
            return (int) find("__OFF_T_MATCHES_OFF64_T", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __INO_T_MATCHES_INO64_T() {
        try {
            return (int) find("__INO_T_MATCHES_INO64_T", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __RLIM_T_MATCHES_RLIM64_T() {
        try {
            return (int) find("__RLIM_T_MATCHES_RLIM64_T", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __STATFS_MATCHES_STATFS64() {
        try {
            return (int) find("__STATFS_MATCHES_STATFS64", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __KERNEL_OLD_TIMEVAL_MATCHES_TIMEVAL64() {
        try {
            return (int) find("__KERNEL_OLD_TIMEVAL_MATCHES_TIMEVAL64", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __FD_SETSIZE() {
        try {
            return (int) find("__FD_SETSIZE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_TIME64_H() {
        try {
            return (int) find("_BITS_TIME64_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __clock_t_defined() {
        try {
            return (int) find("__clock_t_defined", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __clockid_t_defined() {
        try {
            return (int) find("__clockid_t_defined", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __time_t_defined() {
        try {
            return (int) find("__time_t_defined", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __timer_t_defined() {
        try {
            return (int) find("__timer_t_defined", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_STDINT_INTN_H() {
        try {
            return (int) find("_BITS_STDINT_INTN_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __BIT_TYPES_DEFINED__() {
        try {
            return (int) find("__BIT_TYPES_DEFINED__", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _ENDIAN_H() {
        try {
            return (int) find("_ENDIAN_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_ENDIAN_H() {
        try {
            return (int) find("_BITS_ENDIAN_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __LITTLE_ENDIAN() {
        try {
            return (int) find("__LITTLE_ENDIAN", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __BIG_ENDIAN() {
        try {
            return (int) find("__BIG_ENDIAN", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __PDP_ENDIAN() {
        try {
            return (int) find("__PDP_ENDIAN", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_ENDIANNESS_H() {
        try {
            return (int) find("_BITS_ENDIANNESS_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_BYTESWAP_H() {
        try {
            return (int) find("_BITS_BYTESWAP_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_UINTN_IDENTITY_H() {
        try {
            return (int) find("_BITS_UINTN_IDENTITY_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _SYS_SELECT_H() {
        try {
            return (int) find("_SYS_SELECT_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __sigset_t_defined() {
        try {
            return (int) find("__sigset_t_defined", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __timeval_defined() {
        try {
            return (int) find("__timeval_defined", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _STRUCT_TIMESPEC() {
        try {
            return (int) find("_STRUCT_TIMESPEC", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_PTHREADTYPES_COMMON_H() {
        try {
            return (int) find("_BITS_PTHREADTYPES_COMMON_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _THREAD_SHARED_TYPES_H() {
        try {
            return (int) find("_THREAD_SHARED_TYPES_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_PTHREADTYPES_ARCH_H() {
        try {
            return (int) find("_BITS_PTHREADTYPES_ARCH_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_MUTEX_T() {
        try {
            return (int) find("__SIZEOF_PTHREAD_MUTEX_T", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_ATTR_T() {
        try {
            return (int) find("__SIZEOF_PTHREAD_ATTR_T", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_RWLOCK_T() {
        try {
            return (int) find("__SIZEOF_PTHREAD_RWLOCK_T", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_BARRIER_T() {
        try {
            return (int) find("__SIZEOF_PTHREAD_BARRIER_T", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_MUTEXATTR_T() {
        try {
            return (int) find("__SIZEOF_PTHREAD_MUTEXATTR_T", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_COND_T() {
        try {
            return (int) find("__SIZEOF_PTHREAD_COND_T", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_CONDATTR_T() {
        try {
            return (int) find("__SIZEOF_PTHREAD_CONDATTR_T", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_RWLOCKATTR_T() {
        try {
            return (int) find("__SIZEOF_PTHREAD_RWLOCKATTR_T", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_BARRIERATTR_T() {
        try {
            return (int) find("__SIZEOF_PTHREAD_BARRIERATTR_T", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _THREAD_MUTEX_INTERNAL_H() {
        try {
            return (int) find("_THREAD_MUTEX_INTERNAL_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __PTHREAD_MUTEX_HAVE_PREV() {
        try {
            return (int) find("__PTHREAD_MUTEX_HAVE_PREV", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __have_pthread_attr_t() {
        try {
            return (int) find("__have_pthread_attr_t", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _ALLOCA_H() {
        try {
            return (int) find("_ALLOCA_H", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_SUCCESS() {
        try {
            return (int) find("HS_SUCCESS", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_CASELESS() {
        try {
            return (int) find("HS_FLAG_CASELESS", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_DOTALL() {
        try {
            return (int) find("HS_FLAG_DOTALL", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_MULTILINE() {
        try {
            return (int) find("HS_FLAG_MULTILINE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_SINGLEMATCH() {
        try {
            return (int) find("HS_FLAG_SINGLEMATCH", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_ALLOWEMPTY() {
        try {
            return (int) find("HS_FLAG_ALLOWEMPTY", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_UTF8() {
        try {
            return (int) find("HS_FLAG_UTF8", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_UCP() {
        try {
            return (int) find("HS_FLAG_UCP", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_PREFILTER() {
        try {
            return (int) find("HS_FLAG_PREFILTER", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_SOM_LEFTMOST() {
        try {
            return (int) find("HS_FLAG_SOM_LEFTMOST", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_COMBINATION() {
        try {
            return (int) find("HS_FLAG_COMBINATION", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_QUIET() {
        try {
            return (int) find("HS_FLAG_QUIET", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_GENERIC() {
        try {
            return (int) find("HS_TUNE_FAMILY_GENERIC", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_SNB() {
        try {
            return (int) find("HS_TUNE_FAMILY_SNB", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_IVB() {
        try {
            return (int) find("HS_TUNE_FAMILY_IVB", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_HSW() {
        try {
            return (int) find("HS_TUNE_FAMILY_HSW", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_SLM() {
        try {
            return (int) find("HS_TUNE_FAMILY_SLM", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_BDW() {
        try {
            return (int) find("HS_TUNE_FAMILY_BDW", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_SKL() {
        try {
            return (int) find("HS_TUNE_FAMILY_SKL", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_SKX() {
        try {
            return (int) find("HS_TUNE_FAMILY_SKX", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_GLM() {
        try {
            return (int) find("HS_TUNE_FAMILY_GLM", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_ICL() {
        try {
            return (int) find("HS_TUNE_FAMILY_ICL", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_ICX() {
        try {
            return (int) find("HS_TUNE_FAMILY_ICX", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_BLOCK() {
        try {
            return (int) find("HS_MODE_BLOCK", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_NOSTREAM() {
        try {
            return (int) find("HS_MODE_NOSTREAM", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_STREAM() {
        try {
            return (int) find("HS_MODE_STREAM", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_VECTORED() {
        try {
            return (int) find("HS_MODE_VECTORED", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MAJOR() {
        try {
            return (int) find("HS_MAJOR", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MINOR() {
        try {
            return (int) find("HS_MINOR", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_PATCH() {
        try {
            return (int) find("HS_PATCH", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor __ctype_get_mb_cur_max$descriptor() {
        try {
            return (FunctionDescriptor) find("__ctype_get_mb_cur_max$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle __ctype_get_mb_cur_max$handle() {
        try {
            return (MethodHandle) find("__ctype_get_mb_cur_max$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment __ctype_get_mb_cur_max$address() {
        try {
            return (MemorySegment) find("__ctype_get_mb_cur_max$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long __ctype_get_mb_cur_max() {
        try {
            return (long) find("__ctype_get_mb_cur_max", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atof$descriptor() {
        try {
            return (FunctionDescriptor) find("atof$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atof$handle() {
        try {
            return (MethodHandle) find("atof$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atof$address() {
        try {
            return (MemorySegment) find("atof$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static double atof(MemorySegment arg0) {
        try {
            return (double) find("atof", double.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atoi$descriptor() {
        try {
            return (FunctionDescriptor) find("atoi$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atoi$handle() {
        try {
            return (MethodHandle) find("atoi$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atoi$address() {
        try {
            return (MemorySegment) find("atoi$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int atoi(MemorySegment arg0) {
        try {
            return (int) find("atoi", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atol$descriptor() {
        try {
            return (FunctionDescriptor) find("atol$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atol$handle() {
        try {
            return (MethodHandle) find("atol$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atol$address() {
        try {
            return (MemorySegment) find("atol$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long atol(MemorySegment arg0) {
        try {
            return (long) find("atol", long.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atoll$descriptor() {
        try {
            return (FunctionDescriptor) find("atoll$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atoll$handle() {
        try {
            return (MethodHandle) find("atoll$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atoll$address() {
        try {
            return (MemorySegment) find("atoll$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long atoll(MemorySegment arg0) {
        try {
            return (long) find("atoll", long.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtod$descriptor() {
        try {
            return (FunctionDescriptor) find("strtod$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtod$handle() {
        try {
            return (MethodHandle) find("strtod$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtod$address() {
        try {
            return (MemorySegment) find("strtod$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static double strtod(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (double) find("strtod", double.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtof$descriptor() {
        try {
            return (FunctionDescriptor) find("strtof$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtof$handle() {
        try {
            return (MethodHandle) find("strtof$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtof$address() {
        try {
            return (MemorySegment) find("strtof$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static float strtof(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (float) find("strtof", float.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtol$descriptor() {
        try {
            return (FunctionDescriptor) find("strtol$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtol$handle() {
        try {
            return (MethodHandle) find("strtol$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtol$address() {
        try {
            return (MemorySegment) find("strtol$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtol(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) find("strtol", long.class, MemorySegment.class, MemorySegment.class, int.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtoul$descriptor() {
        try {
            return (FunctionDescriptor) find("strtoul$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtoul$handle() {
        try {
            return (MethodHandle) find("strtoul$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtoul$address() {
        try {
            return (MemorySegment) find("strtoul$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtoul(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) find("strtoul", long.class, MemorySegment.class, MemorySegment.class, int.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtoq$descriptor() {
        try {
            return (FunctionDescriptor) find("strtoq$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtoq$handle() {
        try {
            return (MethodHandle) find("strtoq$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtoq$address() {
        try {
            return (MemorySegment) find("strtoq$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtoq(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) find("strtoq", long.class, MemorySegment.class, MemorySegment.class, int.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtouq$descriptor() {
        try {
            return (FunctionDescriptor) find("strtouq$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtouq$handle() {
        try {
            return (MethodHandle) find("strtouq$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtouq$address() {
        try {
            return (MemorySegment) find("strtouq$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtouq(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) find("strtouq", long.class, MemorySegment.class, MemorySegment.class, int.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtoll$descriptor() {
        try {
            return (FunctionDescriptor) find("strtoll$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtoll$handle() {
        try {
            return (MethodHandle) find("strtoll$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtoll$address() {
        try {
            return (MemorySegment) find("strtoll$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtoll(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) find("strtoll", long.class, MemorySegment.class, MemorySegment.class, int.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtoull$descriptor() {
        try {
            return (FunctionDescriptor) find("strtoull$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtoull$handle() {
        try {
            return (MethodHandle) find("strtoull$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtoull$address() {
        try {
            return (MemorySegment) find("strtoull$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtoull(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) find("strtoull", long.class, MemorySegment.class, MemorySegment.class, int.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor l64a$descriptor() {
        try {
            return (FunctionDescriptor) find("l64a$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle l64a$handle() {
        try {
            return (MethodHandle) find("l64a$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment l64a$address() {
        try {
            return (MemorySegment) find("l64a$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment l64a(long arg0) {
        try {
            return (MemorySegment) find("l64a", MemorySegment.class, long.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor a64l$descriptor() {
        try {
            return (FunctionDescriptor) find("a64l$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle a64l$handle() {
        try {
            return (MethodHandle) find("a64l$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment a64l$address() {
        try {
            return (MemorySegment) find("a64l$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long a64l(MemorySegment arg0) {
        try {
            return (long) find("a64l", long.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor select$descriptor() {
        try {
            return (FunctionDescriptor) find("select$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle select$handle() {
        try {
            return (MethodHandle) find("select$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment select$address() {
        try {
            return (MemorySegment) find("select$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int select(int arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4) {
        try {
            return (int) find("select", int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor pselect$descriptor() {
        try {
            return (FunctionDescriptor) find("pselect$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle pselect$handle() {
        try {
            return (MethodHandle) find("pselect$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment pselect$address() {
        try {
            return (MemorySegment) find("pselect$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int pselect(int arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4, MemorySegment arg5) {
        try {
            return (int) find("pselect", int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor random$descriptor() {
        try {
            return (FunctionDescriptor) find("random$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle random$handle() {
        try {
            return (MethodHandle) find("random$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment random$address() {
        try {
            return (MemorySegment) find("random$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long random() {
        try {
            return (long) find("random", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srandom$descriptor() {
        try {
            return (FunctionDescriptor) find("srandom$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srandom$handle() {
        try {
            return (MethodHandle) find("srandom$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srandom$address() {
        try {
            return (MemorySegment) find("srandom$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void srandom(int arg0) {
        try {
            find("srandom", void.class, int.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor initstate$descriptor() {
        try {
            return (FunctionDescriptor) find("initstate$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle initstate$handle() {
        try {
            return (MethodHandle) find("initstate$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment initstate$address() {
        try {
            return (MemorySegment) find("initstate$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment initstate(int arg0, MemorySegment arg1, long arg2) {
        try {
            return (MemorySegment) find("initstate", MemorySegment.class, int.class, MemorySegment.class, long.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor setstate$descriptor() {
        try {
            return (FunctionDescriptor) find("setstate$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle setstate$handle() {
        try {
            return (MethodHandle) find("setstate$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment setstate$address() {
        try {
            return (MemorySegment) find("setstate$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment setstate(MemorySegment arg0) {
        try {
            return (MemorySegment) find("setstate", MemorySegment.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor random_r$descriptor() {
        try {
            return (FunctionDescriptor) find("random_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle random_r$handle() {
        try {
            return (MethodHandle) find("random_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment random_r$address() {
        try {
            return (MemorySegment) find("random_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int random_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("random_r", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srandom_r$descriptor() {
        try {
            return (FunctionDescriptor) find("srandom_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srandom_r$handle() {
        try {
            return (MethodHandle) find("srandom_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srandom_r$address() {
        try {
            return (MemorySegment) find("srandom_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int srandom_r(int arg0, MemorySegment arg1) {
        try {
            return (int) find("srandom_r", int.class, int.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor initstate_r$descriptor() {
        try {
            return (FunctionDescriptor) find("initstate_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle initstate_r$handle() {
        try {
            return (MethodHandle) find("initstate_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment initstate_r$address() {
        try {
            return (MemorySegment) find("initstate_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int initstate_r(int arg0, MemorySegment arg1, long arg2, MemorySegment arg3) {
        try {
            return (int) find("initstate_r", int.class, int.class, MemorySegment.class, long.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor setstate_r$descriptor() {
        try {
            return (FunctionDescriptor) find("setstate_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle setstate_r$handle() {
        try {
            return (MethodHandle) find("setstate_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment setstate_r$address() {
        try {
            return (MemorySegment) find("setstate_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int setstate_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("setstate_r", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor rand$descriptor() {
        try {
            return (FunctionDescriptor) find("rand$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle rand$handle() {
        try {
            return (MethodHandle) find("rand$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment rand$address() {
        try {
            return (MemorySegment) find("rand$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int rand() {
        try {
            return (int) find("rand", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srand$descriptor() {
        try {
            return (FunctionDescriptor) find("srand$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srand$handle() {
        try {
            return (MethodHandle) find("srand$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srand$address() {
        try {
            return (MemorySegment) find("srand$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void srand(int arg0) {
        try {
            find("srand", void.class, int.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor rand_r$descriptor() {
        try {
            return (FunctionDescriptor) find("rand_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle rand_r$handle() {
        try {
            return (MethodHandle) find("rand_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment rand_r$address() {
        try {
            return (MemorySegment) find("rand_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int rand_r(MemorySegment arg0) {
        try {
            return (int) find("rand_r", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor drand48$descriptor() {
        try {
            return (FunctionDescriptor) find("drand48$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle drand48$handle() {
        try {
            return (MethodHandle) find("drand48$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment drand48$address() {
        try {
            return (MemorySegment) find("drand48$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static double drand48() {
        try {
            return (double) find("drand48", double.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor erand48$descriptor() {
        try {
            return (FunctionDescriptor) find("erand48$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle erand48$handle() {
        try {
            return (MethodHandle) find("erand48$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment erand48$address() {
        try {
            return (MemorySegment) find("erand48$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static double erand48(MemorySegment arg0) {
        try {
            return (double) find("erand48", double.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lrand48$descriptor() {
        try {
            return (FunctionDescriptor) find("lrand48$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lrand48$handle() {
        try {
            return (MethodHandle) find("lrand48$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lrand48$address() {
        try {
            return (MemorySegment) find("lrand48$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long lrand48() {
        try {
            return (long) find("lrand48", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor nrand48$descriptor() {
        try {
            return (FunctionDescriptor) find("nrand48$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle nrand48$handle() {
        try {
            return (MethodHandle) find("nrand48$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment nrand48$address() {
        try {
            return (MemorySegment) find("nrand48$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long nrand48(MemorySegment arg0) {
        try {
            return (long) find("nrand48", long.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mrand48$descriptor() {
        try {
            return (FunctionDescriptor) find("mrand48$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mrand48$handle() {
        try {
            return (MethodHandle) find("mrand48$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mrand48$address() {
        try {
            return (MemorySegment) find("mrand48$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long mrand48() {
        try {
            return (long) find("mrand48", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor jrand48$descriptor() {
        try {
            return (FunctionDescriptor) find("jrand48$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle jrand48$handle() {
        try {
            return (MethodHandle) find("jrand48$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment jrand48$address() {
        try {
            return (MemorySegment) find("jrand48$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long jrand48(MemorySegment arg0) {
        try {
            return (long) find("jrand48", long.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srand48$descriptor() {
        try {
            return (FunctionDescriptor) find("srand48$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srand48$handle() {
        try {
            return (MethodHandle) find("srand48$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srand48$address() {
        try {
            return (MemorySegment) find("srand48$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void srand48(long arg0) {
        try {
            find("srand48", void.class, long.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor seed48$descriptor() {
        try {
            return (FunctionDescriptor) find("seed48$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle seed48$handle() {
        try {
            return (MethodHandle) find("seed48$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment seed48$address() {
        try {
            return (MemorySegment) find("seed48$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment seed48(MemorySegment arg0) {
        try {
            return (MemorySegment) find("seed48", MemorySegment.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lcong48$descriptor() {
        try {
            return (FunctionDescriptor) find("lcong48$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lcong48$handle() {
        try {
            return (MethodHandle) find("lcong48$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lcong48$address() {
        try {
            return (MemorySegment) find("lcong48$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void lcong48(MemorySegment arg0) {
        try {
            find("lcong48", void.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor drand48_r$descriptor() {
        try {
            return (FunctionDescriptor) find("drand48_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle drand48_r$handle() {
        try {
            return (MethodHandle) find("drand48_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment drand48_r$address() {
        try {
            return (MemorySegment) find("drand48_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int drand48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("drand48_r", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor erand48_r$descriptor() {
        try {
            return (FunctionDescriptor) find("erand48_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle erand48_r$handle() {
        try {
            return (MethodHandle) find("erand48_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment erand48_r$address() {
        try {
            return (MemorySegment) find("erand48_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int erand48_r(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) find("erand48_r", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lrand48_r$descriptor() {
        try {
            return (FunctionDescriptor) find("lrand48_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lrand48_r$handle() {
        try {
            return (MethodHandle) find("lrand48_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lrand48_r$address() {
        try {
            return (MemorySegment) find("lrand48_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int lrand48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("lrand48_r", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor nrand48_r$descriptor() {
        try {
            return (FunctionDescriptor) find("nrand48_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle nrand48_r$handle() {
        try {
            return (MethodHandle) find("nrand48_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment nrand48_r$address() {
        try {
            return (MemorySegment) find("nrand48_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int nrand48_r(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) find("nrand48_r", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mrand48_r$descriptor() {
        try {
            return (FunctionDescriptor) find("mrand48_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mrand48_r$handle() {
        try {
            return (MethodHandle) find("mrand48_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mrand48_r$address() {
        try {
            return (MemorySegment) find("mrand48_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mrand48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("mrand48_r", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor jrand48_r$descriptor() {
        try {
            return (FunctionDescriptor) find("jrand48_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle jrand48_r$handle() {
        try {
            return (MethodHandle) find("jrand48_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment jrand48_r$address() {
        try {
            return (MemorySegment) find("jrand48_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int jrand48_r(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) find("jrand48_r", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srand48_r$descriptor() {
        try {
            return (FunctionDescriptor) find("srand48_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srand48_r$handle() {
        try {
            return (MethodHandle) find("srand48_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srand48_r$address() {
        try {
            return (MemorySegment) find("srand48_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int srand48_r(long arg0, MemorySegment arg1) {
        try {
            return (int) find("srand48_r", int.class, long.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor seed48_r$descriptor() {
        try {
            return (FunctionDescriptor) find("seed48_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle seed48_r$handle() {
        try {
            return (MethodHandle) find("seed48_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment seed48_r$address() {
        try {
            return (MemorySegment) find("seed48_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int seed48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("seed48_r", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lcong48_r$descriptor() {
        try {
            return (FunctionDescriptor) find("lcong48_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lcong48_r$handle() {
        try {
            return (MethodHandle) find("lcong48_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lcong48_r$address() {
        try {
            return (MemorySegment) find("lcong48_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int lcong48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("lcong48_r", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor arc4random$descriptor() {
        try {
            return (FunctionDescriptor) find("arc4random$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle arc4random$handle() {
        try {
            return (MethodHandle) find("arc4random$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment arc4random$address() {
        try {
            return (MemorySegment) find("arc4random$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int arc4random() {
        try {
            return (int) find("arc4random", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor arc4random_buf$descriptor() {
        try {
            return (FunctionDescriptor) find("arc4random_buf$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle arc4random_buf$handle() {
        try {
            return (MethodHandle) find("arc4random_buf$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment arc4random_buf$address() {
        try {
            return (MemorySegment) find("arc4random_buf$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void arc4random_buf(MemorySegment arg0, long arg1) {
        try {
            find("arc4random_buf", void.class, MemorySegment.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor arc4random_uniform$descriptor() {
        try {
            return (FunctionDescriptor) find("arc4random_uniform$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle arc4random_uniform$handle() {
        try {
            return (MethodHandle) find("arc4random_uniform$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment arc4random_uniform$address() {
        try {
            return (MemorySegment) find("arc4random_uniform$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int arc4random_uniform(int arg0) {
        try {
            return (int) find("arc4random_uniform", int.class, int.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor malloc$descriptor() {
        try {
            return (FunctionDescriptor) find("malloc$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle malloc$handle() {
        try {
            return (MethodHandle) find("malloc$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment malloc$address() {
        try {
            return (MemorySegment) find("malloc$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment malloc(long arg0) {
        try {
            return (MemorySegment) find("malloc", MemorySegment.class, long.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor calloc$descriptor() {
        try {
            return (FunctionDescriptor) find("calloc$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle calloc$handle() {
        try {
            return (MethodHandle) find("calloc$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment calloc$address() {
        try {
            return (MemorySegment) find("calloc$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment calloc(long arg0, long arg1) {
        try {
            return (MemorySegment) find("calloc", MemorySegment.class, long.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor realloc$descriptor() {
        try {
            return (FunctionDescriptor) find("realloc$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle realloc$handle() {
        try {
            return (MethodHandle) find("realloc$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment realloc$address() {
        try {
            return (MemorySegment) find("realloc$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment realloc(MemorySegment arg0, long arg1) {
        try {
            return (MemorySegment) find("realloc", MemorySegment.class, MemorySegment.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor free$descriptor() {
        try {
            return (FunctionDescriptor) find("free$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle free$handle() {
        try {
            return (MethodHandle) find("free$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment free$address() {
        try {
            return (MemorySegment) find("free$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void free(MemorySegment arg0) {
        try {
            find("free", void.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor reallocarray$descriptor() {
        try {
            return (FunctionDescriptor) find("reallocarray$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle reallocarray$handle() {
        try {
            return (MethodHandle) find("reallocarray$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment reallocarray$address() {
        try {
            return (MemorySegment) find("reallocarray$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment reallocarray(MemorySegment arg0, long arg1, long arg2) {
        try {
            return (MemorySegment) find("reallocarray", MemorySegment.class, MemorySegment.class, long.class, long.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor alloca$descriptor() {
        try {
            return (FunctionDescriptor) find("alloca$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle alloca$handle() {
        try {
            return (MethodHandle) find("alloca$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment alloca$address() {
        try {
            return (MemorySegment) find("alloca$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment alloca(long arg0) {
        try {
            return (MemorySegment) find("alloca", MemorySegment.class, long.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor valloc$descriptor() {
        try {
            return (FunctionDescriptor) find("valloc$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle valloc$handle() {
        try {
            return (MethodHandle) find("valloc$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment valloc$address() {
        try {
            return (MemorySegment) find("valloc$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment valloc(long arg0) {
        try {
            return (MemorySegment) find("valloc", MemorySegment.class, long.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor posix_memalign$descriptor() {
        try {
            return (FunctionDescriptor) find("posix_memalign$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle posix_memalign$handle() {
        try {
            return (MethodHandle) find("posix_memalign$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment posix_memalign$address() {
        try {
            return (MemorySegment) find("posix_memalign$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int posix_memalign(MemorySegment arg0, long arg1, long arg2) {
        try {
            return (int) find("posix_memalign", int.class, MemorySegment.class, long.class, long.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor aligned_alloc$descriptor() {
        try {
            return (FunctionDescriptor) find("aligned_alloc$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle aligned_alloc$handle() {
        try {
            return (MethodHandle) find("aligned_alloc$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment aligned_alloc$address() {
        try {
            return (MemorySegment) find("aligned_alloc$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment aligned_alloc(long arg0, long arg1) {
        try {
            return (MemorySegment) find("aligned_alloc", MemorySegment.class, long.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor abort$descriptor() {
        try {
            return (FunctionDescriptor) find("abort$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle abort$handle() {
        try {
            return (MethodHandle) find("abort$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment abort$address() {
        try {
            return (MemorySegment) find("abort$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void abort() {
        try {
            find("abort", void.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atexit$descriptor() {
        try {
            return (FunctionDescriptor) find("atexit$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atexit$handle() {
        try {
            return (MethodHandle) find("atexit$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atexit$address() {
        try {
            return (MemorySegment) find("atexit$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int atexit(MemorySegment arg0) {
        try {
            return (int) find("atexit", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor at_quick_exit$descriptor() {
        try {
            return (FunctionDescriptor) find("at_quick_exit$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle at_quick_exit$handle() {
        try {
            return (MethodHandle) find("at_quick_exit$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment at_quick_exit$address() {
        try {
            return (MemorySegment) find("at_quick_exit$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int at_quick_exit(MemorySegment arg0) {
        try {
            return (int) find("at_quick_exit", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor on_exit$descriptor() {
        try {
            return (FunctionDescriptor) find("on_exit$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle on_exit$handle() {
        try {
            return (MethodHandle) find("on_exit$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment on_exit$address() {
        try {
            return (MemorySegment) find("on_exit$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int on_exit(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("on_exit", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor exit$descriptor() {
        try {
            return (FunctionDescriptor) find("exit$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle exit$handle() {
        try {
            return (MethodHandle) find("exit$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment exit$address() {
        try {
            return (MemorySegment) find("exit$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void exit(int arg0) {
        try {
            find("exit", void.class, int.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor quick_exit$descriptor() {
        try {
            return (FunctionDescriptor) find("quick_exit$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle quick_exit$handle() {
        try {
            return (MethodHandle) find("quick_exit$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment quick_exit$address() {
        try {
            return (MemorySegment) find("quick_exit$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void quick_exit(int arg0) {
        try {
            find("quick_exit", void.class, int.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor _Exit$descriptor() {
        try {
            return (FunctionDescriptor) find("_Exit$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle _Exit$handle() {
        try {
            return (MethodHandle) find("_Exit$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment _Exit$address() {
        try {
            return (MemorySegment) find("_Exit$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void _Exit(int arg0) {
        try {
            find("_Exit", void.class, int.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor getenv$descriptor() {
        try {
            return (FunctionDescriptor) find("getenv$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle getenv$handle() {
        try {
            return (MethodHandle) find("getenv$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment getenv$address() {
        try {
            return (MemorySegment) find("getenv$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment getenv(MemorySegment arg0) {
        try {
            return (MemorySegment) find("getenv", MemorySegment.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor putenv$descriptor() {
        try {
            return (FunctionDescriptor) find("putenv$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle putenv$handle() {
        try {
            return (MethodHandle) find("putenv$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment putenv$address() {
        try {
            return (MemorySegment) find("putenv$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int putenv(MemorySegment arg0) {
        try {
            return (int) find("putenv", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor setenv$descriptor() {
        try {
            return (FunctionDescriptor) find("setenv$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle setenv$handle() {
        try {
            return (MethodHandle) find("setenv$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment setenv$address() {
        try {
            return (MemorySegment) find("setenv$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int setenv(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (int) find("setenv", int.class, MemorySegment.class, MemorySegment.class, int.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor unsetenv$descriptor() {
        try {
            return (FunctionDescriptor) find("unsetenv$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle unsetenv$handle() {
        try {
            return (MethodHandle) find("unsetenv$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment unsetenv$address() {
        try {
            return (MemorySegment) find("unsetenv$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int unsetenv(MemorySegment arg0) {
        try {
            return (int) find("unsetenv", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor clearenv$descriptor() {
        try {
            return (FunctionDescriptor) find("clearenv$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle clearenv$handle() {
        try {
            return (MethodHandle) find("clearenv$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment clearenv$address() {
        try {
            return (MemorySegment) find("clearenv$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int clearenv() {
        try {
            return (int) find("clearenv", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mktemp$descriptor() {
        try {
            return (FunctionDescriptor) find("mktemp$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mktemp$handle() {
        try {
            return (MethodHandle) find("mktemp$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mktemp$address() {
        try {
            return (MemorySegment) find("mktemp$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mktemp(MemorySegment arg0) {
        try {
            return (MemorySegment) find("mktemp", MemorySegment.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mkstemp$descriptor() {
        try {
            return (FunctionDescriptor) find("mkstemp$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mkstemp$handle() {
        try {
            return (MethodHandle) find("mkstemp$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mkstemp$address() {
        try {
            return (MemorySegment) find("mkstemp$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mkstemp(MemorySegment arg0) {
        try {
            return (int) find("mkstemp", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mkstemps$descriptor() {
        try {
            return (FunctionDescriptor) find("mkstemps$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mkstemps$handle() {
        try {
            return (MethodHandle) find("mkstemps$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mkstemps$address() {
        try {
            return (MemorySegment) find("mkstemps$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mkstemps(MemorySegment arg0, int arg1) {
        try {
            return (int) find("mkstemps", int.class, MemorySegment.class, int.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mkdtemp$descriptor() {
        try {
            return (FunctionDescriptor) find("mkdtemp$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mkdtemp$handle() {
        try {
            return (MethodHandle) find("mkdtemp$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mkdtemp$address() {
        try {
            return (MemorySegment) find("mkdtemp$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mkdtemp(MemorySegment arg0) {
        try {
            return (MemorySegment) find("mkdtemp", MemorySegment.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor system$descriptor() {
        try {
            return (FunctionDescriptor) find("system$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle system$handle() {
        try {
            return (MethodHandle) find("system$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment system$address() {
        try {
            return (MemorySegment) find("system$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int system(MemorySegment arg0) {
        try {
            return (int) find("system", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor realpath$descriptor() {
        try {
            return (FunctionDescriptor) find("realpath$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle realpath$handle() {
        try {
            return (MethodHandle) find("realpath$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment realpath$address() {
        try {
            return (MemorySegment) find("realpath$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment realpath(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (MemorySegment) find("realpath", MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor bsearch$descriptor() {
        try {
            return (FunctionDescriptor) find("bsearch$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle bsearch$handle() {
        try {
            return (MethodHandle) find("bsearch$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment bsearch$address() {
        try {
            return (MemorySegment) find("bsearch$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment bsearch(MemorySegment arg0, MemorySegment arg1, long arg2, long arg3, MemorySegment arg4) {
        try {
            return (MemorySegment) find("bsearch", MemorySegment.class, MemorySegment.class, MemorySegment.class, long.class, long.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor qsort$descriptor() {
        try {
            return (FunctionDescriptor) find("qsort$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle qsort$handle() {
        try {
            return (MethodHandle) find("qsort$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment qsort$address() {
        try {
            return (MemorySegment) find("qsort$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void qsort(MemorySegment arg0, long arg1, long arg2, MemorySegment arg3) {
        try {
            find("qsort", void.class, MemorySegment.class, long.class, long.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor abs$descriptor() {
        try {
            return (FunctionDescriptor) find("abs$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle abs$handle() {
        try {
            return (MethodHandle) find("abs$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment abs$address() {
        try {
            return (MemorySegment) find("abs$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int abs(int arg0) {
        try {
            return (int) find("abs", int.class, int.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor labs$descriptor() {
        try {
            return (FunctionDescriptor) find("labs$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle labs$handle() {
        try {
            return (MethodHandle) find("labs$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment labs$address() {
        try {
            return (MemorySegment) find("labs$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long labs(long arg0) {
        try {
            return (long) find("labs", long.class, long.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor llabs$descriptor() {
        try {
            return (FunctionDescriptor) find("llabs$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle llabs$handle() {
        try {
            return (MethodHandle) find("llabs$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment llabs$address() {
        try {
            return (MemorySegment) find("llabs$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long llabs(long arg0) {
        try {
            return (long) find("llabs", long.class, long.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor div$descriptor() {
        try {
            return (FunctionDescriptor) find("div$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle div$handle() {
        try {
            return (MethodHandle) find("div$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment div$address() {
        try {
            return (MemorySegment) find("div$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment div(SegmentAllocator arg0, int arg1, int arg2) {
        try {
            return (MemorySegment) find("div", MemorySegment.class, SegmentAllocator.class, int.class, int.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor ldiv$descriptor() {
        try {
            return (FunctionDescriptor) find("ldiv$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle ldiv$handle() {
        try {
            return (MethodHandle) find("ldiv$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ldiv$address() {
        try {
            return (MemorySegment) find("ldiv$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ldiv(SegmentAllocator arg0, long arg1, long arg2) {
        try {
            return (MemorySegment) find("ldiv", MemorySegment.class, SegmentAllocator.class, long.class, long.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lldiv$descriptor() {
        try {
            return (FunctionDescriptor) find("lldiv$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lldiv$handle() {
        try {
            return (MethodHandle) find("lldiv$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lldiv$address() {
        try {
            return (MemorySegment) find("lldiv$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lldiv(SegmentAllocator arg0, long arg1, long arg2) {
        try {
            return (MemorySegment) find("lldiv", MemorySegment.class, SegmentAllocator.class, long.class, long.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor ecvt$descriptor() {
        try {
            return (FunctionDescriptor) find("ecvt$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle ecvt$handle() {
        try {
            return (MethodHandle) find("ecvt$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ecvt$address() {
        try {
            return (MemorySegment) find("ecvt$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ecvt(double arg0, int arg1, MemorySegment arg2, MemorySegment arg3) {
        try {
            return (MemorySegment) find("ecvt", MemorySegment.class, double.class, int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor fcvt$descriptor() {
        try {
            return (FunctionDescriptor) find("fcvt$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle fcvt$handle() {
        try {
            return (MethodHandle) find("fcvt$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment fcvt$address() {
        try {
            return (MemorySegment) find("fcvt$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment fcvt(double arg0, int arg1, MemorySegment arg2, MemorySegment arg3) {
        try {
            return (MemorySegment) find("fcvt", MemorySegment.class, double.class, int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor gcvt$descriptor() {
        try {
            return (FunctionDescriptor) find("gcvt$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle gcvt$handle() {
        try {
            return (MethodHandle) find("gcvt$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment gcvt$address() {
        try {
            return (MemorySegment) find("gcvt$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment gcvt(double arg0, int arg1, MemorySegment arg2) {
        try {
            return (MemorySegment) find("gcvt", MemorySegment.class, double.class, int.class, MemorySegment.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor ecvt_r$descriptor() {
        try {
            return (FunctionDescriptor) find("ecvt_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle ecvt_r$handle() {
        try {
            return (MethodHandle) find("ecvt_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ecvt_r$address() {
        try {
            return (MemorySegment) find("ecvt_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int ecvt_r(double arg0, int arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4, long arg5) {
        try {
            return (int) find("ecvt_r", int.class, double.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, long.class).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor fcvt_r$descriptor() {
        try {
            return (FunctionDescriptor) find("fcvt_r$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle fcvt_r$handle() {
        try {
            return (MethodHandle) find("fcvt_r$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment fcvt_r$address() {
        try {
            return (MemorySegment) find("fcvt_r$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int fcvt_r(double arg0, int arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4, long arg5) {
        try {
            return (int) find("fcvt_r", int.class, double.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, long.class).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mblen$descriptor() {
        try {
            return (FunctionDescriptor) find("mblen$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mblen$handle() {
        try {
            return (MethodHandle) find("mblen$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mblen$address() {
        try {
            return (MemorySegment) find("mblen$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mblen(MemorySegment arg0, long arg1) {
        try {
            return (int) find("mblen", int.class, MemorySegment.class, long.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mbtowc$descriptor() {
        try {
            return (FunctionDescriptor) find("mbtowc$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mbtowc$handle() {
        try {
            return (MethodHandle) find("mbtowc$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mbtowc$address() {
        try {
            return (MemorySegment) find("mbtowc$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mbtowc(MemorySegment arg0, MemorySegment arg1, long arg2) {
        try {
            return (int) find("mbtowc", int.class, MemorySegment.class, MemorySegment.class, long.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor wctomb$descriptor() {
        try {
            return (FunctionDescriptor) find("wctomb$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle wctomb$handle() {
        try {
            return (MethodHandle) find("wctomb$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment wctomb$address() {
        try {
            return (MemorySegment) find("wctomb$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int wctomb(MemorySegment arg0, int arg1) {
        try {
            return (int) find("wctomb", int.class, MemorySegment.class, int.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mbstowcs$descriptor() {
        try {
            return (FunctionDescriptor) find("mbstowcs$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mbstowcs$handle() {
        try {
            return (MethodHandle) find("mbstowcs$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mbstowcs$address() {
        try {
            return (MemorySegment) find("mbstowcs$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long mbstowcs(MemorySegment arg0, MemorySegment arg1, long arg2) {
        try {
            return (long) find("mbstowcs", long.class, MemorySegment.class, MemorySegment.class, long.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor wcstombs$descriptor() {
        try {
            return (FunctionDescriptor) find("wcstombs$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle wcstombs$handle() {
        try {
            return (MethodHandle) find("wcstombs$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment wcstombs$address() {
        try {
            return (MemorySegment) find("wcstombs$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long wcstombs(MemorySegment arg0, MemorySegment arg1, long arg2) {
        try {
            return (long) find("wcstombs", long.class, MemorySegment.class, MemorySegment.class, long.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor rpmatch$descriptor() {
        try {
            return (FunctionDescriptor) find("rpmatch$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle rpmatch$handle() {
        try {
            return (MethodHandle) find("rpmatch$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment rpmatch$address() {
        try {
            return (MemorySegment) find("rpmatch$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int rpmatch(MemorySegment arg0) {
        try {
            return (int) find("rpmatch", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor getsubopt$descriptor() {
        try {
            return (FunctionDescriptor) find("getsubopt$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle getsubopt$handle() {
        try {
            return (MethodHandle) find("getsubopt$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment getsubopt$address() {
        try {
            return (MemorySegment) find("getsubopt$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int getsubopt(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) find("getsubopt", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor getloadavg$descriptor() {
        try {
            return (FunctionDescriptor) find("getloadavg$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle getloadavg$handle() {
        try {
            return (MethodHandle) find("getloadavg$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment getloadavg$address() {
        try {
            return (MemorySegment) find("getloadavg$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int getloadavg(MemorySegment arg0, int arg1) {
        try {
            return (int) find("getloadavg", int.class, MemorySegment.class, int.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_free_database$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_free_database$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_free_database$handle() {
        try {
            return (MethodHandle) find("hs_free_database$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_free_database$address() {
        try {
            return (MemorySegment) find("hs_free_database$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_free_database(MemorySegment arg0) {
        try {
            return (int) find("hs_free_database", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_serialize_database$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_serialize_database$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_serialize_database$handle() {
        try {
            return (MethodHandle) find("hs_serialize_database$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_serialize_database$address() {
        try {
            return (MemorySegment) find("hs_serialize_database$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_serialize_database(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) find("hs_serialize_database", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_deserialize_database$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_deserialize_database$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_deserialize_database$handle() {
        try {
            return (MethodHandle) find("hs_deserialize_database$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_deserialize_database$address() {
        try {
            return (MemorySegment) find("hs_deserialize_database$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_deserialize_database(MemorySegment arg0, long arg1, MemorySegment arg2) {
        try {
            return (int) find("hs_deserialize_database", int.class, MemorySegment.class, long.class, MemorySegment.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_deserialize_database_at$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_deserialize_database_at$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_deserialize_database_at$handle() {
        try {
            return (MethodHandle) find("hs_deserialize_database_at$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_deserialize_database_at$address() {
        try {
            return (MemorySegment) find("hs_deserialize_database_at$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_deserialize_database_at(MemorySegment arg0, long arg1, MemorySegment arg2) {
        try {
            return (int) find("hs_deserialize_database_at", int.class, MemorySegment.class, long.class, MemorySegment.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_stream_size$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_stream_size$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_stream_size$handle() {
        try {
            return (MethodHandle) find("hs_stream_size$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_stream_size$address() {
        try {
            return (MemorySegment) find("hs_stream_size$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_stream_size(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("hs_stream_size", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_database_size$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_database_size$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_database_size$handle() {
        try {
            return (MethodHandle) find("hs_database_size$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_database_size$address() {
        try {
            return (MemorySegment) find("hs_database_size$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_database_size(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("hs_database_size", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_serialized_database_size$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_serialized_database_size$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_serialized_database_size$handle() {
        try {
            return (MethodHandle) find("hs_serialized_database_size$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_serialized_database_size$address() {
        try {
            return (MemorySegment) find("hs_serialized_database_size$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_serialized_database_size(MemorySegment arg0, long arg1, MemorySegment arg2) {
        try {
            return (int) find("hs_serialized_database_size", int.class, MemorySegment.class, long.class, MemorySegment.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_database_info$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_database_info$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_database_info$handle() {
        try {
            return (MethodHandle) find("hs_database_info$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_database_info$address() {
        try {
            return (MemorySegment) find("hs_database_info$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_database_info(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("hs_database_info", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_serialized_database_info$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_serialized_database_info$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_serialized_database_info$handle() {
        try {
            return (MethodHandle) find("hs_serialized_database_info$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_serialized_database_info$address() {
        try {
            return (MemorySegment) find("hs_serialized_database_info$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_serialized_database_info(MemorySegment arg0, long arg1, MemorySegment arg2) {
        try {
            return (int) find("hs_serialized_database_info", int.class, MemorySegment.class, long.class, MemorySegment.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_allocator$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_set_allocator$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_allocator$handle() {
        try {
            return (MethodHandle) find("hs_set_allocator$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_allocator$address() {
        try {
            return (MemorySegment) find("hs_set_allocator$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("hs_set_allocator", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_database_allocator$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_set_database_allocator$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_database_allocator$handle() {
        try {
            return (MethodHandle) find("hs_set_database_allocator$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_database_allocator$address() {
        try {
            return (MemorySegment) find("hs_set_database_allocator$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_database_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("hs_set_database_allocator", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_misc_allocator$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_set_misc_allocator$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_misc_allocator$handle() {
        try {
            return (MethodHandle) find("hs_set_misc_allocator$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_misc_allocator$address() {
        try {
            return (MemorySegment) find("hs_set_misc_allocator$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_misc_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("hs_set_misc_allocator", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_scratch_allocator$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_set_scratch_allocator$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_scratch_allocator$handle() {
        try {
            return (MethodHandle) find("hs_set_scratch_allocator$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_scratch_allocator$address() {
        try {
            return (MemorySegment) find("hs_set_scratch_allocator$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_scratch_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("hs_set_scratch_allocator", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_stream_allocator$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_set_stream_allocator$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_stream_allocator$handle() {
        try {
            return (MethodHandle) find("hs_set_stream_allocator$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_stream_allocator$address() {
        try {
            return (MemorySegment) find("hs_set_stream_allocator$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_stream_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("hs_set_stream_allocator", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_version$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_version$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_version$handle() {
        try {
            return (MethodHandle) find("hs_version$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_version$address() {
        try {
            return (MemorySegment) find("hs_version$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_version() {
        try {
            return (MemorySegment) find("hs_version", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_valid_platform$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_valid_platform$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_valid_platform$handle() {
        try {
            return (MethodHandle) find("hs_valid_platform$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_valid_platform$address() {
        try {
            return (MemorySegment) find("hs_valid_platform$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_valid_platform() {
        try {
            return (int) find("hs_valid_platform", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_compile$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile$handle() {
        try {
            return (MethodHandle) find("hs_compile$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile$address() {
        try {
            return (MemorySegment) find("hs_compile$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile(MemorySegment arg0, int arg1, int arg2, MemorySegment arg3, MemorySegment arg4, MemorySegment arg5) {
        try {
            return (int) find("hs_compile", int.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile_multi$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_compile_multi$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile_multi$handle() {
        try {
            return (MethodHandle) find("hs_compile_multi$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile_multi$address() {
        try {
            return (MemorySegment) find("hs_compile_multi$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile_multi(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, int arg3, int arg4, MemorySegment arg5, MemorySegment arg6, MemorySegment arg7) {
        try {
            return (int) find("hs_compile_multi", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile_ext_multi$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_compile_ext_multi$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile_ext_multi$handle() {
        try {
            return (MethodHandle) find("hs_compile_ext_multi$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile_ext_multi$address() {
        try {
            return (MemorySegment) find("hs_compile_ext_multi$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile_ext_multi(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, int arg4, int arg5, MemorySegment arg6, MemorySegment arg7, MemorySegment arg8) {
        try {
            return (int) find("hs_compile_ext_multi", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile_lit$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_compile_lit$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile_lit$handle() {
        try {
            return (MethodHandle) find("hs_compile_lit$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile_lit$address() {
        try {
            return (MemorySegment) find("hs_compile_lit$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile_lit(MemorySegment arg0, int arg1, long arg2, int arg3, MemorySegment arg4, MemorySegment arg5, MemorySegment arg6) {
        try {
            return (int) find("hs_compile_lit", int.class, MemorySegment.class, int.class, long.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile_lit_multi$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_compile_lit_multi$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile_lit_multi$handle() {
        try {
            return (MethodHandle) find("hs_compile_lit_multi$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile_lit_multi$address() {
        try {
            return (MemorySegment) find("hs_compile_lit_multi$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile_lit_multi(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, int arg4, int arg5, MemorySegment arg6, MemorySegment arg7, MemorySegment arg8) {
        try {
            return (int) find("hs_compile_lit_multi", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_free_compile_error$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_free_compile_error$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_free_compile_error$handle() {
        try {
            return (MethodHandle) find("hs_free_compile_error$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_free_compile_error$address() {
        try {
            return (MemorySegment) find("hs_free_compile_error$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_free_compile_error(MemorySegment arg0) {
        try {
            return (int) find("hs_free_compile_error", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_expression_info$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_expression_info$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_expression_info$handle() {
        try {
            return (MethodHandle) find("hs_expression_info$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_expression_info$address() {
        try {
            return (MemorySegment) find("hs_expression_info$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_expression_info(MemorySegment arg0, int arg1, MemorySegment arg2, MemorySegment arg3) {
        try {
            return (int) find("hs_expression_info", int.class, MemorySegment.class, int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_expression_ext_info$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_expression_ext_info$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_expression_ext_info$handle() {
        try {
            return (MethodHandle) find("hs_expression_ext_info$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_expression_ext_info$address() {
        try {
            return (MemorySegment) find("hs_expression_ext_info$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_expression_ext_info(MemorySegment arg0, int arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4) {
        try {
            return (int) find("hs_expression_ext_info", int.class, MemorySegment.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_populate_platform$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_populate_platform$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_populate_platform$handle() {
        try {
            return (MethodHandle) find("hs_populate_platform$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_populate_platform$address() {
        try {
            return (MemorySegment) find("hs_populate_platform$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_populate_platform(MemorySegment arg0) {
        try {
            return (int) find("hs_populate_platform", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_open_stream$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_open_stream$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_open_stream$handle() {
        try {
            return (MethodHandle) find("hs_open_stream$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_open_stream$address() {
        try {
            return (MemorySegment) find("hs_open_stream$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_open_stream(MemorySegment arg0, int arg1, MemorySegment arg2) {
        try {
            return (int) find("hs_open_stream", int.class, MemorySegment.class, int.class, MemorySegment.class).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_scan_stream$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_scan_stream$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_scan_stream$handle() {
        try {
            return (MethodHandle) find("hs_scan_stream$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_scan_stream$address() {
        try {
            return (MemorySegment) find("hs_scan_stream$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_scan_stream(MemorySegment arg0, MemorySegment arg1, int arg2, int arg3, MemorySegment arg4, MemorySegment arg5, MemorySegment arg6) {
        try {
            return (int) find("hs_scan_stream", int.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_close_stream$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_close_stream$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_close_stream$handle() {
        try {
            return (MethodHandle) find("hs_close_stream$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_close_stream$address() {
        try {
            return (MemorySegment) find("hs_close_stream$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_close_stream(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3) {
        try {
            return (int) find("hs_close_stream", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_reset_stream$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_reset_stream$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_reset_stream$handle() {
        try {
            return (MethodHandle) find("hs_reset_stream$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_reset_stream$address() {
        try {
            return (MemorySegment) find("hs_reset_stream$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_reset_stream(MemorySegment arg0, int arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4) {
        try {
            return (int) find("hs_reset_stream", int.class, MemorySegment.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_copy_stream$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_copy_stream$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_copy_stream$handle() {
        try {
            return (MethodHandle) find("hs_copy_stream$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_copy_stream$address() {
        try {
            return (MemorySegment) find("hs_copy_stream$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_copy_stream(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("hs_copy_stream", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_reset_and_copy_stream$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_reset_and_copy_stream$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_reset_and_copy_stream$handle() {
        try {
            return (MethodHandle) find("hs_reset_and_copy_stream$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_reset_and_copy_stream$address() {
        try {
            return (MemorySegment) find("hs_reset_and_copy_stream$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_reset_and_copy_stream(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4) {
        try {
            return (int) find("hs_reset_and_copy_stream", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compress_stream$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_compress_stream$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compress_stream$handle() {
        try {
            return (MethodHandle) find("hs_compress_stream$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compress_stream$address() {
        try {
            return (MemorySegment) find("hs_compress_stream$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compress_stream(MemorySegment arg0, MemorySegment arg1, long arg2, MemorySegment arg3) {
        try {
            return (int) find("hs_compress_stream", int.class, MemorySegment.class, MemorySegment.class, long.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_expand_stream$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_expand_stream$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_expand_stream$handle() {
        try {
            return (MethodHandle) find("hs_expand_stream$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_expand_stream$address() {
        try {
            return (MemorySegment) find("hs_expand_stream$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_expand_stream(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, long arg3) {
        try {
            return (int) find("hs_expand_stream", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, long.class).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_reset_and_expand_stream$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_reset_and_expand_stream$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_reset_and_expand_stream$handle() {
        try {
            return (MethodHandle) find("hs_reset_and_expand_stream$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_reset_and_expand_stream$address() {
        try {
            return (MemorySegment) find("hs_reset_and_expand_stream$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_reset_and_expand_stream(MemorySegment arg0, MemorySegment arg1, long arg2, MemorySegment arg3, MemorySegment arg4, MemorySegment arg5) {
        try {
            return (int) find("hs_reset_and_expand_stream", int.class, MemorySegment.class, MemorySegment.class, long.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_scan$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_scan$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_scan$handle() {
        try {
            return (MethodHandle) find("hs_scan$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_scan$address() {
        try {
            return (MemorySegment) find("hs_scan$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_scan(MemorySegment arg0, MemorySegment arg1, int arg2, int arg3, MemorySegment arg4, MemorySegment arg5, MemorySegment arg6) {
        try {
            return (int) find("hs_scan", int.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_scan_vector$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_scan_vector$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_scan_vector$handle() {
        try {
            return (MethodHandle) find("hs_scan_vector$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_scan_vector$address() {
        try {
            return (MemorySegment) find("hs_scan_vector$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_scan_vector(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, int arg3, int arg4, MemorySegment arg5, MemorySegment arg6, MemorySegment arg7) {
        try {
            return (int) find("hs_scan_vector", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_alloc_scratch$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_alloc_scratch$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_alloc_scratch$handle() {
        try {
            return (MethodHandle) find("hs_alloc_scratch$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_alloc_scratch$address() {
        try {
            return (MemorySegment) find("hs_alloc_scratch$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_alloc_scratch(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("hs_alloc_scratch", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_clone_scratch$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_clone_scratch$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_clone_scratch$handle() {
        try {
            return (MethodHandle) find("hs_clone_scratch$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_clone_scratch$address() {
        try {
            return (MemorySegment) find("hs_clone_scratch$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_clone_scratch(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("hs_clone_scratch", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_scratch_size$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_scratch_size$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_scratch_size$handle() {
        try {
            return (MethodHandle) find("hs_scratch_size$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_scratch_size$address() {
        try {
            return (MemorySegment) find("hs_scratch_size$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_scratch_size(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) find("hs_scratch_size", int.class, MemorySegment.class, MemorySegment.class).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_free_scratch$descriptor() {
        try {
            return (FunctionDescriptor) find("hs_free_scratch$descriptor", FunctionDescriptor.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_free_scratch$handle() {
        try {
            return (MethodHandle) find("hs_free_scratch$handle", MethodHandle.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_free_scratch$address() {
        try {
            return (MemorySegment) find("hs_free_scratch$address", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_free_scratch(MemorySegment arg0) {
        try {
            return (int) find("hs_free_scratch", int.class, MemorySegment.class).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long _POSIX_C_SOURCE() {
        try {
            return (long) find("_POSIX_C_SOURCE", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __TIMESIZE() {
        try {
            return (int) find("__TIMESIZE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long __STDC_IEC_60559_BFP__() {
        try {
            return (long) find("__STDC_IEC_60559_BFP__", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long __STDC_IEC_60559_COMPLEX__() {
        try {
            return (long) find("__STDC_IEC_60559_COMPLEX__", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long __STDC_ISO_10646__() {
        try {
            return (long) find("__STDC_ISO_10646__", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment NULL() {
        try {
            return (MemorySegment) find("NULL", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WCLONE() {
        try {
            return (int) find("__WCLONE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT16() {
        try {
            return (int) find("__HAVE_DISTINCT_FLOAT16", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT128X() {
        try {
            return (int) find("__HAVE_DISTINCT_FLOAT128X", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT128_UNLIKE_LDBL() {
        try {
            return (int) find("__HAVE_FLOAT128_UNLIKE_LDBL", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __BYTE_ORDER() {
        try {
            return (int) find("__BYTE_ORDER", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __FLOAT_WORD_ORDER() {
        try {
            return (int) find("__FLOAT_WORD_ORDER", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int LITTLE_ENDIAN() {
        try {
            return (int) find("LITTLE_ENDIAN", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int BIG_ENDIAN() {
        try {
            return (int) find("BIG_ENDIAN", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int PDP_ENDIAN() {
        try {
            return (int) find("PDP_ENDIAN", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int BYTE_ORDER() {
        try {
            return (int) find("BYTE_ORDER", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long _SIGSET_NWORDS() {
        try {
            return (long) find("_SIGSET_NWORDS", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __NFDBITS() {
        try {
            return (int) find("__NFDBITS", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int FD_SETSIZE() {
        try {
            return (int) find("FD_SETSIZE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int NFDBITS() {
        try {
            return (int) find("NFDBITS", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __PTHREAD_RWLOCK_ELISION_EXTRA() {
        try {
            return (int) find("__PTHREAD_RWLOCK_ELISION_EXTRA", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_INVALID() {
        try {
            return (int) find("HS_INVALID", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_NOMEM() {
        try {
            return (int) find("HS_NOMEM", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_SCAN_TERMINATED() {
        try {
            return (int) find("HS_SCAN_TERMINATED", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_COMPILER_ERROR() {
        try {
            return (int) find("HS_COMPILER_ERROR", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_DB_VERSION_ERROR() {
        try {
            return (int) find("HS_DB_VERSION_ERROR", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_DB_PLATFORM_ERROR() {
        try {
            return (int) find("HS_DB_PLATFORM_ERROR", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_DB_MODE_ERROR() {
        try {
            return (int) find("HS_DB_MODE_ERROR", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_BAD_ALIGN() {
        try {
            return (int) find("HS_BAD_ALIGN", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_BAD_ALLOC() {
        try {
            return (int) find("HS_BAD_ALLOC", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_SCRATCH_IN_USE() {
        try {
            return (int) find("HS_SCRATCH_IN_USE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_ARCH_ERROR() {
        try {
            return (int) find("HS_ARCH_ERROR", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_INSUFFICIENT_SPACE() {
        try {
            return (int) find("HS_INSUFFICIENT_SPACE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_UNKNOWN_ERROR() {
        try {
            return (int) find("HS_UNKNOWN_ERROR", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_MIN_OFFSET() {
        try {
            return (long) find("HS_EXT_FLAG_MIN_OFFSET", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_MAX_OFFSET() {
        try {
            return (long) find("HS_EXT_FLAG_MAX_OFFSET", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_MIN_LENGTH() {
        try {
            return (long) find("HS_EXT_FLAG_MIN_LENGTH", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_EDIT_DISTANCE() {
        try {
            return (long) find("HS_EXT_FLAG_EDIT_DISTANCE", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_HAMMING_DISTANCE() {
        try {
            return (long) find("HS_EXT_FLAG_HAMMING_DISTANCE", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_CPU_FEATURES_AVX2() {
        try {
            return (long) find("HS_CPU_FEATURES_AVX2", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_CPU_FEATURES_AVX512() {
        try {
            return (long) find("HS_CPU_FEATURES_AVX512", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_CPU_FEATURES_AVX512VBMI() {
        try {
            return (long) find("HS_CPU_FEATURES_AVX512VBMI", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_SOM_HORIZON_LARGE() {
        try {
            return (int) find("HS_MODE_SOM_HORIZON_LARGE", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_SOM_HORIZON_MEDIUM() {
        try {
            return (int) find("HS_MODE_SOM_HORIZON_MEDIUM", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_SOM_HORIZON_SMALL() {
        try {
            return (int) find("HS_MODE_SOM_HORIZON_SMALL", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_OFFSET_PAST_HORIZON() {
        try {
            return (long) find("HS_OFFSET_PAST_HORIZON", long.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment HS_VERSION_STRING() {
        try {
            return (MemorySegment) find("HS_VERSION_STRING", MemorySegment.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_VERSION_32BIT() {
        try {
            return (int) find("HS_VERSION_32BIT", int.class).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}