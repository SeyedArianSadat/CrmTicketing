package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.Message.MessageDto;
import com.company.crmticketing.dto.Message.MessageUpdateDto;
import com.company.crmticketing.model.Message;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MessageMapper {
    //    @Mapping(source = "ticket_id",target = "ticket")
//    @Mapping(source = "request_id",target = "request")
//    @Mapping(source = "senderUser_id",target = "senderUser")
    Message toEntity(MessageDto messageDto);

    //    @Mapping(target = "ticket",ignore = true)
//    @Mapping(target = "request",ignore = true)
//    @Mapping(target = "senderUser",ignore = true)
    MessageDto toDto(Message message);

    List<MessageDto> toMessageDtoList(List<Message> messages);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMessageFromDto(MessageUpdateDto updateDto, @MappingTarget Message message);
}
