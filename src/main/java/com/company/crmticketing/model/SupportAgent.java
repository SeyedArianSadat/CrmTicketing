package com.company.crmticketing.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "supportAgentEntity")
@Table(name = "support_agents")

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SupportAgent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agentId;

    @NotBlank
    @Column(name = "agent_name",length = 50)
    private String agentName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id",nullable = false)
    private Department department;

    @OneToMany(mappedBy = "agentId")
    private List<Ticket> assignedTickets=new ArrayList<>();
}
