package com.company.crmticketing.mapper;


import com.company.crmticketing.dto.Customer.CustomerDto;
import com.company.crmticketing.dto.Customer.CustomerUpdateDto;
import com.company.crmticketing.model.Customer;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
//    @Mapping(source = "userId",target = "user")
    Customer toEntity(CustomerDto customerDto);

//    @Mapping(target = "user", ignore = true)
    CustomerDto toDto(Customer customer);


    List<CustomerDto> toCustomerDtoList(List<Customer> customers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerFromDto(CustomerUpdateDto updateDto, @MappingTarget Customer customer);

}
