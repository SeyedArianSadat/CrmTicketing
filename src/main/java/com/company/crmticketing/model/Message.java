package com.company.crmticketing.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "messageEntity")
@Table(name = "messages")


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Message extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @NotBlank
    @Column(name = "content", length = 200)
    private String content;

    private boolean internalNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private CustomerRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "sender_user_id",nullable = false)
    // private User senderUser;
}
