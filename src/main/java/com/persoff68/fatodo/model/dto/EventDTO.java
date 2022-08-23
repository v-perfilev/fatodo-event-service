package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.constant.EventType;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class EventDTO {

    private List<UUID> userIds;

    private EventType type;

    private Object payload;

    private UUID userId;

    private Date date;

}
