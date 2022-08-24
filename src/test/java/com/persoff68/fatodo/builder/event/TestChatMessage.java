package com.persoff68.fatodo.builder.event;

import com.persoff68.fatodo.model.event.ChatMessage;
import lombok.Builder;

import java.util.UUID;

public class TestChatMessage extends ChatMessage {

    @Builder
    public TestChatMessage(UUID id, UUID chatId, UUID userId) {
        super();
        super.setId(id);
        super.setChatId(chatId);
        super.setUserId(userId);
    }

    public static TestChatMessageBuilder defaultBuilder() {
        return TestChatMessage.builder()
                .id(UUID.randomUUID())
                .chatId(UUID.randomUUID())
                .userId(UUID.randomUUID());
    }

    public ChatMessage toParent() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(getId());
        chatMessage.setChatId(getChatId());
        chatMessage.setUserId(getUserId());
        return chatMessage;
    }

}
