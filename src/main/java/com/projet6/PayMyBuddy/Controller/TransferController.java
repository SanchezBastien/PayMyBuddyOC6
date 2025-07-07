package com.projet6.PayMyBuddy.Controller;

import com.projet6.PayMyBuddy.Model.Transaction;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.ConnectionService;
import com.projet6.PayMyBuddy.Services.TransactionService;
import com.projet6.PayMyBuddy.Services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

/**
 * Contrôleur gérant les transferts d’argent entre utilisateurs connectés.
 * Permet d’afficher la page de transfert, de lister les connexions de l’utilisateur courant,
 * et de traiter l’envoi d’argent vers l’un de ses contacts.
 */
@Controller
public class TransferController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    /**
     * Affiche la page de transfert pour l’utilisateur connecté.
     * Affiche la liste de ses connexions (amis) ainsi que ses transactions envoyées.
     * @param model Le modèle Spring MVC pour transmettre les connexions et transactions à la vue.
     * @param principal Les informations d’authentification (email) de l’utilisateur connecté.
     * @return Le nom de la vue "transfer".
     */
    @GetMapping("/transfer")
    public String showTransferPage(Model model, Principal principal) {
        User currentUser = userService.getUserByEmail(principal.getName()).orElseThrow();

        model.addAttribute("connections", connectionService.getConnectionsByUser(currentUser));
        model.addAttribute("transactions", transactionService.getTransactionBySender(currentUser));

        return "transfer";
    }

    /**
     * Traite la soumission d’un transfert d’argent vers un autre utilisateur connecté.
     * Déduit le montant du solde de l’expéditeur, crédite le destinataire, et enregistre la transaction.
     * @param receiverId  L’identifiant de l’utilisateur destinataire.
     * @param description Le motif ou commentaire du transfert.
     * @param amount Le montant à transférer.
     * @param principal Les informations d’authentification de l’utilisateur expéditeur.
     * @return Une redirection vers la page de transfert après opération.
     */
    @PostMapping("/transfer")
    public String processTransfer(@RequestParam Integer receiverId,
                                  @RequestParam String description,
                                  @RequestParam BigDecimal amount,
                                  Principal principal) {
        User sender = userService.getUserByEmail(principal.getName()).orElseThrow();
        User receiver = userService.getUserById(receiverId).orElseThrow();

        transactionService.transfer(sender, receiver, amount, description);
        return "redirect:/transfer";
    }
}
