package com.persoff68.fatodo.builder.event;

import com.persoff68.fatodo.model.event.ContactRelation;
import lombok.Builder;

import java.util.UUID;

public class TestContactRelation extends ContactRelation {

    @Builder
    public TestContactRelation(UUID firstUserId, UUID secondUserId) {
        super();
        super.setFirstUserId(firstUserId);
        super.setSecondUserId(secondUserId);
    }

    public static TestContactRelationBuilder defaultBuilder() {
        return TestContactRelation.builder()
                .firstUserId(UUID.randomUUID())
                .secondUserId(UUID.randomUUID());
    }

    public ContactRelation toParent() {
        ContactRelation contactRelation = new ContactRelation();
        contactRelation.setFirstUserId(getFirstUserId());
        contactRelation.setSecondUserId(getSecondUserId());
        return contactRelation;
    }

}
