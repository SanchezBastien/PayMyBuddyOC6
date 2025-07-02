package com.projet6.PayMyBuddy.Repository;

import com.projet6.PayMyBuddy.Model.Connection;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Model.UserConnectionId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Gère les connexions utilisateurs en base
//extend CRUD et repository signifie que la classe a pour role de communiquer avec la source de donnéees
@Repository
public interface ConnectionRepository extends CrudRepository<Connection, UserConnectionId> {
    List<Connection> findByUser(User user);

    Object findByUserAndFriend(User user1, User user2);
}
