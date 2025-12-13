package com.chatroom.controller;

import com.chatroom.service.AuthService;
import com.chatroom.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword) {
        boolean success = authService.register(username, password, confirmPassword);
        return success ? ResponseEntity.ok("注册成功") :
                ResponseEntity.badRequest().body("注册失败");
    }

    // 修改后的短信注册接口
    @PostMapping("/register-sms")
    public ResponseEntity<?> registerBySms(@RequestParam String phone,
                                           @RequestParam String password,
                                           @RequestParam String confirmPassword,
                                           @RequestParam String code) {
        Map<String, Object> result = authService.registerBySms(phone, password, confirmPassword, code);
        boolean success = (Boolean) result.get("success");
        String message = (String) result.get("message");

        if (success) {
            // 返回生成的账号信息
            String username = (String) result.get("username");
            Map<String, Object> response = new HashMap<>();
            response.put("message", message);
            response.put("username", username);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password) {
        User user = authService.login(username, password);
        return user != null ? ResponseEntity.ok("登录成功") :
                ResponseEntity.badRequest().body("登录失败");
    }

    // 短信登录接口
    @PostMapping("/login-sms")
    public ResponseEntity<?> loginBySms(@RequestParam String phone,
                                        @RequestParam String code) {
        Map<String, Object> result = authService.loginBySms(phone, code);
        boolean success = (Boolean) result.get("success");
        String message = (String) result.get("message");

        if (success) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    @PostMapping("/send-sms")
    public ResponseEntity<?> sendSms(@RequestParam String phone) {
        try {
            String code = authService.generateSmsCode(phone);
            // 模拟发送短信，阿里云或者腾讯云必须企业认证，没办法只能模拟了
            System.out.println("发送验证码到 " + phone + "，验证码为：" + code);
            return ResponseEntity.ok("验证码已发送");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 检查手机号是否已注册
    @GetMapping("/check-phone")
    public ResponseEntity<?> checkPhone(@RequestParam String phone) {
        boolean isRegistered = authService.isPhoneRegistered(phone);
        Map<String, Object> response = new HashMap<>();
        response.put("registered", isRegistered);
        response.put("message", isRegistered ? "手机号已注册" : "手机号未注册");
        return ResponseEntity.ok(response);
    }
}