package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Model.Transaction;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Repository.TransactionRepository;
import com.projet6.PayMyBuddy.Services.TransactionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionService transactionService;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sender = new User();
        sender.setId(1);
        sender.setBalance(new BigDecimal("100.00"));

        receiver = new User();
        receiver.setId(2);
        receiver.setBalance(new BigDecimal("50.00"));
    }

    @Test
    void transfer_ShouldSucceed_WhenSenderHasEnoughBalance() {
        BigDecimal amount = new BigDecimal("30.00");

        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction transaction = transactionService.transfer(sender, receiver, amount, "Test transfert");

        // Vérifie les nouveaux soldes
        assertEquals(new BigDecimal("70.00"), sender.getBalance());
        assertEquals(new BigDecimal("80.00"), receiver.getBalance());

        // Vérifie que les utilisateurs sont sauvegardés
        verify(userService, times(1)).saveUser(sender);
        verify(userService, times(1)).saveUser(receiver);

        // Vérifie que la transaction est sauvegardée
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void transfer_ShouldThrowException_WhenSenderHasNotEnoughBalance() {
        BigDecimal amount = new BigDecimal("200.00"); // plus que le solde du sender

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transfer(sender, receiver, amount, "Transfert impossible");
        });

        assertEquals("Solde insuffisant pour le transfert.", exception.getMessage());

        // Aucun utilisateur ni transaction ne doit être sauvegardé
        verify(userService, never()).saveUser(any());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}