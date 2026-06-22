package com.company.crmticketing.dto.department;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;



@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class DepartmentDto {
    private Long departmentId;

    @NotBlank(message = "department name is required")
    private String departmentName;

}
