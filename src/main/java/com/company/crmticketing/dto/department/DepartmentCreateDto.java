package com.company.crmticketing.dto.department;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DepartmentCreateDto (
        @NotBlank(message = "department name is required")
        @Pattern(regexp = "^[A-Z][a-z]*$")
        String departmentName
){}
