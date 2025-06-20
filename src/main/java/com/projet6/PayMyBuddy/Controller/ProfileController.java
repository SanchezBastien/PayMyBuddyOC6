package com.projet6.PayMyBuddy.Controller;

import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

//Gère l'affichage ou la mise à jour du profil utilisateur

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        // Récupération de l'utilisateur connecté via son email (nom d'utilisateur = email)
        User currentUser = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        model.addAttribute("user", currentUser);
        return "profile";  // Correspond à profile.html
    }
}