package com.company.crmticketing.mapper;

import com.company.crmticketing.dto.ticket.TicketCreateDto;
import com.company.crmticketing.dto.ticket.TicketDto;
import com.company.crmticketing.dto.ticket.TicketUpdateDto;
import com.company.crmticketing.model.Ticket;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TicketMapper {

    @Mapping(target = "ticketId", ignore = true)
    @Mapping(target = "customerRequest", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "agent", ignore = true)
    @Mapping(target = "sla", ignore = true)
    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "ticketHistories", ignore = true)
    Ticket toEntity(TicketCreateDto createDto);

    TicketDto toDto(Ticket ticket);

    List<TicketDto> toTicketDtoList(List<Ticket> tickets);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "ticketId", ignore = true)
    @Mapping(target = "customerRequest", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "agent", ignore = true)
    @Mapping(target = "sla", ignore = true)
    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "ticketHistories", ignore = true)
    void updateTicketFromDto(
            TicketUpdateDto updateDto,
            @MappingTarget Ticket ticket
    );
}