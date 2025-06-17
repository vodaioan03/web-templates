package org.backend.jsp.controllers;

import jakarta.servlet.http.HttpSession;
import org.backend.jsp.entities.User;
import org.backend.jsp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
public class PageController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        try {
            Optional<User> user = userRepository.findByUsername(username);

            if (user.isEmpty() || user.get().getPassword() != Integer.parseInt(password)) {
                model.addAttribute("error", "Invalid username or password");
                return "login";
            }

            session.setAttribute("user", user.get()); // Store user in session
            return "redirect:/dashboard"; // Redirect to a protected page
        } catch (Exception e) {
            model.addAttribute("error", "Login failed");
            return "login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }
        return "dashboard";
    }
}