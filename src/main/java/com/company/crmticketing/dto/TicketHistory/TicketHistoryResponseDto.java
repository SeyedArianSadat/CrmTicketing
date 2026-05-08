package com.company.crmticketing.dto.TicketHistory;

import java.util.List;

public record TicketHistoryResponseDto (
    Long ticketHistoryId,
    String fieldChanged,
    String oldValue,
    String newValue,
    int ticketId
    )
{}
