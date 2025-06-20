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

//Gère les transferts d’argent entre utilisateurs connectés

@Controller
public class TransferController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    @GetMapping("/transfer")
    public String showTransferPage(Model model, Principal principal) {
        User currentUser = userService.getUserByEmail(principal.getName()).orElseThrow();

        model.addAttribute("connections", connectionService.getConnectionsByUser(currentUser));
        model.addAttribute("transactions", transactionService.getTransactionBySender(currentUser));

        return "transfer";
    }

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
