package com.company.crmticketing.dto.Ticket;

import com.company.crmticketing.model.*;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private CustomerRequest customerRequest;

    private Department department;

    private SupportAgent agentId;

    private Sla sla;

    private List<Message> messages = new ArrayList<>();

    private List<TicketHistory> ticketHistories = new ArrayList<>();

    private List<Attachment> attachments = new ArrayList<>();
}
