package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoEventServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestChatEvent;
import com.persoff68.fatodo.builder.TestChatEventDTO;
import com.persoff68.fatodo.builder.TestChatEventUser;
import com.persoff68.fatodo.builder.TestCommentEventDTO;
import com.persoff68.fatodo.builder.TestContactEventDTO;
import com.persoff68.fatodo.builder.TestEvent;
import com.persoff68.fatodo.builder.TestEventDTO;
import com.persoff68.fatodo.builder.TestEventRecipient;
import com.persoff68.fatodo.builder.TestItemEventDTO;
import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.ChatEventUser;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.EventRecipient;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.ChatEventDTO;
import com.persoff68.fatodo.model.dto.CommentEventDTO;
import com.persoff68.fatodo.model.dto.ContactEventDTO;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.dto.ItemEventDTO;
import com.persoff68.fatodo.repository.EventRecipientRepository;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.repository.ReadStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoEventServiceApplication.class)
@AutoConfigureMockMvc
class EventControllerIT {

    private static final String ENDPOINT = "/api/events";

    private static final String USER_ID_1 = "3c300277-b5ea-48d1-80db-ead620cf5846";
    private static final String USER_ID_2 = "357a2a99-7b7e-4336-9cd7-18f2cf73fab9";

    @Autowired
    MockMvc mvc;

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

        Event event = buildChatEvent(USER_ID_1);
        eventRepository.save(event);
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    @Transactional
    void testAddDefaultEvent_ok() throws Exception {
        String url = ENDPOINT + "/default";
        EventDTO dto = TestEventDTO.defaultBuilder()
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        Page<Event> eventPage = eventRepository.findAllByUserId(UUID.fromString(USER_ID_2), Pageable.unpaged());
        assertThat(eventPage).hasSize(1);

        Event event = eventPage.getContent().get(0);
        assertThat(event.getType()).isEqualTo(EventType.WELCOME);
        assertThat(event.getRecipients()).hasSize(1);
        assertThat(event.getContactEvent()).isNull();
        assertThat(event.getItemEvent()).isNull();
        assertThat(event.getCommentEvent()).isNull();
        assertThat(event.getChatEvent()).isNull();

        EventRecipient eventRecipient = event.getRecipients().get(0);
        assertThat(eventRecipient.getUserId()).isEqualTo(UUID.fromString(USER_ID_2));
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddDefaultEvent_wrongType() throws Exception {
        String url = ENDPOINT + "/default";
        EventDTO dto = TestEventDTO.defaultBuilder()
                .eventType(EventType.CHAT_CREATE)
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithCustomSecurityContext
    void testAddDefaultEvent_wrongRole() throws Exception {
        String url = ENDPOINT + "/default";
        EventDTO dto = TestEventDTO.defaultBuilder()
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testAddDefaultEvent_unauthorized() throws Exception {
        String url = ENDPOINT + "/default";
        EventDTO dto = TestEventDTO.defaultBuilder()
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    @Transactional
    void testAddContactEvent_ok() throws Exception {
        String url = ENDPOINT + "/contact";
        ContactEventDTO dto = TestContactEventDTO.defaultBuilder()
                .eventType(EventType.CONTACT_SEND)
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        Page<Event> eventPage = eventRepository.findAllByUserId(UUID.fromString(USER_ID_2), Pageable.unpaged());
        assertThat(eventPage).hasSize(1);

        Event event = eventPage.getContent().get(0);
        assertThat(event.getType()).isEqualTo(EventType.CONTACT_SEND);
    }

    @Test
    @WithAnonymousUser
    void testAddContactEvent_unauthorized() throws Exception {
        String url = ENDPOINT + "/contact";
        ContactEventDTO dto = TestContactEventDTO.defaultBuilder()
                .eventType(EventType.CONTACT_SEND)
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    @Transactional
    void testAddItemEvent_ok() throws Exception {
        String url = ENDPOINT + "/item";
        ItemEventDTO dto = TestItemEventDTO.defaultBuilder()
                .eventType(EventType.ITEM_CREATE)
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        Page<Event> eventPage = eventRepository.findAllByUserId(UUID.fromString(USER_ID_2), Pageable.unpaged());
        assertThat(eventPage).hasSize(1);

        Event event = eventPage.getContent().get(0);
        assertThat(event.getType()).isEqualTo(EventType.ITEM_CREATE);
    }

    @Test
    @WithAnonymousUser
    void testAddItemEvent_unauthorized() throws Exception {
        String url = ENDPOINT + "/item";
        ItemEventDTO dto = TestItemEventDTO.defaultBuilder()
                .eventType(EventType.ITEM_CREATE)
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    @Transactional
    void testAddCommentEvent_ok() throws Exception {
        String url = ENDPOINT + "/comment";
        CommentEventDTO dto = TestCommentEventDTO.defaultBuilder()
                .eventType(EventType.COMMENT_ADD)
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        Page<Event> eventPage = eventRepository.findAllByUserId(UUID.fromString(USER_ID_2), Pageable.unpaged());
        assertThat(eventPage).hasSize(1);

        Event event = eventPage.getContent().get(0);
        assertThat(event.getType()).isEqualTo(EventType.COMMENT_ADD);
    }

    @Test
    @WithAnonymousUser
    void testAddCommentEvent_unauthorized() throws Exception {
        String url = ENDPOINT + "/comment";
        CommentEventDTO dto = TestCommentEventDTO.defaultBuilder()
                .eventType(EventType.COMMENT_ADD)
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    @Transactional
    void testAddChatEvent_ok() throws Exception {
        String url = ENDPOINT + "/chat";
        ChatEventDTO dto = TestChatEventDTO.defaultBuilder()
                .eventType(EventType.CHAT_CREATE)
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        Page<Event> eventPage = eventRepository.findAllByUserId(UUID.fromString(USER_ID_2), Pageable.unpaged());
        assertThat(eventPage).hasSize(1);

        Event event = eventPage.getContent().get(0);
        assertThat(event.getType()).isEqualTo(EventType.CHAT_CREATE);
    }

    @Test
    @WithAnonymousUser
    void testAddChatEvent_unauthorized() throws Exception {
        String url = ENDPOINT + "/chat";
        ChatEventDTO dto = TestChatEventDTO.defaultBuilder()
                .eventType(EventType.CHAT_CREATE)
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    private Event buildChatEvent(String userId) {
        ChatEventUser chatEventUser = TestChatEventUser.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        ChatEvent chatEvent = TestChatEvent.defaultBuilder()
                .users(List.of(chatEventUser)).build().toParent();
        chatEventUser.setChatEvent(chatEvent);

        EventRecipient eventRecipient = TestEventRecipient.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.CHAT_CREATE).eventRecipients(List.of(eventRecipient)).chatEvent(chatEvent)
                .build().toParent();
        eventRecipient.setEvent(event);

        chatEvent.setEvent(event);
        return event;
    }

}
