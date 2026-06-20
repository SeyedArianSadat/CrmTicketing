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

    @Mapping(target = "customerRequestId",ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "slaId", ignore = true)
    @Mapping(target = "agentId", ignore = true)
    Ticket toEntity(TicketDto ticketDto);

    TicketDto toDto(Ticket ticket);

    List<TicketDto> toTicketDtoList(List<Ticket> tickets);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTicketFromDto(TicketUpdateDto updateDto, @MappingTarget Ticket ticket);
}
