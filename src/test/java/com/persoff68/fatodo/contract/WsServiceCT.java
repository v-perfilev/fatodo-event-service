package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.builder.TestWsEventDTO;
import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:wsservice:+:stubs"},
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
class WsServiceCT {

    @Autowired
    WsServiceClient wsServiceClient;

    @Test
    void testSendEvent() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setType(EventType.WELCOME);
        eventDTO.setId(UUID.randomUUID());
        eventDTO.setCreatedAt(new Date());
        WsEventDTO<EventDTO> dto = TestWsEventDTO.<EventDTO>defaultBuilder().content(eventDTO).build().toParent();
        assertDoesNotThrow(() -> wsServiceClient.sendEvent(dto));
    }

}
