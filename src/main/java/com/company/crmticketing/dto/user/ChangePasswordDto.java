package com.company.crmticketing.dto.user;

import jakarta.validation.constraints.*;

public record ChangePasswordDto(
    @NotBlank String currentPassword,
    @NotBlank @Size(min = 8) String newPassword
) {}