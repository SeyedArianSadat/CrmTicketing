package com.company.crmticketing.service;

import com.company.crmticketing.dto.customerRequest.CustomerRequestCreateDto;
import com.company.crmticketing.dto.customerRequest.CustomerRequestDto;
import com.company.crmticketing.dto.customerRequest.CustomerRequestUpdateDto;
import com.company.crmticketing.exception.CustomerNotFoundException;
import com.company.crmticketing.exception.CustomerRequestNotFoundException;
import com.company.crmticketing.mapper.CustomerRequestMapper;
import com.company.crmticketing.model.Customer;
import com.company.crmticketing.model.CustomerRequest;
import com.company.crmticketing.model.enums.Channel;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.model.enums.RequestType;
import com.company.crmticketing.repository.CustomerRepository;
import com.company.crmticketing.repository.CustomerRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerRequestServiceTest {

    @Mock
    private CustomerRequestRepository customerRequestRepository;

    @Mock
    private CustomerRequestMapper customerRequestMapper;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerRequestService customerRequestService;

    @Test
    void createCustomerRequestLinksCustomerAndReturnsDto() {
        CustomerRequestCreateDto createDto = createDto(1L);
        CustomerRequest request = request("Need help");
        Customer customer = new Customer();
        CustomerRequestDto response = responseDto(10L);

        when(customerRequestMapper.toEntity(createDto)).thenReturn(request);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRequestRepository.save(request)).thenReturn(request);
        when(customerRequestMapper.toDto(request)).thenReturn(response);

        CustomerRequestDto result = customerRequestService.createCustomerRequest(createDto);

        assertThat(result).isSameAs(response);
        assertThat(request.getCustomer()).isSameAs(customer);
        verify(customerRequestRepository).save(request);
    }

    @Test
    void createCustomerRequestWrapsMissingCustomer() {
        CustomerRequestCreateDto createDto = createDto(404L);
        CustomerRequest request = request("Need help");

        when(customerRequestMapper.toEntity(createDto)).thenReturn(request);
        when(customerRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerRequestService.createCustomerRequest(createDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasCauseInstanceOf(CustomerNotFoundException.class);

        verify(customerRequestRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void updateCustomerRequestPatchesExistingRequestAndCustomer() {
        CustomerRequest existing = request("Old");
        Customer customer = new Customer();
        CustomerRequestUpdateDto updateDto = updateDto(2L);
        CustomerRequestDto response = responseDto(11L);

        when(customerRequestRepository.findById(11L)).thenReturn(Optional.of(existing));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
        when(customerRequestRepository.save(existing)).thenReturn(existing);
        when(customerRequestMapper.toDto(existing)).thenReturn(response);

        CustomerRequestDto result = customerRequestService.updateCustomerRequest(11L, updateDto);

        assertThat(result).isSameAs(response);
        assertThat(existing.getCustomer()).isSameAs(customer);
        verify(customerRequestMapper).updateCustomerRequestFromDto(updateDto, existing);
    }

    @Test
    void updateCustomerRequestThrowsWhenRequestMissing() {
        CustomerRequestUpdateDto updateDto = updateDto(2L);
        when(customerRequestRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerRequestService.updateCustomerRequest(404L, updateDto))
                .isInstanceOf(CustomerRequestNotFoundException.class);
    }

    @Test
    void deleteByRequestIdSoftDeletesActiveRequest() {
        CustomerRequest request = request("Need help");
        when(customerRequestRepository.findActiveById(11L)).thenReturn(Optional.of(request));
        when(customerRequestRepository.softDeleteWithRetry(11L, 3)).thenReturn(true);

        customerRequestService.deleteByRequestId(11L);

        verify(customerRequestRepository).softDeleteWithRetry(11L, 3);
    }

    @Test
    void findAllMapsEveryRequestToDto() {
        CustomerRequest first = request("First");
        CustomerRequest second = request("Second");
        CustomerRequestDto firstDto = responseDto(1L);
        CustomerRequestDto secondDto = responseDto(2L);

        when(customerRequestRepository.findAll()).thenReturn(List.of(first, second));
        when(customerRequestMapper.toDto(first)).thenReturn(firstDto);
        when(customerRequestMapper.toDto(second)).thenReturn(secondDto);

        List<CustomerRequestDto> result = customerRequestService.findAll();

        assertThat(result).containsExactly(firstDto, secondDto);
    }

    private static CustomerRequestCreateDto createDto(Long customerId) {
        return new CustomerRequestCreateDto(
                "Need help",
                "Please help",
                RequestType.TECHNICAL,
                customerId
        );
    }

    private static CustomerRequestUpdateDto updateDto(Long customerId) {
        return new CustomerRequestUpdateDto(
                "Updated",
                "Updated description",
                Channel.PORTAL,
                RequestStatus.OPEN,
                RequestType.TECHNICAL,
                customerId
        );
    }

    private static CustomerRequest request(String title) {
        CustomerRequest request = new CustomerRequest();
        request.setTitle(title);
        return request;
    }

    private static CustomerRequestDto responseDto(Long id) {
        CustomerRequestDto dto = new CustomerRequestDto();
        dto.setRequestId(id);
        return dto;
    }
}
