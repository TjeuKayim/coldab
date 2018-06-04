package com.github.coldab.server.ws;

import com.github.coldab.shared.account.Account;
import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.MessageEncoder;
import com.github.coldab.shared.ws.ServerEndpoint;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.github.tjeukayim.socketinterface.SocketSender;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

  private final Map<WebSocketSession, SocketSession> sessions = new HashMap<>();
  private final ConnectionManager connectionManager;

  private static final Logger LOGGER = Logger.getLogger(SocketHandler.class.getName());

  public SocketHandler(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    // TODO: logged in account
    Account account = new Account("HenkJan", "henk@jan.org");
    int projectId = (int) session.getAttributes().get("projectId");
    Project project = connectionManager.getProject(projectId);
    if (project == null) {
      LOGGER.info("ProjectId not found");
      session.close(new CloseStatus(1000, "ProjectId not found"));
      return;
    }
    ClientEndpoint clientEndpoint = SocketSender.create(ClientEndpoint.class,
        socketMessage -> {
          try {
            byte[] payload = MessageEncoder.encodeMessage(socketMessage);
            session.sendMessage(new TextMessage(payload));
          } catch (IOException e) {
            LOGGER.severe(e.toString());
          }
        });
    ServerEndpoint serverEndpoint = connectionManager.connect(project, account, clientEndpoint);
    SocketReceiver socketReceiver = new SocketReceiver(ServerEndpoint.class, serverEndpoint);
    sessions.put(session, new SocketSession(socketReceiver, clientEndpoint));
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    byte[] bytes = message.asBytes();
    Optional.ofNullable(sessions.get(session)).ifPresent(
        socketSession -> MessageEncoder.decodeMessage(bytes, socketSession.socketReceiver));
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    SocketSession socketSession = sessions.remove(session);
    if (socketSession != null) {
      connectionManager.disconnect(socketSession.clientEndpoint);
    }
  }

  private class SocketSession {

    final SocketReceiver socketReceiver;
    final ClientEndpoint clientEndpoint;

    private SocketSession(SocketReceiver socketReceiver,
        ClientEndpoint clientEndpoint) {
      this.socketReceiver = socketReceiver;
      this.clientEndpoint = clientEndpoint;
    }
  }

}
