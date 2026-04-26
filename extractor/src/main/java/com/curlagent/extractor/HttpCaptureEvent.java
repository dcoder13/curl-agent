package com.curlagent.extractor;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class HttpCaptureEvent {
    private final String clientType;
    private final String method;
    private final String url;
    private final Map<String, List<String>> headers;
    private final String body;
    private final Instant capturedAt;
    private final long threadId;

    private HttpCaptureEvent(Builder builder) {
        this.clientType = builder.clientType;
        this.method = builder.method;
        this.url = builder.url;
        this.headers = builder.headers == null ? Map.of() : Collections.unmodifiableMap(builder.headers);
        this.body = builder.body;
        this.capturedAt = builder.capturedAt == null ? Instant.now() : builder.capturedAt;
        this.threadId = builder.threadId == 0 ? Thread.currentThread().getId() : builder.threadId;
    }

    public String getClientType() { return clientType; }
    public String getMethod() { return method; }
    public String getUrl() { return url; }
    public Map<String, List<String>> getHeaders() { return headers; }
    public String getBody() { return body; }
    public Instant getCapturedAt() { return capturedAt; }
    public long getThreadId() { return threadId; }

    public Builder toBuilder() {
        return builder()
                .clientType(clientType)
                .method(method)
                .url(url)
                .headers(headers)
                .body(body)
                .capturedAt(capturedAt)
                .threadId(threadId);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String clientType;
        private String method;
        private String url;
        private Map<String, List<String>> headers;
        private String body;
        private Instant capturedAt;
        private long threadId;

        public Builder clientType(String clientType) { this.clientType = clientType; return this; }
        public Builder method(String method) { this.method = method; return this; }
        public Builder url(String url) { this.url = url; return this; }
        public Builder headers(Map<String, List<String>> headers) { this.headers = headers; return this; }
        public Builder body(String body) { this.body = body; return this; }
        public Builder capturedAt(Instant capturedAt) { this.capturedAt = capturedAt; return this; }
        public Builder threadId(long threadId) { this.threadId = threadId; return this; }

        public HttpCaptureEvent build() {
            Objects.requireNonNull(clientType, "clientType");
            Objects.requireNonNull(method, "method");
            Objects.requireNonNull(url, "url");
            return new HttpCaptureEvent(this);
        }
    }
}
