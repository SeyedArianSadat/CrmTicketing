package com.company.crmticketing.dto.Attachment;

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
public class AttachmentDto {
    private Long attachmentId;

    @NotBlank(message = "file name is required")
    @Size(min=2,max = 30,message = "fileName name must be between 2 and 30 characters")
    private String fileName;

    @NotBlank(message = "file path is required")
    private String filePath;

    private Ticket ticket;
}
