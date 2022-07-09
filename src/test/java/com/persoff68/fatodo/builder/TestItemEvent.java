package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.ItemEventUser;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestItemEvent extends ItemEvent {

    @Builder
    public TestItemEvent(UUID id, UUID userId, UUID groupId, UUID itemId,
                         List<ItemEventUser> users) {
        super();
        super.setId(id);
        super.setUserId(userId);
        super.setGroupId(groupId);
        super.setItemId(itemId);
        super.setUsers(users);
    }

    public static TestItemEventBuilder defaultBuilder() {
        return TestItemEvent.builder()
                .userId(UUID.randomUUID())
                .groupId(UUID.randomUUID())
                .itemId(UUID.randomUUID());
    }

    public ItemEvent toParent() {
        ItemEvent itemEvent = new ItemEvent();
        itemEvent.setId(getId());
        itemEvent.setUserId(getUserId());
        itemEvent.setGroupId(getGroupId());
        itemEvent.setItemId(getItemId());
        itemEvent.setUsers(getUsers());
        return itemEvent;
    }

}
