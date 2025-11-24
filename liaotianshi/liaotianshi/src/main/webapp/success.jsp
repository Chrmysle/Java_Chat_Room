<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login");
        return;
    }

    // 获取登录方式信息
    String loginType = (String) session.getAttribute("loginType");
    if (loginType == null) {
        loginType = "未知方式";
    }
%>
<html>
<head>
    <title>登录成功</title>
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

        .success-container {
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
            padding: 40px;
            text-align: center;
            max-width: 500px;
            width: 90%;
        }

        .success-icon {
            font-size: 80px;
            color: #4CAF50;
            margin-bottom: 20px;
        }

        .success-title {
            font-size: 28px;
            color: #333;
            margin-bottom: 15px;
        }

        .login-type {
            background-color: #e3f2fd;
            color: #1976d2;
            padding: 8px 15px;
            border-radius: 20px;
            font-size: 14px;
            display: inline-block;
            margin-bottom: 20px;
        }

        .welcome-message {
            color: #666;
            margin-bottom: 25px;
            font-size: 16px;
        }

        .user-info {
            background-color: #f5f7ff;
            padding: 20px;
            border-radius: 10px;
            margin: 20px 0;
            text-align: left;
        }

        .info-item {
            margin-bottom: 10px;
            font-size: 16px;
        }

        .info-label {
            font-weight: bold;
            color: #555;
        }

        .button-container {
            display: flex;
            gap: 15px;
            margin-top: 25px;
        }

        .logout-btn, .delete-account-btn {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
        }

        .logout-btn {
            background: linear-gradient(to right, #6a11cb, #2575fc);
            color: white;
        }

        .logout-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 15px rgba(37, 117, 252, 0.4);
        }

        .delete-account-btn {
            background: linear-gradient(to right, #ff416c, #ff4b2b);
            color: white;
        }

        .delete-account-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 7px 15px rgba(255, 75, 43, 0.4);
        }

        .confirmation-dialog {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }

        .dialog-content {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            max-width: 400px;
            width: 90%;
            text-align: center;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
        }

        .dialog-title {
            font-size: 20px;
            margin-bottom: 15px;
            color: #333;
        }

        .dialog-message {
            margin-bottom: 20px;
            color: #666;
        }

        .dialog-buttons {
            display: flex;
            gap: 10px;
        }

        .confirm-btn, .cancel-btn {
            flex: 1;
            padding: 10px;
            border: none;
            border-radius: 5px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
        }

        .confirm-btn {
            background-color: #ff4757;
            color: white;
        }

        .confirm-btn:hover {
            background-color: #ff3742;
        }

        .cancel-btn {
            background-color: #f1f2f6;
            color: #333;
        }

        .cancel-btn:hover {
            background-color: #dfe4ea;
        }

        @media (max-width: 480px) {
            .button-container {
                flex-direction: column;
            }

            .success-container {
                padding: 30px 20px;
            }
        }
    </style>
</head>
<body>
<div class="success-container">
    <div class="success-icon">✓</div>
    <h1 class="success-title">登录成功</h1>
    <div class="login-type">通过<%= loginType.equals("sms") ? "短信验证码" : "账号密码" %>登录</div>
    <p class="welcome-message">欢迎回来！您已成功登录系统。</p>

    <div class="user-info">
        <div class="info-item">
            <span class="info-label">用户名：</span> <%= user.getUsername() %>
        </div>
        <% if (user.getPhone() != null) { %>
        <div class="info-item">
            <span class="info-label">手机号：</span> <%= user.getPhone() %>
        </div>
        <% } %>
        <div class="info-item">
            <span class="info-label">注册时间：</span> <%= user.getCreateTime() %>
        </div>
    </div>

    <div class="button-container">
        <button class="logout-btn" onclick="location.href='logout'">退出登录</button>
        <button class="delete-account-btn" onclick="showDeleteConfirmation()">注销账号</button>
    </div>
</div>

<!-- 注销账号确认对话框 -->
<div class="confirmation-dialog" id="deleteConfirmationDialog">
    <div class="dialog-content">
        <h2 class="dialog-title">确认注销账号</h2>
        <p class="dialog-message">
            您确定要注销账号吗？此操作将永久删除您的账号和所有相关数据，且无法恢复。
        </p>
        <div class="dialog-buttons">
            <button class="confirm-btn" onclick="deleteAccount()">确认注销</button>
            <button class="cancel-btn" onclick="hideDeleteConfirmation()">取消</button>
        </div>
    </div>
</div>

<script>
    // 显示注销账号确认对话框
    function showDeleteConfirmation() {
        document.getElementById('deleteConfirmationDialog').style.display = 'flex';
    }

    // 隐藏注销账号确认对话框
    function hideDeleteConfirmation() {
        document.getElementById('deleteConfirmationDialog').style.display = 'none';
    }

    // 执行注销账号操作
    function deleteAccount() {
        // 发送注销账号请求
        const xhr = new XMLHttpRequest();
        xhr.open('POST', 'deleteAccount', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    const response = JSON.parse(xhr.responseText);
                    if (response.success) {
                        alert('账号注销成功');
                        window.location.href = 'register.jsp';
                    } else {
                        alert('账号注销失败: ' + response.message);
                        hideDeleteConfirmation();
                    }
                } else {
                    alert('网络错误，请重试');
                    hideDeleteConfirmation();
                }
            }
        };

        xhr.send();
    }

    // 点击对话框外部关闭对话框
    document.getElementById('deleteConfirmationDialog').addEventListener('click', function(e) {
        if (e.target === this) {
            hideDeleteConfirmation();
        }
    });
</script>
</body>
</html>