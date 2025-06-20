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
import java.util.Optional;

//Permet d'ajouter un ami à la liste de connexions d'un utilisateur

@Controller
public class AddFriendController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConnectionService connectionService;

    @GetMapping("/addfriend")
    public String showAddFriendForm() {
        return "addfriend";
    }

    @PostMapping("/addfriend")
    public String processAddFriend(@RequestParam String friendEmail, Principal principal, Model model) {
        User currentUser = userService.getUserByEmail(principal.getName()).orElseThrow();

        Optional<User> friendOpt = userService.getUserByEmail(friendEmail);
        if (friendOpt.isEmpty()) {
            model.addAttribute("message", "Utilisateur introuvable.");
            model.addAttribute("error", true);
            return "addfriend";
        }

        User friend = friendOpt.get();

        if (currentUser.getId() == friend.getId()) {
            model.addAttribute("message", "Impossible de s’ajouter soi-même.");
            model.addAttribute("error", true);
            return "addfriend";
        }

        boolean alreadyConnected = connectionService.getConnectionsByUser(currentUser)
                .stream()
                .anyMatch(conn -> conn.getFriend().getId() == friend.getId());

        if (alreadyConnected) {
            model.addAttribute("message", "Vous êtes déjà connecté à cet utilisateur.");
            model.addAttribute("error", true);
        } else {
            Connection connection = new Connection();
            connection.setUser(currentUser);
            connection.setFriend(friend);
            connectionService.saveConnection(connection);

            model.addAttribute("message", "Relation ajoutée avec succès !");
            model.addAttribute("error", false);
        }

        return "addfriend";
    }
}