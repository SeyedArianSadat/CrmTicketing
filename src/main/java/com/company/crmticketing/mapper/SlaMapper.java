package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.Sla.SlaCreateDto;
import com.company.crmticketing.dto.Sla.SlaDto;
import com.company.crmticketing.dto.Sla.SlaResponseDto;
import com.company.crmticketing.dto.Sla.SlaUpdateDto;
import com.company.crmticketing.model.Sla;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SlaMapper {
    Sla toEntity(SlaDto slaDto);

    SlaDto toDto(Sla sla);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSlaFromDto(SlaUpdateDto updateDto, @MappingTarget Sla sla);
}
