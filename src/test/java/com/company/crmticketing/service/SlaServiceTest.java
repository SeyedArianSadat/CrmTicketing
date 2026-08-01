package com.company.crmticketing.service;

import com.company.crmticketing.dto.sla.SlaCreateDto;
import com.company.crmticketing.dto.sla.SlaDto;
import com.company.crmticketing.dto.sla.SlaUpdateDto;
import com.company.crmticketing.mapper.SlaMapper;
import com.company.crmticketing.model.Sla;
import com.company.crmticketing.model.enums.Priority;
import com.company.crmticketing.repository.SlaRepository;
import jakarta.persistence.EntityNotFoundException;
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
class SlaServiceTest {

    @Mock
    private SlaRepository slaRepository;

    @Mock
    private SlaMapper slaMapper;

    @InjectMocks
    private SlaService slaService;

    @Test
    void createSlaMapsSavesAndReturnsDto() {
        SlaCreateDto createDto = new SlaCreateDto(Priority.HIGH, 30, 240, "High priority");
        Sla sla = sla(Priority.HIGH);
        SlaDto dto = slaDto(1L, Priority.HIGH);

        when(slaMapper.toEntity(createDto)).thenReturn(sla);
        when(slaRepository.save(sla)).thenReturn(sla);
        when(slaMapper.toDto(sla)).thenReturn(dto);

        SlaDto result = slaService.createSla(createDto);

        assertThat(result).isSameAs(dto);
        verify(slaRepository).save(sla);
    }

    @Test
    void updateSlaPatchesExistingSla() {
        Sla existing = sla(Priority.MEDIUM);
        SlaUpdateDto updateDto = new SlaUpdateDto(Priority.HIGH, 20, 180, "Updated");
        SlaDto dto = slaDto(1L, Priority.HIGH);

        when(slaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(slaRepository.save(existing)).thenReturn(existing);
        when(slaMapper.toDto(existing)).thenReturn(dto);

        SlaDto result = slaService.updateSla(1L, updateDto);

        assertThat(result).isSameAs(dto);
        verify(slaMapper).updateSlaFromDto(updateDto, existing);
    }

    @Test
    void updateSlaThrowsWhenMissing() {
        SlaUpdateDto updateDto = new SlaUpdateDto(Priority.HIGH, 20, 180, "Updated");
        when(slaRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> slaService.updateSla(404L, updateDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("SLA not found with id: 404");
    }

    @Test
    void deleteSlaSoftDeletesActiveSla() {
        Sla sla = sla(Priority.HIGH);
        when(slaRepository.findActiveById(1L)).thenReturn(Optional.of(sla));
        when(slaRepository.softDeleteWithRetry(1L, 3)).thenReturn(true);

        slaService.deleteBySlaId(1L);

        verify(slaRepository).softDeleteWithRetry(1L, 3);
    }

    @Test
    void deleteSlaThrowsWhenMissing() {
        when(slaRepository.findActiveById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> slaService.deleteBySlaId(404L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("SLA not found with id: 404");

        verify(slaRepository, never()).softDeleteWithRetry(404L, 3);
    }

    private static Sla sla(Priority priority) {
        Sla sla = new Sla();
        sla.setPriorityLevel(priority);
        return sla;
    }

    private static SlaDto slaDto(Long id, Priority priority) {
        SlaDto dto = new SlaDto();
        dto.setSlaId(id);
        dto.setPriorityLevel(priority);
        return dto;
    }
}
