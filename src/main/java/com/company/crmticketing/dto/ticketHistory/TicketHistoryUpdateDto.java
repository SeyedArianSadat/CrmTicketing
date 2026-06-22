package com.company.crmticketing.dto.ticketHistory;

public record TicketHistoryUpdateDto(
        String fieldChanged,
        String oldValue,
        String newValue
) {
}
