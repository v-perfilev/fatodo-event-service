package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
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

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "itemEvent", fetch = FetchType.EAGER)
    private List<ItemEventUser> users;

    public ItemEvent(Event event, ItemEvent itemEvent, List<UUID> userIdList) {
        this.event = event;
        this.userId = itemEvent.userId;
        this.groupId = itemEvent.groupId;
        this.itemId = itemEvent.itemId;
        this.role = itemEvent.role;
        this.users = userIdList != null
                ? userIdList.stream().distinct().map(id -> new ItemEventUser(this, id)).toList()
                : Collections.emptyList();
    }

}
