package com.chatroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
        System.out.println("Java_Chat_Room 后端服务已启动，WebSocket 地址: ws://10.128.154.24:8080/chat");
    }
}
