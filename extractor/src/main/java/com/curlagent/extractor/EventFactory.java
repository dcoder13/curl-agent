package com.curlagent.extractor;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class EventFactory {
    private EventFactory() {
    }

    public static HttpCaptureEvent fromParts(String clientType, String method, String url, Map<String, List<String>> headers, String body) {
        return HttpCaptureEvent.builder()
                .clientType(clientType)
                .method(method)
                .url(url)
                .headers(headers == null ? Map.of() : headers)
                .body(limitBody(body))
                .build();
    }

    public static HttpCaptureEvent fromJdkHttpRequest(String clientType, HttpRequest request) {
        Map<String, List<String>> headers = new LinkedHashMap<>(request.headers().map());
        String body = request.bodyPublisher()
                .map(publisher -> bestEffortPublisherHint(publisher.contentLength()))
                .orElse(null);
        return fromParts(clientType, request.method(), request.uri().toString(), headers, body);
    }

    public static HttpCaptureEvent fromUriOnly(String clientType, String method, URI uri) {
        return fromParts(clientType, method, uri.toString(), Map.of(), null);
    }

    private static String bestEffortPublisherHint(long contentLength) {
        if (contentLength <= 0) {
            return null;
        }
        return "<BodyPublisher length=" + contentLength + ">";
    }

    private static String limitBody(String body) {
        if (body == null) {
            return null;
        }
        int max = Integer.getInteger("curl.agent.maxBodyChars", 8192);
        if (body.length() <= max) {
            return body;
        }
        return body.substring(0, max) + "...(truncated)";
    }
}
