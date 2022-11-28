package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.event.Item;
import com.persoff68.fatodo.model.event.ItemGroup;
import com.persoff68.fatodo.model.event.ItemGroupMember;
import com.persoff68.fatodo.repository.EventUserRepository;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.service.exception.EventTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventItemService implements EventService {

    private final JsonService jsonService;
    private final EventRepository eventRepository;
    private final EventUserRepository eventUserRepository;

    public void addEvent(EventDTO eventDTO) {
        switch (eventDTO.getType()) {
            case ITEM_GROUP_CREATE, ITEM_GROUP_UPDATE -> addItemGroupEvent(eventDTO);
            case ITEM_GROUP_DELETE -> deleteItemGroupEvent(eventDTO);
            case ITEM_CREATE, ITEM_UPDATE, ITEM_UPDATE_STATUS, ITEM_UPDATE_ARCHIVED -> addItemEvent(eventDTO);
            case ITEM_DELETE -> deleteItemEvent(eventDTO);
            case ITEM_MEMBER_ADD -> addItemMemberAddEvent(eventDTO);
            case ITEM_MEMBER_DELETE -> addItemMemberDeleteEvent(eventDTO);
            case ITEM_MEMBER_LEAVE -> addItemMemberLeaveEvent(eventDTO);
            case ITEM_MEMBER_ROLE -> addItemMemberRoleEvent(eventDTO);
            default -> throw new EventTypeException();
        }
    }

    private void addItemGroupEvent(EventDTO eventDTO) {
        ItemGroup group = jsonService.deserialize(eventDTO.getPayload(), ItemGroup.class);
        Event event = new Event(eventDTO);
        ItemEvent itemEvent = ItemEvent.of(group, eventDTO.getUserId(), event);
        event.setItemEvent(itemEvent);
        eventRepository.save(event);
    }

    private void deleteItemGroupEvent(EventDTO eventDTO) {
        ItemGroup group = jsonService.deserialize(eventDTO.getPayload(), ItemGroup.class);
        UUID groupId = group.getId();
        eventRepository.deleteGroupEvents(groupId);
        eventRepository.deleteCommentEventsByParentId(groupId);
        eventRepository.deleteReminderEventsByGroupId(groupId);
    }

    private void addItemEvent(EventDTO eventDTO) {
        Item item = jsonService.deserialize(eventDTO.getPayload(), Item.class);
        Event event = new Event(eventDTO);
        ItemEvent itemEvent = ItemEvent.of(item, eventDTO.getUserId(), event);
        event.setItemEvent(itemEvent);
        eventRepository.save(event);
    }

    private void deleteItemEvent(EventDTO eventDTO) {
        Item item = jsonService.deserialize(eventDTO.getPayload(), Item.class);
        UUID itemId = item.getId();
        eventRepository.deleteItemEvents(itemId);
        eventRepository.deleteCommentEventsByTargetId(itemId);
        eventRepository.deleteReminderEventsByItemId(itemId);
    }

    private void addItemMemberAddEvent(EventDTO eventDTO) {
        List<ItemGroupMember> memberList = jsonService.deserializeList(eventDTO.getPayload(), ItemGroupMember.class);
        if (!memberList.isEmpty()) {
            addMembersEvents(memberList, eventDTO);
        }
    }

    private void addItemMemberDeleteEvent(EventDTO eventDTO) {
        List<ItemGroupMember> memberList = jsonService.deserializeList(eventDTO.getPayload(), ItemGroupMember.class);
        if (!memberList.isEmpty()) {
            addMembersEvents(memberList, eventDTO);
            deleteMembersEvents(memberList);
        }
    }

    private void addItemMemberLeaveEvent(EventDTO eventDTO) {
        ItemGroupMember member = jsonService.deserialize(eventDTO.getPayload(), ItemGroupMember.class);
        List<ItemGroupMember> memberList = List.of(member);
        addMembersEvents(memberList, eventDTO);
        deleteMembersEvents(memberList);
    }

    private void addItemMemberRoleEvent(EventDTO eventDTO) {
        ItemGroupMember member = jsonService.deserialize(eventDTO.getPayload(), ItemGroupMember.class);
        List<ItemGroupMember> memberList = List.of(member);
        addMembersEvents(memberList, eventDTO);
    }

    private void addMembersEvents(List<ItemGroupMember> memberList, EventDTO eventDTO) {
        Event event = new Event(eventDTO);
        ItemEvent itemEvent = ItemEvent.of(memberList, eventDTO.getUserId(), event);
        event.setItemEvent(itemEvent);
        eventRepository.save(event);
    }

    private void deleteMembersEvents(List<ItemGroupMember> memberList) {
        UUID groupId = memberList.get(0).getGroupId();
        List<UUID> userIdList = memberList.stream().map(ItemGroupMember::getUserId).toList();
        eventUserRepository.deleteGroupEventUsers(groupId, userIdList);
        eventRepository.deleteEmptyItemGroupEvents(groupId);
        eventUserRepository.deleteCommentEventUsers(groupId, userIdList);
        eventRepository.deleteEmptyCommentEvents(groupId);
        eventUserRepository.deleteReminderEventUsers(groupId, userIdList);
        eventRepository.deleteEmptyReminderEvents(groupId);
    }

}
