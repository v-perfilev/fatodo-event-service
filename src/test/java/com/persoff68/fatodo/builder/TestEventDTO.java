package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.EventDTO;
import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestEventDTO extends EventDTO {

    @Builder
    TestEventDTO(List<UUID> userIdList, EventType type, String payload, UUID userId, Date date) {
        super();
        super.setUserIds(userIdList);
        super.setType(type);
        super.setPayload(payload);
        super.setUserId(userId);
        super.setDate(date);
    }

    public static TestEventDTOBuilder defaultBuilder() {
        return TestEventDTO.builder()
                .userIdList(List.of(UUID.randomUUID()))
                .type(EventType.WELCOME)
                .userId(UUID.randomUUID())
                .date(new Date());
    }

    public EventDTO toParent() {
        EventDTO dto = new EventDTO();
        dto.setUserIds(getUserIds());
        dto.setType(getType());
        dto.setPayload(getPayload());
        dto.setUserId(getUserId());
        dto.setDate(getDate());
        return dto;
    }

}
