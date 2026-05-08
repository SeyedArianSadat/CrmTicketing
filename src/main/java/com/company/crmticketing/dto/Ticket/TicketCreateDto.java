package com.company.crmticketing.dto.Ticket;

import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record TicketCreateDto(
        @NotBlank(message = "title is required")
        @Pattern(regexp = "^[A-Z][a-z]*$")
        String title,

        @NotNull(message = "priority is required")
        Priority priority,

        @NotNull(message = "status is required")
        RequestStatus requestStatus,

        @NotBlank(message = "first response deadline required")
        LocalDateTime firstResponseDeadline,

        @NotBlank(message = "resolution deadline required")
        LocalDateTime resolutionDeadline
) {
}
