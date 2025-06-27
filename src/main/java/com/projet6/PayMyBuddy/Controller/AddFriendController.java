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
        return connectionService.handleAddFriend(friendEmail, currentUser, model, userService);
    }
}