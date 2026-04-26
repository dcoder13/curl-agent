package com.curlagent.extractor;

import java.util.function.Consumer;

public final class CaptureDispatcher {
    private static volatile Consumer<HttpCaptureEvent> consumer = event -> { };

    private CaptureDispatcher() {
    }

    public static void register(Consumer<HttpCaptureEvent> newConsumer) {
        consumer = newConsumer == null ? event -> { } : newConsumer;
    }

    public static void dispatch(HttpCaptureEvent event) {
        if (event != null) {
            consumer.accept(event);
        }
    }
}
