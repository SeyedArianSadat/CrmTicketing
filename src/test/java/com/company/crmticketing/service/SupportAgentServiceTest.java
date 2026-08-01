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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupportAgentServiceTest {

    @Mock
    private SupportAgentRepository supportAgentRepository;

    @Mock
    private SupportAgentMapper supportAgentMapper;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SupportAgentService supportAgentService;

    @Test
    void createAgentLinksDepartmentAndUserAndReturnsDto() {
        SupportAgentCreateDto createDto = new SupportAgentCreateDto("Neda", 5L, 7L);
        SupportAgent agent = agent("Neda");
        Department department = new Department();
        User user = new User();
        SupportAgentDto response = agentDto(1L, "Neda", 5L, 7L);

        when(supportAgentMapper.toEntity(createDto)).thenReturn(agent);
        when(departmentRepository.findById(7L)).thenReturn(Optional.of(department));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(supportAgentRepository.save(agent)).thenReturn(agent);
        when(supportAgentMapper.toDto(agent)).thenReturn(response);

        SupportAgentDto result = supportAgentService.createAgent(createDto);

        assertThat(result).isSameAs(response);
        assertThat(agent.getDepartment()).isSameAs(department);
        assertThat(agent.getUser()).isSameAs(user);
    }

    @Test
    void createAgentThrowsWhenDepartmentMissing() {
        SupportAgentCreateDto createDto = new SupportAgentCreateDto("Neda", 5L, 404L);
        SupportAgent agent = agent("Neda");

        when(supportAgentMapper.toEntity(createDto)).thenReturn(agent);
        when(departmentRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> supportAgentService.createAgent(createDto))
                .isInstanceOf(DepartmentNotFoundException.class);

        verify(supportAgentRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void createAgentThrowsWhenUserMissing() {
        SupportAgentCreateDto createDto = new SupportAgentCreateDto("Neda", 404L, 7L);
        SupportAgent agent = agent("Neda");
        Department department = new Department();

        when(supportAgentMapper.toEntity(createDto)).thenReturn(agent);
        when(departmentRepository.findById(7L)).thenReturn(Optional.of(department));
        when(userRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> supportAgentService.createAgent(createDto))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateAgentPatchesNameAndOptionalRelations() {
        SupportAgent existing = agent("Old");
        SupportAgentDto updateDto = agentDto(9L, "New", 5L, 7L);
        Department department = new Department();
        User user = new User();
        SupportAgentDto response = agentDto(9L, "New", 5L, 7L);

        when(supportAgentRepository.findById(9L)).thenReturn(Optional.of(existing));
        when(departmentRepository.findById(7L)).thenReturn(Optional.of(department));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(supportAgentRepository.save(existing)).thenReturn(existing);
        when(supportAgentMapper.toDto(existing)).thenReturn(response);

        SupportAgentDto result = supportAgentService.updateAgent(9L, updateDto);

        assertThat(result).isSameAs(response);
        assertThat(existing.getDepartment()).isSameAs(department);
        assertThat(existing.getUser()).isSameAs(user);
        verify(supportAgentMapper).updateSupportAgentFromDto(
                org.mockito.ArgumentMatchers.eq(new SupportAgentUpdateDto("New")),
                org.mockito.ArgumentMatchers.same(existing)
        );
    }

    @Test
    void updateAgentThrowsWhenAgentMissing() {
        SupportAgentDto updateDto = agentDto(404L, "New", null, null);
        when(supportAgentRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> supportAgentService.updateAgent(404L, updateDto))
                .isInstanceOf(SupportAgentNotFoundException.class);
    }

    @Test
    void updateAgentWrapsMissingOptionalDepartment() {
        SupportAgent existing = agent("Old");
        SupportAgentDto updateDto = agentDto(9L, "New", null, 404L);

        when(supportAgentRepository.findById(9L)).thenReturn(Optional.of(existing));
        when(departmentRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> supportAgentService.updateAgent(9L, updateDto))
                .isInstanceOf(IllegalStateException.class)
                .hasCauseInstanceOf(DepartmentNotFoundException.class);
    }

    @Test
    void deleteAgentSoftDeletesActiveAgent() {
        SupportAgent agent = agent("Neda");
        when(supportAgentRepository.findActiveById(9L)).thenReturn(Optional.of(agent));
        when(supportAgentRepository.softDeleteWithRetry(9L, 3)).thenReturn(true);

        supportAgentService.deleteAgentById(9L);

        verify(supportAgentRepository).softDeleteWithRetry(9L, 3);
    }

    @Test
    void findAllByAssignedTicketsMapsRepositoryResult() {
        List<SupportAgent> agents = List.of(agent("One"), agent("Two"));
        List<SupportAgentDto> dtos = List.of(agentDto(1L, "One", null, null), agentDto(2L, "Two", null, null));

        when(supportAgentRepository.findAllByAssignedTickets(5L)).thenReturn(agents);
        when(supportAgentMapper.toDtoList(agents)).thenReturn(dtos);

        List<SupportAgentDto> result = supportAgentService.findAllByAssignedTickets(5L);

        assertThat(result).isSameAs(dtos);
    }

    private static SupportAgent agent(String name) {
        SupportAgent agent = new SupportAgent();
        agent.setAgentName(name);
        return agent;
    }

    private static SupportAgentDto agentDto(Long id, String name, Long userId, Long departmentId) {
        SupportAgentDto dto = new SupportAgentDto();
        dto.setAgentId(id);
        dto.setAgentName(name);
        dto.setUserId(userId);
        dto.setDepartmentId(departmentId);
        return dto;
    }
}
