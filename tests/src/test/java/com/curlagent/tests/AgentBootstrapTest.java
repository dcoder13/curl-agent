package com.curlagent.tests;

import com.curlagent.agent.AgentBootstrap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AgentBootstrapTest {
    @Test
    void premainNoopsWhenDisabled() {
        System.clearProperty("curl.agent.enabled");
        assertDoesNotThrow(() -> AgentBootstrap.premain("", null));
    }
}
