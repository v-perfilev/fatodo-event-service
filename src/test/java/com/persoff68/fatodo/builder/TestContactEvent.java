package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.ContactEvent;
import lombok.Builder;

import java.util.UUID;

public class TestContactEvent extends ContactEvent {

    @Builder
    public TestContactEvent(UUID id, UUID firstUserId, UUID secondUserId) {
        super();
        super.setId(id);
        super.setFirstUserId(firstUserId);
        super.setSecondUserId(secondUserId);
    }

    public static TestContactEventBuilder defaultBuilder() {
        return TestContactEvent.builder()
                .firstUserId(UUID.randomUUID())
                .secondUserId(UUID.randomUUID());
    }

    public ContactEvent toParent() {
        ContactEvent contactEvent = new ContactEvent();
        contactEvent.setId(getId());
        contactEvent.setFirstUserId(getFirstUserId());
        contactEvent.setSecondUserId(getSecondUserId());
        return contactEvent;
    }

}
