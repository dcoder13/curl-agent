package com.curlagent.instrumentation.okhttp;

import com.curlagent.extractor.CaptureDispatcher;
import com.curlagent.extractor.EventFactory;
import com.curlagent.extractor.HttpCaptureEvent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class OkHttpInstrumentation {
    private OkHttpInstrumentation() {
    }

    public static AgentBuilder install(AgentBuilder builder) {
        return builder
                .type(ElementMatchers.named("okhttp3.RealCall"))
                .transform((b, td, cl, m, pd) -> b
                        .visit(Advice.to(ExecuteAdvice.class).on(ElementMatchers.named("execute")))
                        .visit(Advice.to(EnqueueAdvice.class).on(ElementMatchers.named("enqueue"))));
    }

    public static final class ExecuteAdvice {
        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.This Object realCall) {
            emit(realCall);
        }
    }

    public static final class EnqueueAdvice {
        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.This Object realCall) {
            emit(realCall);
        }
    }

    private static void emit(Object realCall) {
        try {
            Object request = realCall.getClass().getMethod("request").invoke(realCall);
            String method = (String) request.getClass().getMethod("method").invoke(request);
            Object url = request.getClass().getMethod("url").invoke(request);
            String urlValue = String.valueOf(url);
            Object headers = request.getClass().getMethod("headers").invoke(request);
            Map<String, List<String>> headerMap = toHeaderMap(headers);
            String body = extractBody(request);
            HttpCaptureEvent event = EventFactory.fromParts("okhttp", method, urlValue, headerMap, body);
            CaptureDispatcher.dispatch(event);
        } catch (Throwable ignored) {
            // Suppress instrumentation errors.
        }
    }

    private static Map<String, List<String>> toHeaderMap(Object headers) throws ReflectiveOperationException {
        Map<String, List<String>> map = new LinkedHashMap<>();
        int size = (int) headers.getClass().getMethod("size").invoke(headers);
        for (int i = 0; i < size; i++) {
            String name = (String) headers.getClass().getMethod("name", int.class).invoke(headers, i);
            String value = (String) headers.getClass().getMethod("value", int.class).invoke(headers, i);
            map.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
        }
        return map;
    }

    private static String extractBody(Object request) throws ReflectiveOperationException {
        Object body = request.getClass().getMethod("body").invoke(request);
        if (body == null) {
            return null;
        }
        Object length = body.getClass().getMethod("contentLength").invoke(body);
        return "<RequestBody length=" + length + ">";
    }
}
