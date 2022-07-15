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
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ftd_event_item")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"event"}, callSuper = true)
@ToString(exclude = {"event"})
public class ItemEvent extends AbstractModel {

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID groupId;

    private UUID itemId;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "itemEvent", orphanRemoval = true)
    private List<ItemEventUser> users;

    public ItemEvent(Event event, ItemEvent itemEvent, List<UUID> userIdList) {
        this.event = event;
        this.userId = itemEvent.userId;
        this.groupId = itemEvent.groupId;
        this.itemId = itemEvent.itemId;
        this.users = userIdList.stream()
                .distinct()
                .map(userId -> new ItemEventUser(this, userId))
                .toList();
    }

}
