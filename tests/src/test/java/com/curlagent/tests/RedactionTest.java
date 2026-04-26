package com.curlagent.tests;

import com.curlagent.extractor.HttpCaptureEvent;
import com.curlagent.redaction.DefaultRedactor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedactionTest {
    @Test
    void masksSensitiveHeadersAndBody() {
        HttpCaptureEvent event = HttpCaptureEvent.builder()
                .clientType("okhttp")
                .method("POST")
                .url("https://service/api")
                .headers(Map.of(
                        "Authorization", List.of("Bearer abc123"),
                        "Content-Type", List.of("application/json")
                ))
                .body("{\"password\":\"secret-value\"}")
                .build();

        HttpCaptureEvent redacted = new DefaultRedactor().redact(event);
        assertEquals("***", redacted.getHeaders().get("Authorization").get(0));
        assertTrue(redacted.getBody().contains("***"));
    }
}
