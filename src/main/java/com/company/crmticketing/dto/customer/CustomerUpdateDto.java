package com.company.crmticketing.dto.customer;

public record CustomerUpdateDto (
        String customerName,
        String email,
        String phone
){}
