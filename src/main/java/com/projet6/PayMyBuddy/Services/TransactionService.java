package com.projet6.PayMyBuddy.Services;

import com.projet6.PayMyBuddy.Model.Transaction;
import com.projet6.PayMyBuddy.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Iterable<Transaction> getTransaction() {
        return transactionRepository.findAll();
    }
}
