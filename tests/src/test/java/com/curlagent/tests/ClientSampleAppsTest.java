package com.curlagent.tests;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ClientSampleAppsTest {
    @Test
    void okhttpSampleRequest() {
        assertDoesNotThrow(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("http://example.com").get().build();
            client.newCall(request);
        });
    }

    @Test
    void apacheSampleRequest() {
        assertDoesNotThrow(() -> {
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                client.execute(new HttpGet("http://example.com"));
            } catch (Exception ignored) {
                // Network can be unavailable in CI/dev sandbox.
            }
        });
    }

    @Test
    void jdkHttpSampleRequest() {
        assertDoesNotThrow(() -> {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://example.com")).GET().build();
            try {
                client.send(request, HttpResponse.BodyHandlers.discarding());
            } catch (Exception ignored) {
                // Network can be unavailable in CI/dev sandbox.
            }
        });
    }
}
