package com.company.crmticketing.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "ticketHistoryEntity")
@Table(name = "ticket_histories")

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class TicketHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketHistoryId;

    @NotBlank
    @Column(name = "field_changed",nullable = false)
    private String fieldChanged;

    @NotBlank
    @Column(name = "old_value",nullable = false)
    private String oldValue;

    @NotBlank
    @Column(name = "new_value",nullable = false)
    private String newValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id",nullable = false)
    private Ticket ticket;
}
