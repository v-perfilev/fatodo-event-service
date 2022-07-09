package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.ItemEventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestItemEventDTO extends ItemEventDTO {

    @Builder
    public TestItemEventDTO(EventType eventType, UUID userId, UUID groupId, UUID itemId,
                            List<UUID> userIds, List<UUID> recipientIds) {
        super();
        super.setType(eventType);
        super.setUserId(userId);
        super.setGroupId(groupId);
        super.setItemId(itemId);
        super.setUserIds(userIds);
        super.setRecipientIds(recipientIds);
    }

    public static TestItemEventDTOBuilder defaultBuilder() {
        return TestItemEventDTO.builder()
                .eventType(EventType.ITEM_CREATE)
                .userId(UUID.randomUUID())
                .groupId(UUID.randomUUID())
                .itemId(UUID.randomUUID())
                .userIds(List.of(UUID.randomUUID()))
                .recipientIds(List.of(UUID.randomUUID()));
    }

    public ItemEventDTO toParent() {
        ItemEventDTO dto = new ItemEventDTO();
        dto.setType(getType());
        dto.setUserId(getUserId());
        dto.setGroupId(getGroupId());
        dto.setItemId(getItemId());
        dto.setUserIds(getUserIds());
        dto.setRecipientIds(getRecipientIds());
        return dto;
    }

}
