package com.xenoamess.hyperscan.smoke.dual;

import com.xenoamess.hyperscan_panama.jni.HyperscanNativeLoader;
import com.xenoamess.hyperscan_panama.jni.generated.hyperscan;
import com.xenoamess.hyperscan_panama.jni.generated.hs_alloc_t;
import com.xenoamess.hyperscan_panama.jni.generated.hs_compile_error;
import com.xenoamess.hyperscan_panama.jni.generated.hs_expr_ext;
import com.xenoamess.hyperscan_panama.jni.generated.hs_expr_info;
import com.xenoamess.hyperscan_panama.jni.generated.hs_free_t;
import com.xenoamess.hyperscan_panama.jni.generated.hs_platform_info;
import com.xenoamess.hyperscan_panama.jni.generated.match_event_handler;
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
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.misc.Unsafe;

public class PanamaAdapter implements DualApi {

    private static final Unsafe UNSAFE = getUnsafe();

    static {
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
    private static final SymbolLookup HS_LIBRARY_LOOKUP;

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

    private static final class HsLibrary {
        private static MethodHandle lookup(String name, FunctionDescriptor desc) {
            MemorySegment symbol = HS_LIBRARY_LOOKUP.find(name).orElseThrow(() -> new RuntimeException("Symbol not found: " + name));
            return Linker.nativeLinker().downcallHandle(symbol, desc);
        }

        private static final MethodHandle HS_COMPILE = lookup("hs_compile",
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS));
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

    private static Arena ensureAllocatorArena() {
        if (allocatorArena == null || !allocatorArena.scope().isAlive()) {
            allocatorArena = Arena.ofConfined();
        }
        return allocatorArena;
    }

    private static MemorySegment wrapAllocator(DualAllocator alloc, Arena arena) {
        if (alloc == null) {
            return MemorySegment.NULL;
        }
        return hs_alloc_t.allocate(size -> {
            long address = alloc.allocate(size);
            if (address == 0) {
                return MemorySegment.NULL;
            }
            return MemorySegment.ofAddress(address).reinterpret(size);
        }, arena);
    }

    private static MemorySegment wrapFree(DualFree free, Arena arena) {
        if (free == null) {
            return MemorySegment.NULL;
        }
        return hs_free_t.allocate(ptr -> {
            if (ptr == null || ptr.address() == 0) {
                return;
            }
            free.free(ptr.address());
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

    private static final MethodHandle FREE = lookupFree();

    private static MethodHandle lookupFree() {
        try {
            MemorySegment symbol = Linker.nativeLinker().defaultLookup().find("free").orElseThrow(() -> new RuntimeException("free symbol not found"));
            return Linker.nativeLinker().downcallHandle(symbol, FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static void freeSegment(MemorySegment segment) {
        if (segment == null || segment == MemorySegment.NULL) {
            return;
        }
        try {
            FREE.invokeExact(segment);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static MemorySegment reinterpretHandle(MemorySegment segment) {
        if (segment == null || segment == MemorySegment.NULL) {
            return MemorySegment.NULL;
        }
        return segment.reinterpret(8, Arena.global(), null);
    }

    private static MemorySegment reinterpretCompileError(MemorySegment segment) {
        if (segment == null || segment == MemorySegment.NULL) {
            return MemorySegment.NULL;
        }
        return segment.reinterpret(hs_compile_error.sizeof(), Arena.global(), null);
    }

    private static MemorySegment reinterpretExprInfo(MemorySegment segment) {
        if (segment == null || segment == MemorySegment.NULL) {
            return MemorySegment.NULL;
        }
        return segment.reinterpret(hs_expr_info.sizeof(), Arena.global(), null);
    }

    private static MemorySegment reinterpretString(MemorySegment segment) {
        if (segment == null || segment == MemorySegment.NULL) {
            return MemorySegment.NULL;
        }
        return segment.reinterpret(65536, Arena.global(), null);
    }

    private static MemorySegment reinterpretBuffer(MemorySegment segment, long size) {
        if (segment == null || segment == MemorySegment.NULL) {
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

    private static final ThreadLocal<HandlerContext> STREAM_CALLBACK = new ThreadLocal<>();

    private static final MemorySegment MATCH_HANDLER = createMatchHandler();

    private static MemorySegment createMatchHandler() {
        match_event_handler.Function callback = (id, from, to, flags, context) -> {
            HandlerContext ctx = STREAM_CALLBACK.get();
            if (ctx == null) {
                return 0;
            }
            DualExpression expression = findExpressionById(ctx.expressions(), id);
            if (expression == null) {
                expression = new DualExpression("", EnumSet.noneOf(DualExpressionFlag.class), id);
            }
            return ctx.handler().onMatch(expression, from, to) ? 0 : -1;
        };
        try {
            MethodHandle mh = MethodHandles.lookup()
                    .findVirtual(match_event_handler.Function.class, "apply",
                            MethodType.methodType(int.class, int.class, long.class, long.class, int.class, MemorySegment.class))
                    .bindTo(callback);
            return Linker.nativeLinker().upcallStub(mh, match_event_handler.descriptor(), HS_LIBRARY_ARENA);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private record HandlerContext(DualByteMatchHandler handler, List<DualExpression> expressions) {
    }

    @Override
    public void setAllocator(DualAllocator alloc, DualFree free) {
        Arena arena = ensureAllocatorArena();
        currentAllocator = wrapAllocator(alloc, arena);
        currentFree = wrapFree(free, arena);
        checkResult(hyperscan.hs_set_allocator(currentAllocator, currentFree));
        setHsLibraryAllocator("hs_set_allocator", currentAllocator, currentFree);
    }

    @Override
    public void setDatabaseAllocator(DualAllocator alloc, DualFree free) {
        Arena arena = ensureAllocatorArena();
        currentDatabaseAllocator = wrapAllocator(alloc, arena);
        currentDatabaseFree = wrapFree(free, arena);
        checkResult(hyperscan.hs_set_database_allocator(currentDatabaseAllocator, currentDatabaseFree));
        setHsLibraryAllocator("hs_set_database_allocator", currentDatabaseAllocator, currentDatabaseFree);
    }

    @Override
    public void setMiscAllocator(DualAllocator alloc, DualFree free) {
        Arena arena = ensureAllocatorArena();
        currentMiscAllocator = wrapAllocator(alloc, arena);
        currentMiscFree = wrapFree(free, arena);
        checkResult(hyperscan.hs_set_misc_allocator(currentMiscAllocator, currentMiscFree));
        setHsLibraryAllocator("hs_set_misc_allocator", currentMiscAllocator, currentMiscFree);
    }

    @Override
    public void setScratchAllocator(DualAllocator alloc, DualFree free) {
        Arena arena = ensureAllocatorArena();
        currentScratchAllocator = wrapAllocator(alloc, arena);
        currentScratchFree = wrapFree(free, arena);
        checkResult(hyperscan.hs_set_scratch_allocator(currentScratchAllocator, currentScratchFree));
        setHsLibraryAllocator("hs_set_scratch_allocator", currentScratchAllocator, currentScratchFree);
    }

    @Override
    public void setStreamAllocator(DualAllocator alloc, DualFree free) {
        Arena arena = ensureAllocatorArena();
        currentStreamAllocator = wrapAllocator(alloc, arena);
        currentStreamFree = wrapFree(free, arena);
        checkResult(hyperscan.hs_set_stream_allocator(currentStreamAllocator, currentStreamFree));
        setHsLibraryAllocator("hs_set_stream_allocator", currentStreamAllocator, currentStreamFree);
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
            for (int i = 0; i < n; i++) {
                DualExpression expr = expressions.get(i);
                expressionsPtr.setAtIndex(ValueLayout.ADDRESS, i, arena.allocateFrom(expr.pattern()));
                flags.setAtIndex(ValueLayout.JAVA_INT, i, toFlagBits(expr.flags()));
                ids.setAtIndex(ValueLayout.JAVA_INT, i, expr.id() != null ? expr.id() : 0);
                MemorySegment ext = newDefaultExprExt(arena);
                extPtr.setAtIndex(ValueLayout.ADDRESS, i, ext);
            }
            MemorySegment dbOut = zeroAddressOut(HS_LIBRARY_ARENA);
            MemorySegment errOut = zeroAddressOut(HS_LIBRARY_ARENA);
            int result = hyperscan.hs_compile_ext_multi(expressionsPtr, flags, ids, extPtr, n, mode, MemorySegment.NULL, dbOut, errOut);
            if (result != 0) {
                MemorySegment err = reinterpretCompileError(errOut.get(ValueLayout.ADDRESS, 0));
                String message = readCompileErrorMessage(err);
                if (err != null && err != MemorySegment.NULL) {
                    hyperscan.hs_free_compile_error(err);
                }
                throw new RuntimeException("Compile error: " + message);
            }
            MemorySegment db = dbOut.get(ValueLayout.ADDRESS, 0);
            return new PanamaNativeDatabase(db, List.copyOf(expressions));
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
        PanamaDatabase db = (PanamaDatabase) database;
        s.scanner().allocScratch(db.database());
    }

    @Override
    public List<DualMatch> scan(DualScanner scanner, DualDatabase database, String input) {
        PanamaScanner s = (PanamaScanner) scanner;
        PanamaDatabase db = (PanamaDatabase) database;
        List<Match> matches = s.scanner().scan(db.database(), input);
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
        PanamaDatabase db = (PanamaDatabase) database;
        s.scanner().scan(db.database(), input, new StringMatchEventHandler() {
            @Override
            public boolean onMatch(Expression expression, long from, long to) {
                return handler.onMatch(toDualExpression(expression), from, to);
            }
        });
    }

    @Override
    public void scan(DualScanner scanner, DualDatabase database, byte[] input, DualByteMatchHandler handler) {
        PanamaScanner s = (PanamaScanner) scanner;
        PanamaDatabase db = (PanamaDatabase) database;
        s.scanner().scan(db.database(), input, new ByteMatchEventHandler() {
            @Override
            public boolean onMatch(Expression expression, long from, long to) {
                return handler.onMatch(toDualExpression(expression), from, to);
            }
        });
    }

    @Override
    public boolean hasMatch(DualScanner scanner, DualDatabase database, String input) {
        PanamaScanner s = (PanamaScanner) scanner;
        PanamaDatabase db = (PanamaDatabase) database;
        return s.scanner().hasMatch(db.database(), input);
    }

    @Override
    public boolean hasMatch(DualScanner scanner, DualDatabase database, byte[] input) {
        PanamaScanner s = (PanamaScanner) scanner;
        PanamaDatabase db = (PanamaDatabase) database;
        return s.scanner().hasMatch(db.database(), input);
    }

    @Override
    public DualStream openStream(DualDatabase database) {
        MemorySegment db = nativeDatabase(database);
        List<DualExpression> expressions = database instanceof PanamaNativeDatabase nativeDb ? nativeDb.expressions() : List.of();
        MemorySegment streamOut = zeroAddressOut(HS_LIBRARY_ARENA);
        checkResult(hyperscan.hs_open_stream(db, 0, streamOut));
        MemorySegment stream = reinterpretHandle(streamOut.get(ValueLayout.ADDRESS, 0));
        MemorySegment scratchOut = zeroAddressOut(HS_LIBRARY_ARENA);
        checkResult(hyperscan.hs_alloc_scratch(db, scratchOut));
        MemorySegment scratch = reinterpretHandle(scratchOut.get(ValueLayout.ADDRESS, 0));
        return new PanamaStream(stream, scratch, expressions);
    }

    @Override
    public void scanStream(DualScanner scanner, DualStream stream, byte[] input, DualByteMatchHandler handler) {
        PanamaStream s = (PanamaStream) stream;
        if (s.closed) {
            throw new IllegalStateException("Stream is already closed");
        }
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, s.expressions));
        }
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment data = input == null ? MemorySegment.NULL : allocateBytes(arena, input);
                int length = input == null ? 4 : input.length;
                int result = hyperscan.hs_scan_stream(s.stream, data, length, 0, s.scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL);
                if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED()) {
                    checkResult(result);
                }
            }
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
        }
    }

    @Override
    public void closeStream(DualScanner scanner, DualStream stream, DualByteMatchHandler handler) {
        PanamaStream s = (PanamaStream) stream;
        if (s.closed) {
            return;
        }
        s.closed = true;
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, s.expressions));
        }
        try {
            int result = hyperscan.hs_close_stream(s.stream, s.scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL);
            if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED()) {
                checkResult(result);
            }
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
            hyperscan.hs_free_scratch(s.scratch);
        }
    }

    @Override
    public void scanVector(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler) {
        MemorySegment db = nativeDatabase(database);
        List<DualExpression> expressions = database instanceof PanamaNativeDatabase nativeDb ? nativeDb.expressions() : List.of();
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, expressions));
        }
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment existingScratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
                MemorySegment scratchOut = null;
                MemorySegment scratch;
                if (existingScratch == null || existingScratch == MemorySegment.NULL) {
                    scratchOut = zeroAddressOut(arena);
                    checkResult(hyperscan.hs_alloc_scratch(db, scratchOut));
                    scratch = reinterpretHandle(scratchOut.get(ValueLayout.ADDRESS, 0));
                } else {
                    scratch = existingScratch;
                }
                try {
                    int[] lengths = new int[input.length];
                    for (int i = 0; i < input.length; i++) {
                        lengths[i] = input[i] == null ? 4 : input[i].length;
                    }
                    MemorySegment dataPtrs = arena.allocate(input.length * ValueLayout.ADDRESS.byteSize());
                    MemorySegment lengthPtr = arena.allocate(input.length * ValueLayout.JAVA_INT.byteSize());
                    for (int i = 0; i < input.length; i++) {
                        byte[] data = input[i];
                        MemorySegment dataSeg = data == null ? MemorySegment.NULL : allocateBytes(arena, data);
                        dataPtrs.setAtIndex(ValueLayout.ADDRESS, i, dataSeg);
                        lengthPtr.setAtIndex(ValueLayout.JAVA_INT, i, data == null ? 4 : data.length);
                    }
                    int result = hyperscan.hs_scan_vector(db, dataPtrs, lengthPtr, input.length, 0, scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL);
                    if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED()) {
                        checkResult(result);
                    }
                } finally {
                    if (scratchOut != null) {
                        hyperscan.hs_free_scratch(scratch);
                    }
                }
            }
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
        }
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
            String result = info.getString(0);
            freeSegment(info);
            return result;
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
            byte[] out = new byte[(int) length];
            MemorySegment.copy(bytes, 0, MemorySegment.ofArray(out), 0, length);
            freeSegment(bytes);
            return out;
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
            MemorySegment dbOut = zeroAddressOut(HS_LIBRARY_ARENA);
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
            wrapper.database().close();
        } else if (database instanceof PanamaNativeDatabase nativeDb) {
            checkResult(hyperscan.hs_free_database(nativeDb.database()));
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
            return wrapper.scanner().getSize();
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
            MemorySegment dbOut = zeroAddressOut(HS_LIBRARY_ARENA);
            MemorySegment errOut = zeroAddressOut(HS_LIBRARY_ARENA);
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
            MemorySegment dbOut = zeroAddressOut(HS_LIBRARY_ARENA);
            MemorySegment errOut = zeroAddressOut(HS_LIBRARY_ARENA);
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
            MemorySegment dbOut = zeroAddressOut(HS_LIBRARY_ARENA);
            MemorySegment errOut = zeroAddressOut(HS_LIBRARY_ARENA);
            int result = hyperscan.hs_compile_ext_multi(expressionsPtr, flagsPtr, idsPtr, extPtr, 1, mode, MemorySegment.NULL, dbOut, errOut);
            return buildCompileResult(result, dbOut, errOut, pattern == null ? List.of() : List.of(new DualExpression(pattern, EnumSet.noneOf(DualExpressionFlag.class), 0)));
        }
    }

    private static DualCompileResult buildCompileResult(int result, MemorySegment dbOut, MemorySegment errOut, List<DualExpression> expressions) {
        if (result == 0) {
            MemorySegment db = reinterpretHandle(dbOut.get(ValueLayout.ADDRESS, 0));
            return new DualCompileResult(0, new PanamaNativeDatabase(db, List.copyOf(expressions)), null);
        }
        MemorySegment err = reinterpretCompileError(errOut.get(ValueLayout.ADDRESS, 0));
        String message = readCompileErrorMessage(err);
        if (err != null && err != MemorySegment.NULL) {
            hyperscan.hs_free_compile_error(err);
        }
        return new DualCompileResult(result, null, message);
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
            MemorySegment dbOut = zeroAddressOut(HS_LIBRARY_ARENA);
            MemorySegment errOut = zeroAddressOut(HS_LIBRARY_ARENA);
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
            MemorySegment dbOut = zeroAddressOut(HS_LIBRARY_ARENA);
            MemorySegment errOut = zeroAddressOut(HS_LIBRARY_ARENA);
            int result = hyperscan.hs_compile_multi(expressionsPtr, flagsPtr, idsPtr, n, mode, MemorySegment.NULL, dbOut, errOut);
            return buildCompileResult(result, dbOut, errOut, expressions);
        }
    }

    @Override
    public DualCompileResult compileNullOutputRaw(String pattern, int flags, int mode) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment expr = pattern == null ? MemorySegment.NULL : arena.allocateFrom(pattern);
            MemorySegment errOut = zeroAddressOut(HS_LIBRARY_ARENA);
            int result = hyperscan.hs_compile(expr, flags, mode, MemorySegment.NULL, MemorySegment.NULL, errOut);
            MemorySegment err = reinterpretCompileError(errOut.get(ValueLayout.ADDRESS, 0));
            String message = readCompileErrorMessage(err);
            if (err != null && err != MemorySegment.NULL) {
                hyperscan.hs_free_compile_error(err);
            }
            return new DualCompileResult(result, null, message);
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
            MemorySegment errOut = zeroAddressOut(HS_LIBRARY_ARENA);
            int result = hyperscan.hs_compile_multi(expressionsPtr, flagsPtr, idsPtr, n, mode, MemorySegment.NULL, MemorySegment.NULL, errOut);
            MemorySegment err = reinterpretCompileError(errOut.get(ValueLayout.ADDRESS, 0));
            String message = readCompileErrorMessage(err);
            if (err != null && err != MemorySegment.NULL) {
                hyperscan.hs_free_compile_error(err);
            }
            return new DualCompileResult(result, null, message);
        }
    }

    @Override
    public DualResult<DualDatabase> deserializeRaw(byte[] data) {
        if (data == null) {
            return DualResult.error(hyperscan.HS_INVALID());
        }
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment dataSeg = allocateBytes(arena, data);
            MemorySegment dbOut = zeroAddressOut(HS_LIBRARY_ARENA);
            int result = hyperscan.hs_deserialize_database(dataSeg, data.length, dbOut);
            if (result == 0) {
            MemorySegment db = reinterpretHandle(dbOut.get(ValueLayout.ADDRESS, 0));
                return DualResult.success(new PanamaNativeDatabase(db, List.of()));
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
        Arena arena = Arena.ofConfined();
        MemorySegment memory = arena.allocate(size, 8);
        if ((memory.address() & 7L) != 0) {
            arena.close();
            throw new RuntimeException("Raw database memory is not 8-byte aligned");
        }
        return new PanamaRawDatabase(memory, memory, arena, true);
    }

    @Override
    public DualDatabase offsetRawDatabase(DualDatabase database, long offset) {
        if (!(database instanceof PanamaRawDatabase raw)) {
            throw new IllegalArgumentException("Not a raw database: " + database.getClass());
        }
        MemorySegment db = raw.memory().asSlice(offset);
        return new PanamaRawDatabase(db, raw.memory(), raw.arena(), false);
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
            byte[] out = new byte[(int) length];
            MemorySegment.copy(bytes, 0, MemorySegment.ofArray(out), 0, length);
            freeSegment(bytes);
            return DualResult.success(out);
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
                String value = info.getString(0);
                freeSegment(info);
                return DualResult.success(value);
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
                String value = info.getString(0);
                freeSegment(info);
                return DualResult.success(value);
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
        MemorySegment scratchOut = zeroAddressOut(HS_LIBRARY_ARENA);
        try {
            int result = hyperscan.hs_alloc_scratch(nativeDatabase(database), scratchOut);
            if (result == 0) {
                MemorySegment scratch = reinterpretHandle(scratchOut.get(ValueLayout.ADDRESS, 0));
                return new DualScratchResult(0, new PanamaRawScanner(scratch), null);
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
        MemorySegment scratchOut = zeroAddressOut(HS_LIBRARY_ARENA);
        MemorySegment existing = existingScratch == null ? MemorySegment.NULL : nativeScratch(existingScratch);
        scratchOut.set(ValueLayout.ADDRESS, 0, existing);
        try {
            int result = hyperscan.hs_alloc_scratch(nativeDatabase(database), scratchOut);
            if (result == 0) {
                MemorySegment scratch = reinterpretHandle(scratchOut.get(ValueLayout.ADDRESS, 0));
                return new DualScratchResult(0, new PanamaRawScanner(scratch), null);
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
        MemorySegment clonedOut = zeroAddressOut(HS_LIBRARY_ARENA);
        try {
            int result = hyperscan.hs_clone_scratch(src, clonedOut);
            if (result == 0) {
                MemorySegment cloned = reinterpretHandle(clonedOut.get(ValueLayout.ADDRESS, 0));
                return new DualScratchResult(0, new PanamaRawScanner(cloned), null);
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
        MemorySegment db = nativeDatabase(database);
        MemorySegment streamOut = zeroAddressOut(HS_LIBRARY_ARENA);
        try {
            int result = hyperscan.hs_open_stream(db, 0, streamOut);
            if (result != 0) {
                return new DualStreamResult(result, null, null);
            }
            MemorySegment stream = reinterpretHandle(streamOut.get(ValueLayout.ADDRESS, 0));
            MemorySegment scratchOut = zeroAddressOut(HS_LIBRARY_ARENA);
            int allocResult = hyperscan.hs_alloc_scratch(db, scratchOut);
            if (allocResult != 0) {
                hyperscan.hs_close_stream(stream, MemorySegment.NULL, MemorySegment.NULL, MemorySegment.NULL);
                return new DualStreamResult(allocResult, null, null);
            }
            MemorySegment scratch = reinterpretHandle(scratchOut.get(ValueLayout.ADDRESS, 0));
            List<DualExpression> expressions = database instanceof PanamaNativeDatabase nativeDb ? nativeDb.expressions() : List.of();
            return new DualStreamResult(0, new PanamaStream(stream, scratch, expressions), null);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualScanner getStreamScratch(DualStream stream) {
        return new PanamaRawScanner(((PanamaStream) stream).scratch);
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
        MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, s.expressions));
        }
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment data = input == null ? MemorySegment.NULL : allocateBytes(arena, input);
                int length = input == null ? 4 : input.length;
                return hyperscan.hs_scan_stream(s.stream, data, length, 0, scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL);
            }
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
        }
    }

    @Override
    public int closeStreamRaw(DualStream stream, DualScanner scanner, DualByteMatchHandler handler) {
        if (stream == null) {
            return hyperscan.HS_INVALID();
        }
        PanamaStream s = (PanamaStream) stream;
        if (s.closed) {
            return hyperscan.HS_INVALID();
        }
        s.closed = true;
        MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, s.expressions));
        }
        try {
            return hyperscan.hs_close_stream(s.stream, scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL);
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
            hyperscan.hs_free_scratch(s.scratch);
        }
    }

    @Override
    public int resetStreamRaw(DualStream stream, DualScanner scanner, DualByteMatchHandler handler) {
        if (stream == null) {
            return hyperscan.HS_INVALID();
        }
        PanamaStream s = (PanamaStream) stream;
        MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, s.expressions));
        }
        try {
            return hyperscan.hs_reset_stream(s.stream, 0, scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL);
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
        }
    }

    @Override
    public int copyStreamRaw(DualStream[] to, DualStream from) {
        if (from == null || to == null || to.length == 0) {
            return hyperscan.HS_INVALID();
        }
        PanamaStream src = (PanamaStream) from;
        MemorySegment toOut = zeroAddressOut(HS_LIBRARY_ARENA);
        try {
            int result = hyperscan.hs_copy_stream(toOut, src.stream);
            if (result == 0) {
                MemorySegment copied = reinterpretHandle(toOut.get(ValueLayout.ADDRESS, 0));
                to[0] = new PanamaStream(copied, MemorySegment.NULL, src.expressions);
            }
            return result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
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
        MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, toStream.expressions));
        }
        try {
            return hyperscan.hs_reset_and_copy_stream(toStream.stream, fromStream.stream, scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL);
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
        }
    }

    @Override
    public int scanRaw(DualScanner scanner, DualDatabase database, byte[] input, DualByteMatchHandler handler) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        MemorySegment db = nativeDatabase(database);
        MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
        List<DualExpression> expressions = database instanceof PanamaNativeDatabase nativeDb ? nativeDb.expressions() : List.of();
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, expressions));
        }
        try {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment data = input == null ? MemorySegment.NULL : allocateBytes(arena, input);
                int length = input == null ? 4 : input.length;
                return hyperscan.hs_scan(db, data, length, 0, scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL);
            }
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
        }
    }

    @Override
    public int scanVectorRaw(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        MemorySegment db = nativeDatabase(database);
        MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
        List<DualExpression> expressions = database instanceof PanamaNativeDatabase nativeDb ? nativeDb.expressions() : List.of();
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, expressions));
        }
        try {
            try (Arena arena = Arena.ofConfined()) {
                if (input == null) {
                    return hyperscan.hs_scan_vector(db, MemorySegment.NULL, MemorySegment.NULL, 2, 0, scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL);
                }
                MemorySegment dataPtrs = arena.allocate(input.length * ValueLayout.ADDRESS.byteSize());
                MemorySegment lengths = arena.allocate(input.length * ValueLayout.JAVA_INT.byteSize());
                for (int i = 0; i < input.length; i++) {
                    byte[] data = input[i];
                    MemorySegment dataSeg = data == null ? MemorySegment.NULL : allocateBytes(arena, data);
                    dataPtrs.setAtIndex(ValueLayout.ADDRESS, i, dataSeg);
                    lengths.setAtIndex(ValueLayout.JAVA_INT, i, data == null ? 4 : data.length);
                }
                return hyperscan.hs_scan_vector(db, dataPtrs, lengths, input.length, 0, scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL);
            }
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
        }
    }

    @Override
    public int scanVectorNoLenArrayRaw(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler) {
        if (database == null) {
            return hyperscan.HS_INVALID();
        }
        MemorySegment db = nativeDatabase(database);
        MemorySegment scratch = scanner == null ? MemorySegment.NULL : nativeScratch(scanner);
        List<DualExpression> expressions = database instanceof PanamaNativeDatabase nativeDb ? nativeDb.expressions() : List.of();
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, expressions));
        }
        try {
            try (Arena arena = Arena.ofConfined()) {
                if (input == null) {
                    return hyperscan.hs_scan_vector(db, MemorySegment.NULL, MemorySegment.NULL, 2, 0, scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL);
                }
                MemorySegment dataPtrs = arena.allocate(input.length * ValueLayout.ADDRESS.byteSize());
                for (int i = 0; i < input.length; i++) {
                    byte[] data = input[i];
                    MemorySegment dataSeg = data == null ? MemorySegment.NULL : allocateBytes(arena, data);
                    dataPtrs.setAtIndex(ValueLayout.ADDRESS, i, dataSeg);
                }
                return hyperscan.hs_scan_vector(db, dataPtrs, MemorySegment.NULL, input.length, 0, scratch, handler == null ? MemorySegment.NULL : MATCH_HANDLER, MemorySegment.NULL);
            }
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
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
        return hyperscan.hs_free_database(nativeDatabase(database));
    }

    @Override
    public int freeScratchRaw(DualScanner scanner) {
        if (scanner == null) {
            return hyperscan.HS_SUCCESS();
        }
        return hyperscan.hs_free_scratch(nativeScratch(scanner));
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
            String message = readCompileErrorMessage(err);
            if (err != null && err != MemorySegment.NULL) {
                hyperscan.hs_free_compile_error(err);
            }
            MemorySegment info = nullInfo ? MemorySegment.NULL : reinterpretString(infoOut.get(ValueLayout.ADDRESS, 0));
            if (info != null && info != MemorySegment.NULL) {
                freeSegment(info);
            }
            return new DualResult<>(result, null, message);
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
            String message = readCompileErrorMessage(err);
            if (err != null && err != MemorySegment.NULL) {
                hyperscan.hs_free_compile_error(err);
            }
            MemorySegment info = nullInfo ? MemorySegment.NULL : reinterpretString(infoOut.get(ValueLayout.ADDRESS, 0));
            if (info != null && info != MemorySegment.NULL) {
                freeSegment(info);
            }
            return new DualResult<>(result, null, message);
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
            String message = readCompileErrorMessage(err);
            if (err != null && err != MemorySegment.NULL) {
                hyperscan.hs_free_compile_error(err);
            }
            MemorySegment info = reinterpretExprInfo(infoOut.get(ValueLayout.ADDRESS, 0));
            DualExpressionInfo value = null;
            if (result == 0 && info != null && info != MemorySegment.NULL) {
                value = new DualExpressionInfo(
                        Integer.toUnsignedLong(hs_expr_info.min_width(info)),
                        Integer.toUnsignedLong(hs_expr_info.max_width(info)),
                        hs_expr_info.unordered_matches(info) != 0,
                        hs_expr_info.matches_at_eod(info) != 0,
                        hs_expr_info.matches_only_at_eod(info) != 0);
                freeSegment(info);
            }
            return new DualResult<>(result, value, message);
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
            String message = readCompileErrorMessage(err);
            if (err != null && err != MemorySegment.NULL) {
                hyperscan.hs_free_compile_error(err);
            }
            MemorySegment info = reinterpretExprInfo(infoOut.get(ValueLayout.ADDRESS, 0));
            DualExpressionInfo value = null;
            if (result == 0 && info != null && info != MemorySegment.NULL) {
                value = new DualExpressionInfo(
                        Integer.toUnsignedLong(hs_expr_info.min_width(info)),
                        Integer.toUnsignedLong(hs_expr_info.max_width(info)),
                        hs_expr_info.unordered_matches(info) != 0,
                        hs_expr_info.matches_at_eod(info) != 0,
                        hs_expr_info.matches_only_at_eod(info) != 0);
                freeSegment(info);
            }
            return new DualResult<>(result, value, message);
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
            return nativeDb.database();
        }
        if (database instanceof PanamaRawDatabase rawDb) {
            return rawDb.database();
        }
        throw new IllegalArgumentException("Unsupported database type: " + database.getClass());
    }

    private static MemorySegment getNativeDatabaseHandle(com.xenoamess.hyperscan_panama.wrapper.Database database) {
        try {
            Method method = com.xenoamess.hyperscan_panama.wrapper.Database.class.getDeclaredMethod("getDatabase");
            method.setAccessible(true);
            return (MemorySegment) method.invoke(database);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static MemorySegment nativeScratch(DualScanner scanner) {
        if (scanner == null) {
            return MemorySegment.NULL;
        }
        if (scanner instanceof PanamaRawScanner raw) {
            return raw.scratch();
        }
        if (scanner instanceof PanamaScanner wrapper) {
            return getNativeScratchHandle(wrapper.scanner());
        }
        throw new IllegalArgumentException("Unsupported scanner type: " + scanner.getClass());
    }

    private static MemorySegment getNativeScratchHandle(Scanner scanner) {
        try {
            Field stateField = Scanner.class.getDeclaredField("state");
            stateField.setAccessible(true);
            Object state = stateField.get(scanner);
            Method getScratch = state.getClass().getDeclaredMethod("getScratch");
            getScratch.setAccessible(true);
            return (MemorySegment) getScratch.invoke(state);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static MemorySegment nativeStream(DualStream stream) {
        if (stream == null) {
            return MemorySegment.NULL;
        }
        if (stream instanceof PanamaStream s) {
            return s.stream;
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
            String result = info.getString(0);
            freeSegment(info);
            return result;
        }
    }

    private static DualExpression findExpressionById(List<DualExpression> expressions, int id) {
        for (DualExpression expr : expressions) {
            if (expr.id() != null && expr.id() == id) {
                return expr;
            }
        }
        return null;
    }

    private static String readCompileErrorMessage(MemorySegment err) {
        if (err == null || err == MemorySegment.NULL) {
            return null;
        }
        MemorySegment msg = hs_compile_error.message(err);
        if (msg == null || msg == MemorySegment.NULL) {
            return null;
        }
        return reinterpretString(msg).getString(0);
    }

    private static MemorySegment newDefaultExprExt(Arena arena) {
        MemorySegment ext = arena.allocate(hs_expr_ext.layout());
        hs_expr_ext.flags(ext, 0L);
        hs_expr_ext.min_offset(ext, 0L);
        hs_expr_ext.max_offset(ext, -1L);
        hs_expr_ext.min_length(ext, 0L);
        hs_expr_ext.edit_distance(ext, 0);
        hs_expr_ext.hamming_distance(ext, 0);
        return ext;
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
            database.close();
        }
    }

    private record PanamaScanner(Scanner scanner) implements DualScanner {
        @Override
        public long getSize() {
            return scanner.getSize();
        }

        @Override
        public void close() {
            try {
                scanner.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private record PanamaNativeDatabase(MemorySegment database, List<DualExpression> expressions) implements DualDatabase {
        @Override
        public long getSize() {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment sizeOut = zeroLongOut(arena);
                checkResult(hyperscan.hs_database_size(database, sizeOut));
                return sizeOut.get(ValueLayout.JAVA_LONG, 0);
            }
        }

        @Override
        public void close() {
            checkResult(hyperscan.hs_free_database(database));
        }
    }

    private record PanamaRawDatabase(MemorySegment database, MemorySegment memory, Arena arena, boolean owner) implements DualDatabase {
        @Override
        public long getSize() {
            try (Arena local = Arena.ofConfined()) {
                MemorySegment sizeOut = zeroLongOut(local);
                checkResult(hyperscan.hs_database_size(database, sizeOut));
                return sizeOut.get(ValueLayout.JAVA_LONG, 0);
            }
        }

        @Override
        public void close() {
            if (owner && arena != null) {
                arena.close();
            }
        }
    }

    private record PanamaRawScanner(MemorySegment scratch) implements DualScanner {
        @Override
        public long getSize() {
            try (Arena arena = Arena.ofConfined()) {
                MemorySegment sizeOut = zeroLongOut(arena);
                checkResult(hyperscan.hs_scratch_size(scratch, sizeOut));
                return sizeOut.get(ValueLayout.JAVA_LONG, 0);
            }
        }

        @Override
        public void close() {
            checkResult(hyperscan.hs_free_scratch(scratch));
        }
    }

    private static final class PanamaStream implements DualStream {
        final MemorySegment stream;
        final MemorySegment scratch;
        final List<DualExpression> expressions;
        boolean closed;

        PanamaStream(MemorySegment stream, MemorySegment scratch, List<DualExpression> expressions) {
            this.stream = stream;
            this.scratch = scratch;
            this.expressions = expressions;
        }

        @Override
        public void close() {
            if (closed) {
                return;
            }
            closed = true;
            checkResult(hyperscan.hs_close_stream(stream, scratch == null ? MemorySegment.NULL : scratch, MemorySegment.NULL, MemorySegment.NULL));
            if (scratch != null && scratch != MemorySegment.NULL) {
                checkResult(hyperscan.hs_free_scratch(scratch));
            }
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
