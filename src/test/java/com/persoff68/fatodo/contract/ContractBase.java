package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.builder.TestEvent;
import com.persoff68.fatodo.builder.TestEventRecipient;
import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.EventRecipient;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.repository.EventRecipientRepository;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.repository.ReadStatusRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMessageVerifier
class ContractBase {

    private static final String USER_ID = "8f9a7cae-73c8-4ad6-b135-5bd109b51d2e";

    @Autowired
    WebApplicationContext context;

    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventRecipientRepository eventRecipientRepository;
    @Autowired
    ReadStatusRepository readStatusRepository;

    @MockBean
    WsServiceClient wsServiceClient;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);

        Event event = buildEvent(USER_ID);
        eventRepository.save(event);
    }

    @AfterEach
    void cleanup() {
        eventRepository.deleteAll();
        eventRecipientRepository.deleteAll();
        readStatusRepository.deleteAll();
    }

    private Event buildEvent(String userId) {
        EventRecipient eventRecipient = TestEventRecipient.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.WELCOME).eventRecipients(List.of(eventRecipient))
                .build().toParent();
        eventRecipient.setEvent(event);
        return event;
    }

}
