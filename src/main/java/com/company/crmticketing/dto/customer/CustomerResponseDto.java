package com.company.crmticketing.dto.customer;

import java.util.List;

public record CustomerResponseDto (
        Long customerId,
        String customerName,
        String email,
        String customerPhone,
        int userId,
        List<String> customers
){}
