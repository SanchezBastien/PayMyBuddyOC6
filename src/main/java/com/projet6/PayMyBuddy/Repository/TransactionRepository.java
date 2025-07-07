package com.projet6.PayMyBuddy.Repository;

import com.projet6.PayMyBuddy.Model.Transaction;
import com.projet6.PayMyBuddy.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository Spring Data pour la gestion des transactions (transferts d’argent) entre utilisateurs.
 * Hérite des opérations CRUD standards et fournit des méthodes pour récupérer les transactions
 * envoyées par un utilisateur, éventuellement triées par identifiant décroissant.
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    /**
     * Recherche toutes les transactions envoyées par un utilisateur donné.
     * @param sender L’utilisateur expéditeur.
     * @return Liste des transactions envoyées par cet utilisateur.
     */
    List<Transaction> findBySender(User sender);

    /**
     * Recherche toutes les transactions envoyées par un utilisateur, triées du plus récent au plus ancien.
     * @param sender L’utilisateur expéditeur.
     * @return Liste des transactions envoyées, triées par identifiant décroissant.
     */
    List<Transaction> findBySenderOrderByIdDesc(User sender);
}
