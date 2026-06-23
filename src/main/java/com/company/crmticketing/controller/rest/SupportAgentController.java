package com.company.crmticketing.controller.rest;

import com.company.crmticketing.dto.supportAgent.SupportAgentDto;
import com.company.crmticketing.exception.SupportAgentNotFoundException;
import com.company.crmticketing.service.SupportAgentService;
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
@RequestMapping("/api/v1/agents")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Support Agent Management", description = "Endpoints for managing support agents")
@SecurityRequirement(name = "bearerAuth")
public class SupportAgentController {

    private final SupportAgentService agentService;

    // ────────────────── CREATE ──────────────────
    @Operation(summary = "Create a new support agent", description = "Creates a new support agent from the given data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agent created successfully",
                    content = @Content(schema = @Schema(implementation = SupportAgentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Bearer token missing or invalid")
    })
    @PostMapping
    public ResponseEntity<SupportAgentDto> createAgent(@Valid @RequestBody SupportAgentDto agentDto) {
        log.info("REST request to create agent: {}", agentDto);
        SupportAgentDto created = agentService.createAgent(agentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ────────────────── UPDATE ──────────────────
    @Operation(summary = "Update an existing support agent", description = "Updates the agent identified by the given ID. The ID in the path overrides any ID in the request body.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent updated successfully",
                    content = @Content(schema = @Schema(implementation = SupportAgentDto.class))),
            @ApiResponse(responseCode = "404", description = "Agent not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{agentId}")
    public ResponseEntity<SupportAgentDto> updateAgent(
            @Parameter(description = "ID of the agent to update", required = true) @PathVariable Long agentId,
            @Valid @RequestBody SupportAgentDto agentDto) {
        log.info("REST request to update agent with id {}: {}", agentId, agentDto);
        agentDto.setAgentId(agentId);
        SupportAgentDto updated = agentService.updateAgent(agentId, agentDto);
        return ResponseEntity.ok(updated);
    }

    // ────────────────── DELETE ──────────────────
    @Operation(summary = "Delete a support agent by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Agent deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Agent not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{agentId}")
    public ResponseEntity<Void> deleteAgent(
            @Parameter(description = "ID of the agent to delete", required = true) @PathVariable Long agentId) {
        log.info("REST request to delete agent {}", agentId);
        agentService.deleteAgentById(agentId);
        return ResponseEntity.noContent().build();
    }

    // ────────────────── FIND ONE ──────────────────
    @Operation(summary = "Get a support agent by ID", description = "Returns a single agent by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent found",
                    content = @Content(schema = @Schema(implementation = SupportAgentDto.class))),
            @ApiResponse(responseCode = "404", description = "Agent not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{agentId}")
    public ResponseEntity<SupportAgentDto> getAgentById(
            @Parameter(description = "ID of the agent", required = true) @PathVariable Long agentId) {
        log.debug("REST request to get agent by id {}", agentId);
        Optional<SupportAgentDto> agent = agentService.findDtoByIdOptional(agentId);
        return agent.map(ResponseEntity::ok)
                .orElseThrow(() -> new SupportAgentNotFoundException(agentId));
    }

    // ────────────────── FIND ALL ──────────────────
    @Operation(summary = "Get all support agents", description = "Returns a list of all support agents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of agents"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<SupportAgentDto>> getAllAgents() {
        log.debug("REST request to get all agents");
        List<SupportAgentDto> agents = agentService.findAllDtos();
        return ResponseEntity.ok(agents);
    }

    // ────────────────── FIND BY NAME ──────────────────
    @Operation(summary = "Find an agent by exact name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent found"),
            @ApiResponse(responseCode = "404", description = "No agent with that name"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search/by-name")
    public ResponseEntity<SupportAgentDto> findAgentByName(
            @Parameter(description = "Exact name of the agent", required = true) @RequestParam String name) {
        log.debug("REST request to find agent by name '{}'", name);
        return agentService.findAgentByName(name)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found: " + name));
    }

    // ────────────────── EAGER LOADING ENDPOINTS ──────────────────

    @Operation(summary = "Get agent by ID with user details", description = "Eagerly loads the user associated with the agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent with user"),
            @ApiResponse(responseCode = "404", description = "Agent not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{agentId}/with-user")
    public ResponseEntity<SupportAgentDto> getAgentWithUser(@PathVariable Long agentId) {
        log.debug("REST request to get agent {} with user", agentId);
        Optional<SupportAgentDto> agent = agentService.findByIdWithUser(agentId);
        return agent.map(ResponseEntity::ok)
                .orElseThrow(() -> new SupportAgentNotFoundException(agentId));
    }

    @Operation(summary = "Get agent by ID with department", description = "Eagerly loads the department associated with the agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent with department"),
            @ApiResponse(responseCode = "404", description = "Agent not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{agentId}/with-department")
    public ResponseEntity<SupportAgentDto> getAgentWithDepartment(@PathVariable Long agentId) {
        log.debug("REST request to get agent {} with department", agentId);
        Optional<SupportAgentDto> agent = agentService.findByIdWithDepartment(agentId);
        return agent.map(ResponseEntity::ok)
                .orElseThrow(() -> new SupportAgentNotFoundException(agentId));
    }

    @Operation(summary = "Get agent by ID with user and assigned tickets", description = "Eagerly loads user and assigned tickets for the agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent with user and assigned tickets"),
            @ApiResponse(responseCode = "404", description = "Agent not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{agentId}/with-user-and-tickets")
    public ResponseEntity<SupportAgentDto> getAgentWithUserAndTickets(@PathVariable Long agentId) {
        log.debug("REST request to get agent {} with user and assigned tickets", agentId);
        Optional<SupportAgentDto> agent = agentService.findByIdWithUserWithAssignedTicket(agentId);
        return agent.map(ResponseEntity::ok)
                .orElseThrow(() -> new SupportAgentNotFoundException(agentId));
    }

    @Operation(summary = "Get all agents (by ID) with department and user", description = "Returns a list (usually one) of agents with department and user loaded. The ID parameter is the agent ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of agents with department and user"),
            @ApiResponse(responseCode = "404", description = "No agents found for that ID"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{agentId}/full-details")
    public ResponseEntity<List<SupportAgentDto>> getAgentFullDetails(@PathVariable Long agentId) {
        log.debug("REST request to get agent {} with department and user (list)", agentId);
        List<SupportAgentDto> agents = agentService.findAllAgentByIdWithDepartmentAndUser(agentId);
        if (agents.isEmpty()) {
            throw new SupportAgentNotFoundException(agentId);
        }
        return ResponseEntity.ok(agents);
    }

    @Operation(summary = "Get all assigned tickets for an agent", description = "Returns a list of agents (usually one) with their assigned tickets loaded")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of agents with assigned tickets"),
            @ApiResponse(responseCode = "404", description = "No tickets found for that agent"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{agentId}/assigned-tickets")
    public ResponseEntity<List<SupportAgentDto>> getAgentsWithAssignedTickets(@PathVariable Long agentId) {
        log.debug("REST request to get agents with assigned tickets for id {}", agentId);
        List<SupportAgentDto> agents = agentService.findAllByAssignedTickets(agentId);
        if (agents.isEmpty()) {
            throw new SupportAgentNotFoundException(agentId);
        }
        return ResponseEntity.ok(agents);
    }
}
