package com.company.crmticketing.exception;

public class SupportAgentNotFoundException extends RuntimeException {
    public SupportAgentNotFoundException(Long AgentId) {
        super("Agent with id " + AgentId + " not found");
    }
}
