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

/**
 * Tests unitaires pour le contrôleur {@link ConnectionController}.
 * Vérifie le comportement des endpoints de gestion des connexions (amis) entre utilisateurs,
 * en isolant les dépendances grâce à des mocks.
 */
class ConnectionControllerTest {
    @Mock
    private ConnectionService connectionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ConnectionController connectionController;

    /**
     * Initialise les mocks pour chaque test.
     */
    public ConnectionControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Vérifie que la récupération de toutes les connexions ne retourne pas {@code null}.
     */
    @Test
    void testGetAllConnections() {
        when(connectionService.getConnections()).thenReturn(Collections.emptyList());
        Iterable<Connection> result = connectionController.getAllConnections();
        assertNotNull(result);
    }

    /**
     * Vérifie que la récupération des connexions par email utilisateur retourne bien une liste (même vide).
     */
    @Test
    void testGetConnectionsByUserEmail() {
        when(connectionService.getConnectionsByUser(any())).thenReturn(Collections.emptyList());
        List<Connection> result = connectionController.getConnectionsByUserEmail("email");
        assertNotNull(result);
    }

    /**
     * Vérifie que la création d’une connexion retourne une réponse appropriée.
     */
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

    /**
     * Vérifie que la suppression d’une connexion retourne une réponse appropriée.
     */
    @Test
    void testDeleteConnection() {
        doNothing().when(connectionService).deleteConnection(any(Connection.class));
        ResponseEntity<Void> response = connectionController.deleteConnection(new Connection());
        assertEquals(200, response.getStatusCodeValue());
    }
}
