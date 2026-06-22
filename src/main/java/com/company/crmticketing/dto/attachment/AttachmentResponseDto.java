package com.company.crmticketing.dto.attachment;

public record AttachmentResponseDto(
    Long attachmentId,
    String fileName,
    String filePath,
    int ticketId
){}

