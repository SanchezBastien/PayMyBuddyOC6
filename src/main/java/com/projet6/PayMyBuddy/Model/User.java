package com.projet6.PayMyBuddy.Model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Entité représentant un utilisateur de l’application PayMyBuddy.
 * Contient les informations principales d’un utilisateur : identifiant, nom d’utilisateur,
 * email, mot de passe, solde du compte, transactions envoyées et reçues, connexions (amis).
 */
@Entity
@Table(name = "user")
public class User {

    /**
     * Identifiant unique de l’utilisateur.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Nom d’utilisateur (affiché dans l’application).
     */
    @Column(nullable = false)
    private String username;

    /**
     * Adresse email unique de l’utilisateur.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Mot de passe (encodé).
     */
    private String password;

    /**
     * Solde du compte utilisateur.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * Transactions envoyées par cet utilisateur.
     */
    @OneToMany(mappedBy = "sender")
    private List<Transaction> sentTransactions;

    /**
     * Transactions reçues par cet utilisateur.
     */
    @OneToMany(mappedBy = "receiver")
    private List<Transaction> receivedTransactions;

    /**
     * Connexions (amis ajoutés par cet utilisateur).
     */
    @OneToMany(mappedBy = "user")
    private List<Connection> connections;

    /**
     * Utilisateurs ayant ajouté cet utilisateur en tant qu’ami.
     */
    @OneToMany(mappedBy = "friend")
    private List<Connection> friends;

    /** @return L’identifiant de l’utilisateur. */
    public int getId() {
        return id;
    }

    /** @param id Définit l’identifiant de l’utilisateur. */
    public void setId(int id) {
        this.id = id;
    }

    /** @return Le nom d’utilisateur. */
    public String getUsername() {
        return username;
    }

    /** @param username Définit le nom d’utilisateur. */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return L’email de l’utilisateur. */
    public String getEmail() {
        return email;
    }

    /** @param email Définit l’email de l’utilisateur. */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return Le mot de passe de l’utilisateur (encodé). */
    public String getPassword() {
        return password;
    }

    /** @param password Définit le mot de passe (encodé) de l’utilisateur. */
    public void setPassword(String password) {
        this.password = password;
    }

    /** @return La liste des transactions envoyées. */
    public List<Transaction> getSentTransactions() {
        return sentTransactions;
    }

    /** @param sentTransactions Définit la liste des transactions envoyées. */
    public void setSentTransactions(List<Transaction> sentTransactions) {
        this.sentTransactions = sentTransactions;
    }

    /** @return La liste des transactions reçues. */
    public List<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }

    /** @param receivedTransactions Définit la liste des transactions reçues. */
    public void setReceivedTransactions(List<Transaction> receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }

    /** @return Les connexions (amis ajoutés). */
    public List<Connection> getConnections() {
        return connections;
    }

    /** @param connections Définit les connexions (amis ajoutés). */
    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    /** @return Les utilisateurs qui ont cet utilisateur en ami. */
    public List<Connection> getFriends() {
        return friends;
    }

    /** @param friends Définit la liste des utilisateurs qui ont cet utilisateur en ami. */
    public void setFriends(List<Connection> friends) {
        this.friends = friends;
    }

    public User() {
    }

    /** @return Le solde du compte utilisateur. */
    public BigDecimal getBalance() {
        return balance;
    }

    /** @param balance Définit le solde du compte utilisateur. */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}