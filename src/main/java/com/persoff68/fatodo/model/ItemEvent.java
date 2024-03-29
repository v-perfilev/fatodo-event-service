package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.model.event.Item;
import com.persoff68.fatodo.model.event.ItemGroup;
import com.persoff68.fatodo.model.event.ItemGroupMember;
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
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ftd_event_item")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"event"}, callSuper = true)
@ToString(exclude = {"event"})
public class ItemEvent extends AbstractModel implements Serializable {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID groupId;

    private UUID itemId;

    private String role;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "itemEvent")
    private List<ItemEventUser> users;

    public static ItemEvent of(Item item, UUID userId, Event event) {
        ItemEvent itemEvent = new ItemEvent();
        itemEvent.event = event;
        itemEvent.userId = userId;
        itemEvent.groupId = item.getGroupId();
        itemEvent.itemId = item.getId();
        return itemEvent;
    }

    public static ItemEvent of(ItemGroup itemGroup, UUID userId, Event event) {
        ItemEvent itemEvent = new ItemEvent();
        itemEvent.event = event;
        itemEvent.userId = userId;
        itemEvent.groupId = itemGroup.getId();
        return itemEvent;
    }

    public static ItemEvent of(List<ItemGroupMember> memberList, UUID userId, Event event) {
        ItemEvent itemEvent = new ItemEvent();
        itemEvent.event = event;
        itemEvent.userId = userId;
        itemEvent.groupId = memberList.get(0).getGroupId();
        itemEvent.users = memberList.stream()
                .distinct()
                .map(member -> new ItemEventUser(itemEvent, member.getUserId()))
                .toList();
        return itemEvent;
    }

}
