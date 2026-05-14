package com.company.crmticketing.service;

import com.company.crmticketing.dto.Sla.SlaDto;
import com.company.crmticketing.mapper.SlaMapper;
import com.company.crmticketing.model.Sla;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.repository.SlaRepository;
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

    @Override
    protected String getEntityTypeName() {
        return "Sla";
    }

    @Transactional
    public SlaDto createSla(SlaDto slaDto) {
        log.debug("Creating a new sla");
        try {
            Sla sla = slaMapper.toEntity(slaDto);
            slaRepository.save(sla);
            return slaMapper.toDto(sla);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Sla could not be created");
        }
    }

    @Transactional
    public SlaDto updateSla(SlaDto slaDto) {
        log.debug("Updating a new sla");
        try {
            Sla sla = slaMapper.toEntity(slaDto);
            slaRepository.save(sla);
            return slaMapper.toDto(sla);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Sla could not be updated");
        }
    }

    @Transactional
    public void deleteBySlaId(Long slaId) {
        log.debug("Deleting sla");
        try {
            slaRepository.deleteById(slaId);
        } catch (Exception e) {
            log.error("Sla could not be deleted");
            throw new UnsupportedOperationException("Sla could not be deleted");
        }
    }

    public Optional<SlaDto> findByPriorityLevel(Priority priority) {
        log.debug("Finding a sla by priority level");
        return slaRepository.findByPriorityLevel(priority)
                .map(slaMapper::toDto);
    }
}
