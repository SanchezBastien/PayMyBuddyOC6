package com.projet6.PayMyBuddy.Controller;

import com.projet6.PayMyBuddy.Model.Transaction;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.TransactionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

//Fournit les opérations liées aux transactions (envoi, consultation)

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping
    public Iterable<Transaction> getAllTransactions() {
        return transactionService.getTransactions();
    }

    @GetMapping("/by-sender")
    public List<Transaction> getTransactionsBySenderEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(transactionService::getTransactionBySender)
                .orElse(Collections.emptyList());
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction saved = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(saved);
    }

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