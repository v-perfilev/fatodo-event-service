package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.ReminderEventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestReminderEventDTO extends ReminderEventDTO {

    @Builder
    public TestReminderEventDTO(EventType eventType, UUID groupId, UUID itemId, List<UUID> recipientIds) {
        super();
        super.setType(eventType);
        super.setGroupId(groupId);
        super.setItemId(itemId);
        super.setRecipientIds(recipientIds);
    }

    public static TestReminderEventDTOBuilder defaultBuilder() {
        return TestReminderEventDTO.builder()
                .eventType(EventType.REMINDER)
                .groupId(UUID.randomUUID())
                .itemId(UUID.randomUUID())
                .recipientIds(List.of(UUID.randomUUID()));
    }

    public ReminderEventDTO toParent() {
        ReminderEventDTO dto = new ReminderEventDTO();
        dto.setType(getType());
        dto.setGroupId(getGroupId());
        dto.setItemId(getItemId());
        dto.setRecipientIds(getRecipientIds());
        return dto;
    }

}
