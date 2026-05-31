package com.company.crmticketing.dto.Message;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageCreateDto(
        @NotBlank(message = "content is required")
        @Size(min = 10,max = 200,message = "explain content")
        String content,

        boolean internalNote

) {
}
