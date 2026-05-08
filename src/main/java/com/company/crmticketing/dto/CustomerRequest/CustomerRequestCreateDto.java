package com.company.crmticketing.dto.CustomerRequest;

import com.company.crmticketing.model.enums.Channel;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.model.enums.RequestType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRequestCreateDto(

        @NotBlank(message = "title is required")
        @Size(min = 1, max = 20,message = "title must be between 1 to 20 character")
        @Pattern(regexp = "^[A-Z][a-z]*$")
        String title,

        @NotBlank(message = "description is required")
        @Size(min=1 , max = 100 , message = "description must be between 1 to 100 character ")
        String description,

        @NotBlank(message = "channel is required")
        Channel channel,

        @NotBlank(message = "requestType is required")
        RequestType requestType,

        @NotBlank(message = "requestStatus is required")
        RequestStatus requestStatus
) {
}
