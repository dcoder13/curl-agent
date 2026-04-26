package com.curlagent.instrumentation.jdkhttp;

import com.curlagent.extractor.CaptureDispatcher;
import com.curlagent.extractor.EventFactory;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.net.http.HttpRequest;

public final class JdkHttpInstrumentation {
    private JdkHttpInstrumentation() {
    }

    public static AgentBuilder install(AgentBuilder builder) {
        return builder
                .type(ElementMatchers.isSubTypeOf(java.net.http.HttpClient.class))
                .transform((b, td, cl, m, pd) -> b
                        .visit(Advice.to(SendAdvice.class).on(ElementMatchers.named("send")))
                        .visit(Advice.to(SendAsyncAdvice.class).on(ElementMatchers.named("sendAsync"))));
    }

    public static final class SendAdvice {
        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.Argument(0) HttpRequest request) {
            CaptureDispatcher.dispatch(EventFactory.fromJdkHttpRequest("jdk-httpclient", request));
        }
    }

    public static final class SendAsyncAdvice {
        @Advice.OnMethodEnter(suppress = Throwable.class)
        public static void onEnter(@Advice.Argument(0) HttpRequest request) {
            CaptureDispatcher.dispatch(EventFactory.fromJdkHttpRequest("jdk-httpclient-async", request));
        }
    }
}
