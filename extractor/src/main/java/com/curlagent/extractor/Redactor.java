package com.curlagent.extractor;

public interface Redactor {
    HttpCaptureEvent redact(HttpCaptureEvent event);
}
