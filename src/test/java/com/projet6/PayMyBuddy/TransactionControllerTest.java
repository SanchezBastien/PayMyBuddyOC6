package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Controller.TransactionController;
import com.projet6.PayMyBuddy.Model.Transaction;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Services.TransactionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTransactions_ShouldReturnAllTransactions() {
        // Given
        List<Transaction> mockList = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getTransactions()).thenReturn(mockList);

        // When
        Iterable<Transaction> result = transactionController.getAllTransactions();

        // Then
        assertEquals(mockList, result);
        verify(transactionService).getTransactions();
    }
    
    @Test
    void getTransactionsBySenderEmail_ShouldReturnEmptyList_IfUserNotFound() {
        // Given
        String email = "notfound@email.com";
        when(userService.getUserByEmail(email)).thenReturn(Optional.empty());

        // When
        List<Transaction> result = transactionController.getTransactionsBySenderEmail(email);

        // Then
        assertEquals(Collections.emptyList(), result);
        verify(userService).getUserByEmail(email);
        verify(transactionService, never()).getTransactionBySender(any());
    }

    @Test
    void createTransaction_ShouldSaveAndReturnTransaction() {
        // Given
        Transaction tx = new Transaction();
        Transaction saved = new Transaction();
        when(transactionService.saveTransaction(tx)).thenReturn(saved);

        // When
        ResponseEntity<Transaction> response = transactionController.createTransaction(tx);

        // Then
        assertEquals(saved, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(transactionService).saveTransaction(tx);
    }
}
