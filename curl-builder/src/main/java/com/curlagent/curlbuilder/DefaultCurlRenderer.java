package com.curlagent.curlbuilder;

import com.curlagent.extractor.CurlRenderer;
import com.curlagent.extractor.HttpCaptureEvent;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class DefaultCurlRenderer implements CurlRenderer {
    @Override
    public String render(HttpCaptureEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("curl -X ").append(shellEscape(event.getMethod())).append(" \\\n");
        sb.append("  '").append(shellEscape(event.getUrl())).append("'");

        Map<String, List<String>> sortedHeaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        sortedHeaders.putAll(event.getHeaders());
        for (Map.Entry<String, List<String>> header : sortedHeaders.entrySet()) {
            for (String value : header.getValue()) {
                sb.append(" \\\n  -H '")
                        .append(shellEscape(header.getKey()))
                        .append(": ")
                        .append(shellEscape(value))
                        .append("'");
            }
        }
        if (event.getBody() != null && !event.getBody().isBlank()) {
            sb.append(" \\\n  -d '").append(shellEscape(event.getBody())).append("'");
        }
        return sb.toString();
    }

    private String shellEscape(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "'\"'\"'");
    }
}
