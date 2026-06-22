package com.company.crmticketing.dto.ticketHistory;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class TicketHistoryDto {
    private Long ticketHistoryId;

    @NotBlank(message = "changed field is required")
    private String fieldChanged;

    @NotBlank(message = "old value is required")
    private String oldValue;

    @NotBlank(message = "new value is required")
    private String newValue;

    private Long ticketId;
}
