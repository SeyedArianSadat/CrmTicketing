package com.company.crmticketing.dto.customer;


import jakarta.validation.constraints.Email;
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
public class CustomerDto {

    private Long customerId;

    @NotBlank
    private String customerName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 11, max = 11, message = "phone must be 11 character")
    private String phone;

    private Long userId;


}
