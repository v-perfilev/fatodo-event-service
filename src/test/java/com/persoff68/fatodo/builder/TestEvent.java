package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.CommentEvent;
import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.EventRecipient;
import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.ReminderEvent;
import com.persoff68.fatodo.model.constant.EventType;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestEvent extends Event {

    @Builder
    public TestEvent(UUID id,
                     EventType type,
                     List<EventRecipient> eventRecipients,
                     ContactEvent contactEvent,
                     ItemEvent itemEvent,
                     CommentEvent commentEvent,
                     ChatEvent chatEvent,
                     ReminderEvent reminderEvent) {
        super();
        super.setId(id);
        super.setType(type);
        super.setRecipients(eventRecipients);
        super.setContactEvent(contactEvent);
        super.setItemEvent(itemEvent);
        super.setCommentEvent(commentEvent);
        super.setChatEvent(chatEvent);
        super.setReminderEvent(reminderEvent);
    }

    public static TestEventBuilder defaultBuilder() {
        return TestEvent.builder()
                .type(EventType.WELCOME);
    }

    public Event toParent() {
        Event event = new Event();
        event.setId(getId());
        event.setType(getType());
        event.setRecipients(getRecipients());
        event.setContactEvent(getContactEvent());
        event.setItemEvent(getItemEvent());
        event.setCommentEvent(getCommentEvent());
        event.setChatEvent(getChatEvent());
        event.setReminderEvent(getReminderEvent());
        return event;
    }

}
