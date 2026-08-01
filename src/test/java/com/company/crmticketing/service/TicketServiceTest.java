package com.company.crmticketing.service;

import com.company.crmticketing.dto.ticket.TicketCreateDto;
import com.company.crmticketing.dto.ticket.TicketDto;
import com.company.crmticketing.dto.ticket.TicketUpdateDto;
import com.company.crmticketing.exception.CustomerRequestNotFoundException;
import com.company.crmticketing.exception.DepartmentNotFoundException;
import com.company.crmticketing.exception.TicketAlreadyExistException;
import com.company.crmticketing.exception.TicketNotFoundException;
import com.company.crmticketing.mapper.TicketMapper;
import com.company.crmticketing.model.CustomerRequest;
import com.company.crmticketing.model.Department;
import com.company.crmticketing.model.Sla;
import com.company.crmticketing.model.SupportAgent;
import com.company.crmticketing.model.Ticket;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.repository.CustomerRequestRepository;
import com.company.crmticketing.repository.DepartmentRepository;
import com.company.crmticketing.repository.SlaRepository;
import com.company.crmticketing.repository.SupportAgentRepository;
import com.company.crmticketing.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private CustomerRequestRepository customerRequestRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private SupportAgentRepository supportAgentRepository;

    @Mock
    private SlaRepository slaRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void createTicketRejectsDuplicateTitle() {
        TicketCreateDto createDto = createDto("Duplicate", null, null, null, null);
        when(ticketRepository.existsByTitle("Duplicate")).thenReturn(true);

        assertThatThrownBy(() -> ticketService.createTicket(createDto))
                .isInstanceOf(TicketAlreadyExistException.class);

        verify(ticketRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void createTicketAppliesRelationsLinksCustomerRequestAndReturnsDto() {
        TicketCreateDto createDto = createDto("Broken printer", 100L, 10L, 20L, 30L);
        Ticket ticket = ticket("Broken printer");
        TicketDto dto = ticketDto(5L, "Broken printer");
        CustomerRequest request = new CustomerRequest();
        Department department = new Department();
        SupportAgent agent = new SupportAgent();
        Sla sla = new Sla();

        when(ticketRepository.existsByTitle("Broken printer")).thenReturn(false);
        when(ticketMapper.toEntity(createDto)).thenReturn(ticket);
        when(departmentRepository.findActiveById(10L)).thenReturn(Optional.of(department));
        when(supportAgentRepository.findActiveById(20L)).thenReturn(Optional.of(agent));
        when(slaRepository.findActiveById(30L)).thenReturn(Optional.of(sla));
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(customerRequestRepository.findActiveById(100L)).thenReturn(Optional.of(request));
        when(ticketMapper.toDto(ticket)).thenReturn(dto);

        TicketDto result = ticketService.createTicket(createDto);

        assertThat(result).isSameAs(dto);
        assertThat(ticket.getDepartment()).isSameAs(department);
        assertThat(ticket.getAgent()).isSameAs(agent);
        assertThat(ticket.getSla()).isSameAs(sla);
        assertThat(ticket.getCustomerRequest()).isSameAs(request);
        assertThat(request.getTicket()).isSameAs(ticket);
        verify(customerRequestRepository).save(request);
    }

    @Test
    void createTicketThrowsWhenRelatedDepartmentIsMissing() {
        TicketCreateDto createDto = createDto("Broken printer", null, 404L, null, null);
        Ticket ticket = ticket("Broken printer");

        when(ticketRepository.existsByTitle("Broken printer")).thenReturn(false);
        when(ticketMapper.toEntity(createDto)).thenReturn(ticket);
        when(departmentRepository.findActiveById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.createTicket(createDto))
                .isInstanceOf(DepartmentNotFoundException.class);

        verify(ticketRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void updateTicketAppliesDtoRelationsAndReturnsMappedResult() {
        Ticket existing = ticket("Old");
        TicketUpdateDto updateDto = new TicketUpdateDto(
                "New",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusHours(4),
                Priority.HIGH,
                RequestStatus.IN_PROGRESS,
                100L,
                10L,
                null,
                null
        );
        CustomerRequest request = new CustomerRequest();
        Department department = new Department();
        TicketDto response = ticketDto(9L, "New");

        when(ticketRepository.findById(9L)).thenReturn(Optional.of(existing));
        when(departmentRepository.findActiveById(10L)).thenReturn(Optional.of(department));
        when(customerRequestRepository.findActiveById(100L)).thenReturn(Optional.of(request));
        when(ticketRepository.save(existing)).thenReturn(existing);
        when(ticketMapper.toDto(existing)).thenReturn(response);

        TicketDto result = ticketService.updateTicket(9L, updateDto);

        assertThat(result).isSameAs(response);
        assertThat(existing.getDepartment()).isSameAs(department);
        assertThat(existing.getCustomerRequest()).isSameAs(request);
        assertThat(request.getTicket()).isSameAs(existing);
        verify(ticketMapper).updateTicketFromDto(updateDto, existing);
    }

    @Test
    void updateTicketThrowsWhenTicketDoesNotExist() {
        TicketUpdateDto updateDto = new TicketUpdateDto(
                "New", null, null, null, null, null, null, null, null
        );
        when(ticketRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.updateTicket(9L, updateDto))
                .isInstanceOf(TicketNotFoundException.class);
    }

    @Test
    void updateTicketThrowsWhenRelatedCustomerRequestIsMissing() {
        Ticket existing = ticket("Old");
        TicketUpdateDto updateDto = new TicketUpdateDto(
                "New", null, null, null, null, 404L, null, null, null
        );
        when(ticketRepository.findById(9L)).thenReturn(Optional.of(existing));
        when(customerRequestRepository.findActiveById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.updateTicket(9L, updateDto))
                .isInstanceOf(CustomerRequestNotFoundException.class);
    }

    @Test
    void findByPriorityMapsRepositoryResults() {
        List<Ticket> tickets = List.of(ticket("One"), ticket("Two"));
        List<TicketDto> dtos = List.of(ticketDto(1L, "One"), ticketDto(2L, "Two"));
        when(ticketRepository.findByPriority(Priority.HIGH)).thenReturn(tickets);
        when(ticketMapper.toTicketDtoList(tickets)).thenReturn(dtos);

        List<TicketDto> result = ticketService.findByPriority(Priority.HIGH);

        assertThat(result).isSameAs(dtos);
    }

    @Test
    void deleteByTicketIdSoftDeletesActiveTicket() {
        Ticket existing = ticket("Old");
        when(ticketRepository.findActiveById(4L)).thenReturn(Optional.of(existing));
        when(ticketRepository.softDeleteWithRetry(4L, 3)).thenReturn(true);

        ticketService.deleteByTicketId(4L);

        verify(ticketRepository).softDeleteWithRetry(4L, 3);
    }

    private static TicketCreateDto createDto(
            String title,
            Long customerRequestId,
            Long departmentId,
            Long agentId,
            Long slaId
    ) {
        return new TicketCreateDto(
                title,
                Priority.HIGH,
                RequestStatus.OPEN,
                LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusDays(1),
                customerRequestId,
                departmentId,
                agentId,
                slaId
        );
    }

    private static Ticket ticket(String title) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setPriority(Priority.HIGH);
        ticket.setRequestStatus(RequestStatus.OPEN);
        return ticket;
    }

    private static TicketDto ticketDto(Long id, String title) {
        TicketDto dto = new TicketDto();
        dto.setTicketId(id);
        dto.setTitle(title);
        return dto;
    }
}
