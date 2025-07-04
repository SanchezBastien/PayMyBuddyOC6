package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Model.Connection;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Repository.ConnectionRepository;
import com.projet6.PayMyBuddy.Services.ConnectionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ConnectionServiceTest {

    @Mock
    private ConnectionRepository connectionRepository;

    @InjectMocks
    private ConnectionService connectionService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getConnections_shouldReturnAllConnections() {
        List<Connection> connections = Arrays.asList(new Connection(), new Connection());
        when(connectionRepository.findAll()).thenReturn(connections);

        Iterable<Connection> result = connectionService.getConnections();

        assertThat(result).containsExactlyElementsOf(connections);
        verify(connectionRepository, times(1)).findAll();
    }

    @Test
    void getConnectionsByUser_shouldReturnConnectionsForUser() {
        User user = new User();
        List<Connection> connections = Arrays.asList(new Connection(), new Connection());
        when(connectionRepository.findByUser(user)).thenReturn(connections);

        List<Connection> result = connectionService.getConnectionsByUser(user);

        assertThat(result).containsExactlyElementsOf(connections);
        verify(connectionRepository, times(1)).findByUser(user);
    }

    @Test
    void saveConnection_shouldSaveAndReturnConnection() {
        Connection connection = new Connection();
        when(connectionRepository.save(connection)).thenReturn(connection);

        Connection result = connectionService.saveConnection(connection);

        assertThat(result).isEqualTo(connection);
        verify(connectionRepository, times(1)).save(connection);
    }

    @Test
    void deleteConnection_shouldDeleteConnection() {
        Connection connection = new Connection();

        connectionService.deleteConnection(connection);

        verify(connectionRepository, times(1)).delete(connection);
    }

    @Test
    void handleAddFriend_userNotFound_shouldReturnAddFriendWithError() {
        String friendEmail = "test@test.com";
        User currentUser = new User();
        when(userService.getUserByEmail(friendEmail)).thenReturn(Optional.empty());

        String result = connectionService.handleAddFriend(friendEmail, currentUser, model, userService);

        verify(model).addAttribute("message", "Utilisateur introuvable.");
        verify(model).addAttribute("error", true);
        assertThat(result).isEqualTo("addfriend");
    }

    @Test
    void handleAddFriend_addSelf_shouldReturnAddFriendWithError() {
        String friendEmail = "me@test.com";
        User currentUser = new User();
        currentUser.setId(1);
        User friend = new User();
        friend.setId(1);
        when(userService.getUserByEmail(friendEmail)).thenReturn(Optional.of(friend));

        String result = connectionService.handleAddFriend(friendEmail, currentUser, model, userService);

        verify(model).addAttribute("message", "Impossible de s’ajouter soi-même.");
        verify(model).addAttribute("error", true);
        assertThat(result).isEqualTo("addfriend");
    }

    @Test
    void handleAddFriend_alreadyConnected_shouldReturnAddFriendWithError() {
        String friendEmail = "ami@test.com";
        User currentUser = new User();
        currentUser.setId(1);
        User friend = new User();
        friend.setId(2);
        when(userService.getUserByEmail(friendEmail)).thenReturn(Optional.of(friend));
        // On simule une connexion déjà existante
        Connection existingConnection = new Connection();
        existingConnection.setUser(currentUser);
        existingConnection.setFriend(friend);
        List<Connection> connections = Collections.singletonList(existingConnection);
        when(connectionService.getConnectionsByUser(currentUser)).thenReturn(connections);

        String result = connectionService.handleAddFriend(friendEmail, currentUser, model, userService);

        verify(model).addAttribute("message", "Vous êtes déjà connecté à cet utilisateur.");
        verify(model).addAttribute("error", true);
        assertThat(result).isEqualTo("addfriend");
    }

    @Test
    void handleAddFriend_newFriend_shouldSaveAndReturnSuccess() {
        String friendEmail = "newfriend@test.com";
        User currentUser = new User();
        currentUser.setId(1);
        User friend = new User();
        friend.setId(2);
        when(userService.getUserByEmail(friendEmail)).thenReturn(Optional.of(friend));
        // On simule aucune connexion existante
        when(connectionService.getConnectionsByUser(currentUser)).thenReturn(Collections.emptyList());

        String result = connectionService.handleAddFriend(friendEmail, currentUser, model, userService);

        verify(connectionRepository).save(any(Connection.class));
        verify(model).addAttribute("message", "Ami ajouté avec succès !");
        verify(model).addAttribute("success", true);
        assertThat(result).isEqualTo("addfriend");
    }
}
