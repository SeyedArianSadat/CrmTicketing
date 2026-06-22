package com.company.crmticketing.dto.sla;

import com.company.crmticketing.model.enums.Priority;

public record SlaResponseDto(
        Long slaId,
        Priority priority,
        int responseTimeMinutes,
        int resolutionTimeMinutes,
        String description
) {
}
