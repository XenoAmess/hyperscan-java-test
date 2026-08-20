package com.xenoamess.hyperscan.smoke.dual;

import com.gliwka.hyperscan.jni.hs_alloc_t;
import com.gliwka.hyperscan.jni.hs_compile_error_t;
import com.gliwka.hyperscan.jni.hs_database_t;
import com.gliwka.hyperscan.jni.hs_expr_ext_t;
import com.gliwka.hyperscan.jni.hs_expr_info_t;
import com.gliwka.hyperscan.jni.hs_free_t;
import com.gliwka.hyperscan.jni.hs_platform_info_t;
import com.gliwka.hyperscan.jni.hs_scratch_t;
import com.gliwka.hyperscan.jni.hs_stream_t;
import com.gliwka.hyperscan.jni.hyperscan;
import com.gliwka.hyperscan.jni.match_event_handler;
import com.gliwka.hyperscan.util.PatternFilter;
import com.gliwka.hyperscan.wrapper.ByteMatchEventHandler;
import com.gliwka.hyperscan.wrapper.CompileErrorException;
import com.gliwka.hyperscan.wrapper.Expression;
import com.gliwka.hyperscan.wrapper.ExpressionFlag;
import com.gliwka.hyperscan.wrapper.Match;
import com.gliwka.hyperscan.wrapper.StringMatchEventHandler;
import com.xenoamess.hyperscan.smoke.HyperscanTestHelper;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.SizeTPointer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.misc.Unsafe;

public class JavaCppAdapter implements DualApi {

    private static final Unsafe UNSAFE = getUnsafe();

