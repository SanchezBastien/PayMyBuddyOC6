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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConnectionServiceTest {

    @Mock
    private ConnectionRepository connectionRepository;

    @Spy
    @InjectMocks
    private ConnectionService connectionService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    private User currentUser;
    private User friend;
    private Connection connection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("current@buddy.com");

        friend = new User();
        friend.setId(2);
        friend.setEmail("friend@buddy.com");

        connection = new Connection();
        connection.setUser(currentUser);
        connection.setFriend(friend);
    }

    @Test
    void testGetConnections() {
        List<Connection> conns = Arrays.asList(connection);
        when(connectionRepository.findAll()).thenReturn(conns);

        Iterable<Connection> result = connectionService.getConnections();
        assertNotNull(result);
        assertEquals(conns, result);
        verify(connectionRepository, times(1)).findAll();
    }

    @Test
    void testGetConnectionsByUser() {
        List<Connection> conns = Arrays.asList(connection);
        when(connectionRepository.findByUser(currentUser)).thenReturn(conns);

        List<Connection> result = connectionService.getConnectionsByUser(currentUser);
        assertEquals(conns, result);
        verify(connectionRepository, times(1)).findByUser(currentUser);
    }

    @Test
    void testSaveConnection() {
        when(connectionRepository.save(connection)).thenReturn(connection);
        Connection saved = connectionService.saveConnection(connection);
        assertNotNull(saved);
        verify(connectionRepository).save(connection);
    }

    @Test
    void testDeleteConnection() {
        connectionService.deleteConnection(connection);
        verify(connectionRepository, times(1)).delete(connection);
    }

    // ----- handleAddFriend -----

    @Test
    void testHandleAddFriend_FriendNotFound() {
        when(userService.getUserByEmail("no@friend.com")).thenReturn(Optional.empty());
        String view = connectionService.handleAddFriend("no@friend.com", currentUser, model, userService);
        assertEquals("addfriend", view);
        verify(model).addAttribute("message", "Utilisateur introuvable.");
        verify(model).addAttribute("error", true);
    }

    @Test
    void testHandleAddFriend_AddSelf() {
        when(userService.getUserByEmail("current@buddy.com")).thenReturn(Optional.of(currentUser));
        String view = connectionService.handleAddFriend("current@buddy.com", currentUser, model, userService);
        assertEquals("addfriend", view);
        verify(model).addAttribute("message", "Impossible de s’ajouter soi-même.");
        verify(model).addAttribute("error", true);
    }

    @Test
    void testHandleAddFriend_AlreadyConnected() {
        when(userService.getUserByEmail("friend@buddy.com")).thenReturn(Optional.of(friend));
        // Simule qu'on est déjà connecté à cet ami
        Connection already = new Connection();
        already.setUser(currentUser);
        already.setFriend(friend);
        when(connectionRepository.findByUser(currentUser)).thenReturn(List.of(already));
        // Redéfinir getConnectionsByUser pour retourner ce mock
        doReturn(List.of(already)).when(connectionService).getConnectionsByUser(currentUser);

        String view = connectionService.handleAddFriend("friend@buddy.com", currentUser, model, userService);
        assertEquals("addfriend", view);
        verify(model).addAttribute("message", "Vous êtes déjà connecté à cet utilisateur.");
        verify(model).addAttribute("error", true);
    }
}
