package com.company.crmticketing.dto.message;

public record MessageResponseDto (
        Long messageId,
        String content,
        boolean internalNote,
        int requestId,
        int ticketId,
        int senderUserId
){}
