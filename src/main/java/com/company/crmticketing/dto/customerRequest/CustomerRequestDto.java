package com.company.crmticketing.dto.customerRequest;


import com.company.crmticketing.model.enums.Channel;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.model.enums.RequestType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;



@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CustomerRequestDto {
    private Long requestId;

    @NotBlank(message = "title is required")
    @Size(min = 1, max = 20, message = "title must be between 1 to 20 character")
    private String title;

    @NotBlank(message = "description is required")
    @Size(min = 1, max = 100, message = "description must be between 1 to 100 character ")
    private String description;

    @NotNull(message = "channel is required")
    private Channel channel;

    @NotNull(message = "requestType is required")
    private RequestType requestType;

    @NotNull(message = "requestStatus is required")
    private RequestStatus requestStatus;

    private Long customerId;

    private Long ticketId;


}
