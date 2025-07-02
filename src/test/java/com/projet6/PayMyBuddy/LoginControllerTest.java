package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Controller.LoginController;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginController loginController;

    public LoginControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowLoginForm() {
        String result = loginController.showLoginForm();
        assertEquals("login", result);
    }
}
