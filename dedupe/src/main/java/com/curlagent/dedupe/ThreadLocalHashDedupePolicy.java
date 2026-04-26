package com.curlagent.dedupe;

import com.curlagent.extractor.DedupePolicy;
import com.curlagent.extractor.HttpCaptureEvent;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ThreadLocalHashDedupePolicy implements DedupePolicy {
    private final Map<String, Long> seen = new ConcurrentHashMap<>();
    private final long windowMillis;

    public ThreadLocalHashDedupePolicy(long windowMillis) {
        this.windowMillis = windowMillis;
    }

    @Override
    public boolean shouldEmit(HttpCaptureEvent event) {
        long now = System.currentTimeMillis();
        String key = fingerprint(event, now / windowMillis);
        Long previous = seen.putIfAbsent(key, now);
        cleanup(now);
        return previous == null;
    }

    private void cleanup(long now) {
        seen.entrySet().removeIf(e -> now - e.getValue() > windowMillis * 2);
    }

    private String fingerprint(HttpCaptureEvent event, long bucket) {
        String payload = event.getMethod() + "|" + event.getUrl() + "|" + event.getThreadId() + "|" + (event.getBody() == null ? "" : event.getBody()) + "|" + bucket;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(digest.digest(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(payload.hashCode());
        }
    }
}
