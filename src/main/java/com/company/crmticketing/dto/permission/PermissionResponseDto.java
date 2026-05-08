package com.company.crmticketing.dto.permission;

public record PermissionResponseDto(
    Long id,
    String name,
    String description,
    String category,
    boolean systemDefault,
    int roleCount
) {}