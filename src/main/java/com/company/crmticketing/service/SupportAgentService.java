package com.company.crmticketing.service;


import com.company.crmticketing.dto.supportAgent.SupportAgentCreateDto;
import com.company.crmticketing.dto.supportAgent.SupportAgentDto;
import com.company.crmticketing.dto.supportAgent.SupportAgentUpdateDto;
import com.company.crmticketing.exception.DepartmentNotFoundException;
import com.company.crmticketing.exception.SupportAgentNotFoundException;
import com.company.crmticketing.exception.UserNotFoundException;
import com.company.crmticketing.mapper.SupportAgentMapper;
import com.company.crmticketing.model.Department;
import com.company.crmticketing.model.SupportAgent;
import com.company.crmticketing.model.User;
import com.company.crmticketing.repository.DepartmentRepository;
import com.company.crmticketing.repository.SupportAgentRepository;
import com.company.crmticketing.repository.UserRepository;
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
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public SupportAgentService(SupportAgentRepository supportAgentRepository
            , SupportAgentMapper supportAgentMapper, DepartmentRepository departmentRepository, UserRepository userRepository) {
        super(supportAgentRepository,
                supportAgentMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.supportAgentRepository = supportAgentRepository;
        this.supportAgentMapper = supportAgentMapper;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SupportAgentDto createAgent(SupportAgentCreateDto dto) {

        System.out.println(dto.agentName());
        System.out.println(dto.userId());
        System.out.println(dto.departmentId());

        SupportAgent supportAgent = supportAgentMapper.toEntity(dto);

        Department department = departmentRepository
                .findById(dto.departmentId())
                .orElseThrow(() ->
                        new DepartmentNotFoundException(dto.departmentId()));

        User user = userRepository
                .findById(dto.userId())
                .orElseThrow(() ->
                        new UserNotFoundException(dto.userId()));

        supportAgent.setDepartment(department);
        supportAgent.setUser(user);

        SupportAgent saved = supportAgentRepository.save(supportAgent);

        return supportAgentMapper.toDto(saved);
    }
    @Transactional
    public SupportAgentDto updateAgent(Long agentId, SupportAgentDto dto) {

        log.debug("updating agent");

        SupportAgent existing = supportAgentRepository.findById(agentId)
                .orElseThrow(() -> new SupportAgentNotFoundException(agentId));

        try {

            SupportAgentUpdateDto updateDto =
                    new SupportAgentUpdateDto(dto.getAgentName());

            supportAgentMapper.updateSupportAgentFromDto(updateDto, existing);

            if (dto.getDepartmentId() != null) {
                Department department = departmentRepository
                        .findById(dto.getDepartmentId())
                        .orElseThrow(() ->
                                new DepartmentNotFoundException(dto.getDepartmentId()));
                existing.setDepartment(department);
            }

            if (dto.getUserId() != null) {
                User user = userRepository
                        .findById(dto.getUserId())
                        .orElseThrow(() ->
                                new UserNotFoundException(dto.getUserId()));
                existing.setUser(user);
            }

            SupportAgent saved = supportAgentRepository.save(existing);

            return supportAgentMapper.toDto(saved);

        } catch (Exception e) {
            log.error("error updating agent", e);
            throw new IllegalStateException("error updating agent", e);
        }
    }
    @Transactional
    public void deleteAgentById(Long agentId) {
        log.debug("deleting agent");
        if (!existsActive(agentId)) {
            throw new SupportAgentNotFoundException(agentId);
        }
        softDelete(agentId);
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
    public Optional<SupportAgentDto> findById(Long agentId) {

        log.debug("find ticket by id");

        return supportAgentRepository.findById(agentId)
                .map(supportAgentMapper::toDto);

    }


    @Override
    protected String getEntityTypeName() {
        return "SupportAgent";
    }
}
