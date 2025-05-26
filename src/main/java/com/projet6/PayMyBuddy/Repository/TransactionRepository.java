package com.projet6.PayMyBuddy.Repository;

import com.projet6.PayMyBuddy.Model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//extend CRUD et repository signifie que la classe a pour role de communiquer avec la source de donnéees
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
}
