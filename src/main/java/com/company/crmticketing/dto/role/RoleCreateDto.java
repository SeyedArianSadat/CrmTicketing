package com.company.crmticketing.dto.role;

import jakarta.validation.constraints.*;

public record RoleCreateDto(
    @NotBlank(message = "Role name is required")
    @Size(min = 2, max = 50, message = "Role name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Z][A-Z_]*$", message = "Role name must be uppercase with underscores (e.g., ROLE_ADMIN)")
    String name,
    
    String description,
    
    @Min(value = 0, message = "Priority must be 0 or greater")
    @Max(value = 100, message = "Priority must be 100 or less")
    int priority
) {}



