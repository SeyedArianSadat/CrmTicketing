package com.company.crmticketing.dto.SupportAgent;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SupportAgentCreateDto(
        @NotBlank(message = "agent name is required")
        @Size(max = 20)
        @Pattern(regexp = "^[A-Z][a-z]*$")
        String agentName
) {
}
