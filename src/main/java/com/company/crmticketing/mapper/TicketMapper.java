package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.Ticket.TicketDto;
import com.company.crmticketing.dto.Ticket.TicketUpdateDto;
import com.company.crmticketing.model.Ticket;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TicketMapper {
    //    @Mapping(source = "department_id", target = "department")
//    @Mapping(source = "sla_id", target = "sla")
//    @Mapping(source = "agent_id", target = "agentId")
    Ticket toEntity(TicketDto ticketDto);

    //    @Mapping(target = "department", ignore = true)
//    @Mapping(target = "sla", ignore = true)
//    @Mapping(target = "agentId", ignore = true)
    TicketDto toDto(Ticket ticket);

    List<TicketDto> toTicketDtoList(List<Ticket> tickets);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTicketFromDto(TicketUpdateDto updateDto, @MappingTarget Ticket ticket);
}
