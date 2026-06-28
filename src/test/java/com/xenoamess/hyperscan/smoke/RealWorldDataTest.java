package com.xenoamess.hyperscan.smoke;

import com.xenoamess.hyperscan.jni.hs_database_t;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.xenoamess.hyperscan.jni.hyperscan.HS_FLAG_CASELESS;
import static com.xenoamess.hyperscan.jni.hyperscan.HS_FLAG_SOM_LEFTMOST;
import static org.assertj.core.api.Assertions.assertThat;

class RealWorldDataTest extends BaseSmokeTest {

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

    @Test
    void httpRequestPatterns() {
        String[] patterns = {
                "GET /api/",
                "Host:",
                "User-Agent:",
                "Authorization: Bearer",
                "203.0.113.42",
        };
        int[] ids = {1, 2, 3, 4, 5};
        int[] flags = {HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST};

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, HTTP_REQUEST);
            assertThat(matches).hasSizeGreaterThanOrEqualTo(4);
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }

    @Test
    void httpResponsePatterns() {
        String[] patterns = {
                "HTTP/1.1 200",
                "Set-Cookie:",
                "application/json",
                "charset=utf-8",
        };
        int[] ids = {1, 2, 3, 4};
        int[] flags = {HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST};

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, HTTP_RESPONSE);
            assertThat(matches).hasSizeGreaterThanOrEqualTo(3);
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }

    @Test
    void nginxLogPatterns() {
        String[] patterns = {
                "127.0.0.1",
                "28/Jun/2026",
                "GET /admin",
                "403",
        };
        int[] ids = {1, 2, 3, 4};
        int[] flags = {HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST};

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(patterns, ids, flags);
        try {
            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, NGINX_LOG);
            assertThat(matches).isNotEmpty();
            boolean hasAdminMatch = matches.stream().anyMatch(m -> m.id == 3);
            assertThat(hasAdminMatch).isTrue();
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }

    @Test
    void simpleSecuritySignatures() {
        String[] signatures = {
                "cmd=",
                "eval\\(",
                "javascript:",
                "\u003cscript",
                "union select",
        };
        int[] ids = {1, 2, 3, 4, 5};
        int[] flags = {HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST, HS_FLAG_SOM_LEFTMOST | HS_FLAG_CASELESS};

        hs_database_t db = HyperscanTestHelper.hsCompileMulti(signatures, ids, flags);
        try {
            String suspiciousInput = "page=1\u0026cmd=ls%20-la\u0026q=\u003cscript\u003ealert(1)\u003c/script\u003e";
            List<HyperscanTestHelper.Match> matches = HyperscanTestHelper.hsScan(db, suspiciousInput);
            assertThat(matches).isNotEmpty();
        } finally {
            HyperscanTestHelper.freeDatabase(db);
        }
    }
}
