package com.projet6.PayMyBuddy.Controller;

import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 * Permet d’obtenir la liste des utilisateurs, de rechercher par identifiant ou email,
 * de créer, mettre à jour ou supprimer un utilisateur.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Récupère tous les utilisateurs du système
     * @return Un iterable de tous les utilisateurs {@link User}.
     */
    @GetMapping
    public Iterable<User> getAllUsers() {
        return userService.getUsers();
    }

    /**
     * Recherche un utilisateur par son identifiant.
     * @param id L’identifiant de l’utilisateur.
     * @return L’utilisateur trouvé, ou 404 si inexistant.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Recherche un utilisateur par son email.
     * @param email L’email de l’utilisateur.
     * @return L’utilisateur trouvé, ou 404 si inexistant.
     */
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crée un nouvel utilisateur.
     * @param user L’objet {@link User} à créer (reçu en JSON).
     * @return L’utilisateur nouvellement créé.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Met à jour un utilisateur existant par son identifiant.
     * @param id L’identifiant de l’utilisateur à modifier.
     * @param userDetails Les nouvelles informations utilisateur.
     * @return L’utilisateur modifié, ou 404 si inexistant.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        Optional<User> optionalUser = userService.getUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            return ResponseEntity.ok(userService.saveUser(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Supprime un utilisateur par son identifiant.
     * @param id L’identifiant de l’utilisateur à supprimer.
     * @return Réponse 200 OK si suppression, ou 404 si inexistant.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            userService.deleteUserById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}