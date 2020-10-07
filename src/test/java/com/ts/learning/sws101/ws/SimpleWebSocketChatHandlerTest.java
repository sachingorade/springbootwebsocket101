package com.ts.learning.sws101.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ts.learning.sws101.model.simplechat.ActiveUsersListMessage;
import com.ts.learning.sws101.model.simplechat.SimpleFromMessage;
import com.ts.learning.sws101.model.simplechat.client.GetActiveUsersMessage;
import com.ts.learning.sws101.model.simplechat.client.JoinMessage;
import com.ts.learning.sws101.model.simplechat.client.LeaveMessage;
import com.ts.learning.sws101.model.simplechat.client.SimpleToMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleWebSocketChatHandlerTest {

    @InjectMocks
    private SimpleWebSocketChatHandler simpleWebSocketChatHandler;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testJoinMessage() throws Exception {
        WebSocketSession session = getMockSession();

        simpleWebSocketChatHandler.handleMessage(session, getTestJoinMessage("testUser"));

        assertSame(session, simpleWebSocketChatHandler.getSessionForUser("testUser"));
    }

    private WebSocketSession getMockSession() {
        Map<String, Object> attrMap = new HashMap<>();
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.getAttributes()).thenReturn(attrMap);
        return session;
    }

    private WebSocketMessage<?> getTestJoinMessage(String userName) throws JsonProcessingException {
        return new TextMessage(objectMapper.writeValueAsString(new JoinMessage(userName)));
    }

    @Test
    public void testLeaveMessage() throws Exception {
        WebSocketSession session = getMockSession();

        simpleWebSocketChatHandler.handleMessage(session, getTestJoinMessage("testUser"));
        assertNotNull(simpleWebSocketChatHandler.getSessionForUser("testUser"));

        simpleWebSocketChatHandler.handleMessage(session, getTestLeaveMessage());
        assertNull(simpleWebSocketChatHandler.getSessionForUser("testUser"));
    }

    private WebSocketMessage<?> getTestLeaveMessage() throws JsonProcessingException {
        return new TextMessage(objectMapper.writeValueAsBytes(new LeaveMessage()));
    }

    @Test
    public void testSendMessage() throws Exception {
        WebSocketSession aSession = getMockSession();
        WebSocketSession bSession = getMockSession();

        simpleWebSocketChatHandler.handleMessage(aSession, getTestJoinMessage("aUser"));
        simpleWebSocketChatHandler.handleMessage(bSession, getTestJoinMessage("bUser"));

        simpleWebSocketChatHandler.handleMessage(aSession, getTestSimpleToMessage("bUser", "hello"));
        simpleWebSocketChatHandler.handleMessage(bSession, getTestSimpleToMessage("aUser", "hi"));

        ArgumentCaptor<WebSocketMessage> messageArgumentCaptor = ArgumentCaptor.forClass(WebSocketMessage.class);
        verify(bSession).sendMessage(messageArgumentCaptor.capture());
        verify(aSession).sendMessage(messageArgumentCaptor.capture());

        assertFromMessage(messageArgumentCaptor.getAllValues().get(0).getPayload().toString(), "aUser", "hello");
        assertFromMessage(messageArgumentCaptor.getAllValues().get(1).getPayload().toString(), "bUser", "hi");
    }

    private WebSocketMessage<?> getTestSimpleToMessage(String toUser, String message) throws JsonProcessingException {
        return new TextMessage(objectMapper.writeValueAsBytes(new SimpleToMessage(toUser, message)));
    }

    private void assertFromMessage(String payload, String expectedUser, String expectedMsg) throws JsonProcessingException {
        SimpleFromMessage fromMessage = objectMapper.readValue(payload, SimpleFromMessage.class);
        assertEquals(expectedUser, fromMessage.getFromUser());
        assertEquals(expectedMsg, fromMessage.getMessage());
    }

    @Test
    void testGetActiveUsersMessage() throws Exception {
        WebSocketSession aSession = getMockSession();
        WebSocketSession bSession = getMockSession();

        simpleWebSocketChatHandler.handleMessage(aSession, getTestJoinMessage("a"));
        simpleWebSocketChatHandler.handleMessage(aSession, getActiveUsersMessage());

        ArgumentCaptor<WebSocketMessage> webSocketMessageArgumentCaptor = ArgumentCaptor.forClass(WebSocketMessage.class);
        verify(aSession).sendMessage(webSocketMessageArgumentCaptor.capture());

        simpleWebSocketChatHandler.handleMessage(bSession, getTestJoinMessage("b"));
        simpleWebSocketChatHandler.handleMessage(bSession, getActiveUsersMessage());
        verify(bSession).sendMessage(webSocketMessageArgumentCaptor.capture());

        assertActiveUsers(webSocketMessageArgumentCaptor.getAllValues().get(0).getPayload().toString(), Collections.singletonList("a"));
        assertActiveUsers(webSocketMessageArgumentCaptor.getAllValues().get(1).getPayload().toString(), Arrays.asList("a", "b"));
    }

    private WebSocketMessage<?> getActiveUsersMessage() throws JsonProcessingException {
        return new TextMessage(objectMapper.writeValueAsBytes(new GetActiveUsersMessage()));
    }

    private void assertActiveUsers(String payload, List<String> activeUsersList) throws JsonProcessingException {
        ActiveUsersListMessage activeUsersListMessage = objectMapper.readValue(payload, ActiveUsersListMessage.class);
        assertListContainsAll(activeUsersListMessage.getActiveUsers(), activeUsersList);
    }

    private void assertListContainsAll(List<String> activeUsersFromMsg, List<String> activeUsersList) {
        assertTrue(activeUsersFromMsg.containsAll(activeUsersList));
    }

}