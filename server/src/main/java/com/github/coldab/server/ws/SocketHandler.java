package com.github.coldab.server.ws;

import com.github.coldab.shared.project.Project;
import com.github.coldab.shared.ws.ClientEndpoint;
import com.github.coldab.shared.ws.MessageEncoder;
import com.github.coldab.shared.ws.ServerEndpoint;
import com.github.tjeukayim.socketinterface.SocketReceiver;
import com.github.tjeukayim.socketinterface.SocketSender;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

  private final HashMap<WebSocketSession, SocketSession> sessions = new HashMap<>();

  private final Project testProject = new Project(77, "Project 77");

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    System.out.println("Connected WebSocket");
    int projectId = (int) session.getAttributes().get("projectId");
    if (projectId != testProject.getId()) {
      session.close(new CloseStatus(1000, "ProjectId not found"));
    }
    // TODO: get project somewhere and create ClientEndpoint
    ServerEndpoint serverEndpoint = SocketSender.create(ServerEndpoint.class,
        socketMessage -> {
          try {
            byte[] payload = MessageEncoder.encodeMessage(socketMessage);
            session.sendMessage(new TextMessage(payload));
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
    ClientEndpoint clientEndpoint = new WebSocketEndpoint(serverEndpoint);
    SocketReceiver socketReceiver = new SocketReceiver(ClientEndpoint.class, clientEndpoint);
    sessions.put(session, new SocketSession(socketReceiver));
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) {
    byte[] bytes = message.asBytes();
    Optional.ofNullable(sessions.get(session)).ifPresent(
        socketSession -> MessageEncoder.decodeMessage(bytes, socketSession.socketReceiver));
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    sessions.remove(session);
  }

  private class SocketSession {

    final SocketReceiver socketReceiver;

    private SocketSession(SocketReceiver socketReceiver) {
      this.socketReceiver = socketReceiver;
    }
  }

}
