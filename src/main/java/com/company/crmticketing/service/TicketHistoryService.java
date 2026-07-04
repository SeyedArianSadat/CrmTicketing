package com.company.crmticketing.service;


import com.company.crmticketing.dto.ticketHistory.TicketHistoryCreateDto;
import com.company.crmticketing.dto.ticketHistory.TicketHistoryDto;
import com.company.crmticketing.dto.ticketHistory.TicketHistoryUpdateDto;
import com.company.crmticketing.exception.TicketNotFoundException;
import com.company.crmticketing.mapper.TicketHistoryMapper;
import com.company.crmticketing.model.TicketHistory;
import com.company.crmticketing.repository.TicketHistoryRepository;
import com.company.crmticketing.model.Ticket;
import com.company.crmticketing.repository.TicketRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class TicketHistoryService extends BaseEntityService<TicketHistory, Long, TicketHistoryDto> {
    private final TicketHistoryRepository ticketHistoryRepository;
    private final TicketHistoryMapper ticketHistoryMapper;
    private final TicketRepository ticketRepository;

    public TicketHistoryService(TicketHistoryRepository ticketHistoryRepository
            , TicketHistoryMapper ticketHistoryMapper, TicketRepository ticketRepository) {
        super(ticketHistoryRepository,
                ticketHistoryMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.ticketHistoryRepository = ticketHistoryRepository;
        this.ticketHistoryMapper = ticketHistoryMapper;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public TicketHistoryDto createTicketHistory(TicketHistoryCreateDto createDto) {

        log.debug("Create ticket history");

        if (createDto.ticketId() == null) {
            throw new IllegalArgumentException("Ticket id is required");
        }

        TicketHistory ticketHistory = ticketHistoryMapper.toEntity(createDto);

        Ticket ticket = ticketRepository.findById(createDto.ticketId())
                .orElseThrow(() -> new TicketNotFoundException(createDto.ticketId()));

        ticketHistory.setTicket(ticket);

        TicketHistory saved = ticketHistoryRepository.save(ticketHistory);

        return ticketHistoryMapper.toDto(saved);
    }

    @Transactional
    public TicketHistoryDto updateTicketHistory(
            Long ticketHistoryId,
            TicketHistoryUpdateDto updateDto
    ) {

        log.debug("Update ticket history");

        TicketHistory existing = ticketHistoryRepository.findById(ticketHistoryId)
                .orElseThrow(() -> new TicketNotFoundException(ticketHistoryId));

        ticketHistoryMapper.updateTicketHistoryFromDto(updateDto, existing);

        ticketHistoryRepository.save(existing);

        return ticketHistoryMapper.toDto(existing);
    }

    @Transactional
    public void deleteByTicketHistoryId(Long ticketHistoryId) {
        log.debug("delete ticket history");
        if (!existsActive(ticketHistoryId)) {
            throw new TicketNotFoundException(ticketHistoryId);
        }
        softDelete(ticketHistoryId);
    }

    public Optional<TicketHistoryDto> findByFieldChanged(String fieldChanged) {
        log.debug("findByFieldChanged");
        return ticketHistoryRepository.findByFieldChanged(fieldChanged)
                .map(ticketHistoryMapper::toDto);
    }

    public Optional<TicketHistoryDto> findByIdWithTicket(Long ticketId) {
        log.debug("findByIdWithTicket");
        return ticketHistoryRepository.findByIdWithTicket(ticketId)
                .map(ticketHistoryMapper::toDto);
    }

    public List<TicketHistoryDto> findAllWithTicket() {
        log.debug("findAllWithTicket");
        List<TicketHistory> ticketHistories = ticketHistoryRepository.findAllWithTicket();
        return ticketHistoryMapper.toTicketHistoryDtoList(ticketHistories);
    }

    @Override
    protected String getEntityTypeName() {
        return "TicketHistory";
    }
}
