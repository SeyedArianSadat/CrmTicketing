package com.company.crmticketing.controller.rest;

import com.company.crmticketing.dto.sla.SlaCreateDto;
import com.company.crmticketing.dto.sla.SlaDto;
import com.company.crmticketing.dto.sla.SlaUpdateDto;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.service.SlaService;
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
@RequestMapping("/api/v1/slas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SLA Management", description = "Endpoints for managing Service Level Agreements (SLAs)")
@SecurityRequirement(name = "bearerAuth")
public class SlaController {

    private final SlaService slaService;

    // ────────────────── CREATE ──────────────────
    @Operation(summary = "Create a new SLA", description = "Creates a new Service Level Agreement record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SLA created successfully",
                    content = @Content(schema = @Schema(implementation = SlaDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<SlaDto> createSla(@Valid @RequestBody SlaCreateDto createDto) {
        log.info("REST request to create SLA: {}", createDto);
        SlaDto created = slaService.createSla(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ────────────────── UPDATE ──────────────────
    @Operation(summary = "Update an existing SLA", description = "Updates an SLA. The ID must be provided in the request body (and path for consistency).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SLA updated successfully",
                    content = @Content(schema = @Schema(implementation = SlaDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., ID missing)"),
            @ApiResponse(responseCode = "404", description = "SLA not found (if applicable)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{slaId}")
    public ResponseEntity<SlaDto> updateSla(
            @Parameter(description = "ID of the SLA to update (must also be present in the request body)", required = true) @PathVariable Long slaId,
            @Valid @RequestBody SlaUpdateDto updateDto) {

        log.info("REST request to update SLA {}: {}", slaId, updateDto);
        SlaDto updated = slaService.updateSla(slaId, updateDto);
        return ResponseEntity.ok(updated);
    }

    // ────────────────── DELETE ──────────────────
    @Operation(summary = "Delete an SLA by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "SLA deleted successfully"),
            @ApiResponse(responseCode = "404", description = "SLA not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{slaId}")
    public ResponseEntity<Void> deleteSla(
            @Parameter(description = "ID of the SLA to delete", required = true) @PathVariable Long slaId) {
        log.info("REST request to delete SLA {}", slaId);
        slaService.deleteBySlaId(slaId);
        return ResponseEntity.noContent().build();
    }

    // ────────────────── FIND ALL (simple) ──────────────────
    @Operation(summary = "Get all slas", description = "Returns a list of all slas without any eager loaded relations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of slas"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<SlaDto>> getAllSlas() {
        log.debug("REST request to get all slas");
        List<SlaDto> slas = slaService.findAllDtos();
        return ResponseEntity.ok(slas);
    }


    // ────────────────── FIND BY PRIORITY ──────────────────
    @Operation(summary = "Find an SLA by priority level")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SLA found",
                    content = @Content(schema = @Schema(implementation = SlaDto.class))),
            @ApiResponse(responseCode = "404", description = "No SLA defined for that priority"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search/by-priority")
    public ResponseEntity<SlaDto> findByPriorityLevel(
            @Parameter(description = "Priority level (e.g., HIGH, MEDIUM, LOW)", required = true) @RequestParam Priority priority) {
        log.debug("REST request to find SLA by priority: {}", priority);
        Optional<SlaDto> sla = slaService.findByPriorityLevel(priority);
        return sla.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}