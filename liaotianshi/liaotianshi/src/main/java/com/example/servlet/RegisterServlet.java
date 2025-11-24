// RegisterServlet.java - 修复编码问题
package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.util.VerificationCodeUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置请求和响应编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String type = request.getParameter("type");
        String message = "";
        boolean success = false;

        if ("normal".equals(type)) {
            // 普通注册
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");

            if (!password.equals(confirmPassword)) {
                message = "两次输入的密码不一致";
            } else if (userDAO.isUsernameExists(username)) {
                message = "用户名已存在";
            } else {
                success = userDAO.registerNormal(new com.example.model.User(username, password, null));
                if (success) {
                    message = "注册成功";
                    response.sendRedirect("login.jsp?type=normal&message=" +
                            java.net.URLEncoder.encode(message, "UTF-8"));
                    return;
                } else {
                    message = "注册失败，请稍后重试";
                }
            }

        } else if ("sms".equals(type)) {
            // 短信注册
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String code = request.getParameter("code");

            System.out.println("开始验证验证码: phone=" + phone + ", code=" + code);

            if (!password.equals(confirmPassword)) {
                message = "两次输入的密码不一致";
            } else if (userDAO.isPhoneExists(phone)) {
                message = "手机号已注册";
            } else if (!VerificationCodeUtil.verifyCode(phone, code, "REGISTER")) {
                message = "验证码错误或已过期";
            } else {
                success = userDAO.registerBySMS(phone, password);
                if (success) {
                    message = "注册成功";
                    response.sendRedirect("login.jsp?type=sms&message=" +
                            java.net.URLEncoder.encode(message, "UTF-8"));
                    return;
                } else {
                    message = "注册失败，请稍后重试";
                }
            }
        }

        // 如果注册失败，返回注册页面并显示错误信息
        request.setAttribute("message", message);
        request.setAttribute("type", type);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置请求和响应编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 处理获取验证码请求
        String action = request.getParameter("action");
        if ("sendCode".equals(action)) {
            String phone = request.getParameter("phone");
            String codeType = request.getParameter("codeType");

            // 使用PrintWriter确保正确输出中文
            PrintWriter out = response.getWriter();

            if (phone == null || phone.trim().isEmpty()) {
                out.write("请输入手机号");
                return;
            }

            if (!phone.matches("^1[3-9]\\d{9}$")) {
                out.write("手机号格式不正确");
                return;
            }

            if ("REGISTER".equals(codeType) && userDAO.isPhoneExists(phone)) {
                out.write("手机号已注册");
                return;
            }

            if ("LOGIN".equals(codeType) && !userDAO.isPhoneExists(phone)) {
                out.write("手机号未注册");
                return;
            }

            // 生成验证码
            String code = VerificationCodeUtil.generateCode(6);

            // 保存验证码到数据库
            boolean saved = VerificationCodeUtil.saveCode(phone, code, codeType);

            if (saved) {
                // 实际项目中这里应该调用短信服务商API发送短信
                System.out.println("发送验证码到 " + phone + ": " + code);

                // 同时将验证码返回给前端用于测试
                out.write("success:" + code);
            } else {
                out.write("发送失败，请重试");
            }
        } else {
            // 显示注册页面
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}