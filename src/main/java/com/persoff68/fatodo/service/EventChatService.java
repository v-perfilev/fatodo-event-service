package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.event.Chat;
import com.persoff68.fatodo.model.event.ChatMember;
import com.persoff68.fatodo.model.event.ChatReaction;
import com.persoff68.fatodo.repository.EventRecipientRepository;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.service.exception.EventTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventChatService implements EventService {

    private final JsonService jsonService;
    private final EventRepository eventRepository;
    private final EventRecipientRepository eventRecipientRepository;

    public void addEvent(EventDTO eventDTO) {
        switch (eventDTO.getType()) {
            case CHAT_CREATE -> addChatCreateEvent(eventDTO);
            case CHAT_UPDATE -> addChatUpdateEvent(eventDTO);
            case CHAT_MEMBER_ADD -> addChatMemberAddEvent(eventDTO);
            case CHAT_MEMBER_DELETE -> addChatMemberDeleteEvent(eventDTO);
            case CHAT_MEMBER_LEAVE -> addChatMemberLeaveEvent(eventDTO);
            case CHAT_REACTION_INCOMING -> addChatReactionIncomingEvent(eventDTO);
            default -> throw new EventTypeException();
        }
    }

    public void addChatCreateEvent(EventDTO eventDTO) {
        Chat chat = jsonService.deserialize(eventDTO.getPayload(), Chat.class);
        Event event = new Event(eventDTO);
        List<UUID> memberIdList = chat.getMembers().stream()
                .map(ChatMember::getUserId)
                .filter(userId -> !userId.equals(event.getUserId()))
                .toList();
        if (!memberIdList.isEmpty()) {
            ChatEvent chatEvent = ChatEvent.of(chat, memberIdList, eventDTO.getUserId(), event);
            event.setChatEvent(chatEvent);
            eventRepository.save(event);
        }
    }

    public void addChatUpdateEvent(EventDTO eventDTO) {
        Chat chat = jsonService.deserialize(eventDTO.getPayload(), Chat.class);
        Event event = new Event(eventDTO);
        ChatEvent chatEvent = ChatEvent.of(chat, eventDTO.getUserId(), event);
        event.setChatEvent(chatEvent);
        eventRepository.save(event);
    }

    public void addChatMemberAddEvent(EventDTO eventDTO) {
        List<ChatMember> memberList = jsonService.deserializeList(eventDTO.getPayload(), ChatMember.class);
        if (!memberList.isEmpty()) {
            addMembersEvents(memberList, eventDTO);
        }
    }

    public void addChatMemberDeleteEvent(EventDTO eventDTO) {
        List<ChatMember> memberList = jsonService.deserializeList(eventDTO.getPayload(), ChatMember.class);
        if (!memberList.isEmpty()) {
            addMembersEvents(memberList, eventDTO);
            deleteMembersEvents(memberList);
        }
    }

    public void addChatMemberLeaveEvent(EventDTO eventDTO) {
        ChatMember member = jsonService.deserialize(eventDTO.getPayload(), ChatMember.class);
        List<ChatMember> memberList = List.of(member);
        addMembersEvents(memberList, eventDTO);
        deleteMembersEvents(memberList);
    }

    public void addChatReactionIncomingEvent(EventDTO eventDTO) {
        ChatReaction reaction = jsonService.deserialize(eventDTO.getPayload(), ChatReaction.class);
        UUID userId = reaction.getUserId();
        UUID chatId = reaction.getChatId();
        UUID messageId = reaction.getMessageId();
        eventRepository.deleteChatReaction(userId, chatId, messageId);
        if (!reaction.getType().equals(ChatReaction.ReactionType.NONE)) {
            Event event = new Event(eventDTO);
            ChatEvent chatEvent = ChatEvent.of(reaction, eventDTO.getUserId(), event);
            event.setChatEvent(chatEvent);
            eventRepository.save(event);
        }
    }

    private void addMembersEvents(List<ChatMember> memberList, EventDTO eventDTO) {
        Event event = new Event(eventDTO);
        ChatEvent chatEvent = ChatEvent.of(memberList, eventDTO.getUserId(), event);
        event.setChatEvent(chatEvent);
        eventRepository.save(event);
    }

    private void deleteMembersEvents(List<ChatMember> memberList) {
        UUID chatId = memberList.get(0).getChatId();
        List<UUID> userIdList = memberList.stream().map(ChatMember::getUserId).toList();
        eventRecipientRepository.deleteChatEventRecipients(chatId, userIdList);
        eventRepository.deleteEmptyChatEvents(chatId);
    }

}
