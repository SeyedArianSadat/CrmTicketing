package com.company.crmticketing.mapper;




import com.company.crmticketing.dto.TicketHistory.TicketHistoryDto;
import com.company.crmticketing.dto.TicketHistory.TicketHistoryUpdateDto;
import com.company.crmticketing.model.TicketHistory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TicketHistoryMapper {
//    @Mapping(source = "ticketId", target = "ticket")
    TicketHistory toEntity(TicketHistoryDto ticketHistoryDto);

//    @Mapping(target = "ticket", ignore = true)
    TicketHistoryDto toDto(TicketHistory ticketHistory);

    List<TicketHistoryDto> toTicketHistoryDtoList(List<TicketHistory> ticketHistories);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTicketHistoryFromDto(TicketHistoryUpdateDto updateDto, @MappingTarget TicketHistory ticketHistory);
}
