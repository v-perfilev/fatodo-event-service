package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.create.CreateEventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestCreateEventDTO extends CreateEventDTO {

    @Builder
    public TestCreateEventDTO(EventType eventType, List<UUID> recipientIds) {
        super();
        super.setType(eventType);
        super.setRecipientIds(recipientIds);
    }

    public static TestCreateEventDTOBuilder defaultBuilder() {
        return TestCreateEventDTO.builder()
                .eventType(EventType.WELCOME)
                .recipientIds(List.of(UUID.randomUUID()));
    }

    public CreateEventDTO toParent() {
        CreateEventDTO dto = new CreateEventDTO();
        dto.setType(getType());
        dto.setRecipientIds(getRecipientIds());
        return dto;
    }

}
