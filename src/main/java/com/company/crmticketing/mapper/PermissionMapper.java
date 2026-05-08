package com.company.crmticketing.mapper;

import com.company.crmticketing.dto.permission.PermissionCreateDto;
import com.company.crmticketing.dto.permission.PermissionResponseDto;
import com.company.crmticketing.dto.permission.PermissionUpdateDto;
import com.company.crmticketing.model.Permission;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "systemDefault", constant = "false")
    @Mapping(target = "roles", ignore = true)
    Permission toEntity(PermissionCreateDto createDto);
    
    @Mapping(target = "roleCount", ignore = true)
    PermissionResponseDto toResponseDto(Permission permission);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePermissionFromDto(PermissionUpdateDto updateDto, @MappingTarget Permission permission);
    
    List<PermissionResponseDto> toResponseDtoList(List<Permission> permissions);
}