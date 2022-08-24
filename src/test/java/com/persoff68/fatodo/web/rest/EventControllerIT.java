package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoEventServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestChatEvent;
import com.persoff68.fatodo.builder.TestChatEventUser;
import com.persoff68.fatodo.builder.TestCommentEvent;
import com.persoff68.fatodo.builder.TestContactEvent;
import com.persoff68.fatodo.builder.TestEvent;
import com.persoff68.fatodo.builder.TestEventDTO;
import com.persoff68.fatodo.builder.TestEventUser;
import com.persoff68.fatodo.builder.TestItemEvent;
import com.persoff68.fatodo.builder.TestItemEventUser;
import com.persoff68.fatodo.builder.TestReminderEvent;
import com.persoff68.fatodo.builder.event.TestItemGroup;
import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.ChatEventUser;
import com.persoff68.fatodo.model.CommentEvent;
import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.EventUser;
import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.ItemEventUser;
import com.persoff68.fatodo.model.ReminderEvent;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.event.ItemGroup;
import com.persoff68.fatodo.repository.EventRecipientRepository;
import com.persoff68.fatodo.repository.EventRepository;
import com.persoff68.fatodo.repository.ReadStatusRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoEventServiceApplication.class)
@AutoConfigureMockMvc
class EventControllerIT {

    private static final String ENDPOINT = "/api/event";

    private static final String USER_ID_1 = "3c300277-b5ea-48d1-80db-ead620cf5846";
    private static final String USER_ID_2 = "357a2a99-7b7e-4336-9cd7-18f2cf73fab9";
    private static final String GROUP_ID = "b6e39fc4-bcf1-404a-837f-fd2f46925166";
    private static final String ITEM_ID = "bacf52b5-8794-4291-8cb5-1929bd62dc11";
    private static final String CHAT_ID = "f5d73794-5b66-49ce-8f8e-bcb57c02e951";

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
        Event contactEvent = buildContactEvent(USER_ID_1, USER_ID_2);
        Event itemEvent = buildItemEvent(GROUP_ID, ITEM_ID, USER_ID_1);
        Event commentEvent = buildCommentEvent(GROUP_ID, USER_ID_1);
        Event chatEvent = buildChatEvent(CHAT_ID, USER_ID_1);
        Event reminderEvent = buildReminderEvent(GROUP_ID, ITEM_ID, USER_ID_1);
        eventRepository.save(contactEvent);
        eventRepository.save(itemEvent);
        eventRepository.save(commentEvent);
        eventRepository.save(chatEvent);
        eventRepository.save(reminderEvent);
    }

    @AfterEach
    void cleanup() {
        eventRepository.deleteAll();
        eventRecipientRepository.deleteAll();
        readStatusRepository.deleteAll();
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddDefaultWelcomeEvent() throws Exception {
        EventDTO eventDTO = TestEventDTO.defaultBuilder().type(EventType.WELCOME).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddItemGroupEvent() throws Exception {
        ItemGroup itemGroup = TestItemGroup.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(itemGroup);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.ITEM_GROUP_CREATE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }


    private Event buildContactEvent(String firstUserId, String secondUserId) {
        ContactEvent contactEvent = TestContactEvent.defaultBuilder()
                .userId(UUID.fromString(firstUserId))
                .firstUserId(UUID.fromString(firstUserId))
                .secondUserId(UUID.fromString(secondUserId))
                .build().toParent();

        EventUser eventUser = TestEventUser.defaultBuilder()
                .userId(UUID.fromString(firstUserId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.CONTACT_ACCEPT_INCOMING)
                .eventUsers(List.of(eventUser))
                .contactEvent(contactEvent)
                .build().toParent();
        eventUser.setEvent(event);

        contactEvent.setEvent(event);
        return event;
    }

    private Event buildItemEvent(String groupId, String itemId, String userId) {
        ItemEventUser itemEventUser = TestItemEventUser.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        ItemEvent itemEvent = TestItemEvent.defaultBuilder()
                .groupId(UUID.fromString(groupId))
                .itemId(UUID.fromString(itemId))
                .users(List.of(itemEventUser))
                .build().toParent();
        itemEventUser.setItemEvent(itemEvent);

        EventUser eventUser = TestEventUser.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.ITEM_CREATE)
                .eventUsers(List.of(eventUser))
                .itemEvent(itemEvent)
                .build().toParent();
        eventUser.setEvent(event);

        itemEvent.setEvent(event);
        return event;
    }

    private Event buildCommentEvent(String parentId, String userId) {
        CommentEvent commentEvent = TestCommentEvent.defaultBuilder()
                .parentId(UUID.fromString(parentId))
                .build().toParent();

        EventUser eventUser = TestEventUser.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.COMMENT_CREATE)
                .eventUsers(List.of(eventUser))
                .commentEvent(commentEvent)
                .build().toParent();
        eventUser.setEvent(event);

        commentEvent.setEvent(event);
        return event;
    }

    private Event buildChatEvent(String chatId, String userId) {
        ChatEventUser chatEventUser = TestChatEventUser.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        ChatEvent chatEvent = TestChatEvent.defaultBuilder()
                .chatId(UUID.fromString(chatId))
                .users(List.of(chatEventUser)).build().toParent();
        chatEventUser.setChatEvent(chatEvent);

        EventUser eventUser = TestEventUser.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.CHAT_CREATE)
                .eventUsers(List.of(eventUser))
                .chatEvent(chatEvent)
                .build().toParent();
        eventUser.setEvent(event);

        chatEvent.setEvent(event);
        return event;
    }

    private Event buildReminderEvent(String groupId, String itemId, String userId) {
        ReminderEvent reminderEvent = TestReminderEvent.defaultBuilder()
                .groupId(UUID.fromString(groupId))
                .itemId(UUID.fromString(itemId))
                .build().toParent();

        EventUser eventUser = TestEventUser.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.REMINDER)
                .eventUsers(List.of(eventUser))
                .reminderEvent(reminderEvent)
                .build().toParent();
        eventUser.setEvent(event);

        reminderEvent.setEvent(event);
        return event;
    }

}
