package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.ReminderEvent;
import lombok.Builder;

import java.util.UUID;

public class TestReminderEvent extends ReminderEvent {

    @Builder
    public TestReminderEvent(UUID id, UUID groupId, UUID itemId) {
        super();
        super.setId(id);
        super.setGroupId(groupId);
        super.setItemId(itemId);
    }

    public static TestReminderEventBuilder defaultBuilder() {
        return TestReminderEvent.builder()
                .groupId(UUID.randomUUID())
                .itemId(UUID.randomUUID());
    }

    public ReminderEvent toParent() {
        ReminderEvent reminderEvent = new ReminderEvent();
        reminderEvent.setId(getId());
        reminderEvent.setGroupId(getGroupId());
        reminderEvent.setItemId(getItemId());
        return reminderEvent;
    }

}
