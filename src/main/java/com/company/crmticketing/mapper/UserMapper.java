package com.company.crmticketing.mapper;

import com.company.crmticketing.dto.user.UserResponseDto;
import com.company.crmticketing.dto.user.UserUpdateDto;
import com.company.crmticketing.model.Role;
import com.company.crmticketing.model.User;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {Collectors.class})
public abstract class UserMapper {
    
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    @Mapping(target = "roles", source = "roles")
    public abstract UserResponseDto toResponseDto(User user);
    
    protected Set<String> mapRolesToNames(Set<Role> roles) {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateUserFromDto(UserUpdateDto updateDto, @MappingTarget User user);
}