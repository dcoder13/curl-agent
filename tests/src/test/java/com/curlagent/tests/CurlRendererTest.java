package com.curlagent.tests;

import com.curlagent.curlbuilder.DefaultCurlRenderer;
import com.curlagent.extractor.HttpCaptureEvent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CurlRendererTest {
    @Test
    void rendersDeterministicCurl() {
        HttpCaptureEvent event = HttpCaptureEvent.builder()
                .clientType("jdk-httpclient")
                .method("POST")
                .url("https://service/api/user?id=123")
                .headers(Map.of("Content-Type", List.of("application/json")))
                .body("{\"userId\":\"123\"}")
                .build();

        String curl = new DefaultCurlRenderer().render(event);
        assertTrue(curl.contains("curl -X POST"));
        assertTrue(curl.contains("-H 'Content-Type: application/json'"));
        assertTrue(curl.contains("-d '{\"userId\":\"123\"}'"));
    }
}
