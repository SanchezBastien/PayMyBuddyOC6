package com.projet6.PayMyBuddy.Model;

import java.io.Serializable;
import java.util.Objects;


/*table de liaison*/

public class UserConnectionId implements Serializable {

    private int user;
    private int friend;

    public UserConnectionId() {}

    public UserConnectionId(int user, int friend) {
        this.user = user;
        this.friend = friend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserConnectionId)) return false;
        UserConnectionId that = (UserConnectionId) o;
        return user == that.user && friend == that.friend;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, friend);
    }
}
