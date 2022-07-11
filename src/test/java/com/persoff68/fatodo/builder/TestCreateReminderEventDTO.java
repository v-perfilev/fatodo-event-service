package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.create.CreateReminderEventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestCreateReminderEventDTO extends CreateReminderEventDTO {

    @Builder
    public TestCreateReminderEventDTO(EventType eventType, UUID groupId, UUID itemId, List<UUID> recipientIds) {
        super();
        super.setType(eventType);
        super.setGroupId(groupId);
        super.setItemId(itemId);
        super.setRecipientIds(recipientIds);
    }

    public static TestCreateReminderEventDTOBuilder defaultBuilder() {
        return TestCreateReminderEventDTO.builder()
                .eventType(EventType.REMINDER)
                .groupId(UUID.randomUUID())
                .itemId(UUID.randomUUID())
                .recipientIds(List.of(UUID.randomUUID()));
    }

    public CreateReminderEventDTO toParent() {
        CreateReminderEventDTO dto = new CreateReminderEventDTO();
        dto.setType(getType());
        dto.setGroupId(getGroupId());
        dto.setItemId(getItemId());
        dto.setRecipientIds(getRecipientIds());
        return dto;
    }

}
