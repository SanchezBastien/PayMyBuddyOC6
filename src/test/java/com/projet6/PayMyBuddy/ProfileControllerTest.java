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

/**
 * Tests unitaires pour le contrôleur {@link ProfileController}.
 * Vérifie l’affichage du profil utilisateur dans différents cas (succès, utilisateur non trouvé).
 * Les dépendances sont mockées pour isoler le comportement du contrôleur.
 */
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

    /**
     * Vérifie que l’affichage du profil fonctionne et transmet les bonnes informations
     * au modèle lorsque l’utilisateur connecté est trouvé.
     */
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

    /**
     * Vérifie que l’affichage du profil lance une exception ou gère le cas
     * où l’utilisateur connecté n’existe pas.
     */
    @Test
    void testShowProfileUserNotFound() {
        when(principal.getName()).thenReturn("user@email.com");
        when(userService.getUserByEmail("user@email.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            profileController.showProfile(model, principal);
        });
    }
}