package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.CommentEvent;
import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.PageableReadableList;
import com.persoff68.fatodo.model.ReadStatus;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.repository.EventRecipientRepository;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.repository.ReadStatusRepository;
import com.persoff68.fatodo.service.exception.ModelInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private static final List<EventType> CONTACT_EVENT_TYPES =
            Arrays.stream(EventType.values()).filter(EventType::isContactEvent).toList();
    private static final List<EventType> ITEM_EVENT_TYPES =
            Arrays.stream(EventType.values()).filter(EventType::isItemEvent).toList();
    private static final List<EventType> COMMENT_EVENT_TYPES =
            Arrays.stream(EventType.values()).filter(EventType::isCommentEvent).toList();
    private static final List<EventType> CHAT_EVENT_TYPES =
            Arrays.stream(EventType.values()).filter(EventType::isChatEvent).toList();

    private final EventRepository eventRepository;
    private final EventRecipientRepository eventRecipientRepository;

    private final ReadStatusRepository readStatusRepository;

    public PageableReadableList<Event> getAllPageable(UUID userId, Pageable pageable) {
        Date lastReadAt = updateLastRead(userId);
        Page<Event> eventPage = eventRepository.findAllByUserId(userId, pageable);
        long unreadCount = eventRepository.countFromByUserId(userId, lastReadAt);
        return PageableReadableList.of(eventPage.getContent(), eventPage.getTotalElements(), unreadCount);
    }

    public long getUnreadCount(UUID userId) {
        ReadStatus readStatus = readStatusRepository.findByUserId(userId)
                .orElse(new ReadStatus(userId, new Date(0)));
        Date from = readStatus.getLastReadAt();
        return eventRepository.countFromByUserId(userId, from);
    }

    public Date updateLastRead(UUID userId) {
        ReadStatus readStatus = readStatusRepository.findByUserId(userId)
                .orElse(new ReadStatus(userId, new Date(0)));
        Date from = readStatus.getLastReadAt();
        readStatus.setLastReadAt(new Date());
        readStatusRepository.save(readStatus);
        return from;
    }

    public void addDefaultEvent(EventType type, List<UUID> recipientIdList) {
        if (!type.isDefaultEvent()) {
            throw new ModelInvalidException();
        }
        Event event = new Event(type, recipientIdList);
        eventRepository.save(event);
    }

    public void addContactEvent(EventType type, List<UUID> recipientIdList, ContactEvent contactEvent) {
        if (!type.isContactEvent()) {
            throw new ModelInvalidException();
        }
        Event event = new Event(type, recipientIdList);
        contactEvent.setEvent(event);
        event.setContactEvent(contactEvent);
        eventRepository.save(event);
    }

    public void addItemEvent(EventType type, List<UUID> recipientIdList, ItemEvent itemEvent, List<UUID> userIdList) {
        if (!type.isItemEvent()) {
            throw new ModelInvalidException();
        }
        Event event = new Event(type, recipientIdList);
        itemEvent = new ItemEvent(event, itemEvent, userIdList);
        event.setItemEvent(itemEvent);
        eventRepository.save(event);
    }

    public void addCommentEvent(EventType type, List<UUID> recipientIdList, CommentEvent commentEvent) {
        if (!type.isCommentEvent()) {
            throw new ModelInvalidException();
        }
        Event event = new Event(type, recipientIdList);
        commentEvent.setEvent(event);
        event.setCommentEvent(commentEvent);
        eventRepository.save(event);
    }

    public void addChatEvent(EventType type, List<UUID> recipientIdList, ChatEvent chatEvent, List<UUID> userIdList) {
        if (!type.isChatEvent()) {
            throw new ModelInvalidException();
        }
        Event event = new Event(type, recipientIdList);
        chatEvent = new ChatEvent(event, chatEvent, userIdList);
        event.setChatEvent(chatEvent);
        eventRepository.save(event);
    }

    public void deleteGroupAndCommentEventsForUser(UUID groupId, List<UUID> userIdList) {
        eventRecipientRepository.deleteGroupEventRecipients(ITEM_EVENT_TYPES, groupId, userIdList);
        eventRepository.deleteEmptyItemGroupEvents(ITEM_EVENT_TYPES, groupId);
        eventRecipientRepository.deleteCommentEventRecipients(COMMENT_EVENT_TYPES, groupId, userIdList);
        eventRepository.deleteEmptyCommentEvents(COMMENT_EVENT_TYPES, groupId);
    }

    public void deleteChatEventsForUser(UUID chatId, List<UUID> userIdList) {
        eventRecipientRepository.deleteChatEventRecipients(CHAT_EVENT_TYPES, chatId, userIdList);
        eventRepository.deleteEmptyChatEvents(CHAT_EVENT_TYPES, chatId);
    }

    public void deleteContactsEvents(List<UUID> userIdList) {
        eventRepository.deleteContactEvents(CONTACT_EVENT_TYPES, userIdList);
    }

    public void deleteItemAndCommentEvents(UUID itemId) {
        eventRepository.deleteItemEvents(ITEM_EVENT_TYPES, itemId);
        eventRepository.deleteCommentEventsByTargetId(COMMENT_EVENT_TYPES, itemId);
    }

    public void deleteGroupAndCommentEvents(UUID groupId) {
        eventRepository.deleteGroupEvents(ITEM_EVENT_TYPES, groupId);
        eventRepository.deleteCommentEventsByParentId(COMMENT_EVENT_TYPES, groupId);
    }

    public void deleteChatEvents(UUID chatId) {
        eventRepository.deleteChatEvents(CHAT_EVENT_TYPES, chatId);
    }

}
