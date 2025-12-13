package com.chatroom.Repository;

import com.chatroom.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;


    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setPhone(rs.getString("phone"));
            return user;
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, phone FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User findByPhone(String phone) {
        String sql = "SELECT id, username, password, phone FROM users WHERE phone = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), phone);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public boolean existsByPhone(String phone) {
        String sql = "SELECT COUNT(*) FROM users WHERE phone = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phone);
        return count != null && count > 0;
    }

    public User findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT id, username, password, phone FROM users WHERE username = ? AND password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username, password);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean save(User user) {
        String sql = "INSERT INTO users (username, password, phone) VALUES (?, ?, ?)";
        try {
            int result = jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getPhone());
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<User> findByUsernameOrPhone(String identifier) {
        String sql = "SELECT id, username, password, phone FROM users WHERE username = ? OR phone = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), identifier, identifier);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}