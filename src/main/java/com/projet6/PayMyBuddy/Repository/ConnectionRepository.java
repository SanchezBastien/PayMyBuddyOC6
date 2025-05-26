package com.projet6.PayMyBuddy.Repository;

import com.projet6.PayMyBuddy.Model.Connection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//extend CRUD et repository signifie que la classe a pour role de communiquer avec la source de donnéees
@Repository
public interface ConnectionRepository extends CrudRepository<Connection, String> {
}
