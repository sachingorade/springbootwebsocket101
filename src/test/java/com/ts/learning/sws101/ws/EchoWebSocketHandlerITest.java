package com.ts.learning.sws101.ws;

import com.ts.learning.sws101.AbstractIntegrationTest;
import com.ts.learning.sws101.client.WebSocketClient;
import org.junit.jupiter.api.Test;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EchoWebSocketHandlerITest extends AbstractIntegrationTest {

    @Test
    void testMessageToEchoWebSocketHandler() throws IOException, DeploymentException, InterruptedException {
        WebSocketClient webSocketClient = new WebSocketClient();
        webSocketClient.connect(URI.create("ws://localhost:" + serverPort+ "/echoWs"));
        AtomicBoolean result = new AtomicBoolean(false);
        webSocketClient.setTextMessageListener(message -> {
            assertEquals("hello", message);
            result.set(true);
        });
        webSocketClient.sendMessage("hello");

        // Dirtiest way of having an assertion in the test but as of now this is the one which I came up with :)
        Thread.sleep(200);
        assertTrue(result.get());
    }

}