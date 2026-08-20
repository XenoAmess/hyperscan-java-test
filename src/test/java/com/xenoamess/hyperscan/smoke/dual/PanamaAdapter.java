package com.xenoamess.hyperscan.smoke.dual;

import com.xenoamess.hyperscan_panama.jni.HyperscanJni;
import com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader;
import com.xenoamess.hyperscan_panama.jni.generated.hyperscan;
import com.xenoamess.hyperscan_panama.jni.generated.hs_alloc_t;
import com.xenoamess.hyperscan_panama.jni.generated.hs_compile_error;
import com.xenoamess.hyperscan_panama.jni.generated.hs_expr_ext;
import com.xenoamess.hyperscan_panama.jni.generated.hs_expr_info;
import com.xenoamess.hyperscan_panama.jni.generated.hs_free_t;
import com.xenoamess.hyperscan_panama.jni.generated.hs_platform_info;
import com.xenoamess.hyperscan_panama.util.PatternFilter;
import com.xenoamess.hyperscan_panama.wrapper.ByteMatchEventHandler;
import com.xenoamess.hyperscan_panama.wrapper.CompileErrorException;
import com.xenoamess.hyperscan_panama.wrapper.Expression;
import com.xenoamess.hyperscan_panama.wrapper.ExpressionFlag;
import com.xenoamess.hyperscan_panama.wrapper.Match;
import com.xenoamess.hyperscan_panama.wrapper.Scanner;
import com.xenoamess.hyperscan_panama.wrapper.StringMatchEventHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.misc.Unsafe;

public class PanamaAdapter implements DualApi {

    private static final Unsafe UNSAFE = getUnsafe();

    static {
        String requestedPlatform = System.getProperty("javacpp.platform");
        if (requestedPlatform != null && !requestedPlatform.isBlank()
                && System.getProperty("com.xenoamess.hyperscan_panama.platform") == null) {
            System.setProperty("com.xenoamess.hyperscan_panama.platform", requestedPlatform);
        }
        HyperscanNativeLoader.load();
    }

    private static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final Arena HS_LIBRARY_ARENA = Arena.global();
    private static final ThreadLocal<ByteBuffer> STREAM_BUFFER =
            ThreadLocal.withInitial(() -> ByteBuffer.allocateDirect(0));
    private static final IdentityHashMap<Object, Integer> DATABASE_STREAM_LEASES = new IdentityHashMap<>();
    private static final IdentityHashMap<Object, Integer> DATABASE_OPERATION_LEASES = new IdentityHashMap<>();
    private static final Object ALLOCATOR_LOCK = new Object();
    private static final SymbolLookup HS_LIBRARY_LOOKUP;

    private static final ThreadLocal<DirectBufferCache> DIRECT_BUFFER_CACHE = ThreadLocal.withInitial(DirectBufferCache::new);

    private static final class DirectBufferCache {
        ByteBuffer buffer;
        int position = -1;
        int limit = -1;
        MemorySegment segment = MemorySegment.NULL;
    }

    private static MemorySegment directBufferSegment(ByteBuffer input) {
        DirectBufferCache cache = DIRECT_BUFFER_CACHE.get();
        if (cache.buffer == input && cache.position == input.position() && cache.limit == input.limit()) {
            return cache.segment;
        }
        MemorySegment segment = MemorySegment.ofBuffer(input).asSlice(input.position(), input.remaining());
        cache.buffer = input;
        cache.position = input.position();
        cache.limit = input.limit();
        cache.segment = segment;
        return segment;
    }

