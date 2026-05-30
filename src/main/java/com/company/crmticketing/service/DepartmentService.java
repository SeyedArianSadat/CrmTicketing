package com.company.crmticketing.service;

import com.company.crmticketing.dto.Department.DepartmentDto;
import com.company.crmticketing.exception.DepartmentNotFoundException;
import com.company.crmticketing.mapper.DepartmentMapper;
import com.company.crmticketing.model.Department;
import com.company.crmticketing.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class DepartmentService extends BaseEntityService<Department, Long, DepartmentDto> {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository
            , DepartmentMapper departmentMapper) {
        super(departmentRepository,
                departmentMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Transactional
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        log.debug("creating department");
        try {
            Department department = departmentMapper.toEntity(departmentDto);
            departmentRepository.save(department);
            return departmentMapper.toDto(department);
        } catch (Exception e) {
            log.error("failed to create department", e);
            throw new IllegalArgumentException("failed to create department", e);
        }
    }

    @Transactional
    public DepartmentDto updateDepartment(Long departmentId, DepartmentDto departmentDto) {
        log.debug("updating department");
        departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        try {
            Department department = departmentMapper.toEntity(departmentDto);
            departmentRepository.save(department);
            return departmentMapper.toDto(department);
        } catch (Exception e) {
            log.error("failed to update department", e);
            throw new DepartmentNotFoundException(departmentDto.getDepartmentId());
        }
    }

    @Transactional
    public void deleteDepartmentById(Long DepartmentId) {
        log.debug("deleting department");
        if (!departmentRepository.existsById(DepartmentId)) {
            throw new DepartmentNotFoundException(DepartmentId);
        }
        try {
            departmentRepository.deleteById(DepartmentId);
        } catch (Exception e) {
            log.error("failed to delete department", e);
        }
    }

    public Optional<DepartmentDto> findByDepartmentName(String departmentName) {
        log.debug("finding department");
        return departmentRepository.findByDepartmentName(departmentName)
                .map(departmentMapper::toDto);
    }

    public Optional<DepartmentDto> findByIdWithSupportAgent(Long id) {
        log.debug("finding department with agent");
        return departmentRepository.findByIdWithSupportAgent(id)
                .map(departmentMapper::toDto);
    }

    public Optional<DepartmentDto> findBtIdWithTicket(Long id) {
        log.debug("finding department with ticket");
        return departmentRepository.findByIdWithTickets(id)
                .map(departmentMapper::toDto);
    }

    public Optional<DepartmentDto> findByIdWithSupportAgentAndTicket(Long id) {
        log.debug("finding department with ticket and its agent");
        return departmentRepository.findByIdWithAgentsAndTickets(id)
                .map(departmentMapper::toDto);
    }

    @Override
    protected String getEntityTypeName() {
        return "Department";
    }
}
