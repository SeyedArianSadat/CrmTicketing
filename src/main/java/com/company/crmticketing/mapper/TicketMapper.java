package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.ticket.TicketDto;
import com.company.crmticketing.dto.ticket.TicketUpdateDto;
import com.company.crmticketing.model.Ticket;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TicketMapper {

    @Mapping(target = "customerRequest",ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "sla", ignore = true)
    @Mapping(target = "agent", ignore = true)
    @Mapping(target = "ticketHistories",ignore = true)
    @Mapping(target = "messages",ignore = true)
    @Mapping(target = "attachments",ignore = true)
    Ticket toEntity(TicketDto ticketDto);

    @Mapping(target = "customerRequestId",ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "slaId", ignore = true)
    @Mapping(target = "agentId", ignore = true)
    TicketDto toDto(Ticket ticket);

    List<TicketDto> toTicketDtoList(List<Ticket> tickets);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "customerRequest",ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "sla", ignore = true)
    @Mapping(target = "messages",ignore = true)
    @Mapping(target = "agent", ignore = true)
    @Mapping(target = "ticketHistories",ignore = true)
    @Mapping(target = "attachments",ignore = true)
    void updateTicketFromDto(TicketUpdateDto updateDto, @MappingTarget Ticket ticket);
}
