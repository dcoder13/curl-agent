package com.curlagent.extractor;

public interface OutputSink {
    void write(HttpCaptureEvent event, String curlCommand);
}
