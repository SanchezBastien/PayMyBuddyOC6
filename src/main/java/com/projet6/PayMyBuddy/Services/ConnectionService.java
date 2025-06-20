package com.projet6.PayMyBuddy.Services;

import com.projet6.PayMyBuddy.Model.Connection;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Vérifie et crée les liens de connexion entre utilisateurs (amis)

@Service
public class ConnectionService {
    @Autowired
    private ConnectionRepository connectionRepository;

    public Iterable<Connection> getConnections() {
        return connectionRepository.findAll();
    }

    public List<Connection> getConnectionsByUser(User user) {
        return connectionRepository.findByUser(user);
    }

    @Transactional
    public Connection saveConnection(Connection connection) {
        return connectionRepository.save(connection);
    }

    @Transactional
    public void deleteConnection(Connection connection) {
        connectionRepository.delete(connection);
    }
}
