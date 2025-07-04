package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Controller.UserController;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("Alice");
        user1.setEmail("alice@mail.com");
        user1.setPassword("pwd");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("Bob");
        user2.setEmail("bob@mail.com");
        user2.setPassword("pwd2");

        when(userService.getUsers()).thenReturn(Arrays.asList(user1, user2));

        Iterable<User> users = userController.getAllUsers();
        assertNotNull(users);
        assertEquals("Alice", ((User)users.iterator().next()).getUsername());
    }

    @Test
    void getUserById_found() {
        User user = new User();
        user.setId(1);
        user.setUsername("Alice");
        user.setEmail("alice@mail.com");
        user.setPassword("pwd");

        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(1);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Alice", response.getBody().getUsername());
    }

    @Test
    void getUserById_notFound() {
        when(userService.getUserById(99)).thenReturn(Optional.empty());
        ResponseEntity<User> response = userController.getUserById(99);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void getUserByEmail_found() {
        User user = new User();
        user.setId(1);
        user.setUsername("Alice");
        user.setEmail("alice@mail.com");
        user.setPassword("pwd");

        when(userService.getUserByEmail("alice@mail.com")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByEmail("alice@mail.com");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void getUserByEmail_notFound() {
        when(userService.getUserByEmail("no@mail.com")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserByEmail("no@mail.com");
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void createUser_shouldReturnCreatedUser() {
        User user = new User();
        user.setUsername("Alice");
        user.setEmail("alice@mail.com");
        user.setPassword("pwd");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("Alice");
        savedUser.setEmail("alice@mail.com");
        savedUser.setPassword("pwd");

        when(userService.saveUser(any(User.class))).thenReturn(savedUser);

        ResponseEntity<User> response = userController.createUser(user);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void updateUser_found() {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("Old");
        existingUser.setEmail("old@mail.com");
        existingUser.setPassword("pwd");

        User details = new User();
        details.setUsername("Alice");
        details.setEmail("alice@mail.com");
        details.setPassword("newpwd");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setUsername("Alice");
        updatedUser.setEmail("alice@mail.com");
        updatedUser.setPassword("newpwd");

        when(userService.getUserById(1)).thenReturn(Optional.of(existingUser));
        when(userService.saveUser(any(User.class))).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateUser(1, details);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Alice", response.getBody().getUsername());
    }

    @Test
    void updateUser_notFound() {
        User details = new User();
        details.setUsername("Alice");
        details.setEmail("alice@mail.com");
        details.setPassword("newpwd");

        when(userService.getUserById(1)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.updateUser(1, details);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void deleteUser_found() {
        User user = new User();
        user.setId(1);
        user.setUsername("Alice");
        user.setEmail("alice@mail.com");
        user.setPassword("pwd");

        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        doNothing().when(userService).deleteUserById(1);

        ResponseEntity<Void> response = userController.deleteUser(1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void deleteUser_notFound() {
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = userController.deleteUser(1);
        assertEquals(404, response.getStatusCodeValue());
    }
}