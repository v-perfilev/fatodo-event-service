package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.repository.EventRecipientRepository;
import com.persoff68.fatodo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventItemService implements EventService {

    private final EventRepository eventRepository;
    private final EventRecipientRepository eventRecipientRepository;

    public void addEvent(List<UUID> userIdList, EventType type, Object payload) {
        switch (type) {
            case ITEM_GROUP_CREATE -> addItemGroupCreate(userIdList, type, payload);
            case ITEM_GROUP_UPDATE -> addItemGroupUpdate(userIdList, type, payload);
            case ITEM_GROUP_DELETE -> addItemGroupDelete(userIdList, type, payload);
            case ITEM_CREATE -> addItemCreate(userIdList, type, payload);
            case ITEM_UPDATE -> addItemUpdate(userIdList, type, payload);
            case ITEM_DELETE -> addItemDelete(userIdList, type, payload);
            case ITEM_MEMBER_ADD -> addItemMemberAdd(userIdList, type, payload);
            case ITEM_MEMBER_DELETE -> addItemMemberDelete(userIdList, type, payload);
            case ITEM_MEMBER_LEAVE -> addItemMemberLeave(userIdList, type, payload);
            case ITEM_MEMBER_ROLE -> addItemMemberRole(userIdList, type, payload);
        }
    }

    private void addItemGroupCreate(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addItemGroupUpdate(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addItemGroupDelete(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addItemCreate(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addItemUpdate(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addItemDelete(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addItemMemberAdd(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addItemMemberDelete(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addItemMemberLeave(List<UUID> userIdList, EventType type, Object payload) {

    }

    private void addItemMemberRole(List<UUID> userIdList, EventType type, Object payload) {

    }

}
