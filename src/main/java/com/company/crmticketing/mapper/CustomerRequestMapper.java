package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.customerRequest.CustomerRequestCreateDto;
import com.company.crmticketing.dto.customerRequest.CustomerRequestDto;
import com.company.crmticketing.dto.customerRequest.CustomerRequestUpdateDto;
import com.company.crmticketing.model.CustomerRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerRequestMapper {

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    @Mapping(target = "messages", ignore = true)
    CustomerRequest toEntity(CustomerRequestCreateDto customerRequestDtoDto);

    @Mapping(target = "customerId", source = "customer.customerId")
    @Mapping(target = "ticketId", source = "ticket.ticketId")
    CustomerRequestDto toDto(CustomerRequest customerRequest);

    List<CustomerRequestDto> toCustomerDtoList(List<CustomerRequest> customerRequests);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "ticket", ignore = true)
    @Mapping(target = "messages", ignore = true)
    void updateCustomerRequestFromDto(CustomerRequestUpdateDto updateDto, @MappingTarget CustomerRequest customerRequest);
}
