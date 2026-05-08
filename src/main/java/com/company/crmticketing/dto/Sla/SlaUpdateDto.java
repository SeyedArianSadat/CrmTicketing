package com.company.crmticketing.dto.Sla;

import com.company.crmticketing.model.enums.Priority;

public record SlaUpdateDto(
        Priority priority,
        int responseTimeMinutes,
        int resolutionTimeMinutes,
        String description
) {
}
