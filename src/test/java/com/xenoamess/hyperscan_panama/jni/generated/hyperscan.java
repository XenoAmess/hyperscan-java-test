package com.xenoamess.hyperscan_panama.jni.generated;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/** Optimized compatibility facade for hyperscan. */
public class hyperscan {

    private static final Class<?> DELEGATE;
    private static final String PLATFORM_FAMILY;
    public static final java.lang.foreign.SymbolLookup SYMBOL_LOOKUP;
    public static final java.lang.foreign.Arena LIBRARY_ARENA;

    private static final MethodHandle MH_FEATURES_H;
    private static final MethodHandle MH_DEFAULT_SOURCE;
    private static final MethodHandle MH_GLIBC_USE_ISOC2X;
    private static final MethodHandle MH_USE_ISOC11;
    private static final MethodHandle MH_USE_ISOC99;
    private static final MethodHandle MH_USE_ISOC95;
    private static final MethodHandle MH_USE_POSIX_IMPLICITLY;
    private static final MethodHandle MH_POSIX_SOURCE;
    private static final MethodHandle MH_USE_POSIX;
    private static final MethodHandle MH_USE_POSIX2;
    private static final MethodHandle MH_USE_POSIX199309;
    private static final MethodHandle MH_USE_POSIX199506;
    private static final MethodHandle MH_USE_XOPEN2K;
    private static final MethodHandle MH_USE_XOPEN2K8;
    private static final MethodHandle MH_ATFILE_SOURCE;
    private static final MethodHandle MH_WORDSIZE;
    private static final MethodHandle MH_WORDSIZE_TIME64_COMPAT32;
    private static final MethodHandle MH_SYSCALL_WORDSIZE;
    private static final MethodHandle MH_USE_MISC;
    private static final MethodHandle MH_USE_ATFILE;
    private static final MethodHandle MH_USE_FORTIFY_LEVEL;
    private static final MethodHandle MH_GLIBC_USE_DEPRECATED_GETS;
    private static final MethodHandle MH_GLIBC_USE_DEPRECATED_SCANF;
    private static final MethodHandle MH_GLIBC_USE_C2X_STRTOL;
    private static final MethodHandle MH_STDC_PREDEF_H;
    private static final MethodHandle MH_STDC_IEC_559;
    private static final MethodHandle MH_STDC_IEC_559_COMPLEX;
    private static final MethodHandle MH_GNU_LIBRARY;
    private static final MethodHandle MH_GLIBC;
    private static final MethodHandle MH_GLIBC_MINOR;
    private static final MethodHandle MH_SYS_CDEFS_H;
    private static final MethodHandle MH_GLIBC_C99_FLEXARR_AVAILABLE;
    private static final MethodHandle MH_LDOUBLE_REDIRECTS_TO_FLOAT128_ABI;
    private static final MethodHandle MH_HAVE_GENERIC_SELECTION;
    private static final MethodHandle MH_GLIBC_USE_LIB_EXT2;
    private static final MethodHandle MH_GLIBC_USE_IEC_60559_BFP_EXT;
    private static final MethodHandle MH_GLIBC_USE_IEC_60559_BFP_EXT_C2X;
    private static final MethodHandle MH_GLIBC_USE_IEC_60559_EXT;
    private static final MethodHandle MH_GLIBC_USE_IEC_60559_FUNCS_EXT;
    private static final MethodHandle MH_GLIBC_USE_IEC_60559_FUNCS_EXT_C2X;
    private static final MethodHandle MH_GLIBC_USE_IEC_60559_TYPES_EXT;
    private static final MethodHandle MH_STDLIB_H;
    private static final MethodHandle MH_WNOHANG;
    private static final MethodHandle MH_WUNTRACED;
    private static final MethodHandle MH_WSTOPPED;
    private static final MethodHandle MH_WEXITED;
    private static final MethodHandle MH_WCONTINUED;
    private static final MethodHandle MH_WNOWAIT;
    private static final MethodHandle MH_WNOTHREAD;
    private static final MethodHandle MH_WALL;
    private static final MethodHandle MH_W_CONTINUED;
    private static final MethodHandle MH_WCOREFLAG;
    private static final MethodHandle MH_HAVE_FLOAT128;
    private static final MethodHandle MH_HAVE_DISTINCT_FLOAT128;
    private static final MethodHandle MH_HAVE_FLOAT64X;
    private static final MethodHandle MH_HAVE_FLOAT64X_LONG_DOUBLE;
    private static final MethodHandle MH_HAVE_FLOAT16;
    private static final MethodHandle MH_HAVE_FLOAT32;
    private static final MethodHandle MH_HAVE_FLOAT64;
    private static final MethodHandle MH_HAVE_FLOAT32X;
    private static final MethodHandle MH_HAVE_FLOAT128X;
    private static final MethodHandle MH_HAVE_DISTINCT_FLOAT32;
    private static final MethodHandle MH_HAVE_DISTINCT_FLOAT64;
    private static final MethodHandle MH_HAVE_DISTINCT_FLOAT32X;
    private static final MethodHandle MH_HAVE_DISTINCT_FLOAT64X;
    private static final MethodHandle MH_HAVE_FLOATN_NOT_TYPEDEF;
    private static final MethodHandle MH_LDIV_T_DEFINED;
    private static final MethodHandle MH_LLDIV_T_DEFINED;
    private static final MethodHandle MH_RAND_MAX;
    private static final MethodHandle MH_EXIT_FAILURE;
    private static final MethodHandle MH_EXIT_SUCCESS;
    private static final MethodHandle MH_SYS_TYPES_H;
    private static final MethodHandle MH_BITS_TYPES_H;
    private static final MethodHandle MH_BITS_TYPESIZES_H;
    private static final MethodHandle MH_OFF_T_MATCHES_OFF64_T;
    private static final MethodHandle MH_INO_T_MATCHES_INO64_T;
    private static final MethodHandle MH_RLIM_T_MATCHES_RLIM64_T;
    private static final MethodHandle MH_STATFS_MATCHES_STATFS64;
    private static final MethodHandle MH_KERNEL_OLD_TIMEVAL_MATCHES_TIMEVAL64;
    private static final MethodHandle MH_FD_SETSIZE;
    private static final MethodHandle MH_BITS_TIME64_H;
    private static final MethodHandle MH_CLOCK_T_DEFINED;
    private static final MethodHandle MH_CLOCKID_T_DEFINED;
    private static final MethodHandle MH_TIME_T_DEFINED;
    private static final MethodHandle MH_TIMER_T_DEFINED;
    private static final MethodHandle MH_BITS_STDINT_INTN_H;
    private static final MethodHandle MH_BIT_TYPES_DEFINED;
    private static final MethodHandle MH_ENDIAN_H;
    private static final MethodHandle MH_BITS_ENDIAN_H;
    private static final MethodHandle MH_LITTLE_ENDIAN;
    private static final MethodHandle MH_BIG_ENDIAN;
    private static final MethodHandle MH_PDP_ENDIAN;
    private static final MethodHandle MH_BITS_ENDIANNESS_H;
    private static final MethodHandle MH_BITS_BYTESWAP_H;
    private static final MethodHandle MH_BITS_UINTN_IDENTITY_H;
    private static final MethodHandle MH_SYS_SELECT_H;
    private static final MethodHandle MH_SIGSET_T_DEFINED;
    private static final MethodHandle MH_TIMEVAL_DEFINED;
    private static final MethodHandle MH_STRUCT_TIMESPEC;
    private static final MethodHandle MH_BITS_PTHREADTYPES_COMMON_H;
    private static final MethodHandle MH_THREAD_SHARED_TYPES_H;
    private static final MethodHandle MH_BITS_PTHREADTYPES_ARCH_H;
    private static final MethodHandle MH_SIZEOF_PTHREAD_MUTEX_T;
    private static final MethodHandle MH_SIZEOF_PTHREAD_ATTR_T;
    private static final MethodHandle MH_SIZEOF_PTHREAD_RWLOCK_T;
    private static final MethodHandle MH_SIZEOF_PTHREAD_BARRIER_T;
    private static final MethodHandle MH_SIZEOF_PTHREAD_MUTEXATTR_T;
    private static final MethodHandle MH_SIZEOF_PTHREAD_COND_T;
    private static final MethodHandle MH_SIZEOF_PTHREAD_CONDATTR_T;
    private static final MethodHandle MH_SIZEOF_PTHREAD_RWLOCKATTR_T;
    private static final MethodHandle MH_SIZEOF_PTHREAD_BARRIERATTR_T;
    private static final MethodHandle MH_THREAD_MUTEX_INTERNAL_H;
    private static final MethodHandle MH_PTHREAD_MUTEX_HAVE_PREV;
    private static final MethodHandle MH_HAVE_PTHREAD_ATTR_T;
    private static final MethodHandle MH_ALLOCA_H;
    private static final MethodHandle MH_HS_SUCCESS;
    private static final MethodHandle MH_HS_FLAG_CASELESS;
    private static final MethodHandle MH_HS_FLAG_DOTALL;
    private static final MethodHandle MH_HS_FLAG_MULTILINE;
    private static final MethodHandle MH_HS_FLAG_SINGLEMATCH;
    private static final MethodHandle MH_HS_FLAG_ALLOWEMPTY;
    private static final MethodHandle MH_HS_FLAG_UTF8;
    private static final MethodHandle MH_HS_FLAG_UCP;
    private static final MethodHandle MH_HS_FLAG_PREFILTER;
    private static final MethodHandle MH_HS_FLAG_SOM_LEFTMOST;
    private static final MethodHandle MH_HS_FLAG_COMBINATION;
    private static final MethodHandle MH_HS_FLAG_QUIET;
    private static final MethodHandle MH_HS_TUNE_FAMILY_GENERIC;
    private static final MethodHandle MH_HS_TUNE_FAMILY_SNB;
    private static final MethodHandle MH_HS_TUNE_FAMILY_IVB;
    private static final MethodHandle MH_HS_TUNE_FAMILY_HSW;
    private static final MethodHandle MH_HS_TUNE_FAMILY_SLM;
    private static final MethodHandle MH_HS_TUNE_FAMILY_BDW;
    private static final MethodHandle MH_HS_TUNE_FAMILY_SKL;
    private static final MethodHandle MH_HS_TUNE_FAMILY_SKX;
    private static final MethodHandle MH_HS_TUNE_FAMILY_GLM;
    private static final MethodHandle MH_HS_TUNE_FAMILY_ICL;
    private static final MethodHandle MH_HS_TUNE_FAMILY_ICX;
    private static final MethodHandle MH_HS_MODE_BLOCK;
    private static final MethodHandle MH_HS_MODE_NOSTREAM;
    private static final MethodHandle MH_HS_MODE_STREAM;
    private static final MethodHandle MH_HS_MODE_VECTORED;
    private static final MethodHandle MH_HS_MAJOR;
    private static final MethodHandle MH_HS_MINOR;
    private static final MethodHandle MH_HS_PATCH;
    private static final MethodHandle MH_CTYPE_GET_MB_CUR_MAX_DESCRIPTOR;
    private static final MethodHandle MH_CTYPE_GET_MB_CUR_MAX_HANDLE;
    private static final MethodHandle MH_CTYPE_GET_MB_CUR_MAX_ADDRESS;
    private static final MethodHandle MH_CTYPE_GET_MB_CUR_MAX;
    private static final MethodHandle MH_ATOF_DESCRIPTOR;
    private static final MethodHandle MH_ATOF_HANDLE;
    private static final MethodHandle MH_ATOF_ADDRESS;
    private static final MethodHandle MH_ATOF;
    private static final MethodHandle MH_ATOI_DESCRIPTOR;
    private static final MethodHandle MH_ATOI_HANDLE;
    private static final MethodHandle MH_ATOI_ADDRESS;
    private static final MethodHandle MH_ATOI;
    private static final MethodHandle MH_ATOL_DESCRIPTOR;
    private static final MethodHandle MH_ATOL_HANDLE;
    private static final MethodHandle MH_ATOL_ADDRESS;
    private static final MethodHandle MH_ATOL;
    private static final MethodHandle MH_ATOLL_DESCRIPTOR;
    private static final MethodHandle MH_ATOLL_HANDLE;
    private static final MethodHandle MH_ATOLL_ADDRESS;
    private static final MethodHandle MH_ATOLL;
    private static final MethodHandle MH_STRTOD_DESCRIPTOR;
    private static final MethodHandle MH_STRTOD_HANDLE;
    private static final MethodHandle MH_STRTOD_ADDRESS;
    private static final MethodHandle MH_STRTOD;
    private static final MethodHandle MH_STRTOF_DESCRIPTOR;
    private static final MethodHandle MH_STRTOF_HANDLE;
    private static final MethodHandle MH_STRTOF_ADDRESS;
    private static final MethodHandle MH_STRTOF;
    private static final MethodHandle MH_STRTOL_DESCRIPTOR;
    private static final MethodHandle MH_STRTOL_HANDLE;
    private static final MethodHandle MH_STRTOL_ADDRESS;
    private static final MethodHandle MH_STRTOL;
    private static final MethodHandle MH_STRTOUL_DESCRIPTOR;
    private static final MethodHandle MH_STRTOUL_HANDLE;
    private static final MethodHandle MH_STRTOUL_ADDRESS;
    private static final MethodHandle MH_STRTOUL;
    private static final MethodHandle MH_STRTOQ_DESCRIPTOR;
    private static final MethodHandle MH_STRTOQ_HANDLE;
    private static final MethodHandle MH_STRTOQ_ADDRESS;
    private static final MethodHandle MH_STRTOQ;
    private static final MethodHandle MH_STRTOUQ_DESCRIPTOR;
    private static final MethodHandle MH_STRTOUQ_HANDLE;
    private static final MethodHandle MH_STRTOUQ_ADDRESS;
    private static final MethodHandle MH_STRTOUQ;
    private static final MethodHandle MH_STRTOLL_DESCRIPTOR;
    private static final MethodHandle MH_STRTOLL_HANDLE;
    private static final MethodHandle MH_STRTOLL_ADDRESS;
    private static final MethodHandle MH_STRTOLL;
    private static final MethodHandle MH_STRTOULL_DESCRIPTOR;
    private static final MethodHandle MH_STRTOULL_HANDLE;
    private static final MethodHandle MH_STRTOULL_ADDRESS;
    private static final MethodHandle MH_STRTOULL;
    private static final MethodHandle MH_L64A_DESCRIPTOR;
    private static final MethodHandle MH_L64A_HANDLE;
    private static final MethodHandle MH_L64A_ADDRESS;
    private static final MethodHandle MH_L64A;
    private static final MethodHandle MH_A64L_DESCRIPTOR;
    private static final MethodHandle MH_A64L_HANDLE;
    private static final MethodHandle MH_A64L_ADDRESS;
    private static final MethodHandle MH_A64L;
    private static final MethodHandle MH_SELECT_DESCRIPTOR;
    private static final MethodHandle MH_SELECT_HANDLE;
    private static final MethodHandle MH_SELECT_ADDRESS;
    private static final MethodHandle MH_SELECT;
    private static final MethodHandle MH_PSELECT_DESCRIPTOR;
    private static final MethodHandle MH_PSELECT_HANDLE;
    private static final MethodHandle MH_PSELECT_ADDRESS;
    private static final MethodHandle MH_PSELECT;
    private static final MethodHandle MH_RANDOM_DESCRIPTOR;
    private static final MethodHandle MH_RANDOM_HANDLE;
    private static final MethodHandle MH_RANDOM_ADDRESS;
    private static final MethodHandle MH_RANDOM;
    private static final MethodHandle MH_SRANDOM_DESCRIPTOR;
    private static final MethodHandle MH_SRANDOM_HANDLE;
    private static final MethodHandle MH_SRANDOM_ADDRESS;
    private static final MethodHandle MH_SRANDOM;
    private static final MethodHandle MH_INITSTATE_DESCRIPTOR;
    private static final MethodHandle MH_INITSTATE_HANDLE;
    private static final MethodHandle MH_INITSTATE_ADDRESS;
    private static final MethodHandle MH_INITSTATE;
    private static final MethodHandle MH_SETSTATE_DESCRIPTOR;
    private static final MethodHandle MH_SETSTATE_HANDLE;
    private static final MethodHandle MH_SETSTATE_ADDRESS;
    private static final MethodHandle MH_SETSTATE;
    private static final MethodHandle MH_RANDOM_R_DESCRIPTOR;
    private static final MethodHandle MH_RANDOM_R_HANDLE;
    private static final MethodHandle MH_RANDOM_R_ADDRESS;
    private static final MethodHandle MH_RANDOM_R;
    private static final MethodHandle MH_SRANDOM_R_DESCRIPTOR;
    private static final MethodHandle MH_SRANDOM_R_HANDLE;
    private static final MethodHandle MH_SRANDOM_R_ADDRESS;
    private static final MethodHandle MH_SRANDOM_R;
    private static final MethodHandle MH_INITSTATE_R_DESCRIPTOR;
    private static final MethodHandle MH_INITSTATE_R_HANDLE;
    private static final MethodHandle MH_INITSTATE_R_ADDRESS;
    private static final MethodHandle MH_INITSTATE_R;
    private static final MethodHandle MH_SETSTATE_R_DESCRIPTOR;
    private static final MethodHandle MH_SETSTATE_R_HANDLE;
    private static final MethodHandle MH_SETSTATE_R_ADDRESS;
    private static final MethodHandle MH_SETSTATE_R;
    private static final MethodHandle MH_RAND_DESCRIPTOR;
    private static final MethodHandle MH_RAND_HANDLE;
    private static final MethodHandle MH_RAND_ADDRESS;
    private static final MethodHandle MH_RAND;
    private static final MethodHandle MH_SRAND_DESCRIPTOR;
    private static final MethodHandle MH_SRAND_HANDLE;
    private static final MethodHandle MH_SRAND_ADDRESS;
    private static final MethodHandle MH_SRAND;
    private static final MethodHandle MH_RAND_R_DESCRIPTOR;
    private static final MethodHandle MH_RAND_R_HANDLE;
    private static final MethodHandle MH_RAND_R_ADDRESS;
    private static final MethodHandle MH_RAND_R;
    private static final MethodHandle MH_DRAND48_DESCRIPTOR;
    private static final MethodHandle MH_DRAND48_HANDLE;
    private static final MethodHandle MH_DRAND48_ADDRESS;
    private static final MethodHandle MH_DRAND48;
    private static final MethodHandle MH_ERAND48_DESCRIPTOR;
    private static final MethodHandle MH_ERAND48_HANDLE;
    private static final MethodHandle MH_ERAND48_ADDRESS;
    private static final MethodHandle MH_ERAND48;
    private static final MethodHandle MH_LRAND48_DESCRIPTOR;
    private static final MethodHandle MH_LRAND48_HANDLE;
    private static final MethodHandle MH_LRAND48_ADDRESS;
    private static final MethodHandle MH_LRAND48;
    private static final MethodHandle MH_NRAND48_DESCRIPTOR;
    private static final MethodHandle MH_NRAND48_HANDLE;
    private static final MethodHandle MH_NRAND48_ADDRESS;
    private static final MethodHandle MH_NRAND48;
    private static final MethodHandle MH_MRAND48_DESCRIPTOR;
    private static final MethodHandle MH_MRAND48_HANDLE;
    private static final MethodHandle MH_MRAND48_ADDRESS;
    private static final MethodHandle MH_MRAND48;
    private static final MethodHandle MH_JRAND48_DESCRIPTOR;
    private static final MethodHandle MH_JRAND48_HANDLE;
    private static final MethodHandle MH_JRAND48_ADDRESS;
    private static final MethodHandle MH_JRAND48;
    private static final MethodHandle MH_SRAND48_DESCRIPTOR;
    private static final MethodHandle MH_SRAND48_HANDLE;
    private static final MethodHandle MH_SRAND48_ADDRESS;
    private static final MethodHandle MH_SRAND48;
    private static final MethodHandle MH_SEED48_DESCRIPTOR;
    private static final MethodHandle MH_SEED48_HANDLE;
    private static final MethodHandle MH_SEED48_ADDRESS;
    private static final MethodHandle MH_SEED48;
    private static final MethodHandle MH_LCONG48_DESCRIPTOR;
    private static final MethodHandle MH_LCONG48_HANDLE;
    private static final MethodHandle MH_LCONG48_ADDRESS;
    private static final MethodHandle MH_LCONG48;
    private static final MethodHandle MH_DRAND48_R_DESCRIPTOR;
    private static final MethodHandle MH_DRAND48_R_HANDLE;
    private static final MethodHandle MH_DRAND48_R_ADDRESS;
    private static final MethodHandle MH_DRAND48_R;
    private static final MethodHandle MH_ERAND48_R_DESCRIPTOR;
    private static final MethodHandle MH_ERAND48_R_HANDLE;
    private static final MethodHandle MH_ERAND48_R_ADDRESS;
    private static final MethodHandle MH_ERAND48_R;
    private static final MethodHandle MH_LRAND48_R_DESCRIPTOR;
    private static final MethodHandle MH_LRAND48_R_HANDLE;
    private static final MethodHandle MH_LRAND48_R_ADDRESS;
    private static final MethodHandle MH_LRAND48_R;
    private static final MethodHandle MH_NRAND48_R_DESCRIPTOR;
    private static final MethodHandle MH_NRAND48_R_HANDLE;
    private static final MethodHandle MH_NRAND48_R_ADDRESS;
    private static final MethodHandle MH_NRAND48_R;
    private static final MethodHandle MH_MRAND48_R_DESCRIPTOR;
    private static final MethodHandle MH_MRAND48_R_HANDLE;
    private static final MethodHandle MH_MRAND48_R_ADDRESS;
    private static final MethodHandle MH_MRAND48_R;
    private static final MethodHandle MH_JRAND48_R_DESCRIPTOR;
    private static final MethodHandle MH_JRAND48_R_HANDLE;
    private static final MethodHandle MH_JRAND48_R_ADDRESS;
    private static final MethodHandle MH_JRAND48_R;
    private static final MethodHandle MH_SRAND48_R_DESCRIPTOR;
    private static final MethodHandle MH_SRAND48_R_HANDLE;
    private static final MethodHandle MH_SRAND48_R_ADDRESS;
    private static final MethodHandle MH_SRAND48_R;
    private static final MethodHandle MH_SEED48_R_DESCRIPTOR;
    private static final MethodHandle MH_SEED48_R_HANDLE;
    private static final MethodHandle MH_SEED48_R_ADDRESS;
    private static final MethodHandle MH_SEED48_R;
    private static final MethodHandle MH_LCONG48_R_DESCRIPTOR;
    private static final MethodHandle MH_LCONG48_R_HANDLE;
    private static final MethodHandle MH_LCONG48_R_ADDRESS;
    private static final MethodHandle MH_LCONG48_R;
    private static final MethodHandle MH_ARC4RANDOM_DESCRIPTOR;
    private static final MethodHandle MH_ARC4RANDOM_HANDLE;
    private static final MethodHandle MH_ARC4RANDOM_ADDRESS;
    private static final MethodHandle MH_ARC4RANDOM;
    private static final MethodHandle MH_ARC4RANDOM_BUF_DESCRIPTOR;
    private static final MethodHandle MH_ARC4RANDOM_BUF_HANDLE;
    private static final MethodHandle MH_ARC4RANDOM_BUF_ADDRESS;
    private static final MethodHandle MH_ARC4RANDOM_BUF;
    private static final MethodHandle MH_ARC4RANDOM_UNIFORM_DESCRIPTOR;
    private static final MethodHandle MH_ARC4RANDOM_UNIFORM_HANDLE;
    private static final MethodHandle MH_ARC4RANDOM_UNIFORM_ADDRESS;
    private static final MethodHandle MH_ARC4RANDOM_UNIFORM;
    private static final MethodHandle MH_MALLOC_DESCRIPTOR;
    private static final MethodHandle MH_MALLOC_HANDLE;
    private static final MethodHandle MH_MALLOC_ADDRESS;
    private static final MethodHandle MH_MALLOC;
    private static final MethodHandle MH_CALLOC_DESCRIPTOR;
    private static final MethodHandle MH_CALLOC_HANDLE;
    private static final MethodHandle MH_CALLOC_ADDRESS;
    private static final MethodHandle MH_CALLOC;
    private static final MethodHandle MH_REALLOC_DESCRIPTOR;
    private static final MethodHandle MH_REALLOC_HANDLE;
    private static final MethodHandle MH_REALLOC_ADDRESS;
    private static final MethodHandle MH_REALLOC;
    private static final MethodHandle MH_FREE_DESCRIPTOR;
    private static final MethodHandle MH_FREE_HANDLE;
    private static final MethodHandle MH_FREE_ADDRESS;
    private static final MethodHandle MH_FREE;
    private static final MethodHandle MH_REALLOCARRAY_DESCRIPTOR;
    private static final MethodHandle MH_REALLOCARRAY_HANDLE;
    private static final MethodHandle MH_REALLOCARRAY_ADDRESS;
    private static final MethodHandle MH_REALLOCARRAY;
    private static final MethodHandle MH_ALLOCA_DESCRIPTOR;
    private static final MethodHandle MH_ALLOCA_HANDLE;
    private static final MethodHandle MH_ALLOCA_ADDRESS;
    private static final MethodHandle MH_ALLOCA;
    private static final MethodHandle MH_VALLOC_DESCRIPTOR;
    private static final MethodHandle MH_VALLOC_HANDLE;
    private static final MethodHandle MH_VALLOC_ADDRESS;
    private static final MethodHandle MH_VALLOC;
    private static final MethodHandle MH_POSIX_MEMALIGN_DESCRIPTOR;
    private static final MethodHandle MH_POSIX_MEMALIGN_HANDLE;
    private static final MethodHandle MH_POSIX_MEMALIGN_ADDRESS;
    private static final MethodHandle MH_POSIX_MEMALIGN;
    private static final MethodHandle MH_ALIGNED_ALLOC_DESCRIPTOR;
    private static final MethodHandle MH_ALIGNED_ALLOC_HANDLE;
    private static final MethodHandle MH_ALIGNED_ALLOC_ADDRESS;
    private static final MethodHandle MH_ALIGNED_ALLOC;
    private static final MethodHandle MH_ABORT_DESCRIPTOR;
    private static final MethodHandle MH_ABORT_HANDLE;
    private static final MethodHandle MH_ABORT_ADDRESS;
    private static final MethodHandle MH_ABORT;
    private static final MethodHandle MH_ATEXIT_DESCRIPTOR;
    private static final MethodHandle MH_ATEXIT_HANDLE;
    private static final MethodHandle MH_ATEXIT_ADDRESS;
    private static final MethodHandle MH_ATEXIT;
    private static final MethodHandle MH_AT_QUICK_EXIT_DESCRIPTOR;
    private static final MethodHandle MH_AT_QUICK_EXIT_HANDLE;
    private static final MethodHandle MH_AT_QUICK_EXIT_ADDRESS;
    private static final MethodHandle MH_AT_QUICK_EXIT;
    private static final MethodHandle MH_ON_EXIT_DESCRIPTOR;
    private static final MethodHandle MH_ON_EXIT_HANDLE;
    private static final MethodHandle MH_ON_EXIT_ADDRESS;
    private static final MethodHandle MH_ON_EXIT;
    private static final MethodHandle MH_EXIT_DESCRIPTOR;
    private static final MethodHandle MH_EXIT_HANDLE;
    private static final MethodHandle MH_EXIT_ADDRESS;
    private static final MethodHandle MH_EXIT;
    private static final MethodHandle MH_QUICK_EXIT_DESCRIPTOR;
    private static final MethodHandle MH_QUICK_EXIT_HANDLE;
    private static final MethodHandle MH_QUICK_EXIT_ADDRESS;
    private static final MethodHandle MH_QUICK_EXIT;
    private static final MethodHandle MH_EXIT_DESCRIPTOR_1;
    private static final MethodHandle MH_EXIT_HANDLE_1;
    private static final MethodHandle MH_EXIT_ADDRESS_1;
    private static final MethodHandle MH_EXIT_1;
    private static final MethodHandle MH_GETENV_DESCRIPTOR;
    private static final MethodHandle MH_GETENV_HANDLE;
    private static final MethodHandle MH_GETENV_ADDRESS;
    private static final MethodHandle MH_GETENV;
    private static final MethodHandle MH_PUTENV_DESCRIPTOR;
    private static final MethodHandle MH_PUTENV_HANDLE;
    private static final MethodHandle MH_PUTENV_ADDRESS;
    private static final MethodHandle MH_PUTENV;
    private static final MethodHandle MH_SETENV_DESCRIPTOR;
    private static final MethodHandle MH_SETENV_HANDLE;
    private static final MethodHandle MH_SETENV_ADDRESS;
    private static final MethodHandle MH_SETENV;
    private static final MethodHandle MH_UNSETENV_DESCRIPTOR;
    private static final MethodHandle MH_UNSETENV_HANDLE;
    private static final MethodHandle MH_UNSETENV_ADDRESS;
    private static final MethodHandle MH_UNSETENV;
    private static final MethodHandle MH_CLEARENV_DESCRIPTOR;
    private static final MethodHandle MH_CLEARENV_HANDLE;
    private static final MethodHandle MH_CLEARENV_ADDRESS;
    private static final MethodHandle MH_CLEARENV;
    private static final MethodHandle MH_MKTEMP_DESCRIPTOR;
    private static final MethodHandle MH_MKTEMP_HANDLE;
    private static final MethodHandle MH_MKTEMP_ADDRESS;
    private static final MethodHandle MH_MKTEMP;
    private static final MethodHandle MH_MKSTEMP_DESCRIPTOR;
    private static final MethodHandle MH_MKSTEMP_HANDLE;
    private static final MethodHandle MH_MKSTEMP_ADDRESS;
    private static final MethodHandle MH_MKSTEMP;
    private static final MethodHandle MH_MKSTEMPS_DESCRIPTOR;
    private static final MethodHandle MH_MKSTEMPS_HANDLE;
    private static final MethodHandle MH_MKSTEMPS_ADDRESS;
    private static final MethodHandle MH_MKSTEMPS;
    private static final MethodHandle MH_MKDTEMP_DESCRIPTOR;
    private static final MethodHandle MH_MKDTEMP_HANDLE;
    private static final MethodHandle MH_MKDTEMP_ADDRESS;
    private static final MethodHandle MH_MKDTEMP;
    private static final MethodHandle MH_SYSTEM_DESCRIPTOR;
    private static final MethodHandle MH_SYSTEM_HANDLE;
    private static final MethodHandle MH_SYSTEM_ADDRESS;
    private static final MethodHandle MH_SYSTEM;
    private static final MethodHandle MH_REALPATH_DESCRIPTOR;
    private static final MethodHandle MH_REALPATH_HANDLE;
    private static final MethodHandle MH_REALPATH_ADDRESS;
    private static final MethodHandle MH_REALPATH;
    private static final MethodHandle MH_BSEARCH_DESCRIPTOR;
    private static final MethodHandle MH_BSEARCH_HANDLE;
    private static final MethodHandle MH_BSEARCH_ADDRESS;
    private static final MethodHandle MH_BSEARCH;
    private static final MethodHandle MH_QSORT_DESCRIPTOR;
    private static final MethodHandle MH_QSORT_HANDLE;
    private static final MethodHandle MH_QSORT_ADDRESS;
    private static final MethodHandle MH_QSORT;
    private static final MethodHandle MH_ABS_DESCRIPTOR;
    private static final MethodHandle MH_ABS_HANDLE;
    private static final MethodHandle MH_ABS_ADDRESS;
    private static final MethodHandle MH_ABS;
    private static final MethodHandle MH_LABS_DESCRIPTOR;
    private static final MethodHandle MH_LABS_HANDLE;
    private static final MethodHandle MH_LABS_ADDRESS;
    private static final MethodHandle MH_LABS;
    private static final MethodHandle MH_LLABS_DESCRIPTOR;
    private static final MethodHandle MH_LLABS_HANDLE;
    private static final MethodHandle MH_LLABS_ADDRESS;
    private static final MethodHandle MH_LLABS;
    private static final MethodHandle MH_DIV_DESCRIPTOR;
    private static final MethodHandle MH_DIV_HANDLE;
    private static final MethodHandle MH_DIV_ADDRESS;
    private static final MethodHandle MH_DIV;
    private static final MethodHandle MH_LDIV_DESCRIPTOR;
    private static final MethodHandle MH_LDIV_HANDLE;
    private static final MethodHandle MH_LDIV_ADDRESS;
    private static final MethodHandle MH_LDIV;
    private static final MethodHandle MH_LLDIV_DESCRIPTOR;
    private static final MethodHandle MH_LLDIV_HANDLE;
    private static final MethodHandle MH_LLDIV_ADDRESS;
    private static final MethodHandle MH_LLDIV;
    private static final MethodHandle MH_ECVT_DESCRIPTOR;
    private static final MethodHandle MH_ECVT_HANDLE;
    private static final MethodHandle MH_ECVT_ADDRESS;
    private static final MethodHandle MH_ECVT;
    private static final MethodHandle MH_FCVT_DESCRIPTOR;
    private static final MethodHandle MH_FCVT_HANDLE;
    private static final MethodHandle MH_FCVT_ADDRESS;
    private static final MethodHandle MH_FCVT;
    private static final MethodHandle MH_GCVT_DESCRIPTOR;
    private static final MethodHandle MH_GCVT_HANDLE;
    private static final MethodHandle MH_GCVT_ADDRESS;
    private static final MethodHandle MH_GCVT;
    private static final MethodHandle MH_ECVT_R_DESCRIPTOR;
    private static final MethodHandle MH_ECVT_R_HANDLE;
    private static final MethodHandle MH_ECVT_R_ADDRESS;
    private static final MethodHandle MH_ECVT_R;
    private static final MethodHandle MH_FCVT_R_DESCRIPTOR;
    private static final MethodHandle MH_FCVT_R_HANDLE;
    private static final MethodHandle MH_FCVT_R_ADDRESS;
    private static final MethodHandle MH_FCVT_R;
    private static final MethodHandle MH_MBLEN_DESCRIPTOR;
    private static final MethodHandle MH_MBLEN_HANDLE;
    private static final MethodHandle MH_MBLEN_ADDRESS;
    private static final MethodHandle MH_MBLEN;
    private static final MethodHandle MH_MBTOWC_DESCRIPTOR;
    private static final MethodHandle MH_MBTOWC_HANDLE;
    private static final MethodHandle MH_MBTOWC_ADDRESS;
    private static final MethodHandle MH_MBTOWC;
    private static final MethodHandle MH_WCTOMB_DESCRIPTOR;
    private static final MethodHandle MH_WCTOMB_HANDLE;
    private static final MethodHandle MH_WCTOMB_ADDRESS;
    private static final MethodHandle MH_WCTOMB;
    private static final MethodHandle MH_MBSTOWCS_DESCRIPTOR;
    private static final MethodHandle MH_MBSTOWCS_HANDLE;
    private static final MethodHandle MH_MBSTOWCS_ADDRESS;
    private static final MethodHandle MH_MBSTOWCS;
    private static final MethodHandle MH_WCSTOMBS_DESCRIPTOR;
    private static final MethodHandle MH_WCSTOMBS_HANDLE;
    private static final MethodHandle MH_WCSTOMBS_ADDRESS;
    private static final MethodHandle MH_WCSTOMBS;
    private static final MethodHandle MH_RPMATCH_DESCRIPTOR;
    private static final MethodHandle MH_RPMATCH_HANDLE;
    private static final MethodHandle MH_RPMATCH_ADDRESS;
    private static final MethodHandle MH_RPMATCH;
    private static final MethodHandle MH_GETSUBOPT_DESCRIPTOR;
    private static final MethodHandle MH_GETSUBOPT_HANDLE;
    private static final MethodHandle MH_GETSUBOPT_ADDRESS;
    private static final MethodHandle MH_GETSUBOPT;
    private static final MethodHandle MH_GETLOADAVG_DESCRIPTOR;
    private static final MethodHandle MH_GETLOADAVG_HANDLE;
    private static final MethodHandle MH_GETLOADAVG_ADDRESS;
    private static final MethodHandle MH_GETLOADAVG;
    private static final MethodHandle MH_HS_FREE_DATABASE_DESCRIPTOR;
    private static final MethodHandle MH_HS_FREE_DATABASE_HANDLE;
    private static final MethodHandle MH_HS_FREE_DATABASE_ADDRESS;
    private static final MethodHandle MH_HS_FREE_DATABASE;
    private static final MethodHandle MH_HS_SERIALIZE_DATABASE_DESCRIPTOR;
    private static final MethodHandle MH_HS_SERIALIZE_DATABASE_HANDLE;
    private static final MethodHandle MH_HS_SERIALIZE_DATABASE_ADDRESS;
    private static final MethodHandle MH_HS_SERIALIZE_DATABASE;
    private static final MethodHandle MH_HS_DESERIALIZE_DATABASE_DESCRIPTOR;
    private static final MethodHandle MH_HS_DESERIALIZE_DATABASE_HANDLE;
    private static final MethodHandle MH_HS_DESERIALIZE_DATABASE_ADDRESS;
    private static final MethodHandle MH_HS_DESERIALIZE_DATABASE;
    private static final MethodHandle MH_HS_DESERIALIZE_DATABASE_AT_DESCRIPTOR;
    private static final MethodHandle MH_HS_DESERIALIZE_DATABASE_AT_HANDLE;
    private static final MethodHandle MH_HS_DESERIALIZE_DATABASE_AT_ADDRESS;
    private static final MethodHandle MH_HS_DESERIALIZE_DATABASE_AT;
    private static final MethodHandle MH_HS_STREAM_SIZE_DESCRIPTOR;
    private static final MethodHandle MH_HS_STREAM_SIZE_HANDLE;
    private static final MethodHandle MH_HS_STREAM_SIZE_ADDRESS;
    private static final MethodHandle MH_HS_STREAM_SIZE;
    private static final MethodHandle MH_HS_DATABASE_SIZE_DESCRIPTOR;
    private static final MethodHandle MH_HS_DATABASE_SIZE_HANDLE;
    private static final MethodHandle MH_HS_DATABASE_SIZE_ADDRESS;
    private static final MethodHandle MH_HS_DATABASE_SIZE;
    private static final MethodHandle MH_HS_SERIALIZED_DATABASE_SIZE_DESCRIPTOR;
    private static final MethodHandle MH_HS_SERIALIZED_DATABASE_SIZE_HANDLE;
    private static final MethodHandle MH_HS_SERIALIZED_DATABASE_SIZE_ADDRESS;
    private static final MethodHandle MH_HS_SERIALIZED_DATABASE_SIZE;
    private static final MethodHandle MH_HS_DATABASE_INFO_DESCRIPTOR;
    private static final MethodHandle MH_HS_DATABASE_INFO_HANDLE;
    private static final MethodHandle MH_HS_DATABASE_INFO_ADDRESS;
    private static final MethodHandle MH_HS_DATABASE_INFO;
    private static final MethodHandle MH_HS_SERIALIZED_DATABASE_INFO_DESCRIPTOR;
    private static final MethodHandle MH_HS_SERIALIZED_DATABASE_INFO_HANDLE;
    private static final MethodHandle MH_HS_SERIALIZED_DATABASE_INFO_ADDRESS;
    private static final MethodHandle MH_HS_SERIALIZED_DATABASE_INFO;
    private static final MethodHandle MH_HS_SET_ALLOCATOR_DESCRIPTOR;
    private static final MethodHandle MH_HS_SET_ALLOCATOR_HANDLE;
    private static final MethodHandle MH_HS_SET_ALLOCATOR_ADDRESS;
    private static final MethodHandle MH_HS_SET_ALLOCATOR;
    private static final MethodHandle MH_HS_SET_DATABASE_ALLOCATOR_DESCRIPTOR;
    private static final MethodHandle MH_HS_SET_DATABASE_ALLOCATOR_HANDLE;
    private static final MethodHandle MH_HS_SET_DATABASE_ALLOCATOR_ADDRESS;
    private static final MethodHandle MH_HS_SET_DATABASE_ALLOCATOR;
    private static final MethodHandle MH_HS_SET_MISC_ALLOCATOR_DESCRIPTOR;
    private static final MethodHandle MH_HS_SET_MISC_ALLOCATOR_HANDLE;
    private static final MethodHandle MH_HS_SET_MISC_ALLOCATOR_ADDRESS;
    private static final MethodHandle MH_HS_SET_MISC_ALLOCATOR;
    private static final MethodHandle MH_HS_SET_SCRATCH_ALLOCATOR_DESCRIPTOR;
    private static final MethodHandle MH_HS_SET_SCRATCH_ALLOCATOR_HANDLE;
    private static final MethodHandle MH_HS_SET_SCRATCH_ALLOCATOR_ADDRESS;
    private static final MethodHandle MH_HS_SET_SCRATCH_ALLOCATOR;
    private static final MethodHandle MH_HS_SET_STREAM_ALLOCATOR_DESCRIPTOR;
    private static final MethodHandle MH_HS_SET_STREAM_ALLOCATOR_HANDLE;
    private static final MethodHandle MH_HS_SET_STREAM_ALLOCATOR_ADDRESS;
    private static final MethodHandle MH_HS_SET_STREAM_ALLOCATOR;
    private static final MethodHandle MH_HS_VERSION_DESCRIPTOR;
    private static final MethodHandle MH_HS_VERSION_HANDLE;
    private static final MethodHandle MH_HS_VERSION_ADDRESS;
    private static final MethodHandle MH_HS_VERSION;
    private static final MethodHandle MH_HS_VALID_PLATFORM_DESCRIPTOR;
    private static final MethodHandle MH_HS_VALID_PLATFORM_HANDLE;
    private static final MethodHandle MH_HS_VALID_PLATFORM_ADDRESS;
    private static final MethodHandle MH_HS_VALID_PLATFORM;
    private static final MethodHandle MH_HS_COMPILE_DESCRIPTOR;
    private static final MethodHandle MH_HS_COMPILE_HANDLE;
    private static final MethodHandle MH_HS_COMPILE_ADDRESS;
    private static final MethodHandle MH_HS_COMPILE;
    private static final MethodHandle MH_HS_COMPILE_MULTI_DESCRIPTOR;
    private static final MethodHandle MH_HS_COMPILE_MULTI_HANDLE;
    private static final MethodHandle MH_HS_COMPILE_MULTI_ADDRESS;
    private static final MethodHandle MH_HS_COMPILE_MULTI;
    private static final MethodHandle MH_HS_COMPILE_EXT_MULTI_DESCRIPTOR;
    private static final MethodHandle MH_HS_COMPILE_EXT_MULTI_HANDLE;
    private static final MethodHandle MH_HS_COMPILE_EXT_MULTI_ADDRESS;
    private static final MethodHandle MH_HS_COMPILE_EXT_MULTI;
    private static final MethodHandle MH_HS_COMPILE_LIT_DESCRIPTOR;
    private static final MethodHandle MH_HS_COMPILE_LIT_HANDLE;
    private static final MethodHandle MH_HS_COMPILE_LIT_ADDRESS;
    private static final MethodHandle MH_HS_COMPILE_LIT;
    private static final MethodHandle MH_HS_COMPILE_LIT_MULTI_DESCRIPTOR;
    private static final MethodHandle MH_HS_COMPILE_LIT_MULTI_HANDLE;
    private static final MethodHandle MH_HS_COMPILE_LIT_MULTI_ADDRESS;
    private static final MethodHandle MH_HS_COMPILE_LIT_MULTI;
    private static final MethodHandle MH_HS_FREE_COMPILE_ERROR_DESCRIPTOR;
    private static final MethodHandle MH_HS_FREE_COMPILE_ERROR_HANDLE;
    private static final MethodHandle MH_HS_FREE_COMPILE_ERROR_ADDRESS;
    private static final MethodHandle MH_HS_FREE_COMPILE_ERROR;
    private static final MethodHandle MH_HS_EXPRESSION_INFO_DESCRIPTOR;
    private static final MethodHandle MH_HS_EXPRESSION_INFO_HANDLE;
    private static final MethodHandle MH_HS_EXPRESSION_INFO_ADDRESS;
    private static final MethodHandle MH_HS_EXPRESSION_INFO;
    private static final MethodHandle MH_HS_EXPRESSION_EXT_INFO_DESCRIPTOR;
    private static final MethodHandle MH_HS_EXPRESSION_EXT_INFO_HANDLE;
    private static final MethodHandle MH_HS_EXPRESSION_EXT_INFO_ADDRESS;
    private static final MethodHandle MH_HS_EXPRESSION_EXT_INFO;
    private static final MethodHandle MH_HS_POPULATE_PLATFORM_DESCRIPTOR;
    private static final MethodHandle MH_HS_POPULATE_PLATFORM_HANDLE;
    private static final MethodHandle MH_HS_POPULATE_PLATFORM_ADDRESS;
    private static final MethodHandle MH_HS_POPULATE_PLATFORM;
    private static final MethodHandle MH_HS_OPEN_STREAM_DESCRIPTOR;
    private static final MethodHandle MH_HS_OPEN_STREAM_HANDLE;
    private static final MethodHandle MH_HS_OPEN_STREAM_ADDRESS;
    private static final MethodHandle MH_HS_OPEN_STREAM;
    private static final MethodHandle MH_HS_SCAN_STREAM_DESCRIPTOR;
    private static final MethodHandle MH_HS_SCAN_STREAM_HANDLE;
    private static final MethodHandle MH_HS_SCAN_STREAM_ADDRESS;
    private static final MethodHandle MH_HS_SCAN_STREAM;
    private static final MethodHandle MH_HS_CLOSE_STREAM_DESCRIPTOR;
    private static final MethodHandle MH_HS_CLOSE_STREAM_HANDLE;
    private static final MethodHandle MH_HS_CLOSE_STREAM_ADDRESS;
    private static final MethodHandle MH_HS_CLOSE_STREAM;
    private static final MethodHandle MH_HS_RESET_STREAM_DESCRIPTOR;
    private static final MethodHandle MH_HS_RESET_STREAM_HANDLE;
    private static final MethodHandle MH_HS_RESET_STREAM_ADDRESS;
    private static final MethodHandle MH_HS_RESET_STREAM;
    private static final MethodHandle MH_HS_COPY_STREAM_DESCRIPTOR;
    private static final MethodHandle MH_HS_COPY_STREAM_HANDLE;
    private static final MethodHandle MH_HS_COPY_STREAM_ADDRESS;
    private static final MethodHandle MH_HS_COPY_STREAM;
    private static final MethodHandle MH_HS_RESET_AND_COPY_STREAM_DESCRIPTOR;
    private static final MethodHandle MH_HS_RESET_AND_COPY_STREAM_HANDLE;
    private static final MethodHandle MH_HS_RESET_AND_COPY_STREAM_ADDRESS;
    private static final MethodHandle MH_HS_RESET_AND_COPY_STREAM;
    private static final MethodHandle MH_HS_COMPRESS_STREAM_DESCRIPTOR;
    private static final MethodHandle MH_HS_COMPRESS_STREAM_HANDLE;
    private static final MethodHandle MH_HS_COMPRESS_STREAM_ADDRESS;
    private static final MethodHandle MH_HS_COMPRESS_STREAM;
    private static final MethodHandle MH_HS_EXPAND_STREAM_DESCRIPTOR;
    private static final MethodHandle MH_HS_EXPAND_STREAM_HANDLE;
    private static final MethodHandle MH_HS_EXPAND_STREAM_ADDRESS;
    private static final MethodHandle MH_HS_EXPAND_STREAM;
    private static final MethodHandle MH_HS_RESET_AND_EXPAND_STREAM_DESCRIPTOR;
    private static final MethodHandle MH_HS_RESET_AND_EXPAND_STREAM_HANDLE;
    private static final MethodHandle MH_HS_RESET_AND_EXPAND_STREAM_ADDRESS;
    private static final MethodHandle MH_HS_RESET_AND_EXPAND_STREAM;
    private static final MethodHandle MH_HS_SCAN_DESCRIPTOR;
    private static final MethodHandle MH_HS_SCAN_HANDLE;
    private static final MethodHandle MH_HS_SCAN_ADDRESS;
    private static final MethodHandle MH_HS_SCAN;
    private static final MethodHandle MH_HS_SCAN_VECTOR_DESCRIPTOR;
    private static final MethodHandle MH_HS_SCAN_VECTOR_HANDLE;
    private static final MethodHandle MH_HS_SCAN_VECTOR_ADDRESS;
    private static final MethodHandle MH_HS_SCAN_VECTOR;
    private static final MethodHandle MH_HS_ALLOC_SCRATCH_DESCRIPTOR;
    private static final MethodHandle MH_HS_ALLOC_SCRATCH_HANDLE;
    private static final MethodHandle MH_HS_ALLOC_SCRATCH_ADDRESS;
    private static final MethodHandle MH_HS_ALLOC_SCRATCH;
    private static final MethodHandle MH_HS_CLONE_SCRATCH_DESCRIPTOR;
    private static final MethodHandle MH_HS_CLONE_SCRATCH_HANDLE;
    private static final MethodHandle MH_HS_CLONE_SCRATCH_ADDRESS;
    private static final MethodHandle MH_HS_CLONE_SCRATCH;
    private static final MethodHandle MH_HS_SCRATCH_SIZE_DESCRIPTOR;
    private static final MethodHandle MH_HS_SCRATCH_SIZE_HANDLE;
    private static final MethodHandle MH_HS_SCRATCH_SIZE_ADDRESS;
    private static final MethodHandle MH_HS_SCRATCH_SIZE;
    private static final MethodHandle MH_HS_FREE_SCRATCH_DESCRIPTOR;
    private static final MethodHandle MH_HS_FREE_SCRATCH_HANDLE;
    private static final MethodHandle MH_HS_FREE_SCRATCH_ADDRESS;
    private static final MethodHandle MH_HS_FREE_SCRATCH;
    private static final MethodHandle MH_POSIX_C_SOURCE;
    private static final MethodHandle MH_TIMESIZE;
    private static final MethodHandle MH_STDC_IEC_60559_BFP;
    private static final MethodHandle MH_STDC_IEC_60559_COMPLEX;
    private static final MethodHandle MH_STDC_ISO_10646;
    private static final MethodHandle MH_NULL;
    private static final MethodHandle MH_WCLONE;
    private static final MethodHandle MH_HAVE_DISTINCT_FLOAT16;
    private static final MethodHandle MH_HAVE_DISTINCT_FLOAT128X;
    private static final MethodHandle MH_HAVE_FLOAT128_UNLIKE_LDBL;
    private static final MethodHandle MH_BYTE_ORDER;
    private static final MethodHandle MH_FLOAT_WORD_ORDER;
    private static final MethodHandle MH_LITTLE_ENDIAN_1;
    private static final MethodHandle MH_BIG_ENDIAN_1;
    private static final MethodHandle MH_PDP_ENDIAN_1;
    private static final MethodHandle MH_BYTE_ORDER_1;
    private static final MethodHandle MH_SIGSET_NWORDS;
    private static final MethodHandle MH_NFDBITS;
    private static final MethodHandle MH_FD_SETSIZE_1;
    private static final MethodHandle MH_NFDBITS_1;
    private static final MethodHandle MH_PTHREAD_RWLOCK_ELISION_EXTRA;
    private static final MethodHandle MH_HS_INVALID;
    private static final MethodHandle MH_HS_NOMEM;
    private static final MethodHandle MH_HS_SCAN_TERMINATED;
    private static final MethodHandle MH_HS_COMPILER_ERROR;
    private static final MethodHandle MH_HS_DB_VERSION_ERROR;
    private static final MethodHandle MH_HS_DB_PLATFORM_ERROR;
    private static final MethodHandle MH_HS_DB_MODE_ERROR;
    private static final MethodHandle MH_HS_BAD_ALIGN;
    private static final MethodHandle MH_HS_BAD_ALLOC;
    private static final MethodHandle MH_HS_SCRATCH_IN_USE;
    private static final MethodHandle MH_HS_ARCH_ERROR;
    private static final MethodHandle MH_HS_INSUFFICIENT_SPACE;
    private static final MethodHandle MH_HS_UNKNOWN_ERROR;
    private static final MethodHandle MH_HS_EXT_FLAG_MIN_OFFSET;
    private static final MethodHandle MH_HS_EXT_FLAG_MAX_OFFSET;
    private static final MethodHandle MH_HS_EXT_FLAG_MIN_LENGTH;
    private static final MethodHandle MH_HS_EXT_FLAG_EDIT_DISTANCE;
    private static final MethodHandle MH_HS_EXT_FLAG_HAMMING_DISTANCE;
    private static final MethodHandle MH_HS_CPU_FEATURES_AVX2;
    private static final MethodHandle MH_HS_CPU_FEATURES_AVX512;
    private static final MethodHandle MH_HS_CPU_FEATURES_AVX512VBMI;
    private static final MethodHandle MH_HS_MODE_SOM_HORIZON_LARGE;
    private static final MethodHandle MH_HS_MODE_SOM_HORIZON_MEDIUM;
    private static final MethodHandle MH_HS_MODE_SOM_HORIZON_SMALL;
    private static final MethodHandle MH_HS_OFFSET_PAST_HORIZON;
    private static final MethodHandle MH_HS_VERSION_STRING;
    private static final MethodHandle MH_HS_VERSION_32BIT;

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
            MH_FEATURES_H = find("_FEATURES_H", int.class);
            MH_DEFAULT_SOURCE = find("_DEFAULT_SOURCE", int.class);
            MH_GLIBC_USE_ISOC2X = find("__GLIBC_USE_ISOC2X", int.class);
            MH_USE_ISOC11 = find("__USE_ISOC11", int.class);
            MH_USE_ISOC99 = find("__USE_ISOC99", int.class);
            MH_USE_ISOC95 = find("__USE_ISOC95", int.class);
            MH_USE_POSIX_IMPLICITLY = find("__USE_POSIX_IMPLICITLY", int.class);
            MH_POSIX_SOURCE = find("_POSIX_SOURCE", int.class);
            MH_USE_POSIX = find("__USE_POSIX", int.class);
            MH_USE_POSIX2 = find("__USE_POSIX2", int.class);
            MH_USE_POSIX199309 = find("__USE_POSIX199309", int.class);
            MH_USE_POSIX199506 = find("__USE_POSIX199506", int.class);
            MH_USE_XOPEN2K = find("__USE_XOPEN2K", int.class);
            MH_USE_XOPEN2K8 = find("__USE_XOPEN2K8", int.class);
            MH_ATFILE_SOURCE = find("_ATFILE_SOURCE", int.class);
            MH_WORDSIZE = find("__WORDSIZE", int.class);
            MH_WORDSIZE_TIME64_COMPAT32 = find("__WORDSIZE_TIME64_COMPAT32", int.class);
            MH_SYSCALL_WORDSIZE = find("__SYSCALL_WORDSIZE", int.class);
            MH_USE_MISC = find("__USE_MISC", int.class);
            MH_USE_ATFILE = find("__USE_ATFILE", int.class);
            MH_USE_FORTIFY_LEVEL = find("__USE_FORTIFY_LEVEL", int.class);
            MH_GLIBC_USE_DEPRECATED_GETS = find("__GLIBC_USE_DEPRECATED_GETS", int.class);
            MH_GLIBC_USE_DEPRECATED_SCANF = find("__GLIBC_USE_DEPRECATED_SCANF", int.class);
            MH_GLIBC_USE_C2X_STRTOL = find("__GLIBC_USE_C2X_STRTOL", int.class);
            MH_STDC_PREDEF_H = find("_STDC_PREDEF_H", int.class);
            MH_STDC_IEC_559 = find("__STDC_IEC_559__", int.class);
            MH_STDC_IEC_559_COMPLEX = find("__STDC_IEC_559_COMPLEX__", int.class);
            MH_GNU_LIBRARY = find("__GNU_LIBRARY__", int.class);
            MH_GLIBC = find("__GLIBC__", int.class);
            MH_GLIBC_MINOR = find("__GLIBC_MINOR__", int.class);
            MH_SYS_CDEFS_H = find("_SYS_CDEFS_H", int.class);
            MH_GLIBC_C99_FLEXARR_AVAILABLE = find("__glibc_c99_flexarr_available", int.class);
            MH_LDOUBLE_REDIRECTS_TO_FLOAT128_ABI = find("__LDOUBLE_REDIRECTS_TO_FLOAT128_ABI", int.class);
            MH_HAVE_GENERIC_SELECTION = find("__HAVE_GENERIC_SELECTION", int.class);
            MH_GLIBC_USE_LIB_EXT2 = find("__GLIBC_USE_LIB_EXT2", int.class);
            MH_GLIBC_USE_IEC_60559_BFP_EXT = find("__GLIBC_USE_IEC_60559_BFP_EXT", int.class);
            MH_GLIBC_USE_IEC_60559_BFP_EXT_C2X = find("__GLIBC_USE_IEC_60559_BFP_EXT_C2X", int.class);
            MH_GLIBC_USE_IEC_60559_EXT = find("__GLIBC_USE_IEC_60559_EXT", int.class);
            MH_GLIBC_USE_IEC_60559_FUNCS_EXT = find("__GLIBC_USE_IEC_60559_FUNCS_EXT", int.class);
            MH_GLIBC_USE_IEC_60559_FUNCS_EXT_C2X = find("__GLIBC_USE_IEC_60559_FUNCS_EXT_C2X", int.class);
            MH_GLIBC_USE_IEC_60559_TYPES_EXT = find("__GLIBC_USE_IEC_60559_TYPES_EXT", int.class);
            MH_STDLIB_H = find("_STDLIB_H", int.class);
            MH_WNOHANG = find("WNOHANG", int.class);
            MH_WUNTRACED = find("WUNTRACED", int.class);
            MH_WSTOPPED = find("WSTOPPED", int.class);
            MH_WEXITED = find("WEXITED", int.class);
            MH_WCONTINUED = find("WCONTINUED", int.class);
            MH_WNOWAIT = find("WNOWAIT", int.class);
            MH_WNOTHREAD = find("__WNOTHREAD", int.class);
            MH_WALL = find("__WALL", int.class);
            MH_W_CONTINUED = find("__W_CONTINUED", int.class);
            MH_WCOREFLAG = find("__WCOREFLAG", int.class);
            MH_HAVE_FLOAT128 = find("__HAVE_FLOAT128", int.class);
            MH_HAVE_DISTINCT_FLOAT128 = find("__HAVE_DISTINCT_FLOAT128", int.class);
            MH_HAVE_FLOAT64X = find("__HAVE_FLOAT64X", int.class);
            MH_HAVE_FLOAT64X_LONG_DOUBLE = find("__HAVE_FLOAT64X_LONG_DOUBLE", int.class);
            MH_HAVE_FLOAT16 = find("__HAVE_FLOAT16", int.class);
            MH_HAVE_FLOAT32 = find("__HAVE_FLOAT32", int.class);
            MH_HAVE_FLOAT64 = find("__HAVE_FLOAT64", int.class);
            MH_HAVE_FLOAT32X = find("__HAVE_FLOAT32X", int.class);
            MH_HAVE_FLOAT128X = find("__HAVE_FLOAT128X", int.class);
            MH_HAVE_DISTINCT_FLOAT32 = find("__HAVE_DISTINCT_FLOAT32", int.class);
            MH_HAVE_DISTINCT_FLOAT64 = find("__HAVE_DISTINCT_FLOAT64", int.class);
            MH_HAVE_DISTINCT_FLOAT32X = find("__HAVE_DISTINCT_FLOAT32X", int.class);
            MH_HAVE_DISTINCT_FLOAT64X = find("__HAVE_DISTINCT_FLOAT64X", int.class);
            MH_HAVE_FLOATN_NOT_TYPEDEF = find("__HAVE_FLOATN_NOT_TYPEDEF", int.class);
            MH_LDIV_T_DEFINED = find("__ldiv_t_defined", int.class);
            MH_LLDIV_T_DEFINED = find("__lldiv_t_defined", int.class);
            MH_RAND_MAX = find("RAND_MAX", int.class);
            MH_EXIT_FAILURE = find("EXIT_FAILURE", int.class);
            MH_EXIT_SUCCESS = find("EXIT_SUCCESS", int.class);
            MH_SYS_TYPES_H = find("_SYS_TYPES_H", int.class);
            MH_BITS_TYPES_H = find("_BITS_TYPES_H", int.class);
            MH_BITS_TYPESIZES_H = find("_BITS_TYPESIZES_H", int.class);
            MH_OFF_T_MATCHES_OFF64_T = find("__OFF_T_MATCHES_OFF64_T", int.class);
            MH_INO_T_MATCHES_INO64_T = find("__INO_T_MATCHES_INO64_T", int.class);
            MH_RLIM_T_MATCHES_RLIM64_T = find("__RLIM_T_MATCHES_RLIM64_T", int.class);
            MH_STATFS_MATCHES_STATFS64 = find("__STATFS_MATCHES_STATFS64", int.class);
            MH_KERNEL_OLD_TIMEVAL_MATCHES_TIMEVAL64 = find("__KERNEL_OLD_TIMEVAL_MATCHES_TIMEVAL64", int.class);
            MH_FD_SETSIZE = find("__FD_SETSIZE", int.class);
            MH_BITS_TIME64_H = find("_BITS_TIME64_H", int.class);
            MH_CLOCK_T_DEFINED = find("__clock_t_defined", int.class);
            MH_CLOCKID_T_DEFINED = find("__clockid_t_defined", int.class);
            MH_TIME_T_DEFINED = find("__time_t_defined", int.class);
            MH_TIMER_T_DEFINED = find("__timer_t_defined", int.class);
            MH_BITS_STDINT_INTN_H = find("_BITS_STDINT_INTN_H", int.class);
            MH_BIT_TYPES_DEFINED = find("__BIT_TYPES_DEFINED__", int.class);
            MH_ENDIAN_H = find("_ENDIAN_H", int.class);
            MH_BITS_ENDIAN_H = find("_BITS_ENDIAN_H", int.class);
            MH_LITTLE_ENDIAN = find("__LITTLE_ENDIAN", int.class);
            MH_BIG_ENDIAN = find("__BIG_ENDIAN", int.class);
            MH_PDP_ENDIAN = find("__PDP_ENDIAN", int.class);
            MH_BITS_ENDIANNESS_H = find("_BITS_ENDIANNESS_H", int.class);
            MH_BITS_BYTESWAP_H = find("_BITS_BYTESWAP_H", int.class);
            MH_BITS_UINTN_IDENTITY_H = find("_BITS_UINTN_IDENTITY_H", int.class);
            MH_SYS_SELECT_H = find("_SYS_SELECT_H", int.class);
            MH_SIGSET_T_DEFINED = find("__sigset_t_defined", int.class);
            MH_TIMEVAL_DEFINED = find("__timeval_defined", int.class);
            MH_STRUCT_TIMESPEC = find("_STRUCT_TIMESPEC", int.class);
            MH_BITS_PTHREADTYPES_COMMON_H = find("_BITS_PTHREADTYPES_COMMON_H", int.class);
            MH_THREAD_SHARED_TYPES_H = find("_THREAD_SHARED_TYPES_H", int.class);
            MH_BITS_PTHREADTYPES_ARCH_H = find("_BITS_PTHREADTYPES_ARCH_H", int.class);
            MH_SIZEOF_PTHREAD_MUTEX_T = find("__SIZEOF_PTHREAD_MUTEX_T", int.class);
            MH_SIZEOF_PTHREAD_ATTR_T = find("__SIZEOF_PTHREAD_ATTR_T", int.class);
            MH_SIZEOF_PTHREAD_RWLOCK_T = find("__SIZEOF_PTHREAD_RWLOCK_T", int.class);
            MH_SIZEOF_PTHREAD_BARRIER_T = find("__SIZEOF_PTHREAD_BARRIER_T", int.class);
            MH_SIZEOF_PTHREAD_MUTEXATTR_T = find("__SIZEOF_PTHREAD_MUTEXATTR_T", int.class);
            MH_SIZEOF_PTHREAD_COND_T = find("__SIZEOF_PTHREAD_COND_T", int.class);
            MH_SIZEOF_PTHREAD_CONDATTR_T = find("__SIZEOF_PTHREAD_CONDATTR_T", int.class);
            MH_SIZEOF_PTHREAD_RWLOCKATTR_T = find("__SIZEOF_PTHREAD_RWLOCKATTR_T", int.class);
            MH_SIZEOF_PTHREAD_BARRIERATTR_T = find("__SIZEOF_PTHREAD_BARRIERATTR_T", int.class);
            MH_THREAD_MUTEX_INTERNAL_H = find("_THREAD_MUTEX_INTERNAL_H", int.class);
            MH_PTHREAD_MUTEX_HAVE_PREV = find("__PTHREAD_MUTEX_HAVE_PREV", int.class);
            MH_HAVE_PTHREAD_ATTR_T = find("__have_pthread_attr_t", int.class);
            MH_ALLOCA_H = find("_ALLOCA_H", int.class);
            MH_HS_SUCCESS = find("HS_SUCCESS", int.class);
            MH_HS_FLAG_CASELESS = find("HS_FLAG_CASELESS", int.class);
            MH_HS_FLAG_DOTALL = find("HS_FLAG_DOTALL", int.class);
            MH_HS_FLAG_MULTILINE = find("HS_FLAG_MULTILINE", int.class);
            MH_HS_FLAG_SINGLEMATCH = find("HS_FLAG_SINGLEMATCH", int.class);
            MH_HS_FLAG_ALLOWEMPTY = find("HS_FLAG_ALLOWEMPTY", int.class);
            MH_HS_FLAG_UTF8 = find("HS_FLAG_UTF8", int.class);
            MH_HS_FLAG_UCP = find("HS_FLAG_UCP", int.class);
            MH_HS_FLAG_PREFILTER = find("HS_FLAG_PREFILTER", int.class);
            MH_HS_FLAG_SOM_LEFTMOST = find("HS_FLAG_SOM_LEFTMOST", int.class);
            MH_HS_FLAG_COMBINATION = find("HS_FLAG_COMBINATION", int.class);
            MH_HS_FLAG_QUIET = find("HS_FLAG_QUIET", int.class);
            MH_HS_TUNE_FAMILY_GENERIC = find("HS_TUNE_FAMILY_GENERIC", int.class);
            MH_HS_TUNE_FAMILY_SNB = find("HS_TUNE_FAMILY_SNB", int.class);
            MH_HS_TUNE_FAMILY_IVB = find("HS_TUNE_FAMILY_IVB", int.class);
            MH_HS_TUNE_FAMILY_HSW = find("HS_TUNE_FAMILY_HSW", int.class);
            MH_HS_TUNE_FAMILY_SLM = find("HS_TUNE_FAMILY_SLM", int.class);
            MH_HS_TUNE_FAMILY_BDW = find("HS_TUNE_FAMILY_BDW", int.class);
            MH_HS_TUNE_FAMILY_SKL = find("HS_TUNE_FAMILY_SKL", int.class);
            MH_HS_TUNE_FAMILY_SKX = find("HS_TUNE_FAMILY_SKX", int.class);
            MH_HS_TUNE_FAMILY_GLM = find("HS_TUNE_FAMILY_GLM", int.class);
            MH_HS_TUNE_FAMILY_ICL = find("HS_TUNE_FAMILY_ICL", int.class);
            MH_HS_TUNE_FAMILY_ICX = find("HS_TUNE_FAMILY_ICX", int.class);
            MH_HS_MODE_BLOCK = find("HS_MODE_BLOCK", int.class);
            MH_HS_MODE_NOSTREAM = find("HS_MODE_NOSTREAM", int.class);
            MH_HS_MODE_STREAM = find("HS_MODE_STREAM", int.class);
            MH_HS_MODE_VECTORED = find("HS_MODE_VECTORED", int.class);
            MH_HS_MAJOR = find("HS_MAJOR", int.class);
            MH_HS_MINOR = find("HS_MINOR", int.class);
            MH_HS_PATCH = find("HS_PATCH", int.class);
            MH_CTYPE_GET_MB_CUR_MAX_DESCRIPTOR = find("__ctype_get_mb_cur_max$descriptor", FunctionDescriptor.class);
            MH_CTYPE_GET_MB_CUR_MAX_HANDLE = find("__ctype_get_mb_cur_max$handle", MethodHandle.class);
            MH_CTYPE_GET_MB_CUR_MAX_ADDRESS = find("__ctype_get_mb_cur_max$address", MemorySegment.class);
            MH_CTYPE_GET_MB_CUR_MAX = find("__ctype_get_mb_cur_max", long.class);
            MH_ATOF_DESCRIPTOR = find("atof$descriptor", FunctionDescriptor.class);
            MH_ATOF_HANDLE = find("atof$handle", MethodHandle.class);
            MH_ATOF_ADDRESS = find("atof$address", MemorySegment.class);
            MH_ATOF = find("atof", double.class, MemorySegment.class);
            MH_ATOI_DESCRIPTOR = find("atoi$descriptor", FunctionDescriptor.class);
            MH_ATOI_HANDLE = find("atoi$handle", MethodHandle.class);
            MH_ATOI_ADDRESS = find("atoi$address", MemorySegment.class);
            MH_ATOI = find("atoi", int.class, MemorySegment.class);
            MH_ATOL_DESCRIPTOR = find("atol$descriptor", FunctionDescriptor.class);
            MH_ATOL_HANDLE = find("atol$handle", MethodHandle.class);
            MH_ATOL_ADDRESS = find("atol$address", MemorySegment.class);
            MH_ATOL = find("atol", long.class, MemorySegment.class);
            MH_ATOLL_DESCRIPTOR = find("atoll$descriptor", FunctionDescriptor.class);
            MH_ATOLL_HANDLE = find("atoll$handle", MethodHandle.class);
            MH_ATOLL_ADDRESS = find("atoll$address", MemorySegment.class);
            MH_ATOLL = find("atoll", long.class, MemorySegment.class);
            MH_STRTOD_DESCRIPTOR = find("strtod$descriptor", FunctionDescriptor.class);
            MH_STRTOD_HANDLE = find("strtod$handle", MethodHandle.class);
            MH_STRTOD_ADDRESS = find("strtod$address", MemorySegment.class);
            MH_STRTOD = find("strtod", double.class, MemorySegment.class, MemorySegment.class);
            MH_STRTOF_DESCRIPTOR = find("strtof$descriptor", FunctionDescriptor.class);
            MH_STRTOF_HANDLE = find("strtof$handle", MethodHandle.class);
            MH_STRTOF_ADDRESS = find("strtof$address", MemorySegment.class);
            MH_STRTOF = find("strtof", float.class, MemorySegment.class, MemorySegment.class);
            MH_STRTOL_DESCRIPTOR = find("strtol$descriptor", FunctionDescriptor.class);
            MH_STRTOL_HANDLE = find("strtol$handle", MethodHandle.class);
            MH_STRTOL_ADDRESS = find("strtol$address", MemorySegment.class);
            MH_STRTOL = find("strtol", long.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_STRTOUL_DESCRIPTOR = find("strtoul$descriptor", FunctionDescriptor.class);
            MH_STRTOUL_HANDLE = find("strtoul$handle", MethodHandle.class);
            MH_STRTOUL_ADDRESS = find("strtoul$address", MemorySegment.class);
            MH_STRTOUL = find("strtoul", long.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_STRTOQ_DESCRIPTOR = find("strtoq$descriptor", FunctionDescriptor.class);
            MH_STRTOQ_HANDLE = find("strtoq$handle", MethodHandle.class);
            MH_STRTOQ_ADDRESS = find("strtoq$address", MemorySegment.class);
            MH_STRTOQ = find("strtoq", long.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_STRTOUQ_DESCRIPTOR = find("strtouq$descriptor", FunctionDescriptor.class);
            MH_STRTOUQ_HANDLE = find("strtouq$handle", MethodHandle.class);
            MH_STRTOUQ_ADDRESS = find("strtouq$address", MemorySegment.class);
            MH_STRTOUQ = find("strtouq", long.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_STRTOLL_DESCRIPTOR = find("strtoll$descriptor", FunctionDescriptor.class);
            MH_STRTOLL_HANDLE = find("strtoll$handle", MethodHandle.class);
            MH_STRTOLL_ADDRESS = find("strtoll$address", MemorySegment.class);
            MH_STRTOLL = find("strtoll", long.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_STRTOULL_DESCRIPTOR = find("strtoull$descriptor", FunctionDescriptor.class);
            MH_STRTOULL_HANDLE = find("strtoull$handle", MethodHandle.class);
            MH_STRTOULL_ADDRESS = find("strtoull$address", MemorySegment.class);
            MH_STRTOULL = find("strtoull", long.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_L64A_DESCRIPTOR = find("l64a$descriptor", FunctionDescriptor.class);
            MH_L64A_HANDLE = find("l64a$handle", MethodHandle.class);
            MH_L64A_ADDRESS = find("l64a$address", MemorySegment.class);
            MH_L64A = find("l64a", MemorySegment.class, long.class);
            MH_A64L_DESCRIPTOR = find("a64l$descriptor", FunctionDescriptor.class);
            MH_A64L_HANDLE = find("a64l$handle", MethodHandle.class);
            MH_A64L_ADDRESS = find("a64l$address", MemorySegment.class);
            MH_A64L = find("a64l", long.class, MemorySegment.class);
            MH_SELECT_DESCRIPTOR = find("select$descriptor", FunctionDescriptor.class);
            MH_SELECT_HANDLE = find("select$handle", MethodHandle.class);
            MH_SELECT_ADDRESS = find("select$address", MemorySegment.class);
            MH_SELECT = find("select", int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_PSELECT_DESCRIPTOR = find("pselect$descriptor", FunctionDescriptor.class);
            MH_PSELECT_HANDLE = find("pselect$handle", MethodHandle.class);
            MH_PSELECT_ADDRESS = find("pselect$address", MemorySegment.class);
            MH_PSELECT = find("pselect", int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_RANDOM_DESCRIPTOR = find("random$descriptor", FunctionDescriptor.class);
            MH_RANDOM_HANDLE = find("random$handle", MethodHandle.class);
            MH_RANDOM_ADDRESS = find("random$address", MemorySegment.class);
            MH_RANDOM = find("random", long.class);
            MH_SRANDOM_DESCRIPTOR = find("srandom$descriptor", FunctionDescriptor.class);
            MH_SRANDOM_HANDLE = find("srandom$handle", MethodHandle.class);
            MH_SRANDOM_ADDRESS = find("srandom$address", MemorySegment.class);
            MH_SRANDOM = find("srandom", void.class, int.class);
            MH_INITSTATE_DESCRIPTOR = find("initstate$descriptor", FunctionDescriptor.class);
            MH_INITSTATE_HANDLE = find("initstate$handle", MethodHandle.class);
            MH_INITSTATE_ADDRESS = find("initstate$address", MemorySegment.class);
            MH_INITSTATE = find("initstate", MemorySegment.class, int.class, MemorySegment.class, long.class);
            MH_SETSTATE_DESCRIPTOR = find("setstate$descriptor", FunctionDescriptor.class);
            MH_SETSTATE_HANDLE = find("setstate$handle", MethodHandle.class);
            MH_SETSTATE_ADDRESS = find("setstate$address", MemorySegment.class);
            MH_SETSTATE = find("setstate", MemorySegment.class, MemorySegment.class);
            MH_RANDOM_R_DESCRIPTOR = find("random_r$descriptor", FunctionDescriptor.class);
            MH_RANDOM_R_HANDLE = find("random_r$handle", MethodHandle.class);
            MH_RANDOM_R_ADDRESS = find("random_r$address", MemorySegment.class);
            MH_RANDOM_R = find("random_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_SRANDOM_R_DESCRIPTOR = find("srandom_r$descriptor", FunctionDescriptor.class);
            MH_SRANDOM_R_HANDLE = find("srandom_r$handle", MethodHandle.class);
            MH_SRANDOM_R_ADDRESS = find("srandom_r$address", MemorySegment.class);
            MH_SRANDOM_R = find("srandom_r", int.class, int.class, MemorySegment.class);
            MH_INITSTATE_R_DESCRIPTOR = find("initstate_r$descriptor", FunctionDescriptor.class);
            MH_INITSTATE_R_HANDLE = find("initstate_r$handle", MethodHandle.class);
            MH_INITSTATE_R_ADDRESS = find("initstate_r$address", MemorySegment.class);
            MH_INITSTATE_R = find("initstate_r", int.class, int.class, MemorySegment.class, long.class, MemorySegment.class);
            MH_SETSTATE_R_DESCRIPTOR = find("setstate_r$descriptor", FunctionDescriptor.class);
            MH_SETSTATE_R_HANDLE = find("setstate_r$handle", MethodHandle.class);
            MH_SETSTATE_R_ADDRESS = find("setstate_r$address", MemorySegment.class);
            MH_SETSTATE_R = find("setstate_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_RAND_DESCRIPTOR = find("rand$descriptor", FunctionDescriptor.class);
            MH_RAND_HANDLE = find("rand$handle", MethodHandle.class);
            MH_RAND_ADDRESS = find("rand$address", MemorySegment.class);
            MH_RAND = find("rand", int.class);
            MH_SRAND_DESCRIPTOR = find("srand$descriptor", FunctionDescriptor.class);
            MH_SRAND_HANDLE = find("srand$handle", MethodHandle.class);
            MH_SRAND_ADDRESS = find("srand$address", MemorySegment.class);
            MH_SRAND = find("srand", void.class, int.class);
            MH_RAND_R_DESCRIPTOR = find("rand_r$descriptor", FunctionDescriptor.class);
            MH_RAND_R_HANDLE = find("rand_r$handle", MethodHandle.class);
            MH_RAND_R_ADDRESS = find("rand_r$address", MemorySegment.class);
            MH_RAND_R = find("rand_r", int.class, MemorySegment.class);
            MH_DRAND48_DESCRIPTOR = find("drand48$descriptor", FunctionDescriptor.class);
            MH_DRAND48_HANDLE = find("drand48$handle", MethodHandle.class);
            MH_DRAND48_ADDRESS = find("drand48$address", MemorySegment.class);
            MH_DRAND48 = find("drand48", double.class);
            MH_ERAND48_DESCRIPTOR = find("erand48$descriptor", FunctionDescriptor.class);
            MH_ERAND48_HANDLE = find("erand48$handle", MethodHandle.class);
            MH_ERAND48_ADDRESS = find("erand48$address", MemorySegment.class);
            MH_ERAND48 = find("erand48", double.class, MemorySegment.class);
            MH_LRAND48_DESCRIPTOR = find("lrand48$descriptor", FunctionDescriptor.class);
            MH_LRAND48_HANDLE = find("lrand48$handle", MethodHandle.class);
            MH_LRAND48_ADDRESS = find("lrand48$address", MemorySegment.class);
            MH_LRAND48 = find("lrand48", long.class);
            MH_NRAND48_DESCRIPTOR = find("nrand48$descriptor", FunctionDescriptor.class);
            MH_NRAND48_HANDLE = find("nrand48$handle", MethodHandle.class);
            MH_NRAND48_ADDRESS = find("nrand48$address", MemorySegment.class);
            MH_NRAND48 = find("nrand48", long.class, MemorySegment.class);
            MH_MRAND48_DESCRIPTOR = find("mrand48$descriptor", FunctionDescriptor.class);
            MH_MRAND48_HANDLE = find("mrand48$handle", MethodHandle.class);
            MH_MRAND48_ADDRESS = find("mrand48$address", MemorySegment.class);
            MH_MRAND48 = find("mrand48", long.class);
            MH_JRAND48_DESCRIPTOR = find("jrand48$descriptor", FunctionDescriptor.class);
            MH_JRAND48_HANDLE = find("jrand48$handle", MethodHandle.class);
            MH_JRAND48_ADDRESS = find("jrand48$address", MemorySegment.class);
            MH_JRAND48 = find("jrand48", long.class, MemorySegment.class);
            MH_SRAND48_DESCRIPTOR = find("srand48$descriptor", FunctionDescriptor.class);
            MH_SRAND48_HANDLE = find("srand48$handle", MethodHandle.class);
            MH_SRAND48_ADDRESS = find("srand48$address", MemorySegment.class);
            MH_SRAND48 = find("srand48", void.class, long.class);
            MH_SEED48_DESCRIPTOR = find("seed48$descriptor", FunctionDescriptor.class);
            MH_SEED48_HANDLE = find("seed48$handle", MethodHandle.class);
            MH_SEED48_ADDRESS = find("seed48$address", MemorySegment.class);
            MH_SEED48 = find("seed48", MemorySegment.class, MemorySegment.class);
            MH_LCONG48_DESCRIPTOR = find("lcong48$descriptor", FunctionDescriptor.class);
            MH_LCONG48_HANDLE = find("lcong48$handle", MethodHandle.class);
            MH_LCONG48_ADDRESS = find("lcong48$address", MemorySegment.class);
            MH_LCONG48 = find("lcong48", void.class, MemorySegment.class);
            MH_DRAND48_R_DESCRIPTOR = find("drand48_r$descriptor", FunctionDescriptor.class);
            MH_DRAND48_R_HANDLE = find("drand48_r$handle", MethodHandle.class);
            MH_DRAND48_R_ADDRESS = find("drand48_r$address", MemorySegment.class);
            MH_DRAND48_R = find("drand48_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_ERAND48_R_DESCRIPTOR = find("erand48_r$descriptor", FunctionDescriptor.class);
            MH_ERAND48_R_HANDLE = find("erand48_r$handle", MethodHandle.class);
            MH_ERAND48_R_ADDRESS = find("erand48_r$address", MemorySegment.class);
            MH_ERAND48_R = find("erand48_r", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_LRAND48_R_DESCRIPTOR = find("lrand48_r$descriptor", FunctionDescriptor.class);
            MH_LRAND48_R_HANDLE = find("lrand48_r$handle", MethodHandle.class);
            MH_LRAND48_R_ADDRESS = find("lrand48_r$address", MemorySegment.class);
            MH_LRAND48_R = find("lrand48_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_NRAND48_R_DESCRIPTOR = find("nrand48_r$descriptor", FunctionDescriptor.class);
            MH_NRAND48_R_HANDLE = find("nrand48_r$handle", MethodHandle.class);
            MH_NRAND48_R_ADDRESS = find("nrand48_r$address", MemorySegment.class);
            MH_NRAND48_R = find("nrand48_r", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_MRAND48_R_DESCRIPTOR = find("mrand48_r$descriptor", FunctionDescriptor.class);
            MH_MRAND48_R_HANDLE = find("mrand48_r$handle", MethodHandle.class);
            MH_MRAND48_R_ADDRESS = find("mrand48_r$address", MemorySegment.class);
            MH_MRAND48_R = find("mrand48_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_JRAND48_R_DESCRIPTOR = find("jrand48_r$descriptor", FunctionDescriptor.class);
            MH_JRAND48_R_HANDLE = find("jrand48_r$handle", MethodHandle.class);
            MH_JRAND48_R_ADDRESS = find("jrand48_r$address", MemorySegment.class);
            MH_JRAND48_R = find("jrand48_r", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_SRAND48_R_DESCRIPTOR = find("srand48_r$descriptor", FunctionDescriptor.class);
            MH_SRAND48_R_HANDLE = find("srand48_r$handle", MethodHandle.class);
            MH_SRAND48_R_ADDRESS = find("srand48_r$address", MemorySegment.class);
            MH_SRAND48_R = find("srand48_r", int.class, long.class, MemorySegment.class);
            MH_SEED48_R_DESCRIPTOR = find("seed48_r$descriptor", FunctionDescriptor.class);
            MH_SEED48_R_HANDLE = find("seed48_r$handle", MethodHandle.class);
            MH_SEED48_R_ADDRESS = find("seed48_r$address", MemorySegment.class);
            MH_SEED48_R = find("seed48_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_LCONG48_R_DESCRIPTOR = find("lcong48_r$descriptor", FunctionDescriptor.class);
            MH_LCONG48_R_HANDLE = find("lcong48_r$handle", MethodHandle.class);
            MH_LCONG48_R_ADDRESS = find("lcong48_r$address", MemorySegment.class);
            MH_LCONG48_R = find("lcong48_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_ARC4RANDOM_DESCRIPTOR = find("arc4random$descriptor", FunctionDescriptor.class);
            MH_ARC4RANDOM_HANDLE = find("arc4random$handle", MethodHandle.class);
            MH_ARC4RANDOM_ADDRESS = find("arc4random$address", MemorySegment.class);
            MH_ARC4RANDOM = find("arc4random", int.class);
            MH_ARC4RANDOM_BUF_DESCRIPTOR = find("arc4random_buf$descriptor", FunctionDescriptor.class);
            MH_ARC4RANDOM_BUF_HANDLE = find("arc4random_buf$handle", MethodHandle.class);
            MH_ARC4RANDOM_BUF_ADDRESS = find("arc4random_buf$address", MemorySegment.class);
            MH_ARC4RANDOM_BUF = find("arc4random_buf", void.class, MemorySegment.class, long.class);
            MH_ARC4RANDOM_UNIFORM_DESCRIPTOR = find("arc4random_uniform$descriptor", FunctionDescriptor.class);
            MH_ARC4RANDOM_UNIFORM_HANDLE = find("arc4random_uniform$handle", MethodHandle.class);
            MH_ARC4RANDOM_UNIFORM_ADDRESS = find("arc4random_uniform$address", MemorySegment.class);
            MH_ARC4RANDOM_UNIFORM = find("arc4random_uniform", int.class, int.class);
            MH_MALLOC_DESCRIPTOR = find("malloc$descriptor", FunctionDescriptor.class);
            MH_MALLOC_HANDLE = find("malloc$handle", MethodHandle.class);
            MH_MALLOC_ADDRESS = find("malloc$address", MemorySegment.class);
            MH_MALLOC = find("malloc", MemorySegment.class, long.class);
            MH_CALLOC_DESCRIPTOR = find("calloc$descriptor", FunctionDescriptor.class);
            MH_CALLOC_HANDLE = find("calloc$handle", MethodHandle.class);
            MH_CALLOC_ADDRESS = find("calloc$address", MemorySegment.class);
            MH_CALLOC = find("calloc", MemorySegment.class, long.class, long.class);
            MH_REALLOC_DESCRIPTOR = find("realloc$descriptor", FunctionDescriptor.class);
            MH_REALLOC_HANDLE = find("realloc$handle", MethodHandle.class);
            MH_REALLOC_ADDRESS = find("realloc$address", MemorySegment.class);
            MH_REALLOC = find("realloc", MemorySegment.class, MemorySegment.class, long.class);
            MH_FREE_DESCRIPTOR = find("free$descriptor", FunctionDescriptor.class);
            MH_FREE_HANDLE = find("free$handle", MethodHandle.class);
            MH_FREE_ADDRESS = find("free$address", MemorySegment.class);
            MH_FREE = find("free", void.class, MemorySegment.class);
            MH_REALLOCARRAY_DESCRIPTOR = find("reallocarray$descriptor", FunctionDescriptor.class);
            MH_REALLOCARRAY_HANDLE = find("reallocarray$handle", MethodHandle.class);
            MH_REALLOCARRAY_ADDRESS = find("reallocarray$address", MemorySegment.class);
            MH_REALLOCARRAY = find("reallocarray", MemorySegment.class, MemorySegment.class, long.class, long.class);
            MH_ALLOCA_DESCRIPTOR = find("alloca$descriptor", FunctionDescriptor.class);
            MH_ALLOCA_HANDLE = find("alloca$handle", MethodHandle.class);
            MH_ALLOCA_ADDRESS = find("alloca$address", MemorySegment.class);
            MH_ALLOCA = find("alloca", MemorySegment.class, long.class);
            MH_VALLOC_DESCRIPTOR = find("valloc$descriptor", FunctionDescriptor.class);
            MH_VALLOC_HANDLE = find("valloc$handle", MethodHandle.class);
            MH_VALLOC_ADDRESS = find("valloc$address", MemorySegment.class);
            MH_VALLOC = find("valloc", MemorySegment.class, long.class);
            MH_POSIX_MEMALIGN_DESCRIPTOR = find("posix_memalign$descriptor", FunctionDescriptor.class);
            MH_POSIX_MEMALIGN_HANDLE = find("posix_memalign$handle", MethodHandle.class);
            MH_POSIX_MEMALIGN_ADDRESS = find("posix_memalign$address", MemorySegment.class);
            MH_POSIX_MEMALIGN = find("posix_memalign", int.class, MemorySegment.class, long.class, long.class);
            MH_ALIGNED_ALLOC_DESCRIPTOR = find("aligned_alloc$descriptor", FunctionDescriptor.class);
            MH_ALIGNED_ALLOC_HANDLE = find("aligned_alloc$handle", MethodHandle.class);
            MH_ALIGNED_ALLOC_ADDRESS = find("aligned_alloc$address", MemorySegment.class);
            MH_ALIGNED_ALLOC = find("aligned_alloc", MemorySegment.class, long.class, long.class);
            MH_ABORT_DESCRIPTOR = find("abort$descriptor", FunctionDescriptor.class);
            MH_ABORT_HANDLE = find("abort$handle", MethodHandle.class);
            MH_ABORT_ADDRESS = find("abort$address", MemorySegment.class);
            MH_ABORT = find("abort", void.class);
            MH_ATEXIT_DESCRIPTOR = find("atexit$descriptor", FunctionDescriptor.class);
            MH_ATEXIT_HANDLE = find("atexit$handle", MethodHandle.class);
            MH_ATEXIT_ADDRESS = find("atexit$address", MemorySegment.class);
            MH_ATEXIT = find("atexit", int.class, MemorySegment.class);
            MH_AT_QUICK_EXIT_DESCRIPTOR = find("at_quick_exit$descriptor", FunctionDescriptor.class);
            MH_AT_QUICK_EXIT_HANDLE = find("at_quick_exit$handle", MethodHandle.class);
            MH_AT_QUICK_EXIT_ADDRESS = find("at_quick_exit$address", MemorySegment.class);
            MH_AT_QUICK_EXIT = find("at_quick_exit", int.class, MemorySegment.class);
            MH_ON_EXIT_DESCRIPTOR = find("on_exit$descriptor", FunctionDescriptor.class);
            MH_ON_EXIT_HANDLE = find("on_exit$handle", MethodHandle.class);
            MH_ON_EXIT_ADDRESS = find("on_exit$address", MemorySegment.class);
            MH_ON_EXIT = find("on_exit", int.class, MemorySegment.class, MemorySegment.class);
            MH_EXIT_DESCRIPTOR = find("exit$descriptor", FunctionDescriptor.class);
            MH_EXIT_HANDLE = find("exit$handle", MethodHandle.class);
            MH_EXIT_ADDRESS = find("exit$address", MemorySegment.class);
            MH_EXIT = find("exit", void.class, int.class);
            MH_QUICK_EXIT_DESCRIPTOR = find("quick_exit$descriptor", FunctionDescriptor.class);
            MH_QUICK_EXIT_HANDLE = find("quick_exit$handle", MethodHandle.class);
            MH_QUICK_EXIT_ADDRESS = find("quick_exit$address", MemorySegment.class);
            MH_QUICK_EXIT = find("quick_exit", void.class, int.class);
            MH_EXIT_DESCRIPTOR_1 = find("_Exit$descriptor", FunctionDescriptor.class);
            MH_EXIT_HANDLE_1 = find("_Exit$handle", MethodHandle.class);
            MH_EXIT_ADDRESS_1 = find("_Exit$address", MemorySegment.class);
            MH_EXIT_1 = find("_Exit", void.class, int.class);
            MH_GETENV_DESCRIPTOR = find("getenv$descriptor", FunctionDescriptor.class);
            MH_GETENV_HANDLE = find("getenv$handle", MethodHandle.class);
            MH_GETENV_ADDRESS = find("getenv$address", MemorySegment.class);
            MH_GETENV = find("getenv", MemorySegment.class, MemorySegment.class);
            MH_PUTENV_DESCRIPTOR = find("putenv$descriptor", FunctionDescriptor.class);
            MH_PUTENV_HANDLE = find("putenv$handle", MethodHandle.class);
            MH_PUTENV_ADDRESS = find("putenv$address", MemorySegment.class);
            MH_PUTENV = find("putenv", int.class, MemorySegment.class);
            MH_SETENV_DESCRIPTOR = find("setenv$descriptor", FunctionDescriptor.class);
            MH_SETENV_HANDLE = find("setenv$handle", MethodHandle.class);
            MH_SETENV_ADDRESS = find("setenv$address", MemorySegment.class);
            MH_SETENV = find("setenv", int.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_UNSETENV_DESCRIPTOR = find("unsetenv$descriptor", FunctionDescriptor.class);
            MH_UNSETENV_HANDLE = find("unsetenv$handle", MethodHandle.class);
            MH_UNSETENV_ADDRESS = find("unsetenv$address", MemorySegment.class);
            MH_UNSETENV = find("unsetenv", int.class, MemorySegment.class);
            MH_CLEARENV_DESCRIPTOR = find("clearenv$descriptor", FunctionDescriptor.class);
            MH_CLEARENV_HANDLE = find("clearenv$handle", MethodHandle.class);
            MH_CLEARENV_ADDRESS = find("clearenv$address", MemorySegment.class);
            MH_CLEARENV = find("clearenv", int.class);
            MH_MKTEMP_DESCRIPTOR = find("mktemp$descriptor", FunctionDescriptor.class);
            MH_MKTEMP_HANDLE = find("mktemp$handle", MethodHandle.class);
            MH_MKTEMP_ADDRESS = find("mktemp$address", MemorySegment.class);
            MH_MKTEMP = find("mktemp", MemorySegment.class, MemorySegment.class);
            MH_MKSTEMP_DESCRIPTOR = find("mkstemp$descriptor", FunctionDescriptor.class);
            MH_MKSTEMP_HANDLE = find("mkstemp$handle", MethodHandle.class);
            MH_MKSTEMP_ADDRESS = find("mkstemp$address", MemorySegment.class);
            MH_MKSTEMP = find("mkstemp", int.class, MemorySegment.class);
            MH_MKSTEMPS_DESCRIPTOR = find("mkstemps$descriptor", FunctionDescriptor.class);
            MH_MKSTEMPS_HANDLE = find("mkstemps$handle", MethodHandle.class);
            MH_MKSTEMPS_ADDRESS = find("mkstemps$address", MemorySegment.class);
            MH_MKSTEMPS = find("mkstemps", int.class, MemorySegment.class, int.class);
            MH_MKDTEMP_DESCRIPTOR = find("mkdtemp$descriptor", FunctionDescriptor.class);
            MH_MKDTEMP_HANDLE = find("mkdtemp$handle", MethodHandle.class);
            MH_MKDTEMP_ADDRESS = find("mkdtemp$address", MemorySegment.class);
            MH_MKDTEMP = find("mkdtemp", MemorySegment.class, MemorySegment.class);
            MH_SYSTEM_DESCRIPTOR = find("system$descriptor", FunctionDescriptor.class);
            MH_SYSTEM_HANDLE = find("system$handle", MethodHandle.class);
            MH_SYSTEM_ADDRESS = find("system$address", MemorySegment.class);
            MH_SYSTEM = find("system", int.class, MemorySegment.class);
            MH_REALPATH_DESCRIPTOR = find("realpath$descriptor", FunctionDescriptor.class);
            MH_REALPATH_HANDLE = find("realpath$handle", MethodHandle.class);
            MH_REALPATH_ADDRESS = find("realpath$address", MemorySegment.class);
            MH_REALPATH = find("realpath", MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_BSEARCH_DESCRIPTOR = find("bsearch$descriptor", FunctionDescriptor.class);
            MH_BSEARCH_HANDLE = find("bsearch$handle", MethodHandle.class);
            MH_BSEARCH_ADDRESS = find("bsearch$address", MemorySegment.class);
            MH_BSEARCH = find("bsearch", MemorySegment.class, MemorySegment.class, MemorySegment.class, long.class, long.class, MemorySegment.class);
            MH_QSORT_DESCRIPTOR = find("qsort$descriptor", FunctionDescriptor.class);
            MH_QSORT_HANDLE = find("qsort$handle", MethodHandle.class);
            MH_QSORT_ADDRESS = find("qsort$address", MemorySegment.class);
            MH_QSORT = find("qsort", void.class, MemorySegment.class, long.class, long.class, MemorySegment.class);
            MH_ABS_DESCRIPTOR = find("abs$descriptor", FunctionDescriptor.class);
            MH_ABS_HANDLE = find("abs$handle", MethodHandle.class);
            MH_ABS_ADDRESS = find("abs$address", MemorySegment.class);
            MH_ABS = find("abs", int.class, int.class);
            MH_LABS_DESCRIPTOR = find("labs$descriptor", FunctionDescriptor.class);
            MH_LABS_HANDLE = find("labs$handle", MethodHandle.class);
            MH_LABS_ADDRESS = find("labs$address", MemorySegment.class);
            MH_LABS = find("labs", long.class, long.class);
            MH_LLABS_DESCRIPTOR = find("llabs$descriptor", FunctionDescriptor.class);
            MH_LLABS_HANDLE = find("llabs$handle", MethodHandle.class);
            MH_LLABS_ADDRESS = find("llabs$address", MemorySegment.class);
            MH_LLABS = find("llabs", long.class, long.class);
            MH_DIV_DESCRIPTOR = find("div$descriptor", FunctionDescriptor.class);
            MH_DIV_HANDLE = find("div$handle", MethodHandle.class);
            MH_DIV_ADDRESS = find("div$address", MemorySegment.class);
            MH_DIV = find("div", MemorySegment.class, SegmentAllocator.class, int.class, int.class);
            MH_LDIV_DESCRIPTOR = find("ldiv$descriptor", FunctionDescriptor.class);
            MH_LDIV_HANDLE = find("ldiv$handle", MethodHandle.class);
            MH_LDIV_ADDRESS = find("ldiv$address", MemorySegment.class);
            MH_LDIV = find("ldiv", MemorySegment.class, SegmentAllocator.class, long.class, long.class);
            MH_LLDIV_DESCRIPTOR = find("lldiv$descriptor", FunctionDescriptor.class);
            MH_LLDIV_HANDLE = find("lldiv$handle", MethodHandle.class);
            MH_LLDIV_ADDRESS = find("lldiv$address", MemorySegment.class);
            MH_LLDIV = find("lldiv", MemorySegment.class, SegmentAllocator.class, long.class, long.class);
            MH_ECVT_DESCRIPTOR = find("ecvt$descriptor", FunctionDescriptor.class);
            MH_ECVT_HANDLE = find("ecvt$handle", MethodHandle.class);
            MH_ECVT_ADDRESS = find("ecvt$address", MemorySegment.class);
            MH_ECVT = find("ecvt", MemorySegment.class, double.class, int.class, MemorySegment.class, MemorySegment.class);
            MH_FCVT_DESCRIPTOR = find("fcvt$descriptor", FunctionDescriptor.class);
            MH_FCVT_HANDLE = find("fcvt$handle", MethodHandle.class);
            MH_FCVT_ADDRESS = find("fcvt$address", MemorySegment.class);
            MH_FCVT = find("fcvt", MemorySegment.class, double.class, int.class, MemorySegment.class, MemorySegment.class);
            MH_GCVT_DESCRIPTOR = find("gcvt$descriptor", FunctionDescriptor.class);
            MH_GCVT_HANDLE = find("gcvt$handle", MethodHandle.class);
            MH_GCVT_ADDRESS = find("gcvt$address", MemorySegment.class);
            MH_GCVT = find("gcvt", MemorySegment.class, double.class, int.class, MemorySegment.class);
            MH_ECVT_R_DESCRIPTOR = find("ecvt_r$descriptor", FunctionDescriptor.class);
            MH_ECVT_R_HANDLE = find("ecvt_r$handle", MethodHandle.class);
            MH_ECVT_R_ADDRESS = find("ecvt_r$address", MemorySegment.class);
            MH_ECVT_R = find("ecvt_r", int.class, double.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, long.class);
            MH_FCVT_R_DESCRIPTOR = find("fcvt_r$descriptor", FunctionDescriptor.class);
            MH_FCVT_R_HANDLE = find("fcvt_r$handle", MethodHandle.class);
            MH_FCVT_R_ADDRESS = find("fcvt_r$address", MemorySegment.class);
            MH_FCVT_R = find("fcvt_r", int.class, double.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, long.class);
            MH_MBLEN_DESCRIPTOR = find("mblen$descriptor", FunctionDescriptor.class);
            MH_MBLEN_HANDLE = find("mblen$handle", MethodHandle.class);
            MH_MBLEN_ADDRESS = find("mblen$address", MemorySegment.class);
            MH_MBLEN = find("mblen", int.class, MemorySegment.class, long.class);
            MH_MBTOWC_DESCRIPTOR = find("mbtowc$descriptor", FunctionDescriptor.class);
            MH_MBTOWC_HANDLE = find("mbtowc$handle", MethodHandle.class);
            MH_MBTOWC_ADDRESS = find("mbtowc$address", MemorySegment.class);
            MH_MBTOWC = find("mbtowc", int.class, MemorySegment.class, MemorySegment.class, long.class);
            MH_WCTOMB_DESCRIPTOR = find("wctomb$descriptor", FunctionDescriptor.class);
            MH_WCTOMB_HANDLE = find("wctomb$handle", MethodHandle.class);
            MH_WCTOMB_ADDRESS = find("wctomb$address", MemorySegment.class);
            MH_WCTOMB = find("wctomb", int.class, MemorySegment.class, int.class);
            MH_MBSTOWCS_DESCRIPTOR = find("mbstowcs$descriptor", FunctionDescriptor.class);
            MH_MBSTOWCS_HANDLE = find("mbstowcs$handle", MethodHandle.class);
            MH_MBSTOWCS_ADDRESS = find("mbstowcs$address", MemorySegment.class);
            MH_MBSTOWCS = find("mbstowcs", long.class, MemorySegment.class, MemorySegment.class, long.class);
            MH_WCSTOMBS_DESCRIPTOR = find("wcstombs$descriptor", FunctionDescriptor.class);
            MH_WCSTOMBS_HANDLE = find("wcstombs$handle", MethodHandle.class);
            MH_WCSTOMBS_ADDRESS = find("wcstombs$address", MemorySegment.class);
            MH_WCSTOMBS = find("wcstombs", long.class, MemorySegment.class, MemorySegment.class, long.class);
            MH_RPMATCH_DESCRIPTOR = find("rpmatch$descriptor", FunctionDescriptor.class);
            MH_RPMATCH_HANDLE = find("rpmatch$handle", MethodHandle.class);
            MH_RPMATCH_ADDRESS = find("rpmatch$address", MemorySegment.class);
            MH_RPMATCH = find("rpmatch", int.class, MemorySegment.class);
            MH_GETSUBOPT_DESCRIPTOR = find("getsubopt$descriptor", FunctionDescriptor.class);
            MH_GETSUBOPT_HANDLE = find("getsubopt$handle", MethodHandle.class);
            MH_GETSUBOPT_ADDRESS = find("getsubopt$address", MemorySegment.class);
            MH_GETSUBOPT = find("getsubopt", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_GETLOADAVG_DESCRIPTOR = find("getloadavg$descriptor", FunctionDescriptor.class);
            MH_GETLOADAVG_HANDLE = find("getloadavg$handle", MethodHandle.class);
            MH_GETLOADAVG_ADDRESS = find("getloadavg$address", MemorySegment.class);
            MH_GETLOADAVG = find("getloadavg", int.class, MemorySegment.class, int.class);
            MH_HS_FREE_DATABASE_DESCRIPTOR = find("hs_free_database$descriptor", FunctionDescriptor.class);
            MH_HS_FREE_DATABASE_HANDLE = find("hs_free_database$handle", MethodHandle.class);
            MH_HS_FREE_DATABASE_ADDRESS = find("hs_free_database$address", MemorySegment.class);
            MH_HS_FREE_DATABASE = find("hs_free_database", int.class, MemorySegment.class);
            MH_HS_SERIALIZE_DATABASE_DESCRIPTOR = find("hs_serialize_database$descriptor", FunctionDescriptor.class);
            MH_HS_SERIALIZE_DATABASE_HANDLE = find("hs_serialize_database$handle", MethodHandle.class);
            MH_HS_SERIALIZE_DATABASE_ADDRESS = find("hs_serialize_database$address", MemorySegment.class);
            MH_HS_SERIALIZE_DATABASE = find("hs_serialize_database", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_DESERIALIZE_DATABASE_DESCRIPTOR = find("hs_deserialize_database$descriptor", FunctionDescriptor.class);
            MH_HS_DESERIALIZE_DATABASE_HANDLE = find("hs_deserialize_database$handle", MethodHandle.class);
            MH_HS_DESERIALIZE_DATABASE_ADDRESS = find("hs_deserialize_database$address", MemorySegment.class);
            MH_HS_DESERIALIZE_DATABASE = find("hs_deserialize_database", int.class, MemorySegment.class, long.class, MemorySegment.class);
            MH_HS_DESERIALIZE_DATABASE_AT_DESCRIPTOR = find("hs_deserialize_database_at$descriptor", FunctionDescriptor.class);
            MH_HS_DESERIALIZE_DATABASE_AT_HANDLE = find("hs_deserialize_database_at$handle", MethodHandle.class);
            MH_HS_DESERIALIZE_DATABASE_AT_ADDRESS = find("hs_deserialize_database_at$address", MemorySegment.class);
            MH_HS_DESERIALIZE_DATABASE_AT = find("hs_deserialize_database_at", int.class, MemorySegment.class, long.class, MemorySegment.class);
            MH_HS_STREAM_SIZE_DESCRIPTOR = find("hs_stream_size$descriptor", FunctionDescriptor.class);
            MH_HS_STREAM_SIZE_HANDLE = find("hs_stream_size$handle", MethodHandle.class);
            MH_HS_STREAM_SIZE_ADDRESS = find("hs_stream_size$address", MemorySegment.class);
            MH_HS_STREAM_SIZE = find("hs_stream_size", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_DATABASE_SIZE_DESCRIPTOR = find("hs_database_size$descriptor", FunctionDescriptor.class);
            MH_HS_DATABASE_SIZE_HANDLE = find("hs_database_size$handle", MethodHandle.class);
            MH_HS_DATABASE_SIZE_ADDRESS = find("hs_database_size$address", MemorySegment.class);
            MH_HS_DATABASE_SIZE = find("hs_database_size", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SERIALIZED_DATABASE_SIZE_DESCRIPTOR = find("hs_serialized_database_size$descriptor", FunctionDescriptor.class);
            MH_HS_SERIALIZED_DATABASE_SIZE_HANDLE = find("hs_serialized_database_size$handle", MethodHandle.class);
            MH_HS_SERIALIZED_DATABASE_SIZE_ADDRESS = find("hs_serialized_database_size$address", MemorySegment.class);
            MH_HS_SERIALIZED_DATABASE_SIZE = find("hs_serialized_database_size", int.class, MemorySegment.class, long.class, MemorySegment.class);
            MH_HS_DATABASE_INFO_DESCRIPTOR = find("hs_database_info$descriptor", FunctionDescriptor.class);
            MH_HS_DATABASE_INFO_HANDLE = find("hs_database_info$handle", MethodHandle.class);
            MH_HS_DATABASE_INFO_ADDRESS = find("hs_database_info$address", MemorySegment.class);
            MH_HS_DATABASE_INFO = find("hs_database_info", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SERIALIZED_DATABASE_INFO_DESCRIPTOR = find("hs_serialized_database_info$descriptor", FunctionDescriptor.class);
            MH_HS_SERIALIZED_DATABASE_INFO_HANDLE = find("hs_serialized_database_info$handle", MethodHandle.class);
            MH_HS_SERIALIZED_DATABASE_INFO_ADDRESS = find("hs_serialized_database_info$address", MemorySegment.class);
            MH_HS_SERIALIZED_DATABASE_INFO = find("hs_serialized_database_info", int.class, MemorySegment.class, long.class, MemorySegment.class);
            MH_HS_SET_ALLOCATOR_DESCRIPTOR = find("hs_set_allocator$descriptor", FunctionDescriptor.class);
            MH_HS_SET_ALLOCATOR_HANDLE = find("hs_set_allocator$handle", MethodHandle.class);
            MH_HS_SET_ALLOCATOR_ADDRESS = find("hs_set_allocator$address", MemorySegment.class);
            MH_HS_SET_ALLOCATOR = find("hs_set_allocator", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SET_DATABASE_ALLOCATOR_DESCRIPTOR = find("hs_set_database_allocator$descriptor", FunctionDescriptor.class);
            MH_HS_SET_DATABASE_ALLOCATOR_HANDLE = find("hs_set_database_allocator$handle", MethodHandle.class);
            MH_HS_SET_DATABASE_ALLOCATOR_ADDRESS = find("hs_set_database_allocator$address", MemorySegment.class);
            MH_HS_SET_DATABASE_ALLOCATOR = find("hs_set_database_allocator", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SET_MISC_ALLOCATOR_DESCRIPTOR = find("hs_set_misc_allocator$descriptor", FunctionDescriptor.class);
            MH_HS_SET_MISC_ALLOCATOR_HANDLE = find("hs_set_misc_allocator$handle", MethodHandle.class);
            MH_HS_SET_MISC_ALLOCATOR_ADDRESS = find("hs_set_misc_allocator$address", MemorySegment.class);
            MH_HS_SET_MISC_ALLOCATOR = find("hs_set_misc_allocator", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SET_SCRATCH_ALLOCATOR_DESCRIPTOR = find("hs_set_scratch_allocator$descriptor", FunctionDescriptor.class);
            MH_HS_SET_SCRATCH_ALLOCATOR_HANDLE = find("hs_set_scratch_allocator$handle", MethodHandle.class);
            MH_HS_SET_SCRATCH_ALLOCATOR_ADDRESS = find("hs_set_scratch_allocator$address", MemorySegment.class);
            MH_HS_SET_SCRATCH_ALLOCATOR = find("hs_set_scratch_allocator", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SET_STREAM_ALLOCATOR_DESCRIPTOR = find("hs_set_stream_allocator$descriptor", FunctionDescriptor.class);
            MH_HS_SET_STREAM_ALLOCATOR_HANDLE = find("hs_set_stream_allocator$handle", MethodHandle.class);
            MH_HS_SET_STREAM_ALLOCATOR_ADDRESS = find("hs_set_stream_allocator$address", MemorySegment.class);
            MH_HS_SET_STREAM_ALLOCATOR = find("hs_set_stream_allocator", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_VERSION_DESCRIPTOR = find("hs_version$descriptor", FunctionDescriptor.class);
            MH_HS_VERSION_HANDLE = find("hs_version$handle", MethodHandle.class);
            MH_HS_VERSION_ADDRESS = find("hs_version$address", MemorySegment.class);
            MH_HS_VERSION = find("hs_version", MemorySegment.class);
            MH_HS_VALID_PLATFORM_DESCRIPTOR = find("hs_valid_platform$descriptor", FunctionDescriptor.class);
            MH_HS_VALID_PLATFORM_HANDLE = find("hs_valid_platform$handle", MethodHandle.class);
            MH_HS_VALID_PLATFORM_ADDRESS = find("hs_valid_platform$address", MemorySegment.class);
            MH_HS_VALID_PLATFORM = find("hs_valid_platform", int.class);
            MH_HS_COMPILE_DESCRIPTOR = find("hs_compile$descriptor", FunctionDescriptor.class);
            MH_HS_COMPILE_HANDLE = find("hs_compile$handle", MethodHandle.class);
            MH_HS_COMPILE_ADDRESS = find("hs_compile$address", MemorySegment.class);
            MH_HS_COMPILE = find("hs_compile", int.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_COMPILE_MULTI_DESCRIPTOR = find("hs_compile_multi$descriptor", FunctionDescriptor.class);
            MH_HS_COMPILE_MULTI_HANDLE = find("hs_compile_multi$handle", MethodHandle.class);
            MH_HS_COMPILE_MULTI_ADDRESS = find("hs_compile_multi$address", MemorySegment.class);
            MH_HS_COMPILE_MULTI = find("hs_compile_multi", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_COMPILE_EXT_MULTI_DESCRIPTOR = find("hs_compile_ext_multi$descriptor", FunctionDescriptor.class);
            MH_HS_COMPILE_EXT_MULTI_HANDLE = find("hs_compile_ext_multi$handle", MethodHandle.class);
            MH_HS_COMPILE_EXT_MULTI_ADDRESS = find("hs_compile_ext_multi$address", MemorySegment.class);
            MH_HS_COMPILE_EXT_MULTI = find("hs_compile_ext_multi", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_COMPILE_LIT_DESCRIPTOR = find("hs_compile_lit$descriptor", FunctionDescriptor.class);
            MH_HS_COMPILE_LIT_HANDLE = find("hs_compile_lit$handle", MethodHandle.class);
            MH_HS_COMPILE_LIT_ADDRESS = find("hs_compile_lit$address", MemorySegment.class);
            MH_HS_COMPILE_LIT = find("hs_compile_lit", int.class, MemorySegment.class, int.class, long.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_COMPILE_LIT_MULTI_DESCRIPTOR = find("hs_compile_lit_multi$descriptor", FunctionDescriptor.class);
            MH_HS_COMPILE_LIT_MULTI_HANDLE = find("hs_compile_lit_multi$handle", MethodHandle.class);
            MH_HS_COMPILE_LIT_MULTI_ADDRESS = find("hs_compile_lit_multi$address", MemorySegment.class);
            MH_HS_COMPILE_LIT_MULTI = find("hs_compile_lit_multi", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_FREE_COMPILE_ERROR_DESCRIPTOR = find("hs_free_compile_error$descriptor", FunctionDescriptor.class);
            MH_HS_FREE_COMPILE_ERROR_HANDLE = find("hs_free_compile_error$handle", MethodHandle.class);
            MH_HS_FREE_COMPILE_ERROR_ADDRESS = find("hs_free_compile_error$address", MemorySegment.class);
            MH_HS_FREE_COMPILE_ERROR = find("hs_free_compile_error", int.class, MemorySegment.class);
            MH_HS_EXPRESSION_INFO_DESCRIPTOR = find("hs_expression_info$descriptor", FunctionDescriptor.class);
            MH_HS_EXPRESSION_INFO_HANDLE = find("hs_expression_info$handle", MethodHandle.class);
            MH_HS_EXPRESSION_INFO_ADDRESS = find("hs_expression_info$address", MemorySegment.class);
            MH_HS_EXPRESSION_INFO = find("hs_expression_info", int.class, MemorySegment.class, int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_EXPRESSION_EXT_INFO_DESCRIPTOR = find("hs_expression_ext_info$descriptor", FunctionDescriptor.class);
            MH_HS_EXPRESSION_EXT_INFO_HANDLE = find("hs_expression_ext_info$handle", MethodHandle.class);
            MH_HS_EXPRESSION_EXT_INFO_ADDRESS = find("hs_expression_ext_info$address", MemorySegment.class);
            MH_HS_EXPRESSION_EXT_INFO = find("hs_expression_ext_info", int.class, MemorySegment.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_POPULATE_PLATFORM_DESCRIPTOR = find("hs_populate_platform$descriptor", FunctionDescriptor.class);
            MH_HS_POPULATE_PLATFORM_HANDLE = find("hs_populate_platform$handle", MethodHandle.class);
            MH_HS_POPULATE_PLATFORM_ADDRESS = find("hs_populate_platform$address", MemorySegment.class);
            MH_HS_POPULATE_PLATFORM = find("hs_populate_platform", int.class, MemorySegment.class);
            MH_HS_OPEN_STREAM_DESCRIPTOR = find("hs_open_stream$descriptor", FunctionDescriptor.class);
            MH_HS_OPEN_STREAM_HANDLE = find("hs_open_stream$handle", MethodHandle.class);
            MH_HS_OPEN_STREAM_ADDRESS = find("hs_open_stream$address", MemorySegment.class);
            MH_HS_OPEN_STREAM = find("hs_open_stream", int.class, MemorySegment.class, int.class, MemorySegment.class);
            MH_HS_SCAN_STREAM_DESCRIPTOR = find("hs_scan_stream$descriptor", FunctionDescriptor.class);
            MH_HS_SCAN_STREAM_HANDLE = find("hs_scan_stream$handle", MethodHandle.class);
            MH_HS_SCAN_STREAM_ADDRESS = find("hs_scan_stream$address", MemorySegment.class);
            MH_HS_SCAN_STREAM = find("hs_scan_stream", int.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_CLOSE_STREAM_DESCRIPTOR = find("hs_close_stream$descriptor", FunctionDescriptor.class);
            MH_HS_CLOSE_STREAM_HANDLE = find("hs_close_stream$handle", MethodHandle.class);
            MH_HS_CLOSE_STREAM_ADDRESS = find("hs_close_stream$address", MemorySegment.class);
            MH_HS_CLOSE_STREAM = find("hs_close_stream", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_RESET_STREAM_DESCRIPTOR = find("hs_reset_stream$descriptor", FunctionDescriptor.class);
            MH_HS_RESET_STREAM_HANDLE = find("hs_reset_stream$handle", MethodHandle.class);
            MH_HS_RESET_STREAM_ADDRESS = find("hs_reset_stream$address", MemorySegment.class);
            MH_HS_RESET_STREAM = find("hs_reset_stream", int.class, MemorySegment.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_COPY_STREAM_DESCRIPTOR = find("hs_copy_stream$descriptor", FunctionDescriptor.class);
            MH_HS_COPY_STREAM_HANDLE = find("hs_copy_stream$handle", MethodHandle.class);
            MH_HS_COPY_STREAM_ADDRESS = find("hs_copy_stream$address", MemorySegment.class);
            MH_HS_COPY_STREAM = find("hs_copy_stream", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_RESET_AND_COPY_STREAM_DESCRIPTOR = find("hs_reset_and_copy_stream$descriptor", FunctionDescriptor.class);
            MH_HS_RESET_AND_COPY_STREAM_HANDLE = find("hs_reset_and_copy_stream$handle", MethodHandle.class);
            MH_HS_RESET_AND_COPY_STREAM_ADDRESS = find("hs_reset_and_copy_stream$address", MemorySegment.class);
            MH_HS_RESET_AND_COPY_STREAM = find("hs_reset_and_copy_stream", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_COMPRESS_STREAM_DESCRIPTOR = find("hs_compress_stream$descriptor", FunctionDescriptor.class);
            MH_HS_COMPRESS_STREAM_HANDLE = find("hs_compress_stream$handle", MethodHandle.class);
            MH_HS_COMPRESS_STREAM_ADDRESS = find("hs_compress_stream$address", MemorySegment.class);
            MH_HS_COMPRESS_STREAM = find("hs_compress_stream", int.class, MemorySegment.class, MemorySegment.class, long.class, MemorySegment.class);
            MH_HS_EXPAND_STREAM_DESCRIPTOR = find("hs_expand_stream$descriptor", FunctionDescriptor.class);
            MH_HS_EXPAND_STREAM_HANDLE = find("hs_expand_stream$handle", MethodHandle.class);
            MH_HS_EXPAND_STREAM_ADDRESS = find("hs_expand_stream$address", MemorySegment.class);
            MH_HS_EXPAND_STREAM = find("hs_expand_stream", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, long.class);
            MH_HS_RESET_AND_EXPAND_STREAM_DESCRIPTOR = find("hs_reset_and_expand_stream$descriptor", FunctionDescriptor.class);
            MH_HS_RESET_AND_EXPAND_STREAM_HANDLE = find("hs_reset_and_expand_stream$handle", MethodHandle.class);
            MH_HS_RESET_AND_EXPAND_STREAM_ADDRESS = find("hs_reset_and_expand_stream$address", MemorySegment.class);
            MH_HS_RESET_AND_EXPAND_STREAM = find("hs_reset_and_expand_stream", int.class, MemorySegment.class, MemorySegment.class, long.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SCAN_DESCRIPTOR = find("hs_scan$descriptor", FunctionDescriptor.class);
            MH_HS_SCAN_HANDLE = find("hs_scan$handle", MethodHandle.class);
            MH_HS_SCAN_ADDRESS = find("hs_scan$address", MemorySegment.class);
            MH_HS_SCAN = find("hs_scan", int.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SCAN_VECTOR_DESCRIPTOR = find("hs_scan_vector$descriptor", FunctionDescriptor.class);
            MH_HS_SCAN_VECTOR_HANDLE = find("hs_scan_vector$handle", MethodHandle.class);
            MH_HS_SCAN_VECTOR_ADDRESS = find("hs_scan_vector$address", MemorySegment.class);
            MH_HS_SCAN_VECTOR = find("hs_scan_vector", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_ALLOC_SCRATCH_DESCRIPTOR = find("hs_alloc_scratch$descriptor", FunctionDescriptor.class);
            MH_HS_ALLOC_SCRATCH_HANDLE = find("hs_alloc_scratch$handle", MethodHandle.class);
            MH_HS_ALLOC_SCRATCH_ADDRESS = find("hs_alloc_scratch$address", MemorySegment.class);
            MH_HS_ALLOC_SCRATCH = find("hs_alloc_scratch", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_CLONE_SCRATCH_DESCRIPTOR = find("hs_clone_scratch$descriptor", FunctionDescriptor.class);
            MH_HS_CLONE_SCRATCH_HANDLE = find("hs_clone_scratch$handle", MethodHandle.class);
            MH_HS_CLONE_SCRATCH_ADDRESS = find("hs_clone_scratch$address", MemorySegment.class);
            MH_HS_CLONE_SCRATCH = find("hs_clone_scratch", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SCRATCH_SIZE_DESCRIPTOR = find("hs_scratch_size$descriptor", FunctionDescriptor.class);
            MH_HS_SCRATCH_SIZE_HANDLE = find("hs_scratch_size$handle", MethodHandle.class);
            MH_HS_SCRATCH_SIZE_ADDRESS = find("hs_scratch_size$address", MemorySegment.class);
            MH_HS_SCRATCH_SIZE = find("hs_scratch_size", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_FREE_SCRATCH_DESCRIPTOR = find("hs_free_scratch$descriptor", FunctionDescriptor.class);
            MH_HS_FREE_SCRATCH_HANDLE = find("hs_free_scratch$handle", MethodHandle.class);
            MH_HS_FREE_SCRATCH_ADDRESS = find("hs_free_scratch$address", MemorySegment.class);
            MH_HS_FREE_SCRATCH = find("hs_free_scratch", int.class, MemorySegment.class);
            MH_POSIX_C_SOURCE = find("_POSIX_C_SOURCE", long.class);
            MH_TIMESIZE = find("__TIMESIZE", int.class);
            MH_STDC_IEC_60559_BFP = find("__STDC_IEC_60559_BFP__", long.class);
            MH_STDC_IEC_60559_COMPLEX = find("__STDC_IEC_60559_COMPLEX__", long.class);
            MH_STDC_ISO_10646 = find("__STDC_ISO_10646__", long.class);
            MH_NULL = find("NULL", MemorySegment.class);
            MH_WCLONE = find("__WCLONE", int.class);
            MH_HAVE_DISTINCT_FLOAT16 = find("__HAVE_DISTINCT_FLOAT16", int.class);
            MH_HAVE_DISTINCT_FLOAT128X = find("__HAVE_DISTINCT_FLOAT128X", int.class);
            MH_HAVE_FLOAT128_UNLIKE_LDBL = find("__HAVE_FLOAT128_UNLIKE_LDBL", int.class);
            MH_BYTE_ORDER = find("__BYTE_ORDER", int.class);
            MH_FLOAT_WORD_ORDER = find("__FLOAT_WORD_ORDER", int.class);
            MH_LITTLE_ENDIAN_1 = find("LITTLE_ENDIAN", int.class);
            MH_BIG_ENDIAN_1 = find("BIG_ENDIAN", int.class);
            MH_PDP_ENDIAN_1 = find("PDP_ENDIAN", int.class);
            MH_BYTE_ORDER_1 = find("BYTE_ORDER", int.class);
            MH_SIGSET_NWORDS = find("_SIGSET_NWORDS", long.class);
            MH_NFDBITS = find("__NFDBITS", int.class);
            MH_FD_SETSIZE_1 = find("FD_SETSIZE", int.class);
            MH_NFDBITS_1 = find("NFDBITS", int.class);
            MH_PTHREAD_RWLOCK_ELISION_EXTRA = find("__PTHREAD_RWLOCK_ELISION_EXTRA", int.class);
            MH_HS_INVALID = find("HS_INVALID", int.class);
            MH_HS_NOMEM = find("HS_NOMEM", int.class);
            MH_HS_SCAN_TERMINATED = find("HS_SCAN_TERMINATED", int.class);
            MH_HS_COMPILER_ERROR = find("HS_COMPILER_ERROR", int.class);
            MH_HS_DB_VERSION_ERROR = find("HS_DB_VERSION_ERROR", int.class);
            MH_HS_DB_PLATFORM_ERROR = find("HS_DB_PLATFORM_ERROR", int.class);
            MH_HS_DB_MODE_ERROR = find("HS_DB_MODE_ERROR", int.class);
            MH_HS_BAD_ALIGN = find("HS_BAD_ALIGN", int.class);
            MH_HS_BAD_ALLOC = find("HS_BAD_ALLOC", int.class);
            MH_HS_SCRATCH_IN_USE = find("HS_SCRATCH_IN_USE", int.class);
            MH_HS_ARCH_ERROR = find("HS_ARCH_ERROR", int.class);
            MH_HS_INSUFFICIENT_SPACE = find("HS_INSUFFICIENT_SPACE", int.class);
            MH_HS_UNKNOWN_ERROR = find("HS_UNKNOWN_ERROR", int.class);
            MH_HS_EXT_FLAG_MIN_OFFSET = find("HS_EXT_FLAG_MIN_OFFSET", long.class);
            MH_HS_EXT_FLAG_MAX_OFFSET = find("HS_EXT_FLAG_MAX_OFFSET", long.class);
            MH_HS_EXT_FLAG_MIN_LENGTH = find("HS_EXT_FLAG_MIN_LENGTH", long.class);
            MH_HS_EXT_FLAG_EDIT_DISTANCE = find("HS_EXT_FLAG_EDIT_DISTANCE", long.class);
            MH_HS_EXT_FLAG_HAMMING_DISTANCE = find("HS_EXT_FLAG_HAMMING_DISTANCE", long.class);
            MH_HS_CPU_FEATURES_AVX2 = find("HS_CPU_FEATURES_AVX2", long.class);
            MH_HS_CPU_FEATURES_AVX512 = find("HS_CPU_FEATURES_AVX512", long.class);
            MH_HS_CPU_FEATURES_AVX512VBMI = find("HS_CPU_FEATURES_AVX512VBMI", long.class);
            MH_HS_MODE_SOM_HORIZON_LARGE = find("HS_MODE_SOM_HORIZON_LARGE", int.class);
            MH_HS_MODE_SOM_HORIZON_MEDIUM = find("HS_MODE_SOM_HORIZON_MEDIUM", int.class);
            MH_HS_MODE_SOM_HORIZON_SMALL = find("HS_MODE_SOM_HORIZON_SMALL", int.class);
            MH_HS_OFFSET_PAST_HORIZON = find("HS_OFFSET_PAST_HORIZON", long.class);
            MH_HS_VERSION_STRING = find("HS_VERSION_STRING", MemorySegment.class);
            MH_HS_VERSION_32BIT = find("HS_VERSION_32BIT", int.class);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hyperscan class: " + className, e);
        }
    }

    private hyperscan() {}

    private static MethodHandle find(String name, Class<?> rtype, Class<?>... ptypes) {
        try {
            return MethodHandles.publicLookup().findStatic(DELEGATE, name, MethodType.methodType(rtype, ptypes));
        } catch (Exception e) {
            throw new RuntimeException("Failed to find method " + name + " in " + DELEGATE.getName(), e);
        }
    }

    public static int _FEATURES_H() {
        try {
            return (int) MH_FEATURES_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _DEFAULT_SOURCE() {
        try {
            return (int) MH_DEFAULT_SOURCE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_ISOC2X() {
        try {
            return (int) MH_GLIBC_USE_ISOC2X.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_ISOC11() {
        try {
            return (int) MH_USE_ISOC11.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_ISOC99() {
        try {
            return (int) MH_USE_ISOC99.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_ISOC95() {
        try {
            return (int) MH_USE_ISOC95.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX_IMPLICITLY() {
        try {
            return (int) MH_USE_POSIX_IMPLICITLY.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _POSIX_SOURCE() {
        try {
            return (int) MH_POSIX_SOURCE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX() {
        try {
            return (int) MH_USE_POSIX.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX2() {
        try {
            return (int) MH_USE_POSIX2.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX199309() {
        try {
            return (int) MH_USE_POSIX199309.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX199506() {
        try {
            return (int) MH_USE_POSIX199506.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_XOPEN2K() {
        try {
            return (int) MH_USE_XOPEN2K.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_XOPEN2K8() {
        try {
            return (int) MH_USE_XOPEN2K8.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _ATFILE_SOURCE() {
        try {
            return (int) MH_ATFILE_SOURCE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WORDSIZE() {
        try {
            return (int) MH_WORDSIZE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WORDSIZE_TIME64_COMPAT32() {
        try {
            return (int) MH_WORDSIZE_TIME64_COMPAT32.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SYSCALL_WORDSIZE() {
        try {
            return (int) MH_SYSCALL_WORDSIZE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_MISC() {
        try {
            return (int) MH_USE_MISC.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_ATFILE() {
        try {
            return (int) MH_USE_ATFILE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_FORTIFY_LEVEL() {
        try {
            return (int) MH_USE_FORTIFY_LEVEL.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_DEPRECATED_GETS() {
        try {
            return (int) MH_GLIBC_USE_DEPRECATED_GETS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_DEPRECATED_SCANF() {
        try {
            return (int) MH_GLIBC_USE_DEPRECATED_SCANF.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_C2X_STRTOL() {
        try {
            return (int) MH_GLIBC_USE_C2X_STRTOL.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _STDC_PREDEF_H() {
        try {
            return (int) MH_STDC_PREDEF_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __STDC_IEC_559__() {
        try {
            return (int) MH_STDC_IEC_559.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __STDC_IEC_559_COMPLEX__() {
        try {
            return (int) MH_STDC_IEC_559_COMPLEX.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GNU_LIBRARY__() {
        try {
            return (int) MH_GNU_LIBRARY.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC__() {
        try {
            return (int) MH_GLIBC.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_MINOR__() {
        try {
            return (int) MH_GLIBC_MINOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _SYS_CDEFS_H() {
        try {
            return (int) MH_SYS_CDEFS_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __glibc_c99_flexarr_available() {
        try {
            return (int) MH_GLIBC_C99_FLEXARR_AVAILABLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __LDOUBLE_REDIRECTS_TO_FLOAT128_ABI() {
        try {
            return (int) MH_LDOUBLE_REDIRECTS_TO_FLOAT128_ABI.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_GENERIC_SELECTION() {
        try {
            return (int) MH_HAVE_GENERIC_SELECTION.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_LIB_EXT2() {
        try {
            return (int) MH_GLIBC_USE_LIB_EXT2.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_BFP_EXT() {
        try {
            return (int) MH_GLIBC_USE_IEC_60559_BFP_EXT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_BFP_EXT_C2X() {
        try {
            return (int) MH_GLIBC_USE_IEC_60559_BFP_EXT_C2X.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_EXT() {
        try {
            return (int) MH_GLIBC_USE_IEC_60559_EXT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_FUNCS_EXT() {
        try {
            return (int) MH_GLIBC_USE_IEC_60559_FUNCS_EXT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_FUNCS_EXT_C2X() {
        try {
            return (int) MH_GLIBC_USE_IEC_60559_FUNCS_EXT_C2X.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_TYPES_EXT() {
        try {
            return (int) MH_GLIBC_USE_IEC_60559_TYPES_EXT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _STDLIB_H() {
        try {
            return (int) MH_STDLIB_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WNOHANG() {
        try {
            return (int) MH_WNOHANG.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WUNTRACED() {
        try {
            return (int) MH_WUNTRACED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WSTOPPED() {
        try {
            return (int) MH_WSTOPPED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WEXITED() {
        try {
            return (int) MH_WEXITED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WCONTINUED() {
        try {
            return (int) MH_WCONTINUED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WNOWAIT() {
        try {
            return (int) MH_WNOWAIT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WNOTHREAD() {
        try {
            return (int) MH_WNOTHREAD.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WALL() {
        try {
            return (int) MH_WALL.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __W_CONTINUED() {
        try {
            return (int) MH_W_CONTINUED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WCOREFLAG() {
        try {
            return (int) MH_WCOREFLAG.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT128() {
        try {
            return (int) MH_HAVE_FLOAT128.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT128() {
        try {
            return (int) MH_HAVE_DISTINCT_FLOAT128.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT64X() {
        try {
            return (int) MH_HAVE_FLOAT64X.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT64X_LONG_DOUBLE() {
        try {
            return (int) MH_HAVE_FLOAT64X_LONG_DOUBLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT16() {
        try {
            return (int) MH_HAVE_FLOAT16.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT32() {
        try {
            return (int) MH_HAVE_FLOAT32.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT64() {
        try {
            return (int) MH_HAVE_FLOAT64.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT32X() {
        try {
            return (int) MH_HAVE_FLOAT32X.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT128X() {
        try {
            return (int) MH_HAVE_FLOAT128X.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT32() {
        try {
            return (int) MH_HAVE_DISTINCT_FLOAT32.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT64() {
        try {
            return (int) MH_HAVE_DISTINCT_FLOAT64.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT32X() {
        try {
            return (int) MH_HAVE_DISTINCT_FLOAT32X.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT64X() {
        try {
            return (int) MH_HAVE_DISTINCT_FLOAT64X.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOATN_NOT_TYPEDEF() {
        try {
            return (int) MH_HAVE_FLOATN_NOT_TYPEDEF.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __ldiv_t_defined() {
        try {
            return (int) MH_LDIV_T_DEFINED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __lldiv_t_defined() {
        try {
            return (int) MH_LLDIV_T_DEFINED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int RAND_MAX() {
        try {
            return (int) MH_RAND_MAX.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int EXIT_FAILURE() {
        try {
            return (int) MH_EXIT_FAILURE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int EXIT_SUCCESS() {
        try {
            return (int) MH_EXIT_SUCCESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _SYS_TYPES_H() {
        try {
            return (int) MH_SYS_TYPES_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_TYPES_H() {
        try {
            return (int) MH_BITS_TYPES_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_TYPESIZES_H() {
        try {
            return (int) MH_BITS_TYPESIZES_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __OFF_T_MATCHES_OFF64_T() {
        try {
            return (int) MH_OFF_T_MATCHES_OFF64_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __INO_T_MATCHES_INO64_T() {
        try {
            return (int) MH_INO_T_MATCHES_INO64_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __RLIM_T_MATCHES_RLIM64_T() {
        try {
            return (int) MH_RLIM_T_MATCHES_RLIM64_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __STATFS_MATCHES_STATFS64() {
        try {
            return (int) MH_STATFS_MATCHES_STATFS64.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __KERNEL_OLD_TIMEVAL_MATCHES_TIMEVAL64() {
        try {
            return (int) MH_KERNEL_OLD_TIMEVAL_MATCHES_TIMEVAL64.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __FD_SETSIZE() {
        try {
            return (int) MH_FD_SETSIZE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_TIME64_H() {
        try {
            return (int) MH_BITS_TIME64_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __clock_t_defined() {
        try {
            return (int) MH_CLOCK_T_DEFINED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __clockid_t_defined() {
        try {
            return (int) MH_CLOCKID_T_DEFINED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __time_t_defined() {
        try {
            return (int) MH_TIME_T_DEFINED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __timer_t_defined() {
        try {
            return (int) MH_TIMER_T_DEFINED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_STDINT_INTN_H() {
        try {
            return (int) MH_BITS_STDINT_INTN_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __BIT_TYPES_DEFINED__() {
        try {
            return (int) MH_BIT_TYPES_DEFINED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _ENDIAN_H() {
        try {
            return (int) MH_ENDIAN_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_ENDIAN_H() {
        try {
            return (int) MH_BITS_ENDIAN_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __LITTLE_ENDIAN() {
        try {
            return (int) MH_LITTLE_ENDIAN.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __BIG_ENDIAN() {
        try {
            return (int) MH_BIG_ENDIAN.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __PDP_ENDIAN() {
        try {
            return (int) MH_PDP_ENDIAN.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_ENDIANNESS_H() {
        try {
            return (int) MH_BITS_ENDIANNESS_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_BYTESWAP_H() {
        try {
            return (int) MH_BITS_BYTESWAP_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_UINTN_IDENTITY_H() {
        try {
            return (int) MH_BITS_UINTN_IDENTITY_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _SYS_SELECT_H() {
        try {
            return (int) MH_SYS_SELECT_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __sigset_t_defined() {
        try {
            return (int) MH_SIGSET_T_DEFINED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __timeval_defined() {
        try {
            return (int) MH_TIMEVAL_DEFINED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _STRUCT_TIMESPEC() {
        try {
            return (int) MH_STRUCT_TIMESPEC.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_PTHREADTYPES_COMMON_H() {
        try {
            return (int) MH_BITS_PTHREADTYPES_COMMON_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _THREAD_SHARED_TYPES_H() {
        try {
            return (int) MH_THREAD_SHARED_TYPES_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_PTHREADTYPES_ARCH_H() {
        try {
            return (int) MH_BITS_PTHREADTYPES_ARCH_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_MUTEX_T() {
        try {
            return (int) MH_SIZEOF_PTHREAD_MUTEX_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_ATTR_T() {
        try {
            return (int) MH_SIZEOF_PTHREAD_ATTR_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_RWLOCK_T() {
        try {
            return (int) MH_SIZEOF_PTHREAD_RWLOCK_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_BARRIER_T() {
        try {
            return (int) MH_SIZEOF_PTHREAD_BARRIER_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_MUTEXATTR_T() {
        try {
            return (int) MH_SIZEOF_PTHREAD_MUTEXATTR_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_COND_T() {
        try {
            return (int) MH_SIZEOF_PTHREAD_COND_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_CONDATTR_T() {
        try {
            return (int) MH_SIZEOF_PTHREAD_CONDATTR_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_RWLOCKATTR_T() {
        try {
            return (int) MH_SIZEOF_PTHREAD_RWLOCKATTR_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_BARRIERATTR_T() {
        try {
            return (int) MH_SIZEOF_PTHREAD_BARRIERATTR_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _THREAD_MUTEX_INTERNAL_H() {
        try {
            return (int) MH_THREAD_MUTEX_INTERNAL_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __PTHREAD_MUTEX_HAVE_PREV() {
        try {
            return (int) MH_PTHREAD_MUTEX_HAVE_PREV.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __have_pthread_attr_t() {
        try {
            return (int) MH_HAVE_PTHREAD_ATTR_T.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _ALLOCA_H() {
        try {
            return (int) MH_ALLOCA_H.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_SUCCESS() {
        try {
            return (int) MH_HS_SUCCESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_CASELESS() {
        try {
            return (int) MH_HS_FLAG_CASELESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_DOTALL() {
        try {
            return (int) MH_HS_FLAG_DOTALL.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_MULTILINE() {
        try {
            return (int) MH_HS_FLAG_MULTILINE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_SINGLEMATCH() {
        try {
            return (int) MH_HS_FLAG_SINGLEMATCH.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_ALLOWEMPTY() {
        try {
            return (int) MH_HS_FLAG_ALLOWEMPTY.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_UTF8() {
        try {
            return (int) MH_HS_FLAG_UTF8.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_UCP() {
        try {
            return (int) MH_HS_FLAG_UCP.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_PREFILTER() {
        try {
            return (int) MH_HS_FLAG_PREFILTER.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_SOM_LEFTMOST() {
        try {
            return (int) MH_HS_FLAG_SOM_LEFTMOST.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_COMBINATION() {
        try {
            return (int) MH_HS_FLAG_COMBINATION.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_QUIET() {
        try {
            return (int) MH_HS_FLAG_QUIET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_GENERIC() {
        try {
            return (int) MH_HS_TUNE_FAMILY_GENERIC.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_SNB() {
        try {
            return (int) MH_HS_TUNE_FAMILY_SNB.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_IVB() {
        try {
            return (int) MH_HS_TUNE_FAMILY_IVB.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_HSW() {
        try {
            return (int) MH_HS_TUNE_FAMILY_HSW.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_SLM() {
        try {
            return (int) MH_HS_TUNE_FAMILY_SLM.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_BDW() {
        try {
            return (int) MH_HS_TUNE_FAMILY_BDW.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_SKL() {
        try {
            return (int) MH_HS_TUNE_FAMILY_SKL.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_SKX() {
        try {
            return (int) MH_HS_TUNE_FAMILY_SKX.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_GLM() {
        try {
            return (int) MH_HS_TUNE_FAMILY_GLM.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_ICL() {
        try {
            return (int) MH_HS_TUNE_FAMILY_ICL.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_ICX() {
        try {
            return (int) MH_HS_TUNE_FAMILY_ICX.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_BLOCK() {
        try {
            return (int) MH_HS_MODE_BLOCK.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_NOSTREAM() {
        try {
            return (int) MH_HS_MODE_NOSTREAM.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_STREAM() {
        try {
            return (int) MH_HS_MODE_STREAM.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_VECTORED() {
        try {
            return (int) MH_HS_MODE_VECTORED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MAJOR() {
        try {
            return (int) MH_HS_MAJOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MINOR() {
        try {
            return (int) MH_HS_MINOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_PATCH() {
        try {
            return (int) MH_HS_PATCH.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor __ctype_get_mb_cur_max$descriptor() {
        try {
            return (FunctionDescriptor) MH_CTYPE_GET_MB_CUR_MAX_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle __ctype_get_mb_cur_max$handle() {
        try {
            return (MethodHandle) MH_CTYPE_GET_MB_CUR_MAX_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment __ctype_get_mb_cur_max$address() {
        try {
            return (MemorySegment) MH_CTYPE_GET_MB_CUR_MAX_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long __ctype_get_mb_cur_max() {
        try {
            return (long) MH_CTYPE_GET_MB_CUR_MAX.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atof$descriptor() {
        try {
            return (FunctionDescriptor) MH_ATOF_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atof$handle() {
        try {
            return (MethodHandle) MH_ATOF_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atof$address() {
        try {
            return (MemorySegment) MH_ATOF_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static double atof(MemorySegment arg0) {
        try {
            return (double) MH_ATOF.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atoi$descriptor() {
        try {
            return (FunctionDescriptor) MH_ATOI_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atoi$handle() {
        try {
            return (MethodHandle) MH_ATOI_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atoi$address() {
        try {
            return (MemorySegment) MH_ATOI_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int atoi(MemorySegment arg0) {
        try {
            return (int) MH_ATOI.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atol$descriptor() {
        try {
            return (FunctionDescriptor) MH_ATOL_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atol$handle() {
        try {
            return (MethodHandle) MH_ATOL_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atol$address() {
        try {
            return (MemorySegment) MH_ATOL_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long atol(MemorySegment arg0) {
        try {
            return (long) MH_ATOL.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atoll$descriptor() {
        try {
            return (FunctionDescriptor) MH_ATOLL_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atoll$handle() {
        try {
            return (MethodHandle) MH_ATOLL_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atoll$address() {
        try {
            return (MemorySegment) MH_ATOLL_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long atoll(MemorySegment arg0) {
        try {
            return (long) MH_ATOLL.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtod$descriptor() {
        try {
            return (FunctionDescriptor) MH_STRTOD_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtod$handle() {
        try {
            return (MethodHandle) MH_STRTOD_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtod$address() {
        try {
            return (MemorySegment) MH_STRTOD_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static double strtod(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (double) MH_STRTOD.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtof$descriptor() {
        try {
            return (FunctionDescriptor) MH_STRTOF_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtof$handle() {
        try {
            return (MethodHandle) MH_STRTOF_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtof$address() {
        try {
            return (MemorySegment) MH_STRTOF_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static float strtof(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (float) MH_STRTOF.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtol$descriptor() {
        try {
            return (FunctionDescriptor) MH_STRTOL_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtol$handle() {
        try {
            return (MethodHandle) MH_STRTOL_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtol$address() {
        try {
            return (MemorySegment) MH_STRTOL_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtol(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) MH_STRTOL.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtoul$descriptor() {
        try {
            return (FunctionDescriptor) MH_STRTOUL_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtoul$handle() {
        try {
            return (MethodHandle) MH_STRTOUL_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtoul$address() {
        try {
            return (MemorySegment) MH_STRTOUL_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtoul(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) MH_STRTOUL.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtoq$descriptor() {
        try {
            return (FunctionDescriptor) MH_STRTOQ_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtoq$handle() {
        try {
            return (MethodHandle) MH_STRTOQ_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtoq$address() {
        try {
            return (MemorySegment) MH_STRTOQ_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtoq(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) MH_STRTOQ.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtouq$descriptor() {
        try {
            return (FunctionDescriptor) MH_STRTOUQ_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtouq$handle() {
        try {
            return (MethodHandle) MH_STRTOUQ_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtouq$address() {
        try {
            return (MemorySegment) MH_STRTOUQ_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtouq(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) MH_STRTOUQ.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtoll$descriptor() {
        try {
            return (FunctionDescriptor) MH_STRTOLL_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtoll$handle() {
        try {
            return (MethodHandle) MH_STRTOLL_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtoll$address() {
        try {
            return (MemorySegment) MH_STRTOLL_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtoll(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) MH_STRTOLL.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtoull$descriptor() {
        try {
            return (FunctionDescriptor) MH_STRTOULL_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtoull$handle() {
        try {
            return (MethodHandle) MH_STRTOULL_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtoull$address() {
        try {
            return (MemorySegment) MH_STRTOULL_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtoull(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) MH_STRTOULL.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor l64a$descriptor() {
        try {
            return (FunctionDescriptor) MH_L64A_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle l64a$handle() {
        try {
            return (MethodHandle) MH_L64A_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment l64a$address() {
        try {
            return (MemorySegment) MH_L64A_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment l64a(long arg0) {
        try {
            return (MemorySegment) MH_L64A.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor a64l$descriptor() {
        try {
            return (FunctionDescriptor) MH_A64L_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle a64l$handle() {
        try {
            return (MethodHandle) MH_A64L_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment a64l$address() {
        try {
            return (MemorySegment) MH_A64L_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long a64l(MemorySegment arg0) {
        try {
            return (long) MH_A64L.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor select$descriptor() {
        try {
            return (FunctionDescriptor) MH_SELECT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle select$handle() {
        try {
            return (MethodHandle) MH_SELECT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment select$address() {
        try {
            return (MemorySegment) MH_SELECT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int select(int arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4) {
        try {
            return (int) MH_SELECT.invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor pselect$descriptor() {
        try {
            return (FunctionDescriptor) MH_PSELECT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle pselect$handle() {
        try {
            return (MethodHandle) MH_PSELECT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment pselect$address() {
        try {
            return (MemorySegment) MH_PSELECT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int pselect(int arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4, MemorySegment arg5) {
        try {
            return (int) MH_PSELECT.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor random$descriptor() {
        try {
            return (FunctionDescriptor) MH_RANDOM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle random$handle() {
        try {
            return (MethodHandle) MH_RANDOM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment random$address() {
        try {
            return (MemorySegment) MH_RANDOM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long random() {
        try {
            return (long) MH_RANDOM.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srandom$descriptor() {
        try {
            return (FunctionDescriptor) MH_SRANDOM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srandom$handle() {
        try {
            return (MethodHandle) MH_SRANDOM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srandom$address() {
        try {
            return (MemorySegment) MH_SRANDOM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void srandom(int arg0) {
        try {
            MH_SRANDOM.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor initstate$descriptor() {
        try {
            return (FunctionDescriptor) MH_INITSTATE_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle initstate$handle() {
        try {
            return (MethodHandle) MH_INITSTATE_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment initstate$address() {
        try {
            return (MemorySegment) MH_INITSTATE_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment initstate(int arg0, MemorySegment arg1, long arg2) {
        try {
            return (MemorySegment) MH_INITSTATE.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor setstate$descriptor() {
        try {
            return (FunctionDescriptor) MH_SETSTATE_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle setstate$handle() {
        try {
            return (MethodHandle) MH_SETSTATE_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment setstate$address() {
        try {
            return (MemorySegment) MH_SETSTATE_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment setstate(MemorySegment arg0) {
        try {
            return (MemorySegment) MH_SETSTATE.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor random_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_RANDOM_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle random_r$handle() {
        try {
            return (MethodHandle) MH_RANDOM_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment random_r$address() {
        try {
            return (MemorySegment) MH_RANDOM_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int random_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_RANDOM_R.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srandom_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_SRANDOM_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srandom_r$handle() {
        try {
            return (MethodHandle) MH_SRANDOM_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srandom_r$address() {
        try {
            return (MemorySegment) MH_SRANDOM_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int srandom_r(int arg0, MemorySegment arg1) {
        try {
            return (int) MH_SRANDOM_R.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor initstate_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_INITSTATE_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle initstate_r$handle() {
        try {
            return (MethodHandle) MH_INITSTATE_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment initstate_r$address() {
        try {
            return (MemorySegment) MH_INITSTATE_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int initstate_r(int arg0, MemorySegment arg1, long arg2, MemorySegment arg3) {
        try {
            return (int) MH_INITSTATE_R.invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor setstate_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_SETSTATE_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle setstate_r$handle() {
        try {
            return (MethodHandle) MH_SETSTATE_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment setstate_r$address() {
        try {
            return (MemorySegment) MH_SETSTATE_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int setstate_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_SETSTATE_R.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor rand$descriptor() {
        try {
            return (FunctionDescriptor) MH_RAND_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle rand$handle() {
        try {
            return (MethodHandle) MH_RAND_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment rand$address() {
        try {
            return (MemorySegment) MH_RAND_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int rand() {
        try {
            return (int) MH_RAND.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srand$descriptor() {
        try {
            return (FunctionDescriptor) MH_SRAND_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srand$handle() {
        try {
            return (MethodHandle) MH_SRAND_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srand$address() {
        try {
            return (MemorySegment) MH_SRAND_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void srand(int arg0) {
        try {
            MH_SRAND.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor rand_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_RAND_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle rand_r$handle() {
        try {
            return (MethodHandle) MH_RAND_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment rand_r$address() {
        try {
            return (MemorySegment) MH_RAND_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int rand_r(MemorySegment arg0) {
        try {
            return (int) MH_RAND_R.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor drand48$descriptor() {
        try {
            return (FunctionDescriptor) MH_DRAND48_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle drand48$handle() {
        try {
            return (MethodHandle) MH_DRAND48_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment drand48$address() {
        try {
            return (MemorySegment) MH_DRAND48_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static double drand48() {
        try {
            return (double) MH_DRAND48.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor erand48$descriptor() {
        try {
            return (FunctionDescriptor) MH_ERAND48_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle erand48$handle() {
        try {
            return (MethodHandle) MH_ERAND48_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment erand48$address() {
        try {
            return (MemorySegment) MH_ERAND48_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static double erand48(MemorySegment arg0) {
        try {
            return (double) MH_ERAND48.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lrand48$descriptor() {
        try {
            return (FunctionDescriptor) MH_LRAND48_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lrand48$handle() {
        try {
            return (MethodHandle) MH_LRAND48_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lrand48$address() {
        try {
            return (MemorySegment) MH_LRAND48_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long lrand48() {
        try {
            return (long) MH_LRAND48.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor nrand48$descriptor() {
        try {
            return (FunctionDescriptor) MH_NRAND48_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle nrand48$handle() {
        try {
            return (MethodHandle) MH_NRAND48_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment nrand48$address() {
        try {
            return (MemorySegment) MH_NRAND48_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long nrand48(MemorySegment arg0) {
        try {
            return (long) MH_NRAND48.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mrand48$descriptor() {
        try {
            return (FunctionDescriptor) MH_MRAND48_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mrand48$handle() {
        try {
            return (MethodHandle) MH_MRAND48_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mrand48$address() {
        try {
            return (MemorySegment) MH_MRAND48_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long mrand48() {
        try {
            return (long) MH_MRAND48.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor jrand48$descriptor() {
        try {
            return (FunctionDescriptor) MH_JRAND48_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle jrand48$handle() {
        try {
            return (MethodHandle) MH_JRAND48_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment jrand48$address() {
        try {
            return (MemorySegment) MH_JRAND48_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long jrand48(MemorySegment arg0) {
        try {
            return (long) MH_JRAND48.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srand48$descriptor() {
        try {
            return (FunctionDescriptor) MH_SRAND48_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srand48$handle() {
        try {
            return (MethodHandle) MH_SRAND48_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srand48$address() {
        try {
            return (MemorySegment) MH_SRAND48_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void srand48(long arg0) {
        try {
            MH_SRAND48.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor seed48$descriptor() {
        try {
            return (FunctionDescriptor) MH_SEED48_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle seed48$handle() {
        try {
            return (MethodHandle) MH_SEED48_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment seed48$address() {
        try {
            return (MemorySegment) MH_SEED48_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment seed48(MemorySegment arg0) {
        try {
            return (MemorySegment) MH_SEED48.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lcong48$descriptor() {
        try {
            return (FunctionDescriptor) MH_LCONG48_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lcong48$handle() {
        try {
            return (MethodHandle) MH_LCONG48_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lcong48$address() {
        try {
            return (MemorySegment) MH_LCONG48_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void lcong48(MemorySegment arg0) {
        try {
            MH_LCONG48.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor drand48_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_DRAND48_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle drand48_r$handle() {
        try {
            return (MethodHandle) MH_DRAND48_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment drand48_r$address() {
        try {
            return (MemorySegment) MH_DRAND48_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int drand48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_DRAND48_R.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor erand48_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_ERAND48_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle erand48_r$handle() {
        try {
            return (MethodHandle) MH_ERAND48_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment erand48_r$address() {
        try {
            return (MemorySegment) MH_ERAND48_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int erand48_r(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) MH_ERAND48_R.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lrand48_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_LRAND48_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lrand48_r$handle() {
        try {
            return (MethodHandle) MH_LRAND48_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lrand48_r$address() {
        try {
            return (MemorySegment) MH_LRAND48_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int lrand48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_LRAND48_R.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor nrand48_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_NRAND48_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle nrand48_r$handle() {
        try {
            return (MethodHandle) MH_NRAND48_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment nrand48_r$address() {
        try {
            return (MemorySegment) MH_NRAND48_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int nrand48_r(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) MH_NRAND48_R.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mrand48_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_MRAND48_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mrand48_r$handle() {
        try {
            return (MethodHandle) MH_MRAND48_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mrand48_r$address() {
        try {
            return (MemorySegment) MH_MRAND48_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mrand48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_MRAND48_R.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor jrand48_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_JRAND48_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle jrand48_r$handle() {
        try {
            return (MethodHandle) MH_JRAND48_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment jrand48_r$address() {
        try {
            return (MemorySegment) MH_JRAND48_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int jrand48_r(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) MH_JRAND48_R.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srand48_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_SRAND48_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srand48_r$handle() {
        try {
            return (MethodHandle) MH_SRAND48_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srand48_r$address() {
        try {
            return (MemorySegment) MH_SRAND48_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int srand48_r(long arg0, MemorySegment arg1) {
        try {
            return (int) MH_SRAND48_R.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor seed48_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_SEED48_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle seed48_r$handle() {
        try {
            return (MethodHandle) MH_SEED48_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment seed48_r$address() {
        try {
            return (MemorySegment) MH_SEED48_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int seed48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_SEED48_R.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lcong48_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_LCONG48_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lcong48_r$handle() {
        try {
            return (MethodHandle) MH_LCONG48_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lcong48_r$address() {
        try {
            return (MemorySegment) MH_LCONG48_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int lcong48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_LCONG48_R.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor arc4random$descriptor() {
        try {
            return (FunctionDescriptor) MH_ARC4RANDOM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle arc4random$handle() {
        try {
            return (MethodHandle) MH_ARC4RANDOM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment arc4random$address() {
        try {
            return (MemorySegment) MH_ARC4RANDOM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int arc4random() {
        try {
            return (int) MH_ARC4RANDOM.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor arc4random_buf$descriptor() {
        try {
            return (FunctionDescriptor) MH_ARC4RANDOM_BUF_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle arc4random_buf$handle() {
        try {
            return (MethodHandle) MH_ARC4RANDOM_BUF_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment arc4random_buf$address() {
        try {
            return (MemorySegment) MH_ARC4RANDOM_BUF_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void arc4random_buf(MemorySegment arg0, long arg1) {
        try {
            MH_ARC4RANDOM_BUF.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor arc4random_uniform$descriptor() {
        try {
            return (FunctionDescriptor) MH_ARC4RANDOM_UNIFORM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle arc4random_uniform$handle() {
        try {
            return (MethodHandle) MH_ARC4RANDOM_UNIFORM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment arc4random_uniform$address() {
        try {
            return (MemorySegment) MH_ARC4RANDOM_UNIFORM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int arc4random_uniform(int arg0) {
        try {
            return (int) MH_ARC4RANDOM_UNIFORM.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor malloc$descriptor() {
        try {
            return (FunctionDescriptor) MH_MALLOC_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle malloc$handle() {
        try {
            return (MethodHandle) MH_MALLOC_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment malloc$address() {
        try {
            return (MemorySegment) MH_MALLOC_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment malloc(long arg0) {
        try {
            return (MemorySegment) MH_MALLOC.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor calloc$descriptor() {
        try {
            return (FunctionDescriptor) MH_CALLOC_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle calloc$handle() {
        try {
            return (MethodHandle) MH_CALLOC_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment calloc$address() {
        try {
            return (MemorySegment) MH_CALLOC_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment calloc(long arg0, long arg1) {
        try {
            return (MemorySegment) MH_CALLOC.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor realloc$descriptor() {
        try {
            return (FunctionDescriptor) MH_REALLOC_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle realloc$handle() {
        try {
            return (MethodHandle) MH_REALLOC_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment realloc$address() {
        try {
            return (MemorySegment) MH_REALLOC_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment realloc(MemorySegment arg0, long arg1) {
        try {
            return (MemorySegment) MH_REALLOC.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor free$descriptor() {
        try {
            return (FunctionDescriptor) MH_FREE_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle free$handle() {
        try {
            return (MethodHandle) MH_FREE_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment free$address() {
        try {
            return (MemorySegment) MH_FREE_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void free(MemorySegment arg0) {
        try {
            MH_FREE.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor reallocarray$descriptor() {
        try {
            return (FunctionDescriptor) MH_REALLOCARRAY_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle reallocarray$handle() {
        try {
            return (MethodHandle) MH_REALLOCARRAY_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment reallocarray$address() {
        try {
            return (MemorySegment) MH_REALLOCARRAY_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment reallocarray(MemorySegment arg0, long arg1, long arg2) {
        try {
            return (MemorySegment) MH_REALLOCARRAY.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor alloca$descriptor() {
        try {
            return (FunctionDescriptor) MH_ALLOCA_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle alloca$handle() {
        try {
            return (MethodHandle) MH_ALLOCA_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment alloca$address() {
        try {
            return (MemorySegment) MH_ALLOCA_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment alloca(long arg0) {
        try {
            return (MemorySegment) MH_ALLOCA.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor valloc$descriptor() {
        try {
            return (FunctionDescriptor) MH_VALLOC_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle valloc$handle() {
        try {
            return (MethodHandle) MH_VALLOC_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment valloc$address() {
        try {
            return (MemorySegment) MH_VALLOC_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment valloc(long arg0) {
        try {
            return (MemorySegment) MH_VALLOC.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor posix_memalign$descriptor() {
        try {
            return (FunctionDescriptor) MH_POSIX_MEMALIGN_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle posix_memalign$handle() {
        try {
            return (MethodHandle) MH_POSIX_MEMALIGN_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment posix_memalign$address() {
        try {
            return (MemorySegment) MH_POSIX_MEMALIGN_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int posix_memalign(MemorySegment arg0, long arg1, long arg2) {
        try {
            return (int) MH_POSIX_MEMALIGN.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor aligned_alloc$descriptor() {
        try {
            return (FunctionDescriptor) MH_ALIGNED_ALLOC_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle aligned_alloc$handle() {
        try {
            return (MethodHandle) MH_ALIGNED_ALLOC_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment aligned_alloc$address() {
        try {
            return (MemorySegment) MH_ALIGNED_ALLOC_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment aligned_alloc(long arg0, long arg1) {
        try {
            return (MemorySegment) MH_ALIGNED_ALLOC.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor abort$descriptor() {
        try {
            return (FunctionDescriptor) MH_ABORT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle abort$handle() {
        try {
            return (MethodHandle) MH_ABORT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment abort$address() {
        try {
            return (MemorySegment) MH_ABORT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void abort() {
        try {
            MH_ABORT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atexit$descriptor() {
        try {
            return (FunctionDescriptor) MH_ATEXIT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atexit$handle() {
        try {
            return (MethodHandle) MH_ATEXIT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atexit$address() {
        try {
            return (MemorySegment) MH_ATEXIT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int atexit(MemorySegment arg0) {
        try {
            return (int) MH_ATEXIT.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor at_quick_exit$descriptor() {
        try {
            return (FunctionDescriptor) MH_AT_QUICK_EXIT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle at_quick_exit$handle() {
        try {
            return (MethodHandle) MH_AT_QUICK_EXIT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment at_quick_exit$address() {
        try {
            return (MemorySegment) MH_AT_QUICK_EXIT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int at_quick_exit(MemorySegment arg0) {
        try {
            return (int) MH_AT_QUICK_EXIT.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor on_exit$descriptor() {
        try {
            return (FunctionDescriptor) MH_ON_EXIT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle on_exit$handle() {
        try {
            return (MethodHandle) MH_ON_EXIT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment on_exit$address() {
        try {
            return (MemorySegment) MH_ON_EXIT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int on_exit(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_ON_EXIT.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor exit$descriptor() {
        try {
            return (FunctionDescriptor) MH_EXIT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle exit$handle() {
        try {
            return (MethodHandle) MH_EXIT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment exit$address() {
        try {
            return (MemorySegment) MH_EXIT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void exit(int arg0) {
        try {
            MH_EXIT.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor quick_exit$descriptor() {
        try {
            return (FunctionDescriptor) MH_QUICK_EXIT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle quick_exit$handle() {
        try {
            return (MethodHandle) MH_QUICK_EXIT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment quick_exit$address() {
        try {
            return (MemorySegment) MH_QUICK_EXIT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void quick_exit(int arg0) {
        try {
            MH_QUICK_EXIT.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor _Exit$descriptor() {
        try {
            return (FunctionDescriptor) MH_EXIT_DESCRIPTOR_1.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle _Exit$handle() {
        try {
            return (MethodHandle) MH_EXIT_HANDLE_1.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment _Exit$address() {
        try {
            return (MemorySegment) MH_EXIT_ADDRESS_1.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void _Exit(int arg0) {
        try {
            MH_EXIT_1.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor getenv$descriptor() {
        try {
            return (FunctionDescriptor) MH_GETENV_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle getenv$handle() {
        try {
            return (MethodHandle) MH_GETENV_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment getenv$address() {
        try {
            return (MemorySegment) MH_GETENV_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment getenv(MemorySegment arg0) {
        try {
            return (MemorySegment) MH_GETENV.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor putenv$descriptor() {
        try {
            return (FunctionDescriptor) MH_PUTENV_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle putenv$handle() {
        try {
            return (MethodHandle) MH_PUTENV_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment putenv$address() {
        try {
            return (MemorySegment) MH_PUTENV_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int putenv(MemorySegment arg0) {
        try {
            return (int) MH_PUTENV.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor setenv$descriptor() {
        try {
            return (FunctionDescriptor) MH_SETENV_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle setenv$handle() {
        try {
            return (MethodHandle) MH_SETENV_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment setenv$address() {
        try {
            return (MemorySegment) MH_SETENV_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int setenv(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (int) MH_SETENV.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor unsetenv$descriptor() {
        try {
            return (FunctionDescriptor) MH_UNSETENV_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle unsetenv$handle() {
        try {
            return (MethodHandle) MH_UNSETENV_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment unsetenv$address() {
        try {
            return (MemorySegment) MH_UNSETENV_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int unsetenv(MemorySegment arg0) {
        try {
            return (int) MH_UNSETENV.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor clearenv$descriptor() {
        try {
            return (FunctionDescriptor) MH_CLEARENV_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle clearenv$handle() {
        try {
            return (MethodHandle) MH_CLEARENV_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment clearenv$address() {
        try {
            return (MemorySegment) MH_CLEARENV_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int clearenv() {
        try {
            return (int) MH_CLEARENV.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mktemp$descriptor() {
        try {
            return (FunctionDescriptor) MH_MKTEMP_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mktemp$handle() {
        try {
            return (MethodHandle) MH_MKTEMP_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mktemp$address() {
        try {
            return (MemorySegment) MH_MKTEMP_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mktemp(MemorySegment arg0) {
        try {
            return (MemorySegment) MH_MKTEMP.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mkstemp$descriptor() {
        try {
            return (FunctionDescriptor) MH_MKSTEMP_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mkstemp$handle() {
        try {
            return (MethodHandle) MH_MKSTEMP_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mkstemp$address() {
        try {
            return (MemorySegment) MH_MKSTEMP_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mkstemp(MemorySegment arg0) {
        try {
            return (int) MH_MKSTEMP.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mkstemps$descriptor() {
        try {
            return (FunctionDescriptor) MH_MKSTEMPS_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mkstemps$handle() {
        try {
            return (MethodHandle) MH_MKSTEMPS_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mkstemps$address() {
        try {
            return (MemorySegment) MH_MKSTEMPS_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mkstemps(MemorySegment arg0, int arg1) {
        try {
            return (int) MH_MKSTEMPS.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mkdtemp$descriptor() {
        try {
            return (FunctionDescriptor) MH_MKDTEMP_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mkdtemp$handle() {
        try {
            return (MethodHandle) MH_MKDTEMP_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mkdtemp$address() {
        try {
            return (MemorySegment) MH_MKDTEMP_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mkdtemp(MemorySegment arg0) {
        try {
            return (MemorySegment) MH_MKDTEMP.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor system$descriptor() {
        try {
            return (FunctionDescriptor) MH_SYSTEM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle system$handle() {
        try {
            return (MethodHandle) MH_SYSTEM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment system$address() {
        try {
            return (MemorySegment) MH_SYSTEM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int system(MemorySegment arg0) {
        try {
            return (int) MH_SYSTEM.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor realpath$descriptor() {
        try {
            return (FunctionDescriptor) MH_REALPATH_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle realpath$handle() {
        try {
            return (MethodHandle) MH_REALPATH_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment realpath$address() {
        try {
            return (MemorySegment) MH_REALPATH_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment realpath(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (MemorySegment) MH_REALPATH.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor bsearch$descriptor() {
        try {
            return (FunctionDescriptor) MH_BSEARCH_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle bsearch$handle() {
        try {
            return (MethodHandle) MH_BSEARCH_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment bsearch$address() {
        try {
            return (MemorySegment) MH_BSEARCH_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment bsearch(MemorySegment arg0, MemorySegment arg1, long arg2, long arg3, MemorySegment arg4) {
        try {
            return (MemorySegment) MH_BSEARCH.invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor qsort$descriptor() {
        try {
            return (FunctionDescriptor) MH_QSORT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle qsort$handle() {
        try {
            return (MethodHandle) MH_QSORT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment qsort$address() {
        try {
            return (MemorySegment) MH_QSORT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void qsort(MemorySegment arg0, long arg1, long arg2, MemorySegment arg3) {
        try {
            MH_QSORT.invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor abs$descriptor() {
        try {
            return (FunctionDescriptor) MH_ABS_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle abs$handle() {
        try {
            return (MethodHandle) MH_ABS_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment abs$address() {
        try {
            return (MemorySegment) MH_ABS_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int abs(int arg0) {
        try {
            return (int) MH_ABS.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor labs$descriptor() {
        try {
            return (FunctionDescriptor) MH_LABS_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle labs$handle() {
        try {
            return (MethodHandle) MH_LABS_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment labs$address() {
        try {
            return (MemorySegment) MH_LABS_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long labs(long arg0) {
        try {
            return (long) MH_LABS.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor llabs$descriptor() {
        try {
            return (FunctionDescriptor) MH_LLABS_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle llabs$handle() {
        try {
            return (MethodHandle) MH_LLABS_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment llabs$address() {
        try {
            return (MemorySegment) MH_LLABS_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long llabs(long arg0) {
        try {
            return (long) MH_LLABS.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor div$descriptor() {
        try {
            return (FunctionDescriptor) MH_DIV_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle div$handle() {
        try {
            return (MethodHandle) MH_DIV_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment div$address() {
        try {
            return (MemorySegment) MH_DIV_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment div(SegmentAllocator arg0, int arg1, int arg2) {
        try {
            return (MemorySegment) MH_DIV.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor ldiv$descriptor() {
        try {
            return (FunctionDescriptor) MH_LDIV_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle ldiv$handle() {
        try {
            return (MethodHandle) MH_LDIV_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ldiv$address() {
        try {
            return (MemorySegment) MH_LDIV_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ldiv(SegmentAllocator arg0, long arg1, long arg2) {
        try {
            return (MemorySegment) MH_LDIV.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lldiv$descriptor() {
        try {
            return (FunctionDescriptor) MH_LLDIV_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lldiv$handle() {
        try {
            return (MethodHandle) MH_LLDIV_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lldiv$address() {
        try {
            return (MemorySegment) MH_LLDIV_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lldiv(SegmentAllocator arg0, long arg1, long arg2) {
        try {
            return (MemorySegment) MH_LLDIV.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor ecvt$descriptor() {
        try {
            return (FunctionDescriptor) MH_ECVT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle ecvt$handle() {
        try {
            return (MethodHandle) MH_ECVT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ecvt$address() {
        try {
            return (MemorySegment) MH_ECVT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ecvt(double arg0, int arg1, MemorySegment arg2, MemorySegment arg3) {
        try {
            return (MemorySegment) MH_ECVT.invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor fcvt$descriptor() {
        try {
            return (FunctionDescriptor) MH_FCVT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle fcvt$handle() {
        try {
            return (MethodHandle) MH_FCVT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment fcvt$address() {
        try {
            return (MemorySegment) MH_FCVT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment fcvt(double arg0, int arg1, MemorySegment arg2, MemorySegment arg3) {
        try {
            return (MemorySegment) MH_FCVT.invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor gcvt$descriptor() {
        try {
            return (FunctionDescriptor) MH_GCVT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle gcvt$handle() {
        try {
            return (MethodHandle) MH_GCVT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment gcvt$address() {
        try {
            return (MemorySegment) MH_GCVT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment gcvt(double arg0, int arg1, MemorySegment arg2) {
        try {
            return (MemorySegment) MH_GCVT.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor ecvt_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_ECVT_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle ecvt_r$handle() {
        try {
            return (MethodHandle) MH_ECVT_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ecvt_r$address() {
        try {
            return (MemorySegment) MH_ECVT_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int ecvt_r(double arg0, int arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4, long arg5) {
        try {
            return (int) MH_ECVT_R.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor fcvt_r$descriptor() {
        try {
            return (FunctionDescriptor) MH_FCVT_R_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle fcvt_r$handle() {
        try {
            return (MethodHandle) MH_FCVT_R_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment fcvt_r$address() {
        try {
            return (MemorySegment) MH_FCVT_R_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int fcvt_r(double arg0, int arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4, long arg5) {
        try {
            return (int) MH_FCVT_R.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mblen$descriptor() {
        try {
            return (FunctionDescriptor) MH_MBLEN_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mblen$handle() {
        try {
            return (MethodHandle) MH_MBLEN_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mblen$address() {
        try {
            return (MemorySegment) MH_MBLEN_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mblen(MemorySegment arg0, long arg1) {
        try {
            return (int) MH_MBLEN.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mbtowc$descriptor() {
        try {
            return (FunctionDescriptor) MH_MBTOWC_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mbtowc$handle() {
        try {
            return (MethodHandle) MH_MBTOWC_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mbtowc$address() {
        try {
            return (MemorySegment) MH_MBTOWC_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mbtowc(MemorySegment arg0, MemorySegment arg1, long arg2) {
        try {
            return (int) MH_MBTOWC.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor wctomb$descriptor() {
        try {
            return (FunctionDescriptor) MH_WCTOMB_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle wctomb$handle() {
        try {
            return (MethodHandle) MH_WCTOMB_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment wctomb$address() {
        try {
            return (MemorySegment) MH_WCTOMB_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int wctomb(MemorySegment arg0, int arg1) {
        try {
            return (int) MH_WCTOMB.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mbstowcs$descriptor() {
        try {
            return (FunctionDescriptor) MH_MBSTOWCS_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mbstowcs$handle() {
        try {
            return (MethodHandle) MH_MBSTOWCS_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mbstowcs$address() {
        try {
            return (MemorySegment) MH_MBSTOWCS_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long mbstowcs(MemorySegment arg0, MemorySegment arg1, long arg2) {
        try {
            return (long) MH_MBSTOWCS.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor wcstombs$descriptor() {
        try {
            return (FunctionDescriptor) MH_WCSTOMBS_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle wcstombs$handle() {
        try {
            return (MethodHandle) MH_WCSTOMBS_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment wcstombs$address() {
        try {
            return (MemorySegment) MH_WCSTOMBS_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long wcstombs(MemorySegment arg0, MemorySegment arg1, long arg2) {
        try {
            return (long) MH_WCSTOMBS.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor rpmatch$descriptor() {
        try {
            return (FunctionDescriptor) MH_RPMATCH_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle rpmatch$handle() {
        try {
            return (MethodHandle) MH_RPMATCH_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment rpmatch$address() {
        try {
            return (MemorySegment) MH_RPMATCH_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int rpmatch(MemorySegment arg0) {
        try {
            return (int) MH_RPMATCH.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor getsubopt$descriptor() {
        try {
            return (FunctionDescriptor) MH_GETSUBOPT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle getsubopt$handle() {
        try {
            return (MethodHandle) MH_GETSUBOPT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment getsubopt$address() {
        try {
            return (MemorySegment) MH_GETSUBOPT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int getsubopt(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) MH_GETSUBOPT.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor getloadavg$descriptor() {
        try {
            return (FunctionDescriptor) MH_GETLOADAVG_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle getloadavg$handle() {
        try {
            return (MethodHandle) MH_GETLOADAVG_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment getloadavg$address() {
        try {
            return (MemorySegment) MH_GETLOADAVG_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int getloadavg(MemorySegment arg0, int arg1) {
        try {
            return (int) MH_GETLOADAVG.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_free_database$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_FREE_DATABASE_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_free_database$handle() {
        try {
            return (MethodHandle) MH_HS_FREE_DATABASE_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_free_database$address() {
        try {
            return (MemorySegment) MH_HS_FREE_DATABASE_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_free_database(MemorySegment arg0) {
        try {
            return (int) MH_HS_FREE_DATABASE.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_serialize_database$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_SERIALIZE_DATABASE_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_serialize_database$handle() {
        try {
            return (MethodHandle) MH_HS_SERIALIZE_DATABASE_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_serialize_database$address() {
        try {
            return (MemorySegment) MH_HS_SERIALIZE_DATABASE_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_serialize_database(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) MH_HS_SERIALIZE_DATABASE.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_deserialize_database$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_DESERIALIZE_DATABASE_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_deserialize_database$handle() {
        try {
            return (MethodHandle) MH_HS_DESERIALIZE_DATABASE_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_deserialize_database$address() {
        try {
            return (MemorySegment) MH_HS_DESERIALIZE_DATABASE_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_deserialize_database(MemorySegment arg0, long arg1, MemorySegment arg2) {
        try {
            return (int) MH_HS_DESERIALIZE_DATABASE.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_deserialize_database_at$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_DESERIALIZE_DATABASE_AT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_deserialize_database_at$handle() {
        try {
            return (MethodHandle) MH_HS_DESERIALIZE_DATABASE_AT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_deserialize_database_at$address() {
        try {
            return (MemorySegment) MH_HS_DESERIALIZE_DATABASE_AT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_deserialize_database_at(MemorySegment arg0, long arg1, MemorySegment arg2) {
        try {
            return (int) MH_HS_DESERIALIZE_DATABASE_AT.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_stream_size$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_STREAM_SIZE_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_stream_size$handle() {
        try {
            return (MethodHandle) MH_HS_STREAM_SIZE_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_stream_size$address() {
        try {
            return (MemorySegment) MH_HS_STREAM_SIZE_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_stream_size(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_HS_STREAM_SIZE.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_database_size$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_DATABASE_SIZE_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_database_size$handle() {
        try {
            return (MethodHandle) MH_HS_DATABASE_SIZE_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_database_size$address() {
        try {
            return (MemorySegment) MH_HS_DATABASE_SIZE_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_database_size(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_HS_DATABASE_SIZE.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_serialized_database_size$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_SERIALIZED_DATABASE_SIZE_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_serialized_database_size$handle() {
        try {
            return (MethodHandle) MH_HS_SERIALIZED_DATABASE_SIZE_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_serialized_database_size$address() {
        try {
            return (MemorySegment) MH_HS_SERIALIZED_DATABASE_SIZE_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_serialized_database_size(MemorySegment arg0, long arg1, MemorySegment arg2) {
        try {
            return (int) MH_HS_SERIALIZED_DATABASE_SIZE.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_database_info$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_DATABASE_INFO_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_database_info$handle() {
        try {
            return (MethodHandle) MH_HS_DATABASE_INFO_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_database_info$address() {
        try {
            return (MemorySegment) MH_HS_DATABASE_INFO_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_database_info(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_HS_DATABASE_INFO.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_serialized_database_info$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_SERIALIZED_DATABASE_INFO_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_serialized_database_info$handle() {
        try {
            return (MethodHandle) MH_HS_SERIALIZED_DATABASE_INFO_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_serialized_database_info$address() {
        try {
            return (MemorySegment) MH_HS_SERIALIZED_DATABASE_INFO_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_serialized_database_info(MemorySegment arg0, long arg1, MemorySegment arg2) {
        try {
            return (int) MH_HS_SERIALIZED_DATABASE_INFO.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_allocator$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_SET_ALLOCATOR_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_allocator$handle() {
        try {
            return (MethodHandle) MH_HS_SET_ALLOCATOR_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_allocator$address() {
        try {
            return (MemorySegment) MH_HS_SET_ALLOCATOR_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_HS_SET_ALLOCATOR.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_database_allocator$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_SET_DATABASE_ALLOCATOR_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_database_allocator$handle() {
        try {
            return (MethodHandle) MH_HS_SET_DATABASE_ALLOCATOR_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_database_allocator$address() {
        try {
            return (MemorySegment) MH_HS_SET_DATABASE_ALLOCATOR_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_database_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_HS_SET_DATABASE_ALLOCATOR.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_misc_allocator$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_SET_MISC_ALLOCATOR_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_misc_allocator$handle() {
        try {
            return (MethodHandle) MH_HS_SET_MISC_ALLOCATOR_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_misc_allocator$address() {
        try {
            return (MemorySegment) MH_HS_SET_MISC_ALLOCATOR_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_misc_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_HS_SET_MISC_ALLOCATOR.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_scratch_allocator$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_SET_SCRATCH_ALLOCATOR_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_scratch_allocator$handle() {
        try {
            return (MethodHandle) MH_HS_SET_SCRATCH_ALLOCATOR_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_scratch_allocator$address() {
        try {
            return (MemorySegment) MH_HS_SET_SCRATCH_ALLOCATOR_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_scratch_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_HS_SET_SCRATCH_ALLOCATOR.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_stream_allocator$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_SET_STREAM_ALLOCATOR_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_stream_allocator$handle() {
        try {
            return (MethodHandle) MH_HS_SET_STREAM_ALLOCATOR_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_stream_allocator$address() {
        try {
            return (MemorySegment) MH_HS_SET_STREAM_ALLOCATOR_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_stream_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_HS_SET_STREAM_ALLOCATOR.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_version$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_VERSION_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_version$handle() {
        try {
            return (MethodHandle) MH_HS_VERSION_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_version$address() {
        try {
            return (MemorySegment) MH_HS_VERSION_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_version() {
        try {
            return (MemorySegment) MH_HS_VERSION.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_valid_platform$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_VALID_PLATFORM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_valid_platform$handle() {
        try {
            return (MethodHandle) MH_HS_VALID_PLATFORM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_valid_platform$address() {
        try {
            return (MemorySegment) MH_HS_VALID_PLATFORM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_valid_platform() {
        try {
            return (int) MH_HS_VALID_PLATFORM.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_COMPILE_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile$handle() {
        try {
            return (MethodHandle) MH_HS_COMPILE_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile$address() {
        try {
            return (MemorySegment) MH_HS_COMPILE_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile(MemorySegment arg0, int arg1, int arg2, MemorySegment arg3, MemorySegment arg4, MemorySegment arg5) {
        try {
            return (int) MH_HS_COMPILE.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile_multi$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_COMPILE_MULTI_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile_multi$handle() {
        try {
            return (MethodHandle) MH_HS_COMPILE_MULTI_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile_multi$address() {
        try {
            return (MemorySegment) MH_HS_COMPILE_MULTI_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile_multi(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, int arg3, int arg4, MemorySegment arg5, MemorySegment arg6, MemorySegment arg7) {
        try {
            return (int) MH_HS_COMPILE_MULTI.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile_ext_multi$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_COMPILE_EXT_MULTI_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile_ext_multi$handle() {
        try {
            return (MethodHandle) MH_HS_COMPILE_EXT_MULTI_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile_ext_multi$address() {
        try {
            return (MemorySegment) MH_HS_COMPILE_EXT_MULTI_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile_ext_multi(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, int arg4, int arg5, MemorySegment arg6, MemorySegment arg7, MemorySegment arg8) {
        try {
            return (int) MH_HS_COMPILE_EXT_MULTI.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile_lit$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_COMPILE_LIT_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile_lit$handle() {
        try {
            return (MethodHandle) MH_HS_COMPILE_LIT_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile_lit$address() {
        try {
            return (MemorySegment) MH_HS_COMPILE_LIT_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile_lit(MemorySegment arg0, int arg1, long arg2, int arg3, MemorySegment arg4, MemorySegment arg5, MemorySegment arg6) {
        try {
            return (int) MH_HS_COMPILE_LIT.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile_lit_multi$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_COMPILE_LIT_MULTI_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile_lit_multi$handle() {
        try {
            return (MethodHandle) MH_HS_COMPILE_LIT_MULTI_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile_lit_multi$address() {
        try {
            return (MemorySegment) MH_HS_COMPILE_LIT_MULTI_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile_lit_multi(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, int arg4, int arg5, MemorySegment arg6, MemorySegment arg7, MemorySegment arg8) {
        try {
            return (int) MH_HS_COMPILE_LIT_MULTI.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_free_compile_error$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_FREE_COMPILE_ERROR_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_free_compile_error$handle() {
        try {
            return (MethodHandle) MH_HS_FREE_COMPILE_ERROR_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_free_compile_error$address() {
        try {
            return (MemorySegment) MH_HS_FREE_COMPILE_ERROR_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_free_compile_error(MemorySegment arg0) {
        try {
            return (int) MH_HS_FREE_COMPILE_ERROR.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_expression_info$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_EXPRESSION_INFO_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_expression_info$handle() {
        try {
            return (MethodHandle) MH_HS_EXPRESSION_INFO_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_expression_info$address() {
        try {
            return (MemorySegment) MH_HS_EXPRESSION_INFO_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_expression_info(MemorySegment arg0, int arg1, MemorySegment arg2, MemorySegment arg3) {
        try {
            return (int) MH_HS_EXPRESSION_INFO.invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_expression_ext_info$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_EXPRESSION_EXT_INFO_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_expression_ext_info$handle() {
        try {
            return (MethodHandle) MH_HS_EXPRESSION_EXT_INFO_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_expression_ext_info$address() {
        try {
            return (MemorySegment) MH_HS_EXPRESSION_EXT_INFO_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_expression_ext_info(MemorySegment arg0, int arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4) {
        try {
            return (int) MH_HS_EXPRESSION_EXT_INFO.invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_populate_platform$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_POPULATE_PLATFORM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_populate_platform$handle() {
        try {
            return (MethodHandle) MH_HS_POPULATE_PLATFORM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_populate_platform$address() {
        try {
            return (MemorySegment) MH_HS_POPULATE_PLATFORM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_populate_platform(MemorySegment arg0) {
        try {
            return (int) MH_HS_POPULATE_PLATFORM.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_open_stream$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_OPEN_STREAM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_open_stream$handle() {
        try {
            return (MethodHandle) MH_HS_OPEN_STREAM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_open_stream$address() {
        try {
            return (MemorySegment) MH_HS_OPEN_STREAM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_open_stream(MemorySegment arg0, int arg1, MemorySegment arg2) {
        try {
            return (int) MH_HS_OPEN_STREAM.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_scan_stream$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_SCAN_STREAM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_scan_stream$handle() {
        try {
            return (MethodHandle) MH_HS_SCAN_STREAM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_scan_stream$address() {
        try {
            return (MemorySegment) MH_HS_SCAN_STREAM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_scan_stream(MemorySegment arg0, MemorySegment arg1, int arg2, int arg3, MemorySegment arg4, MemorySegment arg5, MemorySegment arg6) {
        try {
            return (int) MH_HS_SCAN_STREAM.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_close_stream$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_CLOSE_STREAM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_close_stream$handle() {
        try {
            return (MethodHandle) MH_HS_CLOSE_STREAM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_close_stream$address() {
        try {
            return (MemorySegment) MH_HS_CLOSE_STREAM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_close_stream(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3) {
        try {
            return (int) MH_HS_CLOSE_STREAM.invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_reset_stream$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_RESET_STREAM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_reset_stream$handle() {
        try {
            return (MethodHandle) MH_HS_RESET_STREAM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_reset_stream$address() {
        try {
            return (MemorySegment) MH_HS_RESET_STREAM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_reset_stream(MemorySegment arg0, int arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4) {
        try {
            return (int) MH_HS_RESET_STREAM.invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_copy_stream$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_COPY_STREAM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_copy_stream$handle() {
        try {
            return (MethodHandle) MH_HS_COPY_STREAM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_copy_stream$address() {
        try {
            return (MemorySegment) MH_HS_COPY_STREAM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_copy_stream(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_HS_COPY_STREAM.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_reset_and_copy_stream$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_RESET_AND_COPY_STREAM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_reset_and_copy_stream$handle() {
        try {
            return (MethodHandle) MH_HS_RESET_AND_COPY_STREAM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_reset_and_copy_stream$address() {
        try {
            return (MemorySegment) MH_HS_RESET_AND_COPY_STREAM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_reset_and_copy_stream(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4) {
        try {
            return (int) MH_HS_RESET_AND_COPY_STREAM.invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compress_stream$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_COMPRESS_STREAM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compress_stream$handle() {
        try {
            return (MethodHandle) MH_HS_COMPRESS_STREAM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compress_stream$address() {
        try {
            return (MemorySegment) MH_HS_COMPRESS_STREAM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compress_stream(MemorySegment arg0, MemorySegment arg1, long arg2, MemorySegment arg3) {
        try {
            return (int) MH_HS_COMPRESS_STREAM.invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_expand_stream$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_EXPAND_STREAM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_expand_stream$handle() {
        try {
            return (MethodHandle) MH_HS_EXPAND_STREAM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_expand_stream$address() {
        try {
            return (MemorySegment) MH_HS_EXPAND_STREAM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_expand_stream(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, long arg3) {
        try {
            return (int) MH_HS_EXPAND_STREAM.invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_reset_and_expand_stream$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_RESET_AND_EXPAND_STREAM_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_reset_and_expand_stream$handle() {
        try {
            return (MethodHandle) MH_HS_RESET_AND_EXPAND_STREAM_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_reset_and_expand_stream$address() {
        try {
            return (MemorySegment) MH_HS_RESET_AND_EXPAND_STREAM_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_reset_and_expand_stream(MemorySegment arg0, MemorySegment arg1, long arg2, MemorySegment arg3, MemorySegment arg4, MemorySegment arg5) {
        try {
            return (int) MH_HS_RESET_AND_EXPAND_STREAM.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_scan$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_SCAN_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_scan$handle() {
        try {
            return (MethodHandle) MH_HS_SCAN_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_scan$address() {
        try {
            return (MemorySegment) MH_HS_SCAN_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_scan(MemorySegment arg0, MemorySegment arg1, int arg2, int arg3, MemorySegment arg4, MemorySegment arg5, MemorySegment arg6) {
        try {
            return (int) MH_HS_SCAN.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_scan_vector$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_SCAN_VECTOR_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_scan_vector$handle() {
        try {
            return (MethodHandle) MH_HS_SCAN_VECTOR_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_scan_vector$address() {
        try {
            return (MemorySegment) MH_HS_SCAN_VECTOR_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_scan_vector(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, int arg3, int arg4, MemorySegment arg5, MemorySegment arg6, MemorySegment arg7) {
        try {
            return (int) MH_HS_SCAN_VECTOR.invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_alloc_scratch$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_ALLOC_SCRATCH_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_alloc_scratch$handle() {
        try {
            return (MethodHandle) MH_HS_ALLOC_SCRATCH_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_alloc_scratch$address() {
        try {
            return (MemorySegment) MH_HS_ALLOC_SCRATCH_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_alloc_scratch(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_HS_ALLOC_SCRATCH.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_clone_scratch$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_CLONE_SCRATCH_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_clone_scratch$handle() {
        try {
            return (MethodHandle) MH_HS_CLONE_SCRATCH_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_clone_scratch$address() {
        try {
            return (MemorySegment) MH_HS_CLONE_SCRATCH_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_clone_scratch(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_HS_CLONE_SCRATCH.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_scratch_size$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_SCRATCH_SIZE_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_scratch_size$handle() {
        try {
            return (MethodHandle) MH_HS_SCRATCH_SIZE_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_scratch_size$address() {
        try {
            return (MemorySegment) MH_HS_SCRATCH_SIZE_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_scratch_size(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) MH_HS_SCRATCH_SIZE.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_free_scratch$descriptor() {
        try {
            return (FunctionDescriptor) MH_HS_FREE_SCRATCH_DESCRIPTOR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_free_scratch$handle() {
        try {
            return (MethodHandle) MH_HS_FREE_SCRATCH_HANDLE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_free_scratch$address() {
        try {
            return (MemorySegment) MH_HS_FREE_SCRATCH_ADDRESS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_free_scratch(MemorySegment arg0) {
        try {
            return (int) MH_HS_FREE_SCRATCH.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long _POSIX_C_SOURCE() {
        try {
            return (long) MH_POSIX_C_SOURCE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __TIMESIZE() {
        try {
            return (int) MH_TIMESIZE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long __STDC_IEC_60559_BFP__() {
        try {
            return (long) MH_STDC_IEC_60559_BFP.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long __STDC_IEC_60559_COMPLEX__() {
        try {
            return (long) MH_STDC_IEC_60559_COMPLEX.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long __STDC_ISO_10646__() {
        try {
            return (long) MH_STDC_ISO_10646.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment NULL() {
        try {
            return (MemorySegment) MH_NULL.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WCLONE() {
        try {
            return (int) MH_WCLONE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT16() {
        try {
            return (int) MH_HAVE_DISTINCT_FLOAT16.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT128X() {
        try {
            return (int) MH_HAVE_DISTINCT_FLOAT128X.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT128_UNLIKE_LDBL() {
        try {
            return (int) MH_HAVE_FLOAT128_UNLIKE_LDBL.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __BYTE_ORDER() {
        try {
            return (int) MH_BYTE_ORDER.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __FLOAT_WORD_ORDER() {
        try {
            return (int) MH_FLOAT_WORD_ORDER.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int LITTLE_ENDIAN() {
        try {
            return (int) MH_LITTLE_ENDIAN_1.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int BIG_ENDIAN() {
        try {
            return (int) MH_BIG_ENDIAN_1.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int PDP_ENDIAN() {
        try {
            return (int) MH_PDP_ENDIAN_1.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int BYTE_ORDER() {
        try {
            return (int) MH_BYTE_ORDER_1.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long _SIGSET_NWORDS() {
        try {
            return (long) MH_SIGSET_NWORDS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __NFDBITS() {
        try {
            return (int) MH_NFDBITS.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int FD_SETSIZE() {
        try {
            return (int) MH_FD_SETSIZE_1.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int NFDBITS() {
        try {
            return (int) MH_NFDBITS_1.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __PTHREAD_RWLOCK_ELISION_EXTRA() {
        try {
            return (int) MH_PTHREAD_RWLOCK_ELISION_EXTRA.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_INVALID() {
        try {
            return (int) MH_HS_INVALID.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_NOMEM() {
        try {
            return (int) MH_HS_NOMEM.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_SCAN_TERMINATED() {
        try {
            return (int) MH_HS_SCAN_TERMINATED.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_COMPILER_ERROR() {
        try {
            return (int) MH_HS_COMPILER_ERROR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_DB_VERSION_ERROR() {
        try {
            return (int) MH_HS_DB_VERSION_ERROR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_DB_PLATFORM_ERROR() {
        try {
            return (int) MH_HS_DB_PLATFORM_ERROR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_DB_MODE_ERROR() {
        try {
            return (int) MH_HS_DB_MODE_ERROR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_BAD_ALIGN() {
        try {
            return (int) MH_HS_BAD_ALIGN.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_BAD_ALLOC() {
        try {
            return (int) MH_HS_BAD_ALLOC.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_SCRATCH_IN_USE() {
        try {
            return (int) MH_HS_SCRATCH_IN_USE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_ARCH_ERROR() {
        try {
            return (int) MH_HS_ARCH_ERROR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_INSUFFICIENT_SPACE() {
        try {
            return (int) MH_HS_INSUFFICIENT_SPACE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_UNKNOWN_ERROR() {
        try {
            return (int) MH_HS_UNKNOWN_ERROR.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_MIN_OFFSET() {
        try {
            return (long) MH_HS_EXT_FLAG_MIN_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_MAX_OFFSET() {
        try {
            return (long) MH_HS_EXT_FLAG_MAX_OFFSET.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_MIN_LENGTH() {
        try {
            return (long) MH_HS_EXT_FLAG_MIN_LENGTH.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_EDIT_DISTANCE() {
        try {
            return (long) MH_HS_EXT_FLAG_EDIT_DISTANCE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_HAMMING_DISTANCE() {
        try {
            return (long) MH_HS_EXT_FLAG_HAMMING_DISTANCE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_CPU_FEATURES_AVX2() {
        try {
            return (long) MH_HS_CPU_FEATURES_AVX2.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_CPU_FEATURES_AVX512() {
        try {
            return (long) MH_HS_CPU_FEATURES_AVX512.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_CPU_FEATURES_AVX512VBMI() {
        try {
            return (long) MH_HS_CPU_FEATURES_AVX512VBMI.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_SOM_HORIZON_LARGE() {
        try {
            return (int) MH_HS_MODE_SOM_HORIZON_LARGE.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_SOM_HORIZON_MEDIUM() {
        try {
            return (int) MH_HS_MODE_SOM_HORIZON_MEDIUM.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_SOM_HORIZON_SMALL() {
        try {
            return (int) MH_HS_MODE_SOM_HORIZON_SMALL.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_OFFSET_PAST_HORIZON() {
        try {
            return (long) MH_HS_OFFSET_PAST_HORIZON.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment HS_VERSION_STRING() {
        try {
            return (MemorySegment) MH_HS_VERSION_STRING.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_VERSION_32BIT() {
        try {
            return (int) MH_HS_VERSION_32BIT.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}