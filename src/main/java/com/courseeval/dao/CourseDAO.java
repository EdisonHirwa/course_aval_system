package com.courseeval.dao;

import com.courseeval.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Course> courseMapper = (rs, rowNum) -> {
        Course c = new Course();
        c.setId(rs.getInt("id"));
        c.setCode(rs.getString("code"));
        c.setTitle(rs.getString("title"));
        c.setDescription(rs.getString("description"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    };

    public List<Course> findAll() {
        return jdbcTemplate.query("SELECT * FROM courses ORDER BY code", courseMapper);
    }

    public Course findById(int id) {
        List<Course> list = jdbcTemplate.query("SELECT * FROM courses WHERE id=?", courseMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Course> findByTeacherId(int teacherId) {
        String sql = "SELECT c.* FROM courses c JOIN teacher_courses tc ON c.id=tc.course_id WHERE tc.teacher_id=?";
        return jdbcTemplate.query(sql, courseMapper, teacherId);
    }

    public int save(Course course) {
        return jdbcTemplate.update("INSERT INTO courses (code, title, description) VALUES (?,?,?)",
                course.getCode(), course.getTitle(), course.getDescription());
    }

    public int update(Course course) {
        return jdbcTemplate.update("UPDATE courses SET code=?, title=?, description=? WHERE id=?",
                course.getCode(), course.getTitle(), course.getDescription(), course.getId());
    }

    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM courses WHERE id=?", id);
    }

    public void assignTeacher(int teacherId, int courseId) {
        jdbcTemplate.update("INSERT IGNORE INTO teacher_courses (teacher_id, course_id) VALUES (?,?)", teacherId, courseId);
    }

    public void unassignTeacher(int teacherId, int courseId) {
        jdbcTemplate.update("DELETE FROM teacher_courses WHERE teacher_id=? AND course_id=?", teacherId, courseId);
    }

    public List<Integer> getAssignedTeacherIds(int courseId) {
        return jdbcTemplate.queryForList("SELECT teacher_id FROM teacher_courses WHERE course_id=?", Integer.class, courseId);
    }

    public Course findByCode(String code) {
        List<Course> list = jdbcTemplate.query("SELECT * FROM courses WHERE code=?", courseMapper, code);
        return list.isEmpty() ? null : list.get(0);
    }
}
