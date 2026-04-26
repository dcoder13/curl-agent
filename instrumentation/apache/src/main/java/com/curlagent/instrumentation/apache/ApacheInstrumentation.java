package com.curlagent.instrumentation.apache;

import com.curlagent.extractor.CaptureDispatcher;
import com.curlagent.extractor.EventFactory;
import com.curlagent.extractor.HttpCaptureEvent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ApacheInstrumentation {
    private ApacheInstrumentation() {
    }

    public static AgentBuilder install(AgentBuilder builder) {
        return builder
                .type(ElementMatchers.named("org.apache.http.impl.client.InternalHttpClient"))
                .transform((b, td, cl, m, pd) -> b
                        .visit(Advice.to(DoExecuteAdvice.class).on(ElementMatchers.named("doExecute"))));
    }

    public static final class DoExecuteAdvice {
        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.Argument(1) Object request) {
            try {
                String method = String.valueOf(request.getClass().getMethod("getRequestLine").invoke(request)
                        .getClass().getMethod("getMethod")
                        .invoke(request.getClass().getMethod("getRequestLine").invoke(request)));
                String uri = String.valueOf(request.getClass().getMethod("getRequestLine").invoke(request)
                        .getClass().getMethod("getUri")
                        .invoke(request.getClass().getMethod("getRequestLine").invoke(request)));
                Map<String, List<String>> headers = new LinkedHashMap<>();
                HttpCaptureEvent event = EventFactory.fromParts("apache-httpclient", method, uri, headers, null);
                CaptureDispatcher.dispatch(event);
            } catch (Throwable ignored) {
            }
        }
    }
}
