package com.persoff68.fatodo.model.mapper;

import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.CommentEvent;
import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.dto.ChatEventDTO;
import com.persoff68.fatodo.model.dto.CommentEventDTO;
import com.persoff68.fatodo.model.dto.ContactEventDTO;
import com.persoff68.fatodo.model.dto.ItemEventDTO;
import com.persoff68.fatodo.model.dto.UserEventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    ContactEvent contactDTOToPojo(ContactEventDTO dto);

    ItemEvent itemDTOToPojo(ItemEventDTO dto);

    CommentEvent commentDTOToPojo(CommentEventDTO dto);

    ChatEvent chatDTOToPojo(ChatEventDTO dto);

    UserEventDTO pojoToDTO(Event event);

}
