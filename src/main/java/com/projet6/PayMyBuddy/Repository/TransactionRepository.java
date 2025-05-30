package com.projet6.PayMyBuddy.Repository;

import com.projet6.PayMyBuddy.Model.Transaction;
import com.projet6.PayMyBuddy.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//extend CRUD et repository signifie que la classe a pour role de communiquer avec la source de donnéees
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    List<Transaction> findBySender(User sender);
    List<Transaction> findBySenderOrderByIdDesc(User sender);
}
