package com.github.coldab.server.ws;

import com.github.coldab.server.services.LoginSessionManager;
import com.github.coldab.shared.account.Account;
import java.util.Map;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final SocketHandler webSocketHandler;
  private final LoginSessionManager loginSessionManager;

  public WebSocketConfig(SocketHandler webSocketHandler,
      LoginSessionManager loginSessionManager) {
    this.webSocketHandler = webSocketHandler;
    this.loginSessionManager = loginSessionManager;
  }

  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(webSocketHandler, "/ws/*")
        .addInterceptors(new Interceptor())
        .setAllowedOrigins("*");
  }

  /**
   * Intercept the path parameter "projectId".
   */
  private class Interceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Map<String, Object> attributes) {
      // Authorize
      String sessionId = request.getHeaders().getFirst("Session");
      if (sessionId == null) {
        return false;
      }
      Account account = loginSessionManager.validateSessionId(sessionId);
      if (account == null) {
        return false;
      }
      attributes.put("account", account);
      // ProjectId
      String path = request.getURI().getPath();
      String param = path.substring(path.lastIndexOf('/') + 1);
      int projectId = Integer.parseInt(param);
      attributes.put("projectId", projectId);
      return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Exception exception) {
      // Nothing to do
    }
  }
}