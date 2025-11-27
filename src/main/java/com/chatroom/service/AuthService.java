package com.chatroom.service;

import com.chatroom.Repository.SmsCodeRepository;
import com.chatroom.Repository.UserRepository;
import com.chatroom.model.SmsCode;
import com.chatroom.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmsCodeRepository smsCodeRepository;

    // 生成随机账号
    private String generateRandomUsername() {
        // 生成8位随机数字作为账号
        String randomNum = String.valueOf((int) ((Math.random() * 9 + 1) * 10000000));
        return "user_" + randomNum;
    }

    // 检查账号是否已存在
    private boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // 生成唯一的随机账号
    private String generateUniqueUsername() {
        String username;
        int maxAttempts = 10; // 最大尝试次数，避免无限循环
        int attempts = 0;

        do {
            username = generateRandomUsername();
            attempts++;
            if (attempts >= maxAttempts) {
                // 如果尝试次数过多，使用时间戳作为后缀确保唯一性
                username = "user_" + System.currentTimeMillis();
                break;
            }
        } while (isUsernameExists(username));

        return username;
    }

    // 检查手机号是否已注册
    public boolean isPhoneRegistered(String phone) {
        return userRepository.existsByPhone(phone);
    }

    // 普通注册
    public boolean register(String username, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return false;
        }
        if (userRepository.existsByUsername(username)) {
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    // 短信注册 - 修改后的版本，自动生成随机账号
    public Map<String, Object> registerBySms(String phone, String password, String confirmPassword, String code) {
        Map<String, Object> result = new HashMap<>();

        if (!password.equals(confirmPassword)) {
            result.put("success", false);
            result.put("message", "密码和确认密码不一致");
            return result;
        }

        // 检查手机号是否已存在
        if (userRepository.existsByPhone(phone)) {
            result.put("success", false);
            result.put("message", "手机号已被注册");
            return result;
        }

        // 验证短信验证码
        SmsCode smsCode = smsCodeRepository.findByPhoneAndCode(phone, code);
        if (smsCode == null) {
            result.put("success", false);
            result.put("message", "验证码错误");
            return result;
        }

        // 检查验证码是否过期（10分钟内有效）
        if (smsCode.getCreateTime().isBefore(java.time.LocalDateTime.now().minusMinutes(10))) {
            smsCodeRepository.delete(smsCode);
            result.put("success", false);
            result.put("message", "验证码已过期");
            return result;
        }

        // 生成随机账号
        String randomUsername = generateUniqueUsername();

        User user = new User();
        user.setUsername(randomUsername);
        user.setPhone(phone);
        user.setPassword(password);

        boolean saveResult = userRepository.save(user);

        if (saveResult) {
            // 删除已使用的验证码
            smsCodeRepository.delete(smsCode);
            result.put("success", true);
            result.put("message", "注册成功");
            result.put("username", randomUsername); // 返回生成的账号
        } else {
            result.put("success", false);
            result.put("message", "注册失败，请重试");
        }

        return result;
    }

    // 普通登录
    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    // 短信登录
    public Map<String, Object> loginBySms(String phone, String code) {
        Map<String, Object> result = new HashMap<>();

        // 先检查手机号是否已注册
        if (!userRepository.existsByPhone(phone)) {
            result.put("success", false);
            result.put("message", "手机号未注册，请先注册");
            return result;
        }

        // 验证短信验证码
        SmsCode smsCode = smsCodeRepository.findByPhoneAndCode(phone, code);
        if (smsCode == null) {
            result.put("success", false);
            result.put("message", "验证码错误");
            return result;
        }

        // 检查验证码是否过期
        if (smsCode.getCreateTime().isBefore(java.time.LocalDateTime.now().minusMinutes(10))) {
            smsCodeRepository.delete(smsCode);
            result.put("success", false);
            result.put("message", "验证码已过期");
            return result;
        }

        // 查找用户
        User user = userRepository.findByPhone(phone);

        // 删除已使用的验证码
        if (user != null) {
            smsCodeRepository.delete(smsCode);
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("user", user);
        } else {
            result.put("success", false);
            result.put("message", "登录失败");
        }

        return result;
    }

    // 生成并保存验证码
    public String generateSmsCode(String phone) {
        // 1分钟内不能重复发送）
        java.time.LocalDateTime oneMinuteAgo = java.time.LocalDateTime.now().minusMinutes(1);
        long recentCount = smsCodeRepository.countByPhoneAndCreateTimeAfter(phone, oneMinuteAgo);
        if (recentCount > 0) {
            throw new RuntimeException("请求过于频繁，请稍后再试");
        }

        // 生成6位随机数字
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        // 清理过期的验证码
        smsCodeRepository.deleteExpiredCodes(java.time.LocalDateTime.now().minusMinutes(10));

        SmsCode smsCode = new SmsCode();
        smsCode.setPhone(phone);
        smsCode.setCode(code);
        smsCode.setCreateTime(java.time.LocalDateTime.now());

        smsCodeRepository.save(smsCode);
        return code;
    }

    // 统一的用户查找方法
    public User findUserByIdentifier(String identifier) {
        return userRepository.findByUsernameOrPhone(identifier).orElse(null);
    }
}