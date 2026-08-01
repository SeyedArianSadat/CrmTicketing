package com.company.crmticketing.controller.rest;

import com.company.crmticketing.dto.ticket.TicketCreateDto;
import com.company.crmticketing.dto.ticket.TicketDto;
import com.company.crmticketing.dto.ticket.TicketUpdateDto;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TicketControllerTest {

    private final TicketService ticketService = mock();
    private final TicketController controller = new TicketController(ticketService);

    @Test
    void createTicketReturnsCreatedTicketWithCreatedStatus() {
        TicketCreateDto createDto = createDto("Broken printer");
        TicketDto response = ticketDto(1L, "Broken printer");
        when(ticketService.createTicket(createDto)).thenReturn(response);

        ResponseEntity<TicketDto> result = controller.createTicket(createDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    void updateTicketReturnsUpdatedTicket() {
        TicketUpdateDto updateDto = new TicketUpdateDto(
                "Updated", null, null, Priority.MEDIUM, RequestStatus.IN_PROGRESS,
                null, null, null, null
        );
        TicketDto response = ticketDto(2L, "Updated");
        when(ticketService.updateTicket(2L, updateDto)).thenReturn(response);

        ResponseEntity<TicketDto> result = controller.updateTicket(2L, updateDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    void deleteTicketDelegatesAndReturnsNoContent() {
        ResponseEntity<Void> result = controller.deleteTicket(2L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(ticketService).deleteByTicketId(2L);
    }

    @Test
    void getTicketByIdReturnsOkWhenTicketExists() {
        TicketDto response = ticketDto(3L, "Found");
        when(ticketService.findDtoByIdOptional(3L)).thenReturn(Optional.of(response));

        ResponseEntity<TicketDto> result = controller.getTicketById(3L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    void getTicketByIdReturnsNotFoundWhenMissing() {
        when(ticketService.findDtoByIdOptional(3L)).thenReturn(Optional.empty());

        ResponseEntity<TicketDto> result = controller.getTicketById(3L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findByTitleReturnsNotFoundWhenMissing() {
        when(ticketService.findByTitle("Missing")).thenReturn(Optional.empty());

        ResponseEntity<TicketDto> result = controller.findByTitle("Missing");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findByPriorityReturnsTickets() {
        List<TicketDto> tickets = List.of(ticketDto(1L, "One"));
        when(ticketService.findByPriority(Priority.HIGH)).thenReturn(tickets);

        ResponseEntity<List<TicketDto>> result = controller.findByPriority(Priority.HIGH);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(tickets);
    }

    @Test
    void getTicketByCustomerRequestReturnsOkWhenFound() {
        TicketDto ticket = ticketDto(7L, "Request ticket");
        when(ticketService.findByCustomerRequest(9L)).thenReturn(Optional.of(ticket));

        ResponseEntity<TicketDto> result = controller.getTicketByCustomerRequest(9L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isSameAs(ticket);
    }

    private static TicketCreateDto createDto(String title) {
        return new TicketCreateDto(
                title,
                Priority.HIGH,
                RequestStatus.OPEN,
                LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusDays(1),
                null,
                null,
                null,
                null
        );
    }

    private static TicketDto ticketDto(Long id, String title) {
        TicketDto dto = new TicketDto();
        dto.setTicketId(id);
        dto.setTitle(title);
        return dto;
    }
}
