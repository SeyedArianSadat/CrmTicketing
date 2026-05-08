package com.company.crmticketing.dto.CustomerRequest;

import com.company.crmticketing.model.Customer;
import com.company.crmticketing.model.Message;
import com.company.crmticketing.model.Ticket;
import com.company.crmticketing.model.enums.Channel;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.model.enums.RequestType;
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
public class CustomerRequestDto {
    private Long requestId;

    @NotBlank(message = "title is required")
    @Size(min = 1, max = 20, message = "title must be between 1 to 20 character")
    @Pattern(regexp = "^[A-Z][a-z]*$")
    private String title;

    @NotBlank(message = "description is required")
    @Size(min = 1, max = 100, message = "description must be between 1 to 100 character ")
    private String description;

    @NotBlank(message = "channel is required")
    private Channel channel;

    @NotBlank(message = "requestType is required")
    private RequestType requestType;

    @NotBlank(message = "requestStatus is required")
    private RequestStatus requestStatus;

    private Customer customer;

    private Ticket ticket;

    private List<Message> messages=new ArrayList<>();

}
