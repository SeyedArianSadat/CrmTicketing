package com.company.crmticketing.service;

import com.company.crmticketing.dto.Attachment.AttachmentDto;
import com.company.crmticketing.exception.AttachmentNotFoundException;
import com.company.crmticketing.mapper.AttachmentMapper;
import com.company.crmticketing.model.Attachment;
import com.company.crmticketing.repository.AttachmentRepository;
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

    public AttachmentService(AttachmentRepository attachmentRepository
            , AttachmentMapper attachmentMapper) {
        super(attachmentRepository,
                attachmentMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
    }

    @Transactional
    public AttachmentDto createAttachment(AttachmentDto attachmentDto) {
        log.debug("create attachment");
        try {
            Attachment attachment = attachmentMapper.toEntity(attachmentDto);
            attachmentRepository.save(attachment);
            return attachmentMapper.toDto(attachment);
        } catch (Exception e) {
            log.error("create attachment failed", e);
            throw new IllegalArgumentException("create attachment failed");
        }
    }

    @Transactional
    public AttachmentDto updateAttachment(Long attachmentId, AttachmentDto attachmentDto) {
        log.debug("update attachment");
        attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new AttachmentNotFoundException(attachmentId));
        try {
            Attachment attachment = attachmentMapper.toEntity(attachmentDto);
            attachmentRepository.save(attachment);
            return attachmentMapper.toDto(attachment);
        } catch (Exception e) {
            log.error("update attachment failed", e);
            throw new IllegalArgumentException("update attachment failed");
        }
    }

    @Transactional
    public void deleteByAttachmentId(Long attachmentId) {
        if (!attachmentRepository.existsById(attachmentId)) {
            throw new AttachmentNotFoundException(attachmentId);
        }
        try {
            attachmentRepository.deleteById(attachmentId);
        } catch (Exception e) {
            log.error("delete attachment failed", e);
            throw new IllegalArgumentException("delete attachment failed");
        }
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
