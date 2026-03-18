package com.courseeval.controller;

import com.courseeval.dao.CourseDAO;
import com.courseeval.dao.SurveyDAO;
import com.courseeval.dao.UserDAO;
import com.courseeval.model.Course;
import com.courseeval.model.User;
import com.courseeval.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserDAO userDAO;
    @Autowired private CourseDAO courseDAO;
    @Autowired private SurveyDAO surveyDAO;
    @Autowired private EmailService emailService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        model.addAttribute("pendingTeachers", userDAO.findPendingTeachers());
        model.addAttribute("totalCourses", courseDAO.findAll().size());
        model.addAttribute("totalSurveys", surveyDAO.findAll().size());
        model.addAttribute("totalUsers", userDAO.findAll().size());
        return "admin/dashboard";
    }

    // ---- Teachers ----
    @GetMapping("/teachers")
    public String teachers(Model model) {
        model.addAttribute("teachers", userDAO.findByRole("TEACHER"));
        return "admin/teachers";
    }

    @PostMapping("/teachers/approve/{id}")
    public String approveTeacher(@PathVariable int id, RedirectAttributes ra) {
        User u = userDAO.findById(id);
        userDAO.updateStatus(id, "ACTIVE");
        if (u != null) emailService.sendTeacherApprovalNotification(u.getEmail(), u.getFullName(), true);
        ra.addFlashAttribute("success", "Teacher approved.");
        return "redirect:/admin/teachers";
    }

    @PostMapping("/teachers/reject/{id}")
    public String rejectTeacher(@PathVariable int id, RedirectAttributes ra) {
        User u = userDAO.findById(id);
        userDAO.updateStatus(id, "REJECTED");
        if (u != null) emailService.sendTeacherApprovalNotification(u.getEmail(), u.getFullName(), false);
        ra.addFlashAttribute("success", "Teacher rejected.");
        return "redirect:/admin/teachers";
    }

    @PostMapping("/teachers/delete/{id}")
    public String deleteTeacher(@PathVariable int id, RedirectAttributes ra) {
        userDAO.delete(id);
        ra.addFlashAttribute("success", "Teacher deleted.");
        return "redirect:/admin/teachers";
    }

    // ---- Courses ----
    @GetMapping("/courses")
    public String courses(Model model) {
        model.addAttribute("courses", courseDAO.findAll());
        return "admin/courses";
    }

    @GetMapping("/courses/new")
    public String newCourseForm(Model model) {
        if (!model.containsAttribute("course")) {
            model.addAttribute("course", new Course());
        }
        return "admin/course-form";
    }

    @PostMapping("/courses/save")
    public String saveCourse(@ModelAttribute Course course, RedirectAttributes ra) {
        // Check for duplicate course code
        Course existing = courseDAO.findByCode(course.getCode());
        if (existing != null && existing.getId() != course.getId()) {
            ra.addFlashAttribute("error", "Course code '" + course.getCode() + "' already exists.");
            ra.addFlashAttribute("course", course); // Preserve input
            if (course.getId() == 0) {
                return "redirect:/admin/courses/new";
            } else {
                return "redirect:/admin/courses/edit/" + course.getId();
            }
        }

        if (course.getId() == 0) courseDAO.save(course);
        else courseDAO.update(course);
        ra.addFlashAttribute("success", "Course saved successfully.");
        return "redirect:/admin/courses";
    }

    @GetMapping("/courses/edit/{id}")
    public String editCourse(@PathVariable int id, Model model) {
        if (!model.containsAttribute("course")) {
            model.addAttribute("course", courseDAO.findById(id));
        }
        return "admin/course-form";
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable int id, RedirectAttributes ra) {
        courseDAO.delete(id);
        ra.addFlashAttribute("success", "Course deleted.");
        return "redirect:/admin/courses";
    }

    // ---- Teacher Assignment ----
    @GetMapping("/courses/{id}/assign")
    public String assignForm(@PathVariable int id, Model model) {
        model.addAttribute("course", courseDAO.findById(id));
        model.addAttribute("allTeachers", userDAO.findByRole("TEACHER"));
        model.addAttribute("assignedIds", courseDAO.getAssignedTeacherIds(id));
        return "admin/assign-teachers";
    }

    @PostMapping("/courses/{id}/assign")
    public String doAssign(@PathVariable int id,
                           @RequestParam(required = false) List<Integer> teacherIds,
                           RedirectAttributes ra) {
        // Remove all current assignments first
        List<Integer> current = courseDAO.getAssignedTeacherIds(id);
        for (int tid : current) courseDAO.unassignTeacher(tid, id);
        // Add selected
        if (teacherIds != null) {
            for (int tid : teacherIds) courseDAO.assignTeacher(tid, id);
        }
        ra.addFlashAttribute("success", "Teachers assigned successfully.");
        return "redirect:/admin/courses";
    }

    // ---- Users management ----
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userDAO.findAll());
        return "admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable int id, RedirectAttributes ra) {
        userDAO.delete(id);
        ra.addFlashAttribute("success", "User deleted.");
        return "redirect:/admin/users";
    }

    // ---- Surveys overview ----
    @GetMapping("/surveys")
    public String surveys(Model model) {
        model.addAttribute("surveys", surveyDAO.findAll());
        return "admin/surveys";
    }
}
