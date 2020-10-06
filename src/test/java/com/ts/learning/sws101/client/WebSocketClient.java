package com.ts.learning.sws101.client;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class WebSocketClient {

    private Session session;
    private MessageHandler messageHandler;
    private WebSocketContainer container;
    private TextMessageListener textMessageListener;

    public WebSocketClient() {
        container = ContainerProvider.getWebSocketContainer();
    }

    public void connect(URI serverUri) throws IOException, DeploymentException {
        container.connectToServer(this, serverUri);
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        textMessageListener.onMessage(message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
    }

    public void setTextMessageListener(TextMessageListener textMessageListener) {
        this.textMessageListener = textMessageListener;
    }

    public void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }

}
