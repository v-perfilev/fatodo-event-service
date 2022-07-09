package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.ItemEventUser;
import lombok.Builder;

import java.util.UUID;

public class TestItemEventUser extends ItemEventUser {

    @Builder
    public TestItemEventUser(ItemEvent itemEvent, UUID userId) {
        super();
        super.setItemEvent(itemEvent);
        super.setUserId(userId);
    }

    public static TestItemEventUserBuilder defaultBuilder() {
        return TestItemEventUser.builder()
                .userId(UUID.randomUUID());
    }

    public ItemEventUser toParent() {
        ItemEventUser itemEventUser = new ItemEventUser();
        itemEventUser.setItemEvent(getItemEvent());
        itemEventUser.setUserId(getUserId());
        return itemEventUser;
    }

}
