package com.company.crmticketing.controller;

import com.company.crmticketing.dto.CustomerRequest.CustomerRequestDto;
import com.company.crmticketing.model.enums.RequestType;
import com.company.crmticketing.service.CustomerRequestService;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/customer-requests")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Request Management", description = "Endpoints for managing customer requests (tickets)")
@SecurityRequirement(name = "bearerAuth")
public class CustomerRequestController {

    private final CustomerRequestService customerRequestService;

    // ────────────────── CREATE ──────────────────
    @Operation(summary = "Create a new customer request", description = "Creates a new customer request/ticket.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer request created successfully",
                    content = @Content(schema = @Schema(implementation = CustomerRequestDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<CustomerRequestDto> createCustomerRequest(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        log.info("REST request to create customer request: {}", customerRequestDto);
        CustomerRequestDto created = customerRequestService.createCustomerRequest(customerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ────────────────── UPDATE ──────────────────
    @Operation(summary = "Update an existing customer request", description = "Updates a customer request by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer request updated successfully",
                    content = @Content(schema = @Schema(implementation = CustomerRequestDto.class))),
            @ApiResponse(responseCode = "404", description = "Customer request not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{requestId}")
    public ResponseEntity<CustomerRequestDto> updateCustomerRequest(
            @Parameter(description = "ID of the customer request to update", required = true) @PathVariable Long requestId,
            @Valid @RequestBody CustomerRequestDto customerRequestDto) {
        log.info("REST request to update customer request {}: {}", requestId, customerRequestDto);
        CustomerRequestDto updated = customerRequestService.updateCustomerRequest(requestId, customerRequestDto);
        return ResponseEntity.ok(updated);
    }

    // ────────────────── DELETE ──────────────────
    @Operation(summary = "Delete a customer request by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer request deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer request not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{requestId}")
    public ResponseEntity<Void> deleteCustomerRequest(
            @Parameter(description = "ID of the customer request to delete", required = true) @PathVariable Long requestId) {
        log.info("REST request to delete customer request {}", requestId);
        customerRequestService.deleteByRequestId(requestId);
        return ResponseEntity.noContent().build();
    }

    // ────────────────── FIND BY TITLE ──────────────────
    @Operation(summary = "Find a customer request by title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer request found",
                    content = @Content(schema = @Schema(implementation = CustomerRequestDto.class))),
            @ApiResponse(responseCode = "404", description = "No customer request with that title"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search/by-title")
    public ResponseEntity<CustomerRequestDto> findByTitle(
            @Parameter(description = "Exact title of the customer request", required = true) @RequestParam String title) {
        log.debug("REST request to find customer request by title '{}'", title);
        Optional<CustomerRequestDto> request = customerRequestService.findByTitle(title);
        return request.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ────────────────── FIND BY REQUEST TYPE ──────────────────
    @Operation(summary = "Find a customer request by request type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer request found",
                    content = @Content(schema = @Schema(implementation = CustomerRequestDto.class))),
            @ApiResponse(responseCode = "404", description = "No customer request with that type"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search/by-request-type")
    public ResponseEntity<CustomerRequestDto> findByRequestType(
            @Parameter(description = "Request type value", required = true) @RequestParam RequestType requestType) {
        log.debug("REST request to find customer request by request type {}", requestType);
        Optional<CustomerRequestDto> request = customerRequestService.findByRequestType(requestType);
        return request.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ────────────────── FIND BY TICKET ──────────────────
    @Operation(summary = "Find a customer request by associated ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer request found",
                    content = @Content(schema = @Schema(implementation = CustomerRequestDto.class))),
            @ApiResponse(responseCode = "404", description = "No customer request linked to that ticket"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{requestId}/ticket")
    public ResponseEntity<CustomerRequestDto> findByTicket(
            @Parameter(description = "Customer request ID (used to find the related ticket)", required = true) @PathVariable Long requestId) {
        log.debug("REST request to find customer request by ticket id {}", requestId);
        Optional<CustomerRequestDto> request = customerRequestService.findCustomerRequestByTicket(requestId);
        return request.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ────────────────── FIND BY MESSAGES ──────────────────
    @Operation(summary = "Find a customer request with its messages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer request found with messages",
                    content = @Content(schema = @Schema(implementation = CustomerRequestDto.class))),
            @ApiResponse(responseCode = "404", description = "No customer request found for that ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{requestId}/messages")
    public ResponseEntity<CustomerRequestDto> findByMessages(
            @Parameter(description = "Customer request ID", required = true) @PathVariable Long requestId) {
        log.debug("REST request to find customer request with messages for id {}", requestId);
        Optional<CustomerRequestDto> request = customerRequestService.findCustomerRequestByMessages(requestId);
        return request.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ────────────────── FIND WITH ALL DETAILS ──────────────────
    @Operation(summary = "Get a customer request with all details", description = "Eager loads all related entities (messages, ticket, etc.)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer request with all details",
                    content = @Content(schema = @Schema(implementation = CustomerRequestDto.class))),
            @ApiResponse(responseCode = "404", description = "Customer request not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{requestId}/details")
    public ResponseEntity<CustomerRequestDto> findByIdWithAllDetails(
            @Parameter(description = "Customer request ID", required = true) @PathVariable Long requestId) {
        log.debug("REST request to get customer request {} with all details", requestId);
        Optional<CustomerRequestDto> request = customerRequestService.findCustomerRequestByIdWithAllDetails(requestId);
        return request.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}