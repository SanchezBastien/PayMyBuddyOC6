package com.projet6.PayMyBuddy.Repository;

import com.projet6.PayMyBuddy.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Permet de récupérer ou enregistrer des utilisateurs
//extend CRUD et repository signifie que la classe a pour role de communiquer avec la source de donnéees
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}