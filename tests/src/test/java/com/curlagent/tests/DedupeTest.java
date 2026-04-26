package com.curlagent.tests;

import com.curlagent.dedupe.ThreadLocalHashDedupePolicy;
import com.curlagent.extractor.HttpCaptureEvent;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DedupeTest {
    @Test
    void dropsDuplicateWithinWindow() {
        ThreadLocalHashDedupePolicy policy = new ThreadLocalHashDedupePolicy(2000);
        HttpCaptureEvent event = HttpCaptureEvent.builder()
                .clientType("apache-httpclient")
                .method("GET")
                .url("https://service/api?id=1")
                .headers(Map.of())
                .body(null)
                .build();

        assertTrue(policy.shouldEmit(event));
        assertFalse(policy.shouldEmit(event));
    }
}
