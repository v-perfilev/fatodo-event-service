package com.persoff68.fatodo.model.constant;

import java.util.List;

public enum EventType {

    WELCOME,

    ITEM_GROUP_CREATE,
    ITEM_GROUP_UPDATE,
    ITEM_GROUP_DELETE,
    ITEM_CREATE,
    ITEM_UPDATE,
    ITEM_UPDATE_STATUS,
    ITEM_UPDATE_ARCHIVED,
    ITEM_DELETE,
    ITEM_MEMBER_ADD,
    ITEM_MEMBER_DELETE,
    ITEM_MEMBER_LEAVE,
    ITEM_MEMBER_ROLE,

    CHAT_CREATE,
    CHAT_UPDATE,
    CHAT_MEMBER_ADD,
    CHAT_MEMBER_DELETE,
    CHAT_MEMBER_LEAVE,
    CHAT_REACTION_INCOMING,

    CONTACT_REQUEST_INCOMING,
    CONTACT_REQUEST_OUTCOMING,
    CONTACT_ACCEPT_INCOMING,
    CONTACT_ACCEPT_OUTCOMING,
    CONTACT_DELETE_INCOMING,
    CONTACT_DELETE_OUTCOMING,
    CONTACT_DELETE,

    COMMENT_CREATE,
    COMMENT_DELETE,
    COMMENT_REACTION_INCOMING,

    REMINDER;

    public boolean isDefaultEvent() {
        List<EventType> eventList = List.of(WELCOME);
        return eventList.contains(this);
    }

    public boolean isItemEvent() {
        List<EventType> eventList = List.of(ITEM_GROUP_CREATE, ITEM_GROUP_UPDATE, ITEM_GROUP_DELETE,
                ITEM_CREATE, ITEM_UPDATE, ITEM_UPDATE_STATUS, ITEM_UPDATE_ARCHIVED, ITEM_DELETE, ITEM_MEMBER_ADD,
                ITEM_MEMBER_DELETE, ITEM_MEMBER_LEAVE, ITEM_MEMBER_ROLE);
        return eventList.contains(this);
    }

    public boolean isContactEvent() {
        List<EventType> eventList = List.of(CONTACT_REQUEST_INCOMING, CONTACT_REQUEST_OUTCOMING,
                CONTACT_ACCEPT_INCOMING, CONTACT_ACCEPT_OUTCOMING, CONTACT_DELETE_INCOMING, CONTACT_DELETE_OUTCOMING,
                CONTACT_DELETE);
        return eventList.contains(this);
    }

    public boolean isCommentEvent() {
        List<EventType> eventList = List.of(COMMENT_CREATE, COMMENT_DELETE, COMMENT_REACTION_INCOMING);
        return eventList.contains(this);
    }

    public boolean isChatEvent() {
        List<EventType> eventList = List.of(CHAT_CREATE, CHAT_UPDATE, CHAT_MEMBER_ADD,
                CHAT_MEMBER_DELETE, CHAT_MEMBER_LEAVE, CHAT_REACTION_INCOMING);
        return eventList.contains(this);
    }

    public boolean isReminderEvent() {
        List<EventType> eventList = List.of(REMINDER);
        return eventList.contains(this);
    }


}
