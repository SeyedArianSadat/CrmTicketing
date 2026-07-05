package com.company.crmticketing.service;


import com.company.crmticketing.dto.customerRequest.CustomerRequestCreateDto;
import com.company.crmticketing.dto.customerRequest.CustomerRequestDto;
import com.company.crmticketing.dto.customerRequest.CustomerRequestUpdateDto;
import com.company.crmticketing.exception.CustomerNotFoundException;
import com.company.crmticketing.exception.CustomerRequestNotFoundException;
import com.company.crmticketing.mapper.CustomerRequestMapper;
import com.company.crmticketing.model.Customer;
import com.company.crmticketing.model.CustomerRequest;
import com.company.crmticketing.model.enums.RequestType;
import com.company.crmticketing.repository.CustomerRepository;
import com.company.crmticketing.repository.CustomerRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CustomerRequestService extends BaseEntityService<CustomerRequest, Long, CustomerRequestDto> {
    private final CustomerRequestRepository customerRequestRepository;
    private final CustomerRequestMapper customerRequestMapper;
    private final CustomerRepository customerRepository;

    public CustomerRequestService(CustomerRequestRepository customerRequestRepository, CustomerRequestMapper customerRequestMapper, CustomerRepository customerRepository) {
        super(customerRequestRepository, customerRequestMapper::toDto, dto -> {
            throw new UnsupportedOperationException("unsupported operation");
        });
        this.customerRequestRepository = customerRequestRepository;
        this.customerRequestMapper = customerRequestMapper;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerRequestDto createCustomerRequest(CustomerRequestCreateDto customerRequestDto) {
        log.debug("creating customer request");
        try {
            CustomerRequest customerRequest = customerRequestMapper.toEntity(customerRequestDto);
            if (customerRequestDto.customerId() != null) {

                Customer customer = customerRepository.findById(customerRequestDto.customerId()).orElseThrow(() -> new CustomerNotFoundException(customerRequestDto.customerId()));

                customerRequest.setCustomer(customer);
            }

            CustomerRequest saved = customerRequestRepository.save(customerRequest);

            return customerRequestMapper.toDto(saved);
        } catch (Exception e) {
            log.error("error creating customer request", e);
            throw new IllegalArgumentException("error creating customer request", e);
        }
    }

    @Transactional
    public CustomerRequestDto updateCustomerRequest(Long requestId, CustomerRequestUpdateDto updateDto) {

        log.debug("updating customer request");

        CustomerRequest existing = customerRequestRepository.findById(requestId).orElseThrow(() -> new CustomerRequestNotFoundException(requestId));

        try {

            customerRequestMapper.updateCustomerRequestFromDto(updateDto, existing);

            if (updateDto.customerId() != null) {

                Customer customer = customerRepository.findById(updateDto.customerId()).orElseThrow(() -> new CustomerNotFoundException(updateDto.customerId()));

                existing.setCustomer(customer);
            }

            CustomerRequest saved = customerRequestRepository.save(existing);

            return customerRequestMapper.toDto(saved);

        } catch (Exception e) {
            log.error("error updating customer request", e);
            throw new IllegalArgumentException("error updating customer request", e);
        }
    }


    @Transactional
    public void deleteByRequestId(Long requestId) {
        log.debug("deleting customer request");
        if (!existsActive(requestId)) {
            throw new CustomerRequestNotFoundException(requestId);
        }
        softDelete(requestId);
    }

    public Optional<CustomerRequestDto> findByTitle(String title) {
        log.debug("finding by ticket by title");
        return customerRequestRepository.findByTitle(title).map(customerRequestMapper::toDto);
    }

    public Optional<CustomerRequestDto> findByRequestType(RequestType requestType) {
        log.debug("finding by request type");
        return customerRequestRepository.findByRequestType(requestType).map(customerRequestMapper::toDto);
    }

    public Optional<CustomerRequestDto> findCustomerRequestByTicket(Long customerRequestId) {
        log.debug("finding by ticket id");
        return customerRequestRepository.findCustomerRequestByTicket(customerRequestId).map(customerRequestMapper::toDto);
    }

    public Optional<CustomerRequestDto> findCustomerRequestByMessages(Long customerRequestId) {
        log.debug("finding by messages");
        return customerRequestRepository.findCustomerRequestByMessages(customerRequestId).map(customerRequestMapper::toDto);
    }

    public Optional<CustomerRequestDto> findCustomerRequestByIdWithAllDetails(Long customerRequestId) {
        log.debug("finding by id");
        return customerRequestRepository.findCustomerRequestByIdWithAllDetails(customerRequestId).map(customerRequestMapper::toDto);
    }
    public Optional<CustomerRequestDto> findById(Long requestId) {

        log.debug("finding customer request by id");

        return customerRequestRepository.findById(requestId)
                .map(customerRequestMapper::toDto);
    }

    public List<CustomerRequestDto> findAllDtos() {

        log.debug("finding all customer requests");

        return customerRequestRepository.findAll()
                .stream()
                .map(customerRequestMapper::toDto)
                .toList();
    }

    @Override
    protected String getEntityTypeName() {
        return "CustomerRequest";
    }
}
