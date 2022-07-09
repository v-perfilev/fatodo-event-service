package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.ContactEventDTO;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

public class TestContactEventDTO extends ContactEventDTO {

    @Builder
    public TestContactEventDTO(EventType eventType, UUID firstUserId, UUID secondUserId, List<UUID> recipientIds) {
        super();
        super.setType(eventType);
        super.setFirstUserId(firstUserId);
        super.setSecondUserId(secondUserId);
        super.setRecipientIds(recipientIds);
    }

    public static TestContactEventDTOBuilder defaultBuilder() {
        return TestContactEventDTO.builder()
                .eventType(EventType.CONTACT_ACCEPT)
                .firstUserId(UUID.randomUUID())
                .secondUserId(UUID.randomUUID())
                .recipientIds(List.of(UUID.randomUUID()));
    }

    public ContactEventDTO toParent() {
        ContactEventDTO dto = new ContactEventDTO();
        dto.setType(getType());
        dto.setFirstUserId(getFirstUserId());
        dto.setSecondUserId(getSecondUserId());
        dto.setRecipientIds(getRecipientIds());
        return dto;
    }

}
