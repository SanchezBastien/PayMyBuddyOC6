package com.projet6.PayMyBuddy.Services;

import com.projet6.PayMyBuddy.Model.Connection;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Connection saveConnection(Connection connection) {
        return connectionRepository.save(connection);
    }

    public void deleteConnection(Connection connection) {
        connectionRepository.delete(connection);
    }
}
