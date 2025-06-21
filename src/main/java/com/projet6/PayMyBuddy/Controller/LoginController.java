package com.projet6.PayMyBuddy.Controller;

import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    // Affiche le formulaire de login
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Traite le formulaire de connexion
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model) {
        Optional<User> optionalUser = userService.getUserByEmail(username);

        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
            return "redirect:/profile";
        } else {
            model.addAttribute("error", "Identifiants incorrects");
            return "login";
        }
    }
}
