package com.company.crmticketing.dto.message;

import com.company.crmticketing.model.CustomerRequest;
import com.company.crmticketing.model.Ticket;
import com.company.crmticketing.model.User;
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
public class MessageDto {
    private Long messageId;


    @NotBlank(message = "content is required")
    @Size(min = 10, max = 200, message = "explain content")
    private String content;


    private boolean internalNote;

    private Long requestId;

    private Long ticketId;

    private Long senderUserId;

}
