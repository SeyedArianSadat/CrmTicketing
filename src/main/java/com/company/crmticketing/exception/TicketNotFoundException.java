package com.company.crmticketing.exception;


public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(Long TicketId) {
        super("Ticket with id " + TicketId + " not found");
    }
}
