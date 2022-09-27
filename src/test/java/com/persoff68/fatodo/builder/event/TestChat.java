package com.persoff68.fatodo.builder.event;

import com.persoff68.fatodo.model.event.Chat;
import com.persoff68.fatodo.model.event.ChatMember;
import lombok.Builder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TestChat extends Chat {

    @Builder
    public TestChat(UUID id, List<ChatMember> members) {
        super();
        super.setId(id);
        super.setMembers(members);
    }

    public static TestChatBuilder defaultBuilder() {
        return TestChat.builder()
                .id(UUID.randomUUID())
                .members(Collections.emptyList());
    }

    public Chat toParent() {
        Chat chat = new Chat();
        chat.setId(getId());
        chat.setMembers(getMembers());
        return chat;
    }

}
