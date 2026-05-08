package com.company.crmticketing.dto.Department;

import java.util.List;

public record DepartmentResponseDto (
        Long departmentId,
        String departmentName,
        List<String> tickets,
        List<String> supportAgents
){}
