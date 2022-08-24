package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.ContactEvent;
import lombok.Builder;

import java.util.UUID;

public class TestContactEvent extends ContactEvent {

    @Builder
    public TestContactEvent(UUID id, UUID userId, UUID firstUserId, UUID secondUserId) {
        super();
        super.setId(id);
        super.setUserId(userId);
        super.setFirstUserId(firstUserId);
        super.setSecondUserId(secondUserId);
    }

    public static TestContactEventBuilder defaultBuilder() {
        return TestContactEvent.builder()
                .userId(UUID.randomUUID())
                .firstUserId(UUID.randomUUID())
                .secondUserId(UUID.randomUUID());
    }

    public ContactEvent toParent() {
        ContactEvent contactEvent = new ContactEvent();
        contactEvent.setId(getId());
        contactEvent.setUserId(getUserId());
        contactEvent.setFirstUserId(getFirstUserId());
        contactEvent.setSecondUserId(getSecondUserId());
        return contactEvent;
    }

}
