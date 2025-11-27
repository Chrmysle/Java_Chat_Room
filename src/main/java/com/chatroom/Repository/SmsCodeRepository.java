package com.chatroom.Repository;

import com.chatroom.model.SmsCode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public class SmsCodeRepository {

    private final JdbcTemplate jdbcTemplate;

    public SmsCodeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class SmsCodeRowMapper implements RowMapper<SmsCode> {
        @Override
        public SmsCode mapRow(ResultSet rs, int rowNum) throws SQLException {
            SmsCode smsCode = new SmsCode();
            smsCode.setId(rs.getLong("id"));
            smsCode.setPhone(rs.getString("phone"));
            smsCode.setCode(rs.getString("code"));

            Timestamp timestamp = rs.getTimestamp("create_time");
            if (timestamp != null) {
                smsCode.setCreateTime(timestamp.toLocalDateTime());
            }
            return smsCode;
        }
    }

    public SmsCode findByPhoneAndCode(String phone, String code) {
        String sql = "SELECT id, phone, code, create_time FROM sms_codes WHERE phone = ? AND code = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new SmsCodeRowMapper(), phone, code);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean existsByPhoneAndCode(String phone, String code) {
        String sql = "SELECT COUNT(*) FROM sms_codes WHERE phone = ? AND code = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phone, code);
        return count != null && count > 0;
    }

    public boolean save(SmsCode smsCode) {
        String sql = "INSERT INTO sms_codes (phone, code, create_time) VALUES (?, ?, ?)";
        try {
            int result = jdbcTemplate.update(sql,
                    smsCode.getPhone(),
                    smsCode.getCode(),
                    Timestamp.valueOf(smsCode.getCreateTime()));
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(SmsCode smsCode) {
        String sql = "DELETE FROM sms_codes WHERE id = ?";
        try {
            int result = jdbcTemplate.update(sql, smsCode.getId());
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteByPhone(String phone) {
        String sql = "DELETE FROM sms_codes WHERE phone = ?";
        jdbcTemplate.update(sql, phone);
    }

    public void deleteExpiredCodes(LocalDateTime expireTime) {
        String sql = "DELETE FROM sms_codes WHERE create_time < ?";
        jdbcTemplate.update(sql, Timestamp.valueOf(expireTime));
    }

    public long countByPhoneAndCreateTimeAfter(String phone, LocalDateTime startTime) {
        String sql = "SELECT COUNT(*) FROM sms_codes WHERE phone = ? AND create_time > ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, phone, Timestamp.valueOf(startTime));
        return count != null ? count : 0;
    }
}