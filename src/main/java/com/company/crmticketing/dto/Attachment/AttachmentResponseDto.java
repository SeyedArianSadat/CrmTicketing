package com.company.crmticketing.dto.Attachment;

public record AttachmentResponseDto(
    Long attachmentId,
    String fileName,
    String filePath,
    int ticketId
){}

