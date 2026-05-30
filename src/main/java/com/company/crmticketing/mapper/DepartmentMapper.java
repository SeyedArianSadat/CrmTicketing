package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.Department.DepartmentDto;
import com.company.crmticketing.dto.Department.DepartmentUpdateDto;
import com.company.crmticketing.model.Department;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartmentMapper {
    Department toEntity(DepartmentDto departmentDto);

    DepartmentDto toDto(Department department);

    List<Department> toEntityList(List<DepartmentDto> departmentDtoList);

    List<DepartmentDto> toDtoList(List<Department> departmentList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartmentFromDto(DepartmentUpdateDto updateDto, @MappingTarget Department department);
}
