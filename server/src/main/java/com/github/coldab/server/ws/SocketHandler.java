package com.github.coldab.server.ws;

import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.MessageEncoder;
import com.github.coldab.shared.ws.ServerEndpoint;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.github.tjeukayim.socketinterface.SocketSender;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
  private final Map<Integer, ProjectClients> projects = new HashMap<>();

  public SocketHandler() {
    projects.put(77, new ProjectClients(new Project(77, "Project 77")));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    System.out.println("Connected WebSocket");
    int projectId = (int) session.getAttributes().get("projectId");
    if (!projects.keySet().contains(projectId)) {
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
    ProjectClients projectClients = projects.get(projectId);
    ServerEndpoint serverEndpoint = new WebSocketEndpoint(clientEndpoint, projectClients.clients);
    SocketReceiver socketReceiver = new SocketReceiver(ServerEndpoint.class, serverEndpoint);
    sessions.put(session, new SocketSession(socketReceiver, clientEndpoint));
    projectClients.clients.add(clientEndpoint);
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
    projects.remove(socketSession.clientEndpoint);
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

  private class ProjectClients {
    final Project project;
    final List<ClientEndpoint> clients = new ArrayList<>();

    private ProjectClients(Project project) {
      this.project = project;
    }
  }
}
