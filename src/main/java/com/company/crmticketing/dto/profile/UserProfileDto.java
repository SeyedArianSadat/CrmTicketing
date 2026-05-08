package com.company.crmticketing.dto.profile;

import java.time.LocalDateTime;
import java.util.Set;

public record UserProfileDto(
    Long id,
    String username,
    String email,
    String firstName,
    String lastName,
    String fullName,
    String phoneNumber,
    String profilePictureUrl,
    
    boolean enabled,
    boolean accountNonLocked,
    boolean emailVerified,
    boolean twoFactorEnabled,
    LocalDateTime lastLogin,
    LocalDateTime lastPasswordChange,
    
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    
    Set<RoleDetailDto> roles,
    
    Set<String> allPermissions,
    
    UserStatistics statistics
) {
    
    public record RoleDetailDto(
        Long id,
        String name,
        String description,
        int priority,
        Set<String> permissions
    ) {}
    
    public record UserStatistics(
        int totalRoles,
        int totalPermissions,
        boolean isPasswordExpired,
        long daysSinceLastLogin,
        int failedAttempts
    ) {}
}