package com.company.crmticketing.dto.attachment;

public record AttachmentUpdateDto (
    String fileName,
    String filePath,
    Long ticketId
){}

