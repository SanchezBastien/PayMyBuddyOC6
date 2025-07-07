package com.projet6.PayMyBuddy.Repository;

import com.projet6.PayMyBuddy.Model.Connection;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Model.UserConnectionId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository Spring Data pour la gestion des connexions (relations d’amitié) entre utilisateurs.
 * Hérite des méthodes CRUD de base et ajoute des méthodes pour rechercher les connexions
 * d’un utilisateur et vérifier la relation entre deux utilisateurs.
 */
@Repository
public interface ConnectionRepository extends CrudRepository<Connection, UserConnectionId> {
    /**
     * Recherche toutes les connexions d’un utilisateur donné.
     * @param user L’utilisateur dont on veut la liste des connexions.
     * @return Liste des connexions de cet utilisateur.
     */
    List<Connection> findByUser(User user);

    /**
     * Recherche une connexion spécifique entre deux utilisateurs.
     * @param user1 L’utilisateur source.
     * @param user2 L’utilisateur ami.
     * @return La connexion trouvée, ou {@code null} si elle n’existe pas.
     */
    Object findByUserAndFriend(User user1, User user2);
}
