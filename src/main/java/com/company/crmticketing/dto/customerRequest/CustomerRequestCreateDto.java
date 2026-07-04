package com.company.crmticketing.dto.customerRequest;

import com.company.crmticketing.model.enums.RequestType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerRequestCreateDto(

        @NotBlank(message = "title is required")
        @Size(min = 1, max = 20,message = "title must be between 1 to 20 character")
        String title,

        @NotBlank(message = "description is required")
        @Size(min=1 , max = 100 , message = "description must be between 1 to 100 character ")
        String description,

        @NotNull(message = "requestType is required")
        RequestType requestType,

        @NotNull(message = "customer is required")
        Long customerId
) {
}
