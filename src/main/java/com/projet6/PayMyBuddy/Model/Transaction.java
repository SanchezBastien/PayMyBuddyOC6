package com.projet6.PayMyBuddy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entité représentant une transaction (transfert d’argent) entre deux utilisateurs.
 * Contient l’expéditeur, le destinataire, le montant transféré et une description éventuelle du transfert.
 */
@Entity
@Table(name = "transaction")
public class Transaction {

    /**
     * Identifiant unique de la transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Utilisateur expéditeur de la transaction.
     */
    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonIgnore
    private User sender;

    /**
     * Utilisateur destinataire de la transaction.
     */
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @JsonIgnore
    private User receiver;

    /**
     * Description ou motif de la transaction.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Montant transféré lors de la transaction.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    /** @return L’identifiant de la transaction. */
    public int getId() {
        return id;
    }

    /** @param id Définit l’identifiant de la transaction. */
    public void setId(int id) {
        this.id = id;
    }

    /** @return L’utilisateur expéditeur de la transaction. */
    public User getSender() {
        return sender;
    }

    /** @param sender Définit l’expéditeur de la transaction. */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /** @return L’utilisateur destinataire de la transaction. */
    public User getReceiver() {
        return receiver;
    }

    /** @param receiver Définit le destinataire de la transaction. */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /** @return La description de la transaction. */
    public String getDescription() {
        return description;
    }

    /** @param description Définit la description de la transaction. */
    public void setDescription(String description) {
        this.description = description;
    }

    /** @return Le montant de la transaction. */
    public BigDecimal getAmount() {
        return amount;
    }

    /** @param amount Définit le montant de la transaction. */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}