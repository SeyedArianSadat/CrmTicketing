package com.company.crmticketing.controller;

import com.company.crmticketing.dto.Customer.CustomerDto;
import com.company.crmticketing.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Management", description = "Endpoints for managing customers")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    private final CustomerService customerService;

    // ────────────────── CREATE ──────────────────
    @Operation(summary = "Create a new customer", description = "Creates a new customer entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        log.info("REST request to create customer: {}", customerDto);
        CustomerDto created = customerService.createCustomer(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ────────────────── UPDATE ──────────────────
    @Operation(summary = "Update an existing customer", description = "Updates a customer by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDto.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @Parameter(description = "ID of the customer to update", required = true) @PathVariable Long customerId,
            @Valid @RequestBody CustomerDto customerDto) {
        customerDto.setCustomerId(customerId);
        log.info("REST request to update customer {}: {}", customerId, customerDto);
        CustomerDto updated = customerService.updateCustomer(customerId, customerDto);
        return ResponseEntity.ok(updated);
    }

    // ────────────────── DELETE ──────────────────
    @Operation(summary = "Delete a customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID of the customer to delete", required = true) @PathVariable Long customerId) {
        log.info("REST request to delete customer {}", customerId);
        customerService.deleteByCustomerId(customerId);
        return ResponseEntity.noContent().build();
    }

    // ────────────────── FIND BY NAME ──────────────────
    @Operation(summary = "Find a customer by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                    content = @Content(schema = @Schema(implementation = CustomerDto.class))),
            @ApiResponse(responseCode = "404", description = "No customer with that name"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search/by-name")
    public ResponseEntity<CustomerDto> findByCustomerName(
            @Parameter(description = "Customer name (exact match)", required = true) @RequestParam String customerName) {
        log.debug("REST request to find customer by name '{}'", customerName);
        Optional<CustomerDto> customer = customerService.findByCustomerName(customerName);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //    // ────────────────── FIND WITH USER ──────────────────
    @Operation(summary = "Find a customer by ID with associated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found with user details",
                    content = @Content(schema = @Schema(implementation = CustomerDto.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{customerId}/user")
    public ResponseEntity<CustomerDto> findByCustomerIdWithUser(
            @Parameter(description = "Customer ID", required = true) @PathVariable Long customerId) {
        log.debug("REST request to find customer {} with user", customerId);
        Optional<CustomerDto> customer = customerService.findByCustomerIdWithUser(customerId);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //    // ────────────────── FIND WITH CUSTOMER REQUEST ──────────────────
    @Operation(summary = "Find a customer by ID with their customer requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found with requests",
                    content = @Content(schema = @Schema(implementation = CustomerDto.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{customerId}/requests")
    public ResponseEntity<CustomerDto> findByCustomerIdWithCustomerRequest(
            @Parameter(description = "Customer ID", required = true) @PathVariable Long customerId) {
        log.debug("REST request to find customer {} with requests", customerId);
        Optional<CustomerDto> customer = customerService.findByCustomerIdWithCustomerRequest(customerId);
        return customer.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //    // ────────────────── FIND ALL WITH CUSTOMER REQUESTS ──────────────────
    @Operation(summary = "Get all customers with their customer requests", description = "Returns a list of all customers eagerly loading their customer requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of customers with requests"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/with-requests")
    public ResponseEntity<List<CustomerDto>> getAllCustomersWithRequests() {
        log.debug("REST request to get all customers with their requests");
        List<CustomerDto> customers = customerService.findAllCustomersWithCustomerRequests();
        return ResponseEntity.ok(customers);
    }
}
