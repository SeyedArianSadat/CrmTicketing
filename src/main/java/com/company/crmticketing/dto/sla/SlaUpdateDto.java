package com.company.crmticketing.dto.sla;

import com.company.crmticketing.model.enums.Priority;

public record SlaUpdateDto(
        Priority priorityLevel,
        int responseTimeMinutes,
        int resolutionTimeMinutes,
        String description
) {
}
