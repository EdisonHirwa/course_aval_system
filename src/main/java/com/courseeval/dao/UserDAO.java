package com.courseeval.dao;

import com.courseeval.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userMapper = (rs, rowNum) -> {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setEmail(rs.getString("email"));
        u.setFullName(rs.getString("full_name"));
        u.setRoleId(rs.getInt("role_id"));
        u.setStatus(rs.getString("status"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        try { u.setRoleName(rs.getString("role_name")); } catch (SQLException e) {}
        return u;
    };

    public User findByUsername(String username) {
        String sql = "SELECT u.*, r.name as role_name FROM users u JOIN roles r ON u.role_id=r.id WHERE u.username=?";
        List<User> list = jdbcTemplate.query(sql, userMapper, username);
        return list.isEmpty() ? null : list.get(0);
    }

    public User findById(int id) {
        String sql = "SELECT u.*, r.name as role_name FROM users u JOIN roles r ON u.role_id=r.id WHERE u.id=?";
        List<User> list = jdbcTemplate.query(sql, userMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<User> findAll() {
        String sql = "SELECT u.*, r.name as role_name FROM users u JOIN roles r ON u.role_id=r.id ORDER BY u.created_at DESC";
        return jdbcTemplate.query(sql, userMapper);
    }

    public List<User> findByRole(String roleName) {
        String sql = "SELECT u.*, r.name as role_name FROM users u JOIN roles r ON u.role_id=r.id WHERE r.name=? ORDER BY u.created_at DESC";
        return jdbcTemplate.query(sql, userMapper, roleName);
    }

    public List<User> findPendingTeachers() {
        String sql = "SELECT u.*, r.name as role_name FROM users u JOIN roles r ON u.role_id=r.id WHERE r.name='TEACHER' AND u.status='PENDING'";
        return jdbcTemplate.query(sql, userMapper);
    }

    public int save(User user) {
        String sql = "INSERT INTO users (username, password, email, full_name, role_id, status) VALUES (?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword(),
                user.getEmail(), user.getFullName(), user.getRoleId(), user.getStatus());
    }

    public int updateStatus(int userId, String status) {
        return jdbcTemplate.update("UPDATE users SET status=? WHERE id=?", status, userId);
    }

    public int update(User user) {
        String sql = "UPDATE users SET email=?, full_name=? WHERE id=?";
        return jdbcTemplate.update(sql, user.getEmail(), user.getFullName(), user.getId());
    }

    public int delete(int userId) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", userId);
    }

    public boolean existsByUsername(String username) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE username=?", Integer.class, username);
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE email=?", Integer.class, email);
        return count != null && count > 0;
    }

    public int getRoleIdByName(String roleName) {
        Integer id = jdbcTemplate.queryForObject("SELECT id FROM roles WHERE name=?", Integer.class, roleName);
        return id != null ? id : 0;
    }
}
