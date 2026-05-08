package com.company.crmticketing.model;

import com.company.crmticketing.model.enums.Priority;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "slaEntity")
@Table(name = "slas")

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Sla extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slaId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Priority priorityLevel;

    @Min(value = 1)
    private int responseTimeMinutes;

    @Min(value = 1)
    private int resolutionTimeMinutes;

    @Column(name = "description", nullable = false)
    private String description;

}
