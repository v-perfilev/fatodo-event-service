package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.CommentEvent;
import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.constant.EventType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserEventDTO {

    private EventType type;

    private ContactEvent contactEvent;

    private ItemEvent itemEvent;

    private CommentEvent commentEvent;

    private ChatEvent chatEvent;

}
