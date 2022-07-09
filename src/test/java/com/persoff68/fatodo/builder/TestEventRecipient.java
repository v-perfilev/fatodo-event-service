package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.EventRecipient;
import lombok.Builder;

import java.util.UUID;

public class TestEventRecipient extends EventRecipient {

    @Builder
    public TestEventRecipient(Event event, UUID userId) {
        super();
        super.setEvent(event);
        super.setUserId(userId);
//        super.setId(new EventRecipientId(event != null ? event.getId() : null, userId));
    }

    public static TestEventRecipientBuilder defaultBuilder() {
        return TestEventRecipient.builder();
    }

    public EventRecipient toParent() {
        EventRecipient eventRecipient = new EventRecipient();
        eventRecipient.setEvent(getEvent());
//        eventRecipient.setId(getId());
        eventRecipient.setUserId(getUserId());
        return eventRecipient;
    }

}
