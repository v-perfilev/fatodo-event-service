package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoEventServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestEvent;
import com.persoff68.fatodo.builder.TestEventUser;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.EventUser;
import com.persoff68.fatodo.model.PageableReadableList;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.UserEventDTO;
import com.persoff68.fatodo.repository.EventUserRepository;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.service.UserEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoEventServiceApplication.class)
@AutoConfigureMockMvc
class UserEventControllerIT {

    private static final String ENDPOINT = "/api/user-event";

    private static final String USER_ID_1 = "3c300277-b5ea-48d1-80db-ead620cf5846";

    @Autowired
    MockMvc mvc;

    @Autowired
    UserEventService userEventService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventUserRepository eventUserRepository;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        eventRepository.deleteAll();
        eventUserRepository.deleteAll();

        Event event1 = buildEvent(USER_ID_1);
        Event event2 = buildEvent(USER_ID_1);
        eventRepository.save(event1);
        eventRepository.save(event2);
    }

    @Test
    @WithCustomSecurityContext(id = USER_ID_1)
    void testGetEventsPageable_ok_withoutParams() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        JavaType javaType = objectMapper.getTypeFactory()
                .constructParametricType(PageableReadableList.class, UserEventDTO.class);
        PageableReadableList<UserEventDTO> dtoList = objectMapper.readValue(resultString, javaType);
        assertThat(dtoList.getData()).hasSize(2);
        assertThat(dtoList.getCount()).isEqualTo(2);
        assertThat(dtoList.getUnread()).isEqualTo(2);
    }

    @Test
    @WithCustomSecurityContext(id = USER_ID_1)
    void testGetEventsPageable_ok_withParams() throws Exception {
        String url = ENDPOINT + "?offset=1&size=10";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        JavaType javaType = objectMapper.getTypeFactory()
                .constructParametricType(PageableReadableList.class, UserEventDTO.class);
        PageableReadableList<UserEventDTO> dtoList = objectMapper.readValue(resultString, javaType);
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
    @WithCustomSecurityContext(id = USER_ID_1)
    void testUnreadCount_ok_unread() throws Exception {
        String url = ENDPOINT + "/unread";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        long unreadCount = objectMapper.readValue(resultString, Long.class);
        assertThat(unreadCount).isEqualTo(2);
    }

    @Test
    @WithCustomSecurityContext(id = USER_ID_1)
    void testUnreadCount_ok_read() throws Exception {
        String refreshUrl = ENDPOINT + "/refresh";
        mvc.perform(put(refreshUrl))
                .andExpect(status().isOk());

        String url = ENDPOINT + "/unread";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        long unreadCount = objectMapper.readValue(resultString, Long.class);
        assertThat(unreadCount).isZero();
    }

    @Test
    @WithAnonymousUser
    void testUnreadCount_unauthorized() throws Exception {
        String url = ENDPOINT + "/unread";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = USER_ID_1)
    void testRefresh_ok() throws Exception {
        String url = ENDPOINT + "/refresh";
        mvc.perform(put(url))
                .andExpect(status().isOk());

        long unreadCount = userEventService.getUnreadCount(UUID.fromString(USER_ID_1));
        assertThat(unreadCount).isZero();
    }

    @Test
    @WithAnonymousUser
    void testRefresh_unauthorized() throws Exception {
        String url = ENDPOINT + "/refresh";
        mvc.perform(put(url))
                .andExpect(status().isUnauthorized());
    }

    private Event buildEvent(String userId) {
        EventUser eventUser = TestEventUser.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.WELCOME)
                .eventUsers(List.of(eventUser))
                .build().toParent();
        eventUser.setEvent(event);
        return event;
    }

}
