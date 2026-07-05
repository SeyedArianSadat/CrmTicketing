package com.company.crmticketing.service;

import com.company.crmticketing.dto.dashboard.DashboardDto;
import com.company.crmticketing.dto.ticket.TicketDto;
import com.company.crmticketing.mapper.TicketMapper;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final DepartmentRepository departmentRepository;
    private final SupportAgentRepository supportAgentRepository;
    private final CustomerRequestRepository customerRequestRepository;
    private final MessageRepository messageRepository;
    private final AttachmentRepository attachmentRepository;
    private final SlaRepository slaRepository;
    private final TicketMapper ticketMapper;

    public DashboardDto getDashboard() {

        return new DashboardDto(

                customerRepository.count(),
                ticketRepository.count(),
                departmentRepository.count(),
                supportAgentRepository.count(),
                customerRequestRepository.count(),
                messageRepository.count(),
                attachmentRepository.count(),
                slaRepository.count(),

                ticketRepository.countByRequestStatus(RequestStatus.OPEN),
                ticketRepository.countByRequestStatus(RequestStatus.IN_PROGRESS),
                ticketRepository.countByRequestStatus(RequestStatus.CLOSED)

        );

    }
    public List<TicketDto> getLatestTickets() {

        return ticketRepository
                .findTop5ByOrderByCreatedAtDesc(PageRequest.of(0,5))
                .stream()
                .map(ticketMapper::toDto)
                .toList();

    }

}