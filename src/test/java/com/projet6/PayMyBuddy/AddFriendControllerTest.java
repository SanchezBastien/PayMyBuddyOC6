package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Controller.AddFriendController;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.ConnectionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour le contrôleur {@link AddFriendController}.
 * Vérifie le bon fonctionnement de l’affichage du formulaire d’ajout d’ami
 * et du traitement du formulaire selon différents cas de figure.
 * Utilise des mocks pour isoler les dépendances.
 */
class AddFriendControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @InjectMocks
    private AddFriendController addFriendController;

    /**
     * Initialise les mocks avant chaque test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Vérifie que l’affichage du formulaire d’ajout d’ami retourne bien la vue attendue.
     */
    @Test
    void testShowAddFriendForm() {
        String result = addFriendController.showAddFriendForm();
        assertEquals("addfriend", result); // Correction casse
    }

    /**
     * Vérifie que le traitement de la demande d’ajout d’ami retourne bien la vue attendue,
     * et que le service métier est correctement appelé.
     */
    @Test
    void testProcessAddFriend() {
        String friendEmail = "ami@email.com";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user@email.com");
        Model model = mock(Model.class);

        User user = new User();
        when(userService.getUserByEmail("user@email.com")).thenReturn(Optional.of(user));
        when(connectionService.handleAddFriend(anyString(), any(User.class), any(Model.class), any(UserService.class)))
                .thenReturn("addfriend");

        String view = addFriendController.processAddFriend(friendEmail, principal, model);
        assertNotNull(view);
        assertEquals("addfriend", view); // optionnel, pour valider le résultat attendu
    }
}
