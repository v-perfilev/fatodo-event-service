package com.persoff68.fatodo.builder.event;

import com.persoff68.fatodo.model.event.Chat;
import lombok.Builder;

import java.util.UUID;

public class TestChat extends Chat {

    @Builder
    public TestChat(UUID id) {
        super();
        super.setId(id);
    }

    public static TestChatBuilder defaultBuilder() {
        return TestChat.builder()
                .id(UUID.randomUUID());
    }

    public Chat toParent() {
        Chat chat = new Chat();
        chat.setId(getId());
        return chat;
    }

}
