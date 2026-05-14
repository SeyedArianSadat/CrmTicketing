package com.company.crmticketing.service;


import com.company.crmticketing.dto.Customer.CustomerDto;
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
    public CustomerDto createCustomer(CustomerDto customerDto) {
        log.debug("create customer with customer");
        try {
            Customer customer = customerMapper.toEntity(customerDto);
            customerRepository.save(customer);
            return customerMapper.toDto(customer);
        } catch (Exception e) {
            log.error("create customer with customer", e);
            throw new IllegalArgumentException("create customer with customer");
        }
    }

    @Transactional
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto) {
        log.debug("update customer with customer");
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        try {
            Customer customer = customerMapper.toEntity(customerDto);
            customerRepository.save(customer);
            return customerMapper.toDto(customer);
        } catch (Exception e) {
            log.error("update customer with customer", e);
            throw new IllegalArgumentException("update customer with customer");
        }
    }

    @Transactional
    public void deleteByCustomerId(Long customerId) {
        log.debug("delete customer with customer");
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }
        try {
            customerRepository.deleteById(customerId);
        } catch (Exception e) {
            log.error("delete customer with customer", e);
            throw new IllegalArgumentException("delete customer with customer");
        }
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
