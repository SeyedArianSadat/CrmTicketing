package com.company.crmticketing.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "customerEntity")
@Table(name = "customers",
        uniqueConstraints =
                {
                 @UniqueConstraint(name = "uk_email", columnNames = "email"),
                 @UniqueConstraint(name = "uk_phone", columnNames = "phone")
                }
)


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotBlank
    @Column(name = "customer_name")
    private String customerName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Column(name = "customer_phone")
    private String phone;

    //@OneToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id",unique = true)
    // private User user;

    @OneToMany(mappedBy = "customer")
    private List<CustomerRequest> customerRequests = new ArrayList<>();

}
