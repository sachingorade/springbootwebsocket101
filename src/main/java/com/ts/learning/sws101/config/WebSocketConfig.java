package com.ts.learning.sws101.config;

import com.ts.learning.sws101.ws.EchoWebSocketHandler;
import com.ts.learning.sws101.ws.SimpleWebSocketChatHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(echoHandler(), "/echoWs");
        webSocketHandlerRegistry.addHandler(simpleWebSocketChatHandler(), "/swsc")
                .setAllowedOrigins("*");
    }

    private WebSocketHandler echoHandler() {
        return new EchoWebSocketHandler();
    }

    private WebSocketHandler simpleWebSocketChatHandler() {
        return new SimpleWebSocketChatHandler();
    }

}
