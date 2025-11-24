<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户注册</title>
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

        .register-container {
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
            width: 100%;
            max-width: 450px;
            padding: 40px;
            position: relative;
            overflow: hidden;
        }

        .register-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .register-title {
            font-size: 28px;
            color: #333;
            margin-bottom: 10px;
        }

        .register-subtitle {
            color: #666;
            font-size: 14px;
        }

        .form-container {
            position: relative;
            min-height: 350px;
            overflow: hidden;
        }

        .register-form {
            position: absolute;
            width: 100%;
            transition: transform 0.5s ease, opacity 0.5s ease;
            opacity: 0;
            transform: translateX(100%);
        }

        .register-form.active {
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

        .input-error {
            border-color: #ff4757;
        }

        .error-message {
            color: #ff4757;
            font-size: 12px;
            margin-top: 5px;
            display: none;
        }

        .password-strength {
            height: 4px;
            border-radius: 2px;
            margin-top: 5px;
            background-color: #eee;
            overflow: hidden;
        }

        .strength-bar {
            height: 100%;
            width: 0;
            transition: width 0.3s, background-color 0.3s;
        }

        .strength-weak {
            background-color: #ff4757;
            width: 33%;
        }

        .strength-medium {
            background-color: #ffa502;
            width: 66%;
        }

        .strength-strong {
            background-color: #2ed573;
            width: 100%;
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
            min-width: 140px; /* 增加最小宽度，确保倒计时文本有足够空间显示 */
            text-align: center; /* 居中对齐文本 */
        }

        .captcha-btn:hover {
            background-color: #e1e8ff;
        }

        .captcha-btn:disabled {
            background-color: #f5f5f5;
            color: #333; /* 改为更深的颜色，确保数字可见 */
            cursor: not-allowed;
        }

        .register-btn {
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
            margin-top: 10px;
        }

        .register-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 15px rgba(37, 117, 252, 0.4);
        }

        .register-btn:disabled {
            background: #cccccc;
            cursor: not-allowed;
            transform: none;
            box-shadow: none;
        }

        .register-type-switch {
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

        .login-link {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
        }

        .login-link a {
            color: #4a7dff;
            text-decoration: none;
            transition: color 0.3s;
        }

        .login-link a:hover {
            color: #2d5bd6;
            text-decoration: underline;
        }

        .terms {
            margin-top: 15px;
            font-size: 12px;
            color: #666;
            text-align: center;
        }

        .terms a {
            color: #4a7dff;
            text-decoration: none;
        }

        .terms a:hover {
            text-decoration: underline;
        }

        @media (max-width: 480px) {
            .register-container {
                padding: 30px 20px;
            }
            .captcha-btn {
                min-width: 120px; /* 移动端稍减小宽度 */
                padding: 14px 10px;
            }
        }
    </style>
</head>
<body>
<div class="register-container">
    <div class="register-header">
        <h1 class="register-title">用户注册</h1>
        <p class="register-subtitle">请选择注册方式并填写信息</p>
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
        <!-- 普通注册表单 -->
        <form class="register-form normal-register active" action="register" method="post" id="normalRegisterForm">
            <input type="hidden" name="type" value="normal">

            <div class="form-group">
                <label class="form-label" for="username">用户名</label>
                <input type="text" id="username" name="username" class="form-input" placeholder="请输入用户名(4-16位字母、数字或下划线)" required>
                <div class="error-message" id="usernameError">用户名格式不正确</div>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">密码</label>
                <input type="password" id="password" name="password" class="form-input" placeholder="请输入密码(6-20位字符)" required>
                <div class="password-strength">
                    <div class="strength-bar" id="passwordStrength"></div>
                </div>
                <div class="error-message" id="passwordError">密码格式不正确</div>
            </div>

            <div class="form-group">
                <label class="form-label" for="confirmPasswordNormal">确认密码</label>
                <input type="password" id="confirmPasswordNormal" name="confirmPassword" class="form-input" placeholder="请再次输入密码" required>
                <div class="error-message" id="confirmPasswordError">两次输入的密码不一致</div>
            </div>

            <div class="terms">
                注册即表示同意 <a href="#">用户协议</a> 和 <a href="#">隐私政策</a>
            </div>

            <button type="submit" class="register-btn" id="normalRegisterBtn">注册</button>
        </form>

        <!-- 短信注册表单 -->
        <form class="register-form sms-register" action="register" method="post" id="smsRegisterForm">
            <input type="hidden" name="type" value="sms">

            <div class="form-group">
                <label class="form-label" for="phone">手机号码</label>
                <input type="tel" id="phone" name="phone" class="form-input" placeholder="请输入手机号码" required>
                <div class="error-message" id="phoneError">手机号码格式不正确</div>
            </div>

            <div class="form-group">
                <label class="form-label" for="smsPassword">密码</label>
                <input type="password" id="smsPassword" name="password" class="form-input" placeholder="请输入密码(6-20位字符)" required>
                <div class="password-strength">
                    <div class="strength-bar" id="smsPasswordStrength"></div>
                </div>
                <div class="error-message" id="smsPasswordError">密码格式不正确</div>
            </div>

            <div class="form-group">
                <label class="form-label" for="confirmPasswordSMS">确认密码</label>
                <input type="password" id="confirmPasswordSMS" name="confirmPassword" class="form-input" placeholder="请再次输入密码" required>
                <div class="error-message" id="smsConfirmPasswordError">两次输入的密码不一致</div>
            </div>

            <div class="form-group">
                <label class="form-label" for="smsCode">验证码</label>
                <div class="captcha-container">
                    <input type="text" id="smsCode" name="code" class="form-input captcha-input" placeholder="请输入验证码" required maxlength="6">
                    <button type="button" class="captcha-btn" id="getSMSCode">获取验证码</button>
                </div>
                <div class="error-message" id="smsCodeError">验证码格式不正确</div>
            </div>

            <div class="terms">
                注册即表示同意 <a href="#">用户协议</a> 和 <a href="#">隐私政策</a>
            </div>

            <button type="submit" class="register-btn" id="smsRegisterBtn">注册</button>
        </form>
    </div>

    <div class="register-type-switch">
        使用
        <span class="switch-btn" id="switchBtn">短信验证码注册</span>
    </div>

    <div class="login-link">
        已有账号？<a href="login">立即登录</a>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const switchBtn = document.getElementById('switchBtn');
        const normalRegister = document.querySelector('.normal-register');
        const smsRegister = document.querySelector('.sms-register');
        const registerTitle = document.querySelector('.register-title');
        const registerSubtitle = document.querySelector('.register-subtitle');
        const getSMSCodeBtn = document.getElementById('getSMSCode');

        // 表单元素
        const normalRegisterForm = document.getElementById('normalRegisterForm');
        const smsRegisterForm = document.getElementById('smsRegisterForm');
        const normalRegisterBtn = document.getElementById('normalRegisterBtn');
        const smsRegisterBtn = document.getElementById('smsRegisterBtn');

        let isSMSRegister = false;
        let countdown = 0; // 恢复使用计数器变量
        let countdownInterval;

        // 初始化页面状态
        updateFormVisibility();
        checkExistingCountdown();

        // 切换注册方式
        switchBtn.addEventListener('click', function() {
            isSMSRegister = !isSMSRegister;
            updateFormVisibility();
            resetFormValidation();
        });

        function updateFormVisibility() {
            if (isSMSRegister) {
                normalRegister.classList.remove('active');
                smsRegister.classList.add('active');
                registerTitle.textContent = '短信注册';
                registerSubtitle.textContent = '请输入您的手机号和验证码';
                switchBtn.textContent = '普通账号注册';
            } else {
                smsRegister.classList.remove('active');
                normalRegister.classList.add('active');
                registerTitle.textContent = '用户注册';
                registerSubtitle.textContent = '请选择注册方式并填写信息';
                switchBtn.textContent = '短信验证码注册';
            }
        }

        function resetFormValidation() {
            // 重置所有错误状态
            document.querySelectorAll('.error-message').forEach(el => {
                el.style.display = 'none';
            });
            document.querySelectorAll('.form-input').forEach(el => {
                el.classList.remove('input-error');
            });
            document.querySelectorAll('.register-btn').forEach(el => {
                el.disabled = false;
            });
        }

        // 用户名验证
        const usernameInput = document.getElementById('username');
        const usernameError = document.getElementById('usernameError');

        usernameInput.addEventListener('blur', function() {
            const username = usernameInput.value.trim();
            if (!username) {
                showError(usernameInput, usernameError, '用户名不能为空');
            } else if (!/^[a-zA-Z0-9_]{4,16}$/.test(username)) {
                showError(usernameInput, usernameError, '用户名格式不正确(4-16位字母、数字或下划线)');
            } else {
                hideError(usernameInput, usernameError);
            }
        });

        // 手机号验证
        const phoneInput = document.getElementById('phone');
        const phoneError = document.getElementById('phoneError');

        phoneInput.addEventListener('blur', function() {
            const phone = phoneInput.value.trim();
            if (!phone) {
                showError(phoneInput, phoneError, '手机号码不能为空');
            } else if (!/^1[3-9]\d{9}$/.test(phone)) {
                showError(phoneInput, phoneError, '手机号码格式不正确');
            } else {
                hideError(phoneInput, phoneError);
            }
        });

        // 密码验证和强度检测
        const passwordInput = document.getElementById('password');
        const passwordError = document.getElementById('passwordError');
        const passwordStrength = document.getElementById('passwordStrength');

        const smsPasswordInput = document.getElementById('smsPassword');
        const smsPasswordError = document.getElementById('smsPasswordError');
        const smsPasswordStrength = document.getElementById('smsPasswordStrength');

        function checkPasswordStrength(password, strengthElement) {
            let strength = 0;

            if (password.length >= 6) strength++;
            if (password.length >= 8) strength++;
            if (/[a-z]/.test(password)) strength++;
            if (/[A-Z]/.test(password)) strength++;
            if (/[0-9]/.test(password)) strength++;
            if (/[^a-zA-Z0-9]/.test(password)) strength++;

            // 更新强度条
            strengthElement.className = 'strength-bar';
            if (password.length === 0) {
                strengthElement.style.width = '0';
            } else if (strength <= 2) {
                strengthElement.classList.add('strength-weak');
            } else if (strength <= 4) {
                strengthElement.classList.add('strength-medium');
            } else {
                strengthElement.classList.add('strength-strong');
            }
        }

        passwordInput.addEventListener('input', function() {
            const password = passwordInput.value;
            checkPasswordStrength(password, passwordStrength);

            if (!password) {
                showError(passwordInput, passwordError, '密码不能为空');
            } else if (password.length < 6 || password.length > 20) {
                showError(passwordInput, passwordError, '密码长度应为6-20位');
            } else {
                hideError(passwordInput, passwordError);
            }
        });

        smsPasswordInput.addEventListener('input', function() {
            const password = smsPasswordInput.value;
            checkPasswordStrength(password, smsPasswordStrength);

            if (!password) {
                showError(smsPasswordInput, smsPasswordError, '密码不能为空');
            } else if (password.length < 6 || password.length > 20) {
                showError(smsPasswordInput, smsPasswordError, '密码长度应为6-20位');
            } else {
                hideError(smsPasswordInput, smsPasswordError);
            }

            // 实时检查密码一致性
            checkPasswordConsistency();
        });

        // 确认密码验证
        const confirmPasswordNormal = document.getElementById('confirmPasswordNormal');
        const confirmPasswordError = document.getElementById('confirmPasswordError');

        const confirmPasswordSMS = document.getElementById('confirmPasswordSMS');
        const smsConfirmPasswordError = document.getElementById('smsConfirmPasswordError');

        confirmPasswordNormal.addEventListener('blur', function() {
            const password = passwordInput.value;
            const confirmPassword = confirmPasswordNormal.value;

            if (!confirmPassword) {
                showError(confirmPasswordNormal, confirmPasswordError, '请确认密码');
            } else if (password !== confirmPassword) {
                showError(confirmPasswordNormal, confirmPasswordError, '两次输入的密码不一致');
            } else {
                hideError(confirmPasswordNormal, confirmPasswordError);
            }
        });

        confirmPasswordSMS.addEventListener('input', function() {
            checkPasswordConsistency();
        });

        confirmPasswordSMS.addEventListener('blur', function() {
            checkPasswordConsistency();
        });

        // 检查密码一致性函数
        function checkPasswordConsistency() {
            const password = smsPasswordInput.value;
            const confirmPassword = confirmPasswordSMS.value;

            if (password && confirmPassword && password !== confirmPassword) {
                showError(confirmPasswordSMS, smsConfirmPasswordError, '两次输入的密码不一致');
                return false;
            } else {
                hideError(confirmPasswordSMS, smsConfirmPasswordError);
                return true;
            }
        }

        // 验证码验证
        const smsCodeInput = document.getElementById('smsCode');
        const smsCodeError = document.getElementById('smsCodeError');

        smsCodeInput.addEventListener('blur', function() {
            const code = smsCodeInput.value.trim();
            if (!code) {
                showError(smsCodeInput, smsCodeError, '验证码不能为空');
            } else if (!/^\d{6}$/.test(code)) {
                showError(smsCodeInput, smsCodeError, '验证码格式不正确');
            } else {
                hideError(smsCodeInput, smsCodeError);
            }
        });

        // 错误显示/隐藏函数
        function showError(inputElement, errorElement, message) {
            inputElement.classList.add('input-error');
            errorElement.textContent = message;
            errorElement.style.display = 'block';
        }

        function hideError(inputElement, errorElement) {
            inputElement.classList.remove('input-error');
            errorElement.style.display = 'none';
        }

        // 更新倒计时显示
        function updateCountdownDisplay() {
            if (countdown > 0) {
                getSMSCodeBtn.textContent = countdown + '秒后重新获取';
                console.log('倒计时更新: ' + countdown + ' 秒'); // 添加控制台日志，便于调试
            } else {
                getSMSCodeBtn.textContent = '获取验证码';
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
                    getSMSCodeBtn.disabled = true;
                    updateCountdownDisplay();

                    // 恢复倒计时间隔
                    countdownInterval = setInterval(function() {
                        countdown--;
                        updateCountdownDisplay();
                        localStorage.setItem('smsCountdown', countdown.toString());
                        localStorage.setItem('smsCountdownTime', Date.now().toString());

                        if (countdown <= 0) {
                            clearInterval(countdownInterval);
                            getSMSCodeBtn.disabled = false;
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

        // 获取验证码 - 使用简单计数器，确保数字显示
        getSMSCodeBtn.addEventListener('click', function() {
            if (countdown > 0) {
                alert('请等待' + countdown + '秒后重新获取验证码');
                return;
            }

            const phone = phoneInput.value.trim();

            // 检查手机号是否为空
            if (!phone) {
                showError(phoneInput, phoneError, '请输入手机号码');
                phoneInput.focus();
                return;
            }

            // 检查手机号格式
            if (!/^1[3-9]\d{9}$/.test(phone)) {
                showError(phoneInput, phoneError, '请输入有效的手机号码');
                phoneInput.focus();
                return;
            }

            // 检查密码是否已填写
            const password = smsPasswordInput.value;
            const confirmPassword = confirmPasswordSMS.value;

            if (!password) {
                showError(smsPasswordInput, smsPasswordError, '请先填写密码');
                smsPasswordInput.focus();
                return;
            }

            if (!confirmPassword) {
                showError(confirmPasswordSMS, smsConfirmPasswordError, '请确认密码');
                confirmPasswordSMS.focus();
                return;
            }

            // 检查密码长度
            if (password.length < 6 || password.length > 20) {
                showError(smsPasswordInput, smsPasswordError, '密码长度应为6-20位');
                smsPasswordInput.focus();
                return;
            }

            // 检查密码一致性
            if (!checkPasswordConsistency()) {
                confirmPasswordSMS.focus();
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
            xhr.open('GET', 'register?action=sendCode&phone=' + encodeURIComponent(phone) + '&codeType=REGISTER', true);
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        const response = xhr.responseText;
                        if (response.startsWith('success')) {
                            // 开始60秒倒计时
                            countdown = 60;
                            getSMSCodeBtn.disabled = true;
                            localStorage.setItem('smsCountdown', countdown.toString());
                            localStorage.setItem('smsCountdownTime', Date.now().toString());
                            updateCountdownDisplay();

                            countdownInterval = setInterval(function() {
                                countdown--;
                                updateCountdownDisplay();
                                localStorage.setItem('smsCountdown', countdown.toString());
                                localStorage.setItem('smsCountdownTime', Date.now().toString());

                                if (countdown <= 0) {
                                    clearInterval(countdownInterval);
                                    getSMSCodeBtn.disabled = false;
                                    localStorage.removeItem('smsCountdown');
                                    localStorage.removeItem('smsCountdownTime');
                                }
                            }, 1000);

                            // 从响应中提取验证码（仅用于测试）
                            const code = response.split(':')[1];
                            console.log('测试用验证码:', code);
                            alert('验证码已发送到您的手机，请注意查收。验证码有效期为60秒。测试验证码: ' + code);
                        } else {
                            // 处理服务器返回的错误信息
                            alert('获取验证码失败: ' + response);
                        }
                    } else {
                        alert('网络错误，请重试');
                    }
                }
            };

            xhr.send();
        });

        // 表单提交验证
        normalRegisterForm.addEventListener('submit', function(e) {
            if (!validateNormalForm()) {
                e.preventDefault();
            }
        });

        smsRegisterForm.addEventListener('submit', function(e) {
            if (!validateSMSForm()) {
                e.preventDefault();
            }
        });

        function validateNormalForm() {
            let isValid = true;

            // 验证用户名
            const username = usernameInput.value.trim();
            if (!username || !/^[a-zA-Z0-9_]{4,16}$/.test(username)) {
                showError(usernameInput, usernameError, '用户名格式不正确(4-16位字母、数字或下划线)');
                isValid = false;
            }

            // 验证密码
            const password = passwordInput.value;
            if (!password || password.length < 6 || password.length > 20) {
                showError(passwordInput, passwordError, '密码长度应为6-20位');
                isValid = false;
            }

            // 验证确认密码
            const confirmPassword = confirmPasswordNormal.value;
            if (!confirmPassword || password !== confirmPassword) {
                showError(confirmPasswordNormal, confirmPasswordError, '两次输入的密码不一致');
                isValid = false;
            }

            return isValid;
        }

        function validateSMSForm() {
            let isValid = true;

            // 验证手机号
            const phone = phoneInput.value.trim();
            if (!phone || !/^1[3-9]\d{9}$/.test(phone)) {
                showError(phoneInput, phoneError, '手机号码格式不正确');
                isValid = false;
            }

            // 验证密码
            const password = smsPasswordInput.value;
            if (!password || password.length < 6 || password.length > 20) {
                showError(smsPasswordInput, smsPasswordError, '密码长度应为6-20位');
                isValid = false;
            }

            // 验证确认密码
            const confirmPassword = confirmPasswordSMS.value;
            if (!confirmPassword || password !== confirmPassword) {
                showError(confirmPasswordSMS, smsConfirmPasswordError, '两次输入的密码不一致');
                isValid = false;
            }

            // 验证验证码
            const code = smsCodeInput.value.trim();
            if (!code || !/^\d{6}$/.test(code)) {
                showError(smsCodeInput, smsCodeError, '验证码格式不正确');
                isValid = false;
            }

            return isValid;
        }
    });
</script>
</body>
</html>