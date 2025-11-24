package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.util.VerificationCodeUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type");
        String message = "";

        if ("normal".equals(type)) {
            // 普通登录
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            User user = userDAO.login(username, password);
            // 普通登录成功部分
            if (user != null) {
                // 登录成功，保存用户信息到session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("loginType", "normal"); // 保存登录方式
                response.sendRedirect("success.jsp");
                return;
            }

// 短信登录成功部分
            if (user != null) {
                // 登录成功，保存用户信息到session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("loginType", "sms"); // 保存登录方式
                response.sendRedirect("success.jsp");
                return;
            }else {
                message = "用户名或密码错误";
            }

        } else if ("sms".equals(type)) {
            // 短信登录
            String phone = request.getParameter("phone");
            String code = request.getParameter("code");

            if (!VerificationCodeUtil.verifyCode(phone, code, "LOGIN")) {
                message = "验证码错误或已过期";
            } else {
                User user = userDAO.getUserByPhone(phone);
                if (user != null) {
                    // 登录成功，保存用户信息到session
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    response.sendRedirect("success.jsp");
                    return;
                } else {
                    message = "登录失败，请重试";
                }
            }
        }


        // 如果登录失败，返回登录页面并显示错误信息
        request.setAttribute("message", message);
        request.setAttribute("type", type);
        request.getRequestDispatcher("login.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 显示登录页面
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}