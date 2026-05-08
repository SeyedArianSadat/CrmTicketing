package com.company.crmticketing.dto.Ticket;

import com.company.crmticketing.model.CustomerRequest;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;

import java.time.LocalDateTime;

public record TicketUpdateDto(
        String title,
        LocalDateTime resolutionDeadline,
        LocalDateTime firstResponseDeadline,
        Priority priority,
        RequestStatus requestStatus,
        CustomerRequest customerRequest
) {
}
