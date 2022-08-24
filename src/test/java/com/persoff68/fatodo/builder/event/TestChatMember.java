package com.persoff68.fatodo.builder.event;

import com.persoff68.fatodo.model.event.ChatMember;
import lombok.Builder;

import java.util.UUID;

public class TestChatMember extends ChatMember {

    @Builder
    public TestChatMember(UUID chatId, UUID userId) {
        super();
        super.setChatId(chatId);
        super.setUserId(userId);
    }

    public static TestChatMemberBuilder defaultBuilder() {
        return TestChatMember.builder()
                .chatId(UUID.randomUUID())
                .userId(UUID.randomUUID());
    }

    public ChatMember toParent() {
        ChatMember chatMember = new ChatMember();
        chatMember.setChatId(getChatId());
        chatMember.setUserId(getUserId());
        return chatMember;
    }

}
