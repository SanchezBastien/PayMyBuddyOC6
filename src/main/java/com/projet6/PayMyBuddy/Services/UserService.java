package com.projet6.PayMyBuddy.Services;

import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service métier pour la gestion des utilisateurs de l’application.
 * Fournit les opérations courantes sur les utilisateurs : recherche par identifiant, email ou username,
 * création, modification, suppression et récupération de la liste des utilisateurs.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Récupère tous les utilisateurs enregistrés dans le système.
     * @return Un iterable de tous les utilisateurs {@link User}.
     */
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Recherche un utilisateur par son identifiant.
     * @param id L’identifiant de l’utilisateur.
     * @return Un Optional contenant l’utilisateur s’il existe, vide sinon.
     */
    public Optional<User> getUserById(Integer id){
        return userRepository.findById(id);
    }

    /**
     * Recherche un utilisateur par son email.
     * @param email L’email de l’utilisateur.
     * @return Un Optional contenant l’utilisateur s’il existe, vide sinon.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Recherche un utilisateur par son nom d’utilisateur (username).
     * @param username Le nom d’utilisateur.
     * @return Un Optional contenant l’utilisateur s’il existe, vide sinon.
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Enregistre ou met à jour un utilisateur.
     * @param user L’utilisateur à enregistrer.
     * @return L’utilisateur persisté.
     */
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Supprime un utilisateur par son identifiant.
     * @param id L’identifiant de l’utilisateur à supprimer.
     */
    @Transactional
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    /**
     * Recherche un utilisateur par son email
     * @param mail L’email de l’utilisateur.
     * @return Un Optional contenant l’utilisateur s’il existe, vide sinon.
     */
    public Optional<User> findUserByEmail(String mail) {
        return userRepository.findByEmail(mail);
    }
}