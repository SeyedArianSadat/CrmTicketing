package com.company.crmticketing.dto.TicketHistory;

public record TicketHistoryUpdateDto(
        String fieldChanged,
        String oldValue,
        String newValue
) {
}
