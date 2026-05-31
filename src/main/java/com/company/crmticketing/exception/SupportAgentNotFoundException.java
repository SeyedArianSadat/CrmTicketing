package com.company.crmticketing.exception;

public class SupportAgentNotFoundException extends RuntimeException {
    public SupportAgentNotFoundException(Long agentId) {
        super("Agent with id " + agentId + " not found");
    }
}
