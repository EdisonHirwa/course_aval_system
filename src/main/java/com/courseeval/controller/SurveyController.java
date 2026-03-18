package com.courseeval.controller;

import com.courseeval.dao.SurveyDAO;
import com.courseeval.model.*;
import com.courseeval.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    @Autowired private SurveyDAO surveyDAO;
    @Autowired private EmailService emailService;

    @GetMapping("/list")
    public String listSurveys(Model model) {
        model.addAttribute("surveys", surveyDAO.findPublished());
        return "respondent/survey-list";
    }

    @GetMapping("/respond/{id}")
    public String respondForm(@PathVariable int id, Model model, HttpSession session) {
        Survey survey = surveyDAO.findById(id);
        if (survey == null || !"PUBLISHED".equals(survey.getStatus())) {
            return "redirect:/survey/list";
        }
        User user = (User) session.getAttribute("loggedUser");

        // Check if authenticated-only survey
        if ("AUTHENTICATED".equals(survey.getAccessType()) && user == null) {
            return "redirect:/login";
        }

        // Check if already responded (for logged-in users)
        if (user != null && surveyDAO.hasUserResponded(id, user.getId())) {
            model.addAttribute("alreadyResponded", true);
            model.addAttribute("survey", survey);
            return "respondent/already-responded";
        }

        List<SurveyQuestion> questions = surveyDAO.findQuestionsBySurvey(id);
        for (SurveyQuestion q : questions) {
            q.setOptions(surveyDAO.findOptionsByQuestion(q.getId()));
        }
        model.addAttribute("survey", survey);
        model.addAttribute("questions", questions);
        return "respondent/respond";
    }

    @PostMapping("/respond/{id}/submit")
    public String submitResponse(@PathVariable int id,
                                  @RequestParam Map<String, String> allParams,
                                  @RequestParam(required = false) String guestEmail,
                                  HttpSession session,
                                  RedirectAttributes ra) {
        Survey survey = surveyDAO.findById(id);
        if (survey == null || !"PUBLISHED".equals(survey.getStatus())) {
            return "redirect:/survey/list";
        }

        User user = (User) session.getAttribute("loggedUser");

        // Check if authenticated-only survey
        if ("AUTHENTICATED".equals(survey.getAccessType()) && user == null) {
            return "redirect:/login";
        }

        // Build respondent
        Respondent respondent = new Respondent();
        respondent.setSurveyId(id);
        String emailForConfirmation = null;

        if (user != null) {
            respondent.setUserId(user.getId());
            emailForConfirmation = user.getEmail();
        } else {
            // Check map if request param was missed
            if (guestEmail == null || guestEmail.trim().isEmpty()) {
                guestEmail = allParams.get("guestEmail");
            }
            
            // Guest
            if (guestEmail == null || guestEmail.trim().isEmpty()) {
                ra.addFlashAttribute("error", "Email is required for guest submissions.");
                return "redirect:/survey/respond/" + id;
            }
            respondent.setGuestEmail(guestEmail.trim());
            emailForConfirmation = guestEmail.trim();
        }

        int respondentId = surveyDAO.saveRespondent(respondent);

        // Save each answer
        List<SurveyQuestion> questions = surveyDAO.findQuestionsBySurvey(id);
        for (SurveyQuestion q : questions) {
            String paramKey = "question_" + q.getId();
            if ("TEXT".equals(q.getQuestionType())) {
                String textVal = allParams.get(paramKey);
                if (textVal != null && !textVal.trim().isEmpty()) {
                    SurveyResponse resp = new SurveyResponse();
                    resp.setRespondentId(respondentId);
                    resp.setQuestionId(q.getId());
                    resp.setTextAnswer(textVal.trim());
                    surveyDAO.saveResponse(resp);
                }
            } else if ("MULTIPLE".equals(q.getQuestionType())) {
                // Multiple checkboxes: question_X_optY
                for (Map.Entry<String, String> entry : allParams.entrySet()) {
                    if (entry.getKey().startsWith("question_" + q.getId() + "_")) {
                        try {
                            int optId = Integer.parseInt(entry.getValue());
                            SurveyResponse resp = new SurveyResponse();
                            resp.setRespondentId(respondentId);
                            resp.setQuestionId(q.getId());
                            resp.setOptionId(optId);
                            surveyDAO.saveResponse(resp);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            } else {
                // SINGLE
                String optVal = allParams.get(paramKey);
                if (optVal != null && !optVal.trim().isEmpty()) {
                    try {
                        SurveyResponse resp = new SurveyResponse();
                        resp.setRespondentId(respondentId);
                        resp.setQuestionId(q.getId());
                        resp.setOptionId(Integer.parseInt(optVal));
                        surveyDAO.saveResponse(resp);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        // Send confirmation email
        if (emailForConfirmation != null) {
            String name = user != null ? user.getFullName() : "Guest";
            emailService.sendSurveyConfirmation(emailForConfirmation, survey.getTitle(), name);
        }

        ra.addFlashAttribute("success", "Thank you! Your response has been submitted.");
        return "redirect:/survey/list";
    }
}
