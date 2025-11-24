package com.example.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            // 获取登录方式，用于重定向
            String loginType = (String) session.getAttribute("loginType");
            session.invalidate();

            // 根据登录方式重定向到相应的登录页面
            if ("sms".equals(loginType)) {
                response.sendRedirect("login.jsp?type=sms&message=" +
                        java.net.URLEncoder.encode("您已成功退出登录", "UTF-8"));
            } else {
                response.sendRedirect("login.jsp?type=normal&message=" +
                        java.net.URLEncoder.encode("您已成功退出登录", "UTF-8"));
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}