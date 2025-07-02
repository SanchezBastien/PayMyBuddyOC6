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

//Vérifie et crée les liens de connexion entre utilisateurs (amis)

@Service
public class ConnectionService {
    @Autowired
    private ConnectionRepository connectionRepository;

    public Iterable<Connection> getConnections() {
        return connectionRepository.findAll();
    }

    public List<Connection> getConnectionsByUser(User user) {
        return connectionRepository.findByUser(user);
    }

    @Transactional
    public Connection saveConnection(Connection connection) {
        return connectionRepository.save(connection);
    }

    @Transactional
    public void deleteConnection(Connection connection) {
        connectionRepository.delete(connection);
    }

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
