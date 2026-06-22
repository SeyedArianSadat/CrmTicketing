package com.company.crmticketing.service;


import com.company.crmticketing.dto.ticket.TicketDto;
import com.company.crmticketing.dto.ticket.TicketUpdateDto;
import com.company.crmticketing.exception.*;
import com.company.crmticketing.mapper.TicketMapper;
import com.company.crmticketing.model.Ticket;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.model.enums.RequestStatus;
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

    public TicketService(TicketRepository ticketRepository
            , TicketMapper ticketMapper) {
        super(ticketRepository,
                ticketMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    @Transactional
    public TicketDto createTicket(TicketDto ticketDto) {
        log.debug("Creating a ticket");
        if (ticketRepository.existsByTitle(ticketDto.getTitle())) {
            throw new TicketAlreadyExistException(ticketDto.getTitle());
        }
        try {
            Ticket ticket = ticketMapper.toEntity(ticketDto);
            ticketRepository.save(ticket);
            return ticketMapper.toDto(ticket);
        } catch (Exception e) {
            log.error("Creating a ticket failed", e);
            throw new TicketCreationException("Creating a ticket failed");
        }
    }

    @Transactional
    public TicketDto updateTicket(Long ticketId, TicketDto ticketDto) {
        log.debug("Updating a ticket");
        Ticket existing = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException(ticketId));
        try {
            TicketUpdateDto updateDto = new TicketUpdateDto(ticketDto.getTitle(), ticketDto.getResolutionDeadline(), ticketDto.getFirstResponseDeadline(), ticketDto.getPriority(), ticketDto.getRequestStatus(), ticketDto.getCustomerRequestId());
            ticketMapper.updateTicketFromDto(updateDto, existing);
            ticketRepository.save(existing);
            return ticketMapper.toDto(existing);
        } catch (Exception e) {
            log.error("Updating a ticket failed", e);
            throw new TicketUpdateException("Updating a ticket failed");
        }
    }

    @Transactional
    public void deleteByTicketId(Long ticketId) {
        log.debug("Deleting a ticket");
        if (!ticketRepository.existsById(ticketId)) {
            throw new TicketNotFoundException(ticketId);
        }
        try {
            ticketRepository.deleteById(ticketId);
        } catch (Exception e) {
            log.error("Deleting a ticket failed", e);
            throw new TicketDeletionException("Deleting a ticket failed");
        }
    }

    public Optional<TicketDto> findByTitle(String title) {
        log.debug("Finding a ticket by title");
        return ticketRepository.findByTitle(title)
                .map(ticketMapper::toDto);
    }

    public List<TicketDto> findByPriority(Priority priority) {
        log.debug("Finding a ticket by priority");
        List<Ticket> ticketsPriority= ticketRepository.findByPriority(priority);
        return ticketMapper.toTicketDtoList(ticketsPriority);
    }

    public List<TicketDto> findByRequestStatus(RequestStatus requestStatus) {
        log.debug("Finding a ticket by request status");
        List<Ticket> ticketsRequestStatus=ticketRepository.findByRequestStatus(requestStatus);
        return ticketMapper.toTicketDtoList(ticketsRequestStatus);
    }

    public Optional<TicketDto> findByIdWithAllDetails(Long id) {
        log.debug("Finding a ticket by id with details");
        return ticketRepository.findByIdWithAllDetails(id)
                .map(ticketMapper::toDto);
    }

    public Optional<TicketDto> findByIdWithAttachments(Long id) {
        log.debug("Finding a ticket by id with attachments");
        return ticketRepository.findByIdWithAttachments(id)
                .map(ticketMapper::toDto);
    }

    public Optional<TicketDto> findByIdWithMessages(Long id) {
        log.debug("Finding a ticket by id with messages");
        return ticketRepository.findByIdWithMessages(id)
                .map(ticketMapper::toDto);
    }

    public Optional<TicketDto> findByIdWithTicketHistories(Long id) {
        log.debug("Finding a ticket by id with ticket history");
        return ticketRepository.findByIdWithTicketHistories(id)
                .map(ticketMapper::toDto);
    }

    public List<TicketDto> findAllWithDepartmentWithAgents() {
        log.debug("Finding all tickets with department and agents");
        List<Ticket> ticketsDepartmentAgents = ticketRepository.findAllWithDepartmentAndAgent();
        return ticketMapper.toTicketDtoList(ticketsDepartmentAgents);
    }

    public List<TicketDto> findAllWithDepartmentIdWithSla(Long departmentId) {
        log.debug("Finding all tickets with sla");
        List<Ticket> ticketsDepartmentSla = ticketRepository.findByDepartmentIdWithSla(departmentId);
        return ticketMapper.toTicketDtoList(ticketsDepartmentSla);
    }

    @Override
    protected String getEntityTypeName() {
        return "Ticket";
    }
}
