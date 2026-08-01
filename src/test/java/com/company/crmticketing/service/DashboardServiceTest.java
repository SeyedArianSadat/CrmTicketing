package com.company.crmticketing.service;

import com.company.crmticketing.dto.dashboard.DashboardDto;
import com.company.crmticketing.dto.ticket.TicketDto;
import com.company.crmticketing.mapper.TicketMapper;
import com.company.crmticketing.model.Ticket;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.repository.AttachmentRepository;
import com.company.crmticketing.repository.CustomerRepository;
import com.company.crmticketing.repository.CustomerRequestRepository;
import com.company.crmticketing.repository.DepartmentRepository;
import com.company.crmticketing.repository.MessageRepository;
import com.company.crmticketing.repository.SlaRepository;
import com.company.crmticketing.repository.SupportAgentRepository;
import com.company.crmticketing.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DashboardServiceTest {

    private final CustomerRepository customerRepository = mock();
    private final TicketRepository ticketRepository = mock();
    private final DepartmentRepository departmentRepository = mock();
    private final SupportAgentRepository supportAgentRepository = mock();
    private final CustomerRequestRepository customerRequestRepository = mock();
    private final MessageRepository messageRepository = mock();
    private final AttachmentRepository attachmentRepository = mock();
    private final SlaRepository slaRepository = mock();
    private final TicketMapper ticketMapper = mock();

    private final DashboardService dashboardService = new DashboardService(
            customerRepository,
            ticketRepository,
            departmentRepository,
            supportAgentRepository,
            customerRequestRepository,
            messageRepository,
            attachmentRepository,
            slaRepository,
            ticketMapper
    );

    @Test
    void getDashboardCollectsCountsFromRepositories() {
        when(customerRepository.count()).thenReturn(1L);
        when(ticketRepository.count()).thenReturn(2L);
        when(departmentRepository.count()).thenReturn(3L);
        when(supportAgentRepository.count()).thenReturn(4L);
        when(customerRequestRepository.count()).thenReturn(5L);
        when(messageRepository.count()).thenReturn(6L);
        when(attachmentRepository.count()).thenReturn(7L);
        when(slaRepository.count()).thenReturn(8L);
        when(ticketRepository.countByRequestStatus(RequestStatus.OPEN)).thenReturn(9L);
        when(ticketRepository.countByRequestStatus(RequestStatus.IN_PROGRESS)).thenReturn(10L);
        when(ticketRepository.countByRequestStatus(RequestStatus.CLOSED)).thenReturn(11L);

        DashboardDto dashboard = dashboardService.getDashboard();

        assertThat(dashboard.getCustomers()).isEqualTo(1L);
        assertThat(dashboard.getTickets()).isEqualTo(2L);
        assertThat(dashboard.getDepartments()).isEqualTo(3L);
        assertThat(dashboard.getAgents()).isEqualTo(4L);
        assertThat(dashboard.getCustomerRequests()).isEqualTo(5L);
        assertThat(dashboard.getMessages()).isEqualTo(6L);
        assertThat(dashboard.getAttachments()).isEqualTo(7L);
        assertThat(dashboard.getSlas()).isEqualTo(8L);
        assertThat(dashboard.getOpenTickets()).isEqualTo(9L);
        assertThat(dashboard.getInProgressTickets()).isEqualTo(10L);
        assertThat(dashboard.getClosedTickets()).isEqualTo(11L);
    }

    @Test
    void getLatestTicketsFetchesTopFiveAndMapsToDtos() {
        Ticket first = new Ticket();
        Ticket second = new Ticket();
        TicketDto firstDto = ticketDto(1L);
        TicketDto secondDto = ticketDto(2L);

        when(ticketRepository.findTop5ByOrderByCreatedAtDesc(PageRequest.of(0, 5)))
                .thenReturn(List.of(first, second));
        when(ticketMapper.toDto(first)).thenReturn(firstDto);
        when(ticketMapper.toDto(second)).thenReturn(secondDto);

        List<TicketDto> result = dashboardService.getLatestTickets();

        assertThat(result).containsExactly(firstDto, secondDto);
        verify(ticketRepository).findTop5ByOrderByCreatedAtDesc(PageRequest.of(0, 5));
    }

    private static TicketDto ticketDto(Long id) {
        TicketDto dto = new TicketDto();
        dto.setTicketId(id);
        return dto;
    }
}
