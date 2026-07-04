package com.company.crmticketing.dto.ticket;

import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;

import java.time.LocalDateTime;

public record TicketUpdateDto(

        String title,

        LocalDateTime resolutionDeadline,

        LocalDateTime firstResponseDeadline,

        Priority priority,

        RequestStatus requestStatus,

        Long customerRequestId,

        Long departmentId,

        Long agentId,

        Long slaId

) {
}