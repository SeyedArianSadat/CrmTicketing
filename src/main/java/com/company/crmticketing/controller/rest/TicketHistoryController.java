package com.company.crmticketing.controller.rest;


import com.company.crmticketing.dto.Ticket.TicketDto;
import com.company.crmticketing.dto.TicketHistory.TicketHistoryDto;
import com.company.crmticketing.service.TicketHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/ticketHistories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "TicketHistory Management", description = "Endpoints for creating, retrieving, updating and deleting tickets")
@SecurityRequirement(name = "bearerAuth")
public class TicketHistoryController {

    private final TicketHistoryService ticketHistoryService;

    // ────────────────── CREATE ──────────────────
    @Operation(summary = "Create a new ticketHistory", description = "Creates a ticketHistory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "TicketHistory created successfully",
                    content = @Content(schema = @Schema(implementation = TicketHistoryDto.class))),
            @ApiResponse(responseCode = "409", description = "TicketHistory with the same title already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Bearer token missing or invalid")
    })
    @PostMapping
    public ResponseEntity<TicketHistoryDto> createTicketHistory(@Valid @RequestBody TicketHistoryDto ticketHistoryDto) {
        log.info("Rest request to createTicketHistory: {}", ticketHistoryDto);
        TicketHistoryDto createdTicketHistory = ticketHistoryService.createTicketHistory(ticketHistoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicketHistory);
    }

    // ────────────────── UPDATE ──────────────────
    @Operation(summary = "Update an existing ticketHistory", description = ("Partially updates a ticketHistory"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TicketHistory updated successfully",
                    content = @Content(schema = @Schema(implementation = TicketHistoryDto.class))),
            @ApiResponse(responseCode = "404", description = "TicketHistory not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping(",{id}")
    public ResponseEntity<TicketHistoryDto> updateTicketHistory(
            @Parameter(description = "ID of ticketHistory to update", required = true) @PathVariable Long id,
            @Valid @RequestBody TicketHistoryDto ticketHistoryDto) {
        log.info("Rest request to updateTicketHistory: {}", id, ticketHistoryDto);
        TicketHistoryDto updatedTicketHistory = ticketHistoryService.updateTicketHistory(id, ticketHistoryDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTicketHistory);
    }

    // ────────────────── DELETE ──────────────────
    @Operation(summary = "Delete a ticketHistory by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "TicketHistory deleted successfully"),
            @ApiResponse(responseCode = "404", description = "TicketHistory not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketHistory(
            @Parameter(description = "ID of the ticketHistory to delete", required = true) @PathVariable Long id) {
        log.info("Rest request to deleteTicketHistory: {}", id);
        ticketHistoryService.deleteByTicketHistoryId(id);
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
    public ResponseEntity<TicketHistoryDto> getTicketHistoryById(
            @Parameter(description = "ID of the ticketHistory", required = true) @PathVariable Long id) {
        log.debug("REST request to get ticketHistory by id {}", id);
        Optional<TicketHistoryDto> ticketHistory = ticketHistoryService.findDtoByIdOptional(id);
        return ticketHistory.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // ────────────────── FIND ALL (simple) ──────────────────
    @Operation(summary = "Get all ticketsHistory", description = "Returns a list of all ticketHistories without any eager loaded relations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of ticketHistories"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<TicketHistoryDto>> getAllTicketHistories() {
        log.debug("REST request to get all ticketsHistory");
        List<TicketHistoryDto> ticketHistories = ticketHistoryService.findAllDtos();
        return ResponseEntity.ok(ticketHistories);
    }

    // ────────────────── FIND BY SIMPLE CRITERIA ──────────────────
    @Operation(summary = "Find a ticket by its fieldChanged")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TicketHistory found"),
            @ApiResponse(responseCode = "404", description = "No ticketHistory with that title"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search/by-fieldChanged")
    public ResponseEntity<TicketHistoryDto> findByFieldChanged(
            @Parameter(description = "Exact FieldChanged of the ticket", required = true) @RequestParam String fieldChanged) {
        log.debug("REST request to find ticket by FieldChanged '{}'", fieldChanged);
        Optional<TicketHistoryDto> ticketHistory = ticketHistoryService.findByFieldChanged(fieldChanged);
        return ticketHistory.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ────────────────── FIND WITH EAGER RELATIONS ──────────────────
    @Operation(summary = "Get a ticketHistory with all details", description = "loads what needed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TicketHistory with all details"),
            @ApiResponse(responseCode = "404", description = "TicketHistory not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}/tickets")
    public ResponseEntity<TicketHistoryDto> getByIdWithTicket(@PathVariable Long id) {
        log.debug("REST request to get ticketHistory {} with all details", id);
        Optional<TicketHistoryDto> ticketHistory = ticketHistoryService.findByIdWithTicket(id);
        return ticketHistory.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all ticketHistory with ticket", description = "Returns a list of ticketHistory with its ticket ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of ticketHistories with ticket"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/with-tickets")
    public ResponseEntity<List<TicketHistoryDto>> getAllWithTicket() {
        log.debug("REST request to get all ticketHistories with its ticket");
        List<TicketHistoryDto> ticketHistories = ticketHistoryService.findAllWithTicket();
        return ResponseEntity.ok(ticketHistories);
    }
}