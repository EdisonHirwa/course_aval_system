package com.courseeval.controller;

import com.courseeval.dao.UserDAO;
import com.courseeval.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/")
    public String index(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user != null) return redirectByRole(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user != null) return redirectByRole(user);
        return "common/login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          RedirectAttributes ra) {
        User user = userDAO.findByUsername(username);
        if (user == null || !BCrypt.checkpw(password, user.getPassword())) {
            ra.addFlashAttribute("error", "Invalid username or password.");
            return "redirect:/login";
        }
        if ("PENDING".equals(user.getStatus())) {
            ra.addFlashAttribute("error", "Your account is pending approval.");
            return "redirect:/login";
        }
        if ("REJECTED".equals(user.getStatus())) {
            ra.addFlashAttribute("error", "Your account has been rejected.");
            return "redirect:/login";
        }
        session.setAttribute("loggedUser", user);
        return redirectByRole(user);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "common/register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String email,
                             @RequestParam String fullName,
                              @RequestParam String role,
                             RedirectAttributes ra) {
        if (!"TEACHER".equals(role) && !"RESPONDENT".equals(role) && !"INITIATOR".equals(role) && !"ADMIN".equals(role)) {
            ra.addFlashAttribute("error", "Invalid role selected.");
            return "redirect:/register";
        }
        if (userDAO.existsByUsername(username)) {
            ra.addFlashAttribute("error", "Username already taken.");
            return "redirect:/register";
        }
        if (userDAO.existsByEmail(email)) {
            ra.addFlashAttribute("error", "Email already registered.");
            return "redirect:/register";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRoleId(userDAO.getRoleIdByName(role));
        
        if ("RESPONDENT".equals(role) || "INITIATOR".equals(role) || "ADMIN".equals(role)) {
            user.setStatus("ACTIVE");
            ra.addFlashAttribute("success", "Registration successful. Please login.");
        } else {
            user.setStatus("PENDING");
            ra.addFlashAttribute("success", "Registration submitted. Please wait for admin approval.");
        }
        
        userDAO.save(user);
        return "redirect:/login";
    }

    private String redirectByRole(User user) {
        switch (user.getRoleName()) {
            case "ADMIN": return "redirect:/admin/dashboard";
            case "INITIATOR": return "redirect:/initiator/dashboard";
            case "TEACHER": return "redirect:/teacher/dashboard";
            default: return "redirect:/survey/list";
        }
    }
}
