package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.SupportAgent.SupportAgentDto;
import com.company.crmticketing.dto.SupportAgent.SupportAgentUpdateDto;
import com.company.crmticketing.model.SupportAgent;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupportAgentMapper {
//    @Mapping(source = "department_id", target = "department")
    SupportAgent toEntity(SupportAgentDto supportAgentDto);

//    @Mapping(target = "department", ignore = true)
    SupportAgentDto toDto(SupportAgent supportAgent);

    List<SupportAgentDto> toDtoList(List<SupportAgent> supportAgents);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSupportAgentFromDto(SupportAgentUpdateDto updateDto, @MappingTarget SupportAgent supportAgent);
}
