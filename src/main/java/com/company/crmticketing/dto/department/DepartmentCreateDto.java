package com.company.crmticketing.dto.department;


import jakarta.validation.constraints.NotBlank;


public record DepartmentCreateDto (
        @NotBlank(message = "department name is required")
        String departmentName
){}
