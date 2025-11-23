package com.chatroom;

import java.time.LocalDateTime;

public class Message {
    private String fromUser;
    private String toUser;
    private String content;
    private String type; // group / private
    private LocalDateTime time;

    public Message() {}

    public Message(String fromUser, String toUser, String content, String type, LocalDateTime time) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.type = type;
        this.time = time;
    }

    public String getFromUser() { return fromUser; }
    public void setFromUser(String fromUser) { this.fromUser = fromUser; }

    public String getToUser() { return toUser; }
    public void setToUser(String toUser) { this.toUser = toUser; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
}
