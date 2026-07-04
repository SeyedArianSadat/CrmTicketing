package com.company.crmticketing.service;

import com.company.crmticketing.dto.sla.SlaCreateDto;
import com.company.crmticketing.dto.sla.SlaDto;
import com.company.crmticketing.dto.sla.SlaUpdateDto;
import com.company.crmticketing.mapper.SlaMapper;
import com.company.crmticketing.model.Sla;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.repository.SlaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class SlaService extends BaseEntityService<Sla, Long, SlaDto> {
    private final SlaRepository slaRepository;
    private final SlaMapper slaMapper;

    public SlaService(SlaRepository slaRepository
            , SlaMapper slaMapper) {
        super(slaRepository,
                slaMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.slaRepository = slaRepository;
        this.slaMapper = slaMapper;
    }

    @Transactional
    public SlaDto createSla(SlaCreateDto createDto) {

        log.debug("Creating new SLA");

        try {

            Sla sla = slaMapper.toEntity(createDto);

            slaRepository.save(sla);

            return slaMapper.toDto(sla);

        } catch (Exception e) {

            log.error("SLA could not be created", e);

            throw new IllegalArgumentException("SLA could not be created");

        }
    }

    @Transactional
    public SlaDto updateSla(Long slaId, SlaUpdateDto updateDto) {

        log.debug("Updating SLA");

        Sla existing = slaRepository.findById(slaId)
                .orElseThrow(() ->
                        new EntityNotFoundException("SLA not found with id: " + slaId));

        slaMapper.updateSlaFromDto(updateDto, existing);

        slaRepository.save(existing);

        return slaMapper.toDto(existing);
    }

    @Transactional
    public void deleteBySlaId(Long slaId) {
        log.debug("Deleting sla");
        if (!existsActive(slaId)) {
            throw new EntityNotFoundException("SLA not found with id: " + slaId);
        }
        softDelete(slaId);
    }

    public Optional<SlaDto> findByPriorityLevel(Priority priority) {
        log.debug("Finding a sla by priority level");
        return slaRepository.findByPriorityLevel(priority)
                .map(slaMapper::toDto);
    }

    @Override
    protected String getEntityTypeName() {
        return "Sla";
    }
}
