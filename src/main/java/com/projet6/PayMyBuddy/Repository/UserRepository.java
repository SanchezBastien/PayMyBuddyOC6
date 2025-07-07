package com.projet6.PayMyBuddy.Repository;

import com.projet6.PayMyBuddy.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository Spring Data pour la gestion des utilisateurs de l’application.
 * Fournit les opérations CRUD de base et des méthodes pour rechercher
 * un utilisateur par email ou par nom d’utilisateur.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     * Recherche un utilisateur par son adresse email.
     * @param email L’email à rechercher.
     * @return Un {@link Optional} contenant l’utilisateur s’il existe, vide sinon.
     */
    Optional<User> findByEmail(String email);

    /**
     * Recherche un utilisateur par son nom d’utilisateur.
     * @param username Le nom d’utilisateur à rechercher.
     * @return Un {@link Optional} contenant l’utilisateur s’il existe, vide sinon.
     */
    Optional<User> findByUsername(String username);
}