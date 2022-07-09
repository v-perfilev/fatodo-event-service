package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.ChatEventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestChatEventDTO extends ChatEventDTO {

    @Builder
    public TestChatEventDTO(EventType eventType, UUID userId, UUID chatId, UUID messageId, String reaction,
                            List<UUID> userIds, List<UUID> recipientIds) {
        super();
        super.setType(eventType);
        super.setUserId(userId);
        super.setChatId(chatId);
        super.setMessageId(messageId);
        super.setReaction(reaction);
        super.setUserIds(userIds);
        super.setRecipientIds(recipientIds);
    }

    public static TestChatEventDTOBuilder defaultBuilder() {
        return TestChatEventDTO.builder()
                .eventType(EventType.CHAT_CREATE)
                .userId(UUID.randomUUID())
                .chatId(UUID.randomUUID())
                .userIds(List.of(UUID.randomUUID()))
                .recipientIds(List.of(UUID.randomUUID()));
    }

    public ChatEventDTO toParent() {
        ChatEventDTO dto = new ChatEventDTO();
        dto.setType(getType());
        dto.setUserId(getUserId());
        dto.setChatId(getChatId());
        dto.setMessageId(getMessageId());
        dto.setReaction(getReaction());
        dto.setUserIds(getUserIds());
        dto.setRecipientIds(getRecipientIds());
        return dto;
    }

}
