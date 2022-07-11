package com.persoff68.fatodo.model.mapper;

import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.ChatEventUser;
import com.persoff68.fatodo.model.CommentEvent;
import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.ItemEventUser;
import com.persoff68.fatodo.model.ReminderEvent;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.dto.create.CreateChatEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateCommentEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateContactEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateItemEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateReminderEventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    ContactEvent contactDTOToPojo(CreateContactEventDTO dto);

    ItemEvent itemDTOToPojo(CreateItemEventDTO dto);

    CommentEvent commentDTOToPojo(CreateCommentEventDTO dto);

    ChatEvent chatDTOToPojo(CreateChatEventDTO dto);

    ReminderEvent reminderDTOToPojo(CreateReminderEventDTO dto);

    @Mapping(target = "itemEvent.userIds", source = "itemEvent.users", qualifiedByName = "itemUserIdsMapper")
    @Mapping(target = "chatEvent.userIds", source = "chatEvent.users", qualifiedByName = "chatUserIdsMapper")
    EventDTO pojoToDTO(Event event);

    @Named("itemUserIdsMapper")
    static List<UUID> itemUsersToIds(List<ItemEventUser> itemEventUserList) {
        return itemEventUserList.stream().map(ItemEventUser::getUserId).toList();
    }

    @Named("chatUserIdsMapper")
    static List<UUID> chatUsersToIds(List<ChatEventUser> chatEventUserList) {
        return chatEventUserList.stream().map(ChatEventUser::getUserId).toList();
    }

}
