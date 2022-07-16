package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoEventServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestChatEvent;
import com.persoff68.fatodo.builder.TestChatEventUser;
import com.persoff68.fatodo.builder.TestCommentEvent;
import com.persoff68.fatodo.builder.TestContactEvent;
import com.persoff68.fatodo.builder.TestCreateChatEventDTO;
import com.persoff68.fatodo.builder.TestCreateCommentEventDTO;
import com.persoff68.fatodo.builder.TestCreateContactEventDTO;
import com.persoff68.fatodo.builder.TestCreateEventDTO;
import com.persoff68.fatodo.builder.TestCreateItemEventDTO;
import com.persoff68.fatodo.builder.TestCreateReminderEventDTO;
import com.persoff68.fatodo.builder.TestEvent;
import com.persoff68.fatodo.builder.TestEventRecipient;
import com.persoff68.fatodo.builder.TestItemEvent;
import com.persoff68.fatodo.builder.TestItemEventUser;
import com.persoff68.fatodo.builder.TestReminderEvent;
import com.persoff68.fatodo.model.ChatEvent;
import com.persoff68.fatodo.model.ChatEventUser;
import com.persoff68.fatodo.model.CommentEvent;
import com.persoff68.fatodo.model.ContactEvent;
import com.persoff68.fatodo.model.Event;
import com.persoff68.fatodo.model.EventRecipient;
import com.persoff68.fatodo.model.ItemEvent;
import com.persoff68.fatodo.model.ItemEventUser;
import com.persoff68.fatodo.model.ReminderEvent;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.create.CreateChatEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateCommentEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateContactEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateItemEventDTO;
import com.persoff68.fatodo.model.dto.create.CreateReminderEventDTO;
import com.persoff68.fatodo.model.dto.delete.DeleteContactEventsDTO;
import com.persoff68.fatodo.model.dto.delete.DeleteEventsDTO;
import com.persoff68.fatodo.model.dto.delete.DeleteUserEventsDTO;
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
        eventRepository.deleteAll();
        eventRecipientRepository.deleteAll();
        readStatusRepository.deleteAll();

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

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    @Transactional
    void testAddDefaultEvent_ok() throws Exception {
        String url = ENDPOINT + "/default";
        CreateEventDTO dto = TestCreateEventDTO.defaultBuilder()
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
        CreateEventDTO dto = TestCreateEventDTO.defaultBuilder()
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
        CreateEventDTO dto = TestCreateEventDTO.defaultBuilder()
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
        CreateEventDTO dto = TestCreateEventDTO.defaultBuilder()
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
        CreateContactEventDTO dto = TestCreateContactEventDTO.defaultBuilder()
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
        CreateContactEventDTO dto = TestCreateContactEventDTO.defaultBuilder()
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
        CreateItemEventDTO dto = TestCreateItemEventDTO.defaultBuilder()
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
        CreateItemEventDTO dto = TestCreateItemEventDTO.defaultBuilder()
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
        CreateCommentEventDTO dto = TestCreateCommentEventDTO.defaultBuilder()
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
        CreateCommentEventDTO dto = TestCreateCommentEventDTO.defaultBuilder()
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
        CreateChatEventDTO dto = TestCreateChatEventDTO.defaultBuilder()
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
        CreateChatEventDTO dto = TestCreateChatEventDTO.defaultBuilder()
                .eventType(EventType.CHAT_CREATE)
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
    void testAddReminderEvent_ok() throws Exception {
        String url = ENDPOINT + "/reminder";
        CreateReminderEventDTO dto = TestCreateReminderEventDTO.defaultBuilder()
                .eventType(EventType.REMINDER)
                .recipientIds(List.of(UUID.fromString(USER_ID_2))).build().toParent();
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        Page<Event> eventPage = eventRepository.findAllByUserId(UUID.fromString(USER_ID_2), Pageable.unpaged());
        assertThat(eventPage).hasSize(1);

        Event event = eventPage.getContent().get(0);
        assertThat(event.getType()).isEqualTo(EventType.REMINDER);
    }

    @Test
    @WithAnonymousUser
    void testAddReminderEvent_unauthorized() throws Exception {
        String url = ENDPOINT + "/reminder";
        CreateReminderEventDTO dto = TestCreateReminderEventDTO.defaultBuilder()
                .eventType(EventType.REMINDER)
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
    void testDeleteGroupEventsForUsers_ok() throws Exception {
        String url = ENDPOINT + "/group/delete-users";
        DeleteUserEventsDTO dto = new DeleteUserEventsDTO(UUID.fromString(GROUP_ID),
                List.of(UUID.fromString(USER_ID_1)));
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        Page<Event> eventPage = eventRepository.findAllByUserId(UUID.fromString(USER_ID_1), Pageable.unpaged());
        List<Event> eventList = eventPage.getContent();
        boolean groupEventsPresented = eventList.stream()
                .anyMatch(e -> e.getType().isItemEvent()
                        && e.getItemEvent().getGroupId().equals(UUID.fromString(GROUP_ID)));
        boolean commentEventsPresented = eventList.stream()
                .anyMatch(e -> e.getType().isCommentEvent()
                        && e.getCommentEvent().getParentId().equals(UUID.fromString(GROUP_ID)));
        boolean reminderEventsPresented = eventList.stream()
                .anyMatch(e -> e.getType().isReminderEvent()
                        && e.getReminderEvent().getGroupId().equals(UUID.fromString(GROUP_ID)));

        assertThat(eventList).isNotEmpty();
        assertThat(groupEventsPresented).isFalse();
        assertThat(commentEventsPresented).isFalse();
        assertThat(reminderEventsPresented).isFalse();
    }

    @Test
    @WithAnonymousUser
    void testDeleteGroupEventsForUsers_unauthorized() throws Exception {
        String url = ENDPOINT + "/group/delete-users";
        DeleteUserEventsDTO dto = new DeleteUserEventsDTO(UUID.fromString(GROUP_ID),
                List.of(UUID.fromString(USER_ID_1)));
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    @Transactional
    void testDeleteChatEventsForUsers_ok() throws Exception {
        String url = ENDPOINT + "/chat/delete-users";
        DeleteUserEventsDTO dto = new DeleteUserEventsDTO(UUID.fromString(CHAT_ID),
                List.of(UUID.fromString(USER_ID_1)));
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        Page<Event> eventPage = eventRepository.findAllByUserId(UUID.fromString(USER_ID_1), Pageable.unpaged());
        List<Event> eventList = eventPage.getContent();
        boolean chatEventsPresented = eventList.stream()
                .anyMatch(e -> e.getType().isChatEvent()
                        && e.getChatEvent().getChatId().equals(UUID.fromString(CHAT_ID)));

        assertThat(eventList).isNotEmpty();
        assertThat(chatEventsPresented).isFalse();
    }

    @Test
    @WithAnonymousUser
    void testDeleteChatEventsForUsers_unauthorized() throws Exception {
        String url = ENDPOINT + "/chat/delete-users";
        DeleteUserEventsDTO dto = new DeleteUserEventsDTO(UUID.fromString(CHAT_ID),
                List.of(UUID.fromString(USER_ID_1)));
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    @Transactional
    void testDeleteContactEvents_ok() throws Exception {
        String url = ENDPOINT + "/contact/delete";
        UUID USER_1 = UUID.fromString(USER_ID_1);
        UUID USER_2 = UUID.fromString(USER_ID_2);
        DeleteContactEventsDTO dto = new DeleteContactEventsDTO(USER_1, USER_2);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        List<Event> eventList = eventRepository.findAll();
        boolean contactEventsPresented = eventList.stream()
                .anyMatch(e -> {
                    boolean isContactEvent = e.getType().isContactEvent();
                    UUID firstUserId = isContactEvent ? e.getContactEvent().getFirstUserId() : null;
                    UUID secondUserId = isContactEvent ? e.getContactEvent().getSecondUserId() : null;
                    return isContactEvent && firstUserId.equals(USER_1) && (secondUserId.equals(USER_2)
                            || firstUserId.equals(USER_2) && secondUserId.equals(USER_1));
                });

        assertThat(eventList).isNotEmpty();
        assertThat(contactEventsPresented).isFalse();
    }

    @Test
    @WithAnonymousUser
    void testDeleteContactEvents_unauthorized() throws Exception {
        String url = ENDPOINT + "/contact/delete";
        UUID USER_1 = UUID.fromString(USER_ID_1);
        UUID USER_2 = UUID.fromString(USER_ID_2);
        DeleteContactEventsDTO dto = new DeleteContactEventsDTO(USER_1, USER_2);
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    @Transactional
    void testDeleteGroupEvents_ok() throws Exception {
        String url = ENDPOINT + "/group/delete";
        DeleteEventsDTO dto = new DeleteEventsDTO(UUID.fromString(GROUP_ID));
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        List<Event> eventList = eventRepository.findAll();
        boolean itemEventsPresented = eventList.stream()
                .anyMatch(e -> e.getType().isItemEvent() && e.getItemEvent().getGroupId().equals(UUID.fromString(GROUP_ID)));
        boolean commentEventsPresented = eventList.stream()
                .anyMatch(e -> e.getType().isCommentEvent() && e.getCommentEvent().getParentId().equals(UUID.fromString(GROUP_ID)));
        boolean reminderEventsPresented = eventList.stream()
                .anyMatch(e -> e.getType().isReminderEvent() && e.getReminderEvent().getGroupId().equals(UUID.fromString(GROUP_ID)));

        assertThat(eventList).isNotEmpty();
        assertThat(itemEventsPresented).isFalse();
        assertThat(commentEventsPresented).isFalse();
        assertThat(reminderEventsPresented).isFalse();
    }

    @Test
    @WithAnonymousUser
    void testDeleteGroupEvents_unauthorized() throws Exception {
        String url = ENDPOINT + "/group/delete";
        DeleteEventsDTO dto = new DeleteEventsDTO(UUID.fromString(GROUP_ID));
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    @Transactional
    void testDeleteItemEvents_ok() throws Exception {
        String url = ENDPOINT + "/item/delete";
        DeleteEventsDTO dto = new DeleteEventsDTO(UUID.fromString(ITEM_ID));
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        List<Event> eventList = eventRepository.findAll();
        boolean itemEventsPresented = eventList.stream()
                .anyMatch(e -> e.getType().isItemEvent() && e.getItemEvent().getItemId().equals(UUID.fromString(ITEM_ID)));
        boolean commentEventsPresented = eventList.stream()
                .anyMatch(e -> e.getType().isCommentEvent() && e.getCommentEvent().getTargetId().equals(UUID.fromString(ITEM_ID)));
        boolean reminderEventsPresented = eventList.stream()
                .anyMatch(e -> e.getType().isReminderEvent() && e.getReminderEvent().getItemId().equals(UUID.fromString(ITEM_ID)));

        assertThat(eventList).isNotEmpty();
        assertThat(itemEventsPresented).isFalse();
        assertThat(commentEventsPresented).isFalse();
        assertThat(reminderEventsPresented).isFalse();
    }

    @Test
    @WithAnonymousUser
    void testDeleteItemEvents_unauthorized() throws Exception {
        String url = ENDPOINT + "/item/delete";
        DeleteEventsDTO dto = new DeleteEventsDTO(UUID.fromString(ITEM_ID));
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    @Transactional
    void testDeleteChatEvents_ok() throws Exception {
        String url = ENDPOINT + "/chat/delete";
        DeleteEventsDTO dto = new DeleteEventsDTO(UUID.fromString(CHAT_ID));
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        List<Event> eventList = eventRepository.findAll();
        boolean chatEventsPresented = eventList.stream()
                .anyMatch(e -> e.getType().isChatEvent() && e.getChatEvent().getChatId().equals(UUID.fromString(CHAT_ID)));

        assertThat(eventList).isNotEmpty();
        assertThat(chatEventsPresented).isFalse();
    }

    @Test
    @WithAnonymousUser
    void testDeleteChatEvents_unauthorized() throws Exception {
        String url = ENDPOINT + "/chat/delete";
        DeleteEventsDTO dto = new DeleteEventsDTO(UUID.fromString(CHAT_ID));
        String requestBody = objectMapper.writeValueAsString(dto);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }


    private Event buildContactEvent(String firstUserId, String secondUserId) {
        ContactEvent contactEvent = TestContactEvent.defaultBuilder()
                .firstUserId(UUID.fromString(firstUserId)).secondUserId(UUID.fromString(secondUserId)).build().toParent();

        EventRecipient eventRecipient = TestEventRecipient.defaultBuilder()
                .userId(UUID.fromString(firstUserId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.CONTACT_ACCEPT).eventRecipients(List.of(eventRecipient)).contactEvent(contactEvent)
                .build().toParent();
        eventRecipient.setEvent(event);

        contactEvent.setEvent(event);
        return event;
    }

    private Event buildItemEvent(String groupId, String itemId, String userId) {
        ItemEventUser itemEventUser = TestItemEventUser.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        ItemEvent itemEvent = TestItemEvent.defaultBuilder()
                .groupId(UUID.fromString(groupId))
                .itemId(UUID.fromString(itemId))
                .users(List.of(itemEventUser)).build().toParent();
        itemEventUser.setItemEvent(itemEvent);

        EventRecipient eventRecipient = TestEventRecipient.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.ITEM_CREATE)
                .eventRecipients(List.of(eventRecipient)).itemEvent(itemEvent)
                .build().toParent();
        eventRecipient.setEvent(event);

        itemEvent.setEvent(event);
        return event;
    }

    private Event buildCommentEvent(String parentId, String userId) {
        CommentEvent commentEvent = TestCommentEvent.defaultBuilder()
                .parentId(UUID.fromString(parentId))
                .build().toParent();

        EventRecipient eventRecipient = TestEventRecipient.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.COMMENT_ADD).eventRecipients(List.of(eventRecipient)).commentEvent(commentEvent)
                .build().toParent();
        eventRecipient.setEvent(event);

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

        EventRecipient eventRecipient = TestEventRecipient.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.CHAT_CREATE).eventRecipients(List.of(eventRecipient)).chatEvent(chatEvent)
                .build().toParent();
        eventRecipient.setEvent(event);

        chatEvent.setEvent(event);
        return event;
    }

    private Event buildReminderEvent(String groupId, String itemId, String userId) {
        ReminderEvent reminderEvent = TestReminderEvent.defaultBuilder()
                .groupId(UUID.fromString(groupId))
                .itemId(UUID.fromString(itemId))
                .build().toParent();

        EventRecipient eventRecipient = TestEventRecipient.defaultBuilder()
                .userId(UUID.fromString(userId)).build().toParent();
        Event event = TestEvent.defaultBuilder()
                .type(EventType.REMINDER)
                .eventRecipients(List.of(eventRecipient)).reminderEvent(reminderEvent)
                .build().toParent();
        eventRecipient.setEvent(event);

        reminderEvent.setEvent(event);
        return event;
    }

}
