package com.persoff68.fatodo.mapper;

import com.persoff68.fatodo.model.ChatEventUser;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.ItemEventUser;
import com.persoff68.fatodo.model.dto.EventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    //TODO
//    @Mapping(target = "itemEvent.userIds", source = "itemEvent.users", qualifiedByName = "itemUserIdsMapper")
//    @Mapping(target = "chatEvent.userIds", source = "chatEvent.users", qualifiedByName = "chatUserIdsMapper")
    EventDTO pojoToDTO(Event event);

    @Named("itemUserIdsMapper")
    static List<UUID> itemUserIdsMapper(List<ItemEventUser> itemEventUserList) {
        return itemEventUserList != null
                ? itemEventUserList.stream().map(ItemEventUser::getUserId).toList()
                : Collections.emptyList();
    }

    @Named("chatUserIdsMapper")
    static List<UUID> chatUserIdsMapper(List<ChatEventUser> chatEventUserList) {
        return chatEventUserList != null
                ? chatEventUserList.stream().map(ChatEventUser::getUserId).toList()
                : Collections.emptyList();
    }

}
