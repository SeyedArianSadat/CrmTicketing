package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.message.MessageDto;
import com.company.crmticketing.dto.message.MessageUpdateDto;
import com.company.crmticketing.model.Message;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MessageMapper {
    @Mapping(target = "ticket", ignore = true)
    @Mapping(target = "request", ignore = true)
    @Mapping(target = "senderUser", ignore = true)
    Message toEntity(MessageDto messageDto);

    @Mapping(target = "ticketId", ignore = true)
    @Mapping(target = "requestId", ignore = true)
    @Mapping(target = "senderUserId", ignore = true)
    MessageDto toDto(Message message);

    List<MessageDto> toMessageDtoList(List<Message> messages);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "ticket", ignore = true)
    @Mapping(target = "request", ignore = true)
    @Mapping(target = "senderUser", ignore = true)
    void updateMessageFromDto(MessageUpdateDto updateDto, @MappingTarget Message message);
}
