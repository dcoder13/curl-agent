package com.curlagent.redaction;

import com.curlagent.extractor.HttpCaptureEvent;
import com.curlagent.extractor.Redactor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class DefaultRedactor implements Redactor {
    private static final Set<String> SENSITIVE_HEADERS = Set.of(
            "authorization",
            "cookie",
            "set-cookie",
            "x-api-key"
    );

    @Override
    public HttpCaptureEvent redact(HttpCaptureEvent event) {
        Map<String, List<String>> redactedHeaders = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : event.getHeaders().entrySet()) {
            String key = entry.getKey();
            String lower = key.toLowerCase(Locale.ROOT);
            if (SENSITIVE_HEADERS.contains(lower)) {
                redactedHeaders.put(key, List.of("***"));
                continue;
            }
            List<String> values = new ArrayList<>();
            for (String value : entry.getValue()) {
                values.add(maskSecrets(value));
            }
            redactedHeaders.put(key, values);
        }

        return event.toBuilder()
                .headers(redactedHeaders)
                .body(maskSecrets(event.getBody()))
                .build();
    }

    private String maskSecrets(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        String masked = value.replaceAll("(?i)bearer\\s+[a-z0-9\\-._~+/]+=*", "Bearer ***");
        masked = masked.replaceAll("(?i)(\"?(password|secret|token|cardNumber|cvv)\"?\\s*[:=]\\s*\")([^\"]+)(\")", "$1***$4");
        masked = masked.replaceAll("(?i)(password|secret|token)=([^&\\s]+)", "$1=***");
        return masked;
    }
}
