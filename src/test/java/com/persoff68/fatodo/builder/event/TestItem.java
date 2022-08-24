package com.persoff68.fatodo.builder.event;

import com.persoff68.fatodo.model.event.Item;
import lombok.Builder;

import java.util.UUID;

public class TestItem extends Item {

    @Builder
    public TestItem(UUID id, UUID groupId) {
        super();
        super.setId(id);
        super.setGroupId(groupId);
    }

    public static TestItemBuilder defaultBuilder() {
        return TestItem.builder()
                .id(UUID.randomUUID())
                .groupId(UUID.randomUUID());
    }

    public Item toParent() {
        Item item = new Item();
        item.setId(getId());
        item.setGroupId(getGroupId());
        return item;
    }

}