    static {
        try {
            HS_LIBRARY_LOOKUP = SymbolLookup.libraryLookup(findHsLibraryPath(), HS_LIBRARY_ARENA);
            patchHyperscanSymbolLookup();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static void patchHyperscanSymbolLookup() {
        try {
            String platform = panamaPlatform();
            String family = com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader.selectPlatformFamily(platform);
            String delegatePackage = "com.xenoamess.hyperscan_panama.jni." + family.replace('-', '_') + ".generated";
            Class<?> facadeClazz = Class.forName("com.xenoamess.hyperscan_panama.jni.generated.hyperscan");
            Class<?> delegateClazz = Class.forName(delegatePackage + ".hyperscan");
            SymbolLookup patched = HS_LIBRARY_LOOKUP.or(Linker.nativeLinker().defaultLookup());
            patchSymbolLookup(facadeClazz, patched);
            patchSymbolLookup(delegateClazz, patched);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static void patchSymbolLookup(Class<?> clazz, SymbolLookup lookup) throws Exception {
        Field field = clazz.getDeclaredField("SYMBOL_LOOKUP");
        field.setAccessible(true);
        java.lang.reflect.Method baseMethod = UNSAFE.getClass().getDeclaredMethod("staticFieldBase", Field.class);
        baseMethod.setAccessible(true);
        Object base = baseMethod.invoke(UNSAFE, field);
        java.lang.reflect.Method offsetMethod = UNSAFE.getClass().getDeclaredMethod("staticFieldOffset", Field.class);
        offsetMethod.setAccessible(true);
        long offset = (Long) offsetMethod.invoke(UNSAFE, field);
        UNSAFE.putObject(base, offset, lookup);
    }

    private static String findHsLibraryPath() {
        String platform = panamaPlatform();
        String libraryName = System.mapLibraryName("hs");
        String resource = "com/xenoamess/hyperscan_panama/jni/" + platform + "/" + libraryName;
        try (InputStream is = PanamaAdapter.class.getClassLoader().getResourceAsStream(resource)) {
            if (is == null) {
                throw new RuntimeException("Native library not found on classpath: " + resource);
            }
            Path tempDir = Files.createTempDirectory("hyperscan-panama-adapter-");
            tempDir.toFile().deleteOnExit();
            Path libFile = tempDir.resolve(libraryName);
            Files.copy(is, libFile, StandardCopyOption.REPLACE_EXISTING);
            libFile.toFile().deleteOnExit();
            return libFile.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String panamaPlatform() {
        String platform = System.getProperty("com.xenoamess.hyperscan_panama.platform");
        if (platform == null || platform.isEmpty()) {
            platform = HyperscanNativeLoader.selectPlatform();
        }
        return platform;
    }


    private static Arena allocatorArena;
    private static MemorySegment currentAllocator;
    private static MemorySegment currentFree;
    private static MemorySegment currentDatabaseAllocator;
    private static MemorySegment currentDatabaseFree;
    private static MemorySegment currentMiscAllocator;
    private static MemorySegment currentMiscFree;
    private static MemorySegment currentScratchAllocator;
    private static MemorySegment currentScratchFree;
    private static MemorySegment currentStreamAllocator;
    private static MemorySegment currentStreamFree;
    private static DualFree effectiveMiscFree;

    private static synchronized Arena ensureAllocatorArena() {
        if (allocatorArena == null || !allocatorArena.scope().isAlive()) {
            allocatorArena = Arena.ofShared();
        }
        return allocatorArena;
    }

    private static MemorySegment wrapAllocator(DualAllocator alloc, Arena arena) {
        if (alloc == null) {
            return MemorySegment.NULL;
        }
        return hs_alloc_t.allocate(size -> {
            try {
                long address = alloc.allocate(size);
                if (address == 0) {
                    return MemorySegment.NULL;
                }
                return MemorySegment.ofAddress(address).reinterpret(size);
            } catch (Throwable ignored) {
                return MemorySegment.NULL;
            }
        }, arena);
    }

    private static MemorySegment wrapFree(DualFree free, Arena arena) {
        if (free == null) {
            return MemorySegment.NULL;
        }
        return hs_free_t.allocate(ptr -> {
            try {
                if (ptr != null && ptr.address() != 0) {
                    free.free(ptr.address());
                }
            } catch (Throwable ignored) {
                // Native free callbacks must not throw across the upcall boundary.
            }
        }, arena);
    }

    private static void setHsLibraryAllocator(String name, MemorySegment alloc, MemorySegment free) {
        try {
            MemorySegment symbol = HS_LIBRARY_LOOKUP.find(name).orElseThrow(() -> new RuntimeException("Symbol not found: " + name));
            FunctionDescriptor descriptor = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS);
            MethodHandle handle = Linker.nativeLinker().downcallHandle(symbol, descriptor);
            int result = (int) handle.invokeExact(alloc, free);
            if (result != 0) {
                throw new RuntimeException(name + " failed: " + result);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static void freeMiscSegment(MemorySegment segment) {
        if (segment == null || segment.address() == 0) {
            return;
        }
        DualFree free = effectiveMiscFree;
        if (free != null) {
            free.free(segment.address());
        } else {
            HYPERSCAN_JNI.free(segment);
        }
    }

    private static Object databaseLeaseKey(DualDatabase database) {
        return database instanceof PanamaRawDatabase raw ? raw.state : database;
    }

    private static void acquireDatabaseStreamLease(DualDatabase database) {
        synchronized (DATABASE_STREAM_LEASES) {
            Object key = databaseLeaseKey(database);
            DATABASE_STREAM_LEASES.put(key, DATABASE_STREAM_LEASES.getOrDefault(key, 0) + 1);
        }
    }

    private static void releaseDatabaseStreamLease(DualDatabase database) {
        synchronized (DATABASE_STREAM_LEASES) {
            Object key = databaseLeaseKey(database);
            Integer count = DATABASE_STREAM_LEASES.get(key);
            if (count == null || count == 0) {
                throw new IllegalStateException("Database stream lease is not held");
            }
            if (count == 1) {
                DATABASE_STREAM_LEASES.remove(key);
            } else {
                DATABASE_STREAM_LEASES.put(key, count - 1);
            }
        }
    }

    private static void acquireDatabaseOperationLease(DualDatabase database) {
        synchronized (DATABASE_STREAM_LEASES) {
            Object key = databaseLeaseKey(database);
            DATABASE_OPERATION_LEASES.put(
                    key, DATABASE_OPERATION_LEASES.getOrDefault(key, 0) + 1);
        }
    }

    private static void releaseDatabaseOperationLease(DualDatabase database) {
        synchronized (DATABASE_STREAM_LEASES) {
            Object key = databaseLeaseKey(database);
            Integer count = DATABASE_OPERATION_LEASES.get(key);
            if (count == null || count == 0) {
                throw new IllegalStateException("Database operation lease is not held");
            }
            if (count == 1) {
                DATABASE_OPERATION_LEASES.remove(key);
            } else {
                DATABASE_OPERATION_LEASES.put(key, count - 1);
            }
        }
    }

    private static PanamaDatabaseOperation acquireDatabaseOperation(DualDatabase database) {
        acquireDatabaseOperationLease(database);
        try {
            return new PanamaDatabaseOperation(database, nativeDatabase(database));
        } catch (RuntimeException | Error e) {
            releaseDatabaseOperationLease(database);
            throw e;
        }
    }

    private static final class PanamaDatabaseOperation implements AutoCloseable {
        final DualDatabase owner;
        final MemorySegment database;

        PanamaDatabaseOperation(DualDatabase owner, MemorySegment database) {
            this.owner = owner;
            this.database = database;
        }

        @Override
        public void close() {
            releaseDatabaseOperationLease(owner);
        }
    }

    private static int freeDatabaseWhenUnused(DualDatabase database, IntSupplier free) {
        synchronized (DATABASE_STREAM_LEASES) {
            if (DATABASE_STREAM_LEASES.getOrDefault(databaseLeaseKey(database), 0) != 0) {
                throw new IllegalStateException("Database is in use by an open stream");
            }
            if (DATABASE_OPERATION_LEASES.getOrDefault(databaseLeaseKey(database), 0) != 0) {
                throw new IllegalStateException("Database is in use by an active operation");
            }
            return free.getAsInt();
        }
    }

    private static void closeDatabaseWhenUnused(DualDatabase database, Runnable close) {
        freeDatabaseWhenUnused(database, () -> {
            close.run();
            return hyperscan.HS_SUCCESS();
        });
    }

    private static MemorySegment reinterpretHandle(MemorySegment segment) {
        if (segment == null || segment.address() == 0) {
            return MemorySegment.NULL;
        }
        return segment.reinterpret(8, Arena.global(), null);
    }

    private static MemorySegment reinterpretCompileError(MemorySegment segment) {
        if (segment == null || segment.address() == 0) {
            return MemorySegment.NULL;
        }
        return segment.reinterpret(hs_compile_error.sizeof(), Arena.global(), null);
    }

    private static MemorySegment reinterpretExprInfo(MemorySegment segment) {
        if (segment == null || segment.address() == 0) {
            return MemorySegment.NULL;
        }
        return segment.reinterpret(hs_expr_info.sizeof(), Arena.global(), null);
    }

    private static MemorySegment reinterpretString(MemorySegment segment) {
        if (segment == null || segment.address() == 0) {
            return MemorySegment.NULL;
        }
        return segment.reinterpret(65536, Arena.global(), null);
    }

    private static MemorySegment reinterpretBuffer(MemorySegment segment, long size) {
        if (segment == null || segment.address() == 0) {
            return MemorySegment.NULL;
        }
        return segment.reinterpret(size, Arena.global(), null);
    }

    private static MemorySegment allocateBytes(Arena arena, byte[] data) {
        if (data == null) {
            return MemorySegment.NULL;
        }
        int size = data.length == 0 ? 1 : data.length;
        MemorySegment segment = arena.allocate(size, 16);
        if (data.length > 0) {
            MemorySegment.copy(MemorySegment.ofArray(data), 0, segment, 0, data.length);
        }
        return segment;
    }

    private static MemorySegment zeroAddressOut(Arena arena) {
        MemorySegment seg = arena.allocate(ValueLayout.ADDRESS);
        seg.set(ValueLayout.ADDRESS, 0, MemorySegment.NULL);
        return seg;
    }

    private static MemorySegment zeroLongOut(Arena arena) {
        MemorySegment seg = arena.allocate(ValueLayout.JAVA_LONG);
        seg.set(ValueLayout.JAVA_LONG, 0, 0L);
        return seg;
    }

    private static final HyperscanJni HYPERSCAN_JNI = HyperscanNativeLoader.loadJni();

    private static final ThreadLocal<HandlerContext> STREAM_CALLBACK = new ThreadLocal<>();

    private static final MemorySegment MATCH_HANDLER = HYPERSCAN_JNI.allocateMatchEventHandler(
            (id, from, to, flags) -> {
                HandlerContext ctx = STREAM_CALLBACK.get();
                try {
                    if (ctx == null || ctx.handler == null) {
                        return 0;
                    }
                    DualExpression[] byId = ctx.expressionsById;
                    DualExpression expression = id >= 0 && id < byId.length ? byId[id] : null;
                    if (expression == null) {
                        expression = new DualExpression("", EnumSet.noneOf(DualExpressionFlag.class), id);
                    }
                    return ctx.handler.onMatch(expression, from, to) ? 0 : -1;
                } catch (Throwable failure) {
                    if (ctx != null && ctx.failure == null) {
                        ctx.failure = failure;
                    }
                    return -1;
                }
            },
            HS_LIBRARY_ARENA
    );

    private static final class HandlerContext {
        final DualByteMatchHandler handler;
        final DualExpression[] expressionsById;
        final HandlerContext previous;
        Throwable failure;

        HandlerContext(DualByteMatchHandler handler, DualExpression[] expressionsById,
                       HandlerContext previous) {
            this.handler = handler;
            this.expressionsById = expressionsById;
            this.previous = previous;
        }
    }

    private static final class AdapterByteMatchHandler implements ByteMatchEventHandler {
        final DualByteMatchHandler handler;
        Throwable failure;

        AdapterByteMatchHandler(DualByteMatchHandler handler) {
            this.handler = handler;
        }

        @Override
        public boolean onMatch(Expression expression, long from, long to) {
            try {
                return handler.onMatch(toDualExpression(expression), from, to);
            } catch (Throwable throwable) {
                failure = throwable;
                return false;
            }
        }

        static AdapterByteMatchHandler bind(DualByteMatchHandler handler) {
            return new AdapterByteMatchHandler(handler);
        }

        void propagateFailure() {
            if (failure != null) {
                throwUnchecked(failure);
            }
        }
    }

    private static final class AdapterStringMatchHandler implements StringMatchEventHandler {
        final DualStringMatchHandler handler;
        Throwable failure;

        AdapterStringMatchHandler(DualStringMatchHandler handler) {
            this.handler = handler;
        }

        @Override
        public boolean onMatch(Expression expression, long from, long to) {
            try {
                return handler.onMatch(toDualExpression(expression), from, to);
            } catch (Throwable throwable) {
                failure = throwable;
                return false;
            }
        }

        static AdapterStringMatchHandler bind(DualStringMatchHandler handler) {
            return new AdapterStringMatchHandler(handler);
        }

        void propagateFailure() {
            if (failure != null) {
                throwUnchecked(failure);
            }
        }
    }

    private static DualExpression[] buildExpressionLookup(List<DualExpression> expressions) {
        int maxId = -1;
        for (DualExpression expr : expressions) {
            int id = expr.id() == null ? 0 : expr.id();
            if (id > maxId) {
                maxId = id;
            }
        }
        DualExpression[] byId = new DualExpression[maxId + 1];
        for (DualExpression expr : expressions) {
            int id = expr.id() == null ? 0 : expr.id();
            if (id >= 0) {
                byId[id] = expr;
            }
        }
        return byId;
    }

    private static void setHandlerContext(DualByteMatchHandler handler, DualExpression[] expressionsById) {
        STREAM_CALLBACK.set(new HandlerContext(handler, expressionsById, STREAM_CALLBACK.get()));
    }

    private static int propagateHandlerFailure(int result) {
        HandlerContext ctx = STREAM_CALLBACK.get();
        if (ctx != null && ctx.failure != null) {
            throwUnchecked(ctx.failure);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwUnchecked(Throwable failure) throws E {
        throw (E) failure;
    }

    private static void clearHandlerContext() {
        HandlerContext ctx = STREAM_CALLBACK.get();
        if (ctx != null && ctx.previous != null) {
            STREAM_CALLBACK.set(ctx.previous);
        } else {
            STREAM_CALLBACK.remove();
        }
    }

    @Override
    public void setAllocator(DualAllocator alloc, DualFree free) {
        synchronized (ALLOCATOR_LOCK) {
            Arena arena = ensureAllocatorArena();
            currentAllocator = wrapAllocator(alloc, arena);
            currentFree = wrapFree(free, arena);
            checkResult(hyperscan.hs_set_allocator(currentAllocator, currentFree));
            setHsLibraryAllocator("hs_set_allocator", currentAllocator, currentFree);
            effectiveMiscFree = free;
        }
    }

    @Override
    public void setDatabaseAllocator(DualAllocator alloc, DualFree free) {
        synchronized (ALLOCATOR_LOCK) {
            Arena arena = ensureAllocatorArena();
            currentDatabaseAllocator = wrapAllocator(alloc, arena);
            currentDatabaseFree = wrapFree(free, arena);
            checkResult(hyperscan.hs_set_database_allocator(currentDatabaseAllocator, currentDatabaseFree));
            setHsLibraryAllocator("hs_set_database_allocator", currentDatabaseAllocator, currentDatabaseFree);
        }
    }

    @Override
    public void setMiscAllocator(DualAllocator alloc, DualFree free) {
        synchronized (ALLOCATOR_LOCK) {
            Arena arena = ensureAllocatorArena();
            currentMiscAllocator = wrapAllocator(alloc, arena);
            currentMiscFree = wrapFree(free, arena);
            checkResult(hyperscan.hs_set_misc_allocator(currentMiscAllocator, currentMiscFree));
            setHsLibraryAllocator("hs_set_misc_allocator", currentMiscAllocator, currentMiscFree);
            effectiveMiscFree = free;
        }
    }

    @Override
    public void setScratchAllocator(DualAllocator alloc, DualFree free) {
        synchronized (ALLOCATOR_LOCK) {
            Arena arena = ensureAllocatorArena();
            currentScratchAllocator = wrapAllocator(alloc, arena);
            currentScratchFree = wrapFree(free, arena);
            checkResult(hyperscan.hs_set_scratch_allocator(currentScratchAllocator, currentScratchFree));
            setHsLibraryAllocator("hs_set_scratch_allocator", currentScratchAllocator, currentScratchFree);
        }
    }

    @Override
    public void setStreamAllocator(DualAllocator alloc, DualFree free) {
        synchronized (ALLOCATOR_LOCK) {
            Arena arena = ensureAllocatorArena();
            currentStreamAllocator = wrapAllocator(alloc, arena);
            currentStreamFree = wrapFree(free, arena);
            checkResult(hyperscan.hs_set_stream_allocator(currentStreamAllocator, currentStreamFree));
            setHsLibraryAllocator("hs_set_stream_allocator", currentStreamAllocator, currentStreamFree);
        }
    }

    @Override
    public DualExpression createExpression(String pattern, EnumSet<DualExpressionFlag> flags, Integer id) {
        return new DualExpression(pattern, flags, id);
    }

    @Override
    public DualDatabase compileDatabase(List<DualExpression> expressions, DualMode mode) {
        if (mode == DualMode.BLOCK) {
            return compileDatabaseWrapper(expressions);
        }
        int nativeMode = toPanamaMode(mode);
        if (mode == DualMode.STREAM && hasFlag(expressions, DualExpressionFlag.SOM_LEFTMOST)) {
            nativeMode |= hyperscan.HS_MODE_SOM_HORIZON_LARGE();
        }
        return compileNative(expressions, nativeMode);
    }

    private static DualDatabase compileDatabaseWrapper(List<DualExpression> expressions) {
        List<Expression> panamaExpressions = new ArrayList<>();
        for (DualExpression expr : expressions) {
            panamaExpressions.add(toPanamaExpression(expr));
        }
        try {
            return new PanamaDatabase(com.xenoamess.hyperscan_panama.wrapper.Database.compile(panamaExpressions));
        } catch (CompileErrorException e) {
            throw new RuntimeException(e);
        }
    }

    private static DualDatabase compileNative(List<DualExpression> expressions, int mode) {
        int n = expressions.size();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expressionsPtr = arena.allocate(n * ValueLayout.ADDRESS.byteSize());
            MemorySegment flags = arena.allocate(n * ValueLayout.JAVA_INT.byteSize());
            MemorySegment ids = arena.allocate(n * ValueLayout.JAVA_INT.byteSize());
            MemorySegment extPtr = arena.allocate(n * ValueLayout.ADDRESS.byteSize());

            // Bulk allocate all pattern strings into a single native block.
            byte[][] encoded = new byte[n][];
            long totalPatternBytes = 0;
            for (int i = 0; i < n; i++) {
                byte[] bytes = expressions.get(i).pattern().getBytes(StandardCharsets.UTF_8);
                encoded[i] = bytes;
                totalPatternBytes += (long) bytes.length + 1L;
            }
            MemorySegment strings = arena.allocate(totalPatternBytes);
            long offset = 0;
            for (int i = 0; i < n; i++) {
                DualExpression expr = expressions.get(i);
                byte[] bytes = encoded[i];
                long len = bytes.length;
                MemorySegment ptr = strings.asSlice(offset, len + 1L);
                ptr.copyFrom(MemorySegment.ofArray(bytes).asSlice(0, len));
                ptr.set(ValueLayout.JAVA_BYTE, len, (byte) 0);
                expressionsPtr.setAtIndex(ValueLayout.ADDRESS, i, ptr);
                flags.setAtIndex(ValueLayout.JAVA_INT, i, toFlagBits(expr.flags()));
                ids.setAtIndex(ValueLayout.JAVA_INT, i, expr.id() != null ? expr.id() : 0);
                offset += len + 1L;
            }

            // Bulk allocate expr_ext structs.
            long extSize = hs_expr_ext.layout().byteSize();
            MemorySegment extBlock = arena.allocate(n * extSize);
            for (int i = 0; i < n; i++) {
                MemorySegment ext = extBlock.asSlice(i * extSize, extSize);
                initDefaultExprExt(ext);
                extPtr.setAtIndex(ValueLayout.ADDRESS, i, ext);
            }

            MemorySegment dbOut = zeroAddressOut(arena);
            MemorySegment errOut = zeroAddressOut(arena);
            int result = hyperscan.hs_compile_ext_multi(expressionsPtr, flags, ids, extPtr, n, mode, MemorySegment.NULL, dbOut, errOut);
            if (result != 0) {
                MemorySegment err = reinterpretCompileError(errOut.get(ValueLayout.ADDRESS, 0));
                try {
                    throw new RuntimeException("Compile error: " + readCompileErrorMessage(err));
                } finally {
                    if (err != null && err.address() != 0) {
                        hyperscan.hs_free_compile_error(err);
                    }
                }
            }
            MemorySegment db = dbOut.get(ValueLayout.ADDRESS, 0);
            try {
                return new PanamaNativeDatabase(reinterpretHandle(db), List.copyOf(expressions));
            } catch (RuntimeException | Error e) {
                hyperscan.hs_free_database(db);
                throw e;
            }
        }
    }

    @Override
    public DualDatabase compileDatabase(DualExpression expression, DualMode mode) {
        return compileDatabase(List.of(expression), mode);
    }

    @Override
    public DualDatabase compileDatabase(DualExpression[] expressions, DualMode mode) {
        return compileDatabase(List.of(expressions), mode);
    }

    @Override
    public DualExpression getExpression(DualDatabase database, int id) {
        PanamaDatabase db = (PanamaDatabase) database;
        try {
            Method method = com.xenoamess.hyperscan_panama.wrapper.Database.class.getDeclaredMethod("getExpression", int.class);
            method.setAccessible(true);
            Expression expr = (Expression) method.invoke(db.database(), id);
            return expr == null ? null : toDualExpression(expr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualScanner createScanner() {
        return new PanamaScanner(new Scanner());
    }

    @Override
    public void allocScratch(DualScanner scanner, DualDatabase database) {
        PanamaScanner s = (PanamaScanner) scanner;
        s.requireOpen();
        if (database instanceof PanamaDatabase db) {
            s.scanner.allocScratch(db.database());
        } else {
            s.ensureNativeScratch(nativeDatabase(database));
        }
    }

    @Override
    public List<DualMatch> scan(DualScanner scanner, DualDatabase database, String input) {
        PanamaScanner s = (PanamaScanner) scanner;
        s.requireOpen();
        PanamaDatabase db = (PanamaDatabase) database;
        List<Match> matches;
        try (PanamaDatabaseOperation ignored = acquireDatabaseOperation(database)) {
            matches = s.scanner.scan(db.database(), input);
        }
        List<DualMatch> result = new ArrayList<>();
        for (Match m : matches) {
            result.add(new DualMatch(
                    toDualExpression(m.getMatchedExpression()),
                    m.getMatchedExpression().getId(),
                    m.getStartPosition(),
                    m.getEndPosition(),
                    m.getMatchedString()
            ));
        }
        return result;
    }

    @Override
    public void scan(DualScanner scanner, DualDatabase database, String input, DualStringMatchHandler handler) {
        PanamaScanner s = (PanamaScanner) scanner;
        s.requireOpen();
        PanamaDatabase db = (PanamaDatabase) database;
        AdapterStringMatchHandler adapterHandler = AdapterStringMatchHandler.bind(handler);
        try (PanamaDatabaseOperation ignored = acquireDatabaseOperation(database)) {
            s.scanner.scan(db.database(), input, adapterHandler);
        }
        adapterHandler.propagateFailure();
    }

    @Override
    public void scan(DualScanner scanner, DualDatabase database, byte[] input, DualByteMatchHandler handler) {
        PanamaScanner s = (PanamaScanner) scanner;
        s.requireOpen();
        PanamaDatabase db = (PanamaDatabase) database;
        AdapterByteMatchHandler adapterHandler = AdapterByteMatchHandler.bind(handler);
        try (PanamaDatabaseOperation ignored = acquireDatabaseOperation(database)) {
            s.scanner.scan(db.database(), input, adapterHandler);
        }
        adapterHandler.propagateFailure();
    }

    @Override
    public void scan(DualScanner scanner, DualDatabase database, ByteBuffer input, DualByteMatchHandler handler) {
        PanamaScanner s = (PanamaScanner) scanner;
        s.requireOpen();
        PanamaDatabase db = (PanamaDatabase) database;
        AdapterByteMatchHandler adapterHandler = AdapterByteMatchHandler.bind(handler);
        try (PanamaDatabaseOperation ignored = acquireDatabaseOperation(database)) {
            s.scanner.scan(db.database(), input, adapterHandler);
        }
        adapterHandler.propagateFailure();
    }

    @Override
    public boolean hasMatch(DualScanner scanner, DualDatabase database, String input) {
        PanamaScanner s = (PanamaScanner) scanner;
        s.requireOpen();
        PanamaDatabase db = (PanamaDatabase) database;
        try (PanamaDatabaseOperation ignored = acquireDatabaseOperation(database)) {
            return s.scanner.hasMatch(db.database(), input);
        }
    }

    @Override
    public boolean hasMatch(DualScanner scanner, DualDatabase database, byte[] input) {
        PanamaScanner s = (PanamaScanner) scanner;
        s.requireOpen();
        PanamaDatabase db = (PanamaDatabase) database;
        try (PanamaDatabaseOperation ignored = acquireDatabaseOperation(database)) {
            return s.scanner.hasMatch(db.database(), input);
        }
    }

    @Override
    public DualStream openStream(DualDatabase database) {
        acquireDatabaseStreamLease(database);
        boolean transferred = false;
        try {
            MemorySegment db = nativeDatabase(database);
            List<DualExpression> expressions = database instanceof PanamaNativeDatabase nativeDb
                    ? nativeDb.expressions() : List.of();
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment streamOut = zeroAddressOut(arena);
                checkResult(hyperscan.hs_open_stream(db, 0, streamOut));
                MemorySegment stream = reinterpretHandle(streamOut.get(ValueLayout.ADDRESS, 0));
                try {
                    MemorySegment scratchOut = zeroAddressOut(arena);
                    checkResult(hyperscan.hs_alloc_scratch(db, scratchOut));
                    MemorySegment scratch = reinterpretHandle(scratchOut.get(ValueLayout.ADDRESS, 0));
                    try {
                        PanamaStream result = new PanamaStream(
                                stream, new PanamaScratchState(scratch), expressions, database);
                        transferred = true;
                        return result;
                    } catch (RuntimeException | Error e) {
                        hyperscan.hs_free_scratch(scratch);
                        throw e;
                    }
                } catch (RuntimeException | Error e) {
                    hyperscan.hs_close_stream(stream, MemorySegment.NULL, MemorySegment.NULL, MemorySegment.NULL);
                    throw e;
                }
            }
        } finally {
            if (!transferred) {
                releaseDatabaseStreamLease(database);
            }
        }
    }

    private static MemorySegment getStreamBuffer(byte[] input) {
        if (input == null) {
            return MemorySegment.NULL;
        }
        ByteBuffer buffer = STREAM_BUFFER.get();
        if (buffer.capacity() < input.length) {
            buffer = ByteBuffer.allocateDirect(input.length);
            STREAM_BUFFER.set(buffer);
        }
        buffer.clear();
        MemorySegment segment = MemorySegment.ofBuffer(buffer);
        UNSAFE.copyMemory(input, Unsafe.ARRAY_BYTE_BASE_OFFSET, null, segment.address(), input.length);
        buffer.limit(input.length);
        return segment.asSlice(0, input.length);
    }

    private static PanamaStreamInput streamInput(byte[] input) {
        if (input == null || STREAM_CALLBACK.get() == null) {
            return new PanamaStreamInput(getStreamBuffer(input), null);
        }
        Arena arena = Arena.ofConfined();
        try {
            return new PanamaStreamInput(allocateBytes(arena, input), arena);
        } catch (RuntimeException | Error e) {
            arena.close();
            throw e;
        }
    }

    private record PanamaStreamInput(MemorySegment data, Arena arena) implements AutoCloseable {
        @Override
        public void close() {
            if (arena != null) {
                arena.close();
            }
        }
    }

    @Override
    public void scanStream(DualScanner scanner, DualStream stream, byte[] input, DualByteMatchHandler handler) {
        PanamaStream s = (PanamaStream) stream;
        MemorySegment nativeStream = s.beginOperation();
        try (PanamaStreamInput data = streamInput(input)) {
            MemorySegment scratch = streamScratch(scanner, s);
            if (handler != null) {
                setHandlerContext(handler, s.expressionsById);
            }
            try {
                int length = input == null ? 4 : input.length;
                int result = propagateHandlerFailure(hyperscan.hs_scan_stream(
                        nativeStream, data.data(), length, 0, scratch,
                        handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL));
                if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED()) {
                    checkResult(result);
                }
            } finally {
                if (handler != null) {
                    clearHandlerContext();
                }
            }
        } finally {
            s.endOperation();
        }
    }

    @Override
    public void scanStream(DualScanner scanner, DualStream stream, ByteBuffer input, DualByteMatchHandler handler) {
        PanamaStream s = (PanamaStream) stream;
        MemorySegment nativeStream = s.beginOperation();
        try {
            MemorySegment data;
            int length;
            if (input == null) {
                data = MemorySegment.NULL;
                length = 4;
            } else {
                data = directBufferSegment(input);
                length = input.remaining();
            }
            MemorySegment scratch = streamScratch(scanner, s);
            if (handler != null) {
                setHandlerContext(handler, s.expressionsById);
            }
            try {
                int result = propagateHandlerFailure(hyperscan.hs_scan_stream(
                        nativeStream, data, length, 0, scratch,
                        handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL));
                if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED()) {
                    checkResult(result);
                }
            } finally {
                if (handler != null) {
                    clearHandlerContext();
                }
            }
        } finally {
            s.endOperation();
        }
    }

    @Override
    public void closeStream(DualScanner scanner, DualStream stream, DualByteMatchHandler handler) {
        PanamaStream s = (PanamaStream) stream;
        if (s.isClosed()) {
            s.close();
            return;
        }
        if (handler != null) {
            setHandlerContext(handler, s.expressionsById);
        }
        try {
            int result = propagateHandlerFailure(s.closeNative(
                    streamScratch(scanner, s),
                    handler == null ? MemorySegment.NULL : MATCH_HANDLER, false));
            if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED()) {
                checkResult(result);
            }
        } finally {
            if (handler != null) {
                clearHandlerContext();
            }
        }
    }

    @Override
    public void scanVector(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler) {
        if (input == null) {
            throw new IllegalArgumentException("Input vector is null");
        }
        try (PanamaDatabaseOperation operation = acquireDatabaseOperation(database);
             PanamaVectorScratch scratchUse = acquireVectorScratch(scanner, operation.database)) {
            MemorySegment db = operation.database;
            MemorySegment scratch = scratchUse.scratch;
            List<DualExpression> expressions = database instanceof PanamaNativeDatabase nativeDb ? nativeDb.expressions() : List.of();
            if (handler != null) {
                setHandlerContext(handler, buildExpressionLookup(expressions));
            }
            try {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment dataPtrs = arena.allocate(input.length * ValueLayout.ADDRESS.byteSize());
                    MemorySegment lengthPtr = arena.allocate(input.length * ValueLayout.JAVA_INT.byteSize());
                    for (int i = 0; i < input.length; i++) {
                        byte[] data = input[i];
                        MemorySegment dataSeg = data == null ? MemorySegment.NULL : allocateBytes(arena, data);
                        dataPtrs.setAtIndex(ValueLayout.ADDRESS, i, dataSeg);
                        lengthPtr.setAtIndex(ValueLayout.JAVA_INT, i, data == null ? 4 : data.length);
                    }
                    int result = propagateHandlerFailure(hyperscan.hs_scan_vector(
                            db, dataPtrs, lengthPtr, input.length, 0, scratch,
                            handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL));
                    if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED()) {
                        checkResult(result);
                    }
                }
            } finally {
                if (handler != null) {
                    clearHandlerContext();
                }
            }
        }
    }

    @Override
    public void scanVector(DualScanner scanner, DualDatabase database, ByteBuffer[] input, DualByteMatchHandler handler) {
        byte[][] data = input == null ? null : new byte[input.length][];
        if (input != null) {
            for (int i = 0; i < input.length; i++) {
                ByteBuffer buffer = input[i];
                if (buffer != null) {
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.duplicate().get(bytes);
                    data[i] = bytes;
                }
            }
        }
        scanVector(scanner, database, data, handler);
    }

    @Override
    public String getDatabaseInfo(DualDatabase database) {
        return databaseInfo(nativeDatabase(database));
    }

    @Override
    public String getSerializedDatabaseInfo(byte[] data) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment dataSeg = allocateBytes(arena, data);
            MemorySegment infoOut = zeroAddressOut(arena);
            checkResult(hyperscan.hs_serialized_database_info(dataSeg, data.length, infoOut));
            MemorySegment info = reinterpretString(infoOut.get(ValueLayout.ADDRESS, 0));
            try {
                return info.getString(0);
            } finally {
                freeMiscSegment(info);
            }
        }
    }

    @Override
    public byte[] serialize(DualDatabase database) {
        if (database instanceof PanamaDatabase wrapper) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                wrapper.database().save(out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return out.toByteArray();
        }
        MemorySegment db = nativeDatabase(database);
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment bytesOut = zeroAddressOut(arena);
            MemorySegment sizeOut = zeroLongOut(arena);
            checkResult(hyperscan.hs_serialize_database(db, bytesOut, sizeOut));
            long length = sizeOut.get(ValueLayout.JAVA_LONG, 0);
            MemorySegment bytes = reinterpretBuffer(bytesOut.get(ValueLayout.ADDRESS, 0), length);
            try {
                byte[] out = new byte[Math.toIntExact(length)];
                MemorySegment.copy(bytes, 0, MemorySegment.ofArray(out), 0, length);
                return out;
            } finally {
                freeMiscSegment(bytes);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualDatabase deserialize(byte[] data) {
        try {
            return new PanamaDatabase(com.xenoamess.hyperscan_panama.wrapper.Database.load(new ByteArrayInputStream(data)));
        } catch (Exception e) {
            return deserializeNative(data);
        }
    }

    private static DualDatabase deserializeNative(byte[] data) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment dataSeg = allocateBytes(arena, data);
            MemorySegment dbOut = zeroAddressOut(arena);
            checkResult(hyperscan.hs_deserialize_database(dataSeg, data.length, dbOut));
            MemorySegment db = reinterpretHandle(dbOut.get(ValueLayout.ADDRESS, 0));
            return new PanamaNativeDatabase(db, List.of());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeScanner(DualScanner scanner) {
        scanner.close();
    }

    @Override
    public void closeDatabase(DualDatabase database) {
        if (database instanceof PanamaDatabase wrapper) {
            wrapper.close();
        } else if (database instanceof PanamaNativeDatabase nativeDb) {
            nativeDb.close();
        } else if (database instanceof PanamaRawDatabase rawDb) {
            rawDb.close();
        }
    }

    @Override
    public long getDatabaseSize(DualDatabase database) {
        if (database instanceof PanamaDatabase wrapper) {
            return wrapper.database().getSize();
        }
        MemorySegment db = nativeDatabase(database);
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment sizeOut = zeroLongOut(arena);
            checkResult(hyperscan.hs_database_size(db, sizeOut));
            return sizeOut.get(ValueLayout.JAVA_LONG, 0);
        }
    }

    @Override
    public long getScannerSize(DualScanner scanner) {
        if (scanner instanceof PanamaScanner wrapper) {
            return wrapper.getSize();
        }
        MemorySegment scratch = nativeScratch(scanner);
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment sizeOut = zeroLongOut(arena);
            checkResult(hyperscan.hs_scratch_size(scratch, sizeOut));
            return sizeOut.get(ValueLayout.JAVA_LONG, 0);
        }
    }

    @Override
    public String getVersion() {
        return Scanner.getVersion();
    }

    @Override
    public String getPlatform() {
        String requestedPlatform = System.getProperty("com.xenoamess.hyperscan_panama.platform");
        if (requestedPlatform != null && !requestedPlatform.isBlank()) {
            return requestedPlatform;
        }
        return HyperscanNativeLoader.selectPlatform();
    }

    @Override
    public boolean validate(DualExpression expression) {
        return toPanamaExpression(expression).validate().isValid();
    }

    @Override
    public String getValidationError(DualExpression expression) {
        return toPanamaExpression(expression).validate().getErrorMessage();
    }

    @Override
    public DualPatternFilter createPatternFilter(List<Pattern> patterns) {
        try {
            return new PanamaPatternFilter(new PatternFilter(patterns));
        } catch (CompileErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int flagsToBits(EnumSet<DualExpressionFlag> flags) {
        return toFlagBits(flags);
    }

    @Override
    public int success() {
        return hyperscan.HS_SUCCESS();
    }

    @Override
    public int invalid() {
        return hyperscan.HS_INVALID();
    }

    @Override
    public int noMem() {
        return hyperscan.HS_NOMEM();
    }

    @Override
    public int badAlloc() {
        return hyperscan.HS_BAD_ALLOC();
    }

    @Override
    public int compilerError() {
        return hyperscan.HS_COMPILER_ERROR();
    }

    @Override
    public int dbVersionError() {
        return hyperscan.HS_DB_VERSION_ERROR();
    }

    @Override
    public int dbModeError() {
        return hyperscan.HS_DB_MODE_ERROR();
    }

    @Override
    public int dbPlatformError() {
        return hyperscan.HS_DB_PLATFORM_ERROR();
    }

    @Override
    public int insufficientSpace() {
        return hyperscan.HS_INSUFFICIENT_SPACE();
    }

    @Override
    public int scanTerminated() {
        return hyperscan.HS_SCAN_TERMINATED();
    }

    @Override
    public int scratchInUse() {
        return hyperscan.HS_SCRATCH_IN_USE();
    }

    @Override
    public int badAlign() {
        return hyperscan.HS_BAD_ALIGN();
    }

    @Override
    public int validPlatformRaw() {
        return hyperscan.hs_valid_platform();
    }

    @Override
    public int modeBlock() {
        return hyperscan.HS_MODE_BLOCK();
    }

    @Override
    public int modeStream() {
        return hyperscan.HS_MODE_STREAM();
    }

    @Override
    public int modeVectored() {
        return hyperscan.HS_MODE_VECTORED();
    }

    @Override
    public int modeSomHorizonLarge() {
        return hyperscan.HS_MODE_SOM_HORIZON_LARGE();
    }

    @Override
    public int modeSomHorizonMedium() {
        return hyperscan.HS_MODE_SOM_HORIZON_MEDIUM();
    }

    @Override
    public int modeSomHorizonSmall() {
        return hyperscan.HS_MODE_SOM_HORIZON_SMALL();
    }

    @Override
    public long offsetPastHorizon() {
        return hyperscan.HS_OFFSET_PAST_HORIZON();
    }

    @Override
    public DualCompileResult compileRaw(String pattern, int flags, int mode) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expr = pattern == null ? MemorySegment.NULL : arena.allocateFrom(pattern);
            MemorySegment dbOut = zeroAddressOut(arena);
            MemorySegment errOut = zeroAddressOut(arena);
            int result = hyperscan.hs_compile(expr, flags, mode, MemorySegment.NULL, dbOut, errOut);
            return buildCompileResult(result, dbOut, errOut, pattern == null ? List.of() : List.of(new DualExpression(pattern, EnumSet.noneOf(DualExpressionFlag.class), 0)));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualCompileResult compileRaw(String pattern, int flags, int mode, DualPlatformInfo platform) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expr = pattern == null ? MemorySegment.NULL : arena.allocateFrom(pattern);
            MemorySegment plat = arena.allocate(hs_platform_info.layout());
            hs_platform_info.tune(plat, platform.tune());
            hs_platform_info.cpu_features(plat, platform.cpuFeatures());
            MemorySegment dbOut = zeroAddressOut(arena);
            MemorySegment errOut = zeroAddressOut(arena);
            int result = hyperscan.hs_compile(expr, flags, mode, plat, dbOut, errOut);
            return buildCompileResult(result, dbOut, errOut, pattern == null ? List.of() : List.of(new DualExpression(pattern, EnumSet.noneOf(DualExpressionFlag.class), 0)));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualCompileResult compileExtRaw(String pattern, int flags, DualExpressionExt ext, int mode) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expr = pattern == null ? MemorySegment.NULL : arena.allocateFrom(pattern);
            MemorySegment expressionsPtr = zeroAddressOut(arena);
            expressionsPtr.set(ValueLayout.ADDRESS, 0, expr);
            MemorySegment flagsPtr = arena.allocate(ValueLayout.JAVA_INT);
            flagsPtr.set(ValueLayout.JAVA_INT, 0, flags);
            MemorySegment idsPtr = arena.allocate(ValueLayout.JAVA_INT);
            idsPtr.set(ValueLayout.JAVA_INT, 0, 0);
            MemorySegment extStruct = arena.allocate(hs_expr_ext.layout());
            applyExprExt(extStruct, ext);
            MemorySegment extPtr = zeroAddressOut(arena);
            extPtr.set(ValueLayout.ADDRESS, 0, extStruct);
            MemorySegment dbOut = zeroAddressOut(arena);
            MemorySegment errOut = zeroAddressOut(arena);
            int result = hyperscan.hs_compile_ext_multi(expressionsPtr, flagsPtr, idsPtr, extPtr, 1, mode, MemorySegment.NULL, dbOut, errOut);
            return buildCompileResult(result, dbOut, errOut, pattern == null ? List.of() : List.of(new DualExpression(pattern, EnumSet.noneOf(DualExpressionFlag.class), 0)));
        }
    }

    private static DualCompileResult buildCompileResult(int result, MemorySegment dbOut, MemorySegment errOut, List<DualExpression> expressions) {
        if (result == 0) {
            MemorySegment db = dbOut.get(ValueLayout.ADDRESS, 0);
            try {
                return new DualCompileResult(0,
                        new PanamaNativeDatabase(reinterpretHandle(db), List.copyOf(expressions)), null);
            } catch (RuntimeException | Error e) {
                hyperscan.hs_free_database(db);
                throw e;
            }
        }
        MemorySegment err = reinterpretCompileError(errOut.get(ValueLayout.ADDRESS, 0));
        try {
            return new DualCompileResult(result, null, readCompileErrorMessage(err));
        } finally {
            if (err != null && err.address() != 0) {
                hyperscan.hs_free_compile_error(err);
            }
        }
    }

    @Override
    public DualCompileResult compileRaw(DualExpression expression, int mode) {
        return compileRaw(List.of(expression), mode);
    }

    @Override
    public DualCompileResult compileRaw(DualExpression[] expressions, int mode) {
        return compileRaw(List.of(expressions), mode);
    }

    @Override
    public DualCompileResult compileRaw(List<DualExpression> expressions, int mode) {
        int n = expressions == null ? 0 : expressions.size();
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expressionsPtr = n == 0 ? MemorySegment.NULL : arena.allocate(n * ValueLayout.ADDRESS.byteSize());
            MemorySegment flags = n == 0 ? MemorySegment.NULL : arena.allocate(n * ValueLayout.JAVA_INT.byteSize());
            MemorySegment ids = n == 0 ? MemorySegment.NULL : arena.allocate(n * ValueLayout.JAVA_INT.byteSize());
            for (int i = 0; i < n; i++) {
                DualExpression expr = expressions.get(i);
                expressionsPtr.setAtIndex(ValueLayout.ADDRESS, i, arena.allocateFrom(expr.pattern()));
                flags.setAtIndex(ValueLayout.JAVA_INT, i, toFlagBits(expr.flags()));
                ids.setAtIndex(ValueLayout.JAVA_INT, i, expr.id() != null ? expr.id() : 0);
            }
            MemorySegment dbOut = zeroAddressOut(arena);
            MemorySegment errOut = zeroAddressOut(arena);
            int result = hyperscan.hs_compile_multi(expressionsPtr, flags, ids, n, mode, MemorySegment.NULL, dbOut, errOut);
            return buildCompileResult(result, dbOut, errOut, expressions == null ? List.of() : expressions);
        }
    }

    @Override
    public DualCompileResult compileRaw(String[] patterns, int[] flags, int[] ids, int mode) {
        int n = patterns == null ? 0 : patterns.length;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expressionsPtr = n == 0 ? MemorySegment.NULL : arena.allocate(n * ValueLayout.ADDRESS.byteSize());
            MemorySegment flagsPtr = flags == null ? MemorySegment.NULL : arena.allocate(n * ValueLayout.JAVA_INT.byteSize());
            MemorySegment idsPtr = ids == null ? MemorySegment.NULL : arena.allocate(n * ValueLayout.JAVA_INT.byteSize());
            List<DualExpression> expressions = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                String pattern = patterns[i];
                MemorySegment expr = pattern == null ? MemorySegment.NULL : arena.allocateFrom(pattern);
                expressionsPtr.setAtIndex(ValueLayout.ADDRESS, i, expr);
                expressions.add(new DualExpression(pattern != null ? pattern : "", EnumSet.noneOf(DualExpressionFlag.class), ids != null ? ids[i] : 0));
            }
            if (flags != null) {
                for (int i = 0; i < n; i++) {
                    flagsPtr.setAtIndex(ValueLayout.JAVA_INT, i, flags[i]);
                }
            }
            if (ids != null) {
                for (int i = 0; i < n; i++) {
                    idsPtr.setAtIndex(ValueLayout.JAVA_INT, i, ids[i]);
                }
            }
            MemorySegment dbOut = zeroAddressOut(arena);
            MemorySegment errOut = zeroAddressOut(arena);
            int result = hyperscan.hs_compile_multi(expressionsPtr, flagsPtr, idsPtr, n, mode, MemorySegment.NULL, dbOut, errOut);
            return buildCompileResult(result, dbOut, errOut, expressions);
        }
    }

    @Override
    public DualCompileResult compileNullOutputRaw(String pattern, int flags, int mode) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expr = pattern == null ? MemorySegment.NULL : arena.allocateFrom(pattern);
            MemorySegment errOut = zeroAddressOut(arena);
            int result = hyperscan.hs_compile(expr, flags, mode, MemorySegment.NULL, MemorySegment.NULL, errOut);
            MemorySegment err = reinterpretCompileError(errOut.get(ValueLayout.ADDRESS, 0));
            try {
                return new DualCompileResult(result, null, readCompileErrorMessage(err));
            } finally {
                if (err != null && err.address() != 0) {
                    hyperscan.hs_free_compile_error(err);
                }
            }
        }
    }

    @Override
    public DualCompileResult compileMultiNullOutputRaw(String[] patterns, int[] flags, int[] ids, int mode) {
        int n = patterns == null ? 0 : patterns.length;
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expressionsPtr = n == 0 ? MemorySegment.NULL : arena.allocate(n * ValueLayout.ADDRESS.byteSize());
            MemorySegment flagsPtr = flags == null ? MemorySegment.NULL : arena.allocate(n * ValueLayout.JAVA_INT.byteSize());
            MemorySegment idsPtr = ids == null ? MemorySegment.NULL : arena.allocate(n * ValueLayout.JAVA_INT.byteSize());
            for (int i = 0; i < n; i++) {
                String pattern = patterns[i];
                MemorySegment expr = pattern == null ? MemorySegment.NULL : arena.allocateFrom(pattern);
                expressionsPtr.setAtIndex(ValueLayout.ADDRESS, i, expr);
            }
            if (flags != null) {
                for (int i = 0; i < n; i++) {
                    flagsPtr.setAtIndex(ValueLayout.JAVA_INT, i, flags[i]);
                }
            }
            if (ids != null) {
                for (int i = 0; i < n; i++) {
                    idsPtr.setAtIndex(ValueLayout.JAVA_INT, i, ids[i]);
                }
            }
            MemorySegment errOut = zeroAddressOut(arena);
            int result = hyperscan.hs_compile_multi(expressionsPtr, flagsPtr, idsPtr, n, mode, MemorySegment.NULL, MemorySegment.NULL, errOut);
            MemorySegment err = reinterpretCompileError(errOut.get(ValueLayout.ADDRESS, 0));
            try {
                return new DualCompileResult(result, null, readCompileErrorMessage(err));
            } finally {
                if (err != null && err.address() != 0) {
                    hyperscan.hs_free_compile_error(err);
                }
            }
        }
    }

    @Override
    public DualResult<DualDatabase> deserializeRaw(byte[] data) {
        if (data == null) {
            return DualResult.error(hyperscan.HS_INVALID());
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment dataSeg = allocateBytes(arena, data);
            MemorySegment dbOut = zeroAddressOut(arena);
            int result = hyperscan.hs_deserialize_database(dataSeg, data.length, dbOut);
            if (result == 0) {
                MemorySegment db = dbOut.get(ValueLayout.ADDRESS, 0);
                try {
                    return DualResult.success(new PanamaNativeDatabase(reinterpretHandle(db), List.of()));
                } catch (RuntimeException | Error e) {
                    hyperscan.hs_free_database(db);
                    throw e;
                }
            }
            return DualResult.error(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualResult<DualDatabase> deserializeNullOutputRaw(byte[] data) {
        if (data == null) {
            return DualResult.error(hyperscan.HS_INVALID());
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment dataSeg = allocateBytes(arena, data);
            int result = hyperscan.hs_deserialize_database(dataSeg, data.length, MemorySegment.NULL);
            return result == 0 ? DualResult.success(null) : DualResult.error(result);
        }
    }

    @Override
    public int deserializeAtRaw(byte[] data, DualDatabase database) {
        if (data == null) {
            return hyperscan.HS_INVALID();
        }
        MemorySegment db = database == null ? MemorySegment.NULL : nativeDatabase(database);
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment dataSeg = allocateBytes(arena, data);
            return hyperscan.hs_deserialize_database_at(dataSeg, data.length, db);
        }
    }

    @Override
    public DualDatabase allocateRawDatabase(long size) {
        Arena arena = Arena.ofShared();
        MemorySegment memory = arena.allocate(size, 8);
        if ((memory.address() & 7L) != 0) {
            arena.close();
            throw new RuntimeException("Raw database memory is not 8-byte aligned");
        }
        return new PanamaRawDatabase(memory, new PanamaRawDatabaseState(memory, arena), true);
    }

    @Override
    public DualDatabase offsetRawDatabase(DualDatabase database, long offset) {
        if (!(database instanceof PanamaRawDatabase raw)) {
            throw new IllegalArgumentException("Not a raw database: " + database.getClass());
        }
        MemorySegment db = raw.requireDatabase().asSlice(offset);
        return new PanamaRawDatabase(db, raw.state, false);
    }

    @Override
    public DualResult<byte[]> serializeRaw(DualDatabase database) {
        if (database == null) {
            return DualResult.error(hyperscan.HS_INVALID());
        }
        MemorySegment db = nativeDatabase(database);
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment bytesOut = zeroAddressOut(arena);
            MemorySegment sizeOut = zeroLongOut(arena);
            int result = hyperscan.hs_serialize_database(db, bytesOut, sizeOut);
            if (result != 0) {
                return DualResult.error(result);
            }
            long length = sizeOut.get(ValueLayout.JAVA_LONG, 0);
            MemorySegment bytes = reinterpretBuffer(bytesOut.get(ValueLayout.ADDRESS, 0), length);
            try {
                byte[] out = new byte[Math.toIntExact(length)];
                MemorySegment.copy(bytes, 0, MemorySegment.ofArray(out), 0, length);
                return DualResult.success(out);
            } finally {
                freeMiscSegment(bytes);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int serializeNoBufferRaw(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        MemorySegment db = nativeDatabase(database);
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment sizeOut = zeroLongOut(arena);
            return hyperscan.hs_serialize_database(db, MemorySegment.NULL, sizeOut);
        }
    }

    @Override
    public int serializeNoLengthRaw(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        MemorySegment db = nativeDatabase(database);
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment bytesOut = zeroAddressOut(arena);
            return hyperscan.hs_serialize_database(db, bytesOut, MemorySegment.NULL);
        }
    }

    @Override
    public DualResult<String> getDatabaseInfoRaw(DualDatabase database) {
        if (database == null) {
            return DualResult.error(hyperscan.HS_INVALID());
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment infoOut = zeroAddressOut(arena);
            int result = hyperscan.hs_database_info(nativeDatabase(database), infoOut);
            if (result == 0) {
                MemorySegment info = reinterpretString(infoOut.get(ValueLayout.ADDRESS, 0));
                try {
                    return DualResult.success(info.getString(0));
                } finally {
                    freeMiscSegment(info);
                }
            }
            return DualResult.error(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualResult<String> getSerializedDatabaseInfoRaw(byte[] data) {
        if (data == null) {
            return DualResult.error(hyperscan.HS_INVALID());
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment dataSeg = allocateBytes(arena, data);
            MemorySegment infoOut = zeroAddressOut(arena);
            int result = hyperscan.hs_serialized_database_info(dataSeg, data.length, infoOut);
            if (result == 0) {
                MemorySegment info = reinterpretString(infoOut.get(ValueLayout.ADDRESS, 0));
                try {
                    return DualResult.success(info.getString(0));
                } finally {
                    freeMiscSegment(info);
                }
            }
            return DualResult.error(result);
        }
    }

    @Override
    public int serializedDatabaseInfoNullOutput(byte[] data) {
        if (data == null) {
            return hyperscan.HS_INVALID();
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment dataSeg = allocateBytes(arena, data);
            return hyperscan.hs_serialized_database_info(dataSeg, data.length, MemorySegment.NULL);
        }
    }

    @Override
    public DualResult<Long> getDatabaseSizeRaw(DualDatabase database) {
        if (database == null) {
            return DualResult.error(hyperscan.HS_INVALID());
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment sizeOut = zeroLongOut(arena);
            int result = hyperscan.hs_database_size(nativeDatabase(database), sizeOut);
            return new DualResult<>(result, result == 0 ? sizeOut.get(ValueLayout.JAVA_LONG, 0) : null, null);
        }
    }

    @Override
    public DualResult<Long> getSerializedDatabaseSizeRaw(byte[] data) {
        if (data == null) {
            return DualResult.error(hyperscan.HS_INVALID());
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment dataSeg = allocateBytes(arena, data);
            MemorySegment sizeOut = zeroLongOut(arena);
            int result = hyperscan.hs_serialized_database_size(dataSeg, data.length, sizeOut);
            return new DualResult<>(result, result == 0 ? sizeOut.get(ValueLayout.JAVA_LONG, 0) : null, null);
        }
    }

    @Override
    public int serializedDatabaseSizeNullOutput(byte[] data) {
        if (data == null) {
            return hyperscan.HS_INVALID();
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment dataSeg = allocateBytes(arena, data);
            return hyperscan.hs_serialized_database_size(dataSeg, data.length, MemorySegment.NULL);
        }
    }

    @Override
    public DualResult<Long> getStreamSizeRaw(DualDatabase database) {
        if (database == null) {
            return DualResult.error(hyperscan.HS_INVALID());
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment sizeOut = zeroLongOut(arena);
            int result = hyperscan.hs_stream_size(nativeDatabase(database), sizeOut);
            return new DualResult<>(result, result == 0 ? sizeOut.get(ValueLayout.JAVA_LONG, 0) : null, null);
        }
    }

    @Override
    public int streamSizeNullOutput(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        return hyperscan.hs_stream_size(nativeDatabase(database), MemorySegment.NULL);
    }

    @Override
    public DualResult<Long> getScratchSizeRaw(DualScanner scanner) {
        if (scanner == null) {
            return DualResult.error(hyperscan.HS_INVALID());
        }
        MemorySegment scratch = nativeScratch(scanner);
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment sizeOut = zeroLongOut(arena);
            int result = hyperscan.hs_scratch_size(scratch, sizeOut);
            return new DualResult<>(result, result == 0 ? sizeOut.get(ValueLayout.JAVA_LONG, 0) : null, null);
        }
    }

    @Override
    public DualScratchResult allocScratchRaw(DualDatabase database) {
        if (database == null) {
            return new DualScratchResult(hyperscan.HS_INVALID(), null, null);
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment scratchOut = zeroAddressOut(arena);
            int result = hyperscan.hs_alloc_scratch(nativeDatabase(database), scratchOut);
            if (result == 0) {
                MemorySegment scratch = reinterpretHandle(scratchOut.get(ValueLayout.ADDRESS, 0));
                return new DualScratchResult(0,
                        new PanamaRawScanner(new PanamaScratchState(scratch), true), null);
            }
            return new DualScratchResult(result, null, null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualScratchResult allocScratchRaw(DualDatabase database, DualScanner existingScratch) {
        if (database == null) {
            return new DualScratchResult(hyperscan.HS_INVALID(), null, null);
        }
        if (existingScratch != null
                && (!(existingScratch instanceof PanamaRawScanner raw) || !raw.isOwner())) {
            throw new IllegalArgumentException("Scratch is not an owning raw scratch");
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment scratchOut = zeroAddressOut(arena);
            MemorySegment existing = existingScratch == null ? MemorySegment.NULL : nativeScratch(existingScratch);
            scratchOut.set(ValueLayout.ADDRESS, 0, existing);
            int result = hyperscan.hs_alloc_scratch(nativeDatabase(database), scratchOut);
            if (result == 0) {
                MemorySegment scratch = reinterpretHandle(scratchOut.get(ValueLayout.ADDRESS, 0));
                if (existingScratch instanceof PanamaRawScanner raw) {
                    raw.replace(scratch);
                    return new DualScratchResult(0, raw, null);
                }
                if (existingScratch != null) {
                    throw new IllegalArgumentException("Unsupported scratch owner: " + existingScratch.getClass());
                }
                return new DualScratchResult(0,
                        new PanamaRawScanner(new PanamaScratchState(scratch), true), null);
            }
            return new DualScratchResult(result, null, null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int allocScratchNullOutput(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        return hyperscan.hs_alloc_scratch(nativeDatabase(database), MemorySegment.NULL);
    }

    @Override
    public int scratchSizeNullOutput() {
        return hyperscan.hs_scratch_size(MemorySegment.NULL, MemorySegment.NULL);
    }

    @Override
    public DualScratchResult cloneScratchRaw(DualScanner source) {
        if (source == null) {
            return new DualScratchResult(hyperscan.HS_INVALID(), null, null);
        }
        MemorySegment src = nativeScratch(source);
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment clonedOut = zeroAddressOut(arena);
            int result = hyperscan.hs_clone_scratch(src, clonedOut);
            if (result == 0) {
                MemorySegment cloned = reinterpretHandle(clonedOut.get(ValueLayout.ADDRESS, 0));
                return new DualScratchResult(0,
                        new PanamaRawScanner(new PanamaScratchState(cloned), true), null);
            }
            return new DualScratchResult(result, null, null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualStreamResult openStreamRaw(DualDatabase database) {
        if (database == null) {
            return new DualStreamResult(hyperscan.HS_INVALID(), null, null);
        }
        acquireDatabaseStreamLease(database);
        boolean transferred = false;
        try {
            MemorySegment db = nativeDatabase(database);
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment streamOut = zeroAddressOut(arena);
                int result = hyperscan.hs_open_stream(db, 0, streamOut);
                if (result != 0) {
                    return new DualStreamResult(result, null, null);
                }
                MemorySegment stream = reinterpretHandle(streamOut.get(ValueLayout.ADDRESS, 0));
                MemorySegment scratchOut = zeroAddressOut(arena);
                int allocResult = hyperscan.hs_alloc_scratch(db, scratchOut);
                if (allocResult != 0) {
                    hyperscan.hs_close_stream(stream, MemorySegment.NULL, MemorySegment.NULL, MemorySegment.NULL);
                    return new DualStreamResult(allocResult, null, null);
                }
                MemorySegment scratch = reinterpretHandle(scratchOut.get(ValueLayout.ADDRESS, 0));
                List<DualExpression> expressions = database instanceof PanamaNativeDatabase nativeDb
                        ? nativeDb.expressions() : List.of();
                try {
                    PanamaStream opened = new PanamaStream(
                            stream, new PanamaScratchState(scratch), expressions, database);
                    DualStreamResult response = new DualStreamResult(0, opened, null);
                    transferred = true;
                    return response;
                } catch (RuntimeException | Error e) {
                    hyperscan.hs_free_scratch(scratch);
                    hyperscan.hs_close_stream(stream, MemorySegment.NULL, MemorySegment.NULL, MemorySegment.NULL);
                    throw e;
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (!transferred) {
                releaseDatabaseStreamLease(database);
            }
        }
    }

    @Override
    public DualScanner getStreamScratch(DualStream stream) {
        PanamaScratchState scratchState = ((PanamaStream) stream).scratchState;
        return scratchState == null ? null : new PanamaRawScanner(scratchState, false);
    }

    @Override
    public int openStreamNullOutput(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        return hyperscan.hs_open_stream(nativeDatabase(database), 0, MemorySegment.NULL);
    }

    @Override
    public int scanStreamRaw(DualStream stream, byte[] input, DualScanner scanner, DualByteMatchHandler handler) {
        if (stream == null) {
            return hyperscan.HS_INVALID();
        }
        PanamaStream s = (PanamaStream) stream;
        MemorySegment nativeStream;
        try {
            nativeStream = s.beginOperation();
        } catch (IllegalStateException e) {
            return hyperscan.HS_INVALID();
        }
        try {
            MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
            if (handler != null) {
                setHandlerContext(handler, s.expressionsById);
            }
            try {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment data = input == null ? MemorySegment.NULL : allocateBytes(arena, input);
                    int length = input == null ? 4 : input.length;
                    return propagateHandlerFailure(hyperscan.hs_scan_stream(
                            nativeStream, data, length, 0, scratch,
                            handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL));
                }
            } finally {
                if (handler != null) {
                    clearHandlerContext();
                }
            }
        } finally {
            s.endOperation();
        }
    }

    @Override
    public int closeStreamRaw(DualStream stream, DualScanner scanner, DualByteMatchHandler handler) {
        if (stream == null) {
            return hyperscan.HS_INVALID();
        }
        PanamaStream s = (PanamaStream) stream;
        if (s.isClosed()) {
            return hyperscan.HS_INVALID();
        }
        MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
        if (handler != null) {
            setHandlerContext(handler, s.expressionsById);
        }
        try {
            return propagateHandlerFailure(s.closeNative(
                    scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, true));
        } finally {
            if (handler != null) {
                clearHandlerContext();
            }
        }
    }

    @Override
    public int resetStreamRaw(DualStream stream, DualScanner scanner, DualByteMatchHandler handler) {
        if (stream == null) {
            return hyperscan.HS_INVALID();
        }
        PanamaStream s = (PanamaStream) stream;
        MemorySegment nativeStream;
        try {
            nativeStream = s.beginOperation();
        } catch (IllegalStateException e) {
            return hyperscan.HS_INVALID();
        }
        try {
            MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
            if (handler != null) {
                setHandlerContext(handler, s.expressionsById);
            }
            try {
                return propagateHandlerFailure(hyperscan.hs_reset_stream(
                        nativeStream, 0, scratch,
                        handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL));
            } finally {
                if (handler != null) {
                    clearHandlerContext();
                }
            }
        } finally {
            s.endOperation();
        }
    }

    @Override
    public int copyStreamRaw(DualStream[] to, DualStream from) {
        if (from == null || to == null || to.length == 0) {
            return hyperscan.HS_INVALID();
        }
        PanamaStream src = (PanamaStream) from;
        MemorySegment source;
        try {
            source = src.beginOperation();
        } catch (IllegalStateException e) {
            return hyperscan.HS_INVALID();
        }
        try {
            acquireDatabaseStreamLease(src.databaseOwner);
            boolean transferred = false;
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment toOut = zeroAddressOut(arena);
                try {
                    int result = hyperscan.hs_copy_stream(toOut, source);
                    if (result == 0) {
                        MemorySegment copied = reinterpretHandle(toOut.get(ValueLayout.ADDRESS, 0));
                        try {
                            to[0] = new PanamaStream(copied, null, src.expressions, src.databaseOwner);
                            transferred = true;
                        } catch (RuntimeException | Error e) {
                            hyperscan.hs_close_stream(
                                    copied, MemorySegment.NULL, MemorySegment.NULL, MemorySegment.NULL);
                            throw e;
                        }
                    }
                    return result;
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            } finally {
                if (!transferred) {
                    releaseDatabaseStreamLease(src.databaseOwner);
                }
            }
        } finally {
            src.endOperation();
        }
    }

    @Override
    public int resetAndCopyStreamRaw(DualStream to, DualStream from, DualScanner scanner, DualByteMatchHandler handler) {
        if (to == null || from == null) {
            return hyperscan.HS_INVALID();
        }
        if (to == from) {
            return hyperscan.HS_INVALID();
        }
        PanamaStream toStream = (PanamaStream) to;
        PanamaStream fromStream = (PanamaStream) from;
        MemorySegment destination;
        try {
            destination = toStream.beginOperation();
        } catch (IllegalStateException e) {
            return hyperscan.HS_INVALID();
        }
        MemorySegment source;
        try {
            source = fromStream.beginOperation();
        } catch (IllegalStateException e) {
            toStream.endOperation();
            return hyperscan.HS_INVALID();
        }
        try {
            MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
            if (handler != null) {
                setHandlerContext(handler, toStream.expressionsById);
            }
            try {
                return propagateHandlerFailure(hyperscan.hs_reset_and_copy_stream(
                        destination, source, scratch,
                        handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL));
            } finally {
                if (handler != null) {
                    clearHandlerContext();
                }
            }
        } finally {
            try {
                fromStream.endOperation();
            } finally {
                toStream.endOperation();
            }
        }
    }

    @Override
    public int scanRaw(DualScanner scanner, DualDatabase database, byte[] input, DualByteMatchHandler handler) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        try (PanamaDatabaseOperation operation = acquireDatabaseOperation(database)) {
            MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
            List<DualExpression> expressions = database instanceof PanamaNativeDatabase nativeDb
                    ? nativeDb.expressions() : List.of();
            if (handler != null) {
                setHandlerContext(handler, buildExpressionLookup(expressions));
            }
            try {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment data = input == null ? MemorySegment.NULL : allocateBytes(arena, input);
                    int length = input == null ? 4 : input.length;
                    return propagateHandlerFailure(hyperscan.hs_scan(
                            operation.database, data, length, 0, scratch,
                            handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL));
                }
            } finally {
                if (handler != null) {
                    clearHandlerContext();
                }
            }
        }
    }

    @Override
    public int scanVectorRaw(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        try (PanamaDatabaseOperation operation = acquireDatabaseOperation(database)) {
            MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
            List<DualExpression> expressions = database instanceof PanamaNativeDatabase nativeDb
                    ? nativeDb.expressions() : List.of();
            if (handler != null) {
                setHandlerContext(handler, buildExpressionLookup(expressions));
            }
            try {
                try (Arena arena = Arena.ofConfined()) {
                    if (input == null) {
                        return propagateHandlerFailure(hyperscan.hs_scan_vector(
                                operation.database, MemorySegment.NULL, MemorySegment.NULL,
                                2, 0, scratch,
                                handler == null ? MemorySegment.NULL : MATCH_HANDLER,
                                MemorySegment.NULL));
                    }
                    MemorySegment dataPtrs = arena.allocate(input.length * ValueLayout.ADDRESS.byteSize());
                    MemorySegment lengths = arena.allocate(input.length * ValueLayout.JAVA_INT.byteSize());
                    for (int i = 0; i < input.length; i++) {
                        byte[] data = input[i];
                        MemorySegment dataSeg = data == null ? MemorySegment.NULL : allocateBytes(arena, data);
                        dataPtrs.setAtIndex(ValueLayout.ADDRESS, i, dataSeg);
                        lengths.setAtIndex(ValueLayout.JAVA_INT, i, data == null ? 4 : data.length);
                    }
                    return propagateHandlerFailure(hyperscan.hs_scan_vector(
                            operation.database, dataPtrs, lengths, input.length, 0, scratch,
                            handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL));
                }
            } finally {
                if (handler != null) {
                    clearHandlerContext();
                }
            }
        }
    }

    @Override
    public int scanVectorNoLenArrayRaw(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        try (PanamaDatabaseOperation operation = acquireDatabaseOperation(database)) {
            MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
            List<DualExpression> expressions = database instanceof PanamaNativeDatabase nativeDb
                    ? nativeDb.expressions() : List.of();
            if (handler != null) {
                setHandlerContext(handler, buildExpressionLookup(expressions));
            }
            try {
                try (Arena arena = Arena.ofConfined()) {
                    if (input == null) {
                        return propagateHandlerFailure(hyperscan.hs_scan_vector(
                                operation.database, MemorySegment.NULL, MemorySegment.NULL,
                                2, 0, scratch,
                                handler == null ? MemorySegment.NULL : MATCH_HANDLER,
                                MemorySegment.NULL));
                    }
                    MemorySegment dataPtrs = arena.allocate(input.length * ValueLayout.ADDRESS.byteSize());
                    for (int i = 0; i < input.length; i++) {
                        byte[] data = input[i];
                        MemorySegment dataSeg = data == null ? MemorySegment.NULL : allocateBytes(arena, data);
                        dataPtrs.setAtIndex(ValueLayout.ADDRESS, i, dataSeg);
                    }
                    return propagateHandlerFailure(hyperscan.hs_scan_vector(
                            operation.database, dataPtrs, MemorySegment.NULL,
                            input.length, 0, scratch,
                            handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL));
                }
            } finally {
                if (handler != null) {
                    clearHandlerContext();
                }
            }
        }
    }

    @Override
    public int databaseSizeNullOutput(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        return hyperscan.hs_database_size(nativeDatabase(database), MemorySegment.NULL);
    }

    @Override
    public int databaseInfoNullOutput(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        return hyperscan.hs_database_info(nativeDatabase(database), MemorySegment.NULL);
    }

    @Override
    public int freeDatabaseRaw(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_SUCCESS();
        }
        if (database instanceof PanamaNativeDatabase nativeDb) {
            return nativeDb.free();
        }
        if (database instanceof PanamaRawDatabase) {
            return hyperscan.HS_INVALID();
        }
        if (database instanceof PanamaDatabase wrapper) {
            wrapper.close();
            return hyperscan.HS_SUCCESS();
        }
        return hyperscan.HS_INVALID();
    }

    @Override
    public int freeScratchRaw(DualScanner scanner) {
        if (scanner == null) {
            return hyperscan.HS_SUCCESS();
        }
        if (scanner instanceof PanamaRawScanner raw) {
            return raw.free();
        }
        return hyperscan.HS_INVALID();
    }

    @Override
    public int freeCompileErrorRaw(Object compileError) {
        if (compileError == null) {
            return hyperscan.HS_SUCCESS();
        }
        return hyperscan.hs_free_compile_error((MemorySegment) compileError);
    }

    @Override
    public DualResult<String> expressionInfoRaw(String pattern, EnumSet<DualExpressionFlag> flags) {
        return expressionInfoInternal(pattern, flags, false, false);
    }

    @Override
    public DualResult<String> expressionInfoNullInfoRaw(String pattern, EnumSet<DualExpressionFlag> flags) {
        return expressionInfoInternal(pattern, flags, true, false);
    }

    @Override
    public DualResult<String> expressionInfoNullErrRaw(String pattern, EnumSet<DualExpressionFlag> flags) {
        return expressionInfoInternal(pattern, flags, false, true);
    }

    private static DualResult<String> expressionInfoInternal(String pattern, EnumSet<DualExpressionFlag> flags, boolean nullInfo, boolean nullErr) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expr = pattern == null ? MemorySegment.NULL : arena.allocateFrom(pattern);
            MemorySegment infoOut = nullInfo ? MemorySegment.NULL : zeroAddressOut(arena);
            MemorySegment errOut = nullErr ? MemorySegment.NULL : zeroAddressOut(arena);
            int result = hyperscan.hs_expression_info(expr, toFlagBits(flags), infoOut, errOut);
            MemorySegment err = nullErr ? MemorySegment.NULL : (result == 0 ? MemorySegment.NULL : reinterpretCompileError(errOut.get(ValueLayout.ADDRESS, 0)));
            MemorySegment info = nullInfo ? MemorySegment.NULL : reinterpretString(infoOut.get(ValueLayout.ADDRESS, 0));
            try {
                return new DualResult<>(result, null, readCompileErrorMessage(err));
            } finally {
                try {
                    if (err != null && err.address() != 0) {
                        hyperscan.hs_free_compile_error(err);
                    }
                } finally {
                    freeMiscSegment(info);
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualResult<String> expressionExtInfoRaw(String pattern, EnumSet<DualExpressionFlag> flags) {
        return expressionExtInfoInternal(pattern, flags, false, false);
    }

    @Override
    public DualResult<String> expressionExtInfoNullInfoRaw(String pattern, EnumSet<DualExpressionFlag> flags) {
        return expressionExtInfoInternal(pattern, flags, true, false);
    }

    @Override
    public DualResult<String> expressionExtInfoNullErrRaw(String pattern, EnumSet<DualExpressionFlag> flags) {
        return expressionExtInfoInternal(pattern, flags, false, true);
    }

    private static DualResult<String> expressionExtInfoInternal(String pattern, EnumSet<DualExpressionFlag> flags, boolean nullInfo, boolean nullErr) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expr = pattern == null ? MemorySegment.NULL : arena.allocateFrom(pattern);
            MemorySegment infoOut = nullInfo ? MemorySegment.NULL : zeroAddressOut(arena);
            MemorySegment errOut = nullErr ? MemorySegment.NULL : zeroAddressOut(arena);
            int result = hyperscan.hs_expression_ext_info(expr, toFlagBits(flags), MemorySegment.NULL, infoOut, errOut);
            MemorySegment err = nullErr ? MemorySegment.NULL : (result == 0 ? MemorySegment.NULL : reinterpretCompileError(errOut.get(ValueLayout.ADDRESS, 0)));
            MemorySegment info = nullInfo ? MemorySegment.NULL : reinterpretString(infoOut.get(ValueLayout.ADDRESS, 0));
            try {
                return new DualResult<>(result, null, readCompileErrorMessage(err));
            } finally {
                try {
                    if (err != null && err.address() != 0) {
                        hyperscan.hs_free_compile_error(err);
                    }
                } finally {
                    freeMiscSegment(info);
                }
            }
        }
    }

    @Override
    public DualResult<DualExpressionInfo> expressionInfoDataRaw(String pattern, EnumSet<DualExpressionFlag> flags) {
        return expressionInfoDataInternal(pattern, flags, false);
    }

    @Override
    public DualResult<DualExpressionInfo> expressionExtInfoDataRaw(String pattern, EnumSet<DualExpressionFlag> flags, DualExpressionExt ext) {
        return expressionExtInfoDataInternal(pattern, flags, ext, false);
    }

    private static DualResult<DualExpressionInfo> expressionInfoDataInternal(String pattern, EnumSet<DualExpressionFlag> flags, boolean nullErr) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expr = pattern == null ? MemorySegment.NULL : arena.allocateFrom(pattern);
            MemorySegment infoOut = zeroAddressOut(arena);
            MemorySegment errOut = nullErr ? MemorySegment.NULL : zeroAddressOut(arena);
            int result = hyperscan.hs_expression_info(expr, toFlagBits(flags), infoOut, errOut);
            MemorySegment err = nullErr ? MemorySegment.NULL : (result == 0 ? MemorySegment.NULL : reinterpretCompileError(errOut.get(ValueLayout.ADDRESS, 0)));
            MemorySegment info = reinterpretExprInfo(infoOut.get(ValueLayout.ADDRESS, 0));
            try {
                DualExpressionInfo value = null;
                if (result == 0 && info != null && info.address() != 0) {
                    value = new DualExpressionInfo(
                            Integer.toUnsignedLong(hs_expr_info.min_width(info)),
                            Integer.toUnsignedLong(hs_expr_info.max_width(info)),
                            hs_expr_info.unordered_matches(info) != 0,
                            hs_expr_info.matches_at_eod(info) != 0,
                            hs_expr_info.matches_only_at_eod(info) != 0);
                }
                return new DualResult<>(result, value, readCompileErrorMessage(err));
            } finally {
                try {
                    if (err != null && err.address() != 0) {
                        hyperscan.hs_free_compile_error(err);
                    }
                } finally {
                    freeMiscSegment(info);
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static DualResult<DualExpressionInfo> expressionExtInfoDataInternal(String pattern, EnumSet<DualExpressionFlag> flags, DualExpressionExt ext, boolean nullErr) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expr = pattern == null ? MemorySegment.NULL : arena.allocateFrom(pattern);
            MemorySegment extSeg = MemorySegment.NULL;
            if (ext != null) {
                extSeg = arena.allocate(hs_expr_ext.layout());
                applyExprExt(extSeg, ext);
            }
            MemorySegment infoOut = zeroAddressOut(arena);
            MemorySegment errOut = nullErr ? MemorySegment.NULL : zeroAddressOut(arena);
            int result = hyperscan.hs_expression_ext_info(expr, toFlagBits(flags), extSeg, infoOut, errOut);
            MemorySegment err = nullErr ? MemorySegment.NULL : (result == 0 ? MemorySegment.NULL : reinterpretCompileError(errOut.get(ValueLayout.ADDRESS, 0)));
            MemorySegment info = reinterpretExprInfo(infoOut.get(ValueLayout.ADDRESS, 0));
            try {
                DualExpressionInfo value = null;
                if (result == 0 && info != null && info.address() != 0) {
                    value = new DualExpressionInfo(
                            Integer.toUnsignedLong(hs_expr_info.min_width(info)),
                            Integer.toUnsignedLong(hs_expr_info.max_width(info)),
                            hs_expr_info.unordered_matches(info) != 0,
                            hs_expr_info.matches_at_eod(info) != 0,
                            hs_expr_info.matches_only_at_eod(info) != 0);
                }
                return new DualResult<>(result, value, readCompileErrorMessage(err));
            } finally {
                try {
                    if (err != null && err.address() != 0) {
                        hyperscan.hs_free_compile_error(err);
                    }
                } finally {
                    freeMiscSegment(info);
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int populatePlatformRaw() {
        return hyperscan.hs_populate_platform(MemorySegment.NULL);
    }

    private static MemorySegment nativeDatabase(DualDatabase database) {
        if (database instanceof PanamaDatabase wrapper) {
            return getNativeDatabaseHandle(wrapper.database());
        }
        if (database instanceof PanamaNativeDatabase nativeDb) {
            return nativeDb.requireDatabase();
        }
        if (database instanceof PanamaRawDatabase rawDb) {
            return rawDb.requireDatabase();
        }
        throw new IllegalArgumentException("Unsupported database type: " + database.getClass());
    }

    private static final MethodHandle DB_GET_DATABASE;
    private static final MethodHandle STATE_GET_SCRATCH;

    static {
        try {
            Method m = com.xenoamess.hyperscan_panama.wrapper.Database.class.getDeclaredMethod("getDatabase");
            m.setAccessible(true);
            DB_GET_DATABASE = MethodHandles.lookup().unreflect(m);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            Field stateField = Scanner.class.getDeclaredField("state");
            stateField.setAccessible(true);
            Class<?> stateClass = stateField.getType();
            Method getScratchMethod = stateClass.getDeclaredMethod("getScratch");
            getScratchMethod.setAccessible(true);
            STATE_GET_SCRATCH = MethodHandles.lookup().unreflect(getScratchMethod);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static MemorySegment getNativeDatabaseHandle(com.xenoamess.hyperscan_panama.wrapper.Database database) {
        try {
            return (MemorySegment) DB_GET_DATABASE.invoke(database);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static MemorySegment nativeScratch(DualScanner scanner) {
        if (scanner == null) {
            return MemorySegment.NULL;
        }
        if (scanner instanceof PanamaRawScanner raw) {
            return raw.requireScratch();
        }
        if (scanner instanceof PanamaScanner wrapper) {
            wrapper.requireOpen();
            if (wrapper.nativeScratch != null) {
                return wrapper.nativeScratch.require();
            }
            return getNativeScratchHandle(wrapper.scanner);
        }
        throw new IllegalArgumentException("Unsupported scanner type: " + scanner.getClass());
    }

    private static MemorySegment streamScratch(DualScanner scanner, PanamaStream stream) {
        return stream.scratchState == null ? nativeScratch(scanner) : stream.scratch();
    }

    private static PanamaVectorScratch acquireVectorScratch(DualScanner scanner, MemorySegment database) {
        if (scanner instanceof PanamaScanner wrapper) {
            return new PanamaVectorScratch(wrapper.reusableNativeScratch(database), false);
        }
        if (scanner != null) {
            return new PanamaVectorScratch(nativeScratch(scanner), false);
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment scratchOut = zeroAddressOut(arena);
            checkResult(hyperscan.hs_alloc_scratch(database, scratchOut));
            MemorySegment scratchAddress = scratchOut.get(ValueLayout.ADDRESS, 0);
            try {
                return new PanamaVectorScratch(reinterpretHandle(scratchAddress), true);
            } catch (RuntimeException | Error e) {
                hyperscan.hs_free_scratch(scratchAddress);
                throw e;
            }
        }
    }

    private static final class PanamaVectorScratch implements AutoCloseable {
        private final MemorySegment scratch;
        private final boolean owner;

        private PanamaVectorScratch(MemorySegment scratch, boolean owner) {
            this.scratch = scratch;
            this.owner = owner;
        }

        @Override
        public void close() {
            if (owner) {
                checkResult(hyperscan.hs_free_scratch(scratch));
            }
        }
    }

    private static MemorySegment getNativeScratchHandle(Scanner scanner) {
        try {
            Field stateField = Scanner.class.getDeclaredField("state");
            stateField.setAccessible(true);
            Object state = stateField.get(scanner);
            return (MemorySegment) STATE_GET_SCRATCH.invoke(state);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static MemorySegment nativeStream(DualStream stream) {
        if (stream == null) {
            return MemorySegment.NULL;
        }
        if (stream instanceof PanamaStream s) {
            return s.requireOpen();
        }
        throw new IllegalArgumentException("Unsupported stream type: " + stream.getClass());
    }

    private static int toPanamaMode(DualMode mode) {
        return switch (mode) {
            case BLOCK -> hyperscan.HS_MODE_BLOCK();
            case STREAM -> hyperscan.HS_MODE_STREAM();
            case VECTORED -> hyperscan.HS_MODE_VECTORED();
        };
    }

    private static boolean hasFlag(List<DualExpression> expressions, DualExpressionFlag flag) {
        for (DualExpression expr : expressions) {
            if (expr.flags().contains(flag)) {
                return true;
            }
        }
        return false;
    }

    private static int toFlagBits(EnumSet<DualExpressionFlag> flags) {
        int bits = 0;
        for (DualExpressionFlag flag : flags) {
            bits |= switch (flag) {
                case CASELESS -> hyperscan.HS_FLAG_CASELESS();
                case DOTALL -> hyperscan.HS_FLAG_DOTALL();
                case MULTILINE -> hyperscan.HS_FLAG_MULTILINE();
                case SINGLEMATCH -> hyperscan.HS_FLAG_SINGLEMATCH();
                case ALLOWEMPTY -> hyperscan.HS_FLAG_ALLOWEMPTY();
                case UTF8 -> hyperscan.HS_FLAG_UTF8();
                case UCP -> hyperscan.HS_FLAG_UCP();
                case PREFILTER -> hyperscan.HS_FLAG_PREFILTER();
                case SOM_LEFTMOST -> hyperscan.HS_FLAG_SOM_LEFTMOST();
                case COMBINATION -> hyperscan.HS_FLAG_COMBINATION();
                case QUIET -> hyperscan.HS_FLAG_QUIET();
            };
        }
        return bits;
    }

    private static void checkResult(int result) {
        if (result != 0) {
            throw new RuntimeException("Hyperscan error " + result);
        }
    }

    private static String databaseInfo(MemorySegment database) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment infoOut = zeroAddressOut(arena);
            checkResult(hyperscan.hs_database_info(database, infoOut));
            MemorySegment info = reinterpretString(infoOut.get(ValueLayout.ADDRESS, 0));
            try {
                return info.getString(0);
            } finally {
                freeMiscSegment(info);
            }
        }
    }

    private static String readCompileErrorMessage(MemorySegment err) {
        if (err == null || err.address() == 0) {
            return null;
        }
        MemorySegment msg = hs_compile_error.message(err);
        if (msg == null || msg.address() == 0) {
            return null;
        }
        return reinterpretString(msg).getString(0);
    }

    private static MemorySegment newDefaultExprExt(Arena arena) {
        MemorySegment ext = arena.allocate(hs_expr_ext.layout());
        initDefaultExprExt(ext);
        return ext;
    }

    private static void initDefaultExprExt(MemorySegment ext) {
        hs_expr_ext.flags(ext, 0L);
        hs_expr_ext.min_offset(ext, 0L);
        hs_expr_ext.max_offset(ext, -1L);
        hs_expr_ext.min_length(ext, 0L);
        hs_expr_ext.edit_distance(ext, 0);
        hs_expr_ext.hamming_distance(ext, 0);
    }

    private static void applyExprExt(MemorySegment ext, DualExpressionExt src) {
        hs_expr_ext.flags(ext, src.flags());
        hs_expr_ext.min_offset(ext, src.minOffset());
        hs_expr_ext.max_offset(ext, src.maxOffset());
        hs_expr_ext.min_length(ext, src.minLength());
        hs_expr_ext.edit_distance(ext, src.editDistance());
        hs_expr_ext.hamming_distance(ext, src.hammingDistance());
    }

    private static Expression toPanamaExpression(DualExpression expr) {
        return new Expression(expr.pattern(), toPanamaFlags(expr.flags()), expr.id());
    }

    private static EnumSet<ExpressionFlag> toPanamaFlags(EnumSet<DualExpressionFlag> flags) {
        EnumSet<ExpressionFlag> result = EnumSet.noneOf(ExpressionFlag.class);
        for (DualExpressionFlag flag : flags) {
            result.add(toPanamaFlag(flag));
        }
        return result;
    }

    private static ExpressionFlag toPanamaFlag(DualExpressionFlag flag) {
        return switch (flag) {
            case CASELESS -> ExpressionFlag.CASELESS;
            case DOTALL -> ExpressionFlag.DOTALL;
            case MULTILINE -> ExpressionFlag.MULTILINE;
            case SINGLEMATCH -> ExpressionFlag.SINGLEMATCH;
            case ALLOWEMPTY -> ExpressionFlag.ALLOWEMPTY;
            case UTF8 -> ExpressionFlag.UTF8;
            case UCP -> ExpressionFlag.UCP;
            case PREFILTER -> ExpressionFlag.PREFILTER;
            case SOM_LEFTMOST -> ExpressionFlag.SOM_LEFTMOST;
            case COMBINATION -> ExpressionFlag.COMBINATION;
            case QUIET -> ExpressionFlag.QUIET;
        };
    }

    private static DualExpression toDualExpression(Expression expr) {
        EnumSet<DualExpressionFlag> flags = EnumSet.noneOf(DualExpressionFlag.class);
        for (ExpressionFlag flag : expr.getFlags()) {
            if (flag == ExpressionFlag.NO_FLAG) {
                continue;
            }
            flags.add(fromPanamaFlag(flag));
        }
        return new DualExpression(expr.getExpression(), flags, expr.getId());
    }

    private static DualExpressionFlag fromPanamaFlag(ExpressionFlag flag) {
        return switch (flag) {
            case CASELESS -> DualExpressionFlag.CASELESS;
            case DOTALL -> DualExpressionFlag.DOTALL;
            case MULTILINE -> DualExpressionFlag.MULTILINE;
            case SINGLEMATCH -> DualExpressionFlag.SINGLEMATCH;
            case ALLOWEMPTY -> DualExpressionFlag.ALLOWEMPTY;
            case UTF8 -> DualExpressionFlag.UTF8;
            case UCP -> DualExpressionFlag.UCP;
            case PREFILTER -> DualExpressionFlag.PREFILTER;
            case SOM_LEFTMOST -> DualExpressionFlag.SOM_LEFTMOST;
            case COMBINATION -> DualExpressionFlag.COMBINATION;
            case QUIET -> DualExpressionFlag.QUIET;
            default -> throw new IllegalArgumentException("Unsupported flag: " + flag);
        };
    }

    private record PanamaDatabase(com.xenoamess.hyperscan_panama.wrapper.Database database) implements DualDatabase {
        @Override
        public long getSize() {
            return database.getSize();
        }

        @Override
        public void close() {
            closeDatabaseWhenUnused(this, database::close);
        }
    }

    private static final class PanamaScanner implements DualScanner {
        final Scanner scanner;
        PanamaScratchState nativeScratch;
        private boolean closed;
        private boolean wrapperClosed;

        PanamaScanner(Scanner scanner) {
            this.scanner = scanner;
        }

        synchronized void requireOpen() {
            if (closed) {
                throw new IllegalStateException("Scanner is already closed");
            }
        }

        synchronized MemorySegment ensureNativeScratch(MemorySegment database) {
            if (closed) {
                throw new IllegalStateException("Scanner is already closed");
            }
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment scratchOut = zeroAddressOut(arena);
                scratchOut.set(ValueLayout.ADDRESS, 0,
                        nativeScratch == null ? MemorySegment.NULL : nativeScratch.require());
                checkResult(hyperscan.hs_alloc_scratch(database, scratchOut));
                MemorySegment scratch = reinterpretHandle(scratchOut.get(ValueLayout.ADDRESS, 0));
                if (nativeScratch == null) {
                    nativeScratch = new PanamaScratchState(scratch);
                } else {
                    nativeScratch.replace(scratch);
                }
                return scratch;
            }
        }

        synchronized MemorySegment reusableNativeScratch(MemorySegment database) {
            requireOpen();
            return nativeScratch == null ? ensureNativeScratch(database) : nativeScratch.require();
        }

        @Override
        public synchronized long getSize() {
            requireOpen();
            if (nativeScratch != null) {
                try (Arena arena = Arena.ofConfined()) {
                    MemorySegment sizeOut = zeroLongOut(arena);
                    checkResult(hyperscan.hs_scratch_size(nativeScratch.require(), sizeOut));
                    return sizeOut.get(ValueLayout.JAVA_LONG, 0);
                }
            }
            return scanner.getSize();
        }

        @Override
        public synchronized void close() {
            if (closed && wrapperClosed && (nativeScratch == null || nativeScratch.isClosed())) {
                return;
            }
            closed = true;
            RuntimeException failure = null;
            if (!wrapperClosed) {
                try {
                    scanner.close();
                    wrapperClosed = true;
                } catch (IOException e) {
                    failure = new RuntimeException(e);
                }
            }
            if (nativeScratch != null) {
                try {
                    checkResult(nativeScratch.free());
                } catch (RuntimeException e) {
                    if (failure == null) {
                        failure = e;
                    } else {
                        failure.addSuppressed(e);
                    }
                }
            }
            if (failure != null) {
                throw failure;
            }
        }
    }

    private static final class PanamaNativeDatabase implements DualDatabase {
        private MemorySegment database;
        private final List<DualExpression> expressions;

        PanamaNativeDatabase(MemorySegment database, List<DualExpression> expressions) {
            this.database = database;
            this.expressions = expressions;
        }

        List<DualExpression> expressions() {
            return expressions;
        }

        synchronized MemorySegment requireDatabase() {
            if (database == null || database.address() == 0) {
                throw new IllegalStateException("Database is already closed");
            }
            return database;
        }

        synchronized int free() {
            return freeDatabaseWhenUnused(this, () -> {
                if (database == null || database.address() == 0) {
                    return hyperscan.HS_SUCCESS();
                }
                int result = hyperscan.hs_free_database(database);
                if (result == hyperscan.HS_SUCCESS()) {
                    database = MemorySegment.NULL;
                }
                return result;
            });
        }

        @Override
        public long getSize() {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment sizeOut = zeroLongOut(arena);
                checkResult(hyperscan.hs_database_size(requireDatabase(), sizeOut));
                return sizeOut.get(ValueLayout.JAVA_LONG, 0);
            }
        }

        @Override
        public void close() {
            checkResult(free());
        }
    }

    private static final class PanamaRawDatabaseState {
        private MemorySegment memory;
        private Arena arena;

        PanamaRawDatabaseState(MemorySegment memory, Arena arena) {
            this.memory = memory;
            this.arena = arena;
        }

        synchronized MemorySegment requireMemory() {
            if (arena == null || !arena.scope().isAlive()) {
                throw new IllegalStateException("Raw database memory is already closed");
            }
            return memory;
        }

        synchronized void close() {
            if (arena == null) {
                return;
            }
            arena.close();
            arena = null;
            memory = MemorySegment.NULL;
        }
    }

    private static final class PanamaRawDatabase implements DualDatabase {
        private MemorySegment database;
        final PanamaRawDatabaseState state;
        private final boolean owner;
        private boolean closed;

        PanamaRawDatabase(MemorySegment database, PanamaRawDatabaseState state, boolean owner) {
            this.database = database;
            this.state = state;
            this.owner = owner;
        }

        synchronized MemorySegment requireDatabase() {
            if (closed || database == null || database.address() == 0) {
                throw new IllegalStateException("Raw database view is already closed");
            }
            state.requireMemory();
            return database;
        }

        @Override
        public long getSize() {
            try (Arena local = Arena.ofConfined()) {
                MemorySegment sizeOut = zeroLongOut(local);
                checkResult(hyperscan.hs_database_size(requireDatabase(), sizeOut));
                return sizeOut.get(ValueLayout.JAVA_LONG, 0);
            }
        }

        @Override
        public synchronized void close() {
            if (closed) {
                return;
            }
            if (owner) {
                closeDatabaseWhenUnused(this, state::close);
            }
            database = MemorySegment.NULL;
            closed = true;
        }
    }

    private static final class PanamaScratchState {
        private MemorySegment scratch;

        PanamaScratchState(MemorySegment scratch) {
            this.scratch = scratch;
        }

        synchronized MemorySegment require() {
            if (scratch == null || scratch.address() == 0) {
                throw new IllegalStateException("Scratch space is already closed");
            }
            return scratch;
        }

        synchronized void replace(MemorySegment replacement) {
            scratch = replacement;
        }

        synchronized boolean isClosed() {
            return scratch == null || scratch.address() == 0;
        }

        synchronized int free() {
            if (scratch == null || scratch.address() == 0) {
                return hyperscan.HS_SUCCESS();
            }
            int result = hyperscan.hs_free_scratch(scratch);
            if (result == hyperscan.HS_SUCCESS()) {
                scratch = MemorySegment.NULL;
            }
            return result;
        }
    }

    private static final class PanamaRawScanner implements DualScanner {
        private final PanamaScratchState state;
        private final boolean owner;

        PanamaRawScanner(PanamaScratchState state, boolean owner) {
            this.state = state;
            this.owner = owner;
        }

        MemorySegment requireScratch() {
            return state.require();
        }

        void replace(MemorySegment scratch) {
            if (!owner) {
                throw new IllegalStateException("Cannot replace borrowed scratch");
            }
            state.replace(scratch);
        }

        boolean isOwner() {
            return owner;
        }

        int free() {
            return owner ? state.free() : hyperscan.HS_INVALID();
        }

        @Override
        public long getSize() {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment sizeOut = zeroLongOut(arena);
                checkResult(hyperscan.hs_scratch_size(requireScratch(), sizeOut));
                return sizeOut.get(ValueLayout.JAVA_LONG, 0);
            }
        }

        @Override
        public void close() {
            if (owner) {
                checkResult(state.free());
            }
        }
    }

    private static final class PanamaStream implements DualStream {
        private MemorySegment stream;
        final PanamaScratchState scratchState;
        final List<DualExpression> expressions;
        final DualExpression[] expressionsById;
        final DualDatabase databaseOwner;
        private boolean closed;
        private boolean operationInProgress;
        private boolean leaseReleased;

        PanamaStream(MemorySegment stream, PanamaScratchState scratchState,
                     List<DualExpression> expressions, DualDatabase databaseOwner) {
            this.stream = stream;
            this.scratchState = scratchState;
            this.expressions = expressions;
            this.expressionsById = buildExpressionLookup(expressions);
            this.databaseOwner = databaseOwner;
        }

        MemorySegment scratch() {
            return scratchState == null ? MemorySegment.NULL : scratchState.require();
        }

        synchronized MemorySegment requireOpen() {
            if (closed || stream == null || stream.address() == 0) {
                throw new IllegalStateException("Stream is already closed");
            }
            return stream;
        }

        synchronized MemorySegment beginOperation() {
            MemorySegment current = requireOpen();
            if (operationInProgress) {
                throw new IllegalStateException("Stream is in use by an active operation");
            }
            operationInProgress = true;
            return current;
        }

        synchronized void endOperation() {
            if (!operationInProgress) {
                throw new IllegalStateException("Stream operation lease is not held");
            }
            operationInProgress = false;
        }

        synchronized boolean isClosed() {
            return closed;
        }

        private void finishCleanup() {
            RuntimeException failure = null;
            if (scratchState != null) {
                try {
                    checkResult(scratchState.free());
                } catch (RuntimeException e) {
                    failure = e;
                }
            }
            if (!leaseReleased) {
                try {
                    releaseDatabaseStreamLease(databaseOwner);
                    leaseReleased = true;
                } catch (RuntimeException e) {
                    if (failure == null) {
                        failure = e;
                    } else {
                        failure.addSuppressed(e);
                    }
                }
            }
            if (failure != null) {
                throw failure;
            }
        }

        synchronized int closeNative(MemorySegment callbackScratch, MemorySegment handler,
                                     boolean invalidWhenClosed) {
            if (closed) {
                finishCleanup();
                return invalidWhenClosed ? hyperscan.HS_INVALID() : hyperscan.HS_SUCCESS();
            }
            if (operationInProgress) {
                return hyperscan.HS_INVALID();
            }
            operationInProgress = true;
            try {
                int result = hyperscan.hs_close_stream(stream, callbackScratch, handler, MemorySegment.NULL);
                if (result != hyperscan.HS_INVALID() && result != hyperscan.HS_SCRATCH_IN_USE()) {
                    closed = true;
                    stream = MemorySegment.NULL;
                    finishCleanup();
                }
                return result;
            } finally {
                operationInProgress = false;
            }
        }

        @Override
        public synchronized void close() {
            if (closed) {
                finishCleanup();
                return;
            }
            checkResult(closeNative(scratch(), MemorySegment.NULL, false));
        }
    }

    private record PanamaPatternFilter(PatternFilter filter) implements DualPatternFilter {
        @Override
        public List<Matcher> filter(String input) {
            return filter.filter(input);
        }

        @Override
        public void close() {
            try {
                filter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
