package com.company.crmticketing.service;


import com.company.crmticketing.dto.TicketHistory.TicketHistoryDto;
import com.company.crmticketing.exception.TicketNotFoundException;
import com.company.crmticketing.mapper.TicketHistoryMapper;
import com.company.crmticketing.model.TicketHistory;
import com.company.crmticketing.repository.TicketHistoryRepository;
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

    public TicketHistoryService(TicketHistoryRepository ticketHistoryRepository
            , TicketHistoryMapper ticketHistoryMapper) {
        super(ticketHistoryRepository,
                ticketHistoryMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.ticketHistoryRepository = ticketHistoryRepository;
        this.ticketHistoryMapper = ticketHistoryMapper;
    }

    @Transactional
    public TicketHistoryDto createTicketHistory(TicketHistoryDto ticketHistoryDto) {
        log.debug("create ticket history");
        if (ticketHistoryDto.getTicket() == null || ticketHistoryDto.getTicket().getTicketId() == null) {
            throw new IllegalArgumentException("ticket history must references to ticket");
        }
        try {
            TicketHistory ticketHistory = ticketHistoryMapper.toEntity(ticketHistoryDto);
            ticketHistoryRepository.save(ticketHistory);
            return ticketHistoryMapper.toDto(ticketHistory);
        } catch (Exception e) {
            log.error("create ticket history failed", e);
            throw new IllegalArgumentException("create ticket failed,", e);
        }
    }

    @Transactional
    public TicketHistoryDto updateTicketHistory(Long ticketHistoryId, TicketHistoryDto ticketHistoryDto) {
        log.debug("update ticket history");

        TicketHistory existing = ticketHistoryRepository.findById(ticketHistoryId).orElseThrow(() -> new TicketNotFoundException(ticketHistoryId));
        try {
            ticketHistoryMapper.updateTicketHistoryFromDto(new com.company.crmticketing.dto.TicketHistory.TicketHistoryUpdateDto(ticketHistoryDto.getFieldChanged(), ticketHistoryDto.getOldValue(), ticketHistoryDto.getNewValue()), existing);
            ticketHistoryRepository.save(existing);
            return ticketHistoryMapper.toDto(existing);
        } catch (Exception e) {
            log.error("update ticket history failed", e);
            throw new IllegalArgumentException("update ticket history failed", e);
        }
    }

    @Transactional
    public void deleteByTicketHistoryId(Long ticketHistoryId) {
        log.debug("delete ticket history");
        if (!ticketHistoryRepository.existsById(ticketHistoryId)) {
            throw new TicketNotFoundException(ticketHistoryId);
        }
        try {
            ticketHistoryRepository.deleteById(ticketHistoryId);
        } catch (Exception e) {
            log.error("delete ticket history failed", e);
        }
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