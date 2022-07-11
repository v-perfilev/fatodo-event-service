package com.persoff68.fatodo.client;

import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ws-service", primary = false, qualifiers = {"feignWsServiceClient"})
public interface WsServiceClient {

    @PostMapping(value = "/api/event")
    void sendEvent(@RequestBody WsEventDTO<EventDTO> event);

}

