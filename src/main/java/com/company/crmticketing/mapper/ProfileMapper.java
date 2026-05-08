package com.company.crmticketing.mapper;

import com.company.crmticketing.dto.profile.UserProfileDto;
import com.company.crmticketing.model.Permission;
import com.company.crmticketing.model.Role;
import com.company.crmticketing.model.User;
import org.mapstruct.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {Collectors.class})
public abstract class ProfileMapper {

    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    @Mapping(target = "accountNonLocked", source = "accountNonLocked")
    @Mapping(target = "lastPasswordChange", source = "passwordChangedAt")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "allPermissions", source = "roles")
    @Mapping(target = "statistics", source = "user")
    public abstract UserProfileDto toProfileDto(User user);

    protected Set<UserProfileDto.RoleDetailDto> mapRolesToRoleDetails(Set<Role> roles) {
        if (roles == null) return Set.of();

        return roles.stream()
                .map(role -> new UserProfileDto.RoleDetailDto(
                        role.getId(),
                        role.getName(),
                        role.getDescription(),
                        role.getPriority(),
                        mapPermissionsToNames(role.getPermissions())
                ))
                .collect(Collectors.toSet());
    }

    protected Set<String> mapRolesToAllPermissions(Set<Role> roles) {
        if (roles == null) return Set.of();

        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    protected Set<String> mapPermissionsToNames(Set<Permission> permissions) {
        if (permissions == null) return Set.of();
        return permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    protected UserProfileDto.UserStatistics mapToStatistics(User user) {
        if (user == null) return null;

        long daysSinceLastLogin = 0;
        if (user.getLastLogin() != null) {
            daysSinceLastLogin = Duration.between(user.getLastLogin(), LocalDateTime.now()).toDays();
        }

        boolean isPasswordExpired = user.isPasswordExpired(90);

        int totalPermissions = user.getRoles() != null ? user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet()).size() : 0;

        return new UserProfileDto.UserStatistics(
                user.getRoles() != null ? user.getRoles().size() : 0,
                totalPermissions,
                isPasswordExpired,
                daysSinceLastLogin,
                user.getFailedAttempts()
        );
    }

    protected String getFullName(User user) {
        return user.getFullName();
    }
}