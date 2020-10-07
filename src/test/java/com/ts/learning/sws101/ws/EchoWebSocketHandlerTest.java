package com.ts.learning.sws101.ws;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EchoWebSocketHandlerTest {

    @InjectMocks
    private EchoWebSocketHandler echoWebSocketHandler;

    @Test
    void testHandleMessageEcho() throws Exception {
        // mock setup
        WebSocketSession webSocketSession = mock(WebSocketSession.class);
        WebSocketMessage<?> webSocketMessage1 = new TextMessage("hello");
        WebSocketMessage<?> webSocketMessage2 = new TextMessage("again");

        // invocation
        echoWebSocketHandler.handleMessage(webSocketSession, webSocketMessage1);
        echoWebSocketHandler.handleMessage(webSocketSession, webSocketMessage2);

        // expectation
        ArgumentCaptor<WebSocketMessage> webSocketMessageArgumentCaptor = ArgumentCaptor.forClass(WebSocketMessage.class);
        verify(webSocketSession, times(2)).sendMessage(webSocketMessageArgumentCaptor.capture());

        assertEquals("hello", webSocketMessageArgumentCaptor.getAllValues().get(0).getPayload());
        assertEquals("again", webSocketMessageArgumentCaptor.getAllValues().get(1).getPayload());
    }

}