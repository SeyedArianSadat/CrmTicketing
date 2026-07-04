package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.customer.CustomerCreateDto;
import com.company.crmticketing.dto.customer.CustomerDto;
import com.company.crmticketing.dto.customer.CustomerUpdateDto;
import com.company.crmticketing.model.Customer;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "customerRequests", ignore = true)
    Customer toEntity(CustomerCreateDto customerCreatDto);

    @Mapping(target = "userId", ignore = true)
    CustomerDto toDto(Customer customer);


    List<CustomerDto> toCustomerDtoList(List<Customer> customers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "customerRequests", ignore = true)
    void updateCustomerFromDto(CustomerUpdateDto updateDto, @MappingTarget Customer customer);

}
