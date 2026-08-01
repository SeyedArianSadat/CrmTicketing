package com.company.crmticketing.service;

import com.company.crmticketing.dto.message.MessageCreateDto;
import com.company.crmticketing.dto.message.MessageDto;
import com.company.crmticketing.dto.message.MessageUpdateDto;
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
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private CustomerRequestRepository customerRequestRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    void createMessageRequiresRequestOrTicket() {
        MessageCreateDto createDto = createDto(null, null, 3L);
        Message message = new Message();
        when(messageMapper.toEntity(createDto)).thenReturn(message);

        assertThatThrownBy(() -> messageService.createMessage(createDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("message must be linked to a customer request or ticket");

        verify(messageRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void createMessageLinksRequestTicketAndSender() {
        MessageCreateDto createDto = createDto(1L, 2L, 3L);
        Message message = new Message();
        MessageDto dto = messageDto(10L);
        CustomerRequest request = new CustomerRequest();
        Ticket ticket = new Ticket();
        User user = new User();

        when(messageMapper.toEntity(createDto)).thenReturn(message);
        when(customerRequestRepository.findActiveById(1L)).thenReturn(Optional.of(request));
        when(ticketRepository.findActiveById(2L)).thenReturn(Optional.of(ticket));
        when(userRepository.findActiveById(3L)).thenReturn(Optional.of(user));
        when(messageRepository.save(message)).thenReturn(message);
        when(messageMapper.toDto(message)).thenReturn(dto);

        MessageDto result = messageService.createMessage(createDto);

        assertThat(result).isSameAs(dto);
        assertThat(message.getRequest()).isSameAs(request);
        assertThat(message.getTicket()).isSameAs(ticket);
        assertThat(message.getSenderUser()).isSameAs(user);
    }

    @Test
    void createMessageThrowsWhenTicketIsMissing() {
        MessageCreateDto createDto = createDto(null, 404L, 3L);
        Message message = new Message();
        when(messageMapper.toEntity(createDto)).thenReturn(message);
        when(ticketRepository.findActiveById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.createMessage(createDto))
                .isInstanceOf(TicketNotFoundException.class);
    }

    @Test
    void createMessageThrowsWhenSenderIsMissing() {
        MessageCreateDto createDto = createDto(null, 2L, 404L);
        Message message = new Message();
        Ticket ticket = new Ticket();

        when(messageMapper.toEntity(createDto)).thenReturn(message);
        when(ticketRepository.findActiveById(2L)).thenReturn(Optional.of(ticket));
        when(userRepository.findActiveById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.createMessage(createDto))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateMessagePatchesExistingMessageAndRelations() {
        Message existing = new Message();
        MessageUpdateDto updateDto = new MessageUpdateDto(
                "updated content",
                true,
                null,
                2L,
                3L
        );
        Ticket ticket = new Ticket();
        User user = new User();
        MessageDto dto = messageDto(11L);

        when(messageRepository.findById(11L)).thenReturn(Optional.of(existing));
        when(ticketRepository.findActiveById(2L)).thenReturn(Optional.of(ticket));
        when(userRepository.findActiveById(3L)).thenReturn(Optional.of(user));
        when(messageRepository.save(existing)).thenReturn(existing);
        when(messageMapper.toDto(existing)).thenReturn(dto);

        MessageDto result = messageService.updateMessage(11L, updateDto);

        assertThat(result).isSameAs(dto);
        assertThat(existing.getTicket()).isSameAs(ticket);
        assertThat(existing.getSenderUser()).isSameAs(user);
        verify(messageMapper).updateMessageFromDto(updateDto, existing);
    }

    @Test
    void updateMessageThrowsWhenMessageIsMissing() {
        MessageUpdateDto updateDto = new MessageUpdateDto(
                "updated content",
                true,
                null,
                2L,
                3L
        );
        when(messageRepository.findById(11L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.updateMessage(11L, updateDto))
                .isInstanceOf(MessageNotFoundException.class);
    }

    @Test
    void deleteMessageSoftDeletesActiveMessage() {
        Message message = new Message();
        when(messageRepository.findActiveById(11L)).thenReturn(Optional.of(message));
        when(messageRepository.softDeleteWithRetry(11L, 3)).thenReturn(true);

        messageService.deleteMessageById(11L);

        verify(messageRepository).softDeleteWithRetry(11L, 3);
    }

    private static MessageCreateDto createDto(Long requestId, Long ticketId, Long senderUserId) {
        return new MessageCreateDto(
                "hello support team",
                false,
                requestId,
                ticketId,
                senderUserId
        );
    }

    private static MessageDto messageDto(Long id) {
        MessageDto dto = new MessageDto();
        dto.setMessageId(id);
        return dto;
    }
}
