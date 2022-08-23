package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.EventUser;
import lombok.Builder;

import java.util.UUID;

public class TestEventUser extends EventUser {

    @Builder
    public TestEventUser(Event event, UUID userId) {
        super();
        super.setEvent(event);
        super.setUserId(userId);
    }

    public static TestEventUserBuilder defaultBuilder() {
        return TestEventUser.builder();
    }

    public EventUser toParent() {
        EventUser eventUser = new EventUser();
        eventUser.setEvent(getEvent());
        eventUser.setUserId(getUserId());
        return eventUser;
    }

}
