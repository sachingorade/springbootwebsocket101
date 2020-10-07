package com.ts.learning.sws101.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ts.learning.sws101.model.simplechat.ActiveUsersListMessage;
import com.ts.learning.sws101.model.simplechat.client.AbstractClientMessage;
import com.ts.learning.sws101.model.simplechat.client.GetActiveUsersMessage;
import com.ts.learning.sws101.model.simplechat.client.JoinMessage;
import com.ts.learning.sws101.model.simplechat.client.LeaveMessage;
import com.ts.learning.sws101.model.simplechat.SimpleFromMessage;
import com.ts.learning.sws101.model.simplechat.client.SimpleToMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SimpleWebSocketChatHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(SimpleWebSocketChatHandler.class);

    private static final String KEY_USER_NAME = "userName";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, WebSocketSession> sessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        logger.info("Web socket session established :[{}]", webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        AbstractClientMessage message = parseMessage(webSocketMessage.getPayload().toString());
        if (message instanceof JoinMessage) {
            webSocketSession.getAttributes().put(KEY_USER_NAME, ((JoinMessage) message).getUserName());
            sessionMap.put(((JoinMessage) message).getUserName(), webSocketSession);
        } else if (message instanceof LeaveMessage) {
            String userName = (String) webSocketSession.getAttributes().get(KEY_USER_NAME);
            Optional.ofNullable(sessionMap.remove(userName)).ifPresent(this::closeSession);
        } else if (message instanceof SimpleToMessage) {
            Optional<String> optionalUser = Optional.ofNullable((String) webSocketSession.getAttributes().get(KEY_USER_NAME));
            optionalUser.ifPresent(fromUser -> sendMessageFromUser(fromUser, (SimpleToMessage) message));
        } else if (message instanceof GetActiveUsersMessage) {
            // user must be active first
            Optional<String> optionalActiveUser = Optional.ofNullable((String) webSocketSession.getAttributes().get(KEY_USER_NAME));
            optionalActiveUser.ifPresent(this::sendActiveUsersList);
        }
    }

    private void sendActiveUsersList(String toUser) {
        Optional<WebSocketSession> webSocketSession = Optional.ofNullable(sessionMap.get(toUser));
        webSocketSession.ifPresent(session -> {
            try {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(new ActiveUsersListMessage(sessionMap.keySet()))));
            } catch (IOException e) {
                logger.error("Error while sending active users list to session:[{}]", session);
            }
        });
    }

    private void sendMessageFromUser(String fromUser, SimpleToMessage message) {
        Optional<WebSocketSession> targetSession = Optional.ofNullable(sessionMap.get(message.getToUser()));
        if (targetSession.isPresent()) {
            // if the user is online!
            try {
                targetSession.get().sendMessage(new TextMessage(objectMapper.writeValueAsBytes(new SimpleFromMessage(fromUser, message.getMessage()))));
            } catch (IOException e) {
                logger.error("Error while sending message to session:[{}]", targetSession.get(), e);
            }
        } else {
            // TODO store user message if not online? :P
        }
    }

    private void closeSession(WebSocketSession session) {
        try {
            session.close(CloseStatus.NORMAL);
        } catch (IOException e) {
            logger.error("Error while closing web socket session: [{}]", session, e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
    }

    public WebSocketSession getSessionForUser(String user) {
        return sessionMap.get(user);
    }

    private AbstractClientMessage parseMessage(String payload) throws JsonProcessingException {
        return objectMapper.readValue(payload, AbstractClientMessage.class);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
