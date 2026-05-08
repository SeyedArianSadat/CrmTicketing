package com.company.crmticketing.dto.Customer;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerCreateDto(

        @NotBlank
        @Pattern(regexp = "^[A-Z][a-z]*$")
        String customerName,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 11, max = 11,message = "phone must be 11 character")
        String phone
){}
