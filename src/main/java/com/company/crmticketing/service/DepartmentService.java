package com.company.crmticketing.service;

import com.company.crmticketing.dto.department.DepartmentCreateDto;
import com.company.crmticketing.dto.department.DepartmentDto;
import com.company.crmticketing.dto.department.DepartmentUpdateDto;
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
    public DepartmentDto createDepartment(DepartmentCreateDto createDto) {

        log.debug("creating department");

        try {

            Department department = departmentMapper.toEntity(createDto);

            Department saved = departmentRepository.save(department);

            return departmentMapper.toDto(saved);

        } catch (Exception e) {
            log.error("failed to create department", e);
            throw new IllegalArgumentException("failed to create department", e);
        }
    }

    @Transactional
    public DepartmentDto updateDepartment(
            Long departmentId,
            DepartmentUpdateDto updateDto) {

        log.debug("updating department");

        Department existing = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        try {

            departmentMapper.updateDepartmentFromDto(updateDto, existing);

            Department saved = departmentRepository.save(existing);

            return departmentMapper.toDto(saved);

        } catch (Exception e) {
            log.error("failed to update department", e);
            throw new IllegalArgumentException("failed to update department", e);
        }
    }

    @Transactional
    public void deleteDepartmentById(Long DepartmentId) {
        log.debug("deleting department");
        if (!existsActive(DepartmentId)) {
            throw new DepartmentNotFoundException(DepartmentId);
        }
        softDelete(DepartmentId);
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
    public Optional<DepartmentDto> findById(Long id) {

        log.debug("find ticket by id");

        return departmentRepository.findById(id)
                .map(departmentMapper::toDto);

    }

    @Override
    protected String getEntityTypeName() {
        return "Department";
    }
}
