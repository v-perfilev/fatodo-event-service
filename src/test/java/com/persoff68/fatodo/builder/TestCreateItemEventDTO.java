package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.create.CreateItemEventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestCreateItemEventDTO extends CreateItemEventDTO {

    @Builder
    public TestCreateItemEventDTO(EventType eventType, UUID userId, UUID groupId, UUID itemId,
                                  List<UUID> userIds, List<UUID> recipientIds) {
        super();
        super.setType(eventType);
        super.setUserId(userId);
        super.setGroupId(groupId);
        super.setItemId(itemId);
        super.setUserIds(userIds);
        super.setRecipientIds(recipientIds);
    }

    public static TestCreateItemEventDTOBuilder defaultBuilder() {
        return TestCreateItemEventDTO.builder()
                .eventType(EventType.ITEM_CREATE)
                .userId(UUID.randomUUID())
                .groupId(UUID.randomUUID())
                .itemId(UUID.randomUUID())
                .userIds(List.of(UUID.randomUUID()))
                .recipientIds(List.of(UUID.randomUUID()));
    }

    public CreateItemEventDTO toParent() {
        CreateItemEventDTO dto = new CreateItemEventDTO();
        dto.setType(getType());
        dto.setUserId(getUserId());
        dto.setGroupId(getGroupId());
        dto.setItemId(getItemId());
        dto.setUserIds(getUserIds());
        dto.setRecipientIds(getRecipientIds());
        return dto;
    }

}
