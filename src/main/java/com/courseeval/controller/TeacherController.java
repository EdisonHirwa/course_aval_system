package com.courseeval.controller;

import com.courseeval.dao.CourseDAO;
import com.courseeval.dao.SurveyDAO;
import com.courseeval.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired private SurveyDAO surveyDAO;
    @Autowired private CourseDAO courseDAO;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        model.addAttribute("surveys", surveyDAO.findByTeacher(user.getId()));
        model.addAttribute("courses", courseDAO.findByTeacherId(user.getId()));
        return "teacher/dashboard";
    }

    @GetMapping("/surveys/{id}/results")
    public String results(@PathVariable int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        Survey survey = surveyDAO.findById(id);
        if (survey == null) return "redirect:/teacher/dashboard";

        // Verify the teacher is assigned to this survey's course
        List<Integer> assignedCourseTeachers = courseDAO.getAssignedTeacherIds(survey.getCourseId());
        if (!assignedCourseTeachers.contains(user.getId())) return "redirect:/teacher/dashboard";

        List<SurveyQuestion> questions = surveyDAO.findQuestionsBySurvey(id);
        for (SurveyQuestion q : questions) {
            if ("TEXT".equals(q.getQuestionType())) {
                List<String> texts = surveyDAO.findTextAnswers(q.getId());
                List<SurveyOption> opts = new java.util.ArrayList<>();
                for (String t : texts) {
                    SurveyOption o = new SurveyOption();
                    o.setOptionText(t);
                    opts.add(o);
                }
                q.setOptions(opts);
            } else {
                q.setOptions(surveyDAO.findOptionsWithVoteCounts(q.getId()));
            }
        }
        model.addAttribute("survey", survey);
        model.addAttribute("questions", questions);
        model.addAttribute("totalRespondents", surveyDAO.countRespondents(id));
        return "teacher/results";
    }
}
