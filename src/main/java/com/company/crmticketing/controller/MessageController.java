package com.company.crmticketing.controller;

import com.company.crmticketing.dto.Message.MessageDto;
import com.company.crmticketing.service.MessageService;
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
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Message Management", description = "Endpoints for creating, updating, deleting and retrieving messages")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final MessageService messageService;

    // ────────────────── CREATE ──────────────────
    @Operation(summary = "Create a new message", description = "Creates a new message entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message created successfully",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – Bearer token missing or invalid")
    })
    @PostMapping
    public ResponseEntity<MessageDto> createMessage(@Valid @RequestBody MessageDto messageDto) {
        log.info("REST request to create message: {}", messageDto);
        MessageDto created = messageService.createMessage(messageDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ────────────────── UPDATE ──────────────────
    @Operation(summary = "Update an existing message", description = "Updates a message. The ID must be present in the DTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message updated successfully",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "Message not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input (ID missing or invalid)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MessageDto> updateMessage(
            @Parameter(description = "ID of the message to update (also must be set in request body)", required = true) @PathVariable Long id,
            @Valid @RequestBody MessageDto messageDto) {
        // Ensure the path id matches the DTO id to avoid inconsistencies
        messageDto.setMessageId(id);
        log.info("REST request to update message {}: {}", id, messageDto);
        MessageDto updated = messageService.updateMessage(id, messageDto);
        return ResponseEntity.ok(updated);
    }

    // ────────────────── DELETE ──────────────────
    @Operation(summary = "Delete a message by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Message deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Message not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "ID of the message to delete", required = true) @PathVariable Long id) {
        log.info("REST request to delete message {}", id);
        messageService.deleteMessageById(id);
        return ResponseEntity.noContent().build();
    }

    // ────────────────── FIND BY CONTENT ──────────────────
    @Operation(summary = "Find a message by its content")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message found",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "No message with that content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search/by-content")
    public ResponseEntity<MessageDto> findByContent(
            @Parameter(description = "Content of the message to search", required = true) @RequestParam String content) {
        log.debug("REST request to find message by content '{}'", content);
        Optional<MessageDto> message = messageService.findByContent(content);
        return message.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //
//    // ────────────────── FIND BY USER ID ──────────────────
    @Operation(summary = "Find a message by user ID with user details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message found",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "No message associated with that user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<MessageDto> findByIdWithUser(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        log.debug("REST request to find message by user id {}", userId);
        Optional<MessageDto> message = messageService.findByIdWithUser(userId);
        return message.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //    // ────────────────── FIND BY TICKET ID ──────────────────
    @Operation(summary = "Find a message by ticket ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message found",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "No message associated with that ticket"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/by-ticket/{ticketId}")
    public ResponseEntity<MessageDto> findByIdWithTicket(
            @Parameter(description = "Ticket ID", required = true) @PathVariable Long ticketId) {
        log.debug("REST request to find message by ticket id {}", ticketId);
        Optional<MessageDto> message = messageService.findByIdWithTicket(ticketId);
        return message.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //    // ────────────────── FIND BY TICKET ID WITH SENDER ──────────────────
    @Operation(summary = "Find a message by ticket ID including sender details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message found with sender",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "No message associated with that ticket"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/by-ticket/{ticketId}/with-sender")
    public ResponseEntity<MessageDto> findByIdWithTicketAndSender(
            @Parameter(description = "Ticket ID", required = true) @PathVariable Long ticketId) {
        log.debug("REST request to find message by ticket id {} with sender", ticketId);
        Optional<MessageDto> message = messageService.findByIdWithTicketAndSender(ticketId);
        return message.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}