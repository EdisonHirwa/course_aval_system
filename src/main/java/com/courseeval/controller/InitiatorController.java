package com.courseeval.controller;

import com.courseeval.dao.CourseDAO;
import com.courseeval.dao.SurveyDAO;
import com.courseeval.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/initiator")
public class InitiatorController {

    @Autowired private SurveyDAO surveyDAO;
    @Autowired private CourseDAO courseDAO;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        model.addAttribute("surveys", surveyDAO.findByInitiator(user.getId()));
        return "initiator/dashboard";
    }

    // ---- Survey CRUD ----
    @GetMapping("/surveys/new")
    public String newSurveyForm(Model model) {
        model.addAttribute("survey", new Survey());
        model.addAttribute("courses", courseDAO.findAll());
        return "initiator/survey-form";
    }

    @PostMapping("/surveys/save")
    public String saveSurvey(@ModelAttribute Survey survey, HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("loggedUser");
        survey.setInitiatorId(user.getId());
        if (survey.getId() == 0) {
            int newId = surveyDAO.save(survey);
            ra.addFlashAttribute("success", "Survey created. Now add questions.");
            return "redirect:/initiator/surveys/" + newId + "/questions";
        } else {
            surveyDAO.update(survey);
            ra.addFlashAttribute("success", "Survey updated.");
            return "redirect:/initiator/dashboard";
        }
    }

    @GetMapping("/surveys/edit/{id}")
    public String editSurvey(@PathVariable int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        Survey survey = surveyDAO.findById(id);
        if (survey == null || survey.getInitiatorId() != user.getId()) return "redirect:/initiator/dashboard";
        model.addAttribute("survey", survey);
        model.addAttribute("courses", courseDAO.findAll());
        return "initiator/survey-form";
    }

    @PostMapping("/surveys/delete/{id}")
    public String deleteSurvey(@PathVariable int id, HttpSession session, RedirectAttributes ra) {
        User user = (User) session.getAttribute("loggedUser");
        Survey survey = surveyDAO.findById(id);
        if (survey != null && survey.getInitiatorId() == user.getId()) {
            surveyDAO.delete(id);
            ra.addFlashAttribute("success", "Survey deleted.");
        }
        return "redirect:/initiator/dashboard";
    }

    @PostMapping("/surveys/publish/{id}")
    public String publishSurvey(@PathVariable int id, RedirectAttributes ra) {
        surveyDAO.updateStatus(id, "PUBLISHED");
        ra.addFlashAttribute("success", "Survey published.");
        return "redirect:/initiator/dashboard";
    }

    @PostMapping("/surveys/close/{id}")
    public String closeSurvey(@PathVariable int id, RedirectAttributes ra) {
        surveyDAO.updateStatus(id, "CLOSED");
        ra.addFlashAttribute("success", "Survey closed.");
        return "redirect:/initiator/dashboard";
    }

    // ---- Questions ----
    @GetMapping("/surveys/{id}/questions")
    public String questionsPage(@PathVariable int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        Survey survey = surveyDAO.findById(id);
        if (survey == null || survey.getInitiatorId() != user.getId()) return "redirect:/initiator/dashboard";
        List<SurveyQuestion> questions = surveyDAO.findQuestionsBySurvey(id);
        for (SurveyQuestion q : questions) {
            q.setOptions(surveyDAO.findOptionsByQuestion(q.getId()));
        }
        model.addAttribute("survey", survey);
        model.addAttribute("questions", questions);
        return "initiator/questions";
    }

    @PostMapping("/surveys/{surveyId}/questions/add")
    public String addQuestion(@PathVariable int surveyId,
                              @RequestParam String questionText,
                              @RequestParam String questionType,
                              @RequestParam(required = false) List<String> optionTexts,
                              RedirectAttributes ra) {
        SurveyQuestion q = new SurveyQuestion();
        q.setSurveyId(surveyId);
        q.setQuestionText(questionText);
        q.setQuestionType(questionType);
        List<SurveyQuestion> existing = surveyDAO.findQuestionsBySurvey(surveyId);
        q.setDisplayOrder(existing.size() + 1);
        int questionId = surveyDAO.saveQuestion(q);

        if (!"TEXT".equals(questionType) && optionTexts != null) {
            int order = 1;
            for (String text : optionTexts) {
                if (text != null && !text.trim().isEmpty()) {
                    SurveyOption opt = new SurveyOption();
                    opt.setQuestionId(questionId);
                    opt.setOptionText(text.trim());
                    opt.setDisplayOrder(order++);
                    surveyDAO.saveOption(opt);
                }
            }
        }
        ra.addFlashAttribute("success", "Question added.");
        return "redirect:/initiator/surveys/" + surveyId + "/questions";
    }

    @PostMapping("/surveys/{surveyId}/questions/delete/{questionId}")
    public String deleteQuestion(@PathVariable int surveyId, @PathVariable int questionId, RedirectAttributes ra) {
        surveyDAO.deleteOptionsByQuestion(questionId);
        surveyDAO.deleteQuestion(questionId);
        ra.addFlashAttribute("success", "Question deleted.");
        return "redirect:/initiator/surveys/" + surveyId + "/questions";
    }

    // ---- Results ----
    @GetMapping("/surveys/{id}/results")
    public String results(@PathVariable int id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        Survey survey = surveyDAO.findById(id);
        if (survey == null || survey.getInitiatorId() != user.getId()) return "redirect:/initiator/dashboard";
        List<SurveyQuestion> questions = surveyDAO.findQuestionsBySurvey(id);
        for (SurveyQuestion q : questions) {
            if ("TEXT".equals(q.getQuestionType())) {
                // store text answers as fake options for uniform rendering
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
        return "initiator/results";
    }
}
