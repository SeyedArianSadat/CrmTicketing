package com.company.crmticketing.dto.ticket;


import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class TicketDto {
    private Long ticketId;

    @NotBlank(message = "title is required")
    private String title;

    @NotNull(message = "priority is required")
    private Priority priority;

    @NotNull(message = "status is required")
    private RequestStatus requestStatus;

    //@NotBlank(message = "first response deadline required")
    private LocalDateTime firstResponseDeadline;

    //@NotBlank(message = "resolution deadline required")
    private LocalDateTime resolutionDeadline;

    private Long customerRequestId;

    private Long departmentId;

    private Long agentId;

    private Long slaId;

}
