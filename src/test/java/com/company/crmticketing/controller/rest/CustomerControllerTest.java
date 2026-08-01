package com.company.crmticketing.controller.rest;

import com.company.crmticketing.dto.customer.CustomerCreateDto;
import com.company.crmticketing.dto.customer.CustomerDto;
import com.company.crmticketing.dto.customer.CustomerUpdateDto;
import com.company.crmticketing.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerControllerTest {

    private final CustomerService customerService = mock();
    private final CustomerController controller = new CustomerController(customerService);

    @Test
    void createCustomerReturnsCreatedCustomerWithCreatedStatus() {
        CustomerCreateDto createDto =
                new CustomerCreateDto("Acme", "support@acme.test", "09123456789", null);
        CustomerDto response = customerDto(1L, "Acme");
        when(customerService.createCustomer(createDto)).thenReturn(response);

        ResponseEntity<CustomerDto> result = controller.createCustomer(createDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    void updateCustomerReturnsUpdatedCustomer() {
        CustomerUpdateDto updateDto = new CustomerUpdateDto("New", "new@test.local", "09111111111");
        CustomerDto response = customerDto(1L, "New");
        when(customerService.updateCustomer(1L, updateDto)).thenReturn(response);

        ResponseEntity<CustomerDto> result = controller.updateCustomer(1L, updateDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    void deleteCustomerDelegatesAndReturnsNoContent() {
        ResponseEntity<Void> result = controller.deleteCustomer(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(customerService).deleteByCustomerId(1L);
    }

    @Test
    void findByCustomerNameReturnsOkWhenCustomerExists() {
        CustomerDto response = customerDto(1L, "Acme");
        when(customerService.findByCustomerName("Acme")).thenReturn(Optional.of(response));

        ResponseEntity<CustomerDto> result = controller.findByCustomerName("Acme");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    void findByCustomerNameReturnsNotFoundWhenMissing() {
        when(customerService.findByCustomerName("Missing")).thenReturn(Optional.empty());

        ResponseEntity<CustomerDto> result = controller.findByCustomerName("Missing");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllCustomersWithRequestsReturnsList() {
        List<CustomerDto> customers = List.of(customerDto(1L, "Acme"));
        when(customerService.findAllCustomersWithCustomerRequests()).thenReturn(customers);

        ResponseEntity<List<CustomerDto>> result = controller.getAllCustomersWithRequests();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(customers);
    }

    private static CustomerDto customerDto(Long id, String name) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(id);
        dto.setCustomerName(name);
        return dto;
    }
}
