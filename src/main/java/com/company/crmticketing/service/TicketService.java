package com.company.crmticketing.service;

import com.company.crmticketing.dto.ticket.TicketCreateDto;
import com.company.crmticketing.dto.ticket.TicketDto;
import com.company.crmticketing.dto.ticket.TicketUpdateDto;
import com.company.crmticketing.exception.*;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class TicketService extends BaseEntityService<Ticket, Long, TicketDto> {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final CustomerRequestRepository customerRequestRepository;
    private final DepartmentRepository departmentRepository;
    private final SupportAgentRepository supportAgentRepository;
    private final SlaRepository slaRepository;

    public TicketService(
            TicketRepository ticketRepository,
            TicketMapper ticketMapper,
            CustomerRequestRepository customerRequestRepository,
            DepartmentRepository departmentRepository,
            SupportAgentRepository supportAgentRepository,
            SlaRepository slaRepository
    ) {

        super(
                ticketRepository,
                ticketMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException("Unsupported operation");
                });

        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.customerRequestRepository = customerRequestRepository;
        this.departmentRepository = departmentRepository;
        this.supportAgentRepository = supportAgentRepository;
        this.slaRepository = slaRepository;
    }

    @Transactional
    public TicketDto createTicket(TicketCreateDto createDto) {

        log.debug("Creating ticket");

        if (ticketRepository.existsByTitle(createDto.title())) {
            throw new TicketAlreadyExistException(createDto.title());
        }

        Ticket ticket = ticketMapper.toEntity(createDto);

        applyRelations(
                createDto.departmentId(),
                createDto.agentId(),
                createDto.slaId(),
                ticket
        );

        Ticket saved = ticketRepository.save(ticket);

        linkCustomerRequest(
                createDto.customerRequestId(),
                saved
        );

        log.info("Ticket created successfully with id {}", saved.getTicketId());

        return ticketMapper.toDto(saved);
    }

    @Transactional
    public TicketDto updateTicket(Long ticketId, TicketUpdateDto updateDto) {

        log.debug("Updating ticket {}", ticketId);

        Ticket existing = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        ticketMapper.updateTicketFromDto(updateDto, existing);
        applyRelations(
                existing,
                updateDto.customerRequestId(),
                updateDto.departmentId(),
                updateDto.agentId(),
                updateDto.slaId()
        );
        Ticket saved = ticketRepository.save(existing);

        log.info("Ticket {} updated successfully", ticketId);

        return ticketMapper.toDto(saved);
    }

    @Transactional
    public void deleteByTicketId(Long ticketId) {

        log.debug("Deleting ticket {}", ticketId);

        if (!existsActive(ticketId)) {
            throw new TicketNotFoundException(ticketId);
        }

        softDelete(ticketId);

        log.info("Ticket {} deleted successfully", ticketId);
    }

    public Optional<TicketDto> findByTitle(String title) {
        log.debug("Finding ticket by title {}", title);
        return ticketRepository.findByTitle(title)
                .map(ticketMapper::toDto);
    }

    public List<TicketDto> findByPriority(Priority priority) {
        log.debug("Finding tickets by priority {}", priority);
        return ticketMapper.toTicketDtoList(
                ticketRepository.findByPriority(priority)
        );
    }

    public List<TicketDto> findByRequestStatus(RequestStatus requestStatus) {
        log.debug("Finding tickets by status {}", requestStatus);
        return ticketMapper.toTicketDtoList(
                ticketRepository.findByRequestStatus(requestStatus)
        );
    }

    public Optional<TicketDto> findByIdWithAllDetails(Long id) {
        return ticketRepository.findByIdWithAllDetails(id)
                .map(ticketMapper::toDto);
    }

    public Optional<TicketDto> findByIdWithAttachments(Long id) {
        return ticketRepository.findByIdWithAttachments(id)
                .map(ticketMapper::toDto);
    }

    public Optional<TicketDto> findByIdWithMessages(Long id) {
        return ticketRepository.findByIdWithMessages(id)
                .map(ticketMapper::toDto);
    }

    public Optional<TicketDto> findByIdWithTicketHistories(Long id) {
        return ticketRepository.findByIdWithTicketHistories(id)
                .map(ticketMapper::toDto);
    }

    public List<TicketDto> findAllWithDepartmentWithAgents() {
        return ticketMapper.toTicketDtoList(
                ticketRepository.findAllWithDepartmentAndAgent()
        );
    }

    public List<TicketDto> findAllWithDepartmentIdWithSla(Long departmentId) {
        return ticketMapper.toTicketDtoList(
                ticketRepository.findByDepartmentIdWithSla(departmentId)
        );
    }

    public Optional<TicketDto> findByCustomerRequest(Long requestId) {

        log.debug("finding ticket by customer request");

        return ticketRepository.findByCustomerRequest(requestId)
                .map(ticketMapper::toDto);
    }
//    public List<TicketDto> findAllDtos() {
//
//        log.debug("find all tickets");
//
//        return ticketMapper.toTicketDtoList(ticketRepository.findAllForMvc());
//
//    }

    public Optional<TicketDto> findById(Long ticketId) {

        log.debug("find ticket by id");

        return ticketRepository.findById(ticketId)
                .map(ticketMapper::toDto);

    }

    @Override
    protected String getEntityTypeName() {
        return "Ticket";
    }

    private void applyRelations(
            Ticket ticket,
            Long customerRequestId,
            Long departmentId,
            Long agentId,
            Long slaId
    ) {

        if (departmentId != null) {
            Department department = departmentRepository.findActiveById(departmentId)
                    .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
            ticket.setDepartment(department);
        }

        if (agentId != null) {
            SupportAgent agent = supportAgentRepository.findActiveById(agentId)
                    .orElseThrow(() -> new SupportAgentNotFoundException(agentId));
            ticket.setAgent(agent);
        }

        if (slaId != null) {
            Sla sla = slaRepository.findActiveById(slaId)
                    .orElseThrow(() ->
                            new IllegalArgumentException("SLA not found with id: " + slaId));
            ticket.setSla(sla);
        }

        if (customerRequestId != null) {

            CustomerRequest customerRequest = customerRequestRepository
                    .findActiveById(customerRequestId)
                    .orElseThrow(() ->
                            new CustomerRequestNotFoundException(customerRequestId));

            customerRequest.setTicket(ticket);
            ticket.setCustomerRequest(customerRequest);
        }
    }

    private void applyRelations(
            Long departmentId,
            Long agentId,
            Long slaId,
            Ticket ticket
    ) {
        applyRelations(ticket, null, departmentId, agentId, slaId);
    }

    private void linkCustomerRequest(
            Long customerRequestId,
            Ticket ticket
    ) {

        if (customerRequestId == null) {
            return;
        }

        CustomerRequest customerRequest = customerRequestRepository
                .findActiveById(customerRequestId)
                .orElseThrow(() ->
                        new CustomerRequestNotFoundException(customerRequestId));

        customerRequest.setTicket(ticket);
        ticket.setCustomerRequest(customerRequest);

        customerRequestRepository.save(customerRequest);
    }
}