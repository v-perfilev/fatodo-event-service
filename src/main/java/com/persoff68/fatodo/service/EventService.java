package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.constant.EventType;

import java.util.List;
import java.util.UUID;

public interface EventService {

    void addEvent(List<UUID> userIdList, EventType type, Object payload);

}
