package com.curlagent.agent;

import com.curlagent.extractor.CaptureDispatcher;
import com.curlagent.instrumentation.apache.ApacheInstrumentation;
import com.curlagent.instrumentation.huc.HucInstrumentation;
import com.curlagent.instrumentation.jdkhttp.JdkHttpInstrumentation;
import com.curlagent.instrumentation.okhttp.OkHttpInstrumentation;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public final class AgentBootstrap {
    private static final Logger log = LoggerFactory.getLogger(AgentBootstrap.class);

    private AgentBootstrap() {
    }

    public static void premain(String agentArgs, Instrumentation inst) {
        if (!Boolean.parseBoolean(System.getProperty("curl.agent.enabled", "false"))) {
            log.info("curl-agent disabled. Enable with -Dcurl.agent.enabled=true");
            return;
        }

        log.info("Starting curl-agent with args={}", agentArgs);
        CaptureDispatcher.register(new CapturePipeline());

        AgentBuilder agentBuilder = new AgentBuilder.Default()
                .ignore(ElementMatchers.nameStartsWith("net.bytebuddy."))
                .ignore(ElementMatchers.nameStartsWith("sun.reflect."))
                .ignore(ElementMatchers.nameStartsWith("jdk.internal."));

        agentBuilder = OkHttpInstrumentation.install(agentBuilder);
        agentBuilder = ApacheInstrumentation.install(agentBuilder);
        agentBuilder = JdkHttpInstrumentation.install(agentBuilder);
        agentBuilder = HucInstrumentation.install(agentBuilder);

        agentBuilder.with(AgentBuilder.Listener.StreamWriting.toSystemOut())
                .installOn(inst);

        log.info("curl-agent instrumentation installed");
    }
}
