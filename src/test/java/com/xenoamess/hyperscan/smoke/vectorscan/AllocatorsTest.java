package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualResult;
import com.xenoamess.hyperscan.smoke.dual.DualScratchResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

class AllocatorsTest {

    private static final Unsafe UNSAFE = getUnsafe();

    private static final ConcurrentHashMap<Long, Long> TWO_ALIGNED_BASES = new ConcurrentHashMap<>();

    private static final DualApi.DualAllocator NULL_MALLOC = size -> 0L;

    private static final DualApi.DualAllocator TWO_ALIGNED_MALLOC = size -> {
        long base = UNSAFE.allocateMemory(size + 2);
        long misaligned = base + 2;
        TWO_ALIGNED_BASES.put(misaligned, base);
        return misaligned;
    };

    private static final DualApi.DualFree TWO_ALIGNED_FREE = address -> {
        Long base = TWO_ALIGNED_BASES.remove(address);
        if (base != null) {
            UNSAFE.freeMemory(base);
        }
    };

    private static final AtomicLong COUNT = new AtomicLong();
    private static final AtomicLong COUNT_B = new AtomicLong();
    private static final ConcurrentHashMap<Long, Long> COUNT_SIZES = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Long> COUNT_B_SIZES = new ConcurrentHashMap<>();

    private static final DualApi.DualAllocator COUNT_MALLOC = size -> {
        long address = UNSAFE.allocateMemory(size);
        COUNT.addAndGet(size);
        COUNT_SIZES.put(address, size);
        return address;
    };

    private static final DualApi.DualFree COUNT_FREE = address -> {
        Long size = COUNT_SIZES.remove(address);
        if (size != null) {
            COUNT.addAndGet(-size);
        }
        UNSAFE.freeMemory(address);
    };

    private static final DualApi.DualAllocator COUNT_B_MALLOC = size -> {
        long address = UNSAFE.allocateMemory(size);
        COUNT_B.addAndGet(size);
        COUNT_B_SIZES.put(address, size);
        return address;
    };

    private static final DualApi.DualFree COUNT_B_FREE = address -> {
        Long size = COUNT_B_SIZES.remove(address);
        if (size != null) {
            COUNT_B.addAndGet(-size);
        }
        UNSAFE.freeMemory(address);
    };

    private static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DualDatabase buildDatabase(DualApi api) {
        DualCompileResult result = api.compileRaw("foobar", 0, api.modeBlock());
        assertThat(result.code()).isEqualTo(api.success());
        assertThat(result.database()).isNotNull();
        return result.database();
    }

    private byte[] serializeDatabase(DualApi api, DualDatabase database) {
        return api.serialize(database);
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void databaseInfoBadAlloc(DualApi api) {
        DualDatabase db = buildDatabase(api);
        try {
            api.setAllocator(NULL_MALLOC, null);
            try {
                DualResult<String> result = api.getDatabaseInfoRaw(db);
                assertThat(result.code()).isEqualTo(api.noMem());
            } finally {
                api.resetAllocators();
            }
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void twoAlignedCompile(DualApi api) {
        api.setDatabaseAllocator(TWO_ALIGNED_MALLOC, TWO_ALIGNED_FREE);
        try {
            DualCompileResult result = api.compileRaw("foobar", 0, api.modeBlock());
            assertThat(result.code()).isEqualTo(api.compilerError());
            assertThat(result.database()).isNull();
            assertThat(result.message()).isNotNull();
        } finally {
            api.resetAllocators();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void twoAlignedCompileError(DualApi api) {
        api.setMiscAllocator(TWO_ALIGNED_MALLOC, TWO_ALIGNED_FREE);
        try {
            DualCompileResult result = api.compileRaw("\\1", 0, api.modeBlock());
            assertThat(result.code()).isEqualTo(api.compilerError());
            assertThat(result.database()).isNull();
            assertThat(result.message()).contains("Allocator returned misaligned memory.");
        } finally {
            api.resetAllocators();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void twoAlignedDatabaseInfo(DualApi api) {
        DualDatabase db = buildDatabase(api);
        try {
            api.setMiscAllocator(TWO_ALIGNED_MALLOC, TWO_ALIGNED_FREE);
            try {
                DualResult<String> result = api.getDatabaseInfoRaw(db);
                assertThat(result.code()).isEqualTo(api.badAlloc());
            } finally {
                api.resetAllocators();
            }
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void twoAlignedSerialize(DualApi api) {
        DualDatabase db = buildDatabase(api);
        try {
            api.setMiscAllocator(TWO_ALIGNED_MALLOC, TWO_ALIGNED_FREE);
            try {
                DualResult<byte[]> result = api.serializeRaw(db);
                assertThat(result.code()).isEqualTo(api.badAlloc());
            } finally {
                api.resetAllocators();
            }
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void twoAlignedDeserialize(DualApi api) {
        DualDatabase db = buildDatabase(api);
        try {
            byte[] bytes = serializeDatabase(api, db);
            api.closeDatabase(db);
            db = null;

            api.setDatabaseAllocator(TWO_ALIGNED_MALLOC, TWO_ALIGNED_FREE);
            try {
                DualResult<DualDatabase> result = api.deserializeRaw(bytes);
                assertThat(result.code()).isEqualTo(api.badAlloc());
                assertThat(result.value()).isNull();
            } finally {
                api.resetAllocators();
            }
        } finally {
            if (db != null) {
                api.closeDatabase(db);
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void twoAlignedAllocScratch(DualApi api) {
        DualDatabase db = buildDatabase(api);
        try {
            api.setScratchAllocator(TWO_ALIGNED_MALLOC, TWO_ALIGNED_FREE);
            try {
                DualScratchResult result = api.allocScratchRaw(db);
                assertThat(result.code()).isEqualTo(api.badAlloc());
                assertThat(result.scratch()).isNull();
            } finally {
                api.resetAllocators();
            }
        } finally {
            api.closeDatabase(db);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void nullMallocExpressionInfo(DualApi api) {
        api.setAllocator(NULL_MALLOC, null);
        try {
            DualResult<String> result = api.expressionInfoRaw("foobar", EnumSet.noneOf(DualExpressionFlag.class));
            assertThat(result.code()).isEqualTo(api.compilerError());
            assertThat(result.message()).isNotNull();
        } finally {
            api.resetAllocators();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void twoAlignedExpressionInfo(DualApi api) {
        api.setMiscAllocator(TWO_ALIGNED_MALLOC, TWO_ALIGNED_FREE);
        try {
            DualResult<String> result = api.expressionInfoRaw("\\1", EnumSet.noneOf(DualExpressionFlag.class));
            assertThat(result.code()).isEqualTo(api.compilerError());
            assertThat(result.message()).contains("Allocator returned misaligned memory.");
        } finally {
            api.resetAllocators();
        }
    }
}
