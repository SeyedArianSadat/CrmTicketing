package com.company.crmticketing.service;


import com.company.crmticketing.dto.SupportAgent.SupportAgentDto;
import com.company.crmticketing.dto.SupportAgent.SupportAgentUpdateDto;
import com.company.crmticketing.exception.SupportAgentNotFoundException;
import com.company.crmticketing.mapper.SupportAgentMapper;
import com.company.crmticketing.model.SupportAgent;
import com.company.crmticketing.repository.SupportAgentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class SupportAgentService extends BaseEntityService<SupportAgent, Long, SupportAgentDto> {
    private final SupportAgentRepository supportAgentRepository;
    private final SupportAgentMapper supportAgentMapper;

    public SupportAgentService(SupportAgentRepository supportAgentRepository
            , SupportAgentMapper supportAgentMapper) {
        super(supportAgentRepository,
                supportAgentMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.supportAgentRepository = supportAgentRepository;
        this.supportAgentMapper = supportAgentMapper;
    }

    @Transactional
    public SupportAgentDto createAgent(SupportAgentDto supportAgentDto) {
        log.debug("creating agent");
        try {
            SupportAgent supportAgent = supportAgentMapper.toEntity(supportAgentDto);
            supportAgentRepository.save(supportAgent);
            return supportAgentMapper.toDto(supportAgent);
        } catch (Exception e) {
            log.error("error creating agent", e);
            throw new IllegalArgumentException("error creating agent", e);
        }
    }

    @Transactional
    public SupportAgentDto updateAgent(Long agentId, SupportAgentDto supportAgentDto) {
        log.debug("updating agent");
        SupportAgent existing = supportAgentRepository.findById(agentId).orElseThrow(() -> new SupportAgentNotFoundException(agentId));
        try {
            SupportAgentUpdateDto updateDto = new SupportAgentUpdateDto(supportAgentDto.getAgentName());
            supportAgentMapper.updateSupportAgentFromDto(updateDto, existing);
            supportAgentRepository.save(existing);
            return supportAgentMapper.toDto(existing);
        } catch (Exception e) {
            log.error("error updating agent", e);
            throw new IllegalStateException("error updating agent", e);
        }
    }

    @Transactional
    public void deleteAgentById(Long agentId) {
        log.debug("deleting agent");
        if (!supportAgentRepository.existsById(agentId)) {
            throw new SupportAgentNotFoundException(agentId);
        }
        try {
            supportAgentRepository.deleteById(agentId);
        } catch (Exception e) {
            log.error("error deleting agent", e);
            throw new NullPointerException("error deleting agent");
        }
    }

    public Optional<SupportAgentDto> findAgentByName(String agentName) {
        log.debug("finding agent");
        return supportAgentRepository.findByAgentName(agentName)
                .map(supportAgentMapper::toDto);
    }

    public Optional<SupportAgentDto> findByIdWithUser(Long id) {
        log.debug("finding agent by id with user");
        return supportAgentRepository.findByIdWithUser(id)
                .map(supportAgentMapper::toDto);
    }

    public Optional<SupportAgentDto> findByIdWithDepartment(Long id) {
        log.debug("finding agent by id with department with user");
        return supportAgentRepository.findByIdWithDepartment(id)
                .map(supportAgentMapper::toDto);
    }

    public Optional<SupportAgentDto> findByIdWithUserWithAssignedTicket(Long id) {
        log.debug("finding agent by id with assigned ticket with user");
        return supportAgentRepository.findByIdWithAssignedTickets(id)
                .map(supportAgentMapper::toDto);
    }

    public List<SupportAgentDto> findAllAgentByIdWithDepartmentAndUser(Long agentId) {
        log.debug("finding all agent with user and department");
        List<SupportAgent> supportAgentDepartmentUser = supportAgentRepository.findAllByIdAgentWithDepartmentAndUser(agentId);
        return supportAgentMapper.toDtoList(supportAgentDepartmentUser);
    }

    public List<SupportAgentDto> findAllByAssignedTickets(Long agentId) {
        log.debug("finding all agent with tickets");
        List<SupportAgent> supportAgentsAssignedTicket = supportAgentRepository.findAllByAssignedTickets(agentId);
        return supportAgentMapper.toDtoList(supportAgentsAssignedTicket);
    }


    @Override
    protected String getEntityTypeName() {
        return "SupportAgent";
    }
}
