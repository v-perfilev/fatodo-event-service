package com.persoff68.fatodo.builder.event;

import com.persoff68.fatodo.model.event.ItemGroupMember;
import lombok.Builder;

import java.util.UUID;

public class TestItemGroupMember extends ItemGroupMember {

    @Builder
    public TestItemGroupMember(UUID groupId, UUID userId, Permission permission) {
        super();
        super.setGroupId(groupId);
        super.setUserId(userId);
        super.setPermission(permission);
    }

    public static TestItemGroupMemberBuilder defaultBuilder() {
        return TestItemGroupMember.builder()
                .groupId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .permission(Permission.READ);
    }

    public ItemGroupMember toParent() {
        ItemGroupMember itemGroupMember = new ItemGroupMember();
        itemGroupMember.setGroupId(getGroupId());
        itemGroupMember.setUserId(getUserId());
        itemGroupMember.setPermission(getPermission());
        return itemGroupMember;
    }

}
