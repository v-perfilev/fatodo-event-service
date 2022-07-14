package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoEventServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestEvent;
import com.persoff68.fatodo.builder.TestEventRecipient;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.EventRecipient;
import com.persoff68.fatodo.model.PageableReadableList;
import com.persoff68.fatodo.model.ReadStatus;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.repository.EventRecipientRepository;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.repository.ReadStatusRepository;
import com.persoff68.fatodo.service.EventService;
import com.persoff68.fatodo.service.UserEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoEventServiceApplication.class)
@AutoConfigureMockMvc
class UserEventControllerIT {

    private static final String ENDPOINT = "/api/user-events";

    private static final String USER_ID = "3c300277-b5ea-48d1-80db-ead620cf5846";

    @Autowired
    MockMvc mvc;

    @Autowired
    EventService eventService;
    @Autowired
    UserEventService userEventService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventRecipientRepository eventRecipientRepository;
    @Autowired
    ReadStatusRepository readStatusRepository;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        eventRepository.deleteAll();
        eventRecipientRepository.deleteAll();
        readStatusRepository.deleteAll();

        Event event1 = buildEvent(USER_ID);
        Event event2 = buildEvent(USER_ID);
        eventRepository.save(event1);
        eventRepository.save(event2);
    }

    @Test
    @WithCustomSecurityContext(id = USER_ID)
    void testGetEventsPageable_ok_withoutParams() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        JavaType javaType = objectMapper.getTypeFactory()
                .constructParametricType(PageableReadableList.class, EventDTO.class);
        PageableReadableList<EventDTO> dtoList = objectMapper.readValue(resultString, javaType);
        assertThat(dtoList.getData()).hasSize(2);
        assertThat(dtoList.getCount()).isEqualTo(2);
        assertThat(dtoList.getUnread()).isEqualTo(2);
    }

    @Test
    @WithCustomSecurityContext(id = USER_ID)
    void testGetEventsPageable_ok_withParams() throws Exception {
        String url = ENDPOINT + "?offset=1&size=10";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        JavaType javaType = objectMapper.getTypeFactory()
                .constructParametricType(PageableReadableList.class, EventDTO.class);
        PageableReadableList<EventDTO> dtoList = objectMapper.readValue(resultString, javaType);
        assertThat(dtoList.getData()).hasSize(1);
        assertThat(dtoList.getCount()).isEqualTo(2);
        assertThat(dtoList.getUnread()).isEqualTo(2);
    }

    @Test
    @WithAnonymousUser
    void testGetEventsPageable_unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = USER_ID)
    void testUnreadCount_ok() throws Exception {
        String url = ENDPOINT + "/unread-count";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        long unreadCount = objectMapper.readValue(resultString, Long.class);
        assertThat(unreadCount).isEqualTo(2);
    }

    @Test
    @WithAnonymousUser
    void testUnreadCount_unauthorized() throws Exception {
        String url = ENDPOINT + "/unread-count";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = USER_ID)
    void testRefresh_ok() throws Exception {
        String url = ENDPOINT + "/refresh";
        mvc.perform(get(url))
                .andExpect(status().isOk());

        ReadStatus readStatus = readStatusRepository.findByUserId(UUID.fromString(USER_ID)).orElse(null);
        Instant before = Instant.now().minus(10, ChronoUnit.SECONDS);
        Instant after = Instant.now().plus(10, ChronoUnit.SECONDS);

        assertThat(readStatus).isNotNull();
        assertThat(readStatus.getLastReadAt())
                .matches(d -> d.after(Date.from(before)))
                .matches(d -> d.before(Date.from(after)));

        long unreadCount = userEventService.getUnreadCount(UUID.fromString(USER_ID));
        assertThat(unreadCount).isEqualTo(0);
    }

    @Test
    @WithAnonymousUser
    void testRefresh_unauthorized() throws Exception {
        String url = ENDPOINT + "/refresh";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
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
