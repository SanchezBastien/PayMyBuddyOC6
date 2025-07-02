package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Controller.ConnectionController;
import com.projet6.PayMyBuddy.Model.Connection;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.ConnectionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConnectionControllerTest {
    @Mock
    private ConnectionService connectionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ConnectionController connectionController;

    public ConnectionControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllConnections() {
        when(connectionService.getConnections()).thenReturn(Collections.emptyList());
        Iterable<Connection> result = connectionController.getAllConnections();
        assertNotNull(result);
    }

    @Test
    void testGetConnectionsByUserEmail() {
        when(connectionService.getConnectionsByUser(any())).thenReturn(Collections.emptyList());
        List<Connection> result = connectionController.getConnectionsByUserEmail("email");
        assertNotNull(result);
    }

    @Test
    void testCreateConnection() {
        // Crée deux utilisateurs fictifs
        User user = new User();
        user.setId(1);
        user.setEmail("a@b.com");

        User friend = new User();
        friend.setId(2);
        friend.setEmail("b@c.com");

        // Crée la connexion complète
        Connection connection = new Connection();
        connection.setUser(user);
        connection.setFriend(friend);

        when(connectionService.createConnectionIfValid(any(Connection.class)))
                .thenReturn(java.util.Optional.of(connection));

        // Appelle le contrôleur
        ResponseEntity<Connection> response = connectionController.createConnection(connection);

        // Doit retourner 200
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteConnection() {
        doNothing().when(connectionService).deleteConnection(any(Connection.class));
        ResponseEntity<Void> response = connectionController.deleteConnection(new Connection());
        assertEquals(200, response.getStatusCodeValue());
    }
}
