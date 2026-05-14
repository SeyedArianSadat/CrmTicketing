package com.company.crmticketing.exception;

public class AttachmentNotFoundException extends RuntimeException {
    public AttachmentNotFoundException(Long AttachmentId) {
        super("Attachment with id " + AttachmentId + " not found");
    }
}
