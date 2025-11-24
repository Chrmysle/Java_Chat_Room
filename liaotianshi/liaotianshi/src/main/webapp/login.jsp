<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户登录</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            padding: 20px;
        }

        .login-container {
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
            width: 100%;
            max-width: 450px;
            padding: 40px;
            position: relative;
            overflow: hidden;
        }

        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .login-title {
            font-size: 28px;
            color: #333;
            margin-bottom: 10px;
        }

        .login-subtitle {
            color: #666;
            font-size: 14px;
        }

        .form-container {
            position: relative;
            min-height: 280px;
            overflow: hidden;
        }

        .login-form {
            position: absolute;
            width: 100%;
            transition: transform 0.5s ease, opacity 0.5s ease;
            opacity: 0;
            transform: translateX(100%);
        }

        .login-form.active {
            opacity: 1;
            transform: translateX(0);
            position: relative;
        }

        .form-group {
            margin-bottom: 20px;
            position: relative;
        }

        .form-label {
            display: block;
            margin-bottom: 8px;
            font-weight: 500;
            color: #555;
        }

        .form-input {
            width: 100%;
            padding: 14px 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s;
        }

        .form-input:focus {
            border-color: #4a7dff;
            outline: none;
            box-shadow: 0 0 0 2px rgba(74, 125, 255, 0.2);
        }

        .captcha-container {
            display: flex;
            gap: 10px;
        }

        .captcha-input {
            flex: 1;
        }

        .captcha-btn {
            padding: 14px 20px;
            background-color: #f0f4ff;
            border: 1px solid #ddd;
            border-radius: 8px;
            cursor: pointer;
            color: #4a7dff;
            font-weight: 500;
            white-space: nowrap;
            transition: all 0.3s;
        }

        .captcha-btn:hover {
            background-color: #e1e8ff;
        }

        .captcha-btn:disabled {
            background-color: #f5f5f5;
            color: #999;
            cursor: not-allowed;
        }

        .remember-forgot {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            font-size: 14px;
        }

        .remember-me {
            display: flex;
            align-items: center;
        }

        .remember-me input {
            margin-right: 8px;
        }

        .forgot-password {
            color: #4a7dff;
            text-decoration: none;
            transition: color 0.3s;
        }

        .forgot-password:hover {
            color: #2d5bd6;
            text-decoration: underline;
        }

        .login-btn {
            width: 100%;
            padding: 14px;
            background: linear-gradient(to right, #6a11cb, #2575fc);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .login-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 15px rgba(37, 117, 252, 0.4);
        }

        .login-btn:active {
            transform: translateY(0);
        }

        .login-type-switch {
            text-align: center;
            margin-top: 25px;
            font-size: 14px;
            color: #666;
        }

        .switch-btn {
            color: #4a7dff;
            cursor: pointer;
            font-weight: 500;
            margin-left: 5px;
            transition: color 0.3s;
        }

        .switch-btn:hover {
            color: #2d5bd6;
            text-decoration: underline;
        }

        .message {
            text-align: center;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            background-color: #ffebee;
            color: #c62828;
        }

        .success-message {
            background-color: #e8f5e9;
            color: #2e7d32;
        }

        .register-link {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
        }

        .register-link a {
            color: #4a7dff;
            text-decoration: none;
            transition: color 0.3s;
        }

        .register-link a:hover {
            color: #2d5bd6;
            text-decoration: underline;
        }

        @media (max-width: 480px) {
            .login-container {
                padding: 30px 20px;
            }

            .form-container {
                height: 280px;
            }
        }
    </style>
</head>
<body>
<div class="login-container">
    <div class="login-header">
        <h1 class="login-title">账号登录</h1>
        <p class="login-subtitle">请选择登录方式并输入您的信息</p>
    </div>

    <%
        String message = (String) request.getAttribute("message");
        String type = request.getParameter("type");
        if (message == null) {
            message = request.getParameter("message");
        }
        if (message != null && !message.isEmpty()) {
            String messageClass = message.contains("成功") ? "message success-message" : "message";
    %>
    <div class="<%= messageClass %>"><%= message %></div>
    <% } %>

    <div class="form-container">
        <!-- 普通登录表单 -->
        <form class="login-form normal-login <%= "sms".equals(type) ? "" : "active" %>" action="login" method="post">
            <input type="hidden" name="type" value="normal">

            <div class="form-group">
                <label class="form-label" for="username">用户名</label>
                <input type="text" id="username" name="username" class="form-input" placeholder="请输入用户名" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">密码</label>
                <input type="password" id="password" name="password" class="form-input" placeholder="请输入密码" required>
            </div>

            <div class="remember-forgot">
                <div class="remember-me">
                    <input type="checkbox" id="remember">
                    <label for="remember">记住我</label>
                </div>
                <a href="#" class="forgot-password">忘记密码?</a>
            </div>

            <button type="submit" class="login-btn">登录</button>
        </form>

        <!-- 短信登录表单 -->
        <form class="login-form sms-login <%= "sms".equals(type) ? "active" : "" %>" action="login" method="post">
            <input type="hidden" name="type" value="sms">

            <div class="form-group">
                <label class="form-label" for="phoneLogin">手机号码</label>
                <input type="tel" id="phoneLogin" name="phone" class="form-input" placeholder="请输入手机号码" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="loginCode">验证码</label>
                <div class="captcha-container">
                    <input type="text" id="loginCode" name="code" class="form-input captcha-input" placeholder="请输入验证码" required>
                    <button type="button" class="captcha-btn" id="getLoginCode">获取验证码</button>
                </div>
            </div>

            <button type="submit" class="login-btn">登录</button>
        </form>
    </div>

    <div class="login-type-switch">
        使用
        <span class="switch-btn" id="switchBtn"><%= "sms".equals(type) ? "账号密码登录" : "手机验证码登录" %></span>
    </div>

    <div class="register-link">
        没有账号？<a href="register">立即注册</a>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const switchBtn = document.getElementById('switchBtn');
        const normalLogin = document.querySelector('.normal-login');
        const smsLogin = document.querySelector('.sms-login');
        const loginTitle = document.querySelector('.login-title');
        const loginSubtitle = document.querySelector('.login-subtitle');
        const getLoginCodeBtn = document.getElementById('getLoginCode');
        // 根据URL参数初始化页面状态
        let isSMSLogin = <%= "sms".equals(type) ? "true" : "false" %>;
        let countdown = 0;
        let countdownInterval;
        updateFormVisibility();
        checkExistingCountdown();
        // 切换登录方式
        switchBtn.addEventListener('click', function() {
            isSMSLogin = !isSMSLogin;
            updateFormVisibility();
            checkExistingCountdown();
        });

        function updateFormVisibility() {
            if (isSMSLogin) {
                normalLogin.classList.remove('active');
                smsLogin.classList.add('active');
                loginTitle.textContent = '手机登录';
                loginSubtitle.textContent = '请输入您的手机号和验证码';
                switchBtn.textContent = '账号密码登录';
            } else {
                smsLogin.classList.remove('active');
                normalLogin.classList.add('active');
                loginTitle.textContent = '账号登录';
                loginSubtitle.textContent = '请选择登录方式并输入您的信息';
                switchBtn.textContent = '手机验证码登录';
            }
        }
        // 更新倒计时显示
        function updateCountdownDisplay() {
            if (countdown > 0) {
                getLoginCodeBtn.textContent = countdown + '秒后重新获取';
                console.log('倒计时更新: ' + countdown + ' 秒'); // 添加控制台日志，便于调试
            } else {
                getLoginCodeBtn.textContent = '获取验证码';
            }
        }
        // 检查现有倒计时（使用localStorage持久化计数器）
        function checkExistingCountdown() {
            const savedCountdown = localStorage.getItem('smsCountdown');
            const savedTime = localStorage.getItem('smsCountdownTime');
            if (savedCountdown && savedTime) {
                const elapsed = Math.floor((Date.now() - parseInt(savedTime)) / 1000);
                countdown = Math.max(0, parseInt(savedCountdown) - elapsed);
                if (countdown > 0) {
                    getLoginCodeBtn.disabled = true;
                    updateCountdownDisplay();

                    // 恢复倒计时间隔
                    countdownInterval = setInterval(function() {
                        countdown--;
                        updateCountdownDisplay();
                        localStorage.setItem('smsCountdown', countdown.toString());
                        localStorage.setItem('smsCountdownTime', Date.now().toString());

                        if (countdown <= 0) {
                            clearInterval(countdownInterval);
                            getLoginCodeBtn.disabled = false;
                            localStorage.removeItem('smsCountdown');
                            localStorage.removeItem('smsCountdownTime');
                        }
                    }, 1000);
                } else {
                    localStorage.removeItem('smsCountdown');
                    localStorage.removeItem('smsCountdownTime');
                }
            }
        }

        // 获取验证码
        getLoginCodeBtn.addEventListener('click', function() {

            if (countdown > 0) {
                alert('请等待' + countdown + '秒后重新获取验证码');
                return;
            }

            const phoneInput = document.getElementById('phoneLogin');
            const phone = phoneInput.value.trim();

            if (!phone) {
                alert('请输入手机号码');
                phoneInput.focus();
                return;
            }

            if (!/^1[3-9]\d{9}$/.test(phone)) {
                alert('请输入有效的手机号码');
                phoneInput.focus();
                return;
            }

            // 清除旧的倒计时
            if (countdownInterval) {
                clearInterval(countdownInterval);
            }
            localStorage.removeItem('smsCountdown');
            localStorage.removeItem('smsCountdownTime');

            // 发送获取验证码请求
            const xhr = new XMLHttpRequest();
            xhr.open('GET', 'register?action=sendCode&phone=' + encodeURIComponent(phone) + '&codeType=LOGIN', true);

            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        const response = xhr.responseText;
                        //getLoginCodeBtn.textContent = countdown + '秒后重新获取';
                        if (response.startsWith('success')) {
                            // 开始60秒倒计时
                            countdown = 60;
                            getLoginCodeBtn.disabled = true;
                            localStorage.setItem('smsCountdown', countdown.toString());
                            localStorage.setItem('smsCountdownTime', Date.now().toString());

                            updateCountdownDisplay();
//getLoginCodeBtn.textContent = countdown + '秒后重新获取';
                            countdownInterval = setInterval(function() {
                                countdown--;
                                updateCountdownDisplay();
                                localStorage.setItem('smsCountdown', countdown.toString());
                                localStorage.setItem('smsCountdownTime', Date.now().toString());

                                if (countdown <= 0) {
                                    clearInterval(countdownInterval);
                                    getLoginCodeBtn.disabled = false;
                                    localStorage.removeItem('smsCountdown');
                                    localStorage.removeItem('smsCountdownTime');
                                }
                            }, 1000);

                            // 从响应中提取验证码（仅用于测试）
                            const code = response.split(':')[1];
                            console.log('测试用验证码:', code);
                            alert('验证码已发送到您的手机，请注意查收。验证码有效期为60秒。测试验证码: ' + code);
                        } else {
                            alert(response);
                        }
                    } else {
                        alert('网络错误，请重试');
                    }
                }
            };
            xhr.send();
        });
    });
</script>
</body>
</html>