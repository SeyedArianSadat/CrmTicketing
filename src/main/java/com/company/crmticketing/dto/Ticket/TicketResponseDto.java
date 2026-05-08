package com.company.crmticketing.dto.Ticket;

import com.company.crmticketing.model.CustomerRequest;
import com.company.crmticketing.model.Ticket;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TicketResponseDto(
    Long ticketId,
    String title,
    LocalDateTime resolutionDeadline,
    LocalDateTime firstResponseDeadline,
    Priority priority,
    RequestStatus requestStatus,
    CustomerRequest customerRequest,
    List<String> messages,
    List<String> ticketHistory,
    List<String> attachments,
    int assignedAgentId,
    int departmentId,
    int slaId,
    int agenId

){}
