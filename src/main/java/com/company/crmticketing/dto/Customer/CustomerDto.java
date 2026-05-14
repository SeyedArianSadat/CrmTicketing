package com.company.crmticketing.dto.Customer;

import com.company.crmticketing.model.CustomerRequest;
import com.company.crmticketing.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CustomerDto {

    private Long CustomerId;

    @NotBlank
    @Pattern(regexp = "^[A-Z][a-z]*$")
    private String customerName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 11, max = 11,message = "phone must be 11 character")
    private String phone;

    private User  user;

    private List<CustomerRequest> customerRequests=new ArrayList<>();

}
