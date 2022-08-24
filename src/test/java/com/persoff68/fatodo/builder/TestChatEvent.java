package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.ChatEventUser;
import com.persoff68.fatodo.model.event.ChatReaction;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestChatEvent extends ChatEvent {

    @Builder
    public TestChatEvent(UUID id, UUID userId, UUID chatId, UUID messageId, ChatReaction.ReactionType reaction,
                         List<ChatEventUser> users) {
        super();
        super.setId(id);
        super.setUserId(userId);
        super.setChatId(chatId);
        super.setMessageId(messageId);
        super.setReaction(reaction);
        super.setUsers(users);
    }

    public static TestChatEventBuilder defaultBuilder() {
        return TestChatEvent.builder()
                .userId(UUID.randomUUID())
                .chatId(UUID.randomUUID());
    }

    public ChatEvent toParent() {
        ChatEvent chatEvent = new ChatEvent();
        chatEvent.setId(getId());
        chatEvent.setUserId(getUserId());
        chatEvent.setChatId(getChatId());
        chatEvent.setMessageId(getMessageId());
        chatEvent.setReaction(getReaction());
        chatEvent.setUsers(getUsers());
        return chatEvent;
    }

}
