package com.persoff68.fatodo.model.constant;

public enum EventType {
    // ACCOUNT
    WELCOME,

    // CONTACT
    CONTACT_SEND,
    CONTACT_ACCEPT,

    // ITEM
    ITEM_CREATE,
    ITEM_UPDATE,
    ITEM_DELETE,
    ITEM_GROUP_CREATE,
    ITEM_GROUP_UPDATE,
    ITEM_MEMBER_ADD,
    ITEM_MEMBER_DELETE,
    ITEM_MEMBER_LEAVE,
    ITEM_MEMBER_ROLE,

    // COMMENT
    COMMENT_ADD,
    COMMENT_REACTION,

    // CHAT
    CHAT_CREATE,
    CHAT_UPDATE,
    CHAT_MEMBER_ADD,
    CHAT_MEMBER_DELETE,
    CHAT_MEMBER_LEAVE,
    CHAT_REACTION,

    // REMINDER
    REMINDER;

    public boolean isDefaultEvent() {
        return switch (this) {
            case WELCOME -> true;
            default -> false;
        };
    }

    public boolean isContactEvent() {
        return switch (this) {
            case CONTACT_SEND, CONTACT_ACCEPT -> true;
            default -> false;
        };
    }

    public boolean isItemEvent() {
        return switch (this) {
            case ITEM_CREATE, ITEM_UPDATE, ITEM_DELETE, ITEM_GROUP_CREATE, ITEM_GROUP_UPDATE, ITEM_MEMBER_ADD,
                    ITEM_MEMBER_DELETE, ITEM_MEMBER_LEAVE, ITEM_MEMBER_ROLE -> true;
            default -> false;
        };
    }

    public boolean isCommentEvent() {
        return switch (this) {
            case COMMENT_ADD, COMMENT_REACTION -> true;
            default -> false;
        };
    }

    public boolean isChatEvent() {
        return switch (this) {
            case CHAT_CREATE, CHAT_UPDATE, CHAT_MEMBER_ADD, CHAT_MEMBER_DELETE,
                    CHAT_MEMBER_LEAVE, CHAT_REACTION -> true;
            default -> false;
        };
    }

    public boolean isReminderEvent() {
        return switch (this) {
            case REMINDER -> true;
            default -> false;
        };
    }

}
