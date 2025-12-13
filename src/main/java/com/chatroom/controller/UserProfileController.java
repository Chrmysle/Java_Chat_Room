package com.chatroom.controller;

import com.chatroom.model.UserProfile;
import com.chatroom.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    private String defaultAvatar(String displayName, String username) {
        String name = (displayName != null && !displayName.trim().isEmpty()) ? displayName : username;
        String encoded;
        try {
            encoded = URLEncoder.encode(name, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            logger.warn("URL 编码失败，使用回退值 name={}", name, e);
            encoded = name == null ? "" : name.replaceAll("\\s+", "+");
        }
        return "https://ui-avatars.com/api/?name=" + encoded + "&background=random&size=128";
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable("username") String username) {
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            UserProfile p = service.getOrCreate(username);
            if (p == null) {
                return ResponseEntity.notFound().build();
            }
            // 响应中补充默认 avatar（不强制持久化）
            if (p.getAvatarUrl() == null || p.getAvatarUrl().trim().isEmpty()) {
                p.setAvatarUrl(defaultAvatar(p.getDisplayName(), p.getUsername()));
            }
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            logger.error("获取用户资料失败，username={}", username, e);
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserProfile> updateProfile(
            @PathVariable("username") String username,
            @RequestBody UserProfile update) {

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            // 读取已有 profile，逐字段合并（避免客户端字段直接覆盖）
            UserProfile existing = service.getOrCreate(username);
            if (existing == null) {
                return ResponseEntity.notFound().build();
            }

            // 更新 displayName：只在前端提交了非空且不等于 username 时应用
            String dn = update.getDisplayName();
            if (dn != null) {
                dn = dn.trim();
                if (!dn.isEmpty() && !dn.equals(username)) {
                    existing.setDisplayName(dn);
                } else if (dn.equals(username)) {
                    // 若前端传来的 displayName 恰好等于 username，记录警告但不覆盖
                    logger.warn("尝试将 displayName 设置为 username，忽略该值 username={}", username);
                }
            }

            // 更新 bio（允许清空）
            if (update.getBio() != null) {
                existing.setBio(update.getBio().trim());
            }

            // 更新 avatarUrl：允许设置或清空（若为空则后续 GET 会补默认）
            if (update.getAvatarUrl() != null) {
                String a = update.getAvatarUrl().trim();
                existing.setAvatarUrl(a.isEmpty() ? null : a);
            }

            // 调用 service 的更新/保存方法（保留原有 service 接口用法）
            UserProfile saved = service.update(username, existing);
            if (saved == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            // 响应中保证有 avatarUrl（返回给前端看）
            if (saved.getAvatarUrl() == null || saved.getAvatarUrl().trim().isEmpty()) {
                saved.setAvatarUrl(defaultAvatar(saved.getDisplayName(), saved.getUsername()));
            }
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            logger.warn("更新资料参数错误，username={}，msg={}", username, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("更新用户资料失败，username={}", username, e);
            return ResponseEntity.status(500).build();
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnhandled(Exception e) {
        logger.error("未处理的异常：", e);
        return ResponseEntity.status(500).body("服务器内部错误");
    }
}
