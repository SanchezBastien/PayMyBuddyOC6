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

/**
 * Service métier pour la gestion des transactions (transferts d’argent) entre utilisateurs.
 * Permet de consulter, créer, supprimer des transactions et de gérer la logique de transfert
 * d’argent entre utilisateurs avec contrôle de solde.
 */
@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    /**
     * Récupère toutes les transactions enregistrées dans le système.
     * @return Un iterable de toutes les transactions {@link Transaction}.
     */
    public Iterable<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Recherche une transaction par son identifiant.
     * @param id L’identifiant de la transaction.
     * @return La transaction correspondante, ou un Optional vide si non trouvée.
     */
    public Optional<Transaction> getTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }

    /**
     * Récupère toutes les transactions envoyées par un utilisateur donné.
     * @param sender L’utilisateur expéditeur.
     * @return La liste des transactions envoyées par cet utilisateur.
     */
    public List<Transaction> getTransactionBySender(User sender) {
        return transactionRepository.findBySender(sender);
    }

    /**
     * Enregistre une transaction en base de données.
     * @param transaction La transaction à enregistrer.
     * @return La transaction persistée.
     */
    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Supprime une transaction par son identifiant.
     * @param id L’identifiant de la transaction à supprimer.
     */
    @Transactional
    public void deleteTransactionById(Integer id) {
        transactionRepository.deleteById(id);
    }

    /**
     * Effectue un transfert d’argent entre deux utilisateurs.
     * Vérifie que le solde de l’expéditeur est suffisant, débite son compte,
     * crédite le destinataire, sauvegarde les nouveaux soldes et enregistre la transaction.
     * Lève une exception en cas de solde insuffisant.
     * @param sender      L’utilisateur expéditeur.
     * @param receiver    L’utilisateur destinataire.
     * @param amount      Le montant à transférer.
     * @param description Le motif du transfert.
     * @return La transaction créée et enregistrée.
     * @throws IllegalArgumentException si le solde du sender est insuffisant.
     */
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