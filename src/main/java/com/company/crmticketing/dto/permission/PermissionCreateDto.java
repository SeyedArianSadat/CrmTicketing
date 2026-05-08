package com.company.crmticketing.dto.permission;

import jakarta.validation.constraints.*;

public record PermissionCreateDto(
    @NotBlank(message = "Permission name is required")
    @Size(min = 2, max = 100, message = "Permission name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-z]+:[a-z_]+$", message = "Permission format must be 'resource:action' (e.g., user:read)")
    String name,

    String description,

    @NotBlank(message = "Category is required")
    String category
) {}

