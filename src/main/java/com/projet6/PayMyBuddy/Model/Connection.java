package com.projet6.PayMyBuddy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entité représentant une connexion (relation d’amitié) entre deux utilisateurs.
 * Une connexion relie un utilisateur à l’un de ses amis dans le système PayMyBuddy.
 * Utilise une clé composite (user, friend) pour garantir l’unicité de la relation.
 */
@Entity
@Table(name = "connection")
@IdClass(UserConnectionId.class)
public class Connection {

    /**
     * Utilisateur source de la connexion (celui qui ajoute un ami).
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Utilisateur ami (cible de la connexion).
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    /**
     * Retourne l’utilisateur source de la connexion.
     * @return L’utilisateur ayant cette connexion.
     */
    public User getUser() {
        return user;
    }

    /**
     * Définit l’utilisateur source de la connexion.
     * @param user L’utilisateur à définir.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Retourne l’ami connecté à l’utilisateur.
     * @return L’utilisateur ami.
     */
    public User getFriend() {
        return friend;
    }

    /**
     * Définit l’ami de cette connexion.
     * @param friend L’ami à ajouter à la connexion.
     */
    public void setFriend(User friend) {
        this.friend = friend;
    }
}