package com.projet6.PayMyBuddy.Controller;

import com.projet6.PayMyBuddy.Model.Transaction;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.TransactionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des transactions entre utilisateurs.
 * Permet de consulter toutes les transactions, de filtrer par expéditeur,
 * de créer une nouvelle transaction et de mettre à jour ou supprimer une transaction existante.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    /**
     * Récupère toutes les transactions enregistrées dans le système.
     * @return Un iterable de toutes les transactions {@link Transaction}.
     */
    @GetMapping
    public Iterable<Transaction> getAllTransactions() {
        return transactionService.getTransactions();
    }

    /**
     * Récupère les transactions effectuées par un utilisateur donné.
     * @param email L’email de l’utilisateur expéditeur.
     * @return Une liste des transactions pour cet expéditeur, ou une liste vide s’il n’existe pas.
     */
    @GetMapping("/by-sender")
    public List<Transaction> getTransactionsBySenderEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(transactionService::getTransactionBySender)
                .orElse(Collections.emptyList());
    }

    /**
     * Crée et enregistre une nouvelle transaction.
     * @param transaction L’objet {@link Transaction} à enregistrer (reçu en JSON).
     * @return La transaction créée.
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction saved = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(saved);
    }

    /**
     * Met à jour une transaction existante par son identifiant.
     * @param id L’ID de la transaction à mettre à jour.
     * @param transactionDetails Les nouvelles données de la transaction.
     * @return La transaction mise à jour, ou 404 si non trouvée.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Integer id, @RequestBody Transaction transactionDetails) {
        Optional<Transaction> optionalTransaction = transactionService.getTransactionById(id);
        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get();
            transaction.setSender(transactionDetails.getSender());
            transaction.setReceiver(transactionDetails.getReceiver());
            transaction.setAmount(transactionDetails.getAmount());
            transaction.setDescription(transactionDetails.getDescription());
            return ResponseEntity.ok(transactionService.saveTransaction(transaction));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Supprime une transaction par son identifiant.
     * Recherche la transaction correspondante à l’ID fourni. Si elle existe, la transaction
     * est supprimée du système et une réponse HTTP 200 OK est retournée. Si la transaction
     * n’existe pas, une réponse HTTP 404 Not Found est renvoyée.
     * @param id L’identifiant de la transaction à supprimer.
     * @return Une réponse HTTP 200 OK si suppression réussie, ou 404 Not Found si la transaction n’existe pas.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Integer id) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        if (transaction.isPresent()) {
            transactionService.deleteTransactionById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}