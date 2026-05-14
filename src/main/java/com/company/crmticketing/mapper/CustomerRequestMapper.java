package com.company.crmticketing.mapper;

import com.company.crmticketing.dto.Customer.CustomerDto;
import com.company.crmticketing.dto.CustomerRequest.CustomerRequestDto;
import com.company.crmticketing.dto.CustomerRequest.CustomerRequestUpdateDto;
import com.company.crmticketing.model.CustomerRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerRequestMapper {
//    @Mapping(source = "customer_id",target = "customer")
//    @Mapping(source = "ticket_id",target = "ticket")
    CustomerRequest toEntity(CustomerRequestDto customerRequestDtoDto);

//    @Mapping(target = "customer",ignore = true)
//    @Mapping(target = "ticket",ignore = true)
    CustomerRequestDto toDto(CustomerRequest customerRequest);

    List<CustomerRequestDto> toCustomerDtoList(List<CustomerRequest> customerRequests);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerRequestFromDto(CustomerRequestUpdateDto updateDto, @MappingTarget CustomerRequest customerRequest);
}
