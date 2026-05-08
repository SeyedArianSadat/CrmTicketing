package com.company.crmticketing.mapper;

import com.company.crmticketing.dto.role.RoleCreateDto;
import com.company.crmticketing.dto.role.RoleResponseDto;
import com.company.crmticketing.dto.role.RoleUpdateDto;
import com.company.crmticketing.model.Permission;
import com.company.crmticketing.model.Role;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {Collectors.class})
public interface RoleMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleCreateDto createDto);
    
    @Mapping(target = "permissions", source = "permissions")
    @Mapping(target = "userCount", ignore = true) // باید از سرویس مقداردهی شود
    RoleResponseDto toResponseDto(Role role);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoleFromDto(RoleUpdateDto updateDto, @MappingTarget Role role);
    
    default Set<String> mapPermissionsToNames(Set<Permission> permissions) {
        if (permissions == null) return Set.of();
        return permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
    
    @Named("stringToPermission")
    default Permission stringToPermission(String permissionName) {
        if (permissionName == null) return null;
        return Permission.builder().name(permissionName).build();
    }
}