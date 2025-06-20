package com.example.web_socket.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * XỬ LÍ LOGIC CHAT QUA WEB SOCKET
 */
public class ChatHandler extends TextWebSocketHandler {

  /**
   * section : Lưu trữ các phiên WebSocket đang hoạt động , key là user ID
   */
  private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();


  /**
   * Được gọi khi client thiết lập kết nối WebSocket thành công
   * Luồng chạy:
   *     1. Lấy userId từ query parameter của URL (ví dụ: /ws?userId=123)
   *     2. Lưu trữ section vào map với key là userId.
   *     3. In thông báo kết nối.
   * @param session
   */
  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    String userId = getUserId(session);
    sessions.put(userId, session);
    System.out.println(userId + " connected");
  }


  /**
   * Chạy khi server nhận được tin nhắn từ client
   * Chuyển đổi luồng tin nhắn (JSON) thành load
   * Lấy thông tin người gửi (from) , nguoif nhận (to) và nội dung ((message))
   * Kiểm tra nếu người nhận đang online (có trong session)
   * Gửi tin nhắn đến người nhận
   * @param session
   * @param message
   * @throws Exception
   */
  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    Map<String, String> msg = new ObjectMapper().readValue(message.getPayload(), Map.class);
    String to = msg.get("to");
    String from = msg.get("from");
    String text = msg.get("message");

    WebSocketSession receiver = sessions.get(to);
    if (receiver != null && receiver.isOpen()) {
      receiver.sendMessage(new TextMessage("From " + from + ": " + text));
    }
  }

  /**
   * Khi nào được gọi: Khi kết nối WebSocket bị đóng
   * Luồng chạy: Khi kết nối web socket bị đóng
   * Nó sẽ xóa khỏi map khi kết nối đóng.
   * @param session
   * @param status
   */

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    sessions.values().removeIf(s -> s.getId().equals(session.getId()));
  }

  private String getUserId(WebSocketSession session) {
    return session.getUri().getQuery().split("=")[1];
  }
}


