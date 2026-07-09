package com.xenoamess.hyperscan.smoke;

import com.xenoamess.hyperscan.smoke.dual.DualApi;
import com.xenoamess.hyperscan.smoke.dual.DualApiArgumentsSource;
import com.xenoamess.hyperscan.smoke.dual.DualDatabase;
import com.xenoamess.hyperscan.smoke.dual.DualExpression;
import com.xenoamess.hyperscan.smoke.dual.DualExpressionFlag;
import com.xenoamess.hyperscan.smoke.dual.DualMatch;
import com.xenoamess.hyperscan.smoke.dual.DualScanner;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RealWorldDataTest {

    private static final String HTTP_REQUEST =
            "GET /api/v1/users/12345/orders?limit=10 HTTP/1.1\r\n" +
            "Host: example.com\r\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36\r\n" +
            "Accept: application/json\r\n" +
            "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9\r\n" +
            "X-Forwarded-For: 203.0.113.42\r\n" +
            "Content-Length: 0\r\n\r\n";

    private static final String HTTP_RESPONSE =
            "HTTP/1.1 200 OK\r\n" +
            "Server: nginx/1.24.0\r\n" +
            "Content-Type: application/json; charset=utf-8\r\n" +
            "Set-Cookie: session_id=abc123; Path=/; HttpOnly; Secure\r\n" +
            "Content-Length: 42\r\n\r\n" +
            "{\"status\":\"ok\",\"count\":7,\"items\":[\"a\",\"b\"]}";

    private static final String NGINX_LOG =
            "127.0.0.1 - - [28/Jun/2026:14:32:11 +0000] \"GET /index.html HTTP/1.1\" 200 612 \"-\" \"Mozilla/5.0\"\n" +
            "192.168.1.15 - - [28/Jun/2026:14:32:12 +0000] \"POST /login HTTP/1.1\" 401 142 \"-\" \"curl/8.0\"\n" +
            "10.0.0.5 - - [28/Jun/2026:14:32:13 +0000] \"GET /admin?cmd=ls%20-la HTTP/1.1\" 403 199 \"-\" \"python-requests/2.31\"\n";

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void httpRequestPatterns(DualApi api) {
        List<DualExpression> expressions = Arrays.asList(
                api.createExpression("GET /api/", DualExpressionFlag.SOM_LEFTMOST, 1),
                api.createExpression("Host:", DualExpressionFlag.SOM_LEFTMOST, 2),
                api.createExpression("User-Agent:", DualExpressionFlag.SOM_LEFTMOST, 3),
                api.createExpression("Authorization: Bearer", DualExpressionFlag.SOM_LEFTMOST, 4),
                api.createExpression("203.0.113.42", DualExpressionFlag.SOM_LEFTMOST, 5)
        );

        try (DualDatabase db = api.compileDatabase(expressions)) {
            try (DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, db);
                List<DualMatch> matches = api.scan(scanner, db, HTTP_REQUEST);
                assertThat(matches).hasSizeGreaterThanOrEqualTo(4);
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void httpResponsePatterns(DualApi api) {
        List<DualExpression> expressions = Arrays.asList(
                api.createExpression("HTTP/1.1 200", DualExpressionFlag.SOM_LEFTMOST, 1),
                api.createExpression("Set-Cookie:", DualExpressionFlag.SOM_LEFTMOST, 2),
                api.createExpression("application/json", DualExpressionFlag.SOM_LEFTMOST, 3),
                api.createExpression("charset=utf-8", DualExpressionFlag.SOM_LEFTMOST, 4)
        );

        try (DualDatabase db = api.compileDatabase(expressions)) {
            try (DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, db);
                List<DualMatch> matches = api.scan(scanner, db, HTTP_RESPONSE);
                assertThat(matches).hasSizeGreaterThanOrEqualTo(3);
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void nginxLogPatterns(DualApi api) {
        List<DualExpression> expressions = Arrays.asList(
                api.createExpression("127.0.0.1", DualExpressionFlag.SOM_LEFTMOST, 1),
                api.createExpression("28/Jun/2026", DualExpressionFlag.SOM_LEFTMOST, 2),
                api.createExpression("GET /admin", DualExpressionFlag.SOM_LEFTMOST, 3),
                api.createExpression("403", DualExpressionFlag.SOM_LEFTMOST, 4)
        );

        try (DualDatabase db = api.compileDatabase(expressions)) {
            try (DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, db);
                List<DualMatch> matches = api.scan(scanner, db, NGINX_LOG);
                assertThat(matches).isNotEmpty();
                boolean hasAdminMatch = matches.stream().anyMatch(m -> m.id() == 3);
                assertThat(hasAdminMatch).isTrue();
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(DualApiArgumentsSource.class)
    void simpleSecuritySignatures(DualApi api) {
        List<DualExpression> expressions = Arrays.asList(
                api.createExpression("cmd=", DualExpressionFlag.SOM_LEFTMOST, 1),
                api.createExpression("eval\\(", DualExpressionFlag.SOM_LEFTMOST, 2),
                api.createExpression("javascript:", DualExpressionFlag.SOM_LEFTMOST, 3),
                api.createExpression("\u003cscript", DualExpressionFlag.SOM_LEFTMOST, 4),
                api.createExpression("union select", EnumSet.of(DualExpressionFlag.SOM_LEFTMOST, DualExpressionFlag.CASELESS), 5)
        );

        try (DualDatabase db = api.compileDatabase(expressions)) {
            try (DualScanner scanner = api.createScanner()) {
                api.allocScratch(scanner, db);
                String suspiciousInput = "page=1\u0026cmd=ls%20-la\u0026q=\u003cscript\u003ealert(1)\u003c/script\u003e";
                List<DualMatch> matches = api.scan(scanner, db, suspiciousInput);
                assertThat(matches).isNotEmpty();
            }
        }
    }
}
