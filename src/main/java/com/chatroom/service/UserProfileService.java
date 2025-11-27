package com.chatroom.service;
import com.chatroom.model.UserProfile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class UserProfileService {
    private final Map<String,UserProfile> profiles=new ConcurrentHashMap<>();
    public UserProfile getOrCreate(String username){
        /*
        computeIfAbsent
        * 它的作用可以分解为几步：
          检查键是否存在: 它会先检查 Map 中是否已经存在指定的 key（这里是 username）。
          如果存在: 直接返回与该 key 关联的 value（即已有的 UserProfile 对象）。
          如果不存在:
          它会调用你提供的第二个参数 mappingFunction（一个函数），并将 key 作为参数传递给这个函数。
          这个函数的职责是根据 key 计算出一个新的 value（在这里，就是创建一个新的 UserProfile 对象）。
          然后，computeIfAbsent 方法会将这个新创建的 key-value 对存入 Map 中。
          最后，返回这个新创建的 value。
        * */
        return profiles.computeIfAbsent(username,UserProfile::new);
    }

    public UserProfile update(String username, UserProfile update) {
        // 1. 获取或创建用户配置文件
        UserProfile p = getOrCreate(username);

        // 2. 部分更新用户信息
        if (update.getDisplayName() != null) p.setDisplayName(update.getDisplayName());
        if (update.getBio() != null) p.setBio(update.getBio());
        if (update.getAvatarUrl() != null) p.setAvatarUrl(update.getAvatarUrl());

        // 3. 明确不更新的字段
        // online/lastSeen 由系统控制

        // 4. 返回更新后的对象
        return p;
    }

    public void setOnline(String username, boolean online) {
        // 1. 获取或创建用户配置文件
        UserProfile p = getOrCreate(username);

        // 2. 设置在线状态
        p.setOnline(online);

        // 3. 更新最后活跃时间
        if (!online) {
            p.setLastSeen(LocalDateTime.now());
        } else {
            p.setLastSeen(null);
        }
    }

    public UserProfile getProfile(String username) {
        if (username == null) return null;
        return profiles.get(username);
    }

}

