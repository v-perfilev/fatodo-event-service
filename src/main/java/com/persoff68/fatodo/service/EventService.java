package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.CommentEvent;
import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.ReminderEvent;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.repository.EventRecipientRepository;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.service.client.WsService;
import com.persoff68.fatodo.service.exception.ModelInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private static final List<EventType> CONTACT_EVENT_TYPES =
            Arrays.stream(EventType.values()).filter(EventType::isContactEvent).toList();
    private static final List<EventType> ITEM_EVENT_TYPES =
            Arrays.stream(EventType.values()).filter(EventType::isItemEvent).toList();
    private static final List<EventType> COMMENT_EVENT_TYPES =
            Arrays.stream(EventType.values()).filter(EventType::isCommentEvent).toList();
    private static final List<EventType> CHAT_EVENT_TYPES =
            Arrays.stream(EventType.values()).filter(EventType::isChatEvent).toList();

    private static final List<EventType> REMINDER_EVENT_TYPES =
            Arrays.stream(EventType.values()).filter(EventType::isReminderEvent).toList();

    private final EventRepository eventRepository;
    private final EventRecipientRepository eventRecipientRepository;
    private final WsService wsService;

    public void addDefaultEvent(EventType type, List<UUID> recipientIdList) {
        if (!type.isDefaultEvent()) {
            throw new ModelInvalidException();
        }
        Event event = new Event(type, recipientIdList);
        event = eventRepository.save(event);
        wsService.sendEvent(event);
    }

    public void addContactEvent(EventType type, List<UUID> recipientIdList, ContactEvent contactEvent) {
        if (!type.isContactEvent()) {
            throw new ModelInvalidException();
        }
        // delete previous events
        List<UUID> userIdList = List.of(contactEvent.getFirstUserId(), contactEvent.getSecondUserId());
        eventRepository.deleteContactEvents(CONTACT_EVENT_TYPES, userIdList);
        // and new event
        Event event = new Event(type, recipientIdList);
        contactEvent.setEvent(event);
        event.setContactEvent(contactEvent);
        event = eventRepository.save(event);
        wsService.sendEvent(event);
    }

    public void addItemEvent(EventType type, List<UUID> recipientIdList, ItemEvent itemEvent, List<UUID> userIdList) {
        if (!type.isItemEvent()) {
            throw new ModelInvalidException();
        }
        Event event = new Event(type, recipientIdList);
        itemEvent = new ItemEvent(event, itemEvent, userIdList);
        event.setItemEvent(itemEvent);
        event = eventRepository.save(event);
        wsService.sendEvent(event);
    }

    public void addCommentEvent(EventType type, List<UUID> recipientIdList, CommentEvent commentEvent) {
        if (!type.isCommentEvent()) {
            throw new ModelInvalidException();
        }
        // delete previous if reaction
        if (type.equals(EventType.COMMENT_REACTION)) {
            deleteCommentReaction(commentEvent);
        }
        Event event = new Event(type, recipientIdList);
        commentEvent.setEvent(event);
        event.setCommentEvent(commentEvent);
        event = eventRepository.save(event);
        wsService.sendEvent(event);
    }

    public void addChatEvent(EventType type, List<UUID> recipientIdList, ChatEvent chatEvent, List<UUID> userIdList) {
        if (!type.isChatEvent()) {
            throw new ModelInvalidException();
        }
        // delete previous if reaction
        if (type.equals(EventType.CHAT_REACTION)) {
            deleteChatReaction(chatEvent);
        }
        Event event = new Event(type, recipientIdList);
        chatEvent = new ChatEvent(event, chatEvent, userIdList);
        event.setChatEvent(chatEvent);
        event = eventRepository.save(event);
        wsService.sendEvent(event);
    }


    public void addReminderEvent(EventType type, List<UUID> recipientIdList, ReminderEvent reminderEvent) {
        if (!type.isReminderEvent()) {
            throw new ModelInvalidException();
        }
        Event event = new Event(type, recipientIdList);
        reminderEvent = new ReminderEvent(event, reminderEvent);
        event.setReminderEvent(reminderEvent);
        event = eventRepository.save(event);
        wsService.sendEvent(event);
    }


    public void deleteGroupEventsForUser(UUID groupId, List<UUID> userIdList) {
        eventRecipientRepository.deleteGroupEventRecipients(ITEM_EVENT_TYPES, groupId, userIdList);
        eventRepository.deleteEmptyItemGroupEvents(ITEM_EVENT_TYPES, groupId);
        eventRecipientRepository.deleteCommentEventRecipients(COMMENT_EVENT_TYPES, groupId, userIdList);
        eventRepository.deleteEmptyCommentEvents(COMMENT_EVENT_TYPES, groupId);
        eventRecipientRepository.deleteReminderEventRecipients(REMINDER_EVENT_TYPES, groupId, userIdList);
        eventRepository.deleteEmptyReminderEvents(REMINDER_EVENT_TYPES, groupId);
    }

    public void deleteChatEventsForUser(UUID chatId, List<UUID> userIdList) {
        eventRecipientRepository.deleteChatEventRecipients(CHAT_EVENT_TYPES, chatId, userIdList);
        eventRepository.deleteEmptyChatEvents(CHAT_EVENT_TYPES, chatId);
    }

    public void deleteContactsEvents(List<UUID> userIdList) {
        eventRepository.deleteContactEvents(CONTACT_EVENT_TYPES, userIdList);
    }

    public void deleteItemEvents(UUID itemId) {
        eventRepository.deleteItemEvents(ITEM_EVENT_TYPES, itemId);
        eventRepository.deleteCommentEventsByTargetId(COMMENT_EVENT_TYPES, itemId);
        eventRepository.deleteReminderEventsByItemId(REMINDER_EVENT_TYPES, itemId);
    }

    public void deleteGroupEvents(UUID groupId) {
        eventRepository.deleteGroupEvents(ITEM_EVENT_TYPES, groupId);
        eventRepository.deleteCommentEventsByParentId(COMMENT_EVENT_TYPES, groupId);
        eventRepository.deleteReminderEventsByGroupId(REMINDER_EVENT_TYPES, groupId);
    }

    public void deleteChatEvents(UUID chatId) {
        eventRepository.deleteChatEvents(CHAT_EVENT_TYPES, chatId);
    }

    public void deleteChatReaction(ChatEvent chatEvent) {
        UUID userId = chatEvent.getUserId();
        UUID chatId = chatEvent.getChatId();
        UUID messageId = chatEvent.getMessageId();
        eventRepository.deleteChatReaction(userId, chatId, messageId);
    }

    public void deleteCommentReaction(CommentEvent commentEvent) {
        UUID userId = commentEvent.getUserId();
        UUID targetId = commentEvent.getTargetId();
        UUID commentId = commentEvent.getCommentId();
        eventRepository.deleteChatReaction(userId, targetId, commentId);
    }

}
