package com.projet6.PayMyBuddy.Model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe (table de liaison) représentant la clé composite pour l’entité {@link Connection}.
 * Utilisée pour identifier de façon unique une connexion entre deux utilisateurs
 * dans la table de liaison. Implémente {@link Serializable} et redéfinit {@code equals} et {@code hashCode}
 * pour garantir l’unicité et le bon fonctionnement des opérations JPA.
 */

public class UserConnectionId implements Serializable {

    /** Identifiant de l’utilisateur source de la connexion. */
    private int user;
    /** Identifiant de l’utilisateur ami. */
    private int friend;

    /** Constructeur par défaut (requis par JPA). */
    public UserConnectionId() {}

    /**
     * Constructeur avec tous les champs.
     * @param user   Identifiant de l’utilisateur source.
     * @param friend Identifiant de l’ami.
     */
    public UserConnectionId(int user, int friend) {
        this.user = user;
        this.friend = friend;
    }

    /**
     * Compare deux objets UserConnectionId pour vérifier leur égalité.
     * @param o L’objet à comparer.
     * @return {@code true} si les deux objets sont égaux, {@code false} sinon.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserConnectionId)) return false;
        UserConnectionId that = (UserConnectionId) o;
        return user == that.user && friend == that.friend;
    }

    /**
     * Calcule le hashcode à partir des deux identifiants.
     * @return Le hashcode de la clé composite.
     */
    @Override
    public int hashCode() {
        return Objects.hash(user, friend);
    }
}
