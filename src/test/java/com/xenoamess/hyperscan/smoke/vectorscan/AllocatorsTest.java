package com.xenoamess.hyperscan.smoke.vectorscan;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualCompileResult;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualMode;
import com.xenoamess.hyperscan.smoke.dual.DualResult;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import com.xenoamess.hyperscan.smoke.dual.DualScratchResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
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
    private static final AtomicLong COUNT_ALLOC_CALLS = new AtomicLong();
    private static final AtomicLong COUNT_FREE_CALLS = new AtomicLong();
    private static final AtomicLong COUNT_B = new AtomicLong();
    private static final ConcurrentHashMap<Long, Long> COUNT_SIZES = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Long> COUNT_B_SIZES = new ConcurrentHashMap<>();

    private static final DualApi.DualAllocator COUNT_MALLOC = size -> {
        long address = UNSAFE.allocateMemory(size);
        COUNT_ALLOC_CALLS.incrementAndGet();
        COUNT.addAndGet(size);
        COUNT_SIZES.put(address, size);
        return address;
    };

    private static final DualApi.DualFree COUNT_FREE = address -> {
        Long size = COUNT_SIZES.remove(address);
        if (size != null) {
            COUNT.addAndGet(-size);
            COUNT_FREE_CALLS.incrementAndGet();
            UNSAFE.freeMemory(address);
        }
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

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void miscOutputsUseConfiguredFree(DualApi api) {
        assertThat(COUNT_SIZES).isEmpty();
        COUNT.set(0);
        COUNT_ALLOC_CALLS.set(0);
        COUNT_FREE_CALLS.set(0);
        DualDatabase database = buildDatabase(api);
        api.setMiscAllocator(COUNT_MALLOC, COUNT_FREE);
        try {
            DualResult<byte[]> serialized = api.serializeRaw(database);
            assertThat(serialized.code()).isEqualTo(api.success());
            assertThat(serialized.value()).isNotEmpty();
            assertThat(api.getDatabaseInfoRaw(database).code()).isEqualTo(api.success());
            assertThat(api.getSerializedDatabaseInfoRaw(serialized.value()).code()).isEqualTo(api.success());
            assertThat(api.expressionInfoDataRaw("foobar", EnumSet.noneOf(DualExpressionFlag.class)).code())
                    .isEqualTo(api.success());
            assertThat(COUNT_ALLOC_CALLS.get()).isPositive();
            assertThat(COUNT_FREE_CALLS.get()).isEqualTo(COUNT_ALLOC_CALLS.get());
            assertThat(COUNT.get()).isZero();
            assertThat(COUNT_SIZES).isEmpty();
        } finally {
            api.resetAllocators();
            database.close();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void nullErrorOutputsNeverFreeForeignMemory(DualApi api) {
        ConcurrentHashMap<Long, Long> allocations = new ConcurrentHashMap<>();
        AtomicLong foreignFrees = new AtomicLong();
        DualApi.DualAllocator allocator = size -> {
            long address = UNSAFE.allocateMemory(size);
            allocations.put(address, size);
            return address;
        };
        DualApi.DualFree free = address -> {
            if (allocations.remove(address) == null) {
                foreignFrees.incrementAndGet();
            } else {
                UNSAFE.freeMemory(address);
            }
        };

        api.setMiscAllocator(allocator, free);
        try {
            assertThat(api.expressionInfoNullErrRaw(
                    "foobar", EnumSet.noneOf(DualExpressionFlag.class)).code())
                    .isEqualTo(api.compilerError());
            assertThat(api.expressionExtInfoNullErrRaw(
                    "foobar", EnumSet.noneOf(DualExpressionFlag.class)).code())
                    .isEqualTo(api.compilerError());
            assertThat(foreignFrees).hasValue(0);
            assertThat(allocations).isEmpty();
        } finally {
            api.resetAllocators();
            allocations.forEach((address, size) -> UNSAFE.freeMemory(address));
            allocations.clear();
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void vectoredScanReusesScannerScratch(DualApi api) {
        assertThat(COUNT_SIZES).isEmpty();
        COUNT.set(0);
        COUNT_ALLOC_CALLS.set(0);
        COUNT_FREE_CALLS.set(0);

        DualDatabase database = api.compileDatabase(api.createExpression("foobar"), DualMode.VECTORED);
        api.setScratchAllocator(COUNT_MALLOC, COUNT_FREE);
        try {
            DualScanner scanner = api.createScanner();
            try {
                api.scanVector(scanner, database,
                        new byte[][]{"foobar".getBytes(StandardCharsets.UTF_8)}, null);
                long allocationsAfterFirstScan = COUNT_ALLOC_CALLS.get();
                long freesAfterFirstScan = COUNT_FREE_CALLS.get();
                for (int i = 1; i < 100; i++) {
                    api.scanVector(scanner, database,
                            new byte[][]{"foobar".getBytes(StandardCharsets.UTF_8)}, null);
                }
                assertThat(allocationsAfterFirstScan).isPositive();
                assertThat(COUNT_ALLOC_CALLS.get()).isEqualTo(allocationsAfterFirstScan);
                assertThat(COUNT_FREE_CALLS.get()).isEqualTo(freesAfterFirstScan);
                assertThat(COUNT.get()).isPositive();
            } finally {
                scanner.close();
            }
            assertThat(COUNT_ALLOC_CALLS.get()).isEqualTo(COUNT_FREE_CALLS.get());
            assertThat(COUNT.get()).isZero();
            assertThat(COUNT_SIZES).isEmpty();
        } finally {
            api.resetAllocators();
            database.close();
        }
    }
}
