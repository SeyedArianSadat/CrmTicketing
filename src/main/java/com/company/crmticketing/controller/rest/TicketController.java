package com.company.crmticketing.controller.rest;

import com.company.crmticketing.dto.ticket.TicketCreateDto;
import com.company.crmticketing.dto.ticket.TicketDto;
import com.company.crmticketing.dto.ticket.TicketUpdateDto;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.service.TicketService;
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
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ticket Management", description = "Endpoints for creating, retrieving, updating and deleting tickets")
@SecurityRequirement(name = "bearerAuth")
public class TicketController {

    private final TicketService ticketService;

    // ────────────────── CREATE ──────────────────
    @Operation(summary = "Create a new ticket", description = "Creates a new ticket.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket created successfully",
                    content = @Content(schema = @Schema(implementation = TicketDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Ticket already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<TicketDto> createTicket(
            @Valid @RequestBody TicketCreateDto createDto) {

        log.info("REST request to create ticket");

        TicketDto created = ticketService.createTicket(createDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }
 //    ────────────────── UPDATE ──────────────────
 @Operation(summary = "Update ticket")
 @ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Ticket updated successfully",
                 content = @Content(schema = @Schema(implementation = TicketDto.class))),
         @ApiResponse(responseCode = "404", description = "Ticket not found"),
         @ApiResponse(responseCode = "400", description = "Invalid request"),
         @ApiResponse(responseCode = "401", description = "Unauthorized")
 })
 @PutMapping("/{id}")
 public ResponseEntity<TicketDto> updateTicket(
         @Parameter(description = "Ticket id")
         @PathVariable Long id,

         @Valid
         @RequestBody TicketUpdateDto updateDto) {

     log.info("REST request to update ticket {}", id);

     TicketDto updated = ticketService.updateTicket(id, updateDto);

     return ResponseEntity.ok(updated);
 }
    // ────────────────── DELETE ──────────────────
    @Operation(summary = "Delete a ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(
            @Parameter(description = "ID of the ticket to delete", required = true) @PathVariable Long id) {
        log.info("REST request to delete ticket {}", id);
        ticketService.deleteByTicketId(id);
        return ResponseEntity.noContent().build();
    }

    // ────────────────── FIND ONE ──────────────────
    @Operation(summary = "Get a ticket by ID", description = "Returns a single ticket without eager loaded relations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket found",
                    content = @Content(schema = @Schema(implementation = TicketDto.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(
            @Parameter(description = "ID of the ticket", required = true) @PathVariable Long id) {
        log.debug("REST request to get ticket by id {}", id);
        Optional<TicketDto> ticket = ticketService.findDtoByIdOptional(id);
        return ticket.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ────────────────── FIND ALL (simple) ──────────────────
    @Operation(summary = "Get all tickets", description = "Returns a list of all tickets without any eager loaded relations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of tickets"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<TicketDto>> getAllTickets() {
        log.debug("REST request to get all tickets");
        List<TicketDto> tickets = ticketService.findAllDtos();
        return ResponseEntity.ok(tickets);
    }

    // ────────────────── FIND BY SIMPLE CRITERIA ──────────────────
    @Operation(summary = "Find a ticket by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket found"),
            @ApiResponse(responseCode = "404", description = "No ticket with that title"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search/title")
    public ResponseEntity<TicketDto> findByTitle(
            @Parameter(description = "Exact title of the ticket", required = true) @RequestParam String title) {
        log.debug("REST request to find ticket by title '{}'", title);
        Optional<TicketDto> ticket = ticketService.findByTitle(title);
        return ticket.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Find a ticket by priority")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket found"),
            @ApiResponse(responseCode = "404", description = "No ticket with that priority"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search/priority")
    public ResponseEntity<List<TicketDto>> findByPriority(
            @Parameter(description = "Priority value (e.g. HIGH, MEDIUM, LOW)", required = true) @RequestParam Priority priority) {
        log.debug("REST request to find ticket by priority {}", priority);
        List<TicketDto> tickets = ticketService.findByPriority(priority);
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Find a ticket by request status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket found"),
            @ApiResponse(responseCode = "404", description = "No ticket with that status"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search/status")
    public ResponseEntity<List<TicketDto>> findByRequestStatus(
            @Parameter(description = "Request status value", required = true) @RequestParam RequestStatus requestStatus) {
        log.debug("REST request to find ticket by status {}", requestStatus);
        List<TicketDto> tickets = ticketService.findByRequestStatus(requestStatus);
        return ResponseEntity.ok( tickets);
    }

    // ────────────────── FIND WITH EAGER RELATIONS ──────────────────
    @Operation(summary = "Get a ticket with all details", description = "Eager loads department, agent, customer request, attachments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket with all details"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}/details")
    public ResponseEntity<TicketDto> getTicketWithAllDetails(@PathVariable Long id) {
        log.debug("REST request to get ticket {} with all details", id);
        Optional<TicketDto> ticket = ticketService.findByIdWithAllDetails(id);
        return ticket.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get a ticket with attachments", description = " loads attachments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket with attachments"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}/attachments")
    public ResponseEntity<TicketDto> getTicketWithAttachments(@PathVariable Long id) {
        log.debug("REST request to get ticket {} with attachments", id);
        Optional<TicketDto> ticket = ticketService.findByIdWithAttachments(id);
        return ticket.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get tickets by ID with their messages", description = "Returns a set of tickets containing their messages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of tickets with messages"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}/messages")
    public ResponseEntity<TicketDto> getTicketsWithMessages(@PathVariable Long id) {
        log.debug("REST request to get tickets with messages for id {}", id);
        Optional<TicketDto> tickets = ticketService.findByIdWithMessages(id);
        return tickets.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get a ticket with its ticket histories", description = "Returns the ticket that contains ticket histories for the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket with ticket histories"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}/ticketWithHistories")
    public ResponseEntity<TicketDto> getTicketWithHistories(@PathVariable Long id) {
        log.debug("REST request to get ticket {} with histories", id);
        Optional<TicketDto> ticket = ticketService.findByIdWithTicketHistories(id);
        return ticket.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all tickets with department and agent", description = "Returns a list of tickets loading department and agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of tickets with department and agent"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/with/department/agent")
    public ResponseEntity<List<TicketDto>> getAllWithDepartmentAndAgent() {
        log.debug("REST request to get all tickets with department and agent");
        List<TicketDto> tickets = ticketService.findAllWithDepartmentWithAgents();
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Get a ticket by department ID with SLA", description = "Returns the ticket belonging to the given department")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket found"),
            @ApiResponse(responseCode = "404", description = "Ticket not found for that department"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/department/{depId}/sla")
    public ResponseEntity<List<TicketDto>> getTicketByDepartmentWithSla(
            @Parameter(description = "Department ID", required = true) @PathVariable Long depId) {
        log.debug("REST request to get ticket for department {} with SLA", depId);
        List<TicketDto> ticket = ticketService.findAllWithDepartmentIdWithSla(depId);
        return ResponseEntity.ok(ticket);
    }
    @Operation(
            summary = "Get ticket by customer request",
            description = "Returns the ticket associated with the given customer request"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket found"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/customer-request/{requestId}")
    public ResponseEntity<TicketDto> getTicketByCustomerRequest(
            @PathVariable Long requestId
    ) {

        log.debug("REST request to get ticket by customer request {}", requestId);

        Optional<TicketDto> ticket = ticketService.findByCustomerRequest(requestId);

        return ticket.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}