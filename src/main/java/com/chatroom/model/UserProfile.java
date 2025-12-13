package com.chatroom.model;

import java.time.LocalDateTime;

public class UserProfile {
    public String username;//用户名
    private String displayName;//用户昵称
    private String bio;//个人简介
    private String avatarUrl;//用户头像url
    private boolean online;//在线状态标识
    private LocalDateTime lastSeen;//上次在线时间

    public UserProfile() {}

    public UserProfile(String username) {
        this.username = username;
        this.displayName = username;
        this.online = false;
    }

    public UserProfile(String username, String displayName, String bio, String avatarUrl, boolean online, LocalDateTime lastSeen) {
        this.username = username;
        this.displayName = displayName;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.online = online;
        this.lastSeen = lastSeen;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }
}
