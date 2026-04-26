package com.curlagent.extractor;

public interface DedupePolicy {
    boolean shouldEmit(HttpCaptureEvent event);
}
