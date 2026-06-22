package com.company.crmticketing.dto.supportAgent;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SupportAgentDto {
    private Long agentId;

    @NotBlank(message = "agent name is required")
    @Size(max = 20)
    private String agentName;

    private Long userId;

    private Long departmentId;

}
