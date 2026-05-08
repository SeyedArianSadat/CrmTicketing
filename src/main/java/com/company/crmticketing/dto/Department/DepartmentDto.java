package com.company.crmticketing.dto.Department;

import com.company.crmticketing.model.SupportAgent;
import com.company.crmticketing.model.Ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class DepartmentDto {
    private Long departmentId;

    @NotBlank(message = "department name is required")
    @Pattern(regexp = "^[A-Z][a-z]*$")
    private String departmentName;

    private List<Ticket> tickets=new ArrayList<>();

    private List<SupportAgent> supportAgents=new ArrayList<>();
}
