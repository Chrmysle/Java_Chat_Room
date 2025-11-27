// java
package com.chatroom.websocket;

import com.chatroom.model.UserProfile;
import com.chatroom.service.UserProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.chatroom.Message;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class ChatWebSocketServer extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> onlineUsers = Collections.synchronizedMap(new HashMap<>());
    private final ObjectMapper mapper = new ObjectMapper();
    private final UserProfileService userProfileService;

    public ChatWebSocketServer(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = extractUsername(session);
        if (username == null || username.isEmpty()) {
            session.close();
            return;
        }
        onlineUsers.put(username, session);
        userProfileService.setOnline(username, true);
        broadcast("系统", resolveDisplayName(username)+ " 已上线", "group", null);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Message msg = mapper.readValue(message.getPayload(), Message.class);
        msg.setTime(Instant.now().toString());

        if ("group".equals(msg.getType()) || msg.getToUser() == null) {
            // 保留客户端传来的 fromDisplayName（若有），否则回退到服务端存储的显示名
            if (msg.getFromDisplayName() == null || msg.getFromDisplayName().trim().isEmpty()) {
                String displayName = resolveDisplayName(msg.getFromUser());
                if (displayName != null) {
                    msg.setFromDisplayName(displayName);
                } else {
                    // 若服务端也没有，回退为用户名（保持不为空）
                    msg.setFromDisplayName(msg.getFromUser());
                }
            }
            broadcast(msg);
        } else {
            // 在发送私聊前补上发送者的显示名（保持原有逻辑）
            String displayName = resolveDisplayName(msg.getFromUser());
            if (displayName != null) {
                msg.setFromDisplayName(displayName);
            }
            sendPrivateMessage(msg.getToUser(), msg);
        }
    }

    // 旧的广播入口保留（构造 Message 后委托）
    private void broadcast(String fromUser, String content, String type, String toUser) throws Exception {
        Message msg = new Message(fromUser, toUser, content, type, Instant.now().toString());
        String displayName = resolveDisplayName(fromUser);
        if (displayName != null) {
            msg.setFromDisplayName(displayName);
        } else {
            msg.setFromDisplayName(fromUser);
        }
        broadcast(msg);
    }

    // 新增：直接广播已有 Message（尊重 msg.fromDisplayName）
    private void broadcast(Message msg) throws Exception {
        for (WebSocketSession s : onlineUsers.values()) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(mapper.writeValueAsString(msg)));
            }
        }
    }

    private void sendPrivateMessage(String toUser, Message msg) throws Exception {
        // 如果还没有显示名，尝试补上
        if (msg.getFromDisplayName() == null || msg.getFromDisplayName().trim().isEmpty()) {
            String displayName = resolveDisplayName(msg.getFromUser());
            if (displayName != null) {
                msg.setFromDisplayName(displayName);
            } else {
                msg.setFromDisplayName(msg.getFromUser());
            }
        }
        WebSocketSession s = onlineUsers.get(toUser);
        if (s != null && s.isOpen()) {
            s.sendMessage(new TextMessage(mapper.writeValueAsString(msg)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        String disconnectedUser = null;
        synchronized (onlineUsers) {
            for (Entry<String, WebSocketSession> e : onlineUsers.entrySet()) {
                if (e.getValue().equals(session)) {
                    disconnectedUser = e.getKey();
                    onlineUsers.remove(disconnectedUser);
                    break;
                }
            }
        }
        if (disconnectedUser != null) {
            userProfileService.setOnline(disconnectedUser, false);
            try {
                broadcast("系统", disconnectedUser + " 已下线", "group", null);
            } catch (Exception ignored) {}
        }
    }

    private String extractUsername(WebSocketSession session) {
        if (session == null || session.getUri() == null || session.getUri().getQuery() == null) return null;
        String q = session.getUri().getQuery(); // 支持 ?username=xxx (简单解析)
        for (String part : q.split("&")) {
            String[] kv = part.split("=", 2);
            if (kv.length == 2 && "username".equals(kv[0])) {
                return kv[1];
            }
        }
        return null;
    }

    // 从 UserProfileService 获取显示名（若找不到返回 null）
    private String resolveDisplayName(String username) {
        if (username == null) return null;
        try {
            UserProfile profile = userProfileService.getProfile(username);
            if (profile == null) return null;
            String dn = profile.getDisplayName();
            return (dn != null && !dn.trim().isEmpty()) ? dn : null;
        } catch (Exception ignored) {
            return null;
        }
    }
}
