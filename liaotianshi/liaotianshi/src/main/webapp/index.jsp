<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>首页</title>
  <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="container">
  <%
    String username = (String) session.getAttribute("username");
    String phoneNumber = (String) session.getAttribute("phoneNumber");
    if (username != null) {
  %>
  <h2>欢迎, <%= username %>!</h2>

  <div class="user-info">
    <%
      if (phoneNumber != null && !phoneNumber.isEmpty()) {
    %>
    <p>绑定手机号: <strong><%= phoneNumber %></strong></p>
    <p>您可以使用以下方式登录：</p>
    <ul>
      <li><strong>手机账号登录</strong> - 手机号 + 密码</li>
      <li><strong>普通账号登录</strong> - 用户名 + 密码</li>
    </ul>
    <%
    } else {
    %>
    <p>您尚未绑定手机号，只能使用<strong>普通账号登录</strong></p>
    <p>如需使用手机号登录，请通过<strong>短信验证注册</strong>重新注册账号</p>
    <%
      }
    %>
  </div>

  <p>您已成功登录系统。</p>

  <div class="button-group">
    <form action="logout" method="post" class="inline-form">
      <button type="submit" class="logout-btn">退出登录</button>
    </form>

    <form action="deleteAccount" method="post" class="inline-form">
      <button type="submit" class="delete-btn" onclick="return confirm('确定要注销账号吗？此操作不可逆！')">注销账号</button>
    </form>
  </div>
  <%
    } else {
      response.sendRedirect("login.jsp");
    }
  %>

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
</div>

<script>
  function confirmDelete() {
    return confirm("确定要注销账号吗？此操作将永久删除您的账户，且无法恢复！");
  }
</script>
</body>
</html>