    static {
        HyperscanTestHelper.loadNativeLibrary();
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

    private static final ThreadLocal<ByteBuffer> SCAN_BUFFER = ThreadLocal.withInitial(() -> ByteBuffer.allocateDirect(0));
    private static final ThreadLocal<HandlerContext> STREAM_CALLBACK = new ThreadLocal<>();
    private static final IdentityHashMap<Object, Integer> DATABASE_STREAM_LEASES = new IdentityHashMap<>();
    private static final IdentityHashMap<Object, Integer> DATABASE_OPERATION_LEASES = new IdentityHashMap<>();
    private static final Object ALLOCATOR_LOCK = new Object();

    private static final match_event_handler MATCH_HANDLER = new match_event_handler() {
        @Override
        public int call(int id, long from, long to, int flags, Pointer context) {
            HandlerContext ctx = STREAM_CALLBACK.get();
            try {
                if (ctx == null) {
                    return 0;
                }
                DualExpression expression = null;
                DualExpression[] byId = ctx.expressionsById();
                if (id >= 0 && id < byId.length) {
                    expression = byId[id];
                }
                if (expression == null) {
                    expression = new DualExpression("", EnumSet.noneOf(DualExpressionFlag.class), id);
                }
                return ctx.handler().onMatch(expression, from, to) ? 0 : -1;
            } catch (Throwable failure) {
                if (ctx != null && ctx.failure == null) {
                    ctx.failure = failure;
                }
                return -1;
            }
        }
    };

    private static hs_alloc_t currentAllocator;
    private static hs_free_t currentFree;
    private static DualAllocator currentDualAllocator;
    private static DualFree currentDualFree;
    private static hs_alloc_t currentDatabaseAllocator;
    private static hs_free_t currentDatabaseFree;
    private static DualAllocator currentDualDatabaseAllocator;
    private static DualFree currentDualDatabaseFree;
    private static hs_alloc_t currentMiscAllocator;
    private static hs_free_t currentMiscFree;
    private static DualAllocator currentDualMiscAllocator;
    private static DualFree currentDualMiscFree;
    private static hs_alloc_t currentScratchAllocator;
    private static hs_free_t currentScratchFree;
    private static DualAllocator currentDualScratchAllocator;
    private static DualFree currentDualScratchFree;
    private static hs_alloc_t currentStreamAllocator;
    private static hs_free_t currentStreamFree;
    private static DualAllocator currentDualStreamAllocator;
    private static DualFree currentDualStreamFree;
    private static DualFree effectiveMiscFree;

    private static final Arena HS_LIBRARY_ARENA = Arena.global();
    private static final SymbolLookup HS_LIBRARY_LOOKUP;

    static {
        try {
            HS_LIBRARY_LOOKUP = SymbolLookup.libraryLookup(findHsLibraryPath(), HS_LIBRARY_ARENA);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static String findHsLibraryPath() {
        String hsLibraryName = System.mapLibraryName("hs");
        String hsRuntimeName = System.mapLibraryName("hs_runtime");
        Map<String, String> loaded = Loader.getLoadedLibraries();
        for (Map.Entry<String, String> entry : loaded.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (matchesLibraryName(key, hsLibraryName, hsRuntimeName)) {
                return value;
            }
            if (matchesLibraryName(value, hsLibraryName, hsRuntimeName)) {
                return value;
            }
        }
        File cacheDir;
        try {
            cacheDir = Loader.getCacheDir();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String platform = Loader.getPlatform();
        File platformDir = new File(cacheDir, platform);
        File libhs = new File(platformDir, hsLibraryName);
        if (libhs.exists()) {
            return libhs.getAbsolutePath();
        }
        File[] cacheRoots = cacheDir.listFiles();
        if (cacheRoots != null) {
            for (File root : cacheRoots) {
                File found = findLibraryFile(root, hsLibraryName, hsRuntimeName);
                if (found != null) {
                    return found.getAbsolutePath();
                }
            }
        }
        throw new RuntimeException("Could not find " + hsLibraryName);
    }

    private static boolean matchesLibraryName(String path, String libraryName, String runtimeName) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        String name = path.contains(File.separator) ? new File(path).getName() : path;
        return name.equals(libraryName) && !name.equals(runtimeName);
    }

    private static File findLibraryFile(File file, String libraryName, String runtimeName) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    File found = findLibraryFile(child, libraryName, runtimeName);
                    if (found != null) {
                        return found;
                    }
                }
            }
        } else if (file.getName().equals(libraryName) && !file.getName().equals(runtimeName)) {
            return file;
        }
        return null;
    }

    private static void setHsLibraryAllocator(String name, DualAllocator alloc, DualFree free) {
        try {
            MemorySegment symbol = HS_LIBRARY_LOOKUP.find(name).orElse(null);
            if (symbol == null) {
                return;
            }
            FunctionDescriptor descriptor = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS);
            MethodHandle handle = Linker.nativeLinker().downcallHandle(symbol, descriptor);
            MemorySegment allocSeg;
            if (alloc == null) {
                allocSeg = MemorySegment.NULL;
            } else {
                allocSeg = com.xenoamess.hyperscan_panama.jni.generated.hs_alloc_t.allocate(size -> {
                    try {
                        long address = alloc.allocate(size);
                        if (address == 0) {
                            return MemorySegment.NULL;
                        }
                        return MemorySegment.ofAddress(address).reinterpret(size);
                    } catch (Throwable ignored) {
                        return MemorySegment.NULL;
                    }
                }, HS_LIBRARY_ARENA);
            }
            MemorySegment freeSeg;
            if (free == null) {
                freeSeg = MemorySegment.NULL;
            } else {
                freeSeg = com.xenoamess.hyperscan_panama.jni.generated.hs_free_t.allocate(ptr -> {
                    try {
                        if (ptr != null && ptr.address() != 0) {
                            free.free(ptr.address());
                        }
                    } catch (Throwable ignored) {
                        // Native free callbacks must not throw across the upcall boundary.
                    }
                }, HS_LIBRARY_ARENA);
            }
            int result = (int) handle.invokeExact(allocSeg, freeSeg);
            if (result != 0) {
                throw new RuntimeException(name + " failed: " + result);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static hs_alloc_t wrapAllocator(DualAllocator alloc) {
        if (alloc == null) {
            return null;
        }
        return new hs_alloc_t() {
            @Override
            public Pointer call(long size) {
                try {
                    long address = alloc.allocate(size);
                    if (address == 0) {
                        return null;
                    }
                    return new OffsetPointer(address);
                } catch (Throwable ignored) {
                    return null;
                }
            }
        };
    }

    private static hs_free_t wrapFree(DualFree free) {
        if (free == null) {
            return null;
        }
        return new hs_free_t() {
            @Override
            public void call(Pointer ptr) {
                try {
                    if (ptr != null) {
                        free.free(ptr.address());
                    }
                } catch (Throwable ignored) {
                    // Native free callbacks must not throw across the upcall boundary.
                }
            }
        };
    }

    @Override
    public void setAllocator(DualAllocator alloc, DualFree free) {
        synchronized (ALLOCATOR_LOCK) {
            currentAllocator = wrapAllocator(alloc);
            currentFree = wrapFree(free);
            currentDualAllocator = alloc;
            currentDualFree = free;
            checkResult(hyperscan.hs_set_allocator(currentAllocator, currentFree));
            setHsLibraryAllocator("hs_set_allocator", alloc, free);
            effectiveMiscFree = free;
        }
    }

    @Override
    public void setDatabaseAllocator(DualAllocator alloc, DualFree free) {
        synchronized (ALLOCATOR_LOCK) {
            currentDatabaseAllocator = wrapAllocator(alloc);
            currentDatabaseFree = wrapFree(free);
            currentDualDatabaseAllocator = alloc;
            currentDualDatabaseFree = free;
            checkResult(hyperscan.hs_set_database_allocator(currentDatabaseAllocator, currentDatabaseFree));
            setHsLibraryAllocator("hs_set_database_allocator", alloc, free);
        }
    }

    @Override
    public void setMiscAllocator(DualAllocator alloc, DualFree free) {
        synchronized (ALLOCATOR_LOCK) {
            currentMiscAllocator = wrapAllocator(alloc);
            currentMiscFree = wrapFree(free);
            currentDualMiscAllocator = alloc;
            currentDualMiscFree = free;
            checkResult(hyperscan.hs_set_misc_allocator(currentMiscAllocator, currentMiscFree));
            setHsLibraryAllocator("hs_set_misc_allocator", alloc, free);
            effectiveMiscFree = free;
        }
    }

    @Override
    public void setScratchAllocator(DualAllocator alloc, DualFree free) {
        synchronized (ALLOCATOR_LOCK) {
            currentScratchAllocator = wrapAllocator(alloc);
            currentScratchFree = wrapFree(free);
            currentDualScratchAllocator = alloc;
            currentDualScratchFree = free;
            checkResult(hyperscan.hs_set_scratch_allocator(currentScratchAllocator, currentScratchFree));
            setHsLibraryAllocator("hs_set_scratch_allocator", alloc, free);
        }
    }

    @Override
    public void setStreamAllocator(DualAllocator alloc, DualFree free) {
        synchronized (ALLOCATOR_LOCK) {
            currentStreamAllocator = wrapAllocator(alloc);
            currentStreamFree = wrapFree(free);
            currentDualStreamAllocator = alloc;
            currentDualStreamFree = free;
            checkResult(hyperscan.hs_set_stream_allocator(currentStreamAllocator, currentStreamFree));
            setHsLibraryAllocator("hs_set_stream_allocator", alloc, free);
        }
    }

    private static MemorySegment toSegment(Pointer p) {
        return p == null ? MemorySegment.NULL : MemorySegment.ofAddress(p.address());
    }

    private static void freeMiscPointer(Pointer pointer) {
        if (pointer == null || pointer.address() == 0) {
            return;
        }
        DualFree free = effectiveMiscFree;
        if (free != null) {
            free.free(pointer.address());
        } else {
            Pointer.free(pointer);
        }
    }

    private static BytePointer utf8CString(String value) {
        return value == null ? new BytePointer() : new BytePointer(value, StandardCharsets.UTF_8);
    }

    private static void closePointers(List<? extends Pointer> pointers) {
        for (int i = pointers.size() - 1; i >= 0; i--) {
            Pointer pointer = pointers.get(i);
            if (pointer != null) {
                pointer.close();
            }
        }
    }

    private static Object databaseLeaseKey(DualDatabase database) {
        return database instanceof JavaCppRawDatabase raw ? raw.state : database;
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

    private static JavaCppDatabaseOperation acquireDatabaseOperation(DualDatabase database) {
        acquireDatabaseOperationLease(database);
        try {
            return new JavaCppDatabaseOperation(database, nativeDatabase(database));
        } catch (RuntimeException | Error e) {
            releaseDatabaseOperationLease(database);
            throw e;
        }
    }

    private static final class JavaCppDatabaseOperation implements AutoCloseable {
        final DualDatabase owner;
        final hs_database_t database;

        JavaCppDatabaseOperation(DualDatabase owner, hs_database_t database) {
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
            return hyperscan.HS_SUCCESS;
        });
    }

    private static String compileErrorMessage(hs_compile_error_t error) {
        return error != null && !error.isNull() && error.message() != null
                ? error.message().getString(StandardCharsets.UTF_8)
                : null;
    }

    private static void freeCompileErrorDirect(hs_compile_error_t error) {
        if (error == null || error.isNull()) {
            return;
        }
        try {
            int ignored = (int) HsLibrary.HS_FREE_COMPILE_ERROR.invokeExact(toSegment(error));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final class HsLibrary {
        private static MethodHandle lookup(String name, FunctionDescriptor desc) {
            MemorySegment symbol = HS_LIBRARY_LOOKUP.find(name).orElseThrow(() -> new RuntimeException("Symbol not found: " + name));
            return Linker.nativeLinker().downcallHandle(symbol, desc);
        }

        private static final MethodHandle HS_COMPILE = lookup("hs_compile",
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        private static final MethodHandle HS_COMPILE_MULTI = lookup("hs_compile_multi",
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        private static final MethodHandle HS_COMPILE_EXT_MULTI = lookup("hs_compile_ext_multi",
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        private static final MethodHandle HS_DATABASE_INFO = lookup("hs_database_info",
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        private static final MethodHandle HS_SERIALIZE_DATABASE = lookup("hs_serialize_database",
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        private static final MethodHandle HS_DESERIALIZE_DATABASE = lookup("hs_deserialize_database",
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));
        private static final MethodHandle HS_ALLOC_SCRATCH = lookup("hs_alloc_scratch",
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        private static final MethodHandle HS_EXPRESSION_INFO = lookup("hs_expression_info",
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
        private static final MethodHandle HS_FREE_COMPILE_ERROR = lookup("hs_free_compile_error",
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
        private static final MethodHandle HS_FREE_DATABASE = lookup("hs_free_database",
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
    }

    private static hs_expr_ext_t newDefaultExprExt() {
        hs_expr_ext_t ext = new hs_expr_ext_t();
        try {
            ext.flags(0L);
            ext.min_offset(0L);
            ext.max_offset(-1L);
            ext.min_length(0L);
            ext.edit_distance(0);
            ext.hamming_distance(0);
            return ext;
        } catch (RuntimeException | Error e) {
            ext.close();
            throw e;
        }
    }

    private static void applyExprExt(hs_expr_ext_t ext, DualExpressionExt src) {
        ext.flags(src.flags());
        ext.min_offset(src.minOffset());
        ext.max_offset(src.maxOffset());
        ext.min_length(src.minLength());
        ext.edit_distance(src.editDistance());
        ext.hamming_distance(src.hammingDistance());
    }

    private static final class HandlerContext {
        private final DualByteMatchHandler handler;
        private final DualExpression[] expressionsById;
        private final HandlerContext previous;
        private Throwable failure;

        private HandlerContext(DualByteMatchHandler handler, DualExpression[] expressionsById,
                               HandlerContext previous) {
            this.handler = handler;
            this.expressionsById = expressionsById;
            this.previous = previous;
        }

        private DualByteMatchHandler handler() {
            return handler;
        }

        private DualExpression[] expressionsById() {
            return expressionsById;
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

    private static HandlerContext newHandlerContext(DualByteMatchHandler handler, List<DualExpression> expressions) {
        return new HandlerContext(handler, buildExpressionLookup(expressions), STREAM_CALLBACK.get());
    }

    private static void restoreHandlerContext() {
        HandlerContext current = STREAM_CALLBACK.get();
        if (current != null && current.previous != null) {
            STREAM_CALLBACK.set(current.previous);
        } else {
            STREAM_CALLBACK.remove();
        }
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

    @Override
    public DualExpression createExpression(String pattern, EnumSet<DualExpressionFlag> flags, Integer id) {
        return new DualExpression(pattern, flags, id);
    }

    @Override
    public DualDatabase compileDatabase(List<DualExpression> expressions, DualMode mode) {
        if (mode == DualMode.BLOCK) {
            List<Expression> javaCppExpressions = new ArrayList<>();
            for (DualExpression expr : expressions) {
                javaCppExpressions.add(toJavaCppExpression(expr));
            }
            try {
                return new JavaCppWrapperDatabase(com.gliwka.hyperscan.wrapper.Database.compile(javaCppExpressions), expressions);
            } catch (CompileErrorException e) {
                throw new RuntimeException(e);
            }
        }
        int nativeMode = toJavaCppMode(mode);
        if (mode == DualMode.STREAM && hasFlag(expressions, DualExpressionFlag.SOM_LEFTMOST)) {
            nativeMode |= hyperscan.HS_MODE_SOM_HORIZON_LARGE;
        }
        return compileNative(expressions, nativeMode);
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
        JavaCppWrapperDatabase db = (JavaCppWrapperDatabase) database;
        try {
            Method method = com.gliwka.hyperscan.wrapper.Database.class.getDeclaredMethod("getExpression", int.class);
            method.setAccessible(true);
            Expression expr = (Expression) method.invoke(db.database, id);
            return expr == null ? null : toDualExpression(expr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualScanner createScanner() {
        return new JavaCppScanner(new com.gliwka.hyperscan.wrapper.Scanner());
    }

    @Override
    public void allocScratch(DualScanner scanner, DualDatabase database) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        s.requireOpen();
        if (database instanceof JavaCppWrapperDatabase db) {
            s.scanner.allocScratch(db.database);
        } else {
            s.ensureNativeScratch(nativeDatabase(database));
        }
    }

    @Override
    public List<DualMatch> scan(DualScanner scanner, DualDatabase database, String input) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        s.requireOpen();
        JavaCppWrapperDatabase db = (JavaCppWrapperDatabase) database;
        List<Match> matches;
        try (JavaCppDatabaseOperation ignored = acquireDatabaseOperation(database)) {
            matches = s.scanner.scan(db.database, input);
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
        JavaCppScanner s = (JavaCppScanner) scanner;
        s.requireOpen();
        JavaCppWrapperDatabase db = (JavaCppWrapperDatabase) database;
        Throwable[] callbackFailure = new Throwable[1];
        try (JavaCppDatabaseOperation ignored = acquireDatabaseOperation(database)) {
            s.scanner.scan(db.database, input, new StringMatchEventHandler() {
                @Override
                public boolean onMatch(Expression expression, long from, long to) {
                    try {
                        return handler.onMatch(toDualExpression(expression), from, to);
                    } catch (Throwable failure) {
                        callbackFailure[0] = failure;
                        return false;
                    }
                }
            });
        }
        if (callbackFailure[0] != null) {
            throwUnchecked(callbackFailure[0]);
        }
    }

    @Override
    public void scan(DualScanner scanner, DualDatabase database, byte[] input, DualByteMatchHandler handler) {
        if (database == null) {
            throw new IllegalArgumentException("Database is null");
        }
        try (JavaCppDatabaseOperation operation = acquireDatabaseOperation(database);
             BytePointer data = newBytePointer(input)) {
            hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
            if (scratch == null) {
                throw new IllegalStateException("Scratch space has already been deallocated");
            }
            List<DualExpression> expressions = expressionsOf(database);
            if (handler != null) {
                STREAM_CALLBACK.set(newHandlerContext(handler, expressions));
            }
            try {
                int length = input == null ? 4 : input.length;
                int result = propagateHandlerFailure(hyperscan.hs_scan(
                        operation.database, data, length, 0, scratch,
                        handler == null ? null : MATCH_HANDLER, null));
                if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED) {
                    checkResult(result);
                }
            } finally {
                if (handler != null) {
                    restoreHandlerContext();
                }
            }
        }
    }

    @Override
    public void scan(DualScanner scanner, DualDatabase database, ByteBuffer input, DualByteMatchHandler handler) {
        if (database == null) {
            throw new IllegalArgumentException("Database is null");
        }
        try (JavaCppDatabaseOperation operation = acquireDatabaseOperation(database);
             BytePointer data = newBytePointer(input)) {
            hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
            if (scratch == null) {
                throw new IllegalStateException("Scratch space has already been deallocated");
            }
            List<DualExpression> expressions = expressionsOf(database);
            if (handler != null) {
                STREAM_CALLBACK.set(newHandlerContext(handler, expressions));
            }
            try {
                int length = input == null ? 4 : input.remaining();
                int result = propagateHandlerFailure(hyperscan.hs_scan(
                        operation.database, data, length, 0, scratch,
                        handler == null ? null : MATCH_HANDLER, null));
                if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED) {
                    checkResult(result);
                }
            } finally {
                if (handler != null) {
                    restoreHandlerContext();
                }
            }
        }
    }

    @Override
    public boolean hasMatch(DualScanner scanner, DualDatabase database, String input) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        s.requireOpen();
        JavaCppWrapperDatabase db = (JavaCppWrapperDatabase) database;
        try (JavaCppDatabaseOperation ignored = acquireDatabaseOperation(database)) {
            return s.scanner.hasMatch(db.database, input);
        }
    }

    @Override
    public boolean hasMatch(DualScanner scanner, DualDatabase database, byte[] input) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        s.requireOpen();
        JavaCppWrapperDatabase db = (JavaCppWrapperDatabase) database;
        try (JavaCppDatabaseOperation ignored = acquireDatabaseOperation(database)) {
            return s.scanner.hasMatch(db.database, input);
        }
    }

    @Override
    public DualStream openStream(DualDatabase database) {
        acquireDatabaseStreamLease(database);
        boolean transferred = false;
        try {
            hs_database_t db = nativeDatabase(database);
            List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb
                    ? nativeDb.expressions : List.of();
            try (PointerPointer<hs_stream_t> streamOut = new PointerPointer<>(1)) {
                streamOut.put(0, (hs_stream_t) null);
                checkResult(hyperscan.hs_open_stream(db, 0, streamOut));
                hs_stream_t stream = streamOut.get(hs_stream_t.class);
                hs_scratch_t scratch = null;
                try {
                    try (PointerPointer<hs_scratch_t> scratchOut = new PointerPointer<>(1)) {
                        scratchOut.put(0, (hs_scratch_t) null);
                        checkResult(hyperscan.hs_alloc_scratch(db, scratchOut));
                        scratch = scratchOut.get(hs_scratch_t.class);
                        JavaCppStream result = new JavaCppStream(
                                stream, new JavaCppScratchState(scratch), expressions, database);
                        transferred = true;
                        return result;
                    }
                } catch (RuntimeException | Error e) {
                    if (scratch != null && !scratch.isNull()) {
                        hyperscan.hs_free_scratch(scratch);
                    }
                    hyperscan.hs_close_stream(stream, null, null, null);
                    throw e;
                }
            }
        } finally {
            if (!transferred) {
                releaseDatabaseStreamLease(database);
            }
        }
    }

    @Override
    public void scanStream(DualScanner scanner, DualStream stream, byte[] input, DualByteMatchHandler handler) {
        JavaCppStream s = (JavaCppStream) stream;
        hs_stream_t nativeStream = s.beginOperation();
        try (BytePointer data = newBytePointer(input)) {
            hs_scratch_t scratch = streamScratch(scanner, s);
            if (handler != null) {
                STREAM_CALLBACK.set(newHandlerContext(handler, s.expressions));
            }
            try {
                int length = input == null ? 4 : input.length;
                int result = propagateHandlerFailure(hyperscan.hs_scan_stream(
                        nativeStream, data, length, 0, scratch,
                        handler == null ? null : MATCH_HANDLER, null));
                if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED) {
                    checkResult(result);
                }
            } finally {
                if (handler != null) {
                    restoreHandlerContext();
                }
            }
        } finally {
            s.endOperation();
        }
    }

    @Override
    public void scanStream(DualScanner scanner, DualStream stream, ByteBuffer input, DualByteMatchHandler handler) {
        JavaCppStream s = (JavaCppStream) stream;
        hs_stream_t nativeStream = s.beginOperation();
        try (BytePointer data = newBytePointer(input)) {
            hs_scratch_t scratch = streamScratch(scanner, s);
            if (handler != null) {
                STREAM_CALLBACK.set(newHandlerContext(handler, s.expressions));
            }
            try {
                int length = input == null ? 4 : input.remaining();
                int result = propagateHandlerFailure(hyperscan.hs_scan_stream(
                        nativeStream, data, length, 0, scratch,
                        handler == null ? null : MATCH_HANDLER, null));
                if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED) {
                    checkResult(result);
                }
            } finally {
                if (handler != null) {
                    restoreHandlerContext();
                }
            }
        } finally {
            s.endOperation();
        }
    }

    @Override
    public void closeStream(DualScanner scanner, DualStream stream, DualByteMatchHandler handler) {
        JavaCppStream s = (JavaCppStream) stream;
        if (s.isClosed()) {
            s.close();
            return;
        }
        if (handler != null) {
            STREAM_CALLBACK.set(newHandlerContext(handler, s.expressions));
        }
        try {
            int result = propagateHandlerFailure(
                    s.closeNative(streamScratch(scanner, s),
                            handler == null ? null : MATCH_HANDLER, false));
            if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED) {
                checkResult(result);
            }
        } finally {
            if (handler != null) {
                restoreHandlerContext();
            }
        }
    }

    @Override
    public void scanVector(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler) {
        if (input == null) {
            throw new IllegalArgumentException("Input vector is null");
        }
        try (JavaCppDatabaseOperation operation = acquireDatabaseOperation(database);
             JavaCppVectorScratch scratchUse = acquireVectorScratch(scanner, operation.database)) {
            hs_database_t db = operation.database;
            hs_scratch_t scratch = scratchUse.scratch;
            List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb ? nativeDb.expressions : List.of();
            int[] lengths = new int[input.length];
            for (int i = 0; i < input.length; i++) {
                lengths[i] = input[i] == null ? 4 : input[i].length;
            }
            BytePointer[] pointers = new BytePointer[input.length];
            if (handler != null) {
                STREAM_CALLBACK.set(newHandlerContext(handler, expressions));
            }
            try {
                for (int i = 0; i < input.length; i++) {
                    pointers[i] = input[i] == null ? new BytePointer() : new BytePointer(input[i]);
                }
                try (PointerPointer<BytePointer> data = new PointerPointer<>(pointers);
                     IntPointer lengthPtr = new IntPointer(lengths)) {
                    int result = propagateHandlerFailure(hyperscan.hs_scan_vector(
                            db, data, lengthPtr, input.length, 0, scratch,
                            handler == null ? null : MATCH_HANDLER, null));
                    if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED) {
                        checkResult(result);
                    }
                }
            } finally {
                for (BytePointer pointer : pointers) {
                    if (pointer != null) {
                        pointer.close();
                    }
                }
                if (handler != null) {
                    restoreHandlerContext();
                }
            }
        }
    }

    @Override
    public void scanVector(DualScanner scanner, DualDatabase database, ByteBuffer[] input, DualByteMatchHandler handler) {
        try (JavaCppDatabaseOperation operation = acquireDatabaseOperation(database);
             JavaCppVectorScratch scratchUse = acquireVectorScratch(scanner, operation.database)) {
            hs_database_t db = operation.database;
            hs_scratch_t scratch = scratchUse.scratch;
            List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb ? nativeDb.expressions : List.of();
            int n = input == null ? 0 : input.length;
            int[] lengths = new int[n];
            BytePointer[] pointers = new BytePointer[n];
            if (handler != null) {
                STREAM_CALLBACK.set(newHandlerContext(handler, expressions));
            }
            try {
                for (int i = 0; i < n; i++) {
                    ByteBuffer buffer = input[i];
                    if (buffer == null) {
                        lengths[i] = 4;
                        pointers[i] = new BytePointer();
                    } else {
                        lengths[i] = buffer.remaining();
                        pointers[i] = newBytePointer(buffer);
                    }
                }
                try (PointerPointer<BytePointer> data = new PointerPointer<>(pointers);
                     IntPointer lengthPtr = new IntPointer(lengths)) {
                    int result = propagateHandlerFailure(hyperscan.hs_scan_vector(
                            db, data, lengthPtr, n, 0, scratch,
                            handler == null ? null : MATCH_HANDLER, null));
                    if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED) {
                        checkResult(result);
                    }
                }
            } finally {
                for (BytePointer bp : pointers) {
                    if (bp != null) {
                        bp.close();
                    }
                }
                if (handler != null) {
                    restoreHandlerContext();
                }
            }
        }
    }

    @Override
    public String getDatabaseInfo(DualDatabase database) {
        return databaseInfo(nativeDatabase(database));
    }

    @Override
    public String getSerializedDatabaseInfo(byte[] data) {
        try (BytePointer bp = new BytePointer(data);
             PointerPointer<BytePointer> info = new PointerPointer<>(1)) {
            info.put(0, (BytePointer) null);
            checkResult(hyperscan.hs_serialized_database_info(bp, data.length, info));
            BytePointer infoPtr = info.get(BytePointer.class);
            try {
                return infoPtr.getString(StandardCharsets.UTF_8);
            } finally {
                freeMiscPointer(infoPtr);
            }
        }
    }

    @Override
    public byte[] serialize(DualDatabase database) {
        if (database instanceof JavaCppWrapperDatabase wrapper) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                wrapper.database.save(out);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return out.toByteArray();
        }
        JavaCppNativeDatabase nativeDb = (JavaCppNativeDatabase) database;
        try (SizeTPointer size = new SizeTPointer(1);
             PointerPointer<BytePointer> bytesOut = new PointerPointer<>(1)) {
            size.put(0, 0);
            bytesOut.put(0, (BytePointer) null);
            checkResult(hyperscan.hs_serialize_database(nativeDb.requireDatabase(), bytesOut, size));
            long length = size.get();
            BytePointer bytes = bytesOut.get(BytePointer.class);
            try {
                bytes.capacity(length);
                java.nio.ByteBuffer buffer = bytes.asBuffer();
                byte[] out = new byte[(int) length];
                buffer.get(out);
                return out;
            } finally {
                freeMiscPointer(bytes);
            }
        }
    }

    @Override
    public DualDatabase deserialize(byte[] data) {
        try {
            return new JavaCppWrapperDatabase(com.gliwka.hyperscan.wrapper.Database.load(new ByteArrayInputStream(data)), List.of());
        } catch (Exception e) {
            return deserializeNative(data);
        }
    }

    private static DualDatabase deserializeNative(byte[] data) {
        try (BytePointer bp = new BytePointer(data);
             PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1)) {
            dbOut.put(0, (hs_database_t) null);
            checkResult(hyperscan.hs_deserialize_database(bp, data.length, dbOut));
            hs_database_t db = dbOut.get(hs_database_t.class);
            return new JavaCppNativeDatabase(db, List.of());
        }
    }

    @Override
    public void closeScanner(DualScanner scanner) {
        scanner.close();
    }

    @Override
    public void closeDatabase(DualDatabase database) {
        if (database instanceof JavaCppWrapperDatabase wrapper) {
            wrapper.close();
        } else if (database instanceof JavaCppNativeDatabase nativeDb) {
            nativeDb.close();
        } else if (database instanceof JavaCppRawDatabase rawDb) {
            rawDb.close();
        }
    }

    @Override
    public long getDatabaseSize(DualDatabase database) {
        if (database instanceof JavaCppWrapperDatabase wrapper) {
            return wrapper.database.getSize();
        }
        return database.getSize();
    }

    @Override
    public long getScannerSize(DualScanner scanner) {
        return scanner.getSize();
    }

    @Override
    public String getVersion() {
        return com.gliwka.hyperscan.wrapper.Scanner.getVersion();
    }

    @Override
    public String getPlatform() {
        try {
            Class<?> loader = Class.forName("com.gliwka.hyperscan.jni.HyperscanNativeLoader");
            Object platform = loader.getMethod("getLoadedPlatform").invoke(null);
            if (platform instanceof String && !((String) platform).isEmpty()) {
                return (String) platform;
            }
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
            // Upstream and older fork artifacts do not expose the selected tier.
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        String requestedPlatform = System.getProperty("org.bytedeco.javacpp.platform");
        if (requestedPlatform != null && !requestedPlatform.isBlank()) {
            return requestedPlatform;
        }
        return Loader.getPlatform();
    }

    @Override
    public boolean validate(DualExpression expression) {
        return toJavaCppExpression(expression).validate().isValid();
    }

    @Override
    public String getValidationError(DualExpression expression) {
        return toJavaCppExpression(expression).validate().getErrorMessage();
    }

    @Override
    public DualPatternFilter createPatternFilter(List<Pattern> patterns) {
        try {
            return new JavaCppPatternFilter(new PatternFilter(patterns));
        } catch (CompileErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int success() {
        return hyperscan.HS_SUCCESS;
    }

    @Override
    public int invalid() {
        return hyperscan.HS_INVALID;
    }

    @Override
    public int noMem() {
        return hyperscan.HS_NOMEM;
    }

    @Override
    public int badAlloc() {
        return hyperscan.HS_BAD_ALLOC;
    }

    @Override
    public int compilerError() {
        return hyperscan.HS_COMPILER_ERROR;
    }

    @Override
    public int dbVersionError() {
        return hyperscan.HS_DB_VERSION_ERROR;
    }

    @Override
    public int dbModeError() {
        return hyperscan.HS_DB_MODE_ERROR;
    }

    @Override
    public int dbPlatformError() {
        return hyperscan.HS_DB_PLATFORM_ERROR;
    }

    @Override
    public int insufficientSpace() {
        return hyperscan.HS_INSUFFICIENT_SPACE;
    }

    @Override
    public int scanTerminated() {
        return hyperscan.HS_SCAN_TERMINATED;
    }

    @Override
    public int scratchInUse() {
        return hyperscan.HS_SCRATCH_IN_USE;
    }

    @Override
    public int badAlign() {
        return hyperscan.HS_BAD_ALIGN;
    }

    @Override
    public int validPlatformRaw() {
        return hyperscan.hs_valid_platform();
    }

    @Override
    public int modeBlock() {
        return hyperscan.HS_MODE_BLOCK;
    }

    @Override
    public int modeStream() {
        return hyperscan.HS_MODE_STREAM;
    }

    @Override
    public int modeVectored() {
        return hyperscan.HS_MODE_VECTORED;
    }

    @Override
    public int modeSomHorizonLarge() {
        return hyperscan.HS_MODE_SOM_HORIZON_LARGE;
    }

    @Override
    public int modeSomHorizonMedium() {
        return hyperscan.HS_MODE_SOM_HORIZON_MEDIUM;
    }

    @Override
    public int modeSomHorizonSmall() {
        return hyperscan.HS_MODE_SOM_HORIZON_SMALL;
    }

    @Override
    public long offsetPastHorizon() {
        return -1L;
    }

    @Override
    public DualCompileResult compileRaw(String pattern, int flags, int mode) {
        try (BytePointer expr = utf8CString(pattern);
             PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
            dbOut.put(0, (hs_database_t) null);
            errOut.put(0, (hs_compile_error_t) null);
            int result = (int) HsLibrary.HS_COMPILE.invokeExact(toSegment(expr), flags, mode, MemorySegment.NULL, toSegment(dbOut), toSegment(errOut));
            return buildCompileResult(result, dbOut, errOut, pattern == null ? List.of() : List.of(new DualExpression(pattern, EnumSet.noneOf(DualExpressionFlag.class), 0)));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualCompileResult compileRaw(String pattern, int flags, int mode, DualPlatformInfo platform) {
        try (BytePointer expr = utf8CString(pattern);
             PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1);
             hs_platform_info_t plat = new hs_platform_info_t()) {
            dbOut.put(0, (hs_database_t) null);
            errOut.put(0, (hs_compile_error_t) null);
            plat.tune(platform.tune());
            plat.cpu_features(platform.cpuFeatures());
            int result = (int) HsLibrary.HS_COMPILE.invokeExact(toSegment(expr), flags, mode, toSegment(plat), toSegment(dbOut), toSegment(errOut));
            return buildCompileResult(result, dbOut, errOut, pattern == null ? List.of() : List.of(new DualExpression(pattern, EnumSet.noneOf(DualExpressionFlag.class), 0)));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualCompileResult compileExtRaw(String pattern, int flags, DualExpressionExt ext, int mode) {
        try (BytePointer expr = utf8CString(pattern);
             PointerPointer<BytePointer> exprPtr = new PointerPointer<>(1);
             IntPointer flagsPtr = new IntPointer(1);
             IntPointer idsPtr = new IntPointer(1);
             hs_expr_ext_t extStruct = new hs_expr_ext_t();
             PointerPointer<hs_expr_ext_t> extPtr = new PointerPointer<>(1);
             PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
            exprPtr.put(0, expr);
            flagsPtr.put(0, flags);
            idsPtr.put(0, 0);
            applyExprExt(extStruct, ext);
            extPtr.put(0, extStruct);
            dbOut.put(0, (hs_database_t) null);
            errOut.put(0, (hs_compile_error_t) null);
            int result = hyperscan.hs_compile_ext_multi(exprPtr, flagsPtr, idsPtr, extPtr, 1, mode, null, dbOut, errOut);
            return buildCompileResult(result, dbOut, errOut, pattern == null ? List.of() : List.of(new DualExpression(pattern, EnumSet.noneOf(DualExpressionFlag.class), 0)));
        }
    }

    private static DualCompileResult buildCompileResult(int result, PointerPointer<hs_database_t> dbOut, PointerPointer<hs_compile_error_t> errOut, List<DualExpression> expressions) {
        if (result == 0) {
            return new DualCompileResult(0, new JavaCppNativeDatabase(dbOut.get(hs_database_t.class), List.copyOf(expressions)), null);
        }
        hs_compile_error_t err = errOut.get(hs_compile_error_t.class);
        try {
            return new DualCompileResult(result, null, compileErrorMessage(err));
        } finally {
            freeCompileErrorDirect(err);
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
        PointerPointer<BytePointer> expressionsPtr = null;
        IntPointer flags = null;
        IntPointer ids = null;
        List<BytePointer> exprPointers = new ArrayList<>();
        try {
            if (n > 0) {
                expressionsPtr = new PointerPointer<>(n);
                flags = new IntPointer(n);
                ids = new IntPointer(n);
                for (int i = 0; i < n; i++) {
                    DualExpression expr = expressions.get(i);
                    BytePointer bp = utf8CString(expr.pattern());
                    exprPointers.add(bp);
                    expressionsPtr.put(i, bp);
                    flags.put(i, toFlagBits(expr.flags()));
                    ids.put(i, expr.id() != null ? expr.id() : 0);
                }
            }
            try (PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1);
                 PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
                dbOut.put(0, (hs_database_t) null);
                errOut.put(0, (hs_compile_error_t) null);
                int result = hyperscan.hs_compile_multi(expressionsPtr, flags, ids, n, mode, null, dbOut, errOut);
                return buildCompileResult(result, dbOut, errOut,
                        expressions == null ? List.of() : expressions);
            }
        } finally {
            closePointers(exprPointers);
            if (ids != null) {
                ids.close();
            }
            if (flags != null) {
                flags.close();
            }
            if (expressionsPtr != null) {
                expressionsPtr.close();
            }
        }
    }

    @Override
    public DualCompileResult compileRaw(String[] patterns, int[] flags, int[] ids, int mode) {
        int n = patterns == null ? 0 : patterns.length;
        PointerPointer<BytePointer> expressionsPtr = null;
        IntPointer flagsPtr = null;
        IntPointer idsPtr = null;
        List<BytePointer> exprPointers = new ArrayList<>();
        List<DualExpression> expressions = new ArrayList<>(n);
        try {
            if (patterns != null) {
                expressionsPtr = new PointerPointer<>(n);
                for (int i = 0; i < n; i++) {
                    BytePointer expr = utf8CString(patterns[i]);
                    exprPointers.add(expr);
                    expressionsPtr.put(i, expr);
                    expressions.add(new DualExpression(patterns[i] != null ? patterns[i] : "", EnumSet.noneOf(DualExpressionFlag.class), ids != null ? ids[i] : 0));
                }
                flagsPtr = flags == null ? null : new IntPointer(flags);
                idsPtr = ids == null ? null : new IntPointer(ids);
            }

            try (PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1);
                 PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
                dbOut.put(0, (hs_database_t) null);
                errOut.put(0, (hs_compile_error_t) null);
                int result = hyperscan.hs_compile_multi(expressionsPtr, flagsPtr, idsPtr, n, mode, null, dbOut, errOut);
                return buildCompileResult(result, dbOut, errOut, expressions);
            }
        } finally {
            closePointers(exprPointers);
            if (idsPtr != null) {
                idsPtr.close();
            }
            if (flagsPtr != null) {
                flagsPtr.close();
            }
            if (expressionsPtr != null) {
                expressionsPtr.close();
            }
        }
    }

    @Override
    public DualCompileResult compileNullOutputRaw(String pattern, int flags, int mode) {
        try (BytePointer expr = utf8CString(pattern);
             PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
            errOut.put(0, (hs_compile_error_t) null);
            int result = hyperscan.hs_compile(expr, flags, mode, null, (PointerPointer<hs_database_t>) null, errOut);
            hs_compile_error_t err = errOut.get(hs_compile_error_t.class);
            try {
                return new DualCompileResult(result, null, compileErrorMessage(err));
            } finally {
                hyperscan.hs_free_compile_error(err);
            }
        }
    }

    @Override
    public DualCompileResult compileMultiNullOutputRaw(String[] patterns, int[] flags, int[] ids, int mode) {
        int n = patterns == null ? 0 : patterns.length;
        PointerPointer<BytePointer> expressionsPtr = null;
        IntPointer flagsPtr = null;
        IntPointer idsPtr = null;
        List<BytePointer> exprPointers = new ArrayList<>();
        try {
            if (patterns != null) {
                expressionsPtr = new PointerPointer<>(n);
                for (int i = 0; i < n; i++) {
                    BytePointer expr = utf8CString(patterns[i]);
                    exprPointers.add(expr);
                    expressionsPtr.put(i, expr);
                }
                flagsPtr = flags == null ? null : new IntPointer(flags);
                idsPtr = ids == null ? null : new IntPointer(ids);
            }
            try (PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
                errOut.put(0, (hs_compile_error_t) null);
                int result = hyperscan.hs_compile_multi(expressionsPtr, flagsPtr, idsPtr, n, mode, null,
                        (PointerPointer<hs_database_t>) null, errOut);
                hs_compile_error_t err = errOut.get(hs_compile_error_t.class);
                try {
                    String message = err != null && err.message() != null
                            ? err.message().getString(StandardCharsets.UTF_8)
                            : null;
                    return new DualCompileResult(result, null, message);
                } finally {
                    hyperscan.hs_free_compile_error(err);
                }
            }
        } finally {
            closePointers(exprPointers);
            if (idsPtr != null) {
                idsPtr.close();
            }
            if (flagsPtr != null) {
                flagsPtr.close();
            }
            if (expressionsPtr != null) {
                expressionsPtr.close();
            }
        }
    }

    @Override
    public DualResult<DualDatabase> deserializeRaw(byte[] data) {
        if (data == null) {
            return DualResult.error(hyperscan.HS_INVALID);
        }
        try (BytePointer bp = new BytePointer(data.length);
             PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1)) {
            bp.put(data);
            bp.position(0);
            dbOut.put(0, (hs_database_t) null);
            int result = (int) HsLibrary.HS_DESERIALIZE_DATABASE.invokeExact(toSegment(bp), (long) data.length, toSegment(dbOut));
            if (result == 0) {
                return DualResult.success(new JavaCppNativeDatabase(dbOut.get(hs_database_t.class), List.of()));
            }
            return DualResult.error(result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualResult<DualDatabase> deserializeNullOutputRaw(byte[] data) {
        if (data == null) {
            return DualResult.error(hyperscan.HS_INVALID);
        }
        try (BytePointer bp = new BytePointer(data.length)) {
            bp.put(data);
            bp.position(0);
            int result = hyperscan.hs_deserialize_database(bp, data.length, (PointerPointer<hs_database_t>) null);
            return result == 0 ? DualResult.success(null) : DualResult.error(result);
        }
    }

    @Override
    public int deserializeAtRaw(byte[] data, DualDatabase database) {
        if (data == null) {
            return hyperscan.HS_INVALID;
        }
        hs_database_t db = database == null ? null : nativeDatabase(database);
        try (BytePointer bp = new BytePointer(data.length)) {
            bp.put(data);
            bp.position(0);
            return hyperscan.hs_deserialize_database_at(bp, data.length, db);
        }
    }

    @Override
    public DualDatabase allocateRawDatabase(long size) {
        BytePointer memory = new BytePointer(size);
        if ((memory.address() & 7L) != 0) {
            memory.close();
            throw new RuntimeException("Raw database memory is not 8-byte aligned");
        }
        hs_database_t database = new hs_database_t(memory);
        return new JavaCppRawDatabase(database, new JavaCppRawDatabaseState(memory), true);
    }

    private static final class OffsetPointer extends Pointer {
        OffsetPointer(long address) {
            this.address = address;
        }
    }

    @Override
    public DualDatabase offsetRawDatabase(DualDatabase database, long offset) {
        if (!(database instanceof JavaCppRawDatabase raw)) {
            throw new IllegalArgumentException("Not a raw database: " + database.getClass());
        }
        hs_database_t db = new hs_database_t(new OffsetPointer(raw.requireDatabase().address() + offset));
        return new JavaCppRawDatabase(db, raw.state, false);
    }

    @Override
    public DualResult<String> getDatabaseInfoRaw(DualDatabase database) {
        if (database == null) {
            return DualResult.error(hyperscan.HS_INVALID);
        }
        try (PointerPointer<BytePointer> info = new PointerPointer<>(1)) {
            info.put(0, (BytePointer) null);
            int result = (int) HsLibrary.HS_DATABASE_INFO.invokeExact(toSegment(nativeDatabase(database)), toSegment(info));
            if (result == 0) {
                BytePointer infoPtr = info.get(BytePointer.class);
                try {
                    return DualResult.success(infoPtr.getString(StandardCharsets.UTF_8));
                } finally {
                    freeMiscPointer(infoPtr);
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
            return DualResult.error(hyperscan.HS_INVALID);
        }
        try (BytePointer bp = new BytePointer(data.length);
             PointerPointer<BytePointer> info = new PointerPointer<>(1)) {
            bp.put(data);
            bp.position(0);
            info.put(0, (BytePointer) null);
            int result = hyperscan.hs_serialized_database_info(bp, data.length, info);
            if (result == 0) {
                BytePointer infoPtr = info.get(BytePointer.class);
                try {
                    return DualResult.success(infoPtr.getString(StandardCharsets.UTF_8));
                } finally {
                    freeMiscPointer(infoPtr);
                }
            }
            return DualResult.error(result);
        }
    }

    @Override
    public int serializedDatabaseInfoNullOutput(byte[] data) {
        if (data == null) {
            return hyperscan.HS_INVALID;
        }
        try (BytePointer bp = new BytePointer(data.length)) {
            bp.put(data);
            bp.position(0);
            return hyperscan.hs_serialized_database_info(bp, data.length, (PointerPointer<BytePointer>) null);
        }
    }

    @Override
    public DualResult<Long> getDatabaseSizeRaw(DualDatabase database) {
        if (database == null) {
            return DualResult.error(hyperscan.HS_INVALID);
        }
        try (SizeTPointer size = new SizeTPointer(1)) {
            int result = hyperscan.hs_database_size(nativeDatabase(database), size);
            return new DualResult<>(result, result == 0 ? size.get() : null, null);
        }
    }

    @Override
    public DualResult<Long> getSerializedDatabaseSizeRaw(byte[] data) {
        if (data == null) {
            return DualResult.error(hyperscan.HS_INVALID);
        }
        try (BytePointer bp = new BytePointer(data.length);
             SizeTPointer size = new SizeTPointer(1)) {
            bp.put(data);
            bp.position(0);
            int result = hyperscan.hs_serialized_database_size(bp, data.length, size);
            return new DualResult<>(result, result == 0 ? size.get() : null, null);
        }
    }

    @Override
    public int serializedDatabaseSizeNullOutput(byte[] data) {
        if (data == null) {
            return hyperscan.HS_INVALID;
        }
        try (BytePointer bp = new BytePointer(data.length)) {
            bp.put(data);
            bp.position(0);
            return hyperscan.hs_serialized_database_size(bp, data.length, (SizeTPointer) null);
        }
    }

    @Override
    public DualResult<Long> getStreamSizeRaw(DualDatabase database) {
        if (database == null) {
            return DualResult.error(hyperscan.HS_INVALID);
        }
        try (SizeTPointer size = new SizeTPointer(1)) {
            int result = hyperscan.hs_stream_size(nativeDatabase(database), size);
            return new DualResult<>(result, result == 0 ? size.get() : null, null);
        }
    }

    @Override
    public int streamSizeNullOutput(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID;
        }
        return hyperscan.hs_stream_size(nativeDatabase(database), (SizeTPointer) null);
    }

    @Override
    public DualResult<Long> getScratchSizeRaw(DualScanner scanner) {
        if (scanner == null) {
            return DualResult.error(hyperscan.HS_INVALID);
        }
        hs_scratch_t scratch = nativeScratch(scanner);
        try (SizeTPointer size = new SizeTPointer(1)) {
            int result = hyperscan.hs_scratch_size(scratch, size);
            return new DualResult<>(result, result == 0 ? size.get() : null, null);
        }
    }

    @Override
    public DualScratchResult allocScratchRaw(DualDatabase database) {
        if (database == null) {
            return new DualScratchResult(hyperscan.HS_INVALID, null, null);
        }
        try (PointerPointer<hs_scratch_t> scratchOut = new PointerPointer<>(1)) {
            scratchOut.put(0, (hs_scratch_t) null);
            int result = (int) HsLibrary.HS_ALLOC_SCRATCH.invokeExact(toSegment(nativeDatabase(database)), toSegment(scratchOut));
            if (result == 0) {
                return new DualScratchResult(0,
                        new JavaCppRawScanner(new JavaCppScratchState(scratchOut.get(hs_scratch_t.class)), true), null);
            }
            return new DualScratchResult(result, null, null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualScratchResult allocScratchRaw(DualDatabase database, DualScanner existingScratch) {
        if (database == null) {
            return new DualScratchResult(hyperscan.HS_INVALID, null, null);
        }
        if (existingScratch != null
                && (!(existingScratch instanceof JavaCppRawScanner raw) || !raw.isOwner())) {
            throw new IllegalArgumentException("Scratch is not an owning raw scratch");
        }
        try (PointerPointer<hs_scratch_t> scratchOut = new PointerPointer<>(1)) {
            scratchOut.put(0, existingScratch == null ? null : nativeScratch(existingScratch));
            int result = hyperscan.hs_alloc_scratch(nativeDatabase(database), scratchOut);
            if (result == 0) {
                hs_scratch_t scratch = scratchOut.get(hs_scratch_t.class);
                if (existingScratch instanceof JavaCppRawScanner raw) {
                    raw.replace(scratch);
                    return new DualScratchResult(0, raw, null);
                }
                if (existingScratch != null) {
                    throw new IllegalArgumentException("Unsupported scratch owner: " + existingScratch.getClass());
                }
                return new DualScratchResult(0,
                        new JavaCppRawScanner(new JavaCppScratchState(scratch), true), null);
            }
            return new DualScratchResult(result, null, null);
        }
    }

    @Override
    public int allocScratchNullOutput(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID;
        }
        return hyperscan.hs_alloc_scratch(nativeDatabase(database), (PointerPointer<hs_scratch_t>) null);
    }

    @Override
    public int scratchSizeNullOutput() {
        return hyperscan.hs_scratch_size(null, (SizeTPointer) null);
    }

    @Override
    public DualScratchResult cloneScratchRaw(DualScanner source) {
        if (source == null) {
            return new DualScratchResult(hyperscan.HS_INVALID, null, null);
        }
        hs_scratch_t src = nativeScratch(source);
        try (PointerPointer<hs_scratch_t> clonedOut = new PointerPointer<>(1)) {
            clonedOut.put(0, (hs_scratch_t) null);
            int result = hyperscan.hs_clone_scratch(src, clonedOut);
            if (result == 0) {
                return new DualScratchResult(0,
                        new JavaCppRawScanner(new JavaCppScratchState(clonedOut.get(hs_scratch_t.class)), true), null);
            }
            return new DualScratchResult(result, null, null);
        }
    }

    @Override
    public DualStreamResult openStreamRaw(DualDatabase database) {
        if (database == null) {
            return new DualStreamResult(hyperscan.HS_INVALID, null, null);
        }
        acquireDatabaseStreamLease(database);
        boolean transferred = false;
        try {
            hs_database_t db = nativeDatabase(database);
            try (PointerPointer<hs_stream_t> streamOut = new PointerPointer<>(1)) {
                streamOut.put(0, (hs_stream_t) null);
                int result = hyperscan.hs_open_stream(db, 0, streamOut);
                if (result != 0) {
                    return new DualStreamResult(result, null, null);
                }
                hs_stream_t stream = streamOut.get(hs_stream_t.class);
                hs_scratch_t scratch = null;
                try {
                    try (PointerPointer<hs_scratch_t> scratchOut = new PointerPointer<>(1)) {
                        scratchOut.put(0, (hs_scratch_t) null);
                        int allocResult = hyperscan.hs_alloc_scratch(db, scratchOut);
                        if (allocResult != 0) {
                            hyperscan.hs_close_stream(stream, null, null, null);
                            return new DualStreamResult(allocResult, null, null);
                        }
                        scratch = scratchOut.get(hs_scratch_t.class);
                        List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb
                                ? nativeDb.expressions : List.of();
                        JavaCppStream opened = new JavaCppStream(
                                stream, new JavaCppScratchState(scratch), expressions, database);
                        DualStreamResult response = new DualStreamResult(0, opened, null);
                        transferred = true;
                        return response;
                    }
                } catch (RuntimeException | Error e) {
                    if (scratch != null && !scratch.isNull()) {
                        hyperscan.hs_free_scratch(scratch);
                    }
                    hyperscan.hs_close_stream(stream, null, null, null);
                    throw e;
                }
            }
        } finally {
            if (!transferred) {
                releaseDatabaseStreamLease(database);
            }
        }
    }

    @Override
    public DualScanner getStreamScratch(DualStream stream) {
        JavaCppScratchState scratchState = ((JavaCppStream) stream).scratchState;
        return scratchState == null ? null : new JavaCppRawScanner(scratchState, false);
    }

    @Override
    public int openStreamNullOutput(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID;
        }
        return hyperscan.hs_open_stream(nativeDatabase(database), 0, (PointerPointer<hs_stream_t>) null);
    }

    @Override
    public int scanStreamRaw(DualStream stream, byte[] input, DualScanner scanner, DualByteMatchHandler handler) {
        if (stream == null) {
            return hyperscan.HS_INVALID;
        }
        JavaCppStream s = (JavaCppStream) stream;
        hs_stream_t nativeStream;
        try {
            nativeStream = s.beginOperation();
        } catch (IllegalStateException e) {
            return hyperscan.HS_INVALID;
        }
        try {
            hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
            if (handler != null) {
                STREAM_CALLBACK.set(newHandlerContext(handler, s.expressions));
            }
            try (BytePointer data = input == null ? new BytePointer() : new BytePointer(input.length)) {
                if (input != null) {
                    data.put(input);
                    data.position(0);
                }
                return propagateHandlerFailure(hyperscan.hs_scan_stream(
                        nativeStream, data, input == null ? 4 : input.length, 0, scratch,
                        handler == null ? null : MATCH_HANDLER, null));
            } finally {
                if (handler != null) {
                    restoreHandlerContext();
                }
            }
        } finally {
            s.endOperation();
        }
    }

    @Override
    public int closeStreamRaw(DualStream stream, DualScanner scanner, DualByteMatchHandler handler) {
        if (stream == null) {
            return hyperscan.HS_INVALID;
        }
        JavaCppStream s = (JavaCppStream) stream;
        if (s.isClosed()) {
            return hyperscan.HS_INVALID;
        }
        hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
        if (handler != null) {
            STREAM_CALLBACK.set(newHandlerContext(handler, s.expressions));
        }
        try {
            return propagateHandlerFailure(
                    s.closeNative(scratch, handler == null ? null : MATCH_HANDLER, true));
        } finally {
            if (handler != null) {
                restoreHandlerContext();
            }
        }
    }

    @Override
    public int resetStreamRaw(DualStream stream, DualScanner scanner, DualByteMatchHandler handler) {
        if (stream == null) {
            return hyperscan.HS_INVALID;
        }
        JavaCppStream s = (JavaCppStream) stream;
        hs_stream_t nativeStream;
        try {
            nativeStream = s.beginOperation();
        } catch (IllegalStateException e) {
            return hyperscan.HS_INVALID;
        }
        try {
            hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
            if (handler != null) {
                STREAM_CALLBACK.set(newHandlerContext(handler, s.expressions));
            }
            try {
                return propagateHandlerFailure(hyperscan.hs_reset_stream(
                        nativeStream, 0, scratch, handler == null ? null : MATCH_HANDLER, null));
            } finally {
                if (handler != null) {
                    restoreHandlerContext();
                }
            }
        } finally {
            s.endOperation();
        }
    }

    @Override
    public int copyStreamRaw(DualStream[] to, DualStream from) {
        if (from == null || to == null || to.length == 0) {
            return hyperscan.HS_INVALID;
        }
        JavaCppStream src = (JavaCppStream) from;
        hs_stream_t source;
        try {
            source = src.beginOperation();
        } catch (IllegalStateException e) {
            return hyperscan.HS_INVALID;
        }
        try {
            acquireDatabaseStreamLease(src.databaseOwner);
            boolean transferred = false;
            try {
                try (PointerPointer<hs_stream_t> toOut = new PointerPointer<>(1)) {
                    toOut.put(0, (hs_stream_t) null);
                    int result = hyperscan.hs_copy_stream(toOut, source);
                    if (result == 0) {
                        hs_stream_t copied = toOut.get(hs_stream_t.class);
                        try {
                            to[0] = new JavaCppStream(copied, null, src.expressions, src.databaseOwner);
                            transferred = true;
                        } catch (RuntimeException | Error e) {
                            hyperscan.hs_close_stream(copied, null, null, null);
                            throw e;
                        }
                    }
                    return result;
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
            return hyperscan.HS_INVALID;
        }
        if (to == from) {
            return hyperscan.HS_INVALID;
        }
        JavaCppStream toStream = (JavaCppStream) to;
        JavaCppStream fromStream = (JavaCppStream) from;
        hs_stream_t destination;
        try {
            destination = toStream.beginOperation();
        } catch (IllegalStateException e) {
            return hyperscan.HS_INVALID;
        }
        hs_stream_t source;
        try {
            source = fromStream.beginOperation();
        } catch (IllegalStateException e) {
            toStream.endOperation();
            return hyperscan.HS_INVALID;
        }
        try {
            hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
            if (handler != null) {
                STREAM_CALLBACK.set(newHandlerContext(handler, toStream.expressions));
            }
            try {
                return propagateHandlerFailure(hyperscan.hs_reset_and_copy_stream(
                        destination, source, scratch, handler == null ? null : MATCH_HANDLER, null));
            } finally {
                if (handler != null) {
                    restoreHandlerContext();
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
            return hyperscan.HS_INVALID;
        }
        try (JavaCppDatabaseOperation operation = acquireDatabaseOperation(database)) {
            hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
            List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb
                    ? nativeDb.expressions : List.of();
            if (handler != null) {
                STREAM_CALLBACK.set(newHandlerContext(handler, expressions));
            }
            try (BytePointer data = input == null ? new BytePointer() : new BytePointer(input.length)) {
                if (input != null) {
                    data.put(input);
                    data.position(0);
                }
                return propagateHandlerFailure(hyperscan.hs_scan(
                        operation.database, data, input == null ? 4 : input.length, 0, scratch,
                        handler == null ? null : MATCH_HANDLER, null));
            } finally {
                if (handler != null) {
                    restoreHandlerContext();
                }
            }
        }
    }

    @Override
    public int scanVectorRaw(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler) {
        if (database == null) {
            return hyperscan.HS_INVALID;
        }
        try (JavaCppDatabaseOperation operation = acquireDatabaseOperation(database)) {
            hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
            List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb
                    ? nativeDb.expressions : List.of();
            if (handler != null) {
                STREAM_CALLBACK.set(newHandlerContext(handler, expressions));
            }
            try {
                if (input == null) {
                    return propagateHandlerFailure(hyperscan.hs_scan_vector(
                            operation.database, (PointerPointer<BytePointer>) null,
                            (IntPointer) null, 2, 0, scratch,
                            handler == null ? null : MATCH_HANDLER, null));
                }
                int[] lengths = new int[input.length];
                for (int i = 0; i < input.length; i++) {
                    lengths[i] = input[i] == null ? 4 : input[i].length;
                }
                List<BytePointer> bpRefs = new ArrayList<>(input.length);
                try (PointerPointer<BytePointer> data = new PointerPointer<>(input.length);
                     IntPointer lengthPtr = new IntPointer(lengths)) {
                    for (int i = 0; i < input.length; i++) {
                        if (input[i] == null) {
                            data.put(i, new BytePointer());
                        } else {
                            BytePointer bp = new BytePointer(input[i].length);
                            bpRefs.add(bp);
                            bp.put(input[i]);
                            data.put(i, bp);
                        }
                    }
                    return propagateHandlerFailure(hyperscan.hs_scan_vector(
                            operation.database, data, lengthPtr, input.length, 0, scratch,
                            handler == null ? null : MATCH_HANDLER, null));
                } finally {
                    closePointers(bpRefs);
                }
            } finally {
                if (handler != null) {
                    restoreHandlerContext();
                }
            }
        }
    }

    @Override
    public int scanVectorNoLenArrayRaw(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler) {
        if (database == null) {
            return hyperscan.HS_INVALID;
        }
        try (JavaCppDatabaseOperation operation = acquireDatabaseOperation(database)) {
            hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
            List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb
                    ? nativeDb.expressions : List.of();
            if (handler != null) {
                STREAM_CALLBACK.set(newHandlerContext(handler, expressions));
            }
            List<BytePointer> bpRefs = new ArrayList<>(input.length);
            try (PointerPointer<BytePointer> data = new PointerPointer<>(input.length)) {
                for (int i = 0; i < input.length; i++) {
                    if (input[i] == null) {
                        data.put(i, new BytePointer());
                    } else {
                        BytePointer bp = new BytePointer(input[i].length);
                        bpRefs.add(bp);
                        bp.put(input[i]);
                        data.put(i, bp);
                    }
                }
                return propagateHandlerFailure(hyperscan.hs_scan_vector(
                        operation.database, data, (IntPointer) null, input.length, 0, scratch,
                        handler == null ? null : MATCH_HANDLER, null));
            } finally {
                try {
                    closePointers(bpRefs);
                } finally {
                    if (handler != null) {
                        restoreHandlerContext();
                    }
                }
            }
        }
    }

    @Override
    public DualResult<byte[]> serializeRaw(DualDatabase database) {
        if (database == null) {
            return DualResult.error(hyperscan.HS_INVALID);
        }
        hs_database_t db = nativeDatabase(database);
        try (SizeTPointer size = new SizeTPointer(1);
             PointerPointer<BytePointer> bytesOut = new PointerPointer<>(1)) {
            size.put(0, 0);
            bytesOut.put(0, (BytePointer) null);
            int result = (int) HsLibrary.HS_SERIALIZE_DATABASE.invokeExact(toSegment(db), toSegment(bytesOut), toSegment(size));
            if (result != 0) {
                return DualResult.error(result);
            }
            long length = size.get();
            BytePointer bytes = bytesOut.get(BytePointer.class);
            try {
                bytes.capacity(length);
                java.nio.ByteBuffer buffer = bytes.asBuffer();
                byte[] out = new byte[(int) length];
                buffer.get(out);
                return DualResult.success(out);
            } finally {
                freeMiscPointer(bytes);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int serializeNoBufferRaw(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID;
        }
        hs_database_t db = nativeDatabase(database);
        try (SizeTPointer size = new SizeTPointer(1)) {
            return hyperscan.hs_serialize_database(db, (PointerPointer<BytePointer>) null, size);
        }
    }

    @Override
    public int serializeNoLengthRaw(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID;
        }
        hs_database_t db = nativeDatabase(database);
        try (BytePointer bytes = new BytePointer(1)) {
            return hyperscan.hs_serialize_database(db, bytes, (SizeTPointer) null);
        }
    }

    @Override
    public int databaseSizeNullOutput(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID;
        }
        return hyperscan.hs_database_size(nativeDatabase(database), (SizeTPointer) null);
    }

    @Override
    public int databaseInfoNullOutput(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_INVALID;
        }
        return hyperscan.hs_database_info(nativeDatabase(database), (PointerPointer<BytePointer>) null);
    }

    @Override
    public int freeDatabaseRaw(DualDatabase database) {
        if (database == null) {
            return hyperscan.HS_SUCCESS;
        }
        if (database instanceof JavaCppNativeDatabase nativeDb) {
            return nativeDb.free();
        }
        if (database instanceof JavaCppRawDatabase) {
            return hyperscan.HS_INVALID;
        }
        if (database instanceof JavaCppWrapperDatabase wrapper) {
            wrapper.close();
            return hyperscan.HS_SUCCESS;
        }
        return hyperscan.HS_INVALID;
    }

    @Override
    public int freeScratchRaw(DualScanner scanner) {
        if (scanner == null) {
            return hyperscan.HS_SUCCESS;
        }
        if (scanner instanceof JavaCppRawScanner raw) {
            return raw.free();
        }
        return hyperscan.HS_INVALID;
    }

    @Override
    public int freeCompileErrorRaw(Object compileError) {
        if (compileError == null) {
            return hyperscan.HS_SUCCESS;
        }
        return hyperscan.hs_free_compile_error((hs_compile_error_t) compileError);
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
        try (BytePointer expr = utf8CString(pattern);
             PointerPointer<hs_expr_info_t> infoOut = nullInfo ? null : new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = nullErr ? null : new PointerPointer<>(1)) {
            if (infoOut != null) {
                infoOut.put(0, (hs_expr_info_t) null);
            }
            if (errOut != null) {
                errOut.put(0, (hs_compile_error_t) null);
            }
            int result;
            try {
                result = (int) HsLibrary.HS_EXPRESSION_INFO.invokeExact(toSegment(expr), toFlagBits(flags), toSegment(infoOut), toSegment(errOut));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            hs_compile_error_t err = errOut == null ? null : errOut.get(hs_compile_error_t.class);
            hs_expr_info_t info = infoOut == null ? null : infoOut.get(hs_expr_info_t.class);
            try {
                return new DualResult<>(result, null, compileErrorMessage(err));
            } finally {
                try {
                    freeCompileErrorDirect(err);
                } finally {
                    freeMiscPointer(info);
                }
            }
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
        try (BytePointer expr = utf8CString(pattern);
             PointerPointer<hs_expr_info_t> infoOut = nullInfo ? null : new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = nullErr ? null : new PointerPointer<>(1)) {
            if (infoOut != null) {
                infoOut.put(0, (hs_expr_info_t) null);
            }
            if (errOut != null) {
                errOut.put(0, (hs_compile_error_t) null);
            }
            int result = hyperscan.hs_expression_ext_info(expr, toFlagBits(flags), null, infoOut, errOut);
            hs_compile_error_t err = errOut == null ? null : errOut.get(hs_compile_error_t.class);
            hs_expr_info_t info = infoOut == null ? null : infoOut.get(hs_expr_info_t.class);
            try {
                return new DualResult<>(result, null, compileErrorMessage(err));
            } finally {
                try {
                    hyperscan.hs_free_compile_error(err);
                } finally {
                    freeMiscPointer(info);
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
        try (BytePointer expr = utf8CString(pattern);
             PointerPointer<hs_expr_info_t> infoOut = new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = nullErr ? null : new PointerPointer<>(1)) {
            infoOut.put(0, (hs_expr_info_t) null);
            if (errOut != null) {
                errOut.put(0, (hs_compile_error_t) null);
            }
            int result = hyperscan.hs_expression_info(expr, toFlagBits(flags), infoOut, errOut);
            hs_compile_error_t err = errOut == null ? null : errOut.get(hs_compile_error_t.class);
            hs_expr_info_t info = infoOut.get(hs_expr_info_t.class);
            try {
                DualExpressionInfo value = null;
                if (result == 0 && info != null) {
                    value = new DualExpressionInfo(
                            Integer.toUnsignedLong(info.min_width()),
                            Integer.toUnsignedLong(info.max_width()),
                            info.unordered_matches() != 0,
                            info.matches_at_eod() != 0,
                            info.matches_only_at_eod() != 0);
                }
                return new DualResult<>(result, value, compileErrorMessage(err));
            } finally {
                try {
                    hyperscan.hs_free_compile_error(err);
                } finally {
                    freeMiscPointer(info);
                }
            }
        }
    }

    private static DualResult<DualExpressionInfo> expressionExtInfoDataInternal(String pattern, EnumSet<DualExpressionFlag> flags, DualExpressionExt ext, boolean nullErr) {
        try (BytePointer expr = utf8CString(pattern);
             hs_expr_ext_t extStruct = ext == null ? null : newDefaultExprExt();
             PointerPointer<hs_expr_info_t> infoOut = new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = nullErr ? null : new PointerPointer<>(1)) {
            if (extStruct != null) {
                applyExprExt(extStruct, ext);
            }
            infoOut.put(0, (hs_expr_info_t) null);
            if (errOut != null) {
                errOut.put(0, (hs_compile_error_t) null);
            }
            int result = hyperscan.hs_expression_ext_info(expr, toFlagBits(flags), extStruct, infoOut, errOut);
            hs_compile_error_t err = errOut == null ? null : errOut.get(hs_compile_error_t.class);
            hs_expr_info_t info = infoOut.get(hs_expr_info_t.class);
            try {
                DualExpressionInfo value = null;
                if (result == 0 && info != null) {
                    value = new DualExpressionInfo(
                            Integer.toUnsignedLong(info.min_width()),
                            Integer.toUnsignedLong(info.max_width()),
                            info.unordered_matches() != 0,
                            info.matches_at_eod() != 0,
                            info.matches_only_at_eod() != 0);
                }
                return new DualResult<>(result, value, compileErrorMessage(err));
            } finally {
                try {
                    hyperscan.hs_free_compile_error(err);
                } finally {
                    freeMiscPointer(info);
                }
            }
        }
    }

    @Override
    public int populatePlatformRaw() {
        return hyperscan.hs_populate_platform(null);
    }

    private static DualDatabase compileNative(List<DualExpression> expressions, int mode) {
        int n = expressions.size();
        try (PointerPointer<BytePointer> expressionsPtr = new PointerPointer<>(n);
             IntPointer flags = new IntPointer(n);
             IntPointer ids = new IntPointer(n);
             PointerPointer<hs_expr_ext_t> extPtr = new PointerPointer<>(n);
             PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
            dbOut.put(0, (hs_database_t) null);
            errOut.put(0, (hs_compile_error_t) null);
            List<BytePointer> exprPointers = new ArrayList<>(n);
            List<hs_expr_ext_t> exprExts = new ArrayList<>(n);
            try {
                for (int i = 0; i < n; i++) {
                    DualExpression expr = expressions.get(i);
                    BytePointer exprPtr = utf8CString(expr.pattern());
                    exprPointers.add(exprPtr);
                    expressionsPtr.put(i, exprPtr);
                    flags.put(i, toFlagBits(expr.flags()));
                    ids.put(i, expr.id() != null ? expr.id() : 0);
                    hs_expr_ext_t ext = newDefaultExprExt();
                    exprExts.add(ext);
                    extPtr.put(i, ext);
                }
                int result = hyperscan.hs_compile_ext_multi(expressionsPtr, flags, ids, extPtr, n, mode, null, dbOut, errOut);
                if (result != 0) {
                    hs_compile_error_t err = errOut.get(hs_compile_error_t.class);
                    try {
                        String message = err != null && err.message() != null
                                ? err.message().getString(StandardCharsets.UTF_8)
                                : "unknown";
                        throw new RuntimeException("Compile error: " + message);
                    } finally {
                        hyperscan.hs_free_compile_error(err);
                    }
                }
                hs_database_t database = dbOut.get(hs_database_t.class);
                return new JavaCppNativeDatabase(database, List.copyOf(expressions));
            } finally {
                closePointers(exprExts);
                closePointers(exprPointers);
            }
        }
    }

    private static hs_database_t nativeDatabase(DualDatabase database) {
        if (database instanceof JavaCppWrapperDatabase wrapper) {
            return getNativeDatabaseHandle(wrapper.database);
        }
        if (database instanceof JavaCppNativeDatabase nativeDb) {
            return nativeDb.requireDatabase();
        }
        if (database instanceof JavaCppRawDatabase rawDb) {
            return rawDb.requireDatabase();
        }
        throw new IllegalArgumentException("Unsupported database type: " + database.getClass());
    }

    private static hs_database_t getNativeDatabaseHandle(com.gliwka.hyperscan.wrapper.Database database) {
        try {
            java.lang.reflect.Field field = com.gliwka.hyperscan.wrapper.Database.class.getDeclaredField("database");
            field.setAccessible(true);
            return (hs_database_t) field.get(database);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static hs_scratch_t nativeScratch(DualScanner scanner) {
        if (scanner == null) {
            return null;
        }
        if (scanner instanceof JavaCppRawScanner raw) {
            return raw.requireScratch();
        }
        if (scanner instanceof JavaCppScanner wrapper) {
            wrapper.requireOpen();
            if (wrapper.nativeScratch != null) {
                return wrapper.nativeScratch.require();
            }
            return getNativeScratchHandle(wrapper.scanner);
        }
        throw new IllegalArgumentException("Unsupported scanner type: " + scanner.getClass());
    }

    private static hs_scratch_t streamScratch(DualScanner scanner, JavaCppStream stream) {
        return stream.scratchState == null ? nativeScratch(scanner) : stream.scratch();
    }

    private static JavaCppVectorScratch acquireVectorScratch(DualScanner scanner, hs_database_t database) {
        if (scanner instanceof JavaCppScanner wrapper) {
            return new JavaCppVectorScratch(wrapper.reusableNativeScratch(database), false);
        }
        if (scanner != null) {
            return new JavaCppVectorScratch(nativeScratch(scanner), false);
        }
        try (PointerPointer<hs_scratch_t> scratchOut = new PointerPointer<>(1)) {
            scratchOut.put(0, (hs_scratch_t) null);
            checkResult(hyperscan.hs_alloc_scratch(database, scratchOut));
            hs_scratch_t scratch = scratchOut.get(hs_scratch_t.class);
            try {
                return new JavaCppVectorScratch(scratch, true);
            } catch (RuntimeException | Error e) {
                hyperscan.hs_free_scratch(scratch);
                throw e;
            }
        }
    }

    private static final class JavaCppVectorScratch implements AutoCloseable {
        private final hs_scratch_t scratch;
        private final boolean owner;

        private JavaCppVectorScratch(hs_scratch_t scratch, boolean owner) {
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

    private static hs_scratch_t getNativeScratchHandle(com.gliwka.hyperscan.wrapper.Scanner scanner) {
        try {
            java.lang.reflect.Field field = com.gliwka.hyperscan.wrapper.Scanner.class.getDeclaredField("scratch");
            field.setAccessible(true);
            return (hs_scratch_t) field.get(scanner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static hs_stream_t nativeStream(DualStream stream) {
        if (stream == null) {
            return null;
        }
        if (stream instanceof JavaCppStream s) {
            return s.requireOpen();
        }
        throw new IllegalArgumentException("Unsupported stream type: " + stream.getClass());
    }

    private static int toJavaCppMode(DualMode mode) {
        return switch (mode) {
            case BLOCK -> hyperscan.HS_MODE_BLOCK;
            case STREAM -> hyperscan.HS_MODE_STREAM;
            case VECTORED -> hyperscan.HS_MODE_VECTORED;
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

    @Override
    public int flagsToBits(EnumSet<DualExpressionFlag> flags) {
        return toFlagBits(flags);
    }

    private static int toFlagBits(EnumSet<DualExpressionFlag> flags) {
        int bits = 0;
        for (DualExpressionFlag flag : flags) {
            bits |= switch (flag) {
                case CASELESS -> hyperscan.HS_FLAG_CASELESS;
                case DOTALL -> hyperscan.HS_FLAG_DOTALL;
                case MULTILINE -> hyperscan.HS_FLAG_MULTILINE;
                case SINGLEMATCH -> hyperscan.HS_FLAG_SINGLEMATCH;
                case ALLOWEMPTY -> hyperscan.HS_FLAG_ALLOWEMPTY;
                case UTF8 -> hyperscan.HS_FLAG_UTF8;
                case UCP -> hyperscan.HS_FLAG_UCP;
                case PREFILTER -> hyperscan.HS_FLAG_PREFILTER;
                case SOM_LEFTMOST -> hyperscan.HS_FLAG_SOM_LEFTMOST;
                case COMBINATION -> hyperscan.HS_FLAG_COMBINATION;
                case QUIET -> hyperscan.HS_FLAG_QUIET;
            };
        }
        return bits;
    }

    private static void checkResult(int result) {
        if (result != 0) {
            throw new RuntimeException("Hyperscan error " + result);
        }
    }

    private static String databaseInfo(hs_database_t database) {
        try (PointerPointer<BytePointer> info = new PointerPointer<>(1)) {
            info.put(0, (BytePointer) null);
            checkResult(hyperscan.hs_database_info(database, info));
            BytePointer infoPtr = info.get(BytePointer.class);
            try {
                return infoPtr.getString(StandardCharsets.UTF_8);
            } finally {
                freeMiscPointer(infoPtr);
            }
        }
    }

    private static Expression toJavaCppExpression(DualExpression expr) {
        return new Expression(expr.pattern(), toJavaCppFlags(expr.flags()), expr.id());
    }

    private static EnumSet<ExpressionFlag> toJavaCppFlags(EnumSet<DualExpressionFlag> flags) {
        EnumSet<ExpressionFlag> result = EnumSet.noneOf(ExpressionFlag.class);
        for (DualExpressionFlag flag : flags) {
            result.add(toJavaCppFlag(flag));
        }
        return result;
    }

    private static ExpressionFlag toJavaCppFlag(DualExpressionFlag flag) {
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
            flags.add(fromJavaCppFlag(flag));
        }
        return new DualExpression(expr.getExpression(), flags, expr.getId());
    }

    private static List<DualExpression> expressionsOf(DualDatabase database) {
        if (database instanceof JavaCppNativeDatabase nativeDb) {
            return nativeDb.expressions;
        }
        if (database instanceof JavaCppWrapperDatabase wrapper) {
            return wrapper.expressions;
        }
        return List.of();
    }

    private static BytePointer newBytePointer(ByteBuffer input) {
        if (input == null) {
            return new BytePointer();
        }
        BytePointer data = new BytePointer(input);
        data.position(input.position());
        return data;
    }

    private static BytePointer newBytePointer(byte[] input) {
        if (input == null) {
            return new BytePointer();
        }
        if (STREAM_CALLBACK.get() != null) {
            return new BytePointer(input);
        }
        ByteBuffer buffer = getScanBuffer(input);
        BytePointer data = new BytePointer(buffer);
        data.position(buffer.position());
        return data;
    }

    private static ByteBuffer getScanBuffer(byte[] input) {
        if (input == null) {
            return null;
        }
        ByteBuffer buffer = SCAN_BUFFER.get();
        if (buffer == null || buffer.capacity() < input.length) {
            buffer = ByteBuffer.allocateDirect(input.length);
            SCAN_BUFFER.set(buffer);
        }
        buffer.clear();
        MemorySegment segment = MemorySegment.ofBuffer(buffer);
        UNSAFE.copyMemory(input, Unsafe.ARRAY_BYTE_BASE_OFFSET, null, segment.address(), input.length);
        buffer.limit(input.length);
        return buffer;
    }

    private static DualExpressionFlag fromJavaCppFlag(ExpressionFlag flag) {
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

    private record JavaCppWrapperDatabase(com.gliwka.hyperscan.wrapper.Database database, List<DualExpression> expressions) implements DualDatabase {
        @Override
        public long getSize() {
            return database.getSize();
        }

        @Override
        public void close() {
            closeDatabaseWhenUnused(this, database::close);
        }
    }

    private static final class JavaCppNativeDatabase implements DualDatabase {
        private hs_database_t database;
        final List<DualExpression> expressions;

        JavaCppNativeDatabase(hs_database_t database, List<DualExpression> expressions) {
            this.database = database;
            this.expressions = expressions;
        }

        synchronized hs_database_t requireDatabase() {
            if (database == null || database.isNull()) {
                throw new IllegalStateException("Database is already closed");
            }
            return database;
        }

        synchronized int free() {
            return freeDatabaseWhenUnused(this, () -> {
                if (database == null || database.isNull()) {
                    return hyperscan.HS_SUCCESS;
                }
                int result = hyperscan.hs_free_database(database);
                if (result == hyperscan.HS_SUCCESS) {
                    database.setNull();
                    database = null;
                }
                return result;
            });
        }

        @Override
        public long getSize() {
            try (SizeTPointer size = new SizeTPointer(1)) {
                checkResult(hyperscan.hs_database_size(requireDatabase(), size));
                return size.get();
            }
        }

        @Override
        public void close() {
            checkResult(free());
        }
    }

    private static final class JavaCppRawDatabaseState {
        private BytePointer memory;

        JavaCppRawDatabaseState(BytePointer memory) {
            this.memory = memory;
        }

        synchronized long address() {
            if (memory == null || memory.isNull()) {
                throw new IllegalStateException("Raw database memory is already closed");
            }
            return memory.address();
        }

        synchronized void close() {
            if (memory == null) {
                return;
            }
            memory.close();
            memory = null;
        }
    }

    private static final class JavaCppRawDatabase implements DualDatabase {
        private hs_database_t database;
        final JavaCppRawDatabaseState state;
        private final boolean owner;
        private boolean closed;

        JavaCppRawDatabase(hs_database_t database, JavaCppRawDatabaseState state, boolean owner) {
            this.database = database;
            this.state = state;
            this.owner = owner;
        }

        synchronized hs_database_t requireDatabase() {
            if (closed || database == null || database.isNull()) {
                throw new IllegalStateException("Raw database view is already closed");
            }
            state.address();
            return database;
        }

        @Override
        public long getSize() {
            try (SizeTPointer size = new SizeTPointer(1)) {
                checkResult(hyperscan.hs_database_size(requireDatabase(), size));
                return size.get();
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
            if (database != null) {
                database.setNull();
                database = null;
            }
            closed = true;
        }
    }

    private static final class JavaCppScratchState {
        private hs_scratch_t scratch;

        JavaCppScratchState(hs_scratch_t scratch) {
            this.scratch = scratch;
        }

        synchronized hs_scratch_t require() {
            if (scratch == null || scratch.isNull()) {
                throw new IllegalStateException("Scratch space is already closed");
            }
            return scratch;
        }

        synchronized void replace(hs_scratch_t replacement) {
            scratch = replacement;
        }

        synchronized boolean isClosed() {
            return scratch == null || scratch.isNull();
        }

        synchronized int free() {
            if (scratch == null || scratch.isNull()) {
                return hyperscan.HS_SUCCESS;
            }
            int result = hyperscan.hs_free_scratch(scratch);
            if (result == hyperscan.HS_SUCCESS) {
                scratch.setNull();
                scratch = null;
            }
            return result;
        }
    }

    private static final class JavaCppStream implements DualStream {
        private hs_stream_t stream;
        final JavaCppScratchState scratchState;
        final List<DualExpression> expressions;
        final DualDatabase databaseOwner;
        private boolean closed;
        private boolean operationInProgress;
        private boolean leaseReleased;

        JavaCppStream(hs_stream_t stream, JavaCppScratchState scratchState,
                      List<DualExpression> expressions, DualDatabase databaseOwner) {
            this.stream = stream;
            this.scratchState = scratchState;
            this.expressions = expressions;
            this.databaseOwner = databaseOwner;
        }

        hs_scratch_t scratch() {
            return scratchState == null ? null : scratchState.require();
        }

        synchronized hs_stream_t requireOpen() {
            if (closed || stream == null || stream.isNull()) {
                throw new IllegalStateException("Stream is already closed");
            }
            return stream;
        }

        synchronized hs_stream_t beginOperation() {
            hs_stream_t current = requireOpen();
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

        synchronized int closeNative(hs_scratch_t callbackScratch, match_event_handler handler,
                                     boolean invalidWhenClosed) {
            if (closed) {
                finishCleanup();
                return invalidWhenClosed ? hyperscan.HS_INVALID : hyperscan.HS_SUCCESS;
            }
            if (operationInProgress) {
                return hyperscan.HS_INVALID;
            }
            operationInProgress = true;
            try {
                int result = hyperscan.hs_close_stream(stream, callbackScratch, handler, null);
                if (result != hyperscan.HS_INVALID && result != hyperscan.HS_SCRATCH_IN_USE) {
                    closed = true;
                    if (stream != null) {
                        stream.setNull();
                        stream = null;
                    }
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
            checkResult(closeNative(scratch(), null, false));
        }
    }

    private static final class JavaCppScanner implements DualScanner {
        final com.gliwka.hyperscan.wrapper.Scanner scanner;
        JavaCppScratchState nativeScratch;
        private boolean closed;
        private boolean wrapperClosed;

        JavaCppScanner(com.gliwka.hyperscan.wrapper.Scanner scanner) {
            this.scanner = scanner;
        }

        synchronized void requireOpen() {
            if (closed) {
                throw new IllegalStateException("Scanner is already closed");
            }
        }

        synchronized hs_scratch_t ensureNativeScratch(hs_database_t database) {
            if (closed) {
                throw new IllegalStateException("Scanner is already closed");
            }
            try (PointerPointer<hs_scratch_t> scratchOut = new PointerPointer<>(1)) {
                scratchOut.put(0, nativeScratch == null ? null : nativeScratch.require());
                checkResult(hyperscan.hs_alloc_scratch(database, scratchOut));
                hs_scratch_t scratch = scratchOut.get(hs_scratch_t.class);
                if (nativeScratch == null) {
                    nativeScratch = new JavaCppScratchState(scratch);
                } else {
                    nativeScratch.replace(scratch);
                }
                return scratch;
            }
        }

        synchronized hs_scratch_t reusableNativeScratch(hs_database_t database) {
            requireOpen();
            return nativeScratch == null ? ensureNativeScratch(database) : nativeScratch.require();
        }

        @Override
        public synchronized long getSize() {
            requireOpen();
            if (nativeScratch != null) {
                try (SizeTPointer size = new SizeTPointer(1)) {
                    checkResult(hyperscan.hs_scratch_size(nativeScratch.require(), size));
                    return size.get();
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

    private static final class JavaCppRawScanner implements DualScanner {
        private final JavaCppScratchState state;
        private final boolean owner;

        JavaCppRawScanner(JavaCppScratchState state, boolean owner) {
            this.state = state;
            this.owner = owner;
        }

        hs_scratch_t requireScratch() {
            return state.require();
        }

        void replace(hs_scratch_t scratch) {
            if (!owner) {
                throw new IllegalStateException("Cannot replace borrowed scratch");
            }
            state.replace(scratch);
        }

        boolean isOwner() {
            return owner;
        }

        int free() {
            return owner ? state.free() : hyperscan.HS_INVALID;
        }

        @Override
        public long getSize() {
            try (SizeTPointer size = new SizeTPointer(1)) {
                checkResult(hyperscan.hs_scratch_size(requireScratch(), size));
                return size.get();
            }
        }

        @Override
        public void close() {
            if (owner) {
                checkResult(state.free());
            }
        }
    }

    private record JavaCppPatternFilter(PatternFilter filter) implements DualPatternFilter {
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
