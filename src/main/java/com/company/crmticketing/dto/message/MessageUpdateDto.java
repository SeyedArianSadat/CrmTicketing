package com.company.crmticketing.dto.message;

public record MessageUpdateDto (
        String content,
        boolean internalNot
){}
