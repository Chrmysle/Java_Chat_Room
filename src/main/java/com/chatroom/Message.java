package com.chatroom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Message 使用字符串形式的 time（ISO 8601），确保前端 new Date(msg.time) 可解析。
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    private String fromUser;
    private String toUser;
    private String content;
    private String type; // group / private
    private String fromDisplayName;
    private String time; // ISO 字符串，例如：2025-11-25T15:10:15.314Z

    public Message() {}

    public Message(String fromUser, String toUser, String content, String type, String time) {
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

    public String getFromDisplayName() { return fromDisplayName; }
    public void setFromDisplayName(String fromDisplayName) { this.fromDisplayName = fromDisplayName; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
