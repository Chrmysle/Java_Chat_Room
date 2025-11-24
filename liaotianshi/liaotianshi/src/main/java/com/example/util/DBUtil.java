package com.example.util;

import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/userdb?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 测试连接是否有效
        try {
            if (conn != null && !conn.isClosed()) {
                // 执行一个简单的查询测试连接 - 避免使用保留关键字作为别名
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT NOW() as db_current_time");
                if (rs.next()) {
                    System.out.println("数据库连接成功，当前时间: " + rs.getTimestamp("db_current_time"));
                }
                rs.close();
                stmt.close();
            }
        } catch (SQLException e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
            throw e;
        }

        return conn;
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 测试数据库连接
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return true;
        } catch (SQLException e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
            return false;
        }
    }
}