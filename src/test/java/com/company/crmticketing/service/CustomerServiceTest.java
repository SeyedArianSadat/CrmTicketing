package com.company.crmticketing.service;

import com.company.crmticketing.dto.customer.CustomerCreateDto;
import com.company.crmticketing.dto.customer.CustomerDto;
import com.company.crmticketing.dto.customer.CustomerUpdateDto;
import com.company.crmticketing.exception.CustomerNotFoundException;
import com.company.crmticketing.mapper.CustomerMapper;
import com.company.crmticketing.model.Customer;
import com.company.crmticketing.repository.CustomerRepository;
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
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void createCustomerMapsSavesAndReturnsDto() {
        CustomerCreateDto createDto =
                new CustomerCreateDto("Acme", "support@acme.test", "09123456789", 3L);
        Customer entity = customer("Acme");
        CustomerDto response = customerDto(1L, "Acme");

        when(customerMapper.toEntity(createDto)).thenReturn(entity);
        when(customerRepository.save(entity)).thenReturn(entity);
        when(customerMapper.toDto(entity)).thenReturn(response);

        CustomerDto result = customerService.createCustomer(createDto);

        assertThat(result).isSameAs(response);
        verify(customerRepository).save(entity);
    }

    @Test
    void updateCustomerAppliesPatchToExistingEntity() {
        Customer existing = customer("Old");
        CustomerUpdateDto updateDto = new CustomerUpdateDto("New", "new@test.local", "09111111111");
        CustomerDto response = customerDto(7L, "New");

        when(customerRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(existing)).thenReturn(existing);
        when(customerMapper.toDto(existing)).thenReturn(response);

        CustomerDto result = customerService.updateCustomer(7L, updateDto);

        assertThat(result).isSameAs(response);
        verify(customerMapper).updateCustomerFromDto(updateDto, existing);
        verify(customerRepository).save(existing);
    }

    @Test
    void updateCustomerThrowsWhenCustomerDoesNotExist() {
        CustomerUpdateDto updateDto = new CustomerUpdateDto("New", "new@test.local", "09111111111");
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.updateCustomer(99L, updateDto))
                .isInstanceOf(CustomerNotFoundException.class);

        verify(customerRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deleteByCustomerIdSoftDeletesActiveCustomer() {
        Customer existing = customer("Acme");
        when(customerRepository.findActiveById(1L)).thenReturn(Optional.of(existing));
        when(customerRepository.softDeleteWithRetry(1L, 3)).thenReturn(true);

        customerService.deleteByCustomerId(1L);

        verify(customerRepository).softDeleteWithRetry(1L, 3);
    }

    @Test
    void deleteByCustomerIdThrowsWhenCustomerIsNotActive() {
        when(customerRepository.findActiveById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.deleteByCustomerId(1L))
                .isInstanceOf(CustomerNotFoundException.class);

        verify(customerRepository, never()).softDeleteWithRetry(1L, 3);
    }

    @Test
    void findAllCustomersWithCustomerRequestsMapsRepositoryResult() {
        List<Customer> customers = List.of(customer("Acme"), customer("Globex"));
        List<CustomerDto> dtos = List.of(customerDto(1L, "Acme"), customerDto(2L, "Globex"));

        when(customerRepository.findAllCustomersWithCustomerRequests()).thenReturn(customers);
        when(customerMapper.toCustomerDtoList(customers)).thenReturn(dtos);

        List<CustomerDto> result = customerService.findAllCustomersWithCustomerRequests();

        assertThat(result).isSameAs(dtos);
    }

    private static Customer customer(String name) {
        Customer customer = new Customer();
        customer.setCustomerName(name);
        return customer;
    }

    private static CustomerDto customerDto(Long id, String name) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(id);
        dto.setCustomerName(name);
        return dto;
    }
}
