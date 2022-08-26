package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.model.event.Chat;
import com.persoff68.fatodo.model.event.ChatMember;
import com.persoff68.fatodo.model.event.ChatReaction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ftd_event_chat")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"event"}, callSuper = true)
@ToString(exclude = {"event"})
public class ChatEvent extends AbstractModel implements Serializable {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID chatId;

    private UUID messageId;

    @Enumerated(EnumType.STRING)
    private ChatReaction.ReactionType reaction;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "chatEvent")
    private List<ChatEventUser> users;

    public static ChatEvent of(Chat chat, UUID userId, Event event) {
        ChatEvent chatEvent = new ChatEvent();
        chatEvent.event = event;
        chatEvent.userId = userId;
        chatEvent.chatId = chat.getId();
        return chatEvent;
    }

    public static ChatEvent of(List<ChatMember> memberList, UUID userId, Event event) {
        ChatEvent chatEvent = new ChatEvent();
        chatEvent.event = event;
        chatEvent.userId = userId;
        chatEvent.chatId = memberList.get(0).getChatId();
        chatEvent.users = memberList.stream()
                .distinct()
                .map(member -> new ChatEventUser(chatEvent, member.getUserId()))
                .toList();
        return chatEvent;
    }

    public static ChatEvent of(ChatReaction reaction, UUID userId, Event event) {
        ChatEvent chatEvent = new ChatEvent();
        chatEvent.event = event;
        chatEvent.userId = userId;
        chatEvent.chatId = reaction.getChatId();
        chatEvent.messageId = reaction.getMessageId();
        chatEvent.reaction = reaction.getType();
        return chatEvent;
    }

}
