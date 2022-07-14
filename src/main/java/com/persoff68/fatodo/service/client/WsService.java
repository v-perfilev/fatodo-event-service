package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.EventRecipient;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import com.persoff68.fatodo.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Async
public class WsService {

    private final WsServiceClient wsServiceClient;
    private final EventMapper eventMapper;

    public void sendEvent(Event event) {
        List<UUID> userIdList = event.getRecipients().stream().map(EventRecipient::getUserId).toList();
        EventDTO eventDTO = eventMapper.pojoToDTO(event);
        WsEventDTO<EventDTO> wsEventDTO = new WsEventDTO<>(userIdList, eventDTO);
        wsServiceClient.sendEvent(wsEventDTO);
    }

}
