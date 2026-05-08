package com.company.crmticketing.dto.user;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponseDto(
    Long id,
    String username,
    String email,
    String firstName,
    String lastName,
    String phoneNumber,
    String profilePictureUrl,
    String fullName,
    boolean enabled,
    boolean emailVerified,
    boolean twoFactorEnabled,
    LocalDateTime lastLogin,
    LocalDateTime createdAt,
    Set<String> roles
) {}

