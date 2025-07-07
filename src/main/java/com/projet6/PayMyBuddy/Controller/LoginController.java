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

import java.util.Optional;

/**
 * Contrôleur gérant l’authentification des utilisateurs.
 * Permet d’afficher le formulaire de connexion et de traiter la soumission pour authentifier
 * un utilisateur à partir de son email et de son mot de passe.
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Affiche le formulaire de connexion.
     * @return Le nom de la vue pour la page de connexion ("login").
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    /**
     * Traite la soumission du formulaire de connexion.
     * Vérifie l’existence de l’utilisateur et la validité du mot de passe.
     * En cas de succès, redirige vers la page de profil. Sinon, réaffiche le formulaire avec un message d’erreur.
     * @param username L’email de l’utilisateur.
     * @param password Le mot de passe saisi.
     * @param model    Le modèle Spring MVC pour transmettre les erreurs éventuelles à la vue.
     * @return Une redirection vers le profil si connexion réussie, sinon la vue de login avec erreur.
     */
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model) {
        Optional<User> optionalUser = userService.getUserByEmail(username);
        String encodePassword = passwordEncoder.encode(password);
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(encodePassword)) {
            return "redirect:/profile";
        } else {
            model.addAttribute("error", "Identifiants incorrects");
            return "login";
        }
    }
}
