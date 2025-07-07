package com.projet6.PayMyBuddy.Controller;

import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * Contrôleur gérant l’inscription des nouveaux utilisateurs.
 * Permet d’afficher le formulaire d’inscription et de traiter la création d’un
 * nouvel utilisateur avec un solde initialisé à zéro.
 */
@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Affiche le formulaire d’inscription.
     * @return Le nom de la vue "signup".
     */
    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";
    }

    /**
     * Traite la soumission du formulaire d’inscription.
     * Crée un nouvel utilisateur avec les informations saisies, encode le mot de passe
     * et initialise le solde à zéro. Sauvegarde l’utilisateur puis redirige vers la page de connexion.
     * @param username Le nom d’utilisateur choisi.
     * @param email    L’adresse email.
     * @param password Le mot de passe non encodé.
     * @return Une redirection vers la page de login après inscription.
     */
    @PostMapping("/signup")
    public String processSignup(@RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setBalance(BigDecimal.ZERO);

        userService.saveUser(user);
        return "redirect:/login";
    }
}