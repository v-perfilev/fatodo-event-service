package com.persoff68.fatodo.builder.event;

import com.persoff68.fatodo.model.event.ChatReaction;
import lombok.Builder;

import java.util.UUID;

public class TestChatReaction extends ChatReaction {

    @Builder
    public TestChatReaction(UUID chatId, UUID messageId, UUID userId, ReactionType type) {
        super();
        super.setChatId(chatId);
        super.setMessageId(messageId);
        super.setUserId(userId);
        super.setType(type);
    }

    public static TestChatReactionBuilder defaultBuilder() {
        return TestChatReaction.builder()
                .chatId(UUID.randomUUID())
                .messageId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .type(ReactionType.LIKE);
    }

    public ChatReaction toParent() {
        ChatReaction chatReaction = new ChatReaction();
        chatReaction.setChatId(getChatId());
        chatReaction.setMessageId(getMessageId());
        chatReaction.setUserId(getUserId());
        chatReaction.setType(getType());
        return chatReaction;
    }

}
