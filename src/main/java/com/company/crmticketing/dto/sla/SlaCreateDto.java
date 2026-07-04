package com.company.crmticketing.dto.sla;

import com.company.crmticketing.model.enums.Priority;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record SlaCreateDto(
        @NotNull(message = "priority level is required")
        Priority priorityLevel,

        @Min(value = 1,message = "must be 1 or greater")
        int responseTimeMinutes,

        @Min(value = 1,message = "must be 1 or greater")
        int resolutionTimeMinutes,

        @NotBlank(message = "description is required ")
        String description
) {
}
