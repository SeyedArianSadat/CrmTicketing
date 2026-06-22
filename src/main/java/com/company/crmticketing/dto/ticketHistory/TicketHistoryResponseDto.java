package com.company.crmticketing.dto.ticketHistory;

public record TicketHistoryResponseDto (
    Long ticketHistoryId,
    String fieldChanged,
    String oldValue,
    String newValue,
    int ticketId
    )
{}
