package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Controller.SignupController;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SignupControllerTest {

    @InjectMocks
    private SignupController signupController;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processSignup_ShouldSaveUser_AndRedirectToLogin() {
        // GIVEN
        String username = "testuser";
        String email = "test@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        // WHEN
        String viewName = signupController.processSignup(username, email, password);

        // THEN
        assertEquals("redirect:/login", viewName);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).saveUser(captor.capture());
        User savedUser = captor.getValue();
        assertEquals(username, savedUser.getUsername());
        assertEquals(email, savedUser.getEmail());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(BigDecimal.ZERO, savedUser.getBalance());
        verify(passwordEncoder).encode(password);
    }

    @Test
    void processSignup_ShouldEncodePassword() {
        String username = "otheruser";
        String email = "other@example.com";
        String password = "secret";
        String encodedPassword = "encodedSecret";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        signupController.processSignup(username, email, password);

        verify(passwordEncoder).encode(password);
    }

    @Test
    void processSignup_ShouldCallSaveUserWithUserHavingBalanceZero() {
        String username = "zeroUser";
        String email = "zero@example.com";
        String password = "123";
        String encodedPassword = "encoded123";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        signupController.processSignup(username, email, password);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).saveUser(captor.capture());
        assertEquals(BigDecimal.ZERO, captor.getValue().getBalance());
    }
}
