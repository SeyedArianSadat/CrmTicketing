package com.company.crmticketing.dto.message;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MessageCreateDto(
        @NotBlank(message = "content is required")
        @Size(min = 10,max = 200,message = "explain content")
        String content,

        boolean internalNote,

        Long requestId,

        Long ticketId,

        @NotNull(message = "sender user is required")
        Long senderUserId

) {
}
