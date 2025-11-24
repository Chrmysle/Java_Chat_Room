// VerificationCodeUtil.java - 修改验证码保存时间为60秒
package com.example.util;

import java.sql.*;
import java.util.Random;

public class VerificationCodeUtil {

    // 生成验证码
    public static String generateCode(int length) {
        String chars = "0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }

    // 保存验证码到数据库 - 修改为60秒过期
    public static boolean saveCode(String phone, String code, String type) {
        // 使用数据库当前时间作为基准，设置60秒后过期
        String sql = "INSERT INTO verification_codes (phone, code, type, expire_time) " +
                "VALUES (?, ?, ?, DATE_ADD(NOW(), INTERVAL 60 SECOND))";

        // 先删除该手机号的旧验证码
        deleteCode(phone, type);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            pstmt.setString(2, code);
            pstmt.setString(3, type);

            int rows = pstmt.executeUpdate();
            System.out.println("保存验证码: phone=" + phone + ", code=" + code + ", type=" + type +
                    ", 过期时间=60秒, 结果=" + (rows > 0));
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("保存验证码失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 验证验证码 - 修改为检查60秒内的验证码
    public static boolean verifyCode(String phone, String code, String type) {
        String sql = "SELECT * FROM verification_codes WHERE phone = ? AND code = ? AND type = ? AND expire_time > NOW()";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            pstmt.setString(2, code);
            pstmt.setString(3, type);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // 验证成功后删除验证码
                deleteCode(phone, type);
                System.out.println("验证码验证成功: phone=" + phone + ", code=" + code + ", type=" + type);
                return true;
            } else {
                System.out.println("验证码验证失败: phone=" + phone + ", code=" + code + ", type=" + type);
                // 记录详细失败原因
                checkVerificationCodeStatus(phone, type);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("验证验证码时发生错误: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 检查验证码状态 - 用于调试
    private static void checkVerificationCodeStatus(String phone, String type) {
        // 避免使用保留关键字作为别名
        String sql = "SELECT code, expire_time, NOW() as current_db_time, " +
                "TIMESTAMPDIFF(SECOND, NOW(), expire_time) as remaining_seconds " +
                "FROM verification_codes WHERE phone = ? AND type = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            pstmt.setString(2, type);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String dbCode = rs.getString("code");
                Timestamp expireTime = rs.getTimestamp("expire_time");
                Timestamp currentTime = rs.getTimestamp("current_db_time");
                int remainingSeconds = rs.getInt("remaining_seconds");

                System.out.println("数据库中的验证码: " + dbCode);
                System.out.println("验证码过期时间: " + expireTime);
                System.out.println("数据库当前时间: " + currentTime);
                System.out.println("剩余有效时间: " + remainingSeconds + "秒");
                System.out.println("验证码是否过期: " + (remainingSeconds <= 0));
            } else {
                System.out.println("未找到该手机号的验证码记录");
            }

        } catch (SQLException e) {
            System.err.println("检查验证码状态时发生错误: " + e.getMessage());
        }
    }

    // 检查所有验证码记录
    private static void checkAllVerificationCodes() {
        // 避免使用保留关键字作为别名
        String sql = "SELECT phone, code, type, expire_time, NOW() as current_db_time, " +
                "TIMESTAMPDIFF(SECOND, NOW(), expire_time) as remaining_seconds " +
                "FROM verification_codes";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            System.out.println("=== 所有验证码记录 ===");
            int count = 0;
            while (rs.next()) {
                count++;
                String phone = rs.getString("phone");
                String code = rs.getString("code");
                String type = rs.getString("type");
                Timestamp expireTime = rs.getTimestamp("expire_time");
                Timestamp currentTime = rs.getTimestamp("current_db_time");
                int remainingSeconds = rs.getInt("remaining_seconds");

                System.out.println("记录 " + count + ": phone=" + phone + ", code=" + code +
                        ", type=" + type + ", expire_time=" + expireTime +
                        ", 当前时间=" + currentTime + ", 剩余时间=" + remainingSeconds + "秒");
            }
            System.out.println("总记录数: " + count);

        } catch (SQLException e) {
            System.err.println("检查所有验证码记录时发生错误: " + e.getMessage());
        }
    }

    // 删除验证码
    private static void deleteCode(String phone, String type) {
        String sql = "DELETE FROM verification_codes WHERE phone = ? AND type = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            pstmt.setString(2, type);
            int deleted = pstmt.executeUpdate();
            if (deleted > 0) {
                System.out.println("删除验证码记录: phone=" + phone + ", type=" + type);
            }

        } catch (SQLException e) {
            System.err.println("删除验证码时发生错误: " + e.getMessage());
        }
    }
}