package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.EventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestEventDTO extends EventDTO {

    @Builder
    public TestEventDTO(EventType eventType, List<UUID> recipientIds) {
        super();
        super.setType(eventType);
        super.setRecipientIds(recipientIds);
    }

    public static TestEventDTOBuilder defaultBuilder() {
        return TestEventDTO.builder()
                .eventType(EventType.WELCOME)
                .recipientIds(List.of(UUID.randomUUID()));
    }

    public EventDTO toParent() {
        EventDTO dto = new EventDTO();
        dto.setType(getType());
        dto.setRecipientIds(getRecipientIds());
        return dto;
    }

}
