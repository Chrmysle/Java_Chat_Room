package com.chatroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatWebSocketServer extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> onlineUsers = Collections.synchronizedMap(new HashMap<>());
    private final ObjectMapper mapper = new ObjectMapper();

    public ChatWebSocketServer() {
        // 支持 LocalDateTime 序列化
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = session.getUri().getQuery().split("=")[1]; // ?username=xxx
        onlineUsers.put(username, session);
        broadcast("系统", username + " 已上线", "group", null);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Message msg = mapper.readValue(message.getPayload(), Message.class);
        msg.setTime(LocalDateTime.now());

        if ("group".equals(msg.getType()) || msg.getToUser() == null) {
            broadcast(msg.getFromUser(), msg.getContent(), "group", null);
        } else {
            sendPrivateMessage(msg.getToUser(), msg);
        }
    }

    private void broadcast(String fromUser, String content, String type, String toUser) throws Exception {
        Message msg = new Message(fromUser, toUser, content, type, LocalDateTime.now());
        for (WebSocketSession s : onlineUsers.values()) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(mapper.writeValueAsString(msg)));
            }
        }
    }

    private void sendPrivateMessage(String toUser, Message msg) throws Exception {
        WebSocketSession s = onlineUsers.get(toUser);
        if (s != null && s.isOpen()) {
            s.sendMessage(new TextMessage(mapper.writeValueAsString(msg)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        onlineUsers.values().remove(session);
    }
}
