package com.company.crmticketing.service;


import com.company.crmticketing.dto.CustomerRequest.CustomerRequestDto;
import com.company.crmticketing.dto.CustomerRequest.CustomerRequestUpdateDto;
import com.company.crmticketing.exception.CustomerRequestNotFoundException;
import com.company.crmticketing.mapper.CustomerRequestMapper;
import com.company.crmticketing.model.CustomerRequest;
import com.company.crmticketing.model.enums.RequestType;
import com.company.crmticketing.repository.CustomerRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CustomerRequestService extends BaseEntityService<CustomerRequest, Long, CustomerRequestDto> {
    private final CustomerRequestRepository customerRequestRepository;
    private final CustomerRequestMapper customerRequestMapper;

    public CustomerRequestService(CustomerRequestRepository customerRequestRepository
            , CustomerRequestMapper customerRequestMapper) {
        super(customerRequestRepository,
                customerRequestMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.customerRequestRepository = customerRequestRepository;
        this.customerRequestMapper = customerRequestMapper;
    }

    @Transactional
    public CustomerRequestDto createCustomerRequest(CustomerRequestDto customerRequestDto) {
        log.debug("creating customer request");
        try {
            CustomerRequest customerRequest = customerRequestMapper.toEntity(customerRequestDto);
            customerRequestRepository.save(customerRequest);
            return customerRequestMapper.toDto(customerRequest);
        } catch (Exception e) {
            log.error("error creating customer request", e);
            throw new IllegalArgumentException("error creating customer request", e);
        }
    }

    @Transactional
    public CustomerRequestDto updateCustomerRequest(Long requestId, CustomerRequestDto customerRequestDto) {
        log.debug("updating customer request");
        CustomerRequest existing = customerRequestRepository.findById(requestId).orElseThrow(() -> new CustomerRequestNotFoundException(requestId));
        try {
            CustomerRequestUpdateDto updateDto = new CustomerRequestUpdateDto(customerRequestDto.getTitle(), customerRequestDto.getDescription(), customerRequestDto.getChannel(), customerRequestDto.getRequestStatus(), customerRequestDto.getRequestType());
            customerRequestMapper.updateCustomerRequestFromDto(updateDto, existing);
            customerRequestRepository.save(existing);
            return customerRequestMapper.toDto(existing);
        } catch (Exception e) {
            log.error("error updating customer request", e);
            throw new IllegalArgumentException("error updating customer request", e);
        }
    }

    @Transactional
    public void deleteByRequestId(Long requestId) {
        log.debug("deleting customer request");
        if (!customerRequestRepository.existsById(requestId)) {
            throw new CustomerRequestNotFoundException(requestId);
        }
        try {
            customerRequestRepository.deleteById(requestId);
        } catch (Exception e) {
            log.error("error deleting customer request", e);
        }
    }

    public Optional<CustomerRequestDto> findByTitle(String title) {
        log.debug("finding by ticket by title");
        return customerRequestRepository.findByTitle(title)
                .map(customerRequestMapper::toDto);
    }

    public Optional<CustomerRequestDto> findByRequestType(RequestType requestType) {
        log.debug("finding by request type");
        return customerRequestRepository.findByRequestType(requestType)
                .map(customerRequestMapper::toDto);
    }

    public Optional<CustomerRequestDto> findCustomerRequestByTicket(Long customerRequestId) {
        log.debug("finding by ticket id");
        return customerRequestRepository.findCustomerRequestByTicket(customerRequestId)
                .map(customerRequestMapper::toDto);
    }

    public Optional<CustomerRequestDto> findCustomerRequestByMessages(Long customerRequestId) {
        log.debug("finding by messages");
        return customerRequestRepository.findCustomerRequestByMessages(customerRequestId)
                .map(customerRequestMapper::toDto);
    }

    public Optional<CustomerRequestDto> findCustomerRequestByIdWithAllDetails(Long customerRequestId) {
        log.debug("finding by id");
        return customerRequestRepository.findCustomerRequestByIdWithAllDetails(customerRequestId)
                .map(customerRequestMapper::toDto);
    }

    @Override
    protected String getEntityTypeName() {
        return "CustomerRequest";
    }
}
