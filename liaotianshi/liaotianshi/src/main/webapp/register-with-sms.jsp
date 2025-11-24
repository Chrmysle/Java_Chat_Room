<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>手机号注册</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="container">
    <h2>手机号注册</h2>
    <p class="info">
        注册后可以使用<strong>手机号</strong>登录系统
    </p>
    <form id="smsRegisterForm" action="register" method="post">
        <input type="hidden" name="registerType" value="sms">

        <div class="form-group">
            <label for="phoneNumber">手机号:</label>
            <input type="tel" id="phoneNumber" name="phoneNumber" required maxlength="11" placeholder="请输入手机号">
        </div>

        <div class="form-group">
            <label for="password">设置密码:</label>
            <input type="password" id="password" name="password" required maxlength="100" placeholder="请设置登录密码">
        </div>

        <div class="form-group">
            <label for="confirmPassword">确认密码:</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required maxlength="100" placeholder="请再次输入密码">
        </div>

        <div class="form-group">
            <label for="smsCode">验证码:</label>
            <div class="sms-code-group">
                <input type="text" id="smsCode" name="smsCode" required maxlength="6" placeholder="请输入验证码">
                <button type="button" id="getCodeBtn" class="get-code-btn">获取验证码</button>
            </div>
        </div>

        <button type="submit">注册</button>
        <p class="link">
            <a href="register.jsp">普通注册</a> |
            <a href="login.jsp">立即登录</a>
        </p>
    </form>

    <%-- 显示错误信息 --%>
    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
    <div class="error-message">
        <%= errorMessage %>
    </div>
    <%
        }
    %>

    <%-- 显示成功信息 --%>
    <%
        String successMessage = (String) request.getAttribute("successMessage");
        if (successMessage != null) {
    %>
    <div class="success-message">
        <%= successMessage %><br>
        <small>
            您可以使用<strong>手机号</strong>登录系统<br>
            手机号：<strong><%= request.getParameter("phoneNumber") != null ? request.getParameter("phoneNumber") : "" %></strong>
        </small>
    </div>
    <%
        }
    %>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        console.log('页面加载完成，初始化短信注册功能');

        var countdown = 0;
        var countdownInterval;
        var getCodeBtn = document.getElementById('getCodeBtn');
        var smsRegisterForm = document.getElementById('smsRegisterForm');
        var phoneNumberInput = document.getElementById('phoneNumber');
        var passwordInput = document.getElementById('password');
        var confirmPasswordInput = document.getElementById('confirmPassword');
        var smsCodeInput = document.getElementById('smsCode');

        // 检查元素是否存在
        if (!getCodeBtn) {
            console.error('获取验证码按钮未找到');
            return;
        }
        if (!phoneNumberInput) {
            console.error('手机号输入框未找到');
            return;
        }

        console.log('所有必需元素已找到，绑定事件');

        // 获取验证码按钮点击事件
        getCodeBtn.addEventListener('click', function() {
            console.log('获取验证码按钮被点击');

            var phoneNumber = phoneNumberInput.value.trim();
            console.log('输入的手机号:', phoneNumber);

            // 验证手机号
            if (!validatePhoneNumber(phoneNumber)) {
                return;
            }

            // 禁用按钮并开始倒计时
            startCountdown();

            // 发送AJAX请求获取验证码
            sendSmsCodeRequest(phoneNumber);
        });

        // 表单提交事件
        smsRegisterForm.addEventListener('submit', function(e) {
            if (!validateForm()) {
                e.preventDefault();
            }
        });

        /**
         * 验证手机号格式
         */
        function validatePhoneNumber(phoneNumber) {
            console.log('验证手机号:', phoneNumber);

            if (!phoneNumber) {
                showAlert('请输入手机号');
                return false;
            }

            if (!/^1[3-9]\d{9}$/.test(phoneNumber)) {
                showAlert('请输入正确的手机号');
                return false;
            }

            console.log('手机号验证通过');
            return true;
        }

        /**
         * 发送短信验证码请求
         */
        function sendSmsCodeRequest(phoneNumber) {
            console.log('准备发送短信验证码请求，手机号:', phoneNumber);

            var xhr = new XMLHttpRequest();
            var url = 'sms?action=send&phoneNumber=' + encodeURIComponent(phoneNumber);
            console.log('请求URL:', url);

            xhr.open('POST', 'sms', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

            xhr.onreadystatechange = function() {
                console.log('XHR状态:', xhr.readyState, 'HTTP状态:', xhr.status);

                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        console.log('收到响应:', xhr.responseText);
                        try {
                            var response = JSON.parse(xhr.responseText);
                            if (response.success) {
                                showAlert('验证码已发送到您的手机');
                            } else {
                                showAlert('发送失败: ' + response.message);
                                resetCountdown();
                            }
                        } catch (e) {
                            console.error('解析响应失败:', e);
                            showAlert('响应解析失败，请重试');
                            resetCountdown();
                        }
                    } else {
                        console.error('请求失败:', xhr.status, xhr.statusText);
                        showAlert('请求失败，请检查网络连接');
                        resetCountdown();
                    }
                }
            };

            xhr.onerror = function() {
                console.error('网络错误');
                showAlert('网络错误，请检查网络连接');
                resetCountdown();
            };

            var params = 'action=send&phoneNumber=' + encodeURIComponent(phoneNumber);
            console.log('发送参数:', params);
            xhr.send(params);
        }

        /**
         * 开始倒计时
         */
        function startCountdown() {
            console.log('开始倒计时');
            countdown = 60;
            getCodeBtn.disabled = true;
            getCodeBtn.textContent = countdown + '秒后重新发送';

            countdownInterval = setInterval(function() {
                countdown--;
                if (countdown > 0) {
                    getCodeBtn.textContent = countdown + '秒后重新发送';
                } else {
                    resetCountdown();
                }
            }, 1000);
        }

        /**
         * 重置倒计时
         */
        function resetCountdown() {
            console.log('重置倒计时');
            if (countdownInterval) {
                clearInterval(countdownInterval);
                countdownInterval = null;
            }

            if (getCodeBtn) {
                getCodeBtn.disabled = false;
                getCodeBtn.textContent = '获取验证码';
            }

            countdown = 0;
        }

        /**
         * 验证表单
         */
        function validateForm() {
            var phoneNumber = phoneNumberInput.value.trim();
            var password = passwordInput.value.trim();
            var confirmPassword = confirmPasswordInput.value.trim();
            var smsCode = smsCodeInput.value.trim();

            if (!validatePhoneNumber(phoneNumber)) {
                phoneNumberInput.focus();
                return false;
            }

            if (!password) {
                showAlert('请输入密码');
                passwordInput.focus();
                return false;
            }

            if (password.length < 6) {
                showAlert('密码长度不能少于6位');
                passwordInput.focus();
                return false;
            }

            if (!confirmPassword) {
                showAlert('请确认密码');
                confirmPasswordInput.focus();
                return false;
            }

            if (password !== confirmPassword) {
                showAlert('两次输入的密码不一致');
                confirmPasswordInput.focus();
                return false;
            }

            if (!smsCode) {
                showAlert('请输入验证码');
                smsCodeInput.focus();
                return false;
            }

            if (!/^\d{6}$/.test(smsCode)) {
                showAlert('验证码必须是6位数字');
                smsCodeInput.focus();
                return false;
            }

            return true;
        }

        /**
         * 显示提示信息
         */
        function showAlert(message) {
            alert(message);
        }

        console.log('短信注册功能初始化完成');
    });
</script>
</body>
</html>