package com.curlagent.agent;

import com.curlagent.curlbuilder.DefaultCurlRenderer;
import com.curlagent.dedupe.ThreadLocalHashDedupePolicy;
import com.curlagent.extractor.CurlRenderer;
import com.curlagent.extractor.DedupePolicy;
import com.curlagent.extractor.HttpCaptureEvent;
import com.curlagent.extractor.OutputSink;
import com.curlagent.extractor.Redactor;
import com.curlagent.output.FileSink;
import com.curlagent.output.StdoutSink;
import com.curlagent.redaction.DefaultRedactor;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public final class CapturePipeline implements Consumer<HttpCaptureEvent> {
    private final Redactor redactor = new DefaultRedactor();
    private final DedupePolicy dedupePolicy = new ThreadLocalHashDedupePolicy(1500);
    private final CurlRenderer curlRenderer = new DefaultCurlRenderer();
    private final List<OutputSink> sinks = List.of(
            new StdoutSink(),
            new FileSink(Path.of(".logs/generated-curl.log"))
    );

    @Override
    public void accept(HttpCaptureEvent event) {
        try {
            HttpCaptureEvent redacted = redactor.redact(event);
            if (!dedupePolicy.shouldEmit(redacted)) {
                return;
            }
            String curl = curlRenderer.render(redacted);
            for (OutputSink sink : sinks) {
                sink.write(redacted, curl);
            }
        } catch (Exception ignored) {
            // Agent must not break host app behavior.
        }
    }
}
