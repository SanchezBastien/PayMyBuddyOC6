package com.projet6.PayMyBuddy.Services;

import com.projet6.PayMyBuddy.Model.Connection;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des connexions (relations d’amitié) entre utilisateurs.
 * Permet d’obtenir, de créer ou de supprimer des connexions, ainsi que de gérer
 * la logique d’ajout d’ami avec contrôle métier (non duplication, non auto-ajout, etc.).
 */
@Service
public class ConnectionService {
    @Autowired
    private ConnectionRepository connectionRepository;

    /**
     * Récupère toutes les connexions présentes dans le système.
     * @return Un iterable de toutes les connexions {@link Connection}.
     */
    public Iterable<Connection> getConnections() {
        return connectionRepository.findAll();
    }

    /**
     * Récupère toutes les connexions d’un utilisateur donné.
     * @param user L’utilisateur dont on veut la liste d’amis.
     * @return La liste des connexions de cet utilisateur.
     */
    public List<Connection> getConnectionsByUser(User user) {
        return connectionRepository.findByUser(user);
    }

    /**
     * Enregistre une nouvelle connexion en base de données.
     * @param connection La connexion à enregistrer.
     * @return La connexion persistée.
     */
    @Transactional
    public Connection saveConnection(Connection connection) {
        return connectionRepository.save(connection);
    }

    /**
     * Supprime une connexion existante en base de données.
     * @param connection La connexion à supprimer.
     */
    @Transactional
    public void deleteConnection(Connection connection) {
        connectionRepository.delete(connection);
    }

    /**
     * Gère la logique d’ajout d’un nouvel ami à la liste de connexions d’un utilisateur.
     * Vérifie l’existence de l’ami, empêche l’auto-ajout, interdit les doublons,
     * et ajoute un message au modèle selon le résultat.
     * @param friendEmail  Email de l’ami à ajouter.
     * @param currentUser  L’utilisateur actuellement connecté.
     * @param model        Le modèle Spring MVC pour transmettre les messages à la vue.
     * @param userService  Service permettant de rechercher un utilisateur par email.
     * @return Le nom de la vue à afficher après traitement.
     */
    public String handleAddFriend(String friendEmail, User currentUser, Model model, UserService userService) {
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

        boolean alreadyConnected = getConnectionsByUser(currentUser)
                .stream()
                .anyMatch(conn -> conn.getFriend().getId() == friend.getId());

        if (alreadyConnected) {
            model.addAttribute("message", "Vous êtes déjà connecté à cet utilisateur.");
            model.addAttribute("error", true);
        } else {
            Connection connection = new Connection();
            connection.setUser(currentUser);
            connection.setFriend(friend);
            saveConnection(connection);
            model.addAttribute("message", "Ami ajouté avec succès !");
            model.addAttribute("success", true);
        }

        return "addfriend";
    }

    @Transactional
    public Optional<Connection> createConnectionIfValid(Connection connection) {
        if (connection.getUser() == null || connection.getFriend() == null) {
            return Optional.empty();
        }

        return Optional.of(connectionRepository.save(connection));
    }

    public Connection addConnection(User user1, User user2) {
        Connection connection = new Connection();
        connection.setUser(user1);
        connection.setFriend(user2);
        return connectionRepository.save(connection);
    }
}
