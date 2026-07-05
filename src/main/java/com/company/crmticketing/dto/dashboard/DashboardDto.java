package com.company.crmticketing.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardDto {

    private long customers;
    private long tickets;
    private long departments;
    private long agents;
    private long customerRequests;
    private long messages;
    private long attachments;
    private long slas;
    private long openTickets;
    private long inProgressTickets;
    private long closedTickets;

}