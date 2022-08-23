package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.constant.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventServiceFactory {

    private final EventDefaultService eventDefaultService;
    private final EventItemService eventItemService;
    private final EventContactService eventContactService;
    private final EventCommentService eventCommentService;
    private final EventChatService eventChatService;
    private final EventReminderService eventReminderService;

    public EventService create(EventType type) {
        EventService eventService = null;
        if (type.isDefaultEvent()) {
            eventService = eventDefaultService;
        } else if (type.isItemEvent()) {
            eventService = eventItemService;
        } else if (type.isContactEvent()) {
            eventService = eventContactService;
        } else if (type.isCommentEvent()) {
            eventService = eventCommentService;
        } else if (type.isChatEvent()) {
            eventService = eventChatService;
        } else if (type.isReminderEvent()) {
            eventService = eventReminderService;
        }
        return eventService;
    }

}
