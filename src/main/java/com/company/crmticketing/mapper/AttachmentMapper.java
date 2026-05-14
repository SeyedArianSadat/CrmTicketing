package com.company.crmticketing.mapper;

import com.company.crmticketing.dto.Attachment.AttachmentDto;
import com.company.crmticketing.dto.Attachment.AttachmentUpdateDto;
import com.company.crmticketing.dto.Customer.CustomerDto;
import com.company.crmticketing.model.Attachment;

import com.company.crmticketing.model.Customer;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AttachmentMapper {
    //    @Mapping(target = "attachmentId",ignore = true)
//    @Mapping(target = "deleted", constant = "false")
//    @Mapping(target = "ticket",ignore = true)
    Attachment toEntity(AttachmentDto attachmentDto);

    //    @Mapping(target = "ticket",ignore = true)
    AttachmentDto toDto(Attachment attachment);

    List<AttachmentDto> toAttachmentDtoList(List<Attachment> attachments);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAttachmentFromDto(AttachmentUpdateDto updateDto, @MappingTarget Attachment attachment);
}
