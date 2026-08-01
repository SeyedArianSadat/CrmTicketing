package com.company.crmticketing.service;

import com.company.crmticketing.dto.department.DepartmentCreateDto;
import com.company.crmticketing.dto.department.DepartmentDto;
import com.company.crmticketing.dto.department.DepartmentUpdateDto;
import com.company.crmticketing.exception.DepartmentNotFoundException;
import com.company.crmticketing.mapper.DepartmentMapper;
import com.company.crmticketing.model.Department;
import com.company.crmticketing.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void createDepartmentMapsSavesAndReturnsDto() {
        DepartmentCreateDto createDto = new DepartmentCreateDto("Support");
        Department department = department("Support");
        DepartmentDto dto = departmentDto(1L, "Support");

        when(departmentMapper.toEntity(createDto)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(departmentMapper.toDto(department)).thenReturn(dto);

        DepartmentDto result = departmentService.createDepartment(createDto);

        assertThat(result).isSameAs(dto);
        verify(departmentRepository).save(department);
    }

    @Test
    void updateDepartmentPatchesExistingDepartment() {
        Department existing = department("Support");
        DepartmentUpdateDto updateDto = new DepartmentUpdateDto("Operations");
        DepartmentDto dto = departmentDto(1L, "Operations");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(departmentRepository.save(existing)).thenReturn(existing);
        when(departmentMapper.toDto(existing)).thenReturn(dto);

        DepartmentDto result = departmentService.updateDepartment(1L, updateDto);

        assertThat(result).isSameAs(dto);
        verify(departmentMapper).updateDepartmentFromDto(updateDto, existing);
    }

    @Test
    void updateDepartmentThrowsWhenMissing() {
        DepartmentUpdateDto updateDto = new DepartmentUpdateDto("Operations");
        when(departmentRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.updateDepartment(404L, updateDto))
                .isInstanceOf(DepartmentNotFoundException.class);
    }

    @Test
    void deleteDepartmentSoftDeletesActiveDepartment() {
        Department department = department("Support");
        when(departmentRepository.findActiveById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.softDeleteWithRetry(1L, 3)).thenReturn(true);

        departmentService.deleteDepartmentById(1L);

        verify(departmentRepository).softDeleteWithRetry(1L, 3);
    }

    @Test
    void deleteDepartmentThrowsWhenMissing() {
        when(departmentRepository.findActiveById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.deleteDepartmentById(404L))
                .isInstanceOf(DepartmentNotFoundException.class);

        verify(departmentRepository, never()).softDeleteWithRetry(404L, 3);
    }

    private static Department department(String name) {
        Department department = new Department();
        department.setDepartmentName(name);
        return department;
    }

    private static DepartmentDto departmentDto(Long id, String name) {
        DepartmentDto dto = new DepartmentDto();
        dto.setDepartmentId(id);
        dto.setDepartmentName(name);
        return dto;
    }
}
