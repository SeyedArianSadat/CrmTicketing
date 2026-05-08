package com.company.crmticketing.mapper;

import com.company.crmticketing.dto.Attachment.AttachmentCreateDto;
import com.company.crmticketing.dto.Attachment.AttachmentResponseDto;
import com.company.crmticketing.model.Attachment;
import jakarta.persistence.Table;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AttachmentMapper {
    @Mapping(target = "attachmentId",ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "ticket",ignore = true)
    Attachment toEntity(AttachmentCreateDto attachmentCreateDto);

    @Mapping(target = "ticket",source = "ticket")
    AttachmentResponseDto toDto(Attachment attachment);
}
