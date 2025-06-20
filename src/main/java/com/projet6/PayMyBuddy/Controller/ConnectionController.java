package com.projet6.PayMyBuddy.Controller;

import com.projet6.PayMyBuddy.Model.Connection;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.ConnectionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

//Gère les connexions entre utilisateurs (par exemple : afficher les amis connectés)

@RestController
@RequestMapping("/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    @GetMapping
    public Iterable<Connection> getAllConnections() {
        return connectionService.getConnections();
    }

    @GetMapping("/by-user")
    public List<Connection> getConnectionsByUserEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(connectionService::getConnectionsByUser)
                .orElse(Collections.emptyList());
    }

    @PostMapping
    public ResponseEntity<Connection> createConnection(@RequestBody Connection connection) {
        if (connection.getUser() == null || connection.getFriend() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Integer userId = connection.getUser().getId();
        Integer friendId = connection.getFriend().getId();

        Optional<User> userOpt = userService.getUserById(userId);
        Optional<User> friendOpt = userService.getUserById(friendId);

        if (userOpt.isEmpty() || friendOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        connection.setUser(userOpt.get());
        connection.setFriend(friendOpt.get());

        return ResponseEntity.ok(connectionService.saveConnection(connection));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteConnection(@RequestBody Connection connection) {
        connectionService.deleteConnection(connection);
        return ResponseEntity.ok().build();
    }
}