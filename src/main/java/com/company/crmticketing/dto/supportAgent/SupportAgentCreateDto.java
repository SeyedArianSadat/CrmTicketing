package com.company.crmticketing.dto.supportAgent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SupportAgentCreateDto(
        @NotBlank(message = "agent name is required")
        @Size(max = 20)

        String agentName
) {
}
