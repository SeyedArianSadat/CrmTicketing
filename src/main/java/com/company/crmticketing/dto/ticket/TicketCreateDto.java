package com.company.crmticketing.dto.ticket;

import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TicketCreateDto(
        @NotBlank(message = "title is required")
        String title,

        @NotNull(message = "priority is required")
        Priority priority,

        @NotNull(message = "status is required")
        RequestStatus requestStatus,

        @NotNull(message = "first response deadline required")
        LocalDateTime firstResponseDeadline,

        @NotNull(message = "resolution deadline required")
        LocalDateTime resolutionDeadline
) {
}
