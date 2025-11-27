package com.chatroom;

import com.chatroom.websocket.ChatWebSocketServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatWebSocketServer chatWebSocketServer;
    public WebSocketConfig(ChatWebSocketServer chatWebSocketServer){
        this.chatWebSocketServer=chatWebSocketServer;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 直接 new，不用依赖注入
        registry.addHandler(chatWebSocketServer, "/chat")
                .setAllowedOrigins("*");  // 允许前端跨域访问
    }
}
