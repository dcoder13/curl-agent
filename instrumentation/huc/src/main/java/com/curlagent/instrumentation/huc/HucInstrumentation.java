package com.curlagent.instrumentation.huc;

import com.curlagent.extractor.CaptureDispatcher;
import com.curlagent.extractor.EventFactory;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.net.URL;

public final class HucInstrumentation {
    private HucInstrumentation() {
    }

    public static AgentBuilder install(AgentBuilder builder) {
        return builder
                .type(ElementMatchers.named("java.net.HttpURLConnection"))
                .transform((b, td, cl, m, pd) -> b
                        .visit(Advice.to(ConnectAdvice.class).on(ElementMatchers.named("connect")))
                        .visit(Advice.to(OutputAdvice.class).on(ElementMatchers.named("getOutputStream"))));
    }

    public static final class ConnectAdvice {
        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.This Object connection) {
            emit(connection, "connect");
        }
    }

    public static final class OutputAdvice {
        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.This Object connection) {
            emit(connection, "getOutputStream");
        }
    }

    private static void emit(Object connection, String method) {
        try {
            URL url = (URL) connection.getClass().getMethod("getURL").invoke(connection);
            String requestMethod = String.valueOf(connection.getClass().getMethod("getRequestMethod").invoke(connection));
            CaptureDispatcher.dispatch(EventFactory.fromParts("httpurlconnection-" + method, requestMethod, url.toString(), java.util.Map.of(), null));
        } catch (Throwable ignored) {
        }
    }
}
