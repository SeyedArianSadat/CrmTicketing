package com.company.crmticketing.mapper;




import com.company.crmticketing.dto.ticketHistory.TicketHistoryCreateDto;
import com.company.crmticketing.dto.ticketHistory.TicketHistoryDto;
import com.company.crmticketing.dto.ticketHistory.TicketHistoryUpdateDto;
import com.company.crmticketing.model.TicketHistory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TicketHistoryMapper {
    @Mapping(target = "ticket", ignore = true)
    TicketHistory toEntity(TicketHistoryCreateDto ticketHistoryDto);

    @Mapping(target = "ticketId", source = "ticket.ticketId")
    TicketHistoryDto toDto(TicketHistory ticketHistory);

    List<TicketHistoryDto> toTicketHistoryDtoList(List<TicketHistory> ticketHistories);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "ticket", ignore = true)
    void updateTicketHistoryFromDto(TicketHistoryUpdateDto updateDto, @MappingTarget TicketHistory ticketHistory);
}
