package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoEventServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestEventDTO;
import com.persoff68.fatodo.builder.event.TestChat;
import com.persoff68.fatodo.builder.event.TestChatMember;
import com.persoff68.fatodo.builder.event.TestChatReaction;
import com.persoff68.fatodo.builder.event.TestComment;
import com.persoff68.fatodo.builder.event.TestCommentReaction;
import com.persoff68.fatodo.builder.event.TestContactRequest;
import com.persoff68.fatodo.builder.event.TestItem;
import com.persoff68.fatodo.builder.event.TestItemGroup;
import com.persoff68.fatodo.builder.event.TestItemGroupMember;
import com.persoff68.fatodo.builder.event.TestReminderMeta;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.event.Chat;
import com.persoff68.fatodo.model.event.ChatMember;
import com.persoff68.fatodo.model.event.ChatReaction;
import com.persoff68.fatodo.model.event.Comment;
import com.persoff68.fatodo.model.event.CommentReaction;
import com.persoff68.fatodo.model.event.ContactRequest;
import com.persoff68.fatodo.model.event.Item;
import com.persoff68.fatodo.model.event.ItemGroup;
import com.persoff68.fatodo.model.event.ItemGroupMember;
import com.persoff68.fatodo.model.event.ReminderMeta;
import com.persoff68.fatodo.repository.EventRecipientRepository;
import com.persoff68.fatodo.repository.EventRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoEventServiceApplication.class)
@AutoConfigureMockMvc
class EventControllerIT {

    private static final String ENDPOINT = "/api/event";

    @Autowired
    MockMvc mvc;

    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventRecipientRepository eventRecipientRepository;
    @Autowired
    ObjectMapper objectMapper;

    @AfterEach
    void cleanup() {
        eventRepository.deleteAll();
        eventRecipientRepository.deleteAll();
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

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testDeleteItemGroupEvent() throws Exception {
        ItemGroup itemGroup = TestItemGroup.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(itemGroup);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.ITEM_GROUP_DELETE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddItemEvent() throws Exception {
        Item item = TestItem.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(item);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.ITEM_CREATE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testDeleteItemEvent() throws Exception {
        Item item = TestItem.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(item);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.ITEM_DELETE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddItemMemberAddEvent() throws Exception {
        ItemGroupMember itemGroupMember = TestItemGroupMember.defaultBuilder().build().toParent();
        List<ItemGroupMember> itemGroupMemberList = List.of(itemGroupMember);
        String payload = objectMapper.writeValueAsString(itemGroupMemberList);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.ITEM_MEMBER_ADD).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddItemMemberDeleteEvent() throws Exception {
        ItemGroupMember itemGroupMember = TestItemGroupMember.defaultBuilder().build().toParent();
        List<ItemGroupMember> itemGroupMemberList = List.of(itemGroupMember);
        String payload = objectMapper.writeValueAsString(itemGroupMemberList);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.ITEM_MEMBER_DELETE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddItemMemberLeaveEvent() throws Exception {
        ItemGroupMember itemGroupMember = TestItemGroupMember.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(itemGroupMember);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.ITEM_MEMBER_LEAVE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddItemMemberRoleEvent() throws Exception {
        ItemGroupMember itemGroupMember = TestItemGroupMember.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(itemGroupMember);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.ITEM_MEMBER_ROLE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddContactRequestEvent() throws Exception {
        ContactRequest contactRequest = TestContactRequest.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(contactRequest);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.CONTACT_REQUEST).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddContactRequestAcceptEvent() throws Exception {
        ContactRequest contactRequest = TestContactRequest.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(contactRequest);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.CONTACT_ACCEPT).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddContactRequestDeleteEvent() throws Exception {
        ContactRequest contactRequest = TestContactRequest.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(contactRequest);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.CONTACT_DECLINE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testDeleteContactRelationEvent() throws Exception {
        ContactRequest contactRequest = TestContactRequest.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(contactRequest);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.CONTACT_DELETE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddCommentEvent() throws Exception {
        Comment comment = TestComment.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(comment);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.COMMENT_CREATE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddCommentReactionEvent_like() throws Exception {
        CommentReaction commentReaction = TestCommentReaction.defaultBuilder()
                .type(CommentReaction.ReactionType.LIKE).build().toParent();
        String payload = objectMapper.writeValueAsString(commentReaction);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.COMMENT_REACTION_INCOMING).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddCommentReactionEvent_none() throws Exception {
        CommentReaction commentReaction = TestCommentReaction.defaultBuilder()
                .type(CommentReaction.ReactionType.NONE).build().toParent();
        String payload = objectMapper.writeValueAsString(commentReaction);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.COMMENT_REACTION_INCOMING).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddChatEvent() throws Exception {
        Chat chat = TestChat.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(chat);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.CHAT_CREATE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddChatMemberAddEvent() throws Exception {
        ChatMember chatMember = TestChatMember.defaultBuilder().build().toParent();
        List<ChatMember> chatMemberList = List.of(chatMember);
        String payload = objectMapper.writeValueAsString(chatMemberList);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.CHAT_MEMBER_ADD).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddChatMemberDeleteEvent() throws Exception {
        ChatMember chatMember = TestChatMember.defaultBuilder().build().toParent();
        List<ChatMember> chatMemberList = List.of(chatMember);
        String payload = objectMapper.writeValueAsString(chatMemberList);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.CHAT_MEMBER_DELETE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddChatMemberLeaveEvent() throws Exception {
        ChatMember chatMember = TestChatMember.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(chatMember);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.CHAT_MEMBER_LEAVE).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddChatReactionIncomingEvent_like() throws Exception {
        ChatReaction chatReaction = TestChatReaction.defaultBuilder()
                .type(ChatReaction.ReactionType.LIKE).build().toParent();
        String payload = objectMapper.writeValueAsString(chatReaction);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.CHAT_REACTION_INCOMING).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddChatReactionIncomingEvent_none() throws Exception {
        ChatReaction chatReaction = TestChatReaction.defaultBuilder()
                .type(ChatReaction.ReactionType.NONE).build().toParent();
        String payload = objectMapper.writeValueAsString(chatReaction);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.CHAT_REACTION_INCOMING).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }


    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testAddReminderEvent() throws Exception {
        ReminderMeta reminderMeta = TestReminderMeta.defaultBuilder().build().toParent();
        String payload = objectMapper.writeValueAsString(reminderMeta);
        EventDTO eventDTO = TestEventDTO.defaultBuilder()
                .type(EventType.REMINDER).payload(payload).build().toParent();
        String body = objectMapper.writeValueAsString(eventDTO);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

}
