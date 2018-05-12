package com.github.coldab.server.ws;

import com.github.coldab.server.dal.ProjectStore;
import com.github.coldab.shared.ws.ChatServer;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.MessageEncoder;
import com.github.coldab.shared.ws.ProjectServer;
import com.github.coldab.shared.ws.ServerEndpoint;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.github.tjeukayim.socketinterface.SocketSender;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

  private final Map<WebSocketSession, SocketSession> sessions = new HashMap<>();
  private final Map<Integer, ProjectService> projects = new HashMap<>();
  private final ProjectStore projectStore;

  public SocketHandler(ProjectStore projectStore) {
    this.projectStore = projectStore;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    System.out.println("Connected WebSocket");
    int projectId = (int) session.getAttributes().get("projectId");
    ProjectService projectService = getProject(projectId);
    if (projectService == null) {
      session.close(new CloseStatus(1000, "ProjectId not found"));
    }
    // TODO: get project somewhere and create ClientEndpoint
    ClientEndpoint clientEndpoint = SocketSender.create(ClientEndpoint.class,
        socketMessage -> {
          try {
            byte[] payload = MessageEncoder.encodeMessage(socketMessage);
            session.sendMessage(new TextMessage(payload));
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
    ProjectServer projectServer = projectService.addClient(clientEndpoint);
    ChatServer chatServer = new ChatService(projectService.getClients(), clientEndpoint.chat());
    ServerEndpoint serverEndpoint =
        new WebSocketEndpoint(chatServer, projectServer);
    SocketReceiver socketReceiver = new SocketReceiver(ServerEndpoint.class, serverEndpoint);
    sessions.put(session, new SocketSession(socketReceiver, clientEndpoint));
  }

  /**
   * Get project from database, and construct a service.
   * @return null if project doesn't exist
   */
  private ProjectService getProject(int projectId) {
    ProjectService service = projects.get(projectId);
    if (service == null) {
      // Load from database
      service = projectStore.findById(projectId)
          .map(ProjectService::new).orElse(null);
      if (service != null) {
        projects.put(projectId, service);
      }
    }
    return service;
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
    for (ProjectService projectService : projects.values()) {
      projectService.removeClient(socketSession.clientEndpoint);
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
