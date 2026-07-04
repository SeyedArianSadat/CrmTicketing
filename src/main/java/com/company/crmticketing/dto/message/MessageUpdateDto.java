package com.company.crmticketing.dto.message;

public record MessageUpdateDto (
        String content,
        Boolean internalNote,
        Long requestId,
        Long ticketId,
        Long senderUserId
){}
