package com.projet6.PayMyBuddy.Controller;

import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // <-- important
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
        return "login";  // Affiche login.html depuis /templates
    }

    // Traite la soumission du formulaire
    @PostMapping("/login")
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              Model model) {

        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            // Connexion réussie
            model.addAttribute("user", user);
            return "redirect:/profile";
        } else {
            // Échec de connexion
            model.addAttribute("error", "Email ou mot de passe incorrect.");
            return "login";
        }
    }
}
