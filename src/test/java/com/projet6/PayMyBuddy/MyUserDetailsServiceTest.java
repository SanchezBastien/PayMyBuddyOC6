package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Repository.UserRepository;
import com.projet6.PayMyBuddy.Services.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("test@email.com");
        user.setPassword("password");
    }

    /**
     * Teste le chargement d’un utilisateur existant par son email.
     * Vérifie que la méthode {@code loadUserByUsername} retourne bien les informations de l’utilisateur attendu.
     */
    @Test
    void testLoadUserByUsername_UserExists() {
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        assertNotNull(myUserDetailsService.loadUserByUsername("test@email.com"));
        verify(userRepository, times(1)).findByEmail("test@email.com");
    }

    /**
     * Teste la gestion du cas où l’utilisateur n’existe pas.
     * Vérifie que la méthode {@code loadUserByUsername} lance une exception
     * {@code UsernameNotFoundException} si l’email n’est pas trouvé.
     */
    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("notfound@email.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                myUserDetailsService.loadUserByUsername("notfound@email.com")
        );
    }
}
