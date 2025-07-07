package com.projet6.PayMyBuddy.Controller;

import com.projet6.PayMyBuddy.Model.Connection;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.ConnectionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Contrôleur gérant l’ajout d’un ami (connexion) pour l’utilisateur courant.
 *
 * <p>
 * Permet d’afficher le formulaire d’ajout d’ami et de traiter la soumission pour
 * ajouter un nouvel ami à la liste de connexions de l’utilisateur connecté.
 * </p>
 */
@Controller
public class AddFriendController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionService connectionService;

    /**
     * Affiche le formulaire d’ajout d’un ami.
     * @return Le nom de la vue pour l’ajout d’ami ("addfriend").
     */
    @GetMapping("/addfriend")
    public String showAddFriendForm() {
        return "addfriend";
    }

    /**
     * Traite la soumission du formulaire d’ajout d’ami.
     * Récupère l’utilisateur courant et tente d’ajouter l’ami correspondant à l’email fourni
     * dans la liste de ses connexions. Utilise {@link ConnectionService#handleAddFriend}
     * pour la logique métier et retourne la vue correspondante.
     * @param friendEmail L’email de l’ami à ajouter.
     * @param principal   Les informations d’authentification de l’utilisateur courant.
     * @param model       Le modèle Spring MVC pour passer les messages/résultats à la vue.
     * @return Le nom de la vue à afficher (succès ou erreur).
     */
    @PostMapping("/addfriend")
    public String processAddFriend(@RequestParam String friendEmail, Principal principal, Model model) {
        User currentUser = userService.getUserByEmail(principal.getName()).orElseThrow();
        return connectionService.handleAddFriend(friendEmail, currentUser, model, userService);
    }
}