package com.company.crmticketing.service;

import com.company.crmticketing.dto.attachment.AttachmentCreateDto;
import com.company.crmticketing.dto.attachment.AttachmentDto;
import com.company.crmticketing.dto.attachment.AttachmentUpdateDto;
import com.company.crmticketing.exception.AttachmentNotFoundException;
import com.company.crmticketing.exception.TicketNotFoundException;
import com.company.crmticketing.mapper.AttachmentMapper;
import com.company.crmticketing.model.Attachment;
import com.company.crmticketing.model.Ticket;
import com.company.crmticketing.repository.AttachmentRepository;
import com.company.crmticketing.repository.TicketRepository;
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
class AttachmentServiceTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private AttachmentMapper attachmentMapper;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private AttachmentService attachmentService;

    @Test
    void createAttachmentLinksTicketAndReturnsDto() {
        AttachmentCreateDto createDto = new AttachmentCreateDto("invoice.pdf", "/files/invoice.pdf", 3L);
        Attachment attachment = attachment("invoice.pdf");
        Ticket ticket = new Ticket();
        AttachmentDto response = attachmentDto(1L);

        when(attachmentMapper.toEntity(createDto)).thenReturn(attachment);
        when(ticketRepository.findById(3L)).thenReturn(Optional.of(ticket));
        when(attachmentRepository.save(attachment)).thenReturn(attachment);
        when(attachmentMapper.toDto(attachment)).thenReturn(response);

        AttachmentDto result = attachmentService.createAttachment(createDto);

        assertThat(result).isSameAs(response);
        assertThat(attachment.getTicket()).isSameAs(ticket);
    }

    @Test
    void createAttachmentWrapsMissingTicket() {
        AttachmentCreateDto createDto = new AttachmentCreateDto("invoice.pdf", "/files/invoice.pdf", 404L);
        Attachment attachment = attachment("invoice.pdf");

        when(attachmentMapper.toEntity(createDto)).thenReturn(attachment);
        when(ticketRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> attachmentService.createAttachment(createDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasCauseInstanceOf(TicketNotFoundException.class);

        verify(attachmentRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void updateAttachmentPatchesExistingAttachmentAndTicket() {
        Attachment existing = attachment("old.pdf");
        AttachmentUpdateDto updateDto = new AttachmentUpdateDto("new.pdf", "/files/new.pdf", 4L);
        Ticket ticket = new Ticket();
        AttachmentDto response = attachmentDto(2L);

        when(attachmentRepository.findById(2L)).thenReturn(Optional.of(existing));
        when(ticketRepository.findById(4L)).thenReturn(Optional.of(ticket));
        when(attachmentRepository.save(existing)).thenReturn(existing);
        when(attachmentMapper.toDto(existing)).thenReturn(response);

        AttachmentDto result = attachmentService.updateAttachment(2L, updateDto);

        assertThat(result).isSameAs(response);
        assertThat(existing.getTicket()).isSameAs(ticket);
        verify(attachmentMapper).updateAttachmentFromDto(updateDto, existing);
    }

    @Test
    void updateAttachmentThrowsWhenAttachmentMissing() {
        AttachmentUpdateDto updateDto = new AttachmentUpdateDto("new.pdf", "/files/new.pdf", 4L);
        when(attachmentRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> attachmentService.updateAttachment(404L, updateDto))
                .isInstanceOf(AttachmentNotFoundException.class);
    }

    @Test
    void deleteAttachmentSoftDeletesActiveAttachment() {
        Attachment attachment = attachment("invoice.pdf");
        when(attachmentRepository.findActiveById(2L)).thenReturn(Optional.of(attachment));
        when(attachmentRepository.softDeleteWithRetry(2L, 3)).thenReturn(true);

        attachmentService.deleteByAttachmentId(2L);

        verify(attachmentRepository).softDeleteWithRetry(2L, 3);
    }

    @Test
    void findAllWithTicketMapsRepositoryResult() {
        List<Attachment> attachments = List.of(attachment("one.pdf"), attachment("two.pdf"));
        List<AttachmentDto> dtos = List.of(attachmentDto(1L), attachmentDto(2L));

        when(attachmentRepository.findAllWithTicket()).thenReturn(attachments);
        when(attachmentMapper.toAttachmentDtoList(attachments)).thenReturn(dtos);

        List<AttachmentDto> result = attachmentService.findAllWithTicket();

        assertThat(result).isSameAs(dtos);
    }

    private static Attachment attachment(String fileName) {
        Attachment attachment = new Attachment();
        attachment.setFileName(fileName);
        return attachment;
    }

    private static AttachmentDto attachmentDto(Long id) {
        AttachmentDto dto = new AttachmentDto();
        dto.setAttachmentId(id);
        return dto;
    }
}
