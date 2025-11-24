// UserDAO.java
package com.example.dao;

import com.example.model.User;
import com.example.util.DBUtil;

import java.sql.*;
import java.util.UUID;

public class UserDAO {

    // 删除用户
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            int rows = pstmt.executeUpdate();
            System.out.println("删除用户: id=" + userId + ", 结果=" + (rows > 0));
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("删除用户失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    // 检查用户名是否存在
    public boolean isUsernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 检查手机号是否存在
    public boolean isPhoneExists(String phone) {
        String sql = "SELECT id FROM users WHERE phone = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 普通注册
    public boolean registerNormal(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 短信注册
    public boolean registerBySMS(String phone, String password) {
        // 生成随机用户名
        String randomUsername = generateRandomUsername();

        String sql = "INSERT INTO users (username, password, phone) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, randomUsername);
            pstmt.setString(2, password);
            pstmt.setString(3, phone);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 生成随机用户名
    private String generateRandomUsername() {
        // 使用UUID生成随机字符串，取前8位加上随机数字
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        int randomNum = (int)(Math.random() * 1000);
        return "user_" + uuid + randomNum;
    }

    // 用户登录验证
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPhone(rs.getString("phone"));
                user.setCreateTime(rs.getTimestamp("create_time"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 通过手机号获取用户
    public User getUserByPhone(String phone) {
        String sql = "SELECT * FROM users WHERE phone = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPhone(rs.getString("phone"));
                user.setCreateTime(rs.getTimestamp("create_time"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}