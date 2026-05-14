package com.company.crmticketing.service;

import com.company.crmticketing.dto.Message.MessageDto;
import com.company.crmticketing.exception.MessageNotFoundException;
import com.company.crmticketing.mapper.MessageMapper;
import com.company.crmticketing.model.Message;
import com.company.crmticketing.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class MessageService extends BaseEntityService<Message, Long, MessageDto> {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository
            , MessageMapper messageMapper) {
        super(messageRepository,
                messageMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @Transactional
    public MessageDto createMessage(MessageDto messageDto) {
        log.debug("creating message");
        try {
            Message message = messageMapper.toEntity(messageDto);
            messageRepository.save(message);
            return messageDto;
        } catch (Exception e) {
            log.error("error creating message", e);
            throw new IllegalStateException("error creating message", e);
        }
    }

    @Transactional
    public MessageDto updateMessage(Long messageId, MessageDto messageDto) {
        log.debug("updating message");
        messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));
        try {
            Message message = messageMapper.toEntity(messageDto);
            messageRepository.save(message);
            return messageDto;
        } catch (Exception e) {
            log.error("error updating message", e);
            throw new IllegalStateException("error creating message", e);
        }
    }

    @Transactional
    public void deleteMessageById(Long MessageId) {
        log.debug("deleting message");
        if (!messageRepository.existsById(MessageId)) {
            throw new MessageNotFoundException(MessageId);
        }
        try {
            messageRepository.deleteById(MessageId);
        } catch (Exception e) {
            log.error("error deleting message", e);
        }
    }

    public Optional<MessageDto> findByContent(String content) {
        log.debug("finding message by its content");
        return messageRepository.findByContent(content)
                .map(messageMapper::toDto);
    }

    public Optional<MessageDto> findByIdWithUser(Long userId) {
        log.debug("finding message by its id");
        return messageRepository.findByIdWithUser(userId)
                .map(messageMapper::toDto);
    }

    public Optional<MessageDto> findByIdWithTicket(Long ticketId) {
        log.debug("finding message by its id");
        return messageRepository.findByIdWithTicket(ticketId)
                .map(messageMapper::toDto);
    }

    public Optional<MessageDto> findByIdWithTicketAndSender(Long ticketId) {
        log.debug("finding message by its id");
        return messageRepository.findByIdWithTicketAndSender(ticketId)
                .map(messageMapper::toDto);
    }

    @Override
    protected String getEntityTypeName() {
        return "Message";
    }
}
