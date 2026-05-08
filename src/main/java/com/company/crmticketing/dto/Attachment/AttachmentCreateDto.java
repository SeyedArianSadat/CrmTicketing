package com.company.crmticketing.dto.Attachment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AttachmentCreateDto (
    @NotBlank(message = "file name is required")
    @Size(min=2,max = 30,message = "fileName name must be between 2 and 30 characters")
     String fileName,

    @NotBlank(message = "file path is required")
    String filePath
    ){}


