package com.projet6.PayMyBuddy.Services;

import com.projet6.PayMyBuddy.Model.Transaction;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

//Traite les transferts d'argent, la création et la récupération des transactions

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    public Iterable<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactionBySender(User sender) {
        return transactionRepository.findBySender(sender);
    }

    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransactionById(Integer id) {
        transactionRepository.deleteById(id);
    }

    @Transactional
    public Transaction transfer(User sender, User receiver, BigDecimal amount, String description) {
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Solde insuffisant pour le transfert.");
        }

        // Débit du sender
        sender.setBalance(sender.getBalance().subtract(amount));

        // Crédit du receiver
        receiver.setBalance(receiver.getBalance().add(amount));

        // Sauvegarder les utilisateurs avec les nouveaux soldes
        userService.saveUser(sender);
        userService.saveUser(receiver);

        // Créer et enregistrer la transaction
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }
}