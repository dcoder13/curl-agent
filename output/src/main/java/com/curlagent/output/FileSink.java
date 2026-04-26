package com.curlagent.output;

import com.curlagent.extractor.HttpCaptureEvent;
import com.curlagent.extractor.OutputSink;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class FileSink implements OutputSink {
    private final Path outputPath;

    public FileSink(Path outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public synchronized void write(HttpCaptureEvent event, String curlCommand) {
        try {
            Files.createDirectories(outputPath.getParent());
            String line = event.getCapturedAt() + " [" + event.getClientType() + "] " + curlCommand + System.lineSeparator();
            Files.writeString(outputPath, line, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ignored) {
            // Keep agent invisible to business logic.
        }
    }
}
