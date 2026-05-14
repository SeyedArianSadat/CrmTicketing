package com.company.crmticketing.exception;


public class TicketAlreadyExistException extends RuntimeException {
    public TicketAlreadyExistException(String Title) {
        super("Ticket already exists: " + Title);
    }
}
