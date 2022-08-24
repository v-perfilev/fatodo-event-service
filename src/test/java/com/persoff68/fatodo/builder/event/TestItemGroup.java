package com.persoff68.fatodo.builder.event;

import com.persoff68.fatodo.model.event.ItemGroup;
import lombok.Builder;

import java.util.UUID;

public class TestItemGroup extends ItemGroup {

    @Builder
    public TestItemGroup(UUID id) {
        super();
        super.setId(id);
    }

    public static TestItemGroupBuilder defaultBuilder() {
        return TestItemGroup.builder()
                .id(UUID.randomUUID());
    }

    public ItemGroup toParent() {
        ItemGroup itemGroup = new ItemGroup();
        itemGroup.setId(getId());
        return itemGroup;
    }

}
