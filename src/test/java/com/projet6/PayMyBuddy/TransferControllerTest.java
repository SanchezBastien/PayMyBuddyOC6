package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Controller.TransferController;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.ConnectionService;
import com.projet6.PayMyBuddy.Services.TransactionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TransferControllerTest {

    @Mock
    private TransactionService transactionService;
    @Mock
    private ConnectionService connectionService;
    @Mock
    private UserService userService;
    @Mock
    private Model model;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("GET /transfer ajoute les attributs attendus au modèle et retourne 'transfer'")
    void showTransferPage_shouldAddAttributesAndReturnTransferView() {
        // Arrange
        Principal principal = () -> "alice@example.com";
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("alice@example.com");

        when(userService.getUserByEmail("alice@example.com")).thenReturn(Optional.of(mockUser));
        when(connectionService.getConnectionsByUser(mockUser)).thenReturn(Collections.emptyList());
        when(transactionService.getTransactionBySender(mockUser)).thenReturn(Collections.emptyList());

        // Act
        String result = transferController.showTransferPage(model, principal);

        // Assert
        assertEquals("transfer", result);
        verify(model).addAttribute(eq("connections"), any());
        verify(model).addAttribute(eq("transactions"), any());
    }

    @Test
    @DisplayName("POST /transfer appelle le service de transfert et retourne la redirection")
    void processTransfer_shouldCallServiceAndRedirect() {
        // Arrange
        Principal principal = () -> "alice@example.com";
        User sender = new User();
        sender.setId(1);
        sender.setEmail("alice@example.com");
        User receiver = new User();
        receiver.setId(2);
        receiver.setEmail("bob@example.com");

        when(userService.getUserByEmail("alice@example.com")).thenReturn(Optional.of(sender));
        when(userService.getUserById(2)).thenReturn(Optional.of(receiver));

        // Act
        String result = transferController.processTransfer(2, "Remboursement", new BigDecimal("12.50"), principal);

        // Assert
        assertEquals("redirect:/transfer", result);
        verify(transactionService).transfer(sender, receiver, new BigDecimal("12.50"), "Remboursement");
    }

    @Test
    @DisplayName("POST /transfer lève une exception si l'utilisateur n'existe pas")
    void processTransfer_userNotFound_shouldThrow() {
        // Arrange
        Principal principal = () -> "ghost@example.com";
        when(userService.getUserByEmail("ghost@example.com")).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> {
            transferController.processTransfer(999, "Test", new BigDecimal("10.00"), principal);
        });
    }

    @Test
    @DisplayName("POST /transfer lève une exception si le receiver n'existe pas")
    void processTransfer_receiverNotFound_shouldThrow() {
        // Arrange
        Principal principal = () -> "alice@example.com";
        User sender = new User();
        sender.setId(1);
        sender.setEmail("alice@example.com");

        when(userService.getUserByEmail("alice@example.com")).thenReturn(Optional.of(sender));
        when(userService.getUserById(1234)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> {
            transferController.processTransfer(1234, "Test", new BigDecimal("20.00"), principal);
        });
    }
}
