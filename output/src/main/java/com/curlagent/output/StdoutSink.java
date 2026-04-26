package com.curlagent.output;

import com.curlagent.extractor.HttpCaptureEvent;
import com.curlagent.extractor.OutputSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StdoutSink implements OutputSink {
    private static final Logger log = LoggerFactory.getLogger(StdoutSink.class);

    @Override
    public void write(HttpCaptureEvent event, String curlCommand) {
        log.info("Captured {} {} via {}:\n{}", event.getMethod(), event.getUrl(), event.getClientType(), curlCommand);
    }
}
