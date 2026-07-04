package com.company.crmticketing.service;


import com.company.crmticketing.dto.customer.CustomerCreateDto;
import com.company.crmticketing.dto.customer.CustomerDto;
import com.company.crmticketing.dto.customer.CustomerUpdateDto;
import com.company.crmticketing.exception.CustomerNotFoundException;
import com.company.crmticketing.mapper.CustomerMapper;
import com.company.crmticketing.model.Customer;
import com.company.crmticketing.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CustomerService extends BaseEntityService<Customer, Long, CustomerDto> {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository
            , CustomerMapper customerMapper) {
        super(customerRepository,
                customerMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    protected String getEntityTypeName() {
        return "Customer";
    }

    @Transactional
    public CustomerDto createCustomer(CustomerCreateDto dto) {

        log.debug("Creating customer");

        try {

            Customer customer = customerMapper.toEntity(dto);

            customerRepository.save(customer);

            return customerMapper.toDto(customer);

        } catch (Exception e) {

            log.error("Error creating customer", e);

            throw new IllegalArgumentException("Error creating customer", e);
        }
    }

    @Transactional
    public CustomerDto updateCustomer(Long customerId,
                                      CustomerUpdateDto dto) {

        log.debug("Updating customer");

        Customer existing =
                customerRepository.findById(customerId)
                        .orElseThrow(() ->
                                new CustomerNotFoundException(customerId));

        try {

            customerMapper.updateCustomerFromDto(dto, existing);

            customerRepository.save(existing);

            return customerMapper.toDto(existing);

        } catch (Exception e) {

            log.error("Error updating customer", e);

            throw new IllegalArgumentException("Error updating customer", e);
        }
    }

    @Transactional
    public void deleteByCustomerId(Long customerId) {
        log.debug("delete customer with customer");
        if (!existsActive(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
        softDelete(customerId);
    }

    public Optional<CustomerDto> findByCustomerName(String customerName) {
        log.debug("find customer with name");
        return customerRepository.findByCustomerName(customerName)
                .map(customerMapper::toDto);
    }

    public Optional<CustomerDto> findByCustomerIdWithUser(Long customerId) {
        log.debug("find customer with id");
        return customerRepository.findByCustomerIdWithUser(customerId)
                .map(customerMapper::toDto);
    }

    public Optional<CustomerDto> findByCustomerIdWithCustomerRequest(Long customerId) {
        log.debug("find customer with request");
        return customerRepository.findByCustomerIdWithCustomerRequest(customerId)
                .map(customerMapper::toDto);
    }

    public List<CustomerDto> findAllCustomersWithCustomerRequests() {
        log.debug("find all customers with customerRequests");
        List<Customer> customersWithCustomerRequests = customerRepository.findAllCustomersWithCustomerRequests();
        return customerMapper.toCustomerDtoList(customersWithCustomerRequests);

    }

}
