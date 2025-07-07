package com.projet6.PayMyBuddy.Controller;

import com.projet6.PayMyBuddy.Model.Connection;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.ConnectionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des connexions (amis) entre utilisateurs.
 * Permet d’afficher toutes les connexions, d’obtenir les connexions d’un utilisateur,
 * d’ajouter ou de supprimer une connexion entre deux utilisateurs.
 */
@RestController
@RequestMapping("/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    /**
     * Récupère l’ensemble des connexions existantes.
     * @return Un iterable de toutes les connexions {@link Connection} du système.
     */
    @GetMapping
    public Iterable<Connection> getAllConnections() {
        return connectionService.getConnections();
    }

    /**
     * Récupère la liste des connexions pour un utilisateur donné par son email.
     * @param email L’email de l’utilisateur.
     * @return Une liste de connexions {@link Connection} pour cet utilisateur, ou une liste vide s’il n’existe pas.
     */
    @GetMapping("/by-user")
    public List<Connection> getConnectionsByUserEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(connectionService::getConnectionsByUser)
                .orElse(Collections.emptyList());
    }

    /**
     * Crée une nouvelle connexion entre deux utilisateurs si elle est valide.
     * @param connection L’objet {@link Connection} à créer (sous forme JSON).
     * @return La connexion créée dans la réponse, ou une erreur (bad request) si invalide.
     */
    @PostMapping
    public ResponseEntity<Connection> createConnection(@RequestBody Connection connection) {
        return connectionService.createConnectionIfValid(connection)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body(null));
    }

    /**
     * Supprime une connexion existante entre deux utilisateurs.
     * @param connection L’objet {@link Connection} à supprimer (sous forme JSON).
     * @return Une réponse HTTP 200 OK si la suppression a réussi.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteConnection(@RequestBody Connection connection) {
        connectionService.deleteConnection(connection);
        return ResponseEntity.ok().build();
    }
}