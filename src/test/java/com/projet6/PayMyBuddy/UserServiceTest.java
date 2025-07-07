package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Repository.UserRepository;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le service {@link UserService}.
 * Vérifie la logique métier liée aux utilisateurs : enregistrement, recherche, suppression,
 * et gestion des cas d’utilisateur existant ou non.
 * Toutes les dépendances sont mockées.
 */
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setEmail("user@email.com");
    }

    /**
     * Vérifie que l’enregistrement d’un utilisateur fonctionne et retourne l’utilisateur sauvegardé.
     */
    @Test
    void testSaveUser_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User saved = userService.saveUser(user);

        assertNotNull(saved);
        assertEquals("user@email.com", saved.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Vérifie que la recherche par email retourne l’utilisateur si celui-ci existe.
     */
    @Test
    void testFindUserByEmail_UserExists() {
        when(userRepository.findByEmail("user@email.com")).thenReturn(Optional.of(user));

        Optional<User> found = userService.findUserByEmail("user@email.com");

        assertTrue(found.isPresent());
        assertEquals("user@email.com", found.get().getEmail());
        verify(userRepository, times(1)).findByEmail("user@email.com");
    }


    /**
     * Vérifie que la recherche par email retourne un Optional vide si aucun utilisateur n’existe.
     */
    @Test
    void testFindUserByEmail_UserNotFound() {
        when(userRepository.findByEmail("unknown@email.com")).thenReturn(Optional.empty());

        Optional<User> found = userService.findUserByEmail("unknown@email.com");

        assertFalse(found.isPresent());
        verify(userRepository, times(1)).findByEmail("unknown@email.com");
    }

    /**
     * Vérifie que la méthode {@code getUsers()} retourne bien tous les utilisateurs sous forme d’Iterable.
     */
    @Test
    void testGetUsers_ReturnsIterable() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        Iterable<User> result = userService.getUsers();
        assertNotNull(result);
        assertEquals(1, ((List<User>)result).size());
        verify(userRepository).findAll();
    }

    /**
     * Vérifie que la suppression d’un utilisateur par son identifiant appelle bien le repository.
     */
    @Test
    void testDeleteUserById() {
        userService.deleteUserById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    /**
     * Vérifie que la recherche par ID retourne l’utilisateur si celui-ci existe.
     */
    @Test
    void testGetUserById_UserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Optional<User> found = userService.getUserById(1);
        assertTrue(found.isPresent());
    }

    /**
     * Vérifie que la recherche par ID retourne un Optional vide si l’utilisateur n’existe pas.
     */
    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());
        Optional<User> found = userService.getUserById(2);
        assertFalse(found.isPresent());
    }
}
