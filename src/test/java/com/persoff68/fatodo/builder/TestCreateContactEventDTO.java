package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.create.CreateContactEventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestCreateContactEventDTO extends CreateContactEventDTO {

    @Builder
    public TestCreateContactEventDTO(EventType eventType, UUID firstUserId, UUID secondUserId, List<UUID> recipientIds) {
        super();
        super.setType(eventType);
        super.setFirstUserId(firstUserId);
        super.setSecondUserId(secondUserId);
        super.setRecipientIds(recipientIds);
    }

    public static TestCreateContactEventDTOBuilder defaultBuilder() {
        return TestCreateContactEventDTO.builder()
                .eventType(EventType.CONTACT_ACCEPT)
                .firstUserId(UUID.randomUUID())
                .secondUserId(UUID.randomUUID())
                .recipientIds(List.of(UUID.randomUUID()));
    }

    public CreateContactEventDTO toParent() {
        CreateContactEventDTO dto = new CreateContactEventDTO();
        dto.setType(getType());
        dto.setFirstUserId(getFirstUserId());
        dto.setSecondUserId(getSecondUserId());
        dto.setRecipientIds(getRecipientIds());
        return dto;
    }

}
