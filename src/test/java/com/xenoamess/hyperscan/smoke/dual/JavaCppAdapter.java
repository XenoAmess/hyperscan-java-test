package com.xenoamess.hyperscan.smoke.dual;

import com.gliwka.hyperscan.jni.HyperscanNativeLoader;
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaCppAdapter implements DualApi {

    static {
        HyperscanNativeLoader.load();
    }

    private static final ThreadLocal<HandlerContext> STREAM_CALLBACK = new ThreadLocal<>();

    private static final match_event_handler MATCH_HANDLER = new match_event_handler() {
        @Override
        public int call(int id, long from, long to, int flags, Pointer context) {
            HandlerContext ctx = STREAM_CALLBACK.get();
            if (ctx == null) {
                return 0;
            }
            DualExpression expression = findExpressionById(ctx.expressions(), id);
            if (expression == null) {
                expression = new DualExpression("", EnumSet.noneOf(DualExpressionFlag.class), id);
            }
            return ctx.handler().onMatch(expression, from, to) ? 0 : -1;
        }
    };

    private static final hs_expr_ext_t DEFAULT_EXPR_EXT = newDefaultExprExt();

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
                    long address = alloc.allocate(size);
                    if (address == 0) {
                        return MemorySegment.NULL;
                    }
                    return MemorySegment.ofAddress(address).reinterpret(size);
                }, HS_LIBRARY_ARENA);
            }
            MemorySegment freeSeg;
            if (free == null) {
                freeSeg = MemorySegment.NULL;
            } else {
                freeSeg = com.xenoamess.hyperscan_panama.jni.generated.hs_free_t.allocate(ptr -> {
                    if (ptr == null || ptr.address() == 0) {
                        return;
                    }
                    free.free(ptr.address());
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
                long address = alloc.allocate(size);
                if (address == 0) {
                    return null;
                }
                return new OffsetPointer(address);
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
                if (ptr == null) {
                    return;
                }
                free.free(ptr.address());
            }
        };
    }

    @Override
    public void setAllocator(DualAllocator alloc, DualFree free) {
        currentAllocator = wrapAllocator(alloc);
        currentFree = wrapFree(free);
        currentDualAllocator = alloc;
        currentDualFree = free;
        checkResult(hyperscan.hs_set_allocator(currentAllocator, currentFree));
        setHsLibraryAllocator("hs_set_allocator", alloc, free);
    }

    @Override
    public void setDatabaseAllocator(DualAllocator alloc, DualFree free) {
        currentDatabaseAllocator = wrapAllocator(alloc);
        currentDatabaseFree = wrapFree(free);
        currentDualDatabaseAllocator = alloc;
        currentDualDatabaseFree = free;
        checkResult(hyperscan.hs_set_database_allocator(currentDatabaseAllocator, currentDatabaseFree));
        setHsLibraryAllocator("hs_set_database_allocator", alloc, free);
    }

    @Override
    public void setMiscAllocator(DualAllocator alloc, DualFree free) {
        currentMiscAllocator = wrapAllocator(alloc);
        currentMiscFree = wrapFree(free);
        currentDualMiscAllocator = alloc;
        currentDualMiscFree = free;
        checkResult(hyperscan.hs_set_misc_allocator(currentMiscAllocator, currentMiscFree));
        setHsLibraryAllocator("hs_set_misc_allocator", alloc, free);
    }

    @Override
    public void setScratchAllocator(DualAllocator alloc, DualFree free) {
        currentScratchAllocator = wrapAllocator(alloc);
        currentScratchFree = wrapFree(free);
        currentDualScratchAllocator = alloc;
        currentDualScratchFree = free;
        checkResult(hyperscan.hs_set_scratch_allocator(currentScratchAllocator, currentScratchFree));
        setHsLibraryAllocator("hs_set_scratch_allocator", alloc, free);
    }

    @Override
    public void setStreamAllocator(DualAllocator alloc, DualFree free) {
        currentStreamAllocator = wrapAllocator(alloc);
        currentStreamFree = wrapFree(free);
        currentDualStreamAllocator = alloc;
        currentDualStreamFree = free;
        checkResult(hyperscan.hs_set_stream_allocator(currentStreamAllocator, currentStreamFree));
        setHsLibraryAllocator("hs_set_stream_allocator", alloc, free);
    }

    private static MemorySegment toSegment(Pointer p) {
        return p == null ? MemorySegment.NULL : MemorySegment.ofAddress(p.address());
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
        ext.flags(0L);
        ext.min_offset(0L);
        ext.max_offset(-1L);
        ext.min_length(0L);
        ext.edit_distance(0);
        ext.hamming_distance(0);
        return ext;
    }

    private static void applyExprExt(hs_expr_ext_t ext, DualExpressionExt src) {
        ext.flags(src.flags());
        ext.min_offset(src.minOffset());
        ext.max_offset(src.maxOffset());
        ext.min_length(src.minLength());
        ext.edit_distance(src.editDistance());
        ext.hamming_distance(src.hammingDistance());
    }

    private record HandlerContext(DualByteMatchHandler handler, List<DualExpression> expressions) {
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
                return new JavaCppWrapperDatabase(com.gliwka.hyperscan.wrapper.Database.compile(javaCppExpressions));
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
        JavaCppWrapperDatabase db = (JavaCppWrapperDatabase) database;
        s.scanner.allocScratch(db.database);
    }

    @Override
    public List<DualMatch> scan(DualScanner scanner, DualDatabase database, String input) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        JavaCppWrapperDatabase db = (JavaCppWrapperDatabase) database;
        List<Match> matches = s.scanner.scan(db.database, input);
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
        JavaCppWrapperDatabase db = (JavaCppWrapperDatabase) database;
        s.scanner.scan(db.database, input, new StringMatchEventHandler() {
            @Override
            public boolean onMatch(Expression expression, long from, long to) {
                return handler.onMatch(toDualExpression(expression), from, to);
            }
        });
    }

    @Override
    public void scan(DualScanner scanner, DualDatabase database, byte[] input, DualByteMatchHandler handler) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        JavaCppWrapperDatabase db = (JavaCppWrapperDatabase) database;
        s.scanner.scan(db.database, input, new ByteMatchEventHandler() {
            @Override
            public boolean onMatch(Expression expression, long from, long to) {
                return handler.onMatch(toDualExpression(expression), from, to);
            }
        });
    }

    @Override
    public boolean hasMatch(DualScanner scanner, DualDatabase database, String input) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        JavaCppWrapperDatabase db = (JavaCppWrapperDatabase) database;
        return s.scanner.hasMatch(db.database, input);
    }

    @Override
    public boolean hasMatch(DualScanner scanner, DualDatabase database, byte[] input) {
        JavaCppScanner s = (JavaCppScanner) scanner;
        JavaCppWrapperDatabase db = (JavaCppWrapperDatabase) database;
        return s.scanner.hasMatch(db.database, input);
    }

    @Override
    public DualStream openStream(DualDatabase database) {
        hs_database_t db = nativeDatabase(database);
        List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb
                ? nativeDb.expressions : List.of();
        try (PointerPointer<hs_stream_t> streamOut = new PointerPointer<>(1)) {
            streamOut.put(0, new hs_stream_t());
            checkResult(hyperscan.hs_open_stream(db, 0, streamOut));
            hs_stream_t stream = streamOut.get(hs_stream_t.class);
            try (PointerPointer<hs_scratch_t> scratchOut = new PointerPointer<>(1)) {
                scratchOut.put(0, new hs_scratch_t());
                checkResult(hyperscan.hs_alloc_scratch(db, scratchOut));
                hs_scratch_t scratch = scratchOut.get(hs_scratch_t.class);
                return new JavaCppStream(stream, scratch, expressions);
            }
        }
    }

    @Override
    public void scanStream(DualScanner scanner, DualStream stream, byte[] input, DualByteMatchHandler handler) {
        JavaCppStream s = (JavaCppStream) stream;
        if (s.closed) {
            throw new IllegalStateException("Stream is already closed");
        }
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, s.expressions));
        }
        try (BytePointer data = new BytePointer(input.length)) {
            data.put(input);
            data.position(0);
            int result = hyperscan.hs_scan_stream(s.stream, data, input.length, 0, s.scratch, MATCH_HANDLER, null);
            if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED) {
                checkResult(result);
            }
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
        }
    }

    @Override
    public void closeStream(DualScanner scanner, DualStream stream, DualByteMatchHandler handler) {
        JavaCppStream s = (JavaCppStream) stream;
        if (s.closed) {
            return;
        }
        s.closed = true;
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, s.expressions));
        }
        try {
            int result = hyperscan.hs_close_stream(s.stream, s.scratch, MATCH_HANDLER, null);
            if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED) {
                checkResult(result);
            }
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
            checkResult(hyperscan.hs_free_scratch(s.scratch));
        }
    }

    @Override
    public void scanVector(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler) {
        hs_database_t db = nativeDatabase(database);
        List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb ? nativeDb.expressions : List.of();
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, expressions));
        }
        int[] lengths = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            lengths[i] = input[i].length;
        }
        try (PointerPointer<BytePointer> data = new PointerPointer<>(input);
             IntPointer lengthPtr = new IntPointer(lengths);
             PointerPointer<hs_scratch_t> scratchOut = new PointerPointer<>(1)) {
            scratchOut.put(0, new hs_scratch_t());
            checkResult(hyperscan.hs_alloc_scratch(db, scratchOut));
            hs_scratch_t scratch = scratchOut.get(hs_scratch_t.class);
            try {
                int result = hyperscan.hs_scan_vector(db, data, lengthPtr, input.length, 0, scratch, MATCH_HANDLER, null);
                if (result != 0 && result != hyperscan.HS_SCAN_TERMINATED) {
                    checkResult(result);
                }
            } finally {
                checkResult(hyperscan.hs_free_scratch(scratch));
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
        try (BytePointer bp = new BytePointer(data);
             PointerPointer<BytePointer> info = new PointerPointer<>(1)) {
            info.put(0, new BytePointer());
            checkResult(hyperscan.hs_serialized_database_info(bp, data.length, info));
            BytePointer infoPtr = info.get(BytePointer.class);
            String result = infoPtr.getString();
            Pointer.free(infoPtr);
            return result;
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
             BytePointer bytes = new BytePointer(1)) {
            size.put(0, 0);
            checkResult(hyperscan.hs_serialize_database(nativeDb.database, bytes, size));
            long length = size.get();
            bytes.capacity(length);
            java.nio.ByteBuffer buffer = bytes.asBuffer();
            byte[] out = new byte[(int) length];
            buffer.get(out);
            return out;
        }
    }

    @Override
    public DualDatabase deserialize(byte[] data) {
        try {
            return new JavaCppWrapperDatabase(com.gliwka.hyperscan.wrapper.Database.load(new ByteArrayInputStream(data)));
        } catch (Exception e) {
            return deserializeNative(data);
        }
    }

    private static DualDatabase deserializeNative(byte[] data) {
        try (BytePointer bp = new BytePointer(data);
             PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1)) {
            dbOut.put(0, new hs_database_t());
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
            wrapper.database.close();
        } else if (database instanceof JavaCppNativeDatabase nativeDb) {
            checkResult(hyperscan.hs_free_database(nativeDb.database));
        } else if (database instanceof JavaCppRawDatabase rawDb) {
            rawDb.close();
        }
    }

    @Override
    public long getDatabaseSize(DualDatabase database) {
        if (database instanceof JavaCppWrapperDatabase wrapper) {
            return wrapper.database.getSize();
        }
        JavaCppNativeDatabase nativeDb = (JavaCppNativeDatabase) database;
        try (SizeTPointer size = new SizeTPointer(1)) {
            checkResult(hyperscan.hs_database_size(nativeDb.database, size));
            return size.get();
        }
    }

    @Override
    public long getScannerSize(DualScanner scanner) {
        return ((JavaCppScanner) scanner).scanner.getSize();
    }

    @Override
    public String getVersion() {
        return com.gliwka.hyperscan.wrapper.Scanner.getVersion();
    }

    @Override
    public String getPlatform() {
        return HyperscanNativeLoader.selectPlatform();
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
        try (PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
            dbOut.put(0, new hs_database_t());
            errOut.put(0, new hs_compile_error_t());
            BytePointer expr = pattern == null ? new BytePointer() : new BytePointer(pattern);
            int result = (int) HsLibrary.HS_COMPILE.invokeExact(toSegment(expr), flags, mode, MemorySegment.NULL, toSegment(dbOut), toSegment(errOut));
            return buildCompileResult(result, dbOut, errOut, pattern == null ? List.of() : List.of(new DualExpression(pattern, EnumSet.noneOf(DualExpressionFlag.class), 0)));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualCompileResult compileRaw(String pattern, int flags, int mode, DualPlatformInfo platform) {
        try (PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1);
             hs_platform_info_t plat = new hs_platform_info_t()) {
            dbOut.put(0, new hs_database_t());
            errOut.put(0, new hs_compile_error_t());
            plat.tune(platform.tune());
            plat.cpu_features(platform.cpuFeatures());
            BytePointer expr = pattern == null ? new BytePointer() : new BytePointer(pattern);
            int result = (int) HsLibrary.HS_COMPILE.invokeExact(toSegment(expr), flags, mode, toSegment(plat), toSegment(dbOut), toSegment(errOut));
            return buildCompileResult(result, dbOut, errOut, pattern == null ? List.of() : List.of(new DualExpression(pattern, EnumSet.noneOf(DualExpressionFlag.class), 0)));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DualCompileResult compileExtRaw(String pattern, int flags, DualExpressionExt ext, int mode) {
        try (PointerPointer<BytePointer> exprPtr = new PointerPointer<>(1);
             IntPointer flagsPtr = new IntPointer(1);
             IntPointer idsPtr = new IntPointer(1);
             hs_expr_ext_t extStruct = new hs_expr_ext_t();
             PointerPointer<hs_expr_ext_t> extPtr = new PointerPointer<>(1);
             PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
            BytePointer expr = pattern == null ? new BytePointer() : new BytePointer(pattern);
            exprPtr.put(0, expr);
            flagsPtr.put(0, flags);
            idsPtr.put(0, 0);
            applyExprExt(extStruct, ext);
            extPtr.put(0, extStruct);
            dbOut.put(0, new hs_database_t());
            errOut.put(0, new hs_compile_error_t());
            int result = hyperscan.hs_compile_ext_multi(exprPtr, flagsPtr, idsPtr, extPtr, 1, mode, null, dbOut, errOut);
            return buildCompileResult(result, dbOut, errOut, pattern == null ? List.of() : List.of(new DualExpression(pattern, EnumSet.noneOf(DualExpressionFlag.class), 0)));
        }
    }

    private static DualCompileResult buildCompileResult(int result, PointerPointer<hs_database_t> dbOut, PointerPointer<hs_compile_error_t> errOut, List<DualExpression> expressions) {
        if (result == 0) {
            return new DualCompileResult(0, new JavaCppNativeDatabase(dbOut.get(hs_database_t.class), List.copyOf(expressions)), null);
        }
        hs_compile_error_t err = errOut.get(hs_compile_error_t.class);
        String message = err != null && err.message() != null ? err.message().getString() : null;
        try {
            int ignored = (int) HsLibrary.HS_FREE_COMPILE_ERROR.invokeExact(toSegment(err));
        } catch (Throwable e) {
            throw new RuntimeException(e);
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
        PointerPointer<BytePointer> expressionsPtr = null;
        IntPointer flags = null;
        IntPointer ids = null;
        List<BytePointer> exprPointers = new ArrayList<>();
        if (n > 0) {
            expressionsPtr = new PointerPointer<>(n);
            flags = new IntPointer(n);
            ids = new IntPointer(n);
            for (int i = 0; i < n; i++) {
                DualExpression expr = expressions.get(i);
                BytePointer bp = new BytePointer(expr.pattern());
                exprPointers.add(bp);
                expressionsPtr.put(i, bp);
                flags.put(i, toFlagBits(expr.flags()));
                ids.put(i, expr.id() != null ? expr.id() : 0);
            }
        }
        try (PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
            dbOut.put(0, new hs_database_t());
            errOut.put(0, new hs_compile_error_t());
            int result = hyperscan.hs_compile_multi(expressionsPtr, flags, ids, n, mode, null, dbOut, errOut);
            if (result == 0) {
                return new DualCompileResult(0, new JavaCppNativeDatabase(dbOut.get(hs_database_t.class), List.copyOf(expressions)), null);
            }
            hs_compile_error_t err = errOut.get(hs_compile_error_t.class);
            String message = err != null && err.message() != null ? err.message().getString() : null;
            hyperscan.hs_free_compile_error(err);
            return new DualCompileResult(result, null, message);
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
            if (patterns != null) {
                expressionsPtr = new PointerPointer<>(n);
                for (int i = 0; i < n; i++) {
                    BytePointer expr = patterns[i] == null ? new BytePointer() : new BytePointer(patterns[i]);
                    exprPointers.add(expr);
                    expressionsPtr.put(i, expr);
                    expressions.add(new DualExpression(patterns[i] != null ? patterns[i] : "", EnumSet.noneOf(DualExpressionFlag.class), ids != null ? ids[i] : 0));
                }
                flagsPtr = flags == null ? null : new IntPointer(flags);
                idsPtr = ids == null ? null : new IntPointer(ids);
            }

        try (PointerPointer<hs_database_t> dbOut = new PointerPointer<>(1);
             PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
            hs_database_t dbPlaceholder = new hs_database_t();
            hs_compile_error_t errPlaceholder = new hs_compile_error_t();
            dbOut.put(0, dbPlaceholder);
            errOut.put(0, errPlaceholder);
            int result = hyperscan.hs_compile_multi(expressionsPtr, flagsPtr, idsPtr, n, mode, null, dbOut, errOut);
            return buildCompileResult(result, dbOut, errOut, expressions);
        }
    }

    @Override
    public DualCompileResult compileNullOutputRaw(String pattern, int flags, int mode) {
        try (PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
            errOut.put(0, new hs_compile_error_t());
            BytePointer expr = pattern == null ? new BytePointer() : new BytePointer(pattern);
            int result = hyperscan.hs_compile(expr, flags, mode, null, (PointerPointer<hs_database_t>) null, errOut);
            hs_compile_error_t err = errOut.get(hs_compile_error_t.class);
            String message = err != null && err.message() != null ? err.message().getString() : null;
            hyperscan.hs_free_compile_error(err);
            return new DualCompileResult(result, null, message);
        }
    }

    @Override
    public DualCompileResult compileMultiNullOutputRaw(String[] patterns, int[] flags, int[] ids, int mode) {
        int n = patterns == null ? 0 : patterns.length;
        PointerPointer<BytePointer> expressionsPtr = null;
        IntPointer flagsPtr = null;
        IntPointer idsPtr = null;
        List<BytePointer> exprPointers = new ArrayList<>();
        if (patterns != null) {
            expressionsPtr = new PointerPointer<>(n);
            for (int i = 0; i < n; i++) {
                BytePointer expr = patterns[i] == null ? new BytePointer() : new BytePointer(patterns[i]);
                exprPointers.add(expr);
                expressionsPtr.put(i, expr);
            }
            flagsPtr = flags == null ? null : new IntPointer(flags);
            idsPtr = ids == null ? null : new IntPointer(ids);
        }
        try (PointerPointer<hs_compile_error_t> errOut = new PointerPointer<>(1)) {
            hs_compile_error_t errPlaceholder = new hs_compile_error_t();
            errOut.put(0, errPlaceholder);
            int result = hyperscan.hs_compile_multi(expressionsPtr, flagsPtr, idsPtr, n, mode, null, (PointerPointer<hs_database_t>) null, errOut);
            hs_compile_error_t err = errOut.get(hs_compile_error_t.class);
            String message = err != null && err.message() != null ? err.message().getString() : null;
            hyperscan.hs_free_compile_error(err);
            return new DualCompileResult(result, null, message);
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
            dbOut.put(0, new hs_database_t());
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
            Pointer.free(memory);
            throw new RuntimeException("Raw database memory is not 8-byte aligned");
        }
        hs_database_t database = new hs_database_t(memory);
        return new JavaCppRawDatabase(database, memory, true);
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
        hs_database_t db = new hs_database_t(new OffsetPointer(raw.memory.address() + offset));
        return new JavaCppRawDatabase(db, raw.memory, false);
    }

    @Override
    public DualResult<String> getDatabaseInfoRaw(DualDatabase database) {
        if (database == null) {
            return DualResult.error(hyperscan.HS_INVALID);
        }
        try (PointerPointer<BytePointer> info = new PointerPointer<>(1)) {
            info.put(0, new BytePointer());
            int result = (int) HsLibrary.HS_DATABASE_INFO.invokeExact(toSegment(nativeDatabase(database)), toSegment(info));
            if (result == 0) {
                BytePointer infoPtr = info.get(BytePointer.class);
                String value = infoPtr.getString();
                Pointer.free(infoPtr);
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
            return DualResult.error(hyperscan.HS_INVALID);
        }
        try (BytePointer bp = new BytePointer(data.length);
             PointerPointer<BytePointer> info = new PointerPointer<>(1)) {
            bp.put(data);
            bp.position(0);
            info.put(0, new BytePointer());
            int result = hyperscan.hs_serialized_database_info(bp, data.length, info);
            if (result == 0) {
                BytePointer infoPtr = info.get(BytePointer.class);
                String value = infoPtr.getString();
                Pointer.free(infoPtr);
                return DualResult.success(value);
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
            scratchOut.put(0, new hs_scratch_t());
            int result = (int) HsLibrary.HS_ALLOC_SCRATCH.invokeExact(toSegment(nativeDatabase(database)), toSegment(scratchOut));
            if (result == 0) {
                return new DualScratchResult(0, new JavaCppRawScanner(scratchOut.get(hs_scratch_t.class)), null);
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
        try (PointerPointer<hs_scratch_t> scratchOut = new PointerPointer<>(1)) {
            scratchOut.put(0, existingScratch == null ? null : nativeScratch(existingScratch));
            int result = hyperscan.hs_alloc_scratch(nativeDatabase(database), scratchOut);
            if (result == 0) {
                return new DualScratchResult(0, new JavaCppRawScanner(scratchOut.get(hs_scratch_t.class)), null);
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
            clonedOut.put(0, new hs_scratch_t());
            int result = hyperscan.hs_clone_scratch(src, clonedOut);
            if (result == 0) {
                return new DualScratchResult(0, new JavaCppRawScanner(clonedOut.get(hs_scratch_t.class)), null);
            }
            return new DualScratchResult(result, null, null);
        }
    }

    @Override
    public DualStreamResult openStreamRaw(DualDatabase database) {
        if (database == null) {
            return new DualStreamResult(hyperscan.HS_INVALID, null, null);
        }
        hs_database_t db = nativeDatabase(database);
        try (PointerPointer<hs_stream_t> streamOut = new PointerPointer<>(1)) {
            streamOut.put(0, new hs_stream_t());
            int result = hyperscan.hs_open_stream(db, 0, streamOut);
            if (result != 0) {
                return new DualStreamResult(result, null, null);
            }
            hs_stream_t stream = streamOut.get(hs_stream_t.class);
            try (PointerPointer<hs_scratch_t> scratchOut = new PointerPointer<>(1)) {
                scratchOut.put(0, new hs_scratch_t());
                int allocResult = hyperscan.hs_alloc_scratch(db, scratchOut);
                if (allocResult != 0) {
                    hyperscan.hs_close_stream(stream, null, null, null);
                    return new DualStreamResult(allocResult, null, null);
                }
                hs_scratch_t scratch = scratchOut.get(hs_scratch_t.class);
                List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb ? nativeDb.expressions : List.of();
                return new DualStreamResult(0, new JavaCppStream(stream, scratch, expressions), null);
            }
        }
    }

    @Override
    public DualScanner getStreamScratch(DualStream stream) {
        return new JavaCppRawScanner(((JavaCppStream) stream).scratch);
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
        hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, s.expressions));
        }
        try (BytePointer data = input == null ? new BytePointer() : new BytePointer(input.length)) {
            if (input != null) {
                data.put(input);
                data.position(0);
            }
            return hyperscan.hs_scan_stream(s.stream, data, input == null ? 4 : input.length, 0, scratch, handler == null ? null : MATCH_HANDLER, null);
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
        }
    }

    @Override
    public int closeStreamRaw(DualStream stream, DualScanner scanner, DualByteMatchHandler handler) {
        if (stream == null) {
            return hyperscan.HS_INVALID;
        }
        JavaCppStream s = (JavaCppStream) stream;
        if (s.closed) {
            return hyperscan.HS_INVALID;
        }
        s.closed = true;
        hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, s.expressions));
        }
        try {
            return hyperscan.hs_close_stream(s.stream, scratch, handler == null ? null : MATCH_HANDLER, null);
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
            return hyperscan.HS_INVALID;
        }
        JavaCppStream s = (JavaCppStream) stream;
        hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, s.expressions));
        }
        try {
            return hyperscan.hs_reset_stream(s.stream, 0, scratch, handler == null ? null : MATCH_HANDLER, null);
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
        }
    }

    @Override
    public int copyStreamRaw(DualStream[] to, DualStream from) {
        if (from == null || to == null || to.length == 0) {
            return hyperscan.HS_INVALID;
        }
        JavaCppStream src = (JavaCppStream) from;
        try (PointerPointer<hs_stream_t> toOut = new PointerPointer<>(1)) {
            toOut.put(0, new hs_stream_t());
            int result = hyperscan.hs_copy_stream(toOut, src.stream);
            if (result == 0) {
                hs_stream_t copied = toOut.get(hs_stream_t.class);
                to[0] = new JavaCppStream(copied, null, src.expressions);
            }
            return result;
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
        hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, toStream.expressions));
        }
        try {
            return hyperscan.hs_reset_and_copy_stream(toStream.stream, fromStream.stream, scratch, handler == null ? null : MATCH_HANDLER, null);
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
        }
    }

    @Override
    public int scanRaw(DualScanner scanner, DualDatabase database, byte[] input, DualByteMatchHandler handler) {
        if (database == null) {
            return hyperscan.HS_INVALID;
        }
        hs_database_t db = nativeDatabase(database);
        hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
        List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb ? nativeDb.expressions : List.of();
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, expressions));
        }
        try (BytePointer data = input == null ? new BytePointer() : new BytePointer(input.length)) {
            if (input != null) {
                data.put(input);
                data.position(0);
            }
            return hyperscan.hs_scan(db, data, input == null ? 4 : input.length, 0, scratch, handler == null ? null : MATCH_HANDLER, null);
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
            }
        }
    }

    @Override
    public int scanVectorRaw(DualScanner scanner, DualDatabase database, byte[][] input, DualByteMatchHandler handler) {
        if (database == null) {
            return hyperscan.HS_INVALID;
        }
        hs_database_t db = nativeDatabase(database);
        hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
        List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb ? nativeDb.expressions : List.of();
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, expressions));
        }
        try {
            if (input == null) {
                return hyperscan.hs_scan_vector(db, (PointerPointer<BytePointer>) null, (IntPointer) null, 2, 0, scratch, handler == null ? null : MATCH_HANDLER, null);
            }
            int[] lengths = new int[input.length];
            for (int i = 0; i < input.length; i++) {
                lengths[i] = input[i] == null ? 4 : input[i].length;
            }
            try (PointerPointer<BytePointer> data = new PointerPointer<>(input.length);
                 IntPointer lengthPtr = new IntPointer(lengths)) {
                List<BytePointer> bpRefs = new ArrayList<>(input.length);
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
                return hyperscan.hs_scan_vector(db, data, lengthPtr, input.length, 0, scratch, handler == null ? null : MATCH_HANDLER, null);
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
            return hyperscan.HS_INVALID;
        }
        hs_database_t db = nativeDatabase(database);
        hs_scratch_t scratch = scanner == null ? null : nativeScratch(scanner);
        List<DualExpression> expressions = database instanceof JavaCppNativeDatabase nativeDb ? nativeDb.expressions : List.of();
        if (handler != null) {
            STREAM_CALLBACK.set(new HandlerContext(handler, expressions));
        }
        try (PointerPointer<BytePointer> data = new PointerPointer<>(input.length)) {
            List<BytePointer> bpRefs = new ArrayList<>(input.length);
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
            return hyperscan.hs_scan_vector(db, data, (IntPointer) null, input.length, 0, scratch, handler == null ? null : MATCH_HANDLER, null);
        } finally {
            if (handler != null) {
                STREAM_CALLBACK.remove();
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
            bytesOut.put(0, new BytePointer());
            int result = (int) HsLibrary.HS_SERIALIZE_DATABASE.invokeExact(toSegment(db), toSegment(bytesOut), toSegment(size));
            if (result != 0) {
                return DualResult.error(result);
            }
            long length = size.get();
            BytePointer bytes = bytesOut.get(BytePointer.class);
            bytes.capacity(length);
            java.nio.ByteBuffer buffer = bytes.asBuffer();
            byte[] out = new byte[(int) length];
            buffer.get(out);
            return DualResult.success(out);
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
        return hyperscan.hs_free_database(nativeDatabase(database));
    }

    @Override
    public int freeScratchRaw(DualScanner scanner) {
        if (scanner == null) {
            return hyperscan.HS_SUCCESS;
        }
        return hyperscan.hs_free_scratch(nativeScratch(scanner));
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
        BytePointer expr = pattern == null ? new BytePointer() : new BytePointer(pattern);
        PointerPointer<hs_expr_info_t> infoOut = nullInfo ? null : new PointerPointer<>(1);
        PointerPointer<hs_compile_error_t> errOut = nullErr ? null : new PointerPointer<>(1);
        if (infoOut != null) {
            infoOut.put(0, new hs_expr_info_t());
        }
        if (errOut != null) {
            errOut.put(0, new hs_compile_error_t());
        }
        try {
            int result;
            try {
                result = (int) HsLibrary.HS_EXPRESSION_INFO.invokeExact(toSegment(expr), toFlagBits(flags), toSegment(infoOut), toSegment(errOut));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            hs_compile_error_t err = errOut == null ? null : errOut.get(hs_compile_error_t.class);
            String message = err != null && err.message() != null ? err.message().getString() : null;
            try {
                int ignored = (int) HsLibrary.HS_FREE_COMPILE_ERROR.invokeExact(toSegment(err));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            hs_expr_info_t info = infoOut == null ? null : infoOut.get(hs_expr_info_t.class);
            if (info != null) {
                Pointer.free(info);
            }
            return new DualResult<>(result, null, message);
        } finally {
            if (infoOut != null) {
                infoOut.close();
            }
            if (errOut != null) {
                errOut.close();
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
        BytePointer expr = pattern == null ? new BytePointer() : new BytePointer(pattern);
        PointerPointer<hs_expr_info_t> infoOut = nullInfo ? null : new PointerPointer<>(1);
        PointerPointer<hs_compile_error_t> errOut = nullErr ? null : new PointerPointer<>(1);
        if (infoOut != null) {
            infoOut.put(0, new hs_expr_info_t());
        }
        if (errOut != null) {
            errOut.put(0, new hs_compile_error_t());
        }
        try {
            int result = hyperscan.hs_expression_ext_info(expr, toFlagBits(flags), null, infoOut, errOut);
            hs_compile_error_t err = errOut == null ? null : errOut.get(hs_compile_error_t.class);
            String message = err != null && err.message() != null ? err.message().getString() : null;
            hyperscan.hs_free_compile_error(err);
            hs_expr_info_t info = infoOut == null ? null : infoOut.get(hs_expr_info_t.class);
            if (info != null) {
                Pointer.free(info);
            }
            return new DualResult<>(result, null, message);
        } finally {
            if (infoOut != null) {
                infoOut.close();
            }
            if (errOut != null) {
                errOut.close();
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
        BytePointer expr = pattern == null ? new BytePointer() : new BytePointer(pattern);
        PointerPointer<hs_expr_info_t> infoOut = new PointerPointer<>(1);
        PointerPointer<hs_compile_error_t> errOut = nullErr ? null : new PointerPointer<>(1);
        infoOut.put(0, new hs_expr_info_t());
        if (errOut != null) {
            errOut.put(0, new hs_compile_error_t());
        }
        try {
            int result = hyperscan.hs_expression_info(expr, toFlagBits(flags), infoOut, errOut);
            hs_compile_error_t err = errOut == null ? null : errOut.get(hs_compile_error_t.class);
            String message = err != null && err.message() != null ? err.message().getString() : null;
            hyperscan.hs_free_compile_error(err);
            hs_expr_info_t info = infoOut.get(hs_expr_info_t.class);
            DualExpressionInfo value = null;
            if (result == 0 && info != null) {
                value = new DualExpressionInfo(
                        Integer.toUnsignedLong(info.min_width()),
                        Integer.toUnsignedLong(info.max_width()),
                        info.unordered_matches() != 0,
                        info.matches_at_eod() != 0,
                        info.matches_only_at_eod() != 0);
            }
            if (info != null) {
                Pointer.free(info);
            }
            return new DualResult<>(result, value, message);
        } finally {
            infoOut.close();
            if (errOut != null) {
                errOut.close();
            }
        }
    }

    private static DualResult<DualExpressionInfo> expressionExtInfoDataInternal(String pattern, EnumSet<DualExpressionFlag> flags, DualExpressionExt ext, boolean nullErr) {
        BytePointer expr = pattern == null ? new BytePointer() : new BytePointer(pattern);
        hs_expr_ext_t extStruct = null;
        if (ext != null) {
            extStruct = newDefaultExprExt();
            applyExprExt(extStruct, ext);
        }
        PointerPointer<hs_expr_info_t> infoOut = new PointerPointer<>(1);
        PointerPointer<hs_compile_error_t> errOut = nullErr ? null : new PointerPointer<>(1);
        infoOut.put(0, new hs_expr_info_t());
        if (errOut != null) {
            errOut.put(0, new hs_compile_error_t());
        }
        try {
            int result = hyperscan.hs_expression_ext_info(expr, toFlagBits(flags), extStruct, infoOut, errOut);
            hs_compile_error_t err = errOut == null ? null : errOut.get(hs_compile_error_t.class);
            String message = err != null && err.message() != null ? err.message().getString() : null;
            hyperscan.hs_free_compile_error(err);
            hs_expr_info_t info = infoOut.get(hs_expr_info_t.class);
            DualExpressionInfo value = null;
            if (result == 0 && info != null) {
                value = new DualExpressionInfo(
                        Integer.toUnsignedLong(info.min_width()),
                        Integer.toUnsignedLong(info.max_width()),
                        info.unordered_matches() != 0,
                        info.matches_at_eod() != 0,
                        info.matches_only_at_eod() != 0);
            }
            if (info != null) {
                Pointer.free(info);
            }
            return new DualResult<>(result, value, message);
        } finally {
            infoOut.close();
            if (errOut != null) {
                errOut.close();
            }
            if (extStruct != null) {
                extStruct.close();
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
            hs_database_t dbPlaceholder = new hs_database_t();
            hs_compile_error_t errPlaceholder = new hs_compile_error_t();
            dbOut.put(0, dbPlaceholder);
            errOut.put(0, errPlaceholder);
            List<BytePointer> exprPointers = new ArrayList<>(n);
            List<hs_expr_ext_t> exprExts = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                DualExpression expr = expressions.get(i);
                BytePointer exprPtr = new BytePointer(expr.pattern());
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
                String message = err != null && err.message() != null ? err.message().getString() : "unknown";
                hyperscan.hs_free_compile_error(err);
                throw new RuntimeException("Compile error: " + message);
            }
            hs_database_t database = dbOut.get(hs_database_t.class);
            return new JavaCppNativeDatabase(database, List.copyOf(expressions));
        }
    }

    private static hs_database_t nativeDatabase(DualDatabase database) {
        if (database instanceof JavaCppWrapperDatabase wrapper) {
            return getNativeDatabaseHandle(wrapper.database);
        }
        if (database instanceof JavaCppNativeDatabase nativeDb) {
            return nativeDb.database;
        }
        if (database instanceof JavaCppRawDatabase rawDb) {
            return rawDb.database;
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
            return raw.scratch;
        }
        if (scanner instanceof JavaCppScanner wrapper) {
            return getNativeScratchHandle(wrapper.scanner);
        }
        throw new IllegalArgumentException("Unsupported scanner type: " + scanner.getClass());
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
            return s.stream;
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
            info.put(0, new BytePointer());
            checkResult(hyperscan.hs_database_info(database, info));
            BytePointer infoPtr = info.get(BytePointer.class);
            String result = infoPtr.getString();
            Pointer.free(infoPtr);
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

    private record JavaCppWrapperDatabase(com.gliwka.hyperscan.wrapper.Database database) implements DualDatabase {
        @Override
        public long getSize() {
            return database.getSize();
        }

        @Override
        public void close() {
            database.close();
        }
    }

    private record JavaCppNativeDatabase(hs_database_t database, List<DualExpression> expressions) implements DualDatabase {
        @Override
        public long getSize() {
            try (SizeTPointer size = new SizeTPointer(1)) {
                checkResult(hyperscan.hs_database_size(database, size));
                return size.get();
            }
        }

        @Override
        public void close() {
            checkResult(hyperscan.hs_free_database(database));
        }
    }

    private record JavaCppRawDatabase(hs_database_t database, Pointer memory, boolean owner) implements DualDatabase {
        @Override
        public long getSize() {
            try (SizeTPointer size = new SizeTPointer(1)) {
                checkResult(hyperscan.hs_database_size(database, size));
                return size.get();
            }
        }

        @Override
        public void close() {
            database.setNull();
            if (owner) {
                Pointer.free(memory);
            }
        }
    }

    private static final class JavaCppStream implements DualStream {
        final hs_stream_t stream;
        final hs_scratch_t scratch;
        final List<DualExpression> expressions;
        boolean closed;

        JavaCppStream(hs_stream_t stream, hs_scratch_t scratch, List<DualExpression> expressions) {
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
            checkResult(hyperscan.hs_close_stream(stream, scratch, null, null));
            checkResult(hyperscan.hs_free_scratch(scratch));
        }
    }

    private record JavaCppScanner(com.gliwka.hyperscan.wrapper.Scanner scanner) implements DualScanner {
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

    private record JavaCppRawScanner(hs_scratch_t scratch) implements DualScanner {
        @Override
        public long getSize() {
            try (SizeTPointer size = new SizeTPointer(1)) {
                checkResult(hyperscan.hs_scratch_size(scratch, size));
                return size.get();
            }
        }

        @Override
        public void close() {
            checkResult(hyperscan.hs_free_scratch(scratch));
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
