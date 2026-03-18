package com.courseeval.dao;

import com.courseeval.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SurveyDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Survey> surveyMapper = (rs, rowNum) -> {
        Survey s = new Survey();
        s.setId(rs.getInt("id"));
        s.setTitle(rs.getString("title"));
        s.setDescription(rs.getString("description"));
        s.setCourseId(rs.getInt("course_id"));
        s.setInitiatorId(rs.getInt("initiator_id"));
        s.setAccessType(rs.getString("access_type"));
        s.setStatus(rs.getString("status"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        s.setUpdatedAt(rs.getTimestamp("updated_at"));
        try { s.setCourseTitle(rs.getString("course_title")); } catch (Exception e) {}
        try { s.setCourseCode(rs.getString("course_code")); } catch (Exception e) {}
        try { s.setInitiatorName(rs.getString("initiator_name")); } catch (Exception e) {}
        try { s.setResponseCount(rs.getInt("response_count")); } catch (Exception e) {}
        return s;
    };

    private RowMapper<SurveyQuestion> questionMapper = (rs, rowNum) -> {
        SurveyQuestion q = new SurveyQuestion();
        q.setId(rs.getInt("id"));
        q.setSurveyId(rs.getInt("survey_id"));
        q.setQuestionText(rs.getString("question_text"));
        q.setQuestionType(rs.getString("question_type"));
        q.setDisplayOrder(rs.getInt("display_order"));
        return q;
    };

    private RowMapper<SurveyOption> optionMapper = (rs, rowNum) -> {
        SurveyOption o = new SurveyOption();
        o.setId(rs.getInt("id"));
        o.setQuestionId(rs.getInt("question_id"));
        o.setOptionText(rs.getString("option_text"));
        o.setDisplayOrder(rs.getInt("display_order"));
        try { o.setVoteCount(rs.getInt("vote_count")); } catch (Exception e) {}
        return o;
    };

    // --- Survey CRUD ---

    public List<Survey> findAll() {
        String sql = "SELECT s.*, c.title as course_title, c.code as course_code, u.full_name as initiator_name, " +
                     "(SELECT COUNT(*) FROM respondents r WHERE r.survey_id=s.id) as response_count " +
                     "FROM surveys s JOIN courses c ON s.course_id=c.id JOIN users u ON s.initiator_id=u.id " +
                     "ORDER BY s.created_at DESC";
        return jdbcTemplate.query(sql, surveyMapper);
    }

    public List<Survey> findByInitiator(int initiatorId) {
        String sql = "SELECT s.*, c.title as course_title, c.code as course_code, u.full_name as initiator_name, " +
                     "(SELECT COUNT(*) FROM respondents r WHERE r.survey_id=s.id) as response_count " +
                     "FROM surveys s JOIN courses c ON s.course_id=c.id JOIN users u ON s.initiator_id=u.id " +
                     "WHERE s.initiator_id=? ORDER BY s.created_at DESC";
        return jdbcTemplate.query(sql, surveyMapper, initiatorId);
    }

    public List<Survey> findByTeacher(int teacherId) {
        String sql = "SELECT s.*, c.title as course_title, c.code as course_code, u.full_name as initiator_name, " +
                     "(SELECT COUNT(*) FROM respondents r WHERE r.survey_id=s.id) as response_count " +
                     "FROM surveys s JOIN courses c ON s.course_id=c.id JOIN users u ON s.initiator_id=u.id " +
                     "JOIN teacher_courses tc ON s.course_id=tc.course_id " +
                     "WHERE tc.teacher_id=? ORDER BY s.created_at DESC";
        return jdbcTemplate.query(sql, surveyMapper, teacherId);
    }

    public List<Survey> findPublished() {
        String sql = "SELECT s.*, c.title as course_title, c.code as course_code, u.full_name as initiator_name " +
                     "FROM surveys s JOIN courses c ON s.course_id=c.id JOIN users u ON s.initiator_id=u.id " +
                     "WHERE s.status='PUBLISHED' ORDER BY s.created_at DESC";
        return jdbcTemplate.query(sql, surveyMapper);
    }

    public Survey findById(int id) {
        String sql = "SELECT s.*, c.title as course_title, c.code as course_code, u.full_name as initiator_name, " +
                     "(SELECT COUNT(*) FROM respondents r WHERE r.survey_id=s.id) as response_count " +
                     "FROM surveys s JOIN courses c ON s.course_id=c.id JOIN users u ON s.initiator_id=u.id " +
                     "WHERE s.id=?";
        List<Survey> list = jdbcTemplate.query(sql, surveyMapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public int save(Survey survey) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO surveys (title, description, course_id, initiator_id, access_type, status) VALUES (?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, survey.getTitle());
            ps.setString(2, survey.getDescription());
            ps.setInt(3, survey.getCourseId());
            ps.setInt(4, survey.getInitiatorId());
            ps.setString(5, survey.getAccessType());
            ps.setString(6, survey.getStatus());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int update(Survey survey) {
        return jdbcTemplate.update(
            "UPDATE surveys SET title=?, description=?, course_id=?, access_type=?, status=? WHERE id=?",
            survey.getTitle(), survey.getDescription(), survey.getCourseId(),
            survey.getAccessType(), survey.getStatus(), survey.getId());
    }

    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM surveys WHERE id=?", id);
    }

    public int updateStatus(int id, String status) {
        return jdbcTemplate.update("UPDATE surveys SET status=? WHERE id=?", status, id);
    }

    // --- Questions ---

    public List<SurveyQuestion> findQuestionsBySurvey(int surveyId) {
        String sql = "SELECT * FROM survey_questions WHERE survey_id=? ORDER BY display_order";
        return jdbcTemplate.query(sql, questionMapper, surveyId);
    }

    public int saveQuestion(SurveyQuestion q) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO survey_questions (survey_id, question_text, question_type, display_order) VALUES (?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, q.getSurveyId());
            ps.setString(2, q.getQuestionText());
            ps.setString(3, q.getQuestionType());
            ps.setInt(4, q.getDisplayOrder());
            return ps;
        }, kh);
        return kh.getKey().intValue();
    }

    public int updateQuestion(SurveyQuestion q) {
        return jdbcTemplate.update(
            "UPDATE survey_questions SET question_text=?, question_type=?, display_order=? WHERE id=?",
            q.getQuestionText(), q.getQuestionType(), q.getDisplayOrder(), q.getId());
    }

    public int deleteQuestion(int questionId) {
        return jdbcTemplate.update("DELETE FROM survey_questions WHERE id=?", questionId);
    }

    public void deleteQuestionsBySurvey(int surveyId) {
        jdbcTemplate.update("DELETE FROM survey_questions WHERE survey_id=?", surveyId);
    }

    // --- Options ---

    public List<SurveyOption> findOptionsByQuestion(int questionId) {
        return jdbcTemplate.query(
            "SELECT * FROM survey_options WHERE question_id=? ORDER BY display_order",
            optionMapper, questionId);
    }

    public int saveOption(SurveyOption o) {
        return jdbcTemplate.update(
            "INSERT INTO survey_options (question_id, option_text, display_order) VALUES (?,?,?)",
            o.getQuestionId(), o.getOptionText(), o.getDisplayOrder());
    }

    public int deleteOption(int optionId) {
        return jdbcTemplate.update("DELETE FROM survey_options WHERE id=?", optionId);
    }

    public void deleteOptionsByQuestion(int questionId) {
        jdbcTemplate.update("DELETE FROM survey_options WHERE question_id=?", questionId);
    }

    // --- Responses ---

    public int saveRespondent(Respondent respondent) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO respondents (survey_id, user_id, guest_email) VALUES (?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, respondent.getSurveyId());
            if (respondent.getUserId() != null) ps.setInt(2, respondent.getUserId());
            else ps.setNull(2, java.sql.Types.INTEGER);
            ps.setString(3, respondent.getGuestEmail());
            return ps;
        }, kh);
        return kh.getKey().intValue();
    }

    public void saveResponse(SurveyResponse response) {
        jdbcTemplate.update(
            "INSERT INTO survey_responses (respondent_id, question_id, option_id, text_answer) VALUES (?,?,?,?)",
            response.getRespondentId(), response.getQuestionId(),
            response.getOptionId(), response.getTextAnswer());
    }

    public boolean hasUserResponded(int surveyId, int userId) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM respondents WHERE survey_id=? AND user_id=?", Integer.class, surveyId, userId);
        return count != null && count > 0;
    }

    // --- Results ---

    public List<SurveyOption> findOptionsWithVoteCounts(int questionId) {
        String sql = "SELECT o.*, COUNT(sr.id) as vote_count FROM survey_options o " +
                     "LEFT JOIN survey_responses sr ON o.id=sr.option_id " +
                     "WHERE o.question_id=? GROUP BY o.id ORDER BY o.display_order";
        return jdbcTemplate.query(sql, optionMapper, questionId);
    }

    public List<String> findTextAnswers(int questionId) {
        return jdbcTemplate.queryForList(
            "SELECT text_answer FROM survey_responses WHERE question_id=? AND text_answer IS NOT NULL AND text_answer != ''",
            String.class, questionId);
    }

    public int countRespondents(int surveyId) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM respondents WHERE survey_id=?", Integer.class, surveyId);
        return count != null ? count : 0;
    }
}
