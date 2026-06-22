package com.company.crmticketing.dto.supportAgent;

import java.util.List;

public record SupportAgentResponseDto(
        Long agentId,
        String agentName,
        int userId,
        int departmentId,
        List<String> assignedTickets
) {
}
