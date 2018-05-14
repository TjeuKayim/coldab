package com.github.coldab.server.ws;

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

  public WebSocketConfig(SocketHandler webSocketHandler) {
    this.webSocketHandler = webSocketHandler;
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
      String path = request.getURI().getPath();
      String param = path.substring(path.lastIndexOf('/') + 1);
      int projectId = Integer.parseInt(param);
      attributes.put("projectId", projectId);
      return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Exception exception) {
    }
  }
}