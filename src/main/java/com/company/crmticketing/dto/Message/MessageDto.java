package com.company.crmticketing.dto.Message;

import com.company.crmticketing.model.CustomerRequest;
import com.company.crmticketing.model.Ticket;
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
    @Size(min = 10,max = 200,message = "explain content")
   private String content;

    @NotBlank
    private boolean internalNote;

    private CustomerRequest request;

    private Ticket ticket;

    // private User senderUser;

}
