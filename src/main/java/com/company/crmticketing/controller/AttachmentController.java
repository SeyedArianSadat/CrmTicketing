package com.company.crmticketing.controller;

import com.company.crmticketing.dto.Attachment.AttachmentDto;
import com.company.crmticketing.service.AttachmentService;
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
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Attachment Management", description = "Endpoints for managing file attachments")
@SecurityRequirement(name = "bearerAuth")
public class AttachmentController {

    private final AttachmentService attachmentService;

    // ────────────────── CREATE ──────────────────
    @Operation(summary = "Create a new attachment", description = "Uploads/creates a new attachment record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Attachment created successfully",
                    content = @Content(schema = @Schema(implementation = AttachmentDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<AttachmentDto> createAttachment(@Valid @RequestBody AttachmentDto attachmentDto) {
        log.info("REST request to create attachment: {}", attachmentDto);
        AttachmentDto created = attachmentService.createAttachment(attachmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ────────────────── UPDATE ──────────────────
    @Operation(summary = "Update an existing attachment", description = "Updates the metadata of an attachment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment updated successfully",
                    content = @Content(schema = @Schema(implementation = AttachmentDto.class))),
            @ApiResponse(responseCode = "404", description = "Attachment not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{attachmentId}")
    public ResponseEntity<AttachmentDto> updateAttachment(
            @Parameter(description = "ID of the attachment to update", required = true) @PathVariable Long attachmentId,
            @Valid @RequestBody AttachmentDto attachmentDto) {
        attachmentDto.setAttachmentId(attachmentId);
        log.info("REST request to update attachment {}: {}", attachmentId, attachmentDto);
        AttachmentDto updated = attachmentService.updateAttachment(attachmentId, attachmentDto);
        return ResponseEntity.ok(updated);
    }

    // ────────────────── DELETE ──────────────────
    @Operation(summary = "Delete an attachment by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Attachment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Attachment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(
            @Parameter(description = "ID of the attachment to delete", required = true) @PathVariable Long attachmentId) {
        log.info("REST request to delete attachment {}", attachmentId);
        attachmentService.deleteByAttachmentId(attachmentId);
        return ResponseEntity.noContent().build();
    }

    // ────────────────── FIND BY FILE NAME ──────────────────
    @Operation(summary = "Find an attachment by file name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment found",
                    content = @Content(schema = @Schema(implementation = AttachmentDto.class))),
            @ApiResponse(responseCode = "404", description = "No attachment with that file name"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search/by-filename")
    public ResponseEntity<AttachmentDto> findByFileName(
            @Parameter(description = "Exact file name to search for", required = true) @RequestParam String fileName) {
        log.debug("REST request to find attachment by filename '{}'", fileName);
        Optional<AttachmentDto> attachment = attachmentService.findAttachmentByFileName(fileName);
        return attachment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ────────────────── FIND BY ID WITH TICKET ──────────────────
    @Operation(summary = "Get attachment by ID with its associated ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment with ticket details",
                    content = @Content(schema = @Schema(implementation = AttachmentDto.class))),
            @ApiResponse(responseCode = "404", description = "Attachment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{attachmentId}/ticket")
    public ResponseEntity<AttachmentDto> findByIdWithTicket(
            @Parameter(description = "Attachment ID", required = true) @PathVariable Long attachmentId) {
        log.debug("REST request to get attachment {} with ticket", attachmentId);
        Optional<AttachmentDto> attachment = attachmentService.findByIdWithTicket(attachmentId);
        return attachment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ────────────────── FIND ALL WITH TICKET ──────────────────
    @Operation(summary = "Get all attachments with their ticket details", description = "Returns a list of all attachments eagerly loading their related ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of attachments with tickets"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/with-ticket")
    public ResponseEntity<List<AttachmentDto>> findAllWithTicket() {
        log.debug("REST request to get all attachments with ticket");
        List<AttachmentDto> attachments = attachmentService.findAllWithTicket();
        return ResponseEntity.ok(attachments);
    }

    // ────────────────── FIND BY TICKET ID WITH TICKET ──────────────────
    @Operation(summary = "Find attachments by ticket ID", description = "Returns an attachment associated with the given ticket ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment found",
                    content = @Content(schema = @Schema(implementation = AttachmentDto.class))),
            @ApiResponse(responseCode = "404", description = "No attachment found for that ticket"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/by-ticket/{ticketId}")
    public ResponseEntity<AttachmentDto> findByTicketIdWithTicket(
            @Parameter(description = "Ticket ID", required = true) @PathVariable Long ticketId) {
        log.debug("REST request to find attachment for ticket id {}", ticketId);
        Optional<AttachmentDto> attachment = attachmentService.findByTicketIdWithTicket(ticketId);
        return attachment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
