package com.company.crmticketing.dto.Sla;

import com.company.crmticketing.model.enums.Priority;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SlaDto {
    private Long slaId;

    @NotNull(message = "priority level is required")
    private Priority priorityLevel;

    @Min(value = 1,message = "must be 1 or greater")
    private int responseTimeMinutes;

    @Min(value = 1,message = "must be 1 or greater")
    private int resolutionTimeMinutes;

    @NotBlank(message = "description is required ")
    @Pattern(regexp = "^[A-Z][a-z]*$")
    private String description;
}
