package com.github.coldab.server.ws;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import com.github.coldab.server.Main;
import com.github.coldab.server.dal.AccountStore;
import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.server.services.LoginSessionManager;
import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.chat.ChatMessage;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ChatClient;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.MessageEncoder;
import com.github.coldab.shared.ws.ProjectClient;
import com.github.coldab.shared.ws.ServerEndpoint;
import com.github.tjeukayim.socketinterface.SocketMessage;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.github.tjeukayim.socketinterface.SocketSender;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private ProjectStore projectStore;

  @Autowired
  private AccountStore accountStore;

  @Autowired
  private LoginSessionManager loginSessionManager;

  private Account account = new Account("Bob", "bob@email", "I'm a dummy");
  private Client client = new Client();
  private WebSocketConnectionManager connectionManager;

  @Before
  public void setUp() {
    // Save dummy account
    accountStore.save(account);
    // Add to project
    Project project = new Project("Dummy Project");
    project.getAdmins().add(account);
    projectStore.save(project);
    // Create session for user
    loginSessionManager.login(account);
    // Start WebSocket connection
    String url = String.format("ws://localhost:%d/ws/" + project.getId(), port);
    connectionManager = new WebSocketConnectionManager(new StandardWebSocketClient(), client, url);
    connectionManager.getHeaders().add("Session", account.getSessionId());
    connectionManager.start();
  }

  @After
  public void tearDown() {
    connectionManager.stop();
  }

  @Test
  public void connect() {
    verify(client.chatMock, timeout(9000)).message(client.chatMessage);
  }

  private class Client extends TextWebSocketHandler implements ClientEndpoint {

    private ServerEndpoint serverEndpoint;
    private SocketReceiver socketReceiver;
    private WebSocketSession session;
    private final ChatClient chatMock = Mockito.mock(ChatClient.class);
    private ProjectClient projectMock = Mockito.mock(ProjectClient.class);
    private ChatMessage chatMessage =
        new ChatMessage("Hello World", account);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
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
      return projectMock;
    }

  }
}