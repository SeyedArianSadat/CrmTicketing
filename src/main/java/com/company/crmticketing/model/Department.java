package com.company.crmticketing.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "departmentEntity")
@Table(name = "departments")

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Department extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long departmentId;

    @NotBlank
    @Column(name = "department_name",nullable = false)
    private String departmentName;

    @OneToMany(mappedBy = "department")
    private List<SupportAgent> supportAgents=new ArrayList<>();

    @OneToMany(mappedBy = "department")
    private List<Ticket> tickets=new ArrayList<>();
}
