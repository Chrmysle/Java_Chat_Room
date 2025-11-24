package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/deleteAccount")
public class DeleteAccountServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null) {
            sendErrorResponse(out, "用户未登录");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            sendErrorResponse(out, "用户未登录");
            return;
        }

        try {
            int userId = user.getId();
            boolean success = userDAO.deleteUser(userId);

            if (success) {
                // 注销成功，清除session
                session.invalidate();
                sendSuccessResponse(out, "账号注销成功");
            } else {
                sendErrorResponse(out, "账号注销失败，请稍后重试");
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(out, "系统错误: " + e.getMessage());
        }
    }

    private void sendSuccessResponse(PrintWriter out, String message) {
        out.print("{\"success\": true, \"message\": \"" + message + "\"}");
        out.flush();
    }

    private void sendErrorResponse(PrintWriter out, String message) {
        out.print("{\"success\": false, \"message\": \"" + message + "\"}");
        out.flush();
    }
}