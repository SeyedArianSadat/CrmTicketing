package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.sla.SlaCreateDto;
import com.company.crmticketing.dto.sla.SlaDto;
import com.company.crmticketing.dto.sla.SlaUpdateDto;
import com.company.crmticketing.model.Sla;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SlaMapper {
    Sla toEntity(SlaCreateDto slaDto);

    SlaDto toDto(Sla sla);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSlaFromDto(SlaUpdateDto updateDto, @MappingTarget Sla sla);
}
