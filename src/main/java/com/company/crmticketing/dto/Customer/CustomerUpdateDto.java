package com.company.crmticketing.dto.Customer;

public record CustomerUpdateDto (
        String customerName,
        String email,
        String customerPhone
){}
