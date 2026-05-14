package com.company.crmticketing.exception;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(Long MessageId) {
        super("Message with id " + MessageId + " not found");
    }
}
