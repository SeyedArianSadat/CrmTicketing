package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.supportAgent.SupportAgentCreateDto;
import com.company.crmticketing.dto.supportAgent.SupportAgentDto;
import com.company.crmticketing.dto.supportAgent.SupportAgentUpdateDto;
import com.company.crmticketing.model.SupportAgent;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupportAgentMapper {
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "assignedTickets", ignore = true)
    @Mapping(target = "user", ignore = true)
    SupportAgent toEntity(SupportAgentCreateDto supportAgentDto);

    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    SupportAgentDto toDto(SupportAgent supportAgent);

    List<SupportAgentDto> toDtoList(List<SupportAgent> supportAgents);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "assignedTickets", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateSupportAgentFromDto(SupportAgentUpdateDto updateDto, @MappingTarget SupportAgent supportAgent);
}
