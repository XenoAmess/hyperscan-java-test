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
            MH_FEATURES_H = findOrNull("_FEATURES_H", int.class);
            MH_DEFAULT_SOURCE = findOrNull("_DEFAULT_SOURCE", int.class);
            MH_GLIBC_USE_ISOC2X = findOrNull("__GLIBC_USE_ISOC2X", int.class);
            MH_USE_ISOC11 = findOrNull("__USE_ISOC11", int.class);
            MH_USE_ISOC99 = findOrNull("__USE_ISOC99", int.class);
            MH_USE_ISOC95 = findOrNull("__USE_ISOC95", int.class);
            MH_USE_POSIX_IMPLICITLY = findOrNull("__USE_POSIX_IMPLICITLY", int.class);
            MH_POSIX_SOURCE = findOrNull("_POSIX_SOURCE", int.class);
            MH_USE_POSIX = findOrNull("__USE_POSIX", int.class);
            MH_USE_POSIX2 = findOrNull("__USE_POSIX2", int.class);
            MH_USE_POSIX199309 = findOrNull("__USE_POSIX199309", int.class);
            MH_USE_POSIX199506 = findOrNull("__USE_POSIX199506", int.class);
            MH_USE_XOPEN2K = findOrNull("__USE_XOPEN2K", int.class);
            MH_USE_XOPEN2K8 = findOrNull("__USE_XOPEN2K8", int.class);
            MH_ATFILE_SOURCE = findOrNull("_ATFILE_SOURCE", int.class);
            MH_WORDSIZE = findOrNull("__WORDSIZE", int.class);
            MH_WORDSIZE_TIME64_COMPAT32 = findOrNull("__WORDSIZE_TIME64_COMPAT32", int.class);
            MH_SYSCALL_WORDSIZE = findOrNull("__SYSCALL_WORDSIZE", int.class);
            MH_USE_MISC = findOrNull("__USE_MISC", int.class);
            MH_USE_ATFILE = findOrNull("__USE_ATFILE", int.class);
            MH_USE_FORTIFY_LEVEL = findOrNull("__USE_FORTIFY_LEVEL", int.class);
            MH_GLIBC_USE_DEPRECATED_GETS = findOrNull("__GLIBC_USE_DEPRECATED_GETS", int.class);
            MH_GLIBC_USE_DEPRECATED_SCANF = findOrNull("__GLIBC_USE_DEPRECATED_SCANF", int.class);
            MH_GLIBC_USE_C2X_STRTOL = findOrNull("__GLIBC_USE_C2X_STRTOL", int.class);
            MH_STDC_PREDEF_H = findOrNull("_STDC_PREDEF_H", int.class);
            MH_STDC_IEC_559 = findOrNull("__STDC_IEC_559__", int.class);
            MH_STDC_IEC_559_COMPLEX = findOrNull("__STDC_IEC_559_COMPLEX__", int.class);
            MH_GNU_LIBRARY = findOrNull("__GNU_LIBRARY__", int.class);
            MH_GLIBC = findOrNull("__GLIBC__", int.class);
            MH_GLIBC_MINOR = findOrNull("__GLIBC_MINOR__", int.class);
            MH_SYS_CDEFS_H = findOrNull("_SYS_CDEFS_H", int.class);
            MH_GLIBC_C99_FLEXARR_AVAILABLE = findOrNull("__glibc_c99_flexarr_available", int.class);
            MH_LDOUBLE_REDIRECTS_TO_FLOAT128_ABI = findOrNull("__LDOUBLE_REDIRECTS_TO_FLOAT128_ABI", int.class);
            MH_HAVE_GENERIC_SELECTION = findOrNull("__HAVE_GENERIC_SELECTION", int.class);
            MH_GLIBC_USE_LIB_EXT2 = findOrNull("__GLIBC_USE_LIB_EXT2", int.class);
            MH_GLIBC_USE_IEC_60559_BFP_EXT = findOrNull("__GLIBC_USE_IEC_60559_BFP_EXT", int.class);
            MH_GLIBC_USE_IEC_60559_BFP_EXT_C2X = findOrNull("__GLIBC_USE_IEC_60559_BFP_EXT_C2X", int.class);
            MH_GLIBC_USE_IEC_60559_EXT = findOrNull("__GLIBC_USE_IEC_60559_EXT", int.class);
            MH_GLIBC_USE_IEC_60559_FUNCS_EXT = findOrNull("__GLIBC_USE_IEC_60559_FUNCS_EXT", int.class);
            MH_GLIBC_USE_IEC_60559_FUNCS_EXT_C2X = findOrNull("__GLIBC_USE_IEC_60559_FUNCS_EXT_C2X", int.class);
            MH_GLIBC_USE_IEC_60559_TYPES_EXT = findOrNull("__GLIBC_USE_IEC_60559_TYPES_EXT", int.class);
            MH_STDLIB_H = findOrNull("_STDLIB_H", int.class);
            MH_WNOHANG = findOrNull("WNOHANG", int.class);
            MH_WUNTRACED = findOrNull("WUNTRACED", int.class);
            MH_WSTOPPED = findOrNull("WSTOPPED", int.class);
            MH_WEXITED = findOrNull("WEXITED", int.class);
            MH_WCONTINUED = findOrNull("WCONTINUED", int.class);
            MH_WNOWAIT = findOrNull("WNOWAIT", int.class);
            MH_WNOTHREAD = findOrNull("__WNOTHREAD", int.class);
            MH_WALL = findOrNull("__WALL", int.class);
            MH_W_CONTINUED = findOrNull("__W_CONTINUED", int.class);
            MH_WCOREFLAG = findOrNull("__WCOREFLAG", int.class);
            MH_HAVE_FLOAT128 = findOrNull("__HAVE_FLOAT128", int.class);
            MH_HAVE_DISTINCT_FLOAT128 = findOrNull("__HAVE_DISTINCT_FLOAT128", int.class);
            MH_HAVE_FLOAT64X = findOrNull("__HAVE_FLOAT64X", int.class);
            MH_HAVE_FLOAT64X_LONG_DOUBLE = findOrNull("__HAVE_FLOAT64X_LONG_DOUBLE", int.class);
            MH_HAVE_FLOAT16 = findOrNull("__HAVE_FLOAT16", int.class);
            MH_HAVE_FLOAT32 = findOrNull("__HAVE_FLOAT32", int.class);
            MH_HAVE_FLOAT64 = findOrNull("__HAVE_FLOAT64", int.class);
            MH_HAVE_FLOAT32X = findOrNull("__HAVE_FLOAT32X", int.class);
            MH_HAVE_FLOAT128X = findOrNull("__HAVE_FLOAT128X", int.class);
            MH_HAVE_DISTINCT_FLOAT32 = findOrNull("__HAVE_DISTINCT_FLOAT32", int.class);
            MH_HAVE_DISTINCT_FLOAT64 = findOrNull("__HAVE_DISTINCT_FLOAT64", int.class);
            MH_HAVE_DISTINCT_FLOAT32X = findOrNull("__HAVE_DISTINCT_FLOAT32X", int.class);
            MH_HAVE_DISTINCT_FLOAT64X = findOrNull("__HAVE_DISTINCT_FLOAT64X", int.class);
            MH_HAVE_FLOATN_NOT_TYPEDEF = findOrNull("__HAVE_FLOATN_NOT_TYPEDEF", int.class);
            MH_LDIV_T_DEFINED = findOrNull("__ldiv_t_defined", int.class);
            MH_LLDIV_T_DEFINED = findOrNull("__lldiv_t_defined", int.class);
            MH_RAND_MAX = findOrNull("RAND_MAX", int.class);
            MH_EXIT_FAILURE = findOrNull("EXIT_FAILURE", int.class);
            MH_EXIT_SUCCESS = findOrNull("EXIT_SUCCESS", int.class);
            MH_SYS_TYPES_H = findOrNull("_SYS_TYPES_H", int.class);
            MH_BITS_TYPES_H = findOrNull("_BITS_TYPES_H", int.class);
            MH_BITS_TYPESIZES_H = findOrNull("_BITS_TYPESIZES_H", int.class);
            MH_OFF_T_MATCHES_OFF64_T = findOrNull("__OFF_T_MATCHES_OFF64_T", int.class);
            MH_INO_T_MATCHES_INO64_T = findOrNull("__INO_T_MATCHES_INO64_T", int.class);
            MH_RLIM_T_MATCHES_RLIM64_T = findOrNull("__RLIM_T_MATCHES_RLIM64_T", int.class);
            MH_STATFS_MATCHES_STATFS64 = findOrNull("__STATFS_MATCHES_STATFS64", int.class);
            MH_KERNEL_OLD_TIMEVAL_MATCHES_TIMEVAL64 = findOrNull("__KERNEL_OLD_TIMEVAL_MATCHES_TIMEVAL64", int.class);
            MH_FD_SETSIZE = findOrNull("__FD_SETSIZE", int.class);
            MH_BITS_TIME64_H = findOrNull("_BITS_TIME64_H", int.class);
            MH_CLOCK_T_DEFINED = findOrNull("__clock_t_defined", int.class);
            MH_CLOCKID_T_DEFINED = findOrNull("__clockid_t_defined", int.class);
            MH_TIME_T_DEFINED = findOrNull("__time_t_defined", int.class);
            MH_TIMER_T_DEFINED = findOrNull("__timer_t_defined", int.class);
            MH_BITS_STDINT_INTN_H = findOrNull("_BITS_STDINT_INTN_H", int.class);
            MH_BIT_TYPES_DEFINED = findOrNull("__BIT_TYPES_DEFINED__", int.class);
            MH_ENDIAN_H = findOrNull("_ENDIAN_H", int.class);
            MH_BITS_ENDIAN_H = findOrNull("_BITS_ENDIAN_H", int.class);
            MH_LITTLE_ENDIAN = findOrNull("__LITTLE_ENDIAN", int.class);
            MH_BIG_ENDIAN = findOrNull("__BIG_ENDIAN", int.class);
            MH_PDP_ENDIAN = findOrNull("__PDP_ENDIAN", int.class);
            MH_BITS_ENDIANNESS_H = findOrNull("_BITS_ENDIANNESS_H", int.class);
            MH_BITS_BYTESWAP_H = findOrNull("_BITS_BYTESWAP_H", int.class);
            MH_BITS_UINTN_IDENTITY_H = findOrNull("_BITS_UINTN_IDENTITY_H", int.class);
            MH_SYS_SELECT_H = findOrNull("_SYS_SELECT_H", int.class);
            MH_SIGSET_T_DEFINED = findOrNull("__sigset_t_defined", int.class);
            MH_TIMEVAL_DEFINED = findOrNull("__timeval_defined", int.class);
            MH_STRUCT_TIMESPEC = findOrNull("_STRUCT_TIMESPEC", int.class);
            MH_BITS_PTHREADTYPES_COMMON_H = findOrNull("_BITS_PTHREADTYPES_COMMON_H", int.class);
            MH_THREAD_SHARED_TYPES_H = findOrNull("_THREAD_SHARED_TYPES_H", int.class);
            MH_BITS_PTHREADTYPES_ARCH_H = findOrNull("_BITS_PTHREADTYPES_ARCH_H", int.class);
            MH_SIZEOF_PTHREAD_MUTEX_T = findOrNull("__SIZEOF_PTHREAD_MUTEX_T", int.class);
            MH_SIZEOF_PTHREAD_ATTR_T = findOrNull("__SIZEOF_PTHREAD_ATTR_T", int.class);
            MH_SIZEOF_PTHREAD_RWLOCK_T = findOrNull("__SIZEOF_PTHREAD_RWLOCK_T", int.class);
            MH_SIZEOF_PTHREAD_BARRIER_T = findOrNull("__SIZEOF_PTHREAD_BARRIER_T", int.class);
            MH_SIZEOF_PTHREAD_MUTEXATTR_T = findOrNull("__SIZEOF_PTHREAD_MUTEXATTR_T", int.class);
            MH_SIZEOF_PTHREAD_COND_T = findOrNull("__SIZEOF_PTHREAD_COND_T", int.class);
            MH_SIZEOF_PTHREAD_CONDATTR_T = findOrNull("__SIZEOF_PTHREAD_CONDATTR_T", int.class);
            MH_SIZEOF_PTHREAD_RWLOCKATTR_T = findOrNull("__SIZEOF_PTHREAD_RWLOCKATTR_T", int.class);
            MH_SIZEOF_PTHREAD_BARRIERATTR_T = findOrNull("__SIZEOF_PTHREAD_BARRIERATTR_T", int.class);
            MH_THREAD_MUTEX_INTERNAL_H = findOrNull("_THREAD_MUTEX_INTERNAL_H", int.class);
            MH_PTHREAD_MUTEX_HAVE_PREV = findOrNull("__PTHREAD_MUTEX_HAVE_PREV", int.class);
            MH_HAVE_PTHREAD_ATTR_T = findOrNull("__have_pthread_attr_t", int.class);
            MH_ALLOCA_H = findOrNull("_ALLOCA_H", int.class);
            MH_HS_SUCCESS = findOrNull("HS_SUCCESS", int.class);
            MH_HS_FLAG_CASELESS = findOrNull("HS_FLAG_CASELESS", int.class);
            MH_HS_FLAG_DOTALL = findOrNull("HS_FLAG_DOTALL", int.class);
            MH_HS_FLAG_MULTILINE = findOrNull("HS_FLAG_MULTILINE", int.class);
            MH_HS_FLAG_SINGLEMATCH = findOrNull("HS_FLAG_SINGLEMATCH", int.class);
            MH_HS_FLAG_ALLOWEMPTY = findOrNull("HS_FLAG_ALLOWEMPTY", int.class);
            MH_HS_FLAG_UTF8 = findOrNull("HS_FLAG_UTF8", int.class);
            MH_HS_FLAG_UCP = findOrNull("HS_FLAG_UCP", int.class);
            MH_HS_FLAG_PREFILTER = findOrNull("HS_FLAG_PREFILTER", int.class);
            MH_HS_FLAG_SOM_LEFTMOST = findOrNull("HS_FLAG_SOM_LEFTMOST", int.class);
            MH_HS_FLAG_COMBINATION = findOrNull("HS_FLAG_COMBINATION", int.class);
            MH_HS_FLAG_QUIET = findOrNull("HS_FLAG_QUIET", int.class);
            MH_HS_TUNE_FAMILY_GENERIC = findOrNull("HS_TUNE_FAMILY_GENERIC", int.class);
            MH_HS_TUNE_FAMILY_SNB = findOrNull("HS_TUNE_FAMILY_SNB", int.class);
            MH_HS_TUNE_FAMILY_IVB = findOrNull("HS_TUNE_FAMILY_IVB", int.class);
            MH_HS_TUNE_FAMILY_HSW = findOrNull("HS_TUNE_FAMILY_HSW", int.class);
            MH_HS_TUNE_FAMILY_SLM = findOrNull("HS_TUNE_FAMILY_SLM", int.class);
            MH_HS_TUNE_FAMILY_BDW = findOrNull("HS_TUNE_FAMILY_BDW", int.class);
            MH_HS_TUNE_FAMILY_SKL = findOrNull("HS_TUNE_FAMILY_SKL", int.class);
            MH_HS_TUNE_FAMILY_SKX = findOrNull("HS_TUNE_FAMILY_SKX", int.class);
            MH_HS_TUNE_FAMILY_GLM = findOrNull("HS_TUNE_FAMILY_GLM", int.class);
            MH_HS_TUNE_FAMILY_ICL = findOrNull("HS_TUNE_FAMILY_ICL", int.class);
            MH_HS_TUNE_FAMILY_ICX = findOrNull("HS_TUNE_FAMILY_ICX", int.class);
            MH_HS_MODE_BLOCK = findOrNull("HS_MODE_BLOCK", int.class);
            MH_HS_MODE_NOSTREAM = findOrNull("HS_MODE_NOSTREAM", int.class);
            MH_HS_MODE_STREAM = findOrNull("HS_MODE_STREAM", int.class);
            MH_HS_MODE_VECTORED = findOrNull("HS_MODE_VECTORED", int.class);
            MH_HS_MAJOR = findOrNull("HS_MAJOR", int.class);
            MH_HS_MINOR = findOrNull("HS_MINOR", int.class);
            MH_HS_PATCH = findOrNull("HS_PATCH", int.class);
            MH_CTYPE_GET_MB_CUR_MAX_DESCRIPTOR = findOrNull("__ctype_get_mb_cur_max$descriptor", FunctionDescriptor.class);
            MH_CTYPE_GET_MB_CUR_MAX_HANDLE = findOrNull("__ctype_get_mb_cur_max$handle", MethodHandle.class);
            MH_CTYPE_GET_MB_CUR_MAX_ADDRESS = findOrNull("__ctype_get_mb_cur_max$address", MemorySegment.class);
            MH_CTYPE_GET_MB_CUR_MAX = findOrNull("__ctype_get_mb_cur_max", long.class);
            MH_ATOF_DESCRIPTOR = findOrNull("atof$descriptor", FunctionDescriptor.class);
            MH_ATOF_HANDLE = findOrNull("atof$handle", MethodHandle.class);
            MH_ATOF_ADDRESS = findOrNull("atof$address", MemorySegment.class);
            MH_ATOF = findOrNull("atof", double.class, MemorySegment.class);
            MH_ATOI_DESCRIPTOR = findOrNull("atoi$descriptor", FunctionDescriptor.class);
            MH_ATOI_HANDLE = findOrNull("atoi$handle", MethodHandle.class);
            MH_ATOI_ADDRESS = findOrNull("atoi$address", MemorySegment.class);
            MH_ATOI = findOrNull("atoi", int.class, MemorySegment.class);
            MH_ATOL_DESCRIPTOR = findOrNull("atol$descriptor", FunctionDescriptor.class);
            MH_ATOL_HANDLE = findOrNull("atol$handle", MethodHandle.class);
            MH_ATOL_ADDRESS = findOrNull("atol$address", MemorySegment.class);
            MH_ATOL = findOrNull("atol", long.class, MemorySegment.class);
            MH_ATOLL_DESCRIPTOR = findOrNull("atoll$descriptor", FunctionDescriptor.class);
            MH_ATOLL_HANDLE = findOrNull("atoll$handle", MethodHandle.class);
            MH_ATOLL_ADDRESS = findOrNull("atoll$address", MemorySegment.class);
            MH_ATOLL = findOrNull("atoll", long.class, MemorySegment.class);
            MH_STRTOD_DESCRIPTOR = findOrNull("strtod$descriptor", FunctionDescriptor.class);
            MH_STRTOD_HANDLE = findOrNull("strtod$handle", MethodHandle.class);
            MH_STRTOD_ADDRESS = findOrNull("strtod$address", MemorySegment.class);
            MH_STRTOD = findOrNull("strtod", double.class, MemorySegment.class, MemorySegment.class);
            MH_STRTOF_DESCRIPTOR = findOrNull("strtof$descriptor", FunctionDescriptor.class);
            MH_STRTOF_HANDLE = findOrNull("strtof$handle", MethodHandle.class);
            MH_STRTOF_ADDRESS = findOrNull("strtof$address", MemorySegment.class);
            MH_STRTOF = findOrNull("strtof", float.class, MemorySegment.class, MemorySegment.class);
            MH_STRTOL_DESCRIPTOR = findOrNull("strtol$descriptor", FunctionDescriptor.class);
            MH_STRTOL_HANDLE = findOrNull("strtol$handle", MethodHandle.class);
            MH_STRTOL_ADDRESS = findOrNull("strtol$address", MemorySegment.class);
            MH_STRTOL = findOrNull("strtol", long.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_STRTOUL_DESCRIPTOR = findOrNull("strtoul$descriptor", FunctionDescriptor.class);
            MH_STRTOUL_HANDLE = findOrNull("strtoul$handle", MethodHandle.class);
            MH_STRTOUL_ADDRESS = findOrNull("strtoul$address", MemorySegment.class);
            MH_STRTOUL = findOrNull("strtoul", long.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_STRTOQ_DESCRIPTOR = findOrNull("strtoq$descriptor", FunctionDescriptor.class);
            MH_STRTOQ_HANDLE = findOrNull("strtoq$handle", MethodHandle.class);
            MH_STRTOQ_ADDRESS = findOrNull("strtoq$address", MemorySegment.class);
            MH_STRTOQ = findOrNull("strtoq", long.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_STRTOUQ_DESCRIPTOR = findOrNull("strtouq$descriptor", FunctionDescriptor.class);
            MH_STRTOUQ_HANDLE = findOrNull("strtouq$handle", MethodHandle.class);
            MH_STRTOUQ_ADDRESS = findOrNull("strtouq$address", MemorySegment.class);
            MH_STRTOUQ = findOrNull("strtouq", long.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_STRTOLL_DESCRIPTOR = findOrNull("strtoll$descriptor", FunctionDescriptor.class);
            MH_STRTOLL_HANDLE = findOrNull("strtoll$handle", MethodHandle.class);
            MH_STRTOLL_ADDRESS = findOrNull("strtoll$address", MemorySegment.class);
            MH_STRTOLL = findOrNull("strtoll", long.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_STRTOULL_DESCRIPTOR = findOrNull("strtoull$descriptor", FunctionDescriptor.class);
            MH_STRTOULL_HANDLE = findOrNull("strtoull$handle", MethodHandle.class);
            MH_STRTOULL_ADDRESS = findOrNull("strtoull$address", MemorySegment.class);
            MH_STRTOULL = findOrNull("strtoull", long.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_L64A_DESCRIPTOR = findOrNull("l64a$descriptor", FunctionDescriptor.class);
            MH_L64A_HANDLE = findOrNull("l64a$handle", MethodHandle.class);
            MH_L64A_ADDRESS = findOrNull("l64a$address", MemorySegment.class);
            MH_L64A = findOrNull("l64a", MemorySegment.class, long.class);
            MH_A64L_DESCRIPTOR = findOrNull("a64l$descriptor", FunctionDescriptor.class);
            MH_A64L_HANDLE = findOrNull("a64l$handle", MethodHandle.class);
            MH_A64L_ADDRESS = findOrNull("a64l$address", MemorySegment.class);
            MH_A64L = findOrNull("a64l", long.class, MemorySegment.class);
            MH_SELECT_DESCRIPTOR = findOrNull("select$descriptor", FunctionDescriptor.class);
            MH_SELECT_HANDLE = findOrNull("select$handle", MethodHandle.class);
            MH_SELECT_ADDRESS = findOrNull("select$address", MemorySegment.class);
            MH_SELECT = findOrNull("select", int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_PSELECT_DESCRIPTOR = findOrNull("pselect$descriptor", FunctionDescriptor.class);
            MH_PSELECT_HANDLE = findOrNull("pselect$handle", MethodHandle.class);
            MH_PSELECT_ADDRESS = findOrNull("pselect$address", MemorySegment.class);
            MH_PSELECT = findOrNull("pselect", int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_RANDOM_DESCRIPTOR = findOrNull("random$descriptor", FunctionDescriptor.class);
            MH_RANDOM_HANDLE = findOrNull("random$handle", MethodHandle.class);
            MH_RANDOM_ADDRESS = findOrNull("random$address", MemorySegment.class);
            MH_RANDOM = findOrNull("random", long.class);
            MH_SRANDOM_DESCRIPTOR = findOrNull("srandom$descriptor", FunctionDescriptor.class);
            MH_SRANDOM_HANDLE = findOrNull("srandom$handle", MethodHandle.class);
            MH_SRANDOM_ADDRESS = findOrNull("srandom$address", MemorySegment.class);
            MH_SRANDOM = findOrNull("srandom", void.class, int.class);
            MH_INITSTATE_DESCRIPTOR = findOrNull("initstate$descriptor", FunctionDescriptor.class);
            MH_INITSTATE_HANDLE = findOrNull("initstate$handle", MethodHandle.class);
            MH_INITSTATE_ADDRESS = findOrNull("initstate$address", MemorySegment.class);
            MH_INITSTATE = findOrNull("initstate", MemorySegment.class, int.class, MemorySegment.class, long.class);
            MH_SETSTATE_DESCRIPTOR = findOrNull("setstate$descriptor", FunctionDescriptor.class);
            MH_SETSTATE_HANDLE = findOrNull("setstate$handle", MethodHandle.class);
            MH_SETSTATE_ADDRESS = findOrNull("setstate$address", MemorySegment.class);
            MH_SETSTATE = findOrNull("setstate", MemorySegment.class, MemorySegment.class);
            MH_RANDOM_R_DESCRIPTOR = findOrNull("random_r$descriptor", FunctionDescriptor.class);
            MH_RANDOM_R_HANDLE = findOrNull("random_r$handle", MethodHandle.class);
            MH_RANDOM_R_ADDRESS = findOrNull("random_r$address", MemorySegment.class);
            MH_RANDOM_R = findOrNull("random_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_SRANDOM_R_DESCRIPTOR = findOrNull("srandom_r$descriptor", FunctionDescriptor.class);
            MH_SRANDOM_R_HANDLE = findOrNull("srandom_r$handle", MethodHandle.class);
            MH_SRANDOM_R_ADDRESS = findOrNull("srandom_r$address", MemorySegment.class);
            MH_SRANDOM_R = findOrNull("srandom_r", int.class, int.class, MemorySegment.class);
            MH_INITSTATE_R_DESCRIPTOR = findOrNull("initstate_r$descriptor", FunctionDescriptor.class);
            MH_INITSTATE_R_HANDLE = findOrNull("initstate_r$handle", MethodHandle.class);
            MH_INITSTATE_R_ADDRESS = findOrNull("initstate_r$address", MemorySegment.class);
            MH_INITSTATE_R = findOrNull("initstate_r", int.class, int.class, MemorySegment.class, long.class, MemorySegment.class);
            MH_SETSTATE_R_DESCRIPTOR = findOrNull("setstate_r$descriptor", FunctionDescriptor.class);
            MH_SETSTATE_R_HANDLE = findOrNull("setstate_r$handle", MethodHandle.class);
            MH_SETSTATE_R_ADDRESS = findOrNull("setstate_r$address", MemorySegment.class);
            MH_SETSTATE_R = findOrNull("setstate_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_RAND_DESCRIPTOR = findOrNull("rand$descriptor", FunctionDescriptor.class);
            MH_RAND_HANDLE = findOrNull("rand$handle", MethodHandle.class);
            MH_RAND_ADDRESS = findOrNull("rand$address", MemorySegment.class);
            MH_RAND = findOrNull("rand", int.class);
            MH_SRAND_DESCRIPTOR = findOrNull("srand$descriptor", FunctionDescriptor.class);
            MH_SRAND_HANDLE = findOrNull("srand$handle", MethodHandle.class);
            MH_SRAND_ADDRESS = findOrNull("srand$address", MemorySegment.class);
            MH_SRAND = findOrNull("srand", void.class, int.class);
            MH_RAND_R_DESCRIPTOR = findOrNull("rand_r$descriptor", FunctionDescriptor.class);
            MH_RAND_R_HANDLE = findOrNull("rand_r$handle", MethodHandle.class);
            MH_RAND_R_ADDRESS = findOrNull("rand_r$address", MemorySegment.class);
            MH_RAND_R = findOrNull("rand_r", int.class, MemorySegment.class);
            MH_DRAND48_DESCRIPTOR = findOrNull("drand48$descriptor", FunctionDescriptor.class);
            MH_DRAND48_HANDLE = findOrNull("drand48$handle", MethodHandle.class);
            MH_DRAND48_ADDRESS = findOrNull("drand48$address", MemorySegment.class);
            MH_DRAND48 = findOrNull("drand48", double.class);
            MH_ERAND48_DESCRIPTOR = findOrNull("erand48$descriptor", FunctionDescriptor.class);
            MH_ERAND48_HANDLE = findOrNull("erand48$handle", MethodHandle.class);
            MH_ERAND48_ADDRESS = findOrNull("erand48$address", MemorySegment.class);
            MH_ERAND48 = findOrNull("erand48", double.class, MemorySegment.class);
            MH_LRAND48_DESCRIPTOR = findOrNull("lrand48$descriptor", FunctionDescriptor.class);
            MH_LRAND48_HANDLE = findOrNull("lrand48$handle", MethodHandle.class);
            MH_LRAND48_ADDRESS = findOrNull("lrand48$address", MemorySegment.class);
            MH_LRAND48 = findOrNull("lrand48", long.class);
            MH_NRAND48_DESCRIPTOR = findOrNull("nrand48$descriptor", FunctionDescriptor.class);
            MH_NRAND48_HANDLE = findOrNull("nrand48$handle", MethodHandle.class);
            MH_NRAND48_ADDRESS = findOrNull("nrand48$address", MemorySegment.class);
            MH_NRAND48 = findOrNull("nrand48", long.class, MemorySegment.class);
            MH_MRAND48_DESCRIPTOR = findOrNull("mrand48$descriptor", FunctionDescriptor.class);
            MH_MRAND48_HANDLE = findOrNull("mrand48$handle", MethodHandle.class);
            MH_MRAND48_ADDRESS = findOrNull("mrand48$address", MemorySegment.class);
            MH_MRAND48 = findOrNull("mrand48", long.class);
            MH_JRAND48_DESCRIPTOR = findOrNull("jrand48$descriptor", FunctionDescriptor.class);
            MH_JRAND48_HANDLE = findOrNull("jrand48$handle", MethodHandle.class);
            MH_JRAND48_ADDRESS = findOrNull("jrand48$address", MemorySegment.class);
            MH_JRAND48 = findOrNull("jrand48", long.class, MemorySegment.class);
            MH_SRAND48_DESCRIPTOR = findOrNull("srand48$descriptor", FunctionDescriptor.class);
            MH_SRAND48_HANDLE = findOrNull("srand48$handle", MethodHandle.class);
            MH_SRAND48_ADDRESS = findOrNull("srand48$address", MemorySegment.class);
            MH_SRAND48 = findOrNull("srand48", void.class, long.class);
            MH_SEED48_DESCRIPTOR = findOrNull("seed48$descriptor", FunctionDescriptor.class);
            MH_SEED48_HANDLE = findOrNull("seed48$handle", MethodHandle.class);
            MH_SEED48_ADDRESS = findOrNull("seed48$address", MemorySegment.class);
            MH_SEED48 = findOrNull("seed48", MemorySegment.class, MemorySegment.class);
            MH_LCONG48_DESCRIPTOR = findOrNull("lcong48$descriptor", FunctionDescriptor.class);
            MH_LCONG48_HANDLE = findOrNull("lcong48$handle", MethodHandle.class);
            MH_LCONG48_ADDRESS = findOrNull("lcong48$address", MemorySegment.class);
            MH_LCONG48 = findOrNull("lcong48", void.class, MemorySegment.class);
            MH_DRAND48_R_DESCRIPTOR = findOrNull("drand48_r$descriptor", FunctionDescriptor.class);
            MH_DRAND48_R_HANDLE = findOrNull("drand48_r$handle", MethodHandle.class);
            MH_DRAND48_R_ADDRESS = findOrNull("drand48_r$address", MemorySegment.class);
            MH_DRAND48_R = findOrNull("drand48_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_ERAND48_R_DESCRIPTOR = findOrNull("erand48_r$descriptor", FunctionDescriptor.class);
            MH_ERAND48_R_HANDLE = findOrNull("erand48_r$handle", MethodHandle.class);
            MH_ERAND48_R_ADDRESS = findOrNull("erand48_r$address", MemorySegment.class);
            MH_ERAND48_R = findOrNull("erand48_r", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_LRAND48_R_DESCRIPTOR = findOrNull("lrand48_r$descriptor", FunctionDescriptor.class);
            MH_LRAND48_R_HANDLE = findOrNull("lrand48_r$handle", MethodHandle.class);
            MH_LRAND48_R_ADDRESS = findOrNull("lrand48_r$address", MemorySegment.class);
            MH_LRAND48_R = findOrNull("lrand48_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_NRAND48_R_DESCRIPTOR = findOrNull("nrand48_r$descriptor", FunctionDescriptor.class);
            MH_NRAND48_R_HANDLE = findOrNull("nrand48_r$handle", MethodHandle.class);
            MH_NRAND48_R_ADDRESS = findOrNull("nrand48_r$address", MemorySegment.class);
            MH_NRAND48_R = findOrNull("nrand48_r", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_MRAND48_R_DESCRIPTOR = findOrNull("mrand48_r$descriptor", FunctionDescriptor.class);
            MH_MRAND48_R_HANDLE = findOrNull("mrand48_r$handle", MethodHandle.class);
            MH_MRAND48_R_ADDRESS = findOrNull("mrand48_r$address", MemorySegment.class);
            MH_MRAND48_R = findOrNull("mrand48_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_JRAND48_R_DESCRIPTOR = findOrNull("jrand48_r$descriptor", FunctionDescriptor.class);
            MH_JRAND48_R_HANDLE = findOrNull("jrand48_r$handle", MethodHandle.class);
            MH_JRAND48_R_ADDRESS = findOrNull("jrand48_r$address", MemorySegment.class);
            MH_JRAND48_R = findOrNull("jrand48_r", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_SRAND48_R_DESCRIPTOR = findOrNull("srand48_r$descriptor", FunctionDescriptor.class);
            MH_SRAND48_R_HANDLE = findOrNull("srand48_r$handle", MethodHandle.class);
            MH_SRAND48_R_ADDRESS = findOrNull("srand48_r$address", MemorySegment.class);
            MH_SRAND48_R = findOrNull("srand48_r", int.class, long.class, MemorySegment.class);
            MH_SEED48_R_DESCRIPTOR = findOrNull("seed48_r$descriptor", FunctionDescriptor.class);
            MH_SEED48_R_HANDLE = findOrNull("seed48_r$handle", MethodHandle.class);
            MH_SEED48_R_ADDRESS = findOrNull("seed48_r$address", MemorySegment.class);
            MH_SEED48_R = findOrNull("seed48_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_LCONG48_R_DESCRIPTOR = findOrNull("lcong48_r$descriptor", FunctionDescriptor.class);
            MH_LCONG48_R_HANDLE = findOrNull("lcong48_r$handle", MethodHandle.class);
            MH_LCONG48_R_ADDRESS = findOrNull("lcong48_r$address", MemorySegment.class);
            MH_LCONG48_R = findOrNull("lcong48_r", int.class, MemorySegment.class, MemorySegment.class);
            MH_ARC4RANDOM_DESCRIPTOR = findOrNull("arc4random$descriptor", FunctionDescriptor.class);
            MH_ARC4RANDOM_HANDLE = findOrNull("arc4random$handle", MethodHandle.class);
            MH_ARC4RANDOM_ADDRESS = findOrNull("arc4random$address", MemorySegment.class);
            MH_ARC4RANDOM = findOrNull("arc4random", int.class);
            MH_ARC4RANDOM_BUF_DESCRIPTOR = findOrNull("arc4random_buf$descriptor", FunctionDescriptor.class);
            MH_ARC4RANDOM_BUF_HANDLE = findOrNull("arc4random_buf$handle", MethodHandle.class);
            MH_ARC4RANDOM_BUF_ADDRESS = findOrNull("arc4random_buf$address", MemorySegment.class);
            MH_ARC4RANDOM_BUF = findOrNull("arc4random_buf", void.class, MemorySegment.class, long.class);
            MH_ARC4RANDOM_UNIFORM_DESCRIPTOR = findOrNull("arc4random_uniform$descriptor", FunctionDescriptor.class);
            MH_ARC4RANDOM_UNIFORM_HANDLE = findOrNull("arc4random_uniform$handle", MethodHandle.class);
            MH_ARC4RANDOM_UNIFORM_ADDRESS = findOrNull("arc4random_uniform$address", MemorySegment.class);
            MH_ARC4RANDOM_UNIFORM = findOrNull("arc4random_uniform", int.class, int.class);
            MH_MALLOC_DESCRIPTOR = findOrNull("malloc$descriptor", FunctionDescriptor.class);
            MH_MALLOC_HANDLE = findOrNull("malloc$handle", MethodHandle.class);
            MH_MALLOC_ADDRESS = findOrNull("malloc$address", MemorySegment.class);
            MH_MALLOC = findOrNull("malloc", MemorySegment.class, long.class);
            MH_CALLOC_DESCRIPTOR = findOrNull("calloc$descriptor", FunctionDescriptor.class);
            MH_CALLOC_HANDLE = findOrNull("calloc$handle", MethodHandle.class);
            MH_CALLOC_ADDRESS = findOrNull("calloc$address", MemorySegment.class);
            MH_CALLOC = findOrNull("calloc", MemorySegment.class, long.class, long.class);
            MH_REALLOC_DESCRIPTOR = findOrNull("realloc$descriptor", FunctionDescriptor.class);
            MH_REALLOC_HANDLE = findOrNull("realloc$handle", MethodHandle.class);
            MH_REALLOC_ADDRESS = findOrNull("realloc$address", MemorySegment.class);
            MH_REALLOC = findOrNull("realloc", MemorySegment.class, MemorySegment.class, long.class);
            MH_FREE_DESCRIPTOR = findOrNull("free$descriptor", FunctionDescriptor.class);
            MH_FREE_HANDLE = findOrNull("free$handle", MethodHandle.class);
            MH_FREE_ADDRESS = findOrNull("free$address", MemorySegment.class);
            MH_FREE = findOrNull("free", void.class, MemorySegment.class);
            MH_REALLOCARRAY_DESCRIPTOR = findOrNull("reallocarray$descriptor", FunctionDescriptor.class);
            MH_REALLOCARRAY_HANDLE = findOrNull("reallocarray$handle", MethodHandle.class);
            MH_REALLOCARRAY_ADDRESS = findOrNull("reallocarray$address", MemorySegment.class);
            MH_REALLOCARRAY = findOrNull("reallocarray", MemorySegment.class, MemorySegment.class, long.class, long.class);
            MH_ALLOCA_DESCRIPTOR = findOrNull("alloca$descriptor", FunctionDescriptor.class);
            MH_ALLOCA_HANDLE = findOrNull("alloca$handle", MethodHandle.class);
            MH_ALLOCA_ADDRESS = findOrNull("alloca$address", MemorySegment.class);
            MH_ALLOCA = findOrNull("alloca", MemorySegment.class, long.class);
            MH_VALLOC_DESCRIPTOR = findOrNull("valloc$descriptor", FunctionDescriptor.class);
            MH_VALLOC_HANDLE = findOrNull("valloc$handle", MethodHandle.class);
            MH_VALLOC_ADDRESS = findOrNull("valloc$address", MemorySegment.class);
            MH_VALLOC = findOrNull("valloc", MemorySegment.class, long.class);
            MH_POSIX_MEMALIGN_DESCRIPTOR = findOrNull("posix_memalign$descriptor", FunctionDescriptor.class);
            MH_POSIX_MEMALIGN_HANDLE = findOrNull("posix_memalign$handle", MethodHandle.class);
            MH_POSIX_MEMALIGN_ADDRESS = findOrNull("posix_memalign$address", MemorySegment.class);
            MH_POSIX_MEMALIGN = findOrNull("posix_memalign", int.class, MemorySegment.class, long.class, long.class);
            MH_ALIGNED_ALLOC_DESCRIPTOR = findOrNull("aligned_alloc$descriptor", FunctionDescriptor.class);
            MH_ALIGNED_ALLOC_HANDLE = findOrNull("aligned_alloc$handle", MethodHandle.class);
            MH_ALIGNED_ALLOC_ADDRESS = findOrNull("aligned_alloc$address", MemorySegment.class);
            MH_ALIGNED_ALLOC = findOrNull("aligned_alloc", MemorySegment.class, long.class, long.class);
            MH_ABORT_DESCRIPTOR = findOrNull("abort$descriptor", FunctionDescriptor.class);
            MH_ABORT_HANDLE = findOrNull("abort$handle", MethodHandle.class);
            MH_ABORT_ADDRESS = findOrNull("abort$address", MemorySegment.class);
            MH_ABORT = findOrNull("abort", void.class);
            MH_ATEXIT_DESCRIPTOR = findOrNull("atexit$descriptor", FunctionDescriptor.class);
            MH_ATEXIT_HANDLE = findOrNull("atexit$handle", MethodHandle.class);
            MH_ATEXIT_ADDRESS = findOrNull("atexit$address", MemorySegment.class);
            MH_ATEXIT = findOrNull("atexit", int.class, MemorySegment.class);
            MH_AT_QUICK_EXIT_DESCRIPTOR = findOrNull("at_quick_exit$descriptor", FunctionDescriptor.class);
            MH_AT_QUICK_EXIT_HANDLE = findOrNull("at_quick_exit$handle", MethodHandle.class);
            MH_AT_QUICK_EXIT_ADDRESS = findOrNull("at_quick_exit$address", MemorySegment.class);
            MH_AT_QUICK_EXIT = findOrNull("at_quick_exit", int.class, MemorySegment.class);
            MH_ON_EXIT_DESCRIPTOR = findOrNull("on_exit$descriptor", FunctionDescriptor.class);
            MH_ON_EXIT_HANDLE = findOrNull("on_exit$handle", MethodHandle.class);
            MH_ON_EXIT_ADDRESS = findOrNull("on_exit$address", MemorySegment.class);
            MH_ON_EXIT = findOrNull("on_exit", int.class, MemorySegment.class, MemorySegment.class);
            MH_EXIT_DESCRIPTOR = findOrNull("exit$descriptor", FunctionDescriptor.class);
            MH_EXIT_HANDLE = findOrNull("exit$handle", MethodHandle.class);
            MH_EXIT_ADDRESS = findOrNull("exit$address", MemorySegment.class);
            MH_EXIT = findOrNull("exit", void.class, int.class);
            MH_QUICK_EXIT_DESCRIPTOR = findOrNull("quick_exit$descriptor", FunctionDescriptor.class);
            MH_QUICK_EXIT_HANDLE = findOrNull("quick_exit$handle", MethodHandle.class);
            MH_QUICK_EXIT_ADDRESS = findOrNull("quick_exit$address", MemorySegment.class);
            MH_QUICK_EXIT = findOrNull("quick_exit", void.class, int.class);
            MH_EXIT_DESCRIPTOR_1 = findOrNull("_Exit$descriptor", FunctionDescriptor.class);
            MH_EXIT_HANDLE_1 = findOrNull("_Exit$handle", MethodHandle.class);
            MH_EXIT_ADDRESS_1 = findOrNull("_Exit$address", MemorySegment.class);
            MH_EXIT_1 = findOrNull("_Exit", void.class, int.class);
            MH_GETENV_DESCRIPTOR = findOrNull("getenv$descriptor", FunctionDescriptor.class);
            MH_GETENV_HANDLE = findOrNull("getenv$handle", MethodHandle.class);
            MH_GETENV_ADDRESS = findOrNull("getenv$address", MemorySegment.class);
            MH_GETENV = findOrNull("getenv", MemorySegment.class, MemorySegment.class);
            MH_PUTENV_DESCRIPTOR = findOrNull("putenv$descriptor", FunctionDescriptor.class);
            MH_PUTENV_HANDLE = findOrNull("putenv$handle", MethodHandle.class);
            MH_PUTENV_ADDRESS = findOrNull("putenv$address", MemorySegment.class);
            MH_PUTENV = findOrNull("putenv", int.class, MemorySegment.class);
            MH_SETENV_DESCRIPTOR = findOrNull("setenv$descriptor", FunctionDescriptor.class);
            MH_SETENV_HANDLE = findOrNull("setenv$handle", MethodHandle.class);
            MH_SETENV_ADDRESS = findOrNull("setenv$address", MemorySegment.class);
            MH_SETENV = findOrNull("setenv", int.class, MemorySegment.class, MemorySegment.class, int.class);
            MH_UNSETENV_DESCRIPTOR = findOrNull("unsetenv$descriptor", FunctionDescriptor.class);
            MH_UNSETENV_HANDLE = findOrNull("unsetenv$handle", MethodHandle.class);
            MH_UNSETENV_ADDRESS = findOrNull("unsetenv$address", MemorySegment.class);
            MH_UNSETENV = findOrNull("unsetenv", int.class, MemorySegment.class);
            MH_CLEARENV_DESCRIPTOR = findOrNull("clearenv$descriptor", FunctionDescriptor.class);
            MH_CLEARENV_HANDLE = findOrNull("clearenv$handle", MethodHandle.class);
            MH_CLEARENV_ADDRESS = findOrNull("clearenv$address", MemorySegment.class);
            MH_CLEARENV = findOrNull("clearenv", int.class);
            MH_MKTEMP_DESCRIPTOR = findOrNull("mktemp$descriptor", FunctionDescriptor.class);
            MH_MKTEMP_HANDLE = findOrNull("mktemp$handle", MethodHandle.class);
            MH_MKTEMP_ADDRESS = findOrNull("mktemp$address", MemorySegment.class);
            MH_MKTEMP = findOrNull("mktemp", MemorySegment.class, MemorySegment.class);
            MH_MKSTEMP_DESCRIPTOR = findOrNull("mkstemp$descriptor", FunctionDescriptor.class);
            MH_MKSTEMP_HANDLE = findOrNull("mkstemp$handle", MethodHandle.class);
            MH_MKSTEMP_ADDRESS = findOrNull("mkstemp$address", MemorySegment.class);
            MH_MKSTEMP = findOrNull("mkstemp", int.class, MemorySegment.class);
            MH_MKSTEMPS_DESCRIPTOR = findOrNull("mkstemps$descriptor", FunctionDescriptor.class);
            MH_MKSTEMPS_HANDLE = findOrNull("mkstemps$handle", MethodHandle.class);
            MH_MKSTEMPS_ADDRESS = findOrNull("mkstemps$address", MemorySegment.class);
            MH_MKSTEMPS = findOrNull("mkstemps", int.class, MemorySegment.class, int.class);
            MH_MKDTEMP_DESCRIPTOR = findOrNull("mkdtemp$descriptor", FunctionDescriptor.class);
            MH_MKDTEMP_HANDLE = findOrNull("mkdtemp$handle", MethodHandle.class);
            MH_MKDTEMP_ADDRESS = findOrNull("mkdtemp$address", MemorySegment.class);
            MH_MKDTEMP = findOrNull("mkdtemp", MemorySegment.class, MemorySegment.class);
            MH_SYSTEM_DESCRIPTOR = findOrNull("system$descriptor", FunctionDescriptor.class);
            MH_SYSTEM_HANDLE = findOrNull("system$handle", MethodHandle.class);
            MH_SYSTEM_ADDRESS = findOrNull("system$address", MemorySegment.class);
            MH_SYSTEM = findOrNull("system", int.class, MemorySegment.class);
            MH_REALPATH_DESCRIPTOR = findOrNull("realpath$descriptor", FunctionDescriptor.class);
            MH_REALPATH_HANDLE = findOrNull("realpath$handle", MethodHandle.class);
            MH_REALPATH_ADDRESS = findOrNull("realpath$address", MemorySegment.class);
            MH_REALPATH = findOrNull("realpath", MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_BSEARCH_DESCRIPTOR = findOrNull("bsearch$descriptor", FunctionDescriptor.class);
            MH_BSEARCH_HANDLE = findOrNull("bsearch$handle", MethodHandle.class);
            MH_BSEARCH_ADDRESS = findOrNull("bsearch$address", MemorySegment.class);
            MH_BSEARCH = findOrNull("bsearch", MemorySegment.class, MemorySegment.class, MemorySegment.class, long.class, long.class, MemorySegment.class);
            MH_QSORT_DESCRIPTOR = findOrNull("qsort$descriptor", FunctionDescriptor.class);
            MH_QSORT_HANDLE = findOrNull("qsort$handle", MethodHandle.class);
            MH_QSORT_ADDRESS = findOrNull("qsort$address", MemorySegment.class);
            MH_QSORT = findOrNull("qsort", void.class, MemorySegment.class, long.class, long.class, MemorySegment.class);
            MH_ABS_DESCRIPTOR = findOrNull("abs$descriptor", FunctionDescriptor.class);
            MH_ABS_HANDLE = findOrNull("abs$handle", MethodHandle.class);
            MH_ABS_ADDRESS = findOrNull("abs$address", MemorySegment.class);
            MH_ABS = findOrNull("abs", int.class, int.class);
            MH_LABS_DESCRIPTOR = findOrNull("labs$descriptor", FunctionDescriptor.class);
            MH_LABS_HANDLE = findOrNull("labs$handle", MethodHandle.class);
            MH_LABS_ADDRESS = findOrNull("labs$address", MemorySegment.class);
            MH_LABS = findOrNull("labs", long.class, long.class);
            MH_LLABS_DESCRIPTOR = findOrNull("llabs$descriptor", FunctionDescriptor.class);
            MH_LLABS_HANDLE = findOrNull("llabs$handle", MethodHandle.class);
            MH_LLABS_ADDRESS = findOrNull("llabs$address", MemorySegment.class);
            MH_LLABS = findOrNull("llabs", long.class, long.class);
            MH_DIV_DESCRIPTOR = findOrNull("div$descriptor", FunctionDescriptor.class);
            MH_DIV_HANDLE = findOrNull("div$handle", MethodHandle.class);
            MH_DIV_ADDRESS = findOrNull("div$address", MemorySegment.class);
            MH_DIV = findOrNull("div", MemorySegment.class, SegmentAllocator.class, int.class, int.class);
            MH_LDIV_DESCRIPTOR = findOrNull("ldiv$descriptor", FunctionDescriptor.class);
            MH_LDIV_HANDLE = findOrNull("ldiv$handle", MethodHandle.class);
            MH_LDIV_ADDRESS = findOrNull("ldiv$address", MemorySegment.class);
            MH_LDIV = findOrNull("ldiv", MemorySegment.class, SegmentAllocator.class, long.class, long.class);
            MH_LLDIV_DESCRIPTOR = findOrNull("lldiv$descriptor", FunctionDescriptor.class);
            MH_LLDIV_HANDLE = findOrNull("lldiv$handle", MethodHandle.class);
            MH_LLDIV_ADDRESS = findOrNull("lldiv$address", MemorySegment.class);
            MH_LLDIV = findOrNull("lldiv", MemorySegment.class, SegmentAllocator.class, long.class, long.class);
            MH_ECVT_DESCRIPTOR = findOrNull("ecvt$descriptor", FunctionDescriptor.class);
            MH_ECVT_HANDLE = findOrNull("ecvt$handle", MethodHandle.class);
            MH_ECVT_ADDRESS = findOrNull("ecvt$address", MemorySegment.class);
            MH_ECVT = findOrNull("ecvt", MemorySegment.class, double.class, int.class, MemorySegment.class, MemorySegment.class);
            MH_FCVT_DESCRIPTOR = findOrNull("fcvt$descriptor", FunctionDescriptor.class);
            MH_FCVT_HANDLE = findOrNull("fcvt$handle", MethodHandle.class);
            MH_FCVT_ADDRESS = findOrNull("fcvt$address", MemorySegment.class);
            MH_FCVT = findOrNull("fcvt", MemorySegment.class, double.class, int.class, MemorySegment.class, MemorySegment.class);
            MH_GCVT_DESCRIPTOR = findOrNull("gcvt$descriptor", FunctionDescriptor.class);
            MH_GCVT_HANDLE = findOrNull("gcvt$handle", MethodHandle.class);
            MH_GCVT_ADDRESS = findOrNull("gcvt$address", MemorySegment.class);
            MH_GCVT = findOrNull("gcvt", MemorySegment.class, double.class, int.class, MemorySegment.class);
            MH_ECVT_R_DESCRIPTOR = findOrNull("ecvt_r$descriptor", FunctionDescriptor.class);
            MH_ECVT_R_HANDLE = findOrNull("ecvt_r$handle", MethodHandle.class);
            MH_ECVT_R_ADDRESS = findOrNull("ecvt_r$address", MemorySegment.class);
            MH_ECVT_R = findOrNull("ecvt_r", int.class, double.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, long.class);
            MH_FCVT_R_DESCRIPTOR = findOrNull("fcvt_r$descriptor", FunctionDescriptor.class);
            MH_FCVT_R_HANDLE = findOrNull("fcvt_r$handle", MethodHandle.class);
            MH_FCVT_R_ADDRESS = findOrNull("fcvt_r$address", MemorySegment.class);
            MH_FCVT_R = findOrNull("fcvt_r", int.class, double.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, long.class);
            MH_MBLEN_DESCRIPTOR = findOrNull("mblen$descriptor", FunctionDescriptor.class);
            MH_MBLEN_HANDLE = findOrNull("mblen$handle", MethodHandle.class);
            MH_MBLEN_ADDRESS = findOrNull("mblen$address", MemorySegment.class);
            MH_MBLEN = findOrNull("mblen", int.class, MemorySegment.class, long.class);
            MH_MBTOWC_DESCRIPTOR = findOrNull("mbtowc$descriptor", FunctionDescriptor.class);
            MH_MBTOWC_HANDLE = findOrNull("mbtowc$handle", MethodHandle.class);
            MH_MBTOWC_ADDRESS = findOrNull("mbtowc$address", MemorySegment.class);
            MH_MBTOWC = findOrNull("mbtowc", int.class, MemorySegment.class, MemorySegment.class, long.class);
            MH_WCTOMB_DESCRIPTOR = findOrNull("wctomb$descriptor", FunctionDescriptor.class);
            MH_WCTOMB_HANDLE = findOrNull("wctomb$handle", MethodHandle.class);
            MH_WCTOMB_ADDRESS = findOrNull("wctomb$address", MemorySegment.class);
            MH_WCTOMB = findOrNull("wctomb", int.class, MemorySegment.class, int.class);
            MH_MBSTOWCS_DESCRIPTOR = findOrNull("mbstowcs$descriptor", FunctionDescriptor.class);
            MH_MBSTOWCS_HANDLE = findOrNull("mbstowcs$handle", MethodHandle.class);
            MH_MBSTOWCS_ADDRESS = findOrNull("mbstowcs$address", MemorySegment.class);
            MH_MBSTOWCS = findOrNull("mbstowcs", long.class, MemorySegment.class, MemorySegment.class, long.class);
            MH_WCSTOMBS_DESCRIPTOR = findOrNull("wcstombs$descriptor", FunctionDescriptor.class);
            MH_WCSTOMBS_HANDLE = findOrNull("wcstombs$handle", MethodHandle.class);
            MH_WCSTOMBS_ADDRESS = findOrNull("wcstombs$address", MemorySegment.class);
            MH_WCSTOMBS = findOrNull("wcstombs", long.class, MemorySegment.class, MemorySegment.class, long.class);
            MH_RPMATCH_DESCRIPTOR = findOrNull("rpmatch$descriptor", FunctionDescriptor.class);
            MH_RPMATCH_HANDLE = findOrNull("rpmatch$handle", MethodHandle.class);
            MH_RPMATCH_ADDRESS = findOrNull("rpmatch$address", MemorySegment.class);
            MH_RPMATCH = findOrNull("rpmatch", int.class, MemorySegment.class);
            MH_GETSUBOPT_DESCRIPTOR = findOrNull("getsubopt$descriptor", FunctionDescriptor.class);
            MH_GETSUBOPT_HANDLE = findOrNull("getsubopt$handle", MethodHandle.class);
            MH_GETSUBOPT_ADDRESS = findOrNull("getsubopt$address", MemorySegment.class);
            MH_GETSUBOPT = findOrNull("getsubopt", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_GETLOADAVG_DESCRIPTOR = findOrNull("getloadavg$descriptor", FunctionDescriptor.class);
            MH_GETLOADAVG_HANDLE = findOrNull("getloadavg$handle", MethodHandle.class);
            MH_GETLOADAVG_ADDRESS = findOrNull("getloadavg$address", MemorySegment.class);
            MH_GETLOADAVG = findOrNull("getloadavg", int.class, MemorySegment.class, int.class);
            MH_HS_FREE_DATABASE_DESCRIPTOR = findOrNull("hs_free_database$descriptor", FunctionDescriptor.class);
            MH_HS_FREE_DATABASE_HANDLE = findOrNull("hs_free_database$handle", MethodHandle.class);
            MH_HS_FREE_DATABASE_ADDRESS = findOrNull("hs_free_database$address", MemorySegment.class);
            MH_HS_FREE_DATABASE = findOrNull("hs_free_database", int.class, MemorySegment.class);
            MH_HS_SERIALIZE_DATABASE_DESCRIPTOR = findOrNull("hs_serialize_database$descriptor", FunctionDescriptor.class);
            MH_HS_SERIALIZE_DATABASE_HANDLE = findOrNull("hs_serialize_database$handle", MethodHandle.class);
            MH_HS_SERIALIZE_DATABASE_ADDRESS = findOrNull("hs_serialize_database$address", MemorySegment.class);
            MH_HS_SERIALIZE_DATABASE = findOrNull("hs_serialize_database", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_DESERIALIZE_DATABASE_DESCRIPTOR = findOrNull("hs_deserialize_database$descriptor", FunctionDescriptor.class);
            MH_HS_DESERIALIZE_DATABASE_HANDLE = findOrNull("hs_deserialize_database$handle", MethodHandle.class);
            MH_HS_DESERIALIZE_DATABASE_ADDRESS = findOrNull("hs_deserialize_database$address", MemorySegment.class);
            MH_HS_DESERIALIZE_DATABASE = findOrNull("hs_deserialize_database", int.class, MemorySegment.class, long.class, MemorySegment.class);
            MH_HS_DESERIALIZE_DATABASE_AT_DESCRIPTOR = findOrNull("hs_deserialize_database_at$descriptor", FunctionDescriptor.class);
            MH_HS_DESERIALIZE_DATABASE_AT_HANDLE = findOrNull("hs_deserialize_database_at$handle", MethodHandle.class);
            MH_HS_DESERIALIZE_DATABASE_AT_ADDRESS = findOrNull("hs_deserialize_database_at$address", MemorySegment.class);
            MH_HS_DESERIALIZE_DATABASE_AT = findOrNull("hs_deserialize_database_at", int.class, MemorySegment.class, long.class, MemorySegment.class);
            MH_HS_STREAM_SIZE_DESCRIPTOR = findOrNull("hs_stream_size$descriptor", FunctionDescriptor.class);
            MH_HS_STREAM_SIZE_HANDLE = findOrNull("hs_stream_size$handle", MethodHandle.class);
            MH_HS_STREAM_SIZE_ADDRESS = findOrNull("hs_stream_size$address", MemorySegment.class);
            MH_HS_STREAM_SIZE = findOrNull("hs_stream_size", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_DATABASE_SIZE_DESCRIPTOR = findOrNull("hs_database_size$descriptor", FunctionDescriptor.class);
            MH_HS_DATABASE_SIZE_HANDLE = findOrNull("hs_database_size$handle", MethodHandle.class);
            MH_HS_DATABASE_SIZE_ADDRESS = findOrNull("hs_database_size$address", MemorySegment.class);
            MH_HS_DATABASE_SIZE = findOrNull("hs_database_size", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SERIALIZED_DATABASE_SIZE_DESCRIPTOR = findOrNull("hs_serialized_database_size$descriptor", FunctionDescriptor.class);
            MH_HS_SERIALIZED_DATABASE_SIZE_HANDLE = findOrNull("hs_serialized_database_size$handle", MethodHandle.class);
            MH_HS_SERIALIZED_DATABASE_SIZE_ADDRESS = findOrNull("hs_serialized_database_size$address", MemorySegment.class);
            MH_HS_SERIALIZED_DATABASE_SIZE = findOrNull("hs_serialized_database_size", int.class, MemorySegment.class, long.class, MemorySegment.class);
            MH_HS_DATABASE_INFO_DESCRIPTOR = findOrNull("hs_database_info$descriptor", FunctionDescriptor.class);
            MH_HS_DATABASE_INFO_HANDLE = findOrNull("hs_database_info$handle", MethodHandle.class);
            MH_HS_DATABASE_INFO_ADDRESS = findOrNull("hs_database_info$address", MemorySegment.class);
            MH_HS_DATABASE_INFO = findOrNull("hs_database_info", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SERIALIZED_DATABASE_INFO_DESCRIPTOR = findOrNull("hs_serialized_database_info$descriptor", FunctionDescriptor.class);
            MH_HS_SERIALIZED_DATABASE_INFO_HANDLE = findOrNull("hs_serialized_database_info$handle", MethodHandle.class);
            MH_HS_SERIALIZED_DATABASE_INFO_ADDRESS = findOrNull("hs_serialized_database_info$address", MemorySegment.class);
            MH_HS_SERIALIZED_DATABASE_INFO = findOrNull("hs_serialized_database_info", int.class, MemorySegment.class, long.class, MemorySegment.class);
            MH_HS_SET_ALLOCATOR_DESCRIPTOR = findOrNull("hs_set_allocator$descriptor", FunctionDescriptor.class);
            MH_HS_SET_ALLOCATOR_HANDLE = findOrNull("hs_set_allocator$handle", MethodHandle.class);
            MH_HS_SET_ALLOCATOR_ADDRESS = findOrNull("hs_set_allocator$address", MemorySegment.class);
            MH_HS_SET_ALLOCATOR = findOrNull("hs_set_allocator", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SET_DATABASE_ALLOCATOR_DESCRIPTOR = findOrNull("hs_set_database_allocator$descriptor", FunctionDescriptor.class);
            MH_HS_SET_DATABASE_ALLOCATOR_HANDLE = findOrNull("hs_set_database_allocator$handle", MethodHandle.class);
            MH_HS_SET_DATABASE_ALLOCATOR_ADDRESS = findOrNull("hs_set_database_allocator$address", MemorySegment.class);
            MH_HS_SET_DATABASE_ALLOCATOR = findOrNull("hs_set_database_allocator", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SET_MISC_ALLOCATOR_DESCRIPTOR = findOrNull("hs_set_misc_allocator$descriptor", FunctionDescriptor.class);
            MH_HS_SET_MISC_ALLOCATOR_HANDLE = findOrNull("hs_set_misc_allocator$handle", MethodHandle.class);
            MH_HS_SET_MISC_ALLOCATOR_ADDRESS = findOrNull("hs_set_misc_allocator$address", MemorySegment.class);
            MH_HS_SET_MISC_ALLOCATOR = findOrNull("hs_set_misc_allocator", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SET_SCRATCH_ALLOCATOR_DESCRIPTOR = findOrNull("hs_set_scratch_allocator$descriptor", FunctionDescriptor.class);
            MH_HS_SET_SCRATCH_ALLOCATOR_HANDLE = findOrNull("hs_set_scratch_allocator$handle", MethodHandle.class);
            MH_HS_SET_SCRATCH_ALLOCATOR_ADDRESS = findOrNull("hs_set_scratch_allocator$address", MemorySegment.class);
            MH_HS_SET_SCRATCH_ALLOCATOR = findOrNull("hs_set_scratch_allocator", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SET_STREAM_ALLOCATOR_DESCRIPTOR = findOrNull("hs_set_stream_allocator$descriptor", FunctionDescriptor.class);
            MH_HS_SET_STREAM_ALLOCATOR_HANDLE = findOrNull("hs_set_stream_allocator$handle", MethodHandle.class);
            MH_HS_SET_STREAM_ALLOCATOR_ADDRESS = findOrNull("hs_set_stream_allocator$address", MemorySegment.class);
            MH_HS_SET_STREAM_ALLOCATOR = findOrNull("hs_set_stream_allocator", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_VERSION_DESCRIPTOR = findOrNull("hs_version$descriptor", FunctionDescriptor.class);
            MH_HS_VERSION_HANDLE = findOrNull("hs_version$handle", MethodHandle.class);
            MH_HS_VERSION_ADDRESS = findOrNull("hs_version$address", MemorySegment.class);
            MH_HS_VERSION = findOrNull("hs_version", MemorySegment.class);
            MH_HS_VALID_PLATFORM_DESCRIPTOR = findOrNull("hs_valid_platform$descriptor", FunctionDescriptor.class);
            MH_HS_VALID_PLATFORM_HANDLE = findOrNull("hs_valid_platform$handle", MethodHandle.class);
            MH_HS_VALID_PLATFORM_ADDRESS = findOrNull("hs_valid_platform$address", MemorySegment.class);
            MH_HS_VALID_PLATFORM = findOrNull("hs_valid_platform", int.class);
            MH_HS_COMPILE_DESCRIPTOR = findOrNull("hs_compile$descriptor", FunctionDescriptor.class);
            MH_HS_COMPILE_HANDLE = findOrNull("hs_compile$handle", MethodHandle.class);
            MH_HS_COMPILE_ADDRESS = findOrNull("hs_compile$address", MemorySegment.class);
            MH_HS_COMPILE = findOrNull("hs_compile", int.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_COMPILE_MULTI_DESCRIPTOR = findOrNull("hs_compile_multi$descriptor", FunctionDescriptor.class);
            MH_HS_COMPILE_MULTI_HANDLE = findOrNull("hs_compile_multi$handle", MethodHandle.class);
            MH_HS_COMPILE_MULTI_ADDRESS = findOrNull("hs_compile_multi$address", MemorySegment.class);
            MH_HS_COMPILE_MULTI = findOrNull("hs_compile_multi", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_COMPILE_EXT_MULTI_DESCRIPTOR = findOrNull("hs_compile_ext_multi$descriptor", FunctionDescriptor.class);
            MH_HS_COMPILE_EXT_MULTI_HANDLE = findOrNull("hs_compile_ext_multi$handle", MethodHandle.class);
            MH_HS_COMPILE_EXT_MULTI_ADDRESS = findOrNull("hs_compile_ext_multi$address", MemorySegment.class);
            MH_HS_COMPILE_EXT_MULTI = findOrNull("hs_compile_ext_multi", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_COMPILE_LIT_DESCRIPTOR = findOrNull("hs_compile_lit$descriptor", FunctionDescriptor.class);
            MH_HS_COMPILE_LIT_HANDLE = findOrNull("hs_compile_lit$handle", MethodHandle.class);
            MH_HS_COMPILE_LIT_ADDRESS = findOrNull("hs_compile_lit$address", MemorySegment.class);
            MH_HS_COMPILE_LIT = findOrNull("hs_compile_lit", int.class, MemorySegment.class, int.class, long.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_COMPILE_LIT_MULTI_DESCRIPTOR = findOrNull("hs_compile_lit_multi$descriptor", FunctionDescriptor.class);
            MH_HS_COMPILE_LIT_MULTI_HANDLE = findOrNull("hs_compile_lit_multi$handle", MethodHandle.class);
            MH_HS_COMPILE_LIT_MULTI_ADDRESS = findOrNull("hs_compile_lit_multi$address", MemorySegment.class);
            MH_HS_COMPILE_LIT_MULTI = findOrNull("hs_compile_lit_multi", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_FREE_COMPILE_ERROR_DESCRIPTOR = findOrNull("hs_free_compile_error$descriptor", FunctionDescriptor.class);
            MH_HS_FREE_COMPILE_ERROR_HANDLE = findOrNull("hs_free_compile_error$handle", MethodHandle.class);
            MH_HS_FREE_COMPILE_ERROR_ADDRESS = findOrNull("hs_free_compile_error$address", MemorySegment.class);
            MH_HS_FREE_COMPILE_ERROR = findOrNull("hs_free_compile_error", int.class, MemorySegment.class);
            MH_HS_EXPRESSION_INFO_DESCRIPTOR = findOrNull("hs_expression_info$descriptor", FunctionDescriptor.class);
            MH_HS_EXPRESSION_INFO_HANDLE = findOrNull("hs_expression_info$handle", MethodHandle.class);
            MH_HS_EXPRESSION_INFO_ADDRESS = findOrNull("hs_expression_info$address", MemorySegment.class);
            MH_HS_EXPRESSION_INFO = findOrNull("hs_expression_info", int.class, MemorySegment.class, int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_EXPRESSION_EXT_INFO_DESCRIPTOR = findOrNull("hs_expression_ext_info$descriptor", FunctionDescriptor.class);
            MH_HS_EXPRESSION_EXT_INFO_HANDLE = findOrNull("hs_expression_ext_info$handle", MethodHandle.class);
            MH_HS_EXPRESSION_EXT_INFO_ADDRESS = findOrNull("hs_expression_ext_info$address", MemorySegment.class);
            MH_HS_EXPRESSION_EXT_INFO = findOrNull("hs_expression_ext_info", int.class, MemorySegment.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_POPULATE_PLATFORM_DESCRIPTOR = findOrNull("hs_populate_platform$descriptor", FunctionDescriptor.class);
            MH_HS_POPULATE_PLATFORM_HANDLE = findOrNull("hs_populate_platform$handle", MethodHandle.class);
            MH_HS_POPULATE_PLATFORM_ADDRESS = findOrNull("hs_populate_platform$address", MemorySegment.class);
            MH_HS_POPULATE_PLATFORM = findOrNull("hs_populate_platform", int.class, MemorySegment.class);
            MH_HS_OPEN_STREAM_DESCRIPTOR = findOrNull("hs_open_stream$descriptor", FunctionDescriptor.class);
            MH_HS_OPEN_STREAM_HANDLE = findOrNull("hs_open_stream$handle", MethodHandle.class);
            MH_HS_OPEN_STREAM_ADDRESS = findOrNull("hs_open_stream$address", MemorySegment.class);
            MH_HS_OPEN_STREAM = findOrNull("hs_open_stream", int.class, MemorySegment.class, int.class, MemorySegment.class);
            MH_HS_SCAN_STREAM_DESCRIPTOR = findOrNull("hs_scan_stream$descriptor", FunctionDescriptor.class);
            MH_HS_SCAN_STREAM_HANDLE = findOrNull("hs_scan_stream$handle", MethodHandle.class);
            MH_HS_SCAN_STREAM_ADDRESS = findOrNull("hs_scan_stream$address", MemorySegment.class);
            MH_HS_SCAN_STREAM = findOrNull("hs_scan_stream", int.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_CLOSE_STREAM_DESCRIPTOR = findOrNull("hs_close_stream$descriptor", FunctionDescriptor.class);
            MH_HS_CLOSE_STREAM_HANDLE = findOrNull("hs_close_stream$handle", MethodHandle.class);
            MH_HS_CLOSE_STREAM_ADDRESS = findOrNull("hs_close_stream$address", MemorySegment.class);
            MH_HS_CLOSE_STREAM = findOrNull("hs_close_stream", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_RESET_STREAM_DESCRIPTOR = findOrNull("hs_reset_stream$descriptor", FunctionDescriptor.class);
            MH_HS_RESET_STREAM_HANDLE = findOrNull("hs_reset_stream$handle", MethodHandle.class);
            MH_HS_RESET_STREAM_ADDRESS = findOrNull("hs_reset_stream$address", MemorySegment.class);
            MH_HS_RESET_STREAM = findOrNull("hs_reset_stream", int.class, MemorySegment.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_COPY_STREAM_DESCRIPTOR = findOrNull("hs_copy_stream$descriptor", FunctionDescriptor.class);
            MH_HS_COPY_STREAM_HANDLE = findOrNull("hs_copy_stream$handle", MethodHandle.class);
            MH_HS_COPY_STREAM_ADDRESS = findOrNull("hs_copy_stream$address", MemorySegment.class);
            MH_HS_COPY_STREAM = findOrNull("hs_copy_stream", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_RESET_AND_COPY_STREAM_DESCRIPTOR = findOrNull("hs_reset_and_copy_stream$descriptor", FunctionDescriptor.class);
            MH_HS_RESET_AND_COPY_STREAM_HANDLE = findOrNull("hs_reset_and_copy_stream$handle", MethodHandle.class);
            MH_HS_RESET_AND_COPY_STREAM_ADDRESS = findOrNull("hs_reset_and_copy_stream$address", MemorySegment.class);
            MH_HS_RESET_AND_COPY_STREAM = findOrNull("hs_reset_and_copy_stream", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_COMPRESS_STREAM_DESCRIPTOR = findOrNull("hs_compress_stream$descriptor", FunctionDescriptor.class);
            MH_HS_COMPRESS_STREAM_HANDLE = findOrNull("hs_compress_stream$handle", MethodHandle.class);
            MH_HS_COMPRESS_STREAM_ADDRESS = findOrNull("hs_compress_stream$address", MemorySegment.class);
            MH_HS_COMPRESS_STREAM = findOrNull("hs_compress_stream", int.class, MemorySegment.class, MemorySegment.class, long.class, MemorySegment.class);
            MH_HS_EXPAND_STREAM_DESCRIPTOR = findOrNull("hs_expand_stream$descriptor", FunctionDescriptor.class);
            MH_HS_EXPAND_STREAM_HANDLE = findOrNull("hs_expand_stream$handle", MethodHandle.class);
            MH_HS_EXPAND_STREAM_ADDRESS = findOrNull("hs_expand_stream$address", MemorySegment.class);
            MH_HS_EXPAND_STREAM = findOrNull("hs_expand_stream", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, long.class);
            MH_HS_RESET_AND_EXPAND_STREAM_DESCRIPTOR = findOrNull("hs_reset_and_expand_stream$descriptor", FunctionDescriptor.class);
            MH_HS_RESET_AND_EXPAND_STREAM_HANDLE = findOrNull("hs_reset_and_expand_stream$handle", MethodHandle.class);
            MH_HS_RESET_AND_EXPAND_STREAM_ADDRESS = findOrNull("hs_reset_and_expand_stream$address", MemorySegment.class);
            MH_HS_RESET_AND_EXPAND_STREAM = findOrNull("hs_reset_and_expand_stream", int.class, MemorySegment.class, MemorySegment.class, long.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SCAN_DESCRIPTOR = findOrNull("hs_scan$descriptor", FunctionDescriptor.class);
            MH_HS_SCAN_HANDLE = findOrNull("hs_scan$handle", MethodHandle.class);
            MH_HS_SCAN_ADDRESS = findOrNull("hs_scan$address", MemorySegment.class);
            MH_HS_SCAN = findOrNull("hs_scan", int.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SCAN_VECTOR_DESCRIPTOR = findOrNull("hs_scan_vector$descriptor", FunctionDescriptor.class);
            MH_HS_SCAN_VECTOR_HANDLE = findOrNull("hs_scan_vector$handle", MethodHandle.class);
            MH_HS_SCAN_VECTOR_ADDRESS = findOrNull("hs_scan_vector$address", MemorySegment.class);
            MH_HS_SCAN_VECTOR = findOrNull("hs_scan_vector", int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class, int.class, int.class, MemorySegment.class, MemorySegment.class, MemorySegment.class);
            MH_HS_ALLOC_SCRATCH_DESCRIPTOR = findOrNull("hs_alloc_scratch$descriptor", FunctionDescriptor.class);
            MH_HS_ALLOC_SCRATCH_HANDLE = findOrNull("hs_alloc_scratch$handle", MethodHandle.class);
            MH_HS_ALLOC_SCRATCH_ADDRESS = findOrNull("hs_alloc_scratch$address", MemorySegment.class);
            MH_HS_ALLOC_SCRATCH = findOrNull("hs_alloc_scratch", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_CLONE_SCRATCH_DESCRIPTOR = findOrNull("hs_clone_scratch$descriptor", FunctionDescriptor.class);
            MH_HS_CLONE_SCRATCH_HANDLE = findOrNull("hs_clone_scratch$handle", MethodHandle.class);
            MH_HS_CLONE_SCRATCH_ADDRESS = findOrNull("hs_clone_scratch$address", MemorySegment.class);
            MH_HS_CLONE_SCRATCH = findOrNull("hs_clone_scratch", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_SCRATCH_SIZE_DESCRIPTOR = findOrNull("hs_scratch_size$descriptor", FunctionDescriptor.class);
            MH_HS_SCRATCH_SIZE_HANDLE = findOrNull("hs_scratch_size$handle", MethodHandle.class);
            MH_HS_SCRATCH_SIZE_ADDRESS = findOrNull("hs_scratch_size$address", MemorySegment.class);
            MH_HS_SCRATCH_SIZE = findOrNull("hs_scratch_size", int.class, MemorySegment.class, MemorySegment.class);
            MH_HS_FREE_SCRATCH_DESCRIPTOR = findOrNull("hs_free_scratch$descriptor", FunctionDescriptor.class);
            MH_HS_FREE_SCRATCH_HANDLE = findOrNull("hs_free_scratch$handle", MethodHandle.class);
            MH_HS_FREE_SCRATCH_ADDRESS = findOrNull("hs_free_scratch$address", MemorySegment.class);
            MH_HS_FREE_SCRATCH = findOrNull("hs_free_scratch", int.class, MemorySegment.class);
            MH_POSIX_C_SOURCE = findOrNull("_POSIX_C_SOURCE", long.class);
            MH_TIMESIZE = findOrNull("__TIMESIZE", int.class);
            MH_STDC_IEC_60559_BFP = findOrNull("__STDC_IEC_60559_BFP__", long.class);
            MH_STDC_IEC_60559_COMPLEX = findOrNull("__STDC_IEC_60559_COMPLEX__", long.class);
            MH_STDC_ISO_10646 = findOrNull("__STDC_ISO_10646__", long.class);
            MH_NULL = findOrNull("NULL", MemorySegment.class);
            MH_WCLONE = findOrNull("__WCLONE", int.class);
            MH_HAVE_DISTINCT_FLOAT16 = findOrNull("__HAVE_DISTINCT_FLOAT16", int.class);
            MH_HAVE_DISTINCT_FLOAT128X = findOrNull("__HAVE_DISTINCT_FLOAT128X", int.class);
            MH_HAVE_FLOAT128_UNLIKE_LDBL = findOrNull("__HAVE_FLOAT128_UNLIKE_LDBL", int.class);
            MH_BYTE_ORDER = findOrNull("__BYTE_ORDER", int.class);
            MH_FLOAT_WORD_ORDER = findOrNull("__FLOAT_WORD_ORDER", int.class);
            MH_LITTLE_ENDIAN_1 = findOrNull("LITTLE_ENDIAN", int.class);
            MH_BIG_ENDIAN_1 = findOrNull("BIG_ENDIAN", int.class);
            MH_PDP_ENDIAN_1 = findOrNull("PDP_ENDIAN", int.class);
            MH_BYTE_ORDER_1 = findOrNull("BYTE_ORDER", int.class);
            MH_SIGSET_NWORDS = findOrNull("_SIGSET_NWORDS", long.class);
            MH_NFDBITS = findOrNull("__NFDBITS", int.class);
            MH_FD_SETSIZE_1 = findOrNull("FD_SETSIZE", int.class);
            MH_NFDBITS_1 = findOrNull("NFDBITS", int.class);
            MH_PTHREAD_RWLOCK_ELISION_EXTRA = findOrNull("__PTHREAD_RWLOCK_ELISION_EXTRA", int.class);
            MH_HS_INVALID = findOrNull("HS_INVALID", int.class);
            MH_HS_NOMEM = findOrNull("HS_NOMEM", int.class);
            MH_HS_SCAN_TERMINATED = findOrNull("HS_SCAN_TERMINATED", int.class);
            MH_HS_COMPILER_ERROR = findOrNull("HS_COMPILER_ERROR", int.class);
            MH_HS_DB_VERSION_ERROR = findOrNull("HS_DB_VERSION_ERROR", int.class);
            MH_HS_DB_PLATFORM_ERROR = findOrNull("HS_DB_PLATFORM_ERROR", int.class);
            MH_HS_DB_MODE_ERROR = findOrNull("HS_DB_MODE_ERROR", int.class);
            MH_HS_BAD_ALIGN = findOrNull("HS_BAD_ALIGN", int.class);
            MH_HS_BAD_ALLOC = findOrNull("HS_BAD_ALLOC", int.class);
            MH_HS_SCRATCH_IN_USE = findOrNull("HS_SCRATCH_IN_USE", int.class);
            MH_HS_ARCH_ERROR = findOrNull("HS_ARCH_ERROR", int.class);
            MH_HS_INSUFFICIENT_SPACE = findOrNull("HS_INSUFFICIENT_SPACE", int.class);
            MH_HS_UNKNOWN_ERROR = findOrNull("HS_UNKNOWN_ERROR", int.class);
            MH_HS_EXT_FLAG_MIN_OFFSET = findOrNull("HS_EXT_FLAG_MIN_OFFSET", long.class);
            MH_HS_EXT_FLAG_MAX_OFFSET = findOrNull("HS_EXT_FLAG_MAX_OFFSET", long.class);
            MH_HS_EXT_FLAG_MIN_LENGTH = findOrNull("HS_EXT_FLAG_MIN_LENGTH", long.class);
            MH_HS_EXT_FLAG_EDIT_DISTANCE = findOrNull("HS_EXT_FLAG_EDIT_DISTANCE", long.class);
            MH_HS_EXT_FLAG_HAMMING_DISTANCE = findOrNull("HS_EXT_FLAG_HAMMING_DISTANCE", long.class);
            MH_HS_CPU_FEATURES_AVX2 = findOrNull("HS_CPU_FEATURES_AVX2", long.class);
            MH_HS_CPU_FEATURES_AVX512 = findOrNull("HS_CPU_FEATURES_AVX512", long.class);
            MH_HS_CPU_FEATURES_AVX512VBMI = findOrNull("HS_CPU_FEATURES_AVX512VBMI", long.class);
            MH_HS_MODE_SOM_HORIZON_LARGE = findOrNull("HS_MODE_SOM_HORIZON_LARGE", int.class);
            MH_HS_MODE_SOM_HORIZON_MEDIUM = findOrNull("HS_MODE_SOM_HORIZON_MEDIUM", int.class);
            MH_HS_MODE_SOM_HORIZON_SMALL = findOrNull("HS_MODE_SOM_HORIZON_SMALL", int.class);
            MH_HS_OFFSET_PAST_HORIZON = findOrNull("HS_OFFSET_PAST_HORIZON", long.class);
            MH_HS_VERSION_STRING = findOrNull("HS_VERSION_STRING", MemorySegment.class);
            MH_HS_VERSION_32BIT = findOrNull("HS_VERSION_32BIT", int.class);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load platform-specific hyperscan class: " + className, e);
        }
    }

    private hyperscan() {}

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

    public static int _FEATURES_H() {
        try {
            return (int) require("_FEATURES_H", MH_FEATURES_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _DEFAULT_SOURCE() {
        try {
            return (int) require("_DEFAULT_SOURCE", MH_DEFAULT_SOURCE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_ISOC2X() {
        try {
            return (int) require("__GLIBC_USE_ISOC2X", MH_GLIBC_USE_ISOC2X).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_ISOC11() {
        try {
            return (int) require("__USE_ISOC11", MH_USE_ISOC11).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_ISOC99() {
        try {
            return (int) require("__USE_ISOC99", MH_USE_ISOC99).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_ISOC95() {
        try {
            return (int) require("__USE_ISOC95", MH_USE_ISOC95).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX_IMPLICITLY() {
        try {
            return (int) require("__USE_POSIX_IMPLICITLY", MH_USE_POSIX_IMPLICITLY).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _POSIX_SOURCE() {
        try {
            return (int) require("_POSIX_SOURCE", MH_POSIX_SOURCE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX() {
        try {
            return (int) require("__USE_POSIX", MH_USE_POSIX).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX2() {
        try {
            return (int) require("__USE_POSIX2", MH_USE_POSIX2).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX199309() {
        try {
            return (int) require("__USE_POSIX199309", MH_USE_POSIX199309).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_POSIX199506() {
        try {
            return (int) require("__USE_POSIX199506", MH_USE_POSIX199506).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_XOPEN2K() {
        try {
            return (int) require("__USE_XOPEN2K", MH_USE_XOPEN2K).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_XOPEN2K8() {
        try {
            return (int) require("__USE_XOPEN2K8", MH_USE_XOPEN2K8).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _ATFILE_SOURCE() {
        try {
            return (int) require("_ATFILE_SOURCE", MH_ATFILE_SOURCE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WORDSIZE() {
        try {
            return (int) require("__WORDSIZE", MH_WORDSIZE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WORDSIZE_TIME64_COMPAT32() {
        try {
            return (int) require("__WORDSIZE_TIME64_COMPAT32", MH_WORDSIZE_TIME64_COMPAT32).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SYSCALL_WORDSIZE() {
        try {
            return (int) require("__SYSCALL_WORDSIZE", MH_SYSCALL_WORDSIZE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_MISC() {
        try {
            return (int) require("__USE_MISC", MH_USE_MISC).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_ATFILE() {
        try {
            return (int) require("__USE_ATFILE", MH_USE_ATFILE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __USE_FORTIFY_LEVEL() {
        try {
            return (int) require("__USE_FORTIFY_LEVEL", MH_USE_FORTIFY_LEVEL).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_DEPRECATED_GETS() {
        try {
            return (int) require("__GLIBC_USE_DEPRECATED_GETS", MH_GLIBC_USE_DEPRECATED_GETS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_DEPRECATED_SCANF() {
        try {
            return (int) require("__GLIBC_USE_DEPRECATED_SCANF", MH_GLIBC_USE_DEPRECATED_SCANF).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_C2X_STRTOL() {
        try {
            return (int) require("__GLIBC_USE_C2X_STRTOL", MH_GLIBC_USE_C2X_STRTOL).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _STDC_PREDEF_H() {
        try {
            return (int) require("_STDC_PREDEF_H", MH_STDC_PREDEF_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __STDC_IEC_559__() {
        try {
            return (int) require("__STDC_IEC_559__", MH_STDC_IEC_559).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __STDC_IEC_559_COMPLEX__() {
        try {
            return (int) require("__STDC_IEC_559_COMPLEX__", MH_STDC_IEC_559_COMPLEX).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GNU_LIBRARY__() {
        try {
            return (int) require("__GNU_LIBRARY__", MH_GNU_LIBRARY).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC__() {
        try {
            return (int) require("__GLIBC__", MH_GLIBC).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_MINOR__() {
        try {
            return (int) require("__GLIBC_MINOR__", MH_GLIBC_MINOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _SYS_CDEFS_H() {
        try {
            return (int) require("_SYS_CDEFS_H", MH_SYS_CDEFS_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __glibc_c99_flexarr_available() {
        try {
            return (int) require("__glibc_c99_flexarr_available", MH_GLIBC_C99_FLEXARR_AVAILABLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __LDOUBLE_REDIRECTS_TO_FLOAT128_ABI() {
        try {
            return (int) require("__LDOUBLE_REDIRECTS_TO_FLOAT128_ABI", MH_LDOUBLE_REDIRECTS_TO_FLOAT128_ABI).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_GENERIC_SELECTION() {
        try {
            return (int) require("__HAVE_GENERIC_SELECTION", MH_HAVE_GENERIC_SELECTION).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_LIB_EXT2() {
        try {
            return (int) require("__GLIBC_USE_LIB_EXT2", MH_GLIBC_USE_LIB_EXT2).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_BFP_EXT() {
        try {
            return (int) require("__GLIBC_USE_IEC_60559_BFP_EXT", MH_GLIBC_USE_IEC_60559_BFP_EXT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_BFP_EXT_C2X() {
        try {
            return (int) require("__GLIBC_USE_IEC_60559_BFP_EXT_C2X", MH_GLIBC_USE_IEC_60559_BFP_EXT_C2X).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_EXT() {
        try {
            return (int) require("__GLIBC_USE_IEC_60559_EXT", MH_GLIBC_USE_IEC_60559_EXT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_FUNCS_EXT() {
        try {
            return (int) require("__GLIBC_USE_IEC_60559_FUNCS_EXT", MH_GLIBC_USE_IEC_60559_FUNCS_EXT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_FUNCS_EXT_C2X() {
        try {
            return (int) require("__GLIBC_USE_IEC_60559_FUNCS_EXT_C2X", MH_GLIBC_USE_IEC_60559_FUNCS_EXT_C2X).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __GLIBC_USE_IEC_60559_TYPES_EXT() {
        try {
            return (int) require("__GLIBC_USE_IEC_60559_TYPES_EXT", MH_GLIBC_USE_IEC_60559_TYPES_EXT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _STDLIB_H() {
        try {
            return (int) require("_STDLIB_H", MH_STDLIB_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WNOHANG() {
        try {
            return (int) require("WNOHANG", MH_WNOHANG).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WUNTRACED() {
        try {
            return (int) require("WUNTRACED", MH_WUNTRACED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WSTOPPED() {
        try {
            return (int) require("WSTOPPED", MH_WSTOPPED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WEXITED() {
        try {
            return (int) require("WEXITED", MH_WEXITED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WCONTINUED() {
        try {
            return (int) require("WCONTINUED", MH_WCONTINUED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int WNOWAIT() {
        try {
            return (int) require("WNOWAIT", MH_WNOWAIT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WNOTHREAD() {
        try {
            return (int) require("__WNOTHREAD", MH_WNOTHREAD).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WALL() {
        try {
            return (int) require("__WALL", MH_WALL).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __W_CONTINUED() {
        try {
            return (int) require("__W_CONTINUED", MH_W_CONTINUED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WCOREFLAG() {
        try {
            return (int) require("__WCOREFLAG", MH_WCOREFLAG).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT128() {
        try {
            return (int) require("__HAVE_FLOAT128", MH_HAVE_FLOAT128).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT128() {
        try {
            return (int) require("__HAVE_DISTINCT_FLOAT128", MH_HAVE_DISTINCT_FLOAT128).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT64X() {
        try {
            return (int) require("__HAVE_FLOAT64X", MH_HAVE_FLOAT64X).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT64X_LONG_DOUBLE() {
        try {
            return (int) require("__HAVE_FLOAT64X_LONG_DOUBLE", MH_HAVE_FLOAT64X_LONG_DOUBLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT16() {
        try {
            return (int) require("__HAVE_FLOAT16", MH_HAVE_FLOAT16).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT32() {
        try {
            return (int) require("__HAVE_FLOAT32", MH_HAVE_FLOAT32).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT64() {
        try {
            return (int) require("__HAVE_FLOAT64", MH_HAVE_FLOAT64).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT32X() {
        try {
            return (int) require("__HAVE_FLOAT32X", MH_HAVE_FLOAT32X).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT128X() {
        try {
            return (int) require("__HAVE_FLOAT128X", MH_HAVE_FLOAT128X).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT32() {
        try {
            return (int) require("__HAVE_DISTINCT_FLOAT32", MH_HAVE_DISTINCT_FLOAT32).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT64() {
        try {
            return (int) require("__HAVE_DISTINCT_FLOAT64", MH_HAVE_DISTINCT_FLOAT64).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT32X() {
        try {
            return (int) require("__HAVE_DISTINCT_FLOAT32X", MH_HAVE_DISTINCT_FLOAT32X).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT64X() {
        try {
            return (int) require("__HAVE_DISTINCT_FLOAT64X", MH_HAVE_DISTINCT_FLOAT64X).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOATN_NOT_TYPEDEF() {
        try {
            return (int) require("__HAVE_FLOATN_NOT_TYPEDEF", MH_HAVE_FLOATN_NOT_TYPEDEF).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __ldiv_t_defined() {
        try {
            return (int) require("__ldiv_t_defined", MH_LDIV_T_DEFINED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __lldiv_t_defined() {
        try {
            return (int) require("__lldiv_t_defined", MH_LLDIV_T_DEFINED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int RAND_MAX() {
        try {
            return (int) require("RAND_MAX", MH_RAND_MAX).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int EXIT_FAILURE() {
        try {
            return (int) require("EXIT_FAILURE", MH_EXIT_FAILURE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int EXIT_SUCCESS() {
        try {
            return (int) require("EXIT_SUCCESS", MH_EXIT_SUCCESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _SYS_TYPES_H() {
        try {
            return (int) require("_SYS_TYPES_H", MH_SYS_TYPES_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_TYPES_H() {
        try {
            return (int) require("_BITS_TYPES_H", MH_BITS_TYPES_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_TYPESIZES_H() {
        try {
            return (int) require("_BITS_TYPESIZES_H", MH_BITS_TYPESIZES_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __OFF_T_MATCHES_OFF64_T() {
        try {
            return (int) require("__OFF_T_MATCHES_OFF64_T", MH_OFF_T_MATCHES_OFF64_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __INO_T_MATCHES_INO64_T() {
        try {
            return (int) require("__INO_T_MATCHES_INO64_T", MH_INO_T_MATCHES_INO64_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __RLIM_T_MATCHES_RLIM64_T() {
        try {
            return (int) require("__RLIM_T_MATCHES_RLIM64_T", MH_RLIM_T_MATCHES_RLIM64_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __STATFS_MATCHES_STATFS64() {
        try {
            return (int) require("__STATFS_MATCHES_STATFS64", MH_STATFS_MATCHES_STATFS64).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __KERNEL_OLD_TIMEVAL_MATCHES_TIMEVAL64() {
        try {
            return (int) require("__KERNEL_OLD_TIMEVAL_MATCHES_TIMEVAL64", MH_KERNEL_OLD_TIMEVAL_MATCHES_TIMEVAL64).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __FD_SETSIZE() {
        try {
            return (int) require("__FD_SETSIZE", MH_FD_SETSIZE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_TIME64_H() {
        try {
            return (int) require("_BITS_TIME64_H", MH_BITS_TIME64_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __clock_t_defined() {
        try {
            return (int) require("__clock_t_defined", MH_CLOCK_T_DEFINED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __clockid_t_defined() {
        try {
            return (int) require("__clockid_t_defined", MH_CLOCKID_T_DEFINED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __time_t_defined() {
        try {
            return (int) require("__time_t_defined", MH_TIME_T_DEFINED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __timer_t_defined() {
        try {
            return (int) require("__timer_t_defined", MH_TIMER_T_DEFINED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_STDINT_INTN_H() {
        try {
            return (int) require("_BITS_STDINT_INTN_H", MH_BITS_STDINT_INTN_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __BIT_TYPES_DEFINED__() {
        try {
            return (int) require("__BIT_TYPES_DEFINED__", MH_BIT_TYPES_DEFINED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _ENDIAN_H() {
        try {
            return (int) require("_ENDIAN_H", MH_ENDIAN_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_ENDIAN_H() {
        try {
            return (int) require("_BITS_ENDIAN_H", MH_BITS_ENDIAN_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __LITTLE_ENDIAN() {
        try {
            return (int) require("__LITTLE_ENDIAN", MH_LITTLE_ENDIAN).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __BIG_ENDIAN() {
        try {
            return (int) require("__BIG_ENDIAN", MH_BIG_ENDIAN).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __PDP_ENDIAN() {
        try {
            return (int) require("__PDP_ENDIAN", MH_PDP_ENDIAN).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_ENDIANNESS_H() {
        try {
            return (int) require("_BITS_ENDIANNESS_H", MH_BITS_ENDIANNESS_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_BYTESWAP_H() {
        try {
            return (int) require("_BITS_BYTESWAP_H", MH_BITS_BYTESWAP_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_UINTN_IDENTITY_H() {
        try {
            return (int) require("_BITS_UINTN_IDENTITY_H", MH_BITS_UINTN_IDENTITY_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _SYS_SELECT_H() {
        try {
            return (int) require("_SYS_SELECT_H", MH_SYS_SELECT_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __sigset_t_defined() {
        try {
            return (int) require("__sigset_t_defined", MH_SIGSET_T_DEFINED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __timeval_defined() {
        try {
            return (int) require("__timeval_defined", MH_TIMEVAL_DEFINED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _STRUCT_TIMESPEC() {
        try {
            return (int) require("_STRUCT_TIMESPEC", MH_STRUCT_TIMESPEC).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_PTHREADTYPES_COMMON_H() {
        try {
            return (int) require("_BITS_PTHREADTYPES_COMMON_H", MH_BITS_PTHREADTYPES_COMMON_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _THREAD_SHARED_TYPES_H() {
        try {
            return (int) require("_THREAD_SHARED_TYPES_H", MH_THREAD_SHARED_TYPES_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _BITS_PTHREADTYPES_ARCH_H() {
        try {
            return (int) require("_BITS_PTHREADTYPES_ARCH_H", MH_BITS_PTHREADTYPES_ARCH_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_MUTEX_T() {
        try {
            return (int) require("__SIZEOF_PTHREAD_MUTEX_T", MH_SIZEOF_PTHREAD_MUTEX_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_ATTR_T() {
        try {
            return (int) require("__SIZEOF_PTHREAD_ATTR_T", MH_SIZEOF_PTHREAD_ATTR_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_RWLOCK_T() {
        try {
            return (int) require("__SIZEOF_PTHREAD_RWLOCK_T", MH_SIZEOF_PTHREAD_RWLOCK_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_BARRIER_T() {
        try {
            return (int) require("__SIZEOF_PTHREAD_BARRIER_T", MH_SIZEOF_PTHREAD_BARRIER_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_MUTEXATTR_T() {
        try {
            return (int) require("__SIZEOF_PTHREAD_MUTEXATTR_T", MH_SIZEOF_PTHREAD_MUTEXATTR_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_COND_T() {
        try {
            return (int) require("__SIZEOF_PTHREAD_COND_T", MH_SIZEOF_PTHREAD_COND_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_CONDATTR_T() {
        try {
            return (int) require("__SIZEOF_PTHREAD_CONDATTR_T", MH_SIZEOF_PTHREAD_CONDATTR_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_RWLOCKATTR_T() {
        try {
            return (int) require("__SIZEOF_PTHREAD_RWLOCKATTR_T", MH_SIZEOF_PTHREAD_RWLOCKATTR_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __SIZEOF_PTHREAD_BARRIERATTR_T() {
        try {
            return (int) require("__SIZEOF_PTHREAD_BARRIERATTR_T", MH_SIZEOF_PTHREAD_BARRIERATTR_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _THREAD_MUTEX_INTERNAL_H() {
        try {
            return (int) require("_THREAD_MUTEX_INTERNAL_H", MH_THREAD_MUTEX_INTERNAL_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __PTHREAD_MUTEX_HAVE_PREV() {
        try {
            return (int) require("__PTHREAD_MUTEX_HAVE_PREV", MH_PTHREAD_MUTEX_HAVE_PREV).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __have_pthread_attr_t() {
        try {
            return (int) require("__have_pthread_attr_t", MH_HAVE_PTHREAD_ATTR_T).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int _ALLOCA_H() {
        try {
            return (int) require("_ALLOCA_H", MH_ALLOCA_H).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_SUCCESS() {
        try {
            return (int) require("HS_SUCCESS", MH_HS_SUCCESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_CASELESS() {
        try {
            return (int) require("HS_FLAG_CASELESS", MH_HS_FLAG_CASELESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_DOTALL() {
        try {
            return (int) require("HS_FLAG_DOTALL", MH_HS_FLAG_DOTALL).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_MULTILINE() {
        try {
            return (int) require("HS_FLAG_MULTILINE", MH_HS_FLAG_MULTILINE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_SINGLEMATCH() {
        try {
            return (int) require("HS_FLAG_SINGLEMATCH", MH_HS_FLAG_SINGLEMATCH).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_ALLOWEMPTY() {
        try {
            return (int) require("HS_FLAG_ALLOWEMPTY", MH_HS_FLAG_ALLOWEMPTY).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_UTF8() {
        try {
            return (int) require("HS_FLAG_UTF8", MH_HS_FLAG_UTF8).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_UCP() {
        try {
            return (int) require("HS_FLAG_UCP", MH_HS_FLAG_UCP).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_PREFILTER() {
        try {
            return (int) require("HS_FLAG_PREFILTER", MH_HS_FLAG_PREFILTER).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_SOM_LEFTMOST() {
        try {
            return (int) require("HS_FLAG_SOM_LEFTMOST", MH_HS_FLAG_SOM_LEFTMOST).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_COMBINATION() {
        try {
            return (int) require("HS_FLAG_COMBINATION", MH_HS_FLAG_COMBINATION).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_FLAG_QUIET() {
        try {
            return (int) require("HS_FLAG_QUIET", MH_HS_FLAG_QUIET).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_GENERIC() {
        try {
            return (int) require("HS_TUNE_FAMILY_GENERIC", MH_HS_TUNE_FAMILY_GENERIC).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_SNB() {
        try {
            return (int) require("HS_TUNE_FAMILY_SNB", MH_HS_TUNE_FAMILY_SNB).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_IVB() {
        try {
            return (int) require("HS_TUNE_FAMILY_IVB", MH_HS_TUNE_FAMILY_IVB).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_HSW() {
        try {
            return (int) require("HS_TUNE_FAMILY_HSW", MH_HS_TUNE_FAMILY_HSW).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_SLM() {
        try {
            return (int) require("HS_TUNE_FAMILY_SLM", MH_HS_TUNE_FAMILY_SLM).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_BDW() {
        try {
            return (int) require("HS_TUNE_FAMILY_BDW", MH_HS_TUNE_FAMILY_BDW).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_SKL() {
        try {
            return (int) require("HS_TUNE_FAMILY_SKL", MH_HS_TUNE_FAMILY_SKL).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_SKX() {
        try {
            return (int) require("HS_TUNE_FAMILY_SKX", MH_HS_TUNE_FAMILY_SKX).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_GLM() {
        try {
            return (int) require("HS_TUNE_FAMILY_GLM", MH_HS_TUNE_FAMILY_GLM).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_ICL() {
        try {
            return (int) require("HS_TUNE_FAMILY_ICL", MH_HS_TUNE_FAMILY_ICL).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_TUNE_FAMILY_ICX() {
        try {
            return (int) require("HS_TUNE_FAMILY_ICX", MH_HS_TUNE_FAMILY_ICX).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_BLOCK() {
        try {
            return (int) require("HS_MODE_BLOCK", MH_HS_MODE_BLOCK).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_NOSTREAM() {
        try {
            return (int) require("HS_MODE_NOSTREAM", MH_HS_MODE_NOSTREAM).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_STREAM() {
        try {
            return (int) require("HS_MODE_STREAM", MH_HS_MODE_STREAM).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_VECTORED() {
        try {
            return (int) require("HS_MODE_VECTORED", MH_HS_MODE_VECTORED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MAJOR() {
        try {
            return (int) require("HS_MAJOR", MH_HS_MAJOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MINOR() {
        try {
            return (int) require("HS_MINOR", MH_HS_MINOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_PATCH() {
        try {
            return (int) require("HS_PATCH", MH_HS_PATCH).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor __ctype_get_mb_cur_max$descriptor() {
        try {
            return (FunctionDescriptor) require("__ctype_get_mb_cur_max$descriptor", MH_CTYPE_GET_MB_CUR_MAX_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle __ctype_get_mb_cur_max$handle() {
        try {
            return (MethodHandle) require("__ctype_get_mb_cur_max$handle", MH_CTYPE_GET_MB_CUR_MAX_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment __ctype_get_mb_cur_max$address() {
        try {
            return (MemorySegment) require("__ctype_get_mb_cur_max$address", MH_CTYPE_GET_MB_CUR_MAX_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long __ctype_get_mb_cur_max() {
        try {
            return (long) require("__ctype_get_mb_cur_max", MH_CTYPE_GET_MB_CUR_MAX).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atof$descriptor() {
        try {
            return (FunctionDescriptor) require("atof$descriptor", MH_ATOF_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atof$handle() {
        try {
            return (MethodHandle) require("atof$handle", MH_ATOF_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atof$address() {
        try {
            return (MemorySegment) require("atof$address", MH_ATOF_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static double atof(MemorySegment arg0) {
        try {
            return (double) require("atof", MH_ATOF).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atoi$descriptor() {
        try {
            return (FunctionDescriptor) require("atoi$descriptor", MH_ATOI_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atoi$handle() {
        try {
            return (MethodHandle) require("atoi$handle", MH_ATOI_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atoi$address() {
        try {
            return (MemorySegment) require("atoi$address", MH_ATOI_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int atoi(MemorySegment arg0) {
        try {
            return (int) require("atoi", MH_ATOI).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atol$descriptor() {
        try {
            return (FunctionDescriptor) require("atol$descriptor", MH_ATOL_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atol$handle() {
        try {
            return (MethodHandle) require("atol$handle", MH_ATOL_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atol$address() {
        try {
            return (MemorySegment) require("atol$address", MH_ATOL_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long atol(MemorySegment arg0) {
        try {
            return (long) require("atol", MH_ATOL).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atoll$descriptor() {
        try {
            return (FunctionDescriptor) require("atoll$descriptor", MH_ATOLL_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atoll$handle() {
        try {
            return (MethodHandle) require("atoll$handle", MH_ATOLL_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atoll$address() {
        try {
            return (MemorySegment) require("atoll$address", MH_ATOLL_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long atoll(MemorySegment arg0) {
        try {
            return (long) require("atoll", MH_ATOLL).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtod$descriptor() {
        try {
            return (FunctionDescriptor) require("strtod$descriptor", MH_STRTOD_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtod$handle() {
        try {
            return (MethodHandle) require("strtod$handle", MH_STRTOD_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtod$address() {
        try {
            return (MemorySegment) require("strtod$address", MH_STRTOD_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static double strtod(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (double) require("strtod", MH_STRTOD).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtof$descriptor() {
        try {
            return (FunctionDescriptor) require("strtof$descriptor", MH_STRTOF_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtof$handle() {
        try {
            return (MethodHandle) require("strtof$handle", MH_STRTOF_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtof$address() {
        try {
            return (MemorySegment) require("strtof$address", MH_STRTOF_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static float strtof(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (float) require("strtof", MH_STRTOF).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtol$descriptor() {
        try {
            return (FunctionDescriptor) require("strtol$descriptor", MH_STRTOL_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtol$handle() {
        try {
            return (MethodHandle) require("strtol$handle", MH_STRTOL_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtol$address() {
        try {
            return (MemorySegment) require("strtol$address", MH_STRTOL_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtol(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) require("strtol", MH_STRTOL).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtoul$descriptor() {
        try {
            return (FunctionDescriptor) require("strtoul$descriptor", MH_STRTOUL_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtoul$handle() {
        try {
            return (MethodHandle) require("strtoul$handle", MH_STRTOUL_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtoul$address() {
        try {
            return (MemorySegment) require("strtoul$address", MH_STRTOUL_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtoul(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) require("strtoul", MH_STRTOUL).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtoq$descriptor() {
        try {
            return (FunctionDescriptor) require("strtoq$descriptor", MH_STRTOQ_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtoq$handle() {
        try {
            return (MethodHandle) require("strtoq$handle", MH_STRTOQ_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtoq$address() {
        try {
            return (MemorySegment) require("strtoq$address", MH_STRTOQ_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtoq(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) require("strtoq", MH_STRTOQ).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtouq$descriptor() {
        try {
            return (FunctionDescriptor) require("strtouq$descriptor", MH_STRTOUQ_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtouq$handle() {
        try {
            return (MethodHandle) require("strtouq$handle", MH_STRTOUQ_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtouq$address() {
        try {
            return (MemorySegment) require("strtouq$address", MH_STRTOUQ_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtouq(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) require("strtouq", MH_STRTOUQ).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtoll$descriptor() {
        try {
            return (FunctionDescriptor) require("strtoll$descriptor", MH_STRTOLL_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtoll$handle() {
        try {
            return (MethodHandle) require("strtoll$handle", MH_STRTOLL_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtoll$address() {
        try {
            return (MemorySegment) require("strtoll$address", MH_STRTOLL_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtoll(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) require("strtoll", MH_STRTOLL).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor strtoull$descriptor() {
        try {
            return (FunctionDescriptor) require("strtoull$descriptor", MH_STRTOULL_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle strtoull$handle() {
        try {
            return (MethodHandle) require("strtoull$handle", MH_STRTOULL_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment strtoull$address() {
        try {
            return (MemorySegment) require("strtoull$address", MH_STRTOULL_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long strtoull(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (long) require("strtoull", MH_STRTOULL).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor l64a$descriptor() {
        try {
            return (FunctionDescriptor) require("l64a$descriptor", MH_L64A_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle l64a$handle() {
        try {
            return (MethodHandle) require("l64a$handle", MH_L64A_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment l64a$address() {
        try {
            return (MemorySegment) require("l64a$address", MH_L64A_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment l64a(long arg0) {
        try {
            return (MemorySegment) require("l64a", MH_L64A).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor a64l$descriptor() {
        try {
            return (FunctionDescriptor) require("a64l$descriptor", MH_A64L_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle a64l$handle() {
        try {
            return (MethodHandle) require("a64l$handle", MH_A64L_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment a64l$address() {
        try {
            return (MemorySegment) require("a64l$address", MH_A64L_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long a64l(MemorySegment arg0) {
        try {
            return (long) require("a64l", MH_A64L).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor select$descriptor() {
        try {
            return (FunctionDescriptor) require("select$descriptor", MH_SELECT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle select$handle() {
        try {
            return (MethodHandle) require("select$handle", MH_SELECT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment select$address() {
        try {
            return (MemorySegment) require("select$address", MH_SELECT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int select(int arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4) {
        try {
            return (int) require("select", MH_SELECT).invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor pselect$descriptor() {
        try {
            return (FunctionDescriptor) require("pselect$descriptor", MH_PSELECT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle pselect$handle() {
        try {
            return (MethodHandle) require("pselect$handle", MH_PSELECT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment pselect$address() {
        try {
            return (MemorySegment) require("pselect$address", MH_PSELECT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int pselect(int arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4, MemorySegment arg5) {
        try {
            return (int) require("pselect", MH_PSELECT).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor random$descriptor() {
        try {
            return (FunctionDescriptor) require("random$descriptor", MH_RANDOM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle random$handle() {
        try {
            return (MethodHandle) require("random$handle", MH_RANDOM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment random$address() {
        try {
            return (MemorySegment) require("random$address", MH_RANDOM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long random() {
        try {
            return (long) require("random", MH_RANDOM).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srandom$descriptor() {
        try {
            return (FunctionDescriptor) require("srandom$descriptor", MH_SRANDOM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srandom$handle() {
        try {
            return (MethodHandle) require("srandom$handle", MH_SRANDOM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srandom$address() {
        try {
            return (MemorySegment) require("srandom$address", MH_SRANDOM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void srandom(int arg0) {
        try {
            require("srandom", MH_SRANDOM).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor initstate$descriptor() {
        try {
            return (FunctionDescriptor) require("initstate$descriptor", MH_INITSTATE_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle initstate$handle() {
        try {
            return (MethodHandle) require("initstate$handle", MH_INITSTATE_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment initstate$address() {
        try {
            return (MemorySegment) require("initstate$address", MH_INITSTATE_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment initstate(int arg0, MemorySegment arg1, long arg2) {
        try {
            return (MemorySegment) require("initstate", MH_INITSTATE).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor setstate$descriptor() {
        try {
            return (FunctionDescriptor) require("setstate$descriptor", MH_SETSTATE_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle setstate$handle() {
        try {
            return (MethodHandle) require("setstate$handle", MH_SETSTATE_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment setstate$address() {
        try {
            return (MemorySegment) require("setstate$address", MH_SETSTATE_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment setstate(MemorySegment arg0) {
        try {
            return (MemorySegment) require("setstate", MH_SETSTATE).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor random_r$descriptor() {
        try {
            return (FunctionDescriptor) require("random_r$descriptor", MH_RANDOM_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle random_r$handle() {
        try {
            return (MethodHandle) require("random_r$handle", MH_RANDOM_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment random_r$address() {
        try {
            return (MemorySegment) require("random_r$address", MH_RANDOM_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int random_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("random_r", MH_RANDOM_R).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srandom_r$descriptor() {
        try {
            return (FunctionDescriptor) require("srandom_r$descriptor", MH_SRANDOM_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srandom_r$handle() {
        try {
            return (MethodHandle) require("srandom_r$handle", MH_SRANDOM_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srandom_r$address() {
        try {
            return (MemorySegment) require("srandom_r$address", MH_SRANDOM_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int srandom_r(int arg0, MemorySegment arg1) {
        try {
            return (int) require("srandom_r", MH_SRANDOM_R).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor initstate_r$descriptor() {
        try {
            return (FunctionDescriptor) require("initstate_r$descriptor", MH_INITSTATE_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle initstate_r$handle() {
        try {
            return (MethodHandle) require("initstate_r$handle", MH_INITSTATE_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment initstate_r$address() {
        try {
            return (MemorySegment) require("initstate_r$address", MH_INITSTATE_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int initstate_r(int arg0, MemorySegment arg1, long arg2, MemorySegment arg3) {
        try {
            return (int) require("initstate_r", MH_INITSTATE_R).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor setstate_r$descriptor() {
        try {
            return (FunctionDescriptor) require("setstate_r$descriptor", MH_SETSTATE_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle setstate_r$handle() {
        try {
            return (MethodHandle) require("setstate_r$handle", MH_SETSTATE_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment setstate_r$address() {
        try {
            return (MemorySegment) require("setstate_r$address", MH_SETSTATE_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int setstate_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("setstate_r", MH_SETSTATE_R).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor rand$descriptor() {
        try {
            return (FunctionDescriptor) require("rand$descriptor", MH_RAND_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle rand$handle() {
        try {
            return (MethodHandle) require("rand$handle", MH_RAND_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment rand$address() {
        try {
            return (MemorySegment) require("rand$address", MH_RAND_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int rand() {
        try {
            return (int) require("rand", MH_RAND).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srand$descriptor() {
        try {
            return (FunctionDescriptor) require("srand$descriptor", MH_SRAND_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srand$handle() {
        try {
            return (MethodHandle) require("srand$handle", MH_SRAND_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srand$address() {
        try {
            return (MemorySegment) require("srand$address", MH_SRAND_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void srand(int arg0) {
        try {
            require("srand", MH_SRAND).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor rand_r$descriptor() {
        try {
            return (FunctionDescriptor) require("rand_r$descriptor", MH_RAND_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle rand_r$handle() {
        try {
            return (MethodHandle) require("rand_r$handle", MH_RAND_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment rand_r$address() {
        try {
            return (MemorySegment) require("rand_r$address", MH_RAND_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int rand_r(MemorySegment arg0) {
        try {
            return (int) require("rand_r", MH_RAND_R).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor drand48$descriptor() {
        try {
            return (FunctionDescriptor) require("drand48$descriptor", MH_DRAND48_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle drand48$handle() {
        try {
            return (MethodHandle) require("drand48$handle", MH_DRAND48_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment drand48$address() {
        try {
            return (MemorySegment) require("drand48$address", MH_DRAND48_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static double drand48() {
        try {
            return (double) require("drand48", MH_DRAND48).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor erand48$descriptor() {
        try {
            return (FunctionDescriptor) require("erand48$descriptor", MH_ERAND48_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle erand48$handle() {
        try {
            return (MethodHandle) require("erand48$handle", MH_ERAND48_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment erand48$address() {
        try {
            return (MemorySegment) require("erand48$address", MH_ERAND48_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static double erand48(MemorySegment arg0) {
        try {
            return (double) require("erand48", MH_ERAND48).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lrand48$descriptor() {
        try {
            return (FunctionDescriptor) require("lrand48$descriptor", MH_LRAND48_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lrand48$handle() {
        try {
            return (MethodHandle) require("lrand48$handle", MH_LRAND48_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lrand48$address() {
        try {
            return (MemorySegment) require("lrand48$address", MH_LRAND48_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long lrand48() {
        try {
            return (long) require("lrand48", MH_LRAND48).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor nrand48$descriptor() {
        try {
            return (FunctionDescriptor) require("nrand48$descriptor", MH_NRAND48_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle nrand48$handle() {
        try {
            return (MethodHandle) require("nrand48$handle", MH_NRAND48_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment nrand48$address() {
        try {
            return (MemorySegment) require("nrand48$address", MH_NRAND48_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long nrand48(MemorySegment arg0) {
        try {
            return (long) require("nrand48", MH_NRAND48).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mrand48$descriptor() {
        try {
            return (FunctionDescriptor) require("mrand48$descriptor", MH_MRAND48_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mrand48$handle() {
        try {
            return (MethodHandle) require("mrand48$handle", MH_MRAND48_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mrand48$address() {
        try {
            return (MemorySegment) require("mrand48$address", MH_MRAND48_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long mrand48() {
        try {
            return (long) require("mrand48", MH_MRAND48).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor jrand48$descriptor() {
        try {
            return (FunctionDescriptor) require("jrand48$descriptor", MH_JRAND48_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle jrand48$handle() {
        try {
            return (MethodHandle) require("jrand48$handle", MH_JRAND48_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment jrand48$address() {
        try {
            return (MemorySegment) require("jrand48$address", MH_JRAND48_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long jrand48(MemorySegment arg0) {
        try {
            return (long) require("jrand48", MH_JRAND48).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srand48$descriptor() {
        try {
            return (FunctionDescriptor) require("srand48$descriptor", MH_SRAND48_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srand48$handle() {
        try {
            return (MethodHandle) require("srand48$handle", MH_SRAND48_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srand48$address() {
        try {
            return (MemorySegment) require("srand48$address", MH_SRAND48_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void srand48(long arg0) {
        try {
            require("srand48", MH_SRAND48).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor seed48$descriptor() {
        try {
            return (FunctionDescriptor) require("seed48$descriptor", MH_SEED48_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle seed48$handle() {
        try {
            return (MethodHandle) require("seed48$handle", MH_SEED48_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment seed48$address() {
        try {
            return (MemorySegment) require("seed48$address", MH_SEED48_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment seed48(MemorySegment arg0) {
        try {
            return (MemorySegment) require("seed48", MH_SEED48).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lcong48$descriptor() {
        try {
            return (FunctionDescriptor) require("lcong48$descriptor", MH_LCONG48_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lcong48$handle() {
        try {
            return (MethodHandle) require("lcong48$handle", MH_LCONG48_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lcong48$address() {
        try {
            return (MemorySegment) require("lcong48$address", MH_LCONG48_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void lcong48(MemorySegment arg0) {
        try {
            require("lcong48", MH_LCONG48).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor drand48_r$descriptor() {
        try {
            return (FunctionDescriptor) require("drand48_r$descriptor", MH_DRAND48_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle drand48_r$handle() {
        try {
            return (MethodHandle) require("drand48_r$handle", MH_DRAND48_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment drand48_r$address() {
        try {
            return (MemorySegment) require("drand48_r$address", MH_DRAND48_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int drand48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("drand48_r", MH_DRAND48_R).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor erand48_r$descriptor() {
        try {
            return (FunctionDescriptor) require("erand48_r$descriptor", MH_ERAND48_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle erand48_r$handle() {
        try {
            return (MethodHandle) require("erand48_r$handle", MH_ERAND48_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment erand48_r$address() {
        try {
            return (MemorySegment) require("erand48_r$address", MH_ERAND48_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int erand48_r(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) require("erand48_r", MH_ERAND48_R).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lrand48_r$descriptor() {
        try {
            return (FunctionDescriptor) require("lrand48_r$descriptor", MH_LRAND48_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lrand48_r$handle() {
        try {
            return (MethodHandle) require("lrand48_r$handle", MH_LRAND48_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lrand48_r$address() {
        try {
            return (MemorySegment) require("lrand48_r$address", MH_LRAND48_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int lrand48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("lrand48_r", MH_LRAND48_R).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor nrand48_r$descriptor() {
        try {
            return (FunctionDescriptor) require("nrand48_r$descriptor", MH_NRAND48_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle nrand48_r$handle() {
        try {
            return (MethodHandle) require("nrand48_r$handle", MH_NRAND48_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment nrand48_r$address() {
        try {
            return (MemorySegment) require("nrand48_r$address", MH_NRAND48_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int nrand48_r(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) require("nrand48_r", MH_NRAND48_R).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mrand48_r$descriptor() {
        try {
            return (FunctionDescriptor) require("mrand48_r$descriptor", MH_MRAND48_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mrand48_r$handle() {
        try {
            return (MethodHandle) require("mrand48_r$handle", MH_MRAND48_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mrand48_r$address() {
        try {
            return (MemorySegment) require("mrand48_r$address", MH_MRAND48_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mrand48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("mrand48_r", MH_MRAND48_R).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor jrand48_r$descriptor() {
        try {
            return (FunctionDescriptor) require("jrand48_r$descriptor", MH_JRAND48_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle jrand48_r$handle() {
        try {
            return (MethodHandle) require("jrand48_r$handle", MH_JRAND48_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment jrand48_r$address() {
        try {
            return (MemorySegment) require("jrand48_r$address", MH_JRAND48_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int jrand48_r(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) require("jrand48_r", MH_JRAND48_R).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor srand48_r$descriptor() {
        try {
            return (FunctionDescriptor) require("srand48_r$descriptor", MH_SRAND48_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle srand48_r$handle() {
        try {
            return (MethodHandle) require("srand48_r$handle", MH_SRAND48_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment srand48_r$address() {
        try {
            return (MemorySegment) require("srand48_r$address", MH_SRAND48_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int srand48_r(long arg0, MemorySegment arg1) {
        try {
            return (int) require("srand48_r", MH_SRAND48_R).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor seed48_r$descriptor() {
        try {
            return (FunctionDescriptor) require("seed48_r$descriptor", MH_SEED48_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle seed48_r$handle() {
        try {
            return (MethodHandle) require("seed48_r$handle", MH_SEED48_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment seed48_r$address() {
        try {
            return (MemorySegment) require("seed48_r$address", MH_SEED48_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int seed48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("seed48_r", MH_SEED48_R).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lcong48_r$descriptor() {
        try {
            return (FunctionDescriptor) require("lcong48_r$descriptor", MH_LCONG48_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lcong48_r$handle() {
        try {
            return (MethodHandle) require("lcong48_r$handle", MH_LCONG48_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lcong48_r$address() {
        try {
            return (MemorySegment) require("lcong48_r$address", MH_LCONG48_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int lcong48_r(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("lcong48_r", MH_LCONG48_R).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor arc4random$descriptor() {
        try {
            return (FunctionDescriptor) require("arc4random$descriptor", MH_ARC4RANDOM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle arc4random$handle() {
        try {
            return (MethodHandle) require("arc4random$handle", MH_ARC4RANDOM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment arc4random$address() {
        try {
            return (MemorySegment) require("arc4random$address", MH_ARC4RANDOM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int arc4random() {
        try {
            return (int) require("arc4random", MH_ARC4RANDOM).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor arc4random_buf$descriptor() {
        try {
            return (FunctionDescriptor) require("arc4random_buf$descriptor", MH_ARC4RANDOM_BUF_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle arc4random_buf$handle() {
        try {
            return (MethodHandle) require("arc4random_buf$handle", MH_ARC4RANDOM_BUF_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment arc4random_buf$address() {
        try {
            return (MemorySegment) require("arc4random_buf$address", MH_ARC4RANDOM_BUF_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void arc4random_buf(MemorySegment arg0, long arg1) {
        try {
            require("arc4random_buf", MH_ARC4RANDOM_BUF).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor arc4random_uniform$descriptor() {
        try {
            return (FunctionDescriptor) require("arc4random_uniform$descriptor", MH_ARC4RANDOM_UNIFORM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle arc4random_uniform$handle() {
        try {
            return (MethodHandle) require("arc4random_uniform$handle", MH_ARC4RANDOM_UNIFORM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment arc4random_uniform$address() {
        try {
            return (MemorySegment) require("arc4random_uniform$address", MH_ARC4RANDOM_UNIFORM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int arc4random_uniform(int arg0) {
        try {
            return (int) require("arc4random_uniform", MH_ARC4RANDOM_UNIFORM).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor malloc$descriptor() {
        try {
            return (FunctionDescriptor) require("malloc$descriptor", MH_MALLOC_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle malloc$handle() {
        try {
            return (MethodHandle) require("malloc$handle", MH_MALLOC_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment malloc$address() {
        try {
            return (MemorySegment) require("malloc$address", MH_MALLOC_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment malloc(long arg0) {
        try {
            return (MemorySegment) require("malloc", MH_MALLOC).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor calloc$descriptor() {
        try {
            return (FunctionDescriptor) require("calloc$descriptor", MH_CALLOC_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle calloc$handle() {
        try {
            return (MethodHandle) require("calloc$handle", MH_CALLOC_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment calloc$address() {
        try {
            return (MemorySegment) require("calloc$address", MH_CALLOC_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment calloc(long arg0, long arg1) {
        try {
            return (MemorySegment) require("calloc", MH_CALLOC).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor realloc$descriptor() {
        try {
            return (FunctionDescriptor) require("realloc$descriptor", MH_REALLOC_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle realloc$handle() {
        try {
            return (MethodHandle) require("realloc$handle", MH_REALLOC_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment realloc$address() {
        try {
            return (MemorySegment) require("realloc$address", MH_REALLOC_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment realloc(MemorySegment arg0, long arg1) {
        try {
            return (MemorySegment) require("realloc", MH_REALLOC).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor free$descriptor() {
        try {
            return (FunctionDescriptor) require("free$descriptor", MH_FREE_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle free$handle() {
        try {
            return (MethodHandle) require("free$handle", MH_FREE_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment free$address() {
        try {
            return (MemorySegment) require("free$address", MH_FREE_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void free(MemorySegment arg0) {
        try {
            require("free", MH_FREE).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor reallocarray$descriptor() {
        try {
            return (FunctionDescriptor) require("reallocarray$descriptor", MH_REALLOCARRAY_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle reallocarray$handle() {
        try {
            return (MethodHandle) require("reallocarray$handle", MH_REALLOCARRAY_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment reallocarray$address() {
        try {
            return (MemorySegment) require("reallocarray$address", MH_REALLOCARRAY_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment reallocarray(MemorySegment arg0, long arg1, long arg2) {
        try {
            return (MemorySegment) require("reallocarray", MH_REALLOCARRAY).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor alloca$descriptor() {
        try {
            return (FunctionDescriptor) require("alloca$descriptor", MH_ALLOCA_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle alloca$handle() {
        try {
            return (MethodHandle) require("alloca$handle", MH_ALLOCA_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment alloca$address() {
        try {
            return (MemorySegment) require("alloca$address", MH_ALLOCA_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment alloca(long arg0) {
        try {
            return (MemorySegment) require("alloca", MH_ALLOCA).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor valloc$descriptor() {
        try {
            return (FunctionDescriptor) require("valloc$descriptor", MH_VALLOC_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle valloc$handle() {
        try {
            return (MethodHandle) require("valloc$handle", MH_VALLOC_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment valloc$address() {
        try {
            return (MemorySegment) require("valloc$address", MH_VALLOC_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment valloc(long arg0) {
        try {
            return (MemorySegment) require("valloc", MH_VALLOC).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor posix_memalign$descriptor() {
        try {
            return (FunctionDescriptor) require("posix_memalign$descriptor", MH_POSIX_MEMALIGN_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle posix_memalign$handle() {
        try {
            return (MethodHandle) require("posix_memalign$handle", MH_POSIX_MEMALIGN_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment posix_memalign$address() {
        try {
            return (MemorySegment) require("posix_memalign$address", MH_POSIX_MEMALIGN_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int posix_memalign(MemorySegment arg0, long arg1, long arg2) {
        try {
            return (int) require("posix_memalign", MH_POSIX_MEMALIGN).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor aligned_alloc$descriptor() {
        try {
            return (FunctionDescriptor) require("aligned_alloc$descriptor", MH_ALIGNED_ALLOC_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle aligned_alloc$handle() {
        try {
            return (MethodHandle) require("aligned_alloc$handle", MH_ALIGNED_ALLOC_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment aligned_alloc$address() {
        try {
            return (MemorySegment) require("aligned_alloc$address", MH_ALIGNED_ALLOC_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment aligned_alloc(long arg0, long arg1) {
        try {
            return (MemorySegment) require("aligned_alloc", MH_ALIGNED_ALLOC).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor abort$descriptor() {
        try {
            return (FunctionDescriptor) require("abort$descriptor", MH_ABORT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle abort$handle() {
        try {
            return (MethodHandle) require("abort$handle", MH_ABORT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment abort$address() {
        try {
            return (MemorySegment) require("abort$address", MH_ABORT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void abort() {
        try {
            require("abort", MH_ABORT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor atexit$descriptor() {
        try {
            return (FunctionDescriptor) require("atexit$descriptor", MH_ATEXIT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle atexit$handle() {
        try {
            return (MethodHandle) require("atexit$handle", MH_ATEXIT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment atexit$address() {
        try {
            return (MemorySegment) require("atexit$address", MH_ATEXIT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int atexit(MemorySegment arg0) {
        try {
            return (int) require("atexit", MH_ATEXIT).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor at_quick_exit$descriptor() {
        try {
            return (FunctionDescriptor) require("at_quick_exit$descriptor", MH_AT_QUICK_EXIT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle at_quick_exit$handle() {
        try {
            return (MethodHandle) require("at_quick_exit$handle", MH_AT_QUICK_EXIT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment at_quick_exit$address() {
        try {
            return (MemorySegment) require("at_quick_exit$address", MH_AT_QUICK_EXIT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int at_quick_exit(MemorySegment arg0) {
        try {
            return (int) require("at_quick_exit", MH_AT_QUICK_EXIT).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor on_exit$descriptor() {
        try {
            return (FunctionDescriptor) require("on_exit$descriptor", MH_ON_EXIT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle on_exit$handle() {
        try {
            return (MethodHandle) require("on_exit$handle", MH_ON_EXIT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment on_exit$address() {
        try {
            return (MemorySegment) require("on_exit$address", MH_ON_EXIT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int on_exit(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("on_exit", MH_ON_EXIT).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor exit$descriptor() {
        try {
            return (FunctionDescriptor) require("exit$descriptor", MH_EXIT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle exit$handle() {
        try {
            return (MethodHandle) require("exit$handle", MH_EXIT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment exit$address() {
        try {
            return (MemorySegment) require("exit$address", MH_EXIT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void exit(int arg0) {
        try {
            require("exit", MH_EXIT).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor quick_exit$descriptor() {
        try {
            return (FunctionDescriptor) require("quick_exit$descriptor", MH_QUICK_EXIT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle quick_exit$handle() {
        try {
            return (MethodHandle) require("quick_exit$handle", MH_QUICK_EXIT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment quick_exit$address() {
        try {
            return (MemorySegment) require("quick_exit$address", MH_QUICK_EXIT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void quick_exit(int arg0) {
        try {
            require("quick_exit", MH_QUICK_EXIT).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor _Exit$descriptor() {
        try {
            return (FunctionDescriptor) require("_Exit$descriptor", MH_EXIT_DESCRIPTOR_1).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle _Exit$handle() {
        try {
            return (MethodHandle) require("_Exit$handle", MH_EXIT_HANDLE_1).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment _Exit$address() {
        try {
            return (MemorySegment) require("_Exit$address", MH_EXIT_ADDRESS_1).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void _Exit(int arg0) {
        try {
            require("_Exit", MH_EXIT_1).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor getenv$descriptor() {
        try {
            return (FunctionDescriptor) require("getenv$descriptor", MH_GETENV_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle getenv$handle() {
        try {
            return (MethodHandle) require("getenv$handle", MH_GETENV_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment getenv$address() {
        try {
            return (MemorySegment) require("getenv$address", MH_GETENV_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment getenv(MemorySegment arg0) {
        try {
            return (MemorySegment) require("getenv", MH_GETENV).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor putenv$descriptor() {
        try {
            return (FunctionDescriptor) require("putenv$descriptor", MH_PUTENV_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle putenv$handle() {
        try {
            return (MethodHandle) require("putenv$handle", MH_PUTENV_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment putenv$address() {
        try {
            return (MemorySegment) require("putenv$address", MH_PUTENV_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int putenv(MemorySegment arg0) {
        try {
            return (int) require("putenv", MH_PUTENV).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor setenv$descriptor() {
        try {
            return (FunctionDescriptor) require("setenv$descriptor", MH_SETENV_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle setenv$handle() {
        try {
            return (MethodHandle) require("setenv$handle", MH_SETENV_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment setenv$address() {
        try {
            return (MemorySegment) require("setenv$address", MH_SETENV_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int setenv(MemorySegment arg0, MemorySegment arg1, int arg2) {
        try {
            return (int) require("setenv", MH_SETENV).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor unsetenv$descriptor() {
        try {
            return (FunctionDescriptor) require("unsetenv$descriptor", MH_UNSETENV_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle unsetenv$handle() {
        try {
            return (MethodHandle) require("unsetenv$handle", MH_UNSETENV_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment unsetenv$address() {
        try {
            return (MemorySegment) require("unsetenv$address", MH_UNSETENV_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int unsetenv(MemorySegment arg0) {
        try {
            return (int) require("unsetenv", MH_UNSETENV).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor clearenv$descriptor() {
        try {
            return (FunctionDescriptor) require("clearenv$descriptor", MH_CLEARENV_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle clearenv$handle() {
        try {
            return (MethodHandle) require("clearenv$handle", MH_CLEARENV_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment clearenv$address() {
        try {
            return (MemorySegment) require("clearenv$address", MH_CLEARENV_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int clearenv() {
        try {
            return (int) require("clearenv", MH_CLEARENV).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mktemp$descriptor() {
        try {
            return (FunctionDescriptor) require("mktemp$descriptor", MH_MKTEMP_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mktemp$handle() {
        try {
            return (MethodHandle) require("mktemp$handle", MH_MKTEMP_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mktemp$address() {
        try {
            return (MemorySegment) require("mktemp$address", MH_MKTEMP_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mktemp(MemorySegment arg0) {
        try {
            return (MemorySegment) require("mktemp", MH_MKTEMP).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mkstemp$descriptor() {
        try {
            return (FunctionDescriptor) require("mkstemp$descriptor", MH_MKSTEMP_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mkstemp$handle() {
        try {
            return (MethodHandle) require("mkstemp$handle", MH_MKSTEMP_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mkstemp$address() {
        try {
            return (MemorySegment) require("mkstemp$address", MH_MKSTEMP_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mkstemp(MemorySegment arg0) {
        try {
            return (int) require("mkstemp", MH_MKSTEMP).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mkstemps$descriptor() {
        try {
            return (FunctionDescriptor) require("mkstemps$descriptor", MH_MKSTEMPS_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mkstemps$handle() {
        try {
            return (MethodHandle) require("mkstemps$handle", MH_MKSTEMPS_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mkstemps$address() {
        try {
            return (MemorySegment) require("mkstemps$address", MH_MKSTEMPS_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mkstemps(MemorySegment arg0, int arg1) {
        try {
            return (int) require("mkstemps", MH_MKSTEMPS).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mkdtemp$descriptor() {
        try {
            return (FunctionDescriptor) require("mkdtemp$descriptor", MH_MKDTEMP_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mkdtemp$handle() {
        try {
            return (MethodHandle) require("mkdtemp$handle", MH_MKDTEMP_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mkdtemp$address() {
        try {
            return (MemorySegment) require("mkdtemp$address", MH_MKDTEMP_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mkdtemp(MemorySegment arg0) {
        try {
            return (MemorySegment) require("mkdtemp", MH_MKDTEMP).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor system$descriptor() {
        try {
            return (FunctionDescriptor) require("system$descriptor", MH_SYSTEM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle system$handle() {
        try {
            return (MethodHandle) require("system$handle", MH_SYSTEM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment system$address() {
        try {
            return (MemorySegment) require("system$address", MH_SYSTEM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int system(MemorySegment arg0) {
        try {
            return (int) require("system", MH_SYSTEM).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor realpath$descriptor() {
        try {
            return (FunctionDescriptor) require("realpath$descriptor", MH_REALPATH_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle realpath$handle() {
        try {
            return (MethodHandle) require("realpath$handle", MH_REALPATH_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment realpath$address() {
        try {
            return (MemorySegment) require("realpath$address", MH_REALPATH_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment realpath(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (MemorySegment) require("realpath", MH_REALPATH).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor bsearch$descriptor() {
        try {
            return (FunctionDescriptor) require("bsearch$descriptor", MH_BSEARCH_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle bsearch$handle() {
        try {
            return (MethodHandle) require("bsearch$handle", MH_BSEARCH_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment bsearch$address() {
        try {
            return (MemorySegment) require("bsearch$address", MH_BSEARCH_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment bsearch(MemorySegment arg0, MemorySegment arg1, long arg2, long arg3, MemorySegment arg4) {
        try {
            return (MemorySegment) require("bsearch", MH_BSEARCH).invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor qsort$descriptor() {
        try {
            return (FunctionDescriptor) require("qsort$descriptor", MH_QSORT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle qsort$handle() {
        try {
            return (MethodHandle) require("qsort$handle", MH_QSORT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment qsort$address() {
        try {
            return (MemorySegment) require("qsort$address", MH_QSORT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void qsort(MemorySegment arg0, long arg1, long arg2, MemorySegment arg3) {
        try {
            require("qsort", MH_QSORT).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor abs$descriptor() {
        try {
            return (FunctionDescriptor) require("abs$descriptor", MH_ABS_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle abs$handle() {
        try {
            return (MethodHandle) require("abs$handle", MH_ABS_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment abs$address() {
        try {
            return (MemorySegment) require("abs$address", MH_ABS_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int abs(int arg0) {
        try {
            return (int) require("abs", MH_ABS).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor labs$descriptor() {
        try {
            return (FunctionDescriptor) require("labs$descriptor", MH_LABS_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle labs$handle() {
        try {
            return (MethodHandle) require("labs$handle", MH_LABS_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment labs$address() {
        try {
            return (MemorySegment) require("labs$address", MH_LABS_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long labs(long arg0) {
        try {
            return (long) require("labs", MH_LABS).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor llabs$descriptor() {
        try {
            return (FunctionDescriptor) require("llabs$descriptor", MH_LLABS_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle llabs$handle() {
        try {
            return (MethodHandle) require("llabs$handle", MH_LLABS_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment llabs$address() {
        try {
            return (MemorySegment) require("llabs$address", MH_LLABS_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long llabs(long arg0) {
        try {
            return (long) require("llabs", MH_LLABS).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor div$descriptor() {
        try {
            return (FunctionDescriptor) require("div$descriptor", MH_DIV_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle div$handle() {
        try {
            return (MethodHandle) require("div$handle", MH_DIV_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment div$address() {
        try {
            return (MemorySegment) require("div$address", MH_DIV_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment div(SegmentAllocator arg0, int arg1, int arg2) {
        try {
            return (MemorySegment) require("div", MH_DIV).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor ldiv$descriptor() {
        try {
            return (FunctionDescriptor) require("ldiv$descriptor", MH_LDIV_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle ldiv$handle() {
        try {
            return (MethodHandle) require("ldiv$handle", MH_LDIV_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ldiv$address() {
        try {
            return (MemorySegment) require("ldiv$address", MH_LDIV_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ldiv(SegmentAllocator arg0, long arg1, long arg2) {
        try {
            return (MemorySegment) require("ldiv", MH_LDIV).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor lldiv$descriptor() {
        try {
            return (FunctionDescriptor) require("lldiv$descriptor", MH_LLDIV_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle lldiv$handle() {
        try {
            return (MethodHandle) require("lldiv$handle", MH_LLDIV_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lldiv$address() {
        try {
            return (MemorySegment) require("lldiv$address", MH_LLDIV_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment lldiv(SegmentAllocator arg0, long arg1, long arg2) {
        try {
            return (MemorySegment) require("lldiv", MH_LLDIV).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor ecvt$descriptor() {
        try {
            return (FunctionDescriptor) require("ecvt$descriptor", MH_ECVT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle ecvt$handle() {
        try {
            return (MethodHandle) require("ecvt$handle", MH_ECVT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ecvt$address() {
        try {
            return (MemorySegment) require("ecvt$address", MH_ECVT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ecvt(double arg0, int arg1, MemorySegment arg2, MemorySegment arg3) {
        try {
            return (MemorySegment) require("ecvt", MH_ECVT).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor fcvt$descriptor() {
        try {
            return (FunctionDescriptor) require("fcvt$descriptor", MH_FCVT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle fcvt$handle() {
        try {
            return (MethodHandle) require("fcvt$handle", MH_FCVT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment fcvt$address() {
        try {
            return (MemorySegment) require("fcvt$address", MH_FCVT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment fcvt(double arg0, int arg1, MemorySegment arg2, MemorySegment arg3) {
        try {
            return (MemorySegment) require("fcvt", MH_FCVT).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor gcvt$descriptor() {
        try {
            return (FunctionDescriptor) require("gcvt$descriptor", MH_GCVT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle gcvt$handle() {
        try {
            return (MethodHandle) require("gcvt$handle", MH_GCVT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment gcvt$address() {
        try {
            return (MemorySegment) require("gcvt$address", MH_GCVT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment gcvt(double arg0, int arg1, MemorySegment arg2) {
        try {
            return (MemorySegment) require("gcvt", MH_GCVT).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor ecvt_r$descriptor() {
        try {
            return (FunctionDescriptor) require("ecvt_r$descriptor", MH_ECVT_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle ecvt_r$handle() {
        try {
            return (MethodHandle) require("ecvt_r$handle", MH_ECVT_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment ecvt_r$address() {
        try {
            return (MemorySegment) require("ecvt_r$address", MH_ECVT_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int ecvt_r(double arg0, int arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4, long arg5) {
        try {
            return (int) require("ecvt_r", MH_ECVT_R).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor fcvt_r$descriptor() {
        try {
            return (FunctionDescriptor) require("fcvt_r$descriptor", MH_FCVT_R_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle fcvt_r$handle() {
        try {
            return (MethodHandle) require("fcvt_r$handle", MH_FCVT_R_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment fcvt_r$address() {
        try {
            return (MemorySegment) require("fcvt_r$address", MH_FCVT_R_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int fcvt_r(double arg0, int arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4, long arg5) {
        try {
            return (int) require("fcvt_r", MH_FCVT_R).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mblen$descriptor() {
        try {
            return (FunctionDescriptor) require("mblen$descriptor", MH_MBLEN_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mblen$handle() {
        try {
            return (MethodHandle) require("mblen$handle", MH_MBLEN_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mblen$address() {
        try {
            return (MemorySegment) require("mblen$address", MH_MBLEN_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mblen(MemorySegment arg0, long arg1) {
        try {
            return (int) require("mblen", MH_MBLEN).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mbtowc$descriptor() {
        try {
            return (FunctionDescriptor) require("mbtowc$descriptor", MH_MBTOWC_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mbtowc$handle() {
        try {
            return (MethodHandle) require("mbtowc$handle", MH_MBTOWC_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mbtowc$address() {
        try {
            return (MemorySegment) require("mbtowc$address", MH_MBTOWC_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int mbtowc(MemorySegment arg0, MemorySegment arg1, long arg2) {
        try {
            return (int) require("mbtowc", MH_MBTOWC).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor wctomb$descriptor() {
        try {
            return (FunctionDescriptor) require("wctomb$descriptor", MH_WCTOMB_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle wctomb$handle() {
        try {
            return (MethodHandle) require("wctomb$handle", MH_WCTOMB_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment wctomb$address() {
        try {
            return (MemorySegment) require("wctomb$address", MH_WCTOMB_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int wctomb(MemorySegment arg0, int arg1) {
        try {
            return (int) require("wctomb", MH_WCTOMB).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor mbstowcs$descriptor() {
        try {
            return (FunctionDescriptor) require("mbstowcs$descriptor", MH_MBSTOWCS_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle mbstowcs$handle() {
        try {
            return (MethodHandle) require("mbstowcs$handle", MH_MBSTOWCS_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment mbstowcs$address() {
        try {
            return (MemorySegment) require("mbstowcs$address", MH_MBSTOWCS_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long mbstowcs(MemorySegment arg0, MemorySegment arg1, long arg2) {
        try {
            return (long) require("mbstowcs", MH_MBSTOWCS).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor wcstombs$descriptor() {
        try {
            return (FunctionDescriptor) require("wcstombs$descriptor", MH_WCSTOMBS_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle wcstombs$handle() {
        try {
            return (MethodHandle) require("wcstombs$handle", MH_WCSTOMBS_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment wcstombs$address() {
        try {
            return (MemorySegment) require("wcstombs$address", MH_WCSTOMBS_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long wcstombs(MemorySegment arg0, MemorySegment arg1, long arg2) {
        try {
            return (long) require("wcstombs", MH_WCSTOMBS).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor rpmatch$descriptor() {
        try {
            return (FunctionDescriptor) require("rpmatch$descriptor", MH_RPMATCH_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle rpmatch$handle() {
        try {
            return (MethodHandle) require("rpmatch$handle", MH_RPMATCH_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment rpmatch$address() {
        try {
            return (MemorySegment) require("rpmatch$address", MH_RPMATCH_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int rpmatch(MemorySegment arg0) {
        try {
            return (int) require("rpmatch", MH_RPMATCH).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor getsubopt$descriptor() {
        try {
            return (FunctionDescriptor) require("getsubopt$descriptor", MH_GETSUBOPT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle getsubopt$handle() {
        try {
            return (MethodHandle) require("getsubopt$handle", MH_GETSUBOPT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment getsubopt$address() {
        try {
            return (MemorySegment) require("getsubopt$address", MH_GETSUBOPT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int getsubopt(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) require("getsubopt", MH_GETSUBOPT).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor getloadavg$descriptor() {
        try {
            return (FunctionDescriptor) require("getloadavg$descriptor", MH_GETLOADAVG_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle getloadavg$handle() {
        try {
            return (MethodHandle) require("getloadavg$handle", MH_GETLOADAVG_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment getloadavg$address() {
        try {
            return (MemorySegment) require("getloadavg$address", MH_GETLOADAVG_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int getloadavg(MemorySegment arg0, int arg1) {
        try {
            return (int) require("getloadavg", MH_GETLOADAVG).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_free_database$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_free_database$descriptor", MH_HS_FREE_DATABASE_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_free_database$handle() {
        try {
            return (MethodHandle) require("hs_free_database$handle", MH_HS_FREE_DATABASE_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_free_database$address() {
        try {
            return (MemorySegment) require("hs_free_database$address", MH_HS_FREE_DATABASE_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_free_database(MemorySegment arg0) {
        try {
            return (int) require("hs_free_database", MH_HS_FREE_DATABASE).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_serialize_database$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_serialize_database$descriptor", MH_HS_SERIALIZE_DATABASE_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_serialize_database$handle() {
        try {
            return (MethodHandle) require("hs_serialize_database$handle", MH_HS_SERIALIZE_DATABASE_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_serialize_database$address() {
        try {
            return (MemorySegment) require("hs_serialize_database$address", MH_HS_SERIALIZE_DATABASE_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_serialize_database(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2) {
        try {
            return (int) require("hs_serialize_database", MH_HS_SERIALIZE_DATABASE).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_deserialize_database$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_deserialize_database$descriptor", MH_HS_DESERIALIZE_DATABASE_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_deserialize_database$handle() {
        try {
            return (MethodHandle) require("hs_deserialize_database$handle", MH_HS_DESERIALIZE_DATABASE_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_deserialize_database$address() {
        try {
            return (MemorySegment) require("hs_deserialize_database$address", MH_HS_DESERIALIZE_DATABASE_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_deserialize_database(MemorySegment arg0, long arg1, MemorySegment arg2) {
        try {
            return (int) require("hs_deserialize_database", MH_HS_DESERIALIZE_DATABASE).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_deserialize_database_at$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_deserialize_database_at$descriptor", MH_HS_DESERIALIZE_DATABASE_AT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_deserialize_database_at$handle() {
        try {
            return (MethodHandle) require("hs_deserialize_database_at$handle", MH_HS_DESERIALIZE_DATABASE_AT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_deserialize_database_at$address() {
        try {
            return (MemorySegment) require("hs_deserialize_database_at$address", MH_HS_DESERIALIZE_DATABASE_AT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_deserialize_database_at(MemorySegment arg0, long arg1, MemorySegment arg2) {
        try {
            return (int) require("hs_deserialize_database_at", MH_HS_DESERIALIZE_DATABASE_AT).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_stream_size$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_stream_size$descriptor", MH_HS_STREAM_SIZE_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_stream_size$handle() {
        try {
            return (MethodHandle) require("hs_stream_size$handle", MH_HS_STREAM_SIZE_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_stream_size$address() {
        try {
            return (MemorySegment) require("hs_stream_size$address", MH_HS_STREAM_SIZE_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_stream_size(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("hs_stream_size", MH_HS_STREAM_SIZE).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_database_size$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_database_size$descriptor", MH_HS_DATABASE_SIZE_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_database_size$handle() {
        try {
            return (MethodHandle) require("hs_database_size$handle", MH_HS_DATABASE_SIZE_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_database_size$address() {
        try {
            return (MemorySegment) require("hs_database_size$address", MH_HS_DATABASE_SIZE_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_database_size(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("hs_database_size", MH_HS_DATABASE_SIZE).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_serialized_database_size$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_serialized_database_size$descriptor", MH_HS_SERIALIZED_DATABASE_SIZE_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_serialized_database_size$handle() {
        try {
            return (MethodHandle) require("hs_serialized_database_size$handle", MH_HS_SERIALIZED_DATABASE_SIZE_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_serialized_database_size$address() {
        try {
            return (MemorySegment) require("hs_serialized_database_size$address", MH_HS_SERIALIZED_DATABASE_SIZE_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_serialized_database_size(MemorySegment arg0, long arg1, MemorySegment arg2) {
        try {
            return (int) require("hs_serialized_database_size", MH_HS_SERIALIZED_DATABASE_SIZE).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_database_info$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_database_info$descriptor", MH_HS_DATABASE_INFO_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_database_info$handle() {
        try {
            return (MethodHandle) require("hs_database_info$handle", MH_HS_DATABASE_INFO_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_database_info$address() {
        try {
            return (MemorySegment) require("hs_database_info$address", MH_HS_DATABASE_INFO_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_database_info(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("hs_database_info", MH_HS_DATABASE_INFO).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_serialized_database_info$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_serialized_database_info$descriptor", MH_HS_SERIALIZED_DATABASE_INFO_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_serialized_database_info$handle() {
        try {
            return (MethodHandle) require("hs_serialized_database_info$handle", MH_HS_SERIALIZED_DATABASE_INFO_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_serialized_database_info$address() {
        try {
            return (MemorySegment) require("hs_serialized_database_info$address", MH_HS_SERIALIZED_DATABASE_INFO_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_serialized_database_info(MemorySegment arg0, long arg1, MemorySegment arg2) {
        try {
            return (int) require("hs_serialized_database_info", MH_HS_SERIALIZED_DATABASE_INFO).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_allocator$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_set_allocator$descriptor", MH_HS_SET_ALLOCATOR_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_allocator$handle() {
        try {
            return (MethodHandle) require("hs_set_allocator$handle", MH_HS_SET_ALLOCATOR_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_allocator$address() {
        try {
            return (MemorySegment) require("hs_set_allocator$address", MH_HS_SET_ALLOCATOR_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("hs_set_allocator", MH_HS_SET_ALLOCATOR).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_database_allocator$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_set_database_allocator$descriptor", MH_HS_SET_DATABASE_ALLOCATOR_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_database_allocator$handle() {
        try {
            return (MethodHandle) require("hs_set_database_allocator$handle", MH_HS_SET_DATABASE_ALLOCATOR_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_database_allocator$address() {
        try {
            return (MemorySegment) require("hs_set_database_allocator$address", MH_HS_SET_DATABASE_ALLOCATOR_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_database_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("hs_set_database_allocator", MH_HS_SET_DATABASE_ALLOCATOR).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_misc_allocator$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_set_misc_allocator$descriptor", MH_HS_SET_MISC_ALLOCATOR_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_misc_allocator$handle() {
        try {
            return (MethodHandle) require("hs_set_misc_allocator$handle", MH_HS_SET_MISC_ALLOCATOR_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_misc_allocator$address() {
        try {
            return (MemorySegment) require("hs_set_misc_allocator$address", MH_HS_SET_MISC_ALLOCATOR_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_misc_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("hs_set_misc_allocator", MH_HS_SET_MISC_ALLOCATOR).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_scratch_allocator$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_set_scratch_allocator$descriptor", MH_HS_SET_SCRATCH_ALLOCATOR_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_scratch_allocator$handle() {
        try {
            return (MethodHandle) require("hs_set_scratch_allocator$handle", MH_HS_SET_SCRATCH_ALLOCATOR_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_scratch_allocator$address() {
        try {
            return (MemorySegment) require("hs_set_scratch_allocator$address", MH_HS_SET_SCRATCH_ALLOCATOR_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_scratch_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("hs_set_scratch_allocator", MH_HS_SET_SCRATCH_ALLOCATOR).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_set_stream_allocator$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_set_stream_allocator$descriptor", MH_HS_SET_STREAM_ALLOCATOR_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_set_stream_allocator$handle() {
        try {
            return (MethodHandle) require("hs_set_stream_allocator$handle", MH_HS_SET_STREAM_ALLOCATOR_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_set_stream_allocator$address() {
        try {
            return (MemorySegment) require("hs_set_stream_allocator$address", MH_HS_SET_STREAM_ALLOCATOR_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_set_stream_allocator(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("hs_set_stream_allocator", MH_HS_SET_STREAM_ALLOCATOR).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_version$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_version$descriptor", MH_HS_VERSION_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_version$handle() {
        try {
            return (MethodHandle) require("hs_version$handle", MH_HS_VERSION_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_version$address() {
        try {
            return (MemorySegment) require("hs_version$address", MH_HS_VERSION_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_version() {
        try {
            return (MemorySegment) require("hs_version", MH_HS_VERSION).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_valid_platform$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_valid_platform$descriptor", MH_HS_VALID_PLATFORM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_valid_platform$handle() {
        try {
            return (MethodHandle) require("hs_valid_platform$handle", MH_HS_VALID_PLATFORM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_valid_platform$address() {
        try {
            return (MemorySegment) require("hs_valid_platform$address", MH_HS_VALID_PLATFORM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_valid_platform() {
        try {
            return (int) require("hs_valid_platform", MH_HS_VALID_PLATFORM).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_compile$descriptor", MH_HS_COMPILE_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile$handle() {
        try {
            return (MethodHandle) require("hs_compile$handle", MH_HS_COMPILE_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile$address() {
        try {
            return (MemorySegment) require("hs_compile$address", MH_HS_COMPILE_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile(MemorySegment arg0, int arg1, int arg2, MemorySegment arg3, MemorySegment arg4, MemorySegment arg5) {
        try {
            return (int) require("hs_compile", MH_HS_COMPILE).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile_multi$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_compile_multi$descriptor", MH_HS_COMPILE_MULTI_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile_multi$handle() {
        try {
            return (MethodHandle) require("hs_compile_multi$handle", MH_HS_COMPILE_MULTI_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile_multi$address() {
        try {
            return (MemorySegment) require("hs_compile_multi$address", MH_HS_COMPILE_MULTI_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile_multi(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, int arg3, int arg4, MemorySegment arg5, MemorySegment arg6, MemorySegment arg7) {
        try {
            return (int) require("hs_compile_multi", MH_HS_COMPILE_MULTI).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile_ext_multi$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_compile_ext_multi$descriptor", MH_HS_COMPILE_EXT_MULTI_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile_ext_multi$handle() {
        try {
            return (MethodHandle) require("hs_compile_ext_multi$handle", MH_HS_COMPILE_EXT_MULTI_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile_ext_multi$address() {
        try {
            return (MemorySegment) require("hs_compile_ext_multi$address", MH_HS_COMPILE_EXT_MULTI_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile_ext_multi(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, int arg4, int arg5, MemorySegment arg6, MemorySegment arg7, MemorySegment arg8) {
        try {
            return (int) require("hs_compile_ext_multi", MH_HS_COMPILE_EXT_MULTI).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile_lit$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_compile_lit$descriptor", MH_HS_COMPILE_LIT_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile_lit$handle() {
        try {
            return (MethodHandle) require("hs_compile_lit$handle", MH_HS_COMPILE_LIT_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile_lit$address() {
        try {
            return (MemorySegment) require("hs_compile_lit$address", MH_HS_COMPILE_LIT_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile_lit(MemorySegment arg0, int arg1, long arg2, int arg3, MemorySegment arg4, MemorySegment arg5, MemorySegment arg6) {
        try {
            return (int) require("hs_compile_lit", MH_HS_COMPILE_LIT).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compile_lit_multi$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_compile_lit_multi$descriptor", MH_HS_COMPILE_LIT_MULTI_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compile_lit_multi$handle() {
        try {
            return (MethodHandle) require("hs_compile_lit_multi$handle", MH_HS_COMPILE_LIT_MULTI_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compile_lit_multi$address() {
        try {
            return (MemorySegment) require("hs_compile_lit_multi$address", MH_HS_COMPILE_LIT_MULTI_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compile_lit_multi(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, int arg4, int arg5, MemorySegment arg6, MemorySegment arg7, MemorySegment arg8) {
        try {
            return (int) require("hs_compile_lit_multi", MH_HS_COMPILE_LIT_MULTI).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_free_compile_error$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_free_compile_error$descriptor", MH_HS_FREE_COMPILE_ERROR_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_free_compile_error$handle() {
        try {
            return (MethodHandle) require("hs_free_compile_error$handle", MH_HS_FREE_COMPILE_ERROR_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_free_compile_error$address() {
        try {
            return (MemorySegment) require("hs_free_compile_error$address", MH_HS_FREE_COMPILE_ERROR_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_free_compile_error(MemorySegment arg0) {
        try {
            return (int) require("hs_free_compile_error", MH_HS_FREE_COMPILE_ERROR).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_expression_info$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_expression_info$descriptor", MH_HS_EXPRESSION_INFO_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_expression_info$handle() {
        try {
            return (MethodHandle) require("hs_expression_info$handle", MH_HS_EXPRESSION_INFO_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_expression_info$address() {
        try {
            return (MemorySegment) require("hs_expression_info$address", MH_HS_EXPRESSION_INFO_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_expression_info(MemorySegment arg0, int arg1, MemorySegment arg2, MemorySegment arg3) {
        try {
            return (int) require("hs_expression_info", MH_HS_EXPRESSION_INFO).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_expression_ext_info$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_expression_ext_info$descriptor", MH_HS_EXPRESSION_EXT_INFO_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_expression_ext_info$handle() {
        try {
            return (MethodHandle) require("hs_expression_ext_info$handle", MH_HS_EXPRESSION_EXT_INFO_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_expression_ext_info$address() {
        try {
            return (MemorySegment) require("hs_expression_ext_info$address", MH_HS_EXPRESSION_EXT_INFO_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_expression_ext_info(MemorySegment arg0, int arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4) {
        try {
            return (int) require("hs_expression_ext_info", MH_HS_EXPRESSION_EXT_INFO).invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_populate_platform$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_populate_platform$descriptor", MH_HS_POPULATE_PLATFORM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_populate_platform$handle() {
        try {
            return (MethodHandle) require("hs_populate_platform$handle", MH_HS_POPULATE_PLATFORM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_populate_platform$address() {
        try {
            return (MemorySegment) require("hs_populate_platform$address", MH_HS_POPULATE_PLATFORM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_populate_platform(MemorySegment arg0) {
        try {
            return (int) require("hs_populate_platform", MH_HS_POPULATE_PLATFORM).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_open_stream$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_open_stream$descriptor", MH_HS_OPEN_STREAM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_open_stream$handle() {
        try {
            return (MethodHandle) require("hs_open_stream$handle", MH_HS_OPEN_STREAM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_open_stream$address() {
        try {
            return (MemorySegment) require("hs_open_stream$address", MH_HS_OPEN_STREAM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_open_stream(MemorySegment arg0, int arg1, MemorySegment arg2) {
        try {
            return (int) require("hs_open_stream", MH_HS_OPEN_STREAM).invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_scan_stream$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_scan_stream$descriptor", MH_HS_SCAN_STREAM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_scan_stream$handle() {
        try {
            return (MethodHandle) require("hs_scan_stream$handle", MH_HS_SCAN_STREAM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_scan_stream$address() {
        try {
            return (MemorySegment) require("hs_scan_stream$address", MH_HS_SCAN_STREAM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_scan_stream(MemorySegment arg0, MemorySegment arg1, int arg2, int arg3, MemorySegment arg4, MemorySegment arg5, MemorySegment arg6) {
        try {
            return (int) require("hs_scan_stream", MH_HS_SCAN_STREAM).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_close_stream$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_close_stream$descriptor", MH_HS_CLOSE_STREAM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_close_stream$handle() {
        try {
            return (MethodHandle) require("hs_close_stream$handle", MH_HS_CLOSE_STREAM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_close_stream$address() {
        try {
            return (MemorySegment) require("hs_close_stream$address", MH_HS_CLOSE_STREAM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_close_stream(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3) {
        try {
            return (int) require("hs_close_stream", MH_HS_CLOSE_STREAM).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_reset_stream$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_reset_stream$descriptor", MH_HS_RESET_STREAM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_reset_stream$handle() {
        try {
            return (MethodHandle) require("hs_reset_stream$handle", MH_HS_RESET_STREAM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_reset_stream$address() {
        try {
            return (MemorySegment) require("hs_reset_stream$address", MH_HS_RESET_STREAM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_reset_stream(MemorySegment arg0, int arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4) {
        try {
            return (int) require("hs_reset_stream", MH_HS_RESET_STREAM).invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_copy_stream$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_copy_stream$descriptor", MH_HS_COPY_STREAM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_copy_stream$handle() {
        try {
            return (MethodHandle) require("hs_copy_stream$handle", MH_HS_COPY_STREAM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_copy_stream$address() {
        try {
            return (MemorySegment) require("hs_copy_stream$address", MH_HS_COPY_STREAM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_copy_stream(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("hs_copy_stream", MH_HS_COPY_STREAM).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_reset_and_copy_stream$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_reset_and_copy_stream$descriptor", MH_HS_RESET_AND_COPY_STREAM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_reset_and_copy_stream$handle() {
        try {
            return (MethodHandle) require("hs_reset_and_copy_stream$handle", MH_HS_RESET_AND_COPY_STREAM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_reset_and_copy_stream$address() {
        try {
            return (MemorySegment) require("hs_reset_and_copy_stream$address", MH_HS_RESET_AND_COPY_STREAM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_reset_and_copy_stream(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, MemorySegment arg3, MemorySegment arg4) {
        try {
            return (int) require("hs_reset_and_copy_stream", MH_HS_RESET_AND_COPY_STREAM).invokeExact(arg0, arg1, arg2, arg3, arg4);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_compress_stream$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_compress_stream$descriptor", MH_HS_COMPRESS_STREAM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_compress_stream$handle() {
        try {
            return (MethodHandle) require("hs_compress_stream$handle", MH_HS_COMPRESS_STREAM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_compress_stream$address() {
        try {
            return (MemorySegment) require("hs_compress_stream$address", MH_HS_COMPRESS_STREAM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_compress_stream(MemorySegment arg0, MemorySegment arg1, long arg2, MemorySegment arg3) {
        try {
            return (int) require("hs_compress_stream", MH_HS_COMPRESS_STREAM).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_expand_stream$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_expand_stream$descriptor", MH_HS_EXPAND_STREAM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_expand_stream$handle() {
        try {
            return (MethodHandle) require("hs_expand_stream$handle", MH_HS_EXPAND_STREAM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_expand_stream$address() {
        try {
            return (MemorySegment) require("hs_expand_stream$address", MH_HS_EXPAND_STREAM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_expand_stream(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, long arg3) {
        try {
            return (int) require("hs_expand_stream", MH_HS_EXPAND_STREAM).invokeExact(arg0, arg1, arg2, arg3);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_reset_and_expand_stream$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_reset_and_expand_stream$descriptor", MH_HS_RESET_AND_EXPAND_STREAM_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_reset_and_expand_stream$handle() {
        try {
            return (MethodHandle) require("hs_reset_and_expand_stream$handle", MH_HS_RESET_AND_EXPAND_STREAM_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_reset_and_expand_stream$address() {
        try {
            return (MemorySegment) require("hs_reset_and_expand_stream$address", MH_HS_RESET_AND_EXPAND_STREAM_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_reset_and_expand_stream(MemorySegment arg0, MemorySegment arg1, long arg2, MemorySegment arg3, MemorySegment arg4, MemorySegment arg5) {
        try {
            return (int) require("hs_reset_and_expand_stream", MH_HS_RESET_AND_EXPAND_STREAM).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_scan$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_scan$descriptor", MH_HS_SCAN_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_scan$handle() {
        try {
            return (MethodHandle) require("hs_scan$handle", MH_HS_SCAN_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_scan$address() {
        try {
            return (MemorySegment) require("hs_scan$address", MH_HS_SCAN_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_scan(MemorySegment arg0, MemorySegment arg1, int arg2, int arg3, MemorySegment arg4, MemorySegment arg5, MemorySegment arg6) {
        try {
            return (int) require("hs_scan", MH_HS_SCAN).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_scan_vector$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_scan_vector$descriptor", MH_HS_SCAN_VECTOR_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_scan_vector$handle() {
        try {
            return (MethodHandle) require("hs_scan_vector$handle", MH_HS_SCAN_VECTOR_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_scan_vector$address() {
        try {
            return (MemorySegment) require("hs_scan_vector$address", MH_HS_SCAN_VECTOR_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_scan_vector(MemorySegment arg0, MemorySegment arg1, MemorySegment arg2, int arg3, int arg4, MemorySegment arg5, MemorySegment arg6, MemorySegment arg7) {
        try {
            return (int) require("hs_scan_vector", MH_HS_SCAN_VECTOR).invokeExact(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_alloc_scratch$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_alloc_scratch$descriptor", MH_HS_ALLOC_SCRATCH_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_alloc_scratch$handle() {
        try {
            return (MethodHandle) require("hs_alloc_scratch$handle", MH_HS_ALLOC_SCRATCH_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_alloc_scratch$address() {
        try {
            return (MemorySegment) require("hs_alloc_scratch$address", MH_HS_ALLOC_SCRATCH_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_alloc_scratch(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("hs_alloc_scratch", MH_HS_ALLOC_SCRATCH).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_clone_scratch$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_clone_scratch$descriptor", MH_HS_CLONE_SCRATCH_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_clone_scratch$handle() {
        try {
            return (MethodHandle) require("hs_clone_scratch$handle", MH_HS_CLONE_SCRATCH_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_clone_scratch$address() {
        try {
            return (MemorySegment) require("hs_clone_scratch$address", MH_HS_CLONE_SCRATCH_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_clone_scratch(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("hs_clone_scratch", MH_HS_CLONE_SCRATCH).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_scratch_size$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_scratch_size$descriptor", MH_HS_SCRATCH_SIZE_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_scratch_size$handle() {
        try {
            return (MethodHandle) require("hs_scratch_size$handle", MH_HS_SCRATCH_SIZE_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_scratch_size$address() {
        try {
            return (MemorySegment) require("hs_scratch_size$address", MH_HS_SCRATCH_SIZE_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_scratch_size(MemorySegment arg0, MemorySegment arg1) {
        try {
            return (int) require("hs_scratch_size", MH_HS_SCRATCH_SIZE).invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static FunctionDescriptor hs_free_scratch$descriptor() {
        try {
            return (FunctionDescriptor) require("hs_free_scratch$descriptor", MH_HS_FREE_SCRATCH_DESCRIPTOR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodHandle hs_free_scratch$handle() {
        try {
            return (MethodHandle) require("hs_free_scratch$handle", MH_HS_FREE_SCRATCH_HANDLE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment hs_free_scratch$address() {
        try {
            return (MemorySegment) require("hs_free_scratch$address", MH_HS_FREE_SCRATCH_ADDRESS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int hs_free_scratch(MemorySegment arg0) {
        try {
            return (int) require("hs_free_scratch", MH_HS_FREE_SCRATCH).invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long _POSIX_C_SOURCE() {
        try {
            return (long) require("_POSIX_C_SOURCE", MH_POSIX_C_SOURCE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __TIMESIZE() {
        try {
            return (int) require("__TIMESIZE", MH_TIMESIZE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long __STDC_IEC_60559_BFP__() {
        try {
            return (long) require("__STDC_IEC_60559_BFP__", MH_STDC_IEC_60559_BFP).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long __STDC_IEC_60559_COMPLEX__() {
        try {
            return (long) require("__STDC_IEC_60559_COMPLEX__", MH_STDC_IEC_60559_COMPLEX).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long __STDC_ISO_10646__() {
        try {
            return (long) require("__STDC_ISO_10646__", MH_STDC_ISO_10646).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment NULL() {
        try {
            return (MemorySegment) require("NULL", MH_NULL).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __WCLONE() {
        try {
            return (int) require("__WCLONE", MH_WCLONE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT16() {
        try {
            return (int) require("__HAVE_DISTINCT_FLOAT16", MH_HAVE_DISTINCT_FLOAT16).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_DISTINCT_FLOAT128X() {
        try {
            return (int) require("__HAVE_DISTINCT_FLOAT128X", MH_HAVE_DISTINCT_FLOAT128X).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __HAVE_FLOAT128_UNLIKE_LDBL() {
        try {
            return (int) require("__HAVE_FLOAT128_UNLIKE_LDBL", MH_HAVE_FLOAT128_UNLIKE_LDBL).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __BYTE_ORDER() {
        try {
            return (int) require("__BYTE_ORDER", MH_BYTE_ORDER).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __FLOAT_WORD_ORDER() {
        try {
            return (int) require("__FLOAT_WORD_ORDER", MH_FLOAT_WORD_ORDER).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int LITTLE_ENDIAN() {
        try {
            return (int) require("LITTLE_ENDIAN", MH_LITTLE_ENDIAN_1).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int BIG_ENDIAN() {
        try {
            return (int) require("BIG_ENDIAN", MH_BIG_ENDIAN_1).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int PDP_ENDIAN() {
        try {
            return (int) require("PDP_ENDIAN", MH_PDP_ENDIAN_1).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int BYTE_ORDER() {
        try {
            return (int) require("BYTE_ORDER", MH_BYTE_ORDER_1).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long _SIGSET_NWORDS() {
        try {
            return (long) require("_SIGSET_NWORDS", MH_SIGSET_NWORDS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __NFDBITS() {
        try {
            return (int) require("__NFDBITS", MH_NFDBITS).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int FD_SETSIZE() {
        try {
            return (int) require("FD_SETSIZE", MH_FD_SETSIZE_1).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int NFDBITS() {
        try {
            return (int) require("NFDBITS", MH_NFDBITS_1).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int __PTHREAD_RWLOCK_ELISION_EXTRA() {
        try {
            return (int) require("__PTHREAD_RWLOCK_ELISION_EXTRA", MH_PTHREAD_RWLOCK_ELISION_EXTRA).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_INVALID() {
        try {
            return (int) require("HS_INVALID", MH_HS_INVALID).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_NOMEM() {
        try {
            return (int) require("HS_NOMEM", MH_HS_NOMEM).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_SCAN_TERMINATED() {
        try {
            return (int) require("HS_SCAN_TERMINATED", MH_HS_SCAN_TERMINATED).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_COMPILER_ERROR() {
        try {
            return (int) require("HS_COMPILER_ERROR", MH_HS_COMPILER_ERROR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_DB_VERSION_ERROR() {
        try {
            return (int) require("HS_DB_VERSION_ERROR", MH_HS_DB_VERSION_ERROR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_DB_PLATFORM_ERROR() {
        try {
            return (int) require("HS_DB_PLATFORM_ERROR", MH_HS_DB_PLATFORM_ERROR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_DB_MODE_ERROR() {
        try {
            return (int) require("HS_DB_MODE_ERROR", MH_HS_DB_MODE_ERROR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_BAD_ALIGN() {
        try {
            return (int) require("HS_BAD_ALIGN", MH_HS_BAD_ALIGN).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_BAD_ALLOC() {
        try {
            return (int) require("HS_BAD_ALLOC", MH_HS_BAD_ALLOC).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_SCRATCH_IN_USE() {
        try {
            return (int) require("HS_SCRATCH_IN_USE", MH_HS_SCRATCH_IN_USE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_ARCH_ERROR() {
        try {
            return (int) require("HS_ARCH_ERROR", MH_HS_ARCH_ERROR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_INSUFFICIENT_SPACE() {
        try {
            return (int) require("HS_INSUFFICIENT_SPACE", MH_HS_INSUFFICIENT_SPACE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_UNKNOWN_ERROR() {
        try {
            return (int) require("HS_UNKNOWN_ERROR", MH_HS_UNKNOWN_ERROR).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_MIN_OFFSET() {
        try {
            return (long) require("HS_EXT_FLAG_MIN_OFFSET", MH_HS_EXT_FLAG_MIN_OFFSET).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_MAX_OFFSET() {
        try {
            return (long) require("HS_EXT_FLAG_MAX_OFFSET", MH_HS_EXT_FLAG_MAX_OFFSET).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_MIN_LENGTH() {
        try {
            return (long) require("HS_EXT_FLAG_MIN_LENGTH", MH_HS_EXT_FLAG_MIN_LENGTH).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_EDIT_DISTANCE() {
        try {
            return (long) require("HS_EXT_FLAG_EDIT_DISTANCE", MH_HS_EXT_FLAG_EDIT_DISTANCE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_EXT_FLAG_HAMMING_DISTANCE() {
        try {
            return (long) require("HS_EXT_FLAG_HAMMING_DISTANCE", MH_HS_EXT_FLAG_HAMMING_DISTANCE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_CPU_FEATURES_AVX2() {
        try {
            return (long) require("HS_CPU_FEATURES_AVX2", MH_HS_CPU_FEATURES_AVX2).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_CPU_FEATURES_AVX512() {
        try {
            return (long) require("HS_CPU_FEATURES_AVX512", MH_HS_CPU_FEATURES_AVX512).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_CPU_FEATURES_AVX512VBMI() {
        try {
            return (long) require("HS_CPU_FEATURES_AVX512VBMI", MH_HS_CPU_FEATURES_AVX512VBMI).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_SOM_HORIZON_LARGE() {
        try {
            return (int) require("HS_MODE_SOM_HORIZON_LARGE", MH_HS_MODE_SOM_HORIZON_LARGE).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_SOM_HORIZON_MEDIUM() {
        try {
            return (int) require("HS_MODE_SOM_HORIZON_MEDIUM", MH_HS_MODE_SOM_HORIZON_MEDIUM).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_MODE_SOM_HORIZON_SMALL() {
        try {
            return (int) require("HS_MODE_SOM_HORIZON_SMALL", MH_HS_MODE_SOM_HORIZON_SMALL).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static long HS_OFFSET_PAST_HORIZON() {
        try {
            return (long) require("HS_OFFSET_PAST_HORIZON", MH_HS_OFFSET_PAST_HORIZON).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static MemorySegment HS_VERSION_STRING() {
        try {
            return (MemorySegment) require("HS_VERSION_STRING", MH_HS_VERSION_STRING).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static int HS_VERSION_32BIT() {
        try {
            return (int) require("HS_VERSION_32BIT", MH_HS_VERSION_32BIT).invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}