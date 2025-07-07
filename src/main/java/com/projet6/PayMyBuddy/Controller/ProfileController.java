package com.projet6.PayMyBuddy.Controller;

import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * Contrôleur gérant l’affichage du profil de l’utilisateur connecté.
 * Permet d’afficher les informations du profil de l’utilisateur courant,
 * récupérées à partir de son email d’authentification.
 */
@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    /**
     * Affiche la page de profil de l’utilisateur actuellement connecté.
     * Récupère l’utilisateur à partir de l’identifiant d’authentification (email)
     * et ajoute ses informations au modèle pour l’affichage
     * @param model     Le modèle Spring MVC pour transmettre les informations utilisateur à la vue.
     * @param principal Les informations d’authentification (email) de l’utilisateur connecté.
     * @return Le nom de la vue "profile" (profil utilisateur).
     * @throws IllegalArgumentException si l’utilisateur connecté n’est pas trouvé en base.
     */
    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        // Récupération de l'utilisateur connecté via son email (nom d'utilisateur = email)
        User currentUser = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        model.addAttribute("user", currentUser);
        return "profile";  // Correspond à profile.html
    }
}