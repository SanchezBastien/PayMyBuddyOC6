package com.projet6.PayMyBuddy.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

//Lien d’amitié/connexion entre deux utilisateurs

@Entity
@Table(name = "connection")
@IdClass(UserConnectionId.class)
public class Connection {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }
}