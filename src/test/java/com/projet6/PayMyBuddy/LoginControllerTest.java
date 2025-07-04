package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Controller.LoginController;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @InjectMocks
    private LoginController loginController;

    private AutoCloseable closeable;

    private User user;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("user@test.com");
        user.setPassword("encodedpassword");
    }

    @Test
    void testShowLoginForm() {
        String view = loginController.showLoginForm();
        assertEquals("login", view);
    }

    @Test
    void testHandleLogin_Success() {
        // Utilisateur trouvé, bon mot de passe
        when(userService.getUserByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("correctPassword")).thenReturn("encodedpassword");

        String result = loginController.handleLogin("user@test.com", "correctPassword", model);

        assertEquals("redirect:/profile", result);
        verify(model, never()).addAttribute(eq("error"), any());
    }

    @Test
    void testHandleLogin_WrongPassword() {
        // Utilisateur trouvé, mauvais mot de passe
        when(userService.getUserByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("wrongPassword")).thenReturn("badEncodedPassword");

        String result = loginController.handleLogin("user@test.com", "wrongPassword", model);

        assertEquals("login", result);
        verify(model, times(1)).addAttribute("error", "Identifiants incorrects");
    }

    @Test
    void testHandleLogin_UserNotFound() {
        // Utilisateur non trouvé
        when(userService.getUserByEmail("absent@test.com")).thenReturn(Optional.empty());

        String result = loginController.handleLogin("absent@test.com", "nimportequoi", model);

        assertEquals("login", result);
        verify(model, times(1)).addAttribute("error", "Identifiants incorrects");
    }
}
