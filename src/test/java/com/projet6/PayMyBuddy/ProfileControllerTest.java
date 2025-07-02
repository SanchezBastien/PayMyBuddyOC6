package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Controller.ProfileController;
import com.projet6.PayMyBuddy.Services.UserService;
import com.projet6.PayMyBuddy.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private Model model;
    @Mock
    private Principal principal;
    @InjectMocks
    private ProfileController profileController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowProfileSuccess() {
        when(principal.getName()).thenReturn("user@email.com");
        User user = new User();
        user.setEmail("user@email.com");
        when(userService.getUserByEmail("user@email.com")).thenReturn(Optional.of(user));

        String view = profileController.showProfile(model, principal);
        assertEquals("profile", view);
        verify(model).addAttribute(eq("user"), any(User.class));
    }

    @Test
    void testShowProfileUserNotFound() {
        when(principal.getName()).thenReturn("user@email.com");
        when(userService.getUserByEmail("user@email.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            profileController.showProfile(model, principal);
        });
    }
}