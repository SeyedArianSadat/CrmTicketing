package com.company.crmticketing.mapper;

import com.company.crmticketing.dto.attachment.AttachmentDto;
import com.company.crmticketing.dto.attachment.AttachmentUpdateDto;
import com.company.crmticketing.model.Attachment;


import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AttachmentMapper {

    @Mapping(target = "ticket", ignore = true)
    Attachment toEntity(AttachmentDto attachmentDto);

    @Mapping(target = "ticketId", ignore = true)
    AttachmentDto toDto(Attachment attachment);

    List<AttachmentDto> toAttachmentDtoList(List<Attachment> attachments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "ticket", ignore = true)
    void updateAttachmentFromDto(AttachmentUpdateDto updateDto, @MappingTarget Attachment attachment);
}
