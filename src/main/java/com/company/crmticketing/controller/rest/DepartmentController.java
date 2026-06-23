package com.company.crmticketing.controller.rest;

import com.company.crmticketing.dto.department.DepartmentDto;
import com.company.crmticketing.service.DepartmentService;
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
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Department Management", description = "Endpoints for managing departments")
@SecurityRequirement(name = "bearerAuth")
public class DepartmentController {

    private final DepartmentService departmentService;

    // ────────────────── CREATE ──────────────────
    @Operation(summary = "Create a new department", description = "Creates a new department record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Department created successfully",
                    content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        log.info("REST request to create department: {}", departmentDto);
        DepartmentDto created = departmentService.createDepartment(departmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ────────────────── UPDATE ──────────────────
    @Operation(summary = "Update an existing department", description = "Updates a department by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department updated successfully",
                    content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{departmentId}")
    public ResponseEntity<DepartmentDto> updateDepartment(
            @Parameter(description = "ID of the department to update", required = true) @PathVariable Long departmentId,
            @Valid @RequestBody DepartmentDto departmentDto) {
        departmentDto.setDepartmentId(departmentId);
        log.info("REST request to update department {}: {}", departmentId, departmentDto);
        DepartmentDto updated = departmentService.updateDepartment(departmentId, departmentDto);
        return ResponseEntity.ok(updated);
    }

    // ────────────────── DELETE ──────────────────
    @Operation(summary = "Delete a department by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Department deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(
            @Parameter(description = "ID of the department to delete", required = true) @PathVariable Long departmentId) {
        log.info("REST request to delete department {}", departmentId);
        departmentService.deleteDepartmentById(departmentId);
        return ResponseEntity.noContent().build();
    }
    // ────────────────── FIND ALL (simple) ──────────────────
    @Operation(summary = "Get all department", description = "Returns a list of all department without any eager loaded relations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of departments"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllSlas() {
        log.debug("REST request to get all slas");
        List<DepartmentDto> departments = departmentService.findAllDtos();
        return ResponseEntity.ok(departments);
    }

    // ────────────────── FIND BY NAME ──────────────────
    @Operation(summary = "Find a department by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department found",
                    content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "404", description = "No department with that name"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search/by-name")
    public ResponseEntity<DepartmentDto> findByDepartmentName(
            @Parameter(description = "Department name (exact match)", required = true) @RequestParam String departmentName) {
        log.debug("REST request to find department by name '{}'", departmentName);
        Optional<DepartmentDto> department = departmentService.findByDepartmentName(departmentName);
        return department.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ────────────────── FIND WITH SUPPORT AGENT ──────────────────
    @Operation(summary = "Get department by ID with support agents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department with agents",
                    content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}/agents")
    public ResponseEntity<DepartmentDto> findByIdWithSupportAgent(
            @Parameter(description = "Department ID", required = true) @PathVariable Long id) {
        log.debug("REST request to find department {} with agents", id);
        Optional<DepartmentDto> department = departmentService.findByIdWithSupportAgent(id);
        return department.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ────────────────── FIND WITH TICKETS ──────────────────
    @Operation(summary = "Get department by ID with tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department with tickets",
                    content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}/tickets")
    public ResponseEntity<DepartmentDto> findByIdWithTicket(
            @Parameter(description = "Department ID", required = true) @PathVariable Long id) {
        log.debug("REST request to find department {} with tickets", id);
        Optional<DepartmentDto> department = departmentService.findBtIdWithTicket(id);
        return department.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ────────────────── FIND WITH AGENTS AND TICKETS ──────────────────
    @Operation(summary = "Get department by ID with support agents and tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department with agents and tickets",
                    content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "404", description = "Department not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}/agents-tickets")
    public ResponseEntity<DepartmentDto> findByIdWithSupportAgentAndTicket(
            @Parameter(description = "Department ID", required = true) @PathVariable Long id) {
        log.debug("REST request to find department {} with agents and tickets", id);
        Optional<DepartmentDto> department = departmentService.findByIdWithSupportAgentAndTicket(id);
        return department.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}