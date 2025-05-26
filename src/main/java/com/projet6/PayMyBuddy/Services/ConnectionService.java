package com.projet6.PayMyBuddy.Services;

import com.projet6.PayMyBuddy.Model.Connection;
import com.projet6.PayMyBuddy.Repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {
    @Autowired
    private ConnectionRepository connectionRepository;

    public Iterable<Connection> getConnection() {
        return connectionRepository.findAll();
    }
}