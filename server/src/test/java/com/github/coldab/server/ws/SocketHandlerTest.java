package com.github.coldab.server.ws;

import static org.junit.Assert.assertEquals;

import com.github.coldab.server.Main;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.ws.ChatClient;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.MessageEncoder;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ServerEndpoint;
import com.github.tjeukayim.socketinterface.SocketMessage;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.github.tjeukayim.socketinterface.SocketSender;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Integration test for WebSocket-connection.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
public class SocketHandlerTest {
  @LocalServerPort
  private int port;

  private Client client = new Client();
  private WebSocketConnectionManager connectionManager;

  @Before
  public void setUp() throws Exception {
    String url = String.format("ws://localhost:%d/ws/1", port);
    connectionManager = new WebSocketConnectionManager(new StandardWebSocketClient(), client, url);
    connectionManager.start();
  }

  @After
  public void tearDown() throws Exception {
    connectionManager.stop();
  }

  @Test
  public void connect() throws InterruptedException {
    ChatMessage message = client.chatMock.messages.poll(20, TimeUnit.SECONDS);
    ChatMessage result = client.chatMessage;
    assertEquals(message.getText(), result.getText());
  }

  private static class Client extends TextWebSocketHandler implements ClientEndpoint {

    private ServerEndpoint serverEndpoint;
    private SocketReceiver socketReceiver;
    private WebSocketSession session;
    private final ChatClientMock chatMock = new ChatClientMock();
    private ChatMessage chatMessage =
        new ChatMessage("Hello World", new Account("HenkJan", "henk@jan.org"));

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
      this.session = session;
      serverEndpoint = SocketSender.create(ServerEndpoint.class, this::sendMessage);
      socketReceiver = new SocketReceiver(ClientEndpoint.class, this);
      serverEndpoint.chat().message(chatMessage);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
      MessageEncoder.decodeMessage(message.asBytes(), socketReceiver);
    }

    private void sendMessage(SocketMessage socketMessage) {
      try {
        session.sendMessage(new TextMessage(MessageEncoder.encodeMessage(socketMessage)));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    public ChatClient chat() {
      return chatMock;
    }

    @Override
    public ProjectClient project() {
      return Mockito.mock(ProjectClient.class);
    }

  }
}