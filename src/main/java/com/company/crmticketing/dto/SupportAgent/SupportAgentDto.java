package com.company.crmticketing.dto.SupportAgent;

import com.company.crmticketing.model.Department;
import com.company.crmticketing.model.Ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class SupportAgentDto {
    private Long agentId;

    @NotBlank(message = "agent name is required")
    @Size(max = 20)
    @Pattern(regexp = "^[A-Z][a-z]*$")
    private String agentName;

    private Department department;

    private List<Ticket> assignedTickets=new ArrayList<>();
}
