package com.projet6.PayMyBuddy.Model;

import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "user")
public class User {

    @Id //clé primaire
    private int id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    // Relations
    @OneToMany(mappedBy = "sender")
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    private List<Transaction> receivedTransactions;

    @OneToMany(mappedBy = "user")
    private List<Connection> connections;

    @OneToMany(mappedBy = "friend")
    private List<Connection> friends;


    // Getters & Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
