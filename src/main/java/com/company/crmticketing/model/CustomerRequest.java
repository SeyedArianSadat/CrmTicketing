package com.company.crmticketing.model;

import com.company.crmticketing.model.enums.Channel;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.model.enums.RequestType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "customerRequestEntity")
@Table(name = "customer_requests")


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CustomerRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RequestId;

    @NotBlank
    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "description",nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @OneToMany(mappedBy = "request",cascade = CascadeType.ALL)
    private List<Message> messages=new ArrayList<Message>();
}
