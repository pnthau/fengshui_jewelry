package com.fengshui.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/notifications")
public class OrderNotificationEndpoint {

    // Danh sách lưu trữ tất cả các Admin đang online (thread-safe)
    private static final Set<Session> saleSessions = ConcurrentHashMap.newKeySet();

    @OnOpen
    public void onOpen(Session session) {
        saleSessions.add(session);
        System.out.println("🔗 Một sale vừa kết nối WebSocket: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        saleSessions.remove(session);
        System.out.println("❌ Một sale đã ngắt kết nối WebSocket: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("⚠️ Lỗi WebSocket ở session " + session.getId() + ": " + throwable.getMessage());
    }

    // Hàm TỐI QUAN TRỌNG: Dùng để các Controller gọi khi có đơn hàng mới
    public static void broadcast(String jsonMessage) {
        for (Session session : saleSessions) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(jsonMessage);
                } catch (IOException e) {
                    System.err.println("Không thể gửi thông báo tới sale: " + e.getMessage());
                }
            }
        }
    }
}
