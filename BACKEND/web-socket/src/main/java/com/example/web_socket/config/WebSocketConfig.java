package com.example.web_socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  /**
   *  Mục đích: Cấu hình Web socket trong Spring
   *  @EnableWebSocket: Kích hoạt hỗ trợ WebSocket
   *  registerWebSocketHandlers(): Được gọi khi ứng dụng khởi động để đăng ký các WebSocket handler
   *  Đăng ký ChatHandler để xử lý các kết nối đến endpoint "/ws"
   *  setAllowedOrigins("*"): Cho phép kết nối từ mọi nguồn (origin)
   * @param registry
   */
  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new ChatHandler(), "/ws").setAllowedOrigins("*");
  }
}