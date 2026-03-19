package com.courseeval.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendSurveyConfirmation(String toEmail, String surveyTitle, String respondentName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Survey Submission Confirmed: " + surveyTitle);
            message.setText("Dear " + (respondentName != null ? respondentName : "Respondent") + ",\n\n" +
                    "Thank you for completing the survey: \"" + surveyTitle + "\".\n\n" +
                    "Your feedback has been recorded and will help improve the course.\n\n" +
                    "Best regards,\nCourse Evaluation System");
            mailSender.send(message);
            logger.info("Confirmation email sent to {}", toEmail);
        } catch (Exception e) {
            logger.warn("Could not send email to {}: {}", toEmail, e.getMessage());
        }
    }

    public void sendTeacherApprovalNotification(String toEmail, String fullName, boolean approved) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            if (approved) {
                message.setSubject("Account Approved - Course Evaluation System");
                message.setText("Dear " + fullName + ",\n\n" +
                        "Your teacher account has been approved. You can now log in to the system.\n\n" +
                        "Best regards,\nAdministrator");
            } else {
                message.setSubject("Account Registration Update");
                message.setText("Dear " + fullName + ",\n\n" +
                        "We regret to inform you that your teacher registration has not been approved at this time.\n\n" +
                        "Please contact the administrator for more information.\n\n" +
                        "Best regards,\nAdministrator");
            }
            mailSender.send(message);
        } catch (Exception e) {
            logger.warn("Could not send approval email to {}: {}", toEmail, e.getMessage());
        }
    }
}
