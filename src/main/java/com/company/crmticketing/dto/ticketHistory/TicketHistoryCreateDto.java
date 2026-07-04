package com.company.crmticketing.dto.ticketHistory;


import jakarta.validation.constraints.NotBlank;

public record TicketHistoryCreateDto(

        @NotBlank(message = "changed field is required")
        String fieldChanged,

        @NotBlank(message = "old value is required")
        String oldValue,

        @NotBlank(message = "new value is required")
        String newValue,

        Long ticketId
) {
}
