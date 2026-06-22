package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.department.DepartmentDto;
import com.company.crmticketing.dto.department.DepartmentUpdateDto;
import com.company.crmticketing.model.Department;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartmentMapper {
    @Mapping(target = "supportAgents", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    Department toEntity(DepartmentDto departmentDto);

    DepartmentDto toDto(Department department);

    List<DepartmentDto> toDepartmentDtoList(List<Department> departmentList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "supportAgents", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    void updateDepartmentFromDto(DepartmentUpdateDto updateDto, @MappingTarget Department department);
}
