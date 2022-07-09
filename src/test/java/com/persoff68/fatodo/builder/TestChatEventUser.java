package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.ChatEventUser;
import lombok.Builder;

import java.util.UUID;

public class TestChatEventUser extends ChatEventUser {

    @Builder
    public TestChatEventUser(ChatEvent chatEvent, UUID userId) {
        super();
        super.setChatEvent(chatEvent);
        super.setUserId(userId);
    }

    public static TestChatEventUserBuilder defaultBuilder() {
        return TestChatEventUser.builder()
                .userId(UUID.randomUUID());
    }

    public ChatEventUser toParent() {
        ChatEventUser chatEventUser = new ChatEventUser();
        chatEventUser.setChatEvent(getChatEvent());
        chatEventUser.setUserId(getUserId());
        return chatEventUser;
    }

}
