package com.xenoamess.hyperscan.smoke.dual;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StreamAndVectorSmokeTest {

    record MatchInfo(int id, long from, long to) {
    }

    static Stream<DualApi> adapters() {
        return Stream.of(new JavaCppAdapter(), new PanamaAdapter());
    }

    @ParameterizedTest
    @MethodSource("adapters")
    void streamingScanFindsMatchesAcrossChunks(DualApi api) {
        DualExpression e1 = api.createExpression("hello", 1);
        DualExpression e2 = api.createExpression("world", 2);
        DualDatabase database = api.compileDatabase(List.of(e1, e2), DualMode.STREAM);
        try {
            List<MatchInfo> matches = new ArrayList<>();
            DualByteMatchHandler handler = (expr, from, to) -> {
                matches.add(new MatchInfo(expr.id(), from, to));
                return true;
            };
            DualScanner scanner = api.createScanner();
            try {
                DualStream stream = api.openStream(database);
                try {
                    api.scanStream(scanner, stream, "hello ".getBytes(), handler);
                    api.scanStream(scanner, stream, "world!".getBytes(), handler);
                } finally {
                    api.closeStream(scanner, stream, handler);
                }
            } finally {
                api.closeScanner(scanner);
            }
            assertThat(matches).hasSize(2);
            assertThat(matches.get(0).id).isEqualTo(1);
            assertThat(matches.get(0).to).isEqualTo(5L);
            assertThat(matches.get(1).id).isEqualTo(2);
            assertThat(matches.get(1).to).isEqualTo(11L);
        } finally {
            api.closeDatabase(database);
        }
    }

    @ParameterizedTest
    @MethodSource("adapters")
    void vectoredScanFindsMatchesInMultipleBuffers(DualApi api) {
        DualExpression e1 = api.createExpression("hello", 1);
        DualExpression e2 = api.createExpression("world", 2);
        DualDatabase database = api.compileDatabase(List.of(e1, e2), DualMode.VECTORED);
        try {
            List<MatchInfo> matches = new ArrayList<>();
            DualByteMatchHandler handler = (expr, from, to) -> {
                matches.add(new MatchInfo(expr.id(), from, to));
                return true;
            };
            DualScanner scanner = api.createScanner();
            try {
                api.scanVector(scanner, database, new byte[][] {"hello ".getBytes(), "world!".getBytes()}, handler);
            } finally {
                api.closeScanner(scanner);
            }
            assertThat(matches).hasSize(2);
            assertThat(matches.get(0).id).isEqualTo(1);
            assertThat(matches.get(0).to).isEqualTo(5L);
            assertThat(matches.get(1).id).isEqualTo(2);
            assertThat(matches.get(1).to).isEqualTo(11L);
        } finally {
            api.closeDatabase(database);
        }
    }

    @ParameterizedTest
    @MethodSource("adapters")
    void vectoredScanFindsMatchesInMultipleByteBuffers(DualApi api) {
        DualExpression e1 = api.createExpression("hello", 1);
        DualExpression e2 = api.createExpression("world", 2);
        DualDatabase database = api.compileDatabase(List.of(e1, e2), DualMode.VECTORED);
        try {
            List<MatchInfo> matches = new ArrayList<>();
            DualByteMatchHandler handler = (expr, from, to) -> {
                matches.add(new MatchInfo(expr.id(), from, to));
                return true;
            };
            DualScanner scanner = api.createScanner();
            try {
                ByteBuffer b1 = ByteBuffer.allocateDirect(6);
                b1.put("hello ".getBytes()).flip();
                ByteBuffer b2 = ByteBuffer.allocateDirect(6);
                b2.put("world!".getBytes()).flip();
                api.scanVector(scanner, database, new ByteBuffer[] {b1, b2}, handler);
            } finally {
                api.closeScanner(scanner);
            }
            assertThat(matches).hasSize(2);
            assertThat(matches.get(0).id).isEqualTo(1);
            assertThat(matches.get(0).to).isEqualTo(5L);
            assertThat(matches.get(1).id).isEqualTo(2);
            assertThat(matches.get(1).to).isEqualTo(11L);
        } finally {
            api.closeDatabase(database);
        }
    }

    @ParameterizedTest
    @MethodSource("adapters")
    void databaseInfoAndSerializationWorkInStreamMode(DualApi api) {
        DualExpression e1 = api.createExpression("test", 1);
        DualDatabase database = api.compileDatabase(e1, DualMode.STREAM);
        try {
            String info = api.getDatabaseInfo(database);
            assertThat(info).isNotNull().isNotEmpty();
            byte[] serialized = api.serialize(database);
            assertThat(serialized).isNotEmpty();
            String serializedInfo = api.getSerializedDatabaseInfo(serialized);
            assertThat(serializedInfo).isNotNull().isNotEmpty();
        } finally {
            api.closeDatabase(database);
        }
    }
}
