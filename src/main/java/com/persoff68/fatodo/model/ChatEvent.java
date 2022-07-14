package com.persoff68.fatodo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID chatId;

    private UUID messageId;

    private String reaction;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "chatEvent", orphanRemoval = true)
    private List<ChatEventUser> users;

    public ChatEvent(Event event, ChatEvent chatEvent, List<UUID> userIdList) {
        this.event = event;
        this.userId = chatEvent.userId;
        this.chatId = chatEvent.chatId;
        this.messageId = chatEvent.messageId;
        this.reaction = chatEvent.reaction;
        this.users = userIdList.stream()
                .distinct()
                .map(userId -> new ChatEventUser(this, userId))
                .toList();
    }

}
