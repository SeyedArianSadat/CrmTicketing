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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@Transactional
public class AttachmentService extends BaseEntityService<Attachment, Long, AttachmentDto> {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final TicketRepository ticketRepository;

    public AttachmentService(AttachmentRepository attachmentRepository
            , AttachmentMapper attachmentMapper, TicketRepository ticketRepository) {
        super(attachmentRepository,
                attachmentMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public AttachmentDto createAttachment(AttachmentCreateDto dto) {

        log.debug("create attachment");

        try {

            Attachment attachment = attachmentMapper.toEntity(dto);

            if (dto.ticketId() != null) {

                Ticket ticket = ticketRepository
                        .findById(dto.ticketId())
                        .orElseThrow(() ->
                                new TicketNotFoundException(dto.ticketId()));

                attachment.setTicket(ticket);
            }

            Attachment saved = attachmentRepository.save(attachment);

            return attachmentMapper.toDto(saved);

        } catch (Exception e) {

            log.error("create attachment failed", e);

            throw new IllegalArgumentException("create attachment failed", e);
        }
    }

    @Transactional
    public AttachmentDto updateAttachment(Long attachmentId, AttachmentUpdateDto updateDto) {

        Attachment existing = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new AttachmentNotFoundException(attachmentId));

        attachmentMapper.updateAttachmentFromDto(updateDto, existing);

        if (updateDto.ticketId() != null) {
            Ticket ticket = ticketRepository.findById(updateDto.ticketId())
                    .orElseThrow(() -> new TicketNotFoundException(updateDto.ticketId()));

            existing.setTicket(ticket);
        }

        Attachment saved = attachmentRepository.save(existing);
        return attachmentMapper.toDto(saved);
    }

    @Transactional
    public void deleteByAttachmentId(Long attachmentId) {
        if (!existsActive(attachmentId)) {
            throw new AttachmentNotFoundException(attachmentId);
        }
        softDelete(attachmentId);
    }

    public Optional<AttachmentDto> findAttachmentByFileName(String fileName) {
        return attachmentRepository.findByFileName(fileName)
                .map(attachmentMapper::toDto);
    }

    public Optional<AttachmentDto> findByIdWithTicket(Long attachmentId) {
        log.debug("find attachment with ticket");
        return attachmentRepository.findByIdWithTicket(attachmentId)
                .map(attachmentMapper::toDto);
    }

    public List<AttachmentDto> findAllWithTicket() {
        log.debug("find all ticket");
        List<Attachment> attachments = attachmentRepository.findAllWithTicket();
        return attachmentMapper.toAttachmentDtoList(attachments);
    }

    public Optional<AttachmentDto> findByTicketIdWithTicket(Long ticketId) {
        log.debug("find attachment with ticketId");
        return attachmentRepository.findByTicketIdWithTicket(ticketId)
                .map(attachmentMapper::toDto);
    }


    @Override
    protected String getEntityTypeName() {
        return "attachment";
    }
}
