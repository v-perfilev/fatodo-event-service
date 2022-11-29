package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoEventServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestEvent;
import com.persoff68.fatodo.builder.TestEventUser;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.EventUser;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.repository.EventUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoEventServiceApplication.class)
@AutoConfigureMockMvc
class SystemControllerIT {
    private static final String ENDPOINT = "/api/system";

    private static final String USER_ID_1 = "3c300277-b5ea-48d1-80db-ead620cf5846";

    @Autowired
    MockMvc mvc;

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
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testDeleteAccountPermanently_ok() throws Exception {
        String url = ENDPOINT + "/" + USER_ID_1;
        mvc.perform(delete(url))
                .andExpect(status().isOk());
        Page<Event> eventList = eventRepository.findAllByUserId(UUID.fromString(USER_ID_1), Pageable.unpaged());
        assertThat(eventList.getContent()).isEmpty();
    }

    @Test
    @WithCustomSecurityContext(id = USER_ID_1)
    void testGetAllPageable_ok_withParams() throws Exception {
        String url = ENDPOINT + "/" + USER_ID_1;
        mvc.perform(delete(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testGetAllPageable_unauthorized() throws Exception {
        String url = ENDPOINT + "/" + USER_ID_1;
        mvc.perform(delete(url))
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
