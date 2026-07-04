package com.company.crmticketing.service;

import com.company.crmticketing.dto.message.MessageCreateDto;
import com.company.crmticketing.dto.message.MessageDto;
import com.company.crmticketing.dto.message.MessageUpdateDto;
import com.company.crmticketing.exception.CustomerRequestNotFoundException;
import com.company.crmticketing.exception.MessageNotFoundException;
import com.company.crmticketing.exception.TicketNotFoundException;
import com.company.crmticketing.exception.UserNotFoundException;
import com.company.crmticketing.mapper.MessageMapper;
import com.company.crmticketing.model.CustomerRequest;
import com.company.crmticketing.model.Message;
import com.company.crmticketing.model.Ticket;
import com.company.crmticketing.model.User;
import com.company.crmticketing.repository.CustomerRequestRepository;
import com.company.crmticketing.repository.MessageRepository;
import com.company.crmticketing.repository.TicketRepository;
import com.company.crmticketing.repository.UserRepository;
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
    private final CustomerRequestRepository customerRequestRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository
            , MessageMapper messageMapper,
                          CustomerRequestRepository customerRequestRepository,
                          TicketRepository ticketRepository,
                          UserRepository userRepository) {
        super(messageRepository,
                messageMapper::toDto,
                dto -> {
                    throw new UnsupportedOperationException(
                            "unsupported operation"
                    );
                });
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
        this.customerRequestRepository = customerRequestRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MessageDto createMessage(MessageCreateDto createDto) {

        log.debug("creating message");

        Message message = messageMapper.toEntity(createDto);
        applyRelations(createDto.requestId(), createDto.ticketId(), createDto.senderUserId(), message);

        Message saved = messageRepository.save(message);

        log.info("message created successfully. id={}", saved.getMessageId());

        return messageMapper.toDto(saved);
    }

    @Transactional
    public MessageDto updateMessage(
            Long messageId,
            MessageUpdateDto updateDto) {

        log.debug("updating message. id={}", messageId);

        Message existing = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        try {

            messageMapper.updateMessageFromDto(updateDto, existing);
            applyRelations(updateDto.requestId(), updateDto.ticketId(), updateDto.senderUserId(), existing);

            Message saved = messageRepository.save(existing);

            log.info("message updated successfully. id={}", saved.getMessageId());

            return messageMapper.toDto(saved);

        } catch (Exception e) {

            log.error("error updating message. id={}", messageId, e);

            throw new IllegalStateException("error updating message", e);
        }
    }

    @Transactional
    public void deleteMessageById(Long MessageId) {
        log.debug("deleting message");
        if (!existsActive(MessageId)) {
            throw new MessageNotFoundException(MessageId);
        }
        softDelete(MessageId);
    }

    public Optional<MessageDto> findByContent(String content) {
        log.debug("finding message by its content");
        return messageRepository.findByContent(content)
                .map(messageMapper::toDto);
    }

    public Optional<MessageDto> findByIdWithUser(Long userId) {
        log.debug("finding message by its id and user");
        return messageRepository.findByIdWithUser(userId)
                .map(messageMapper::toDto);
    }

    public Optional<MessageDto> findByIdWithTicket(Long ticketId) {
        log.debug("finding message by its id and ticket");
        return messageRepository.findByIdWithTicket(ticketId)
                .map(messageMapper::toDto);
    }

    public Optional<MessageDto> findByIdWithTicketAndSender(Long ticketId) {
        log.debug("finding message by its id and sender");
        return messageRepository.findByIdWithTicketAndSender(ticketId)
                .map(messageMapper::toDto);
    }

    @Override
    protected String getEntityTypeName() {
        return "Message";
    }

    private void applyRelations(Long requestId, Long ticketId, Long senderUserId, Message message) {
        if (requestId == null && ticketId == null) {
            throw new IllegalArgumentException("message must be linked to a customer request or ticket");
        }

        if (requestId != null) {
            CustomerRequest request = customerRequestRepository.findActiveById(requestId)
                    .orElseThrow(() -> new CustomerRequestNotFoundException(requestId));
            message.setRequest(request);
        }

        if (ticketId != null) {
            Ticket ticket = ticketRepository.findActiveById(ticketId)
                    .orElseThrow(() -> new TicketNotFoundException(ticketId));
            message.setTicket(ticket);
        }

        if (senderUserId != null) {
            User sender = userRepository.findActiveById(senderUserId)
                    .orElseThrow(() -> new UserNotFoundException(senderUserId));
            message.setSenderUser(sender);
        }
    }
}
