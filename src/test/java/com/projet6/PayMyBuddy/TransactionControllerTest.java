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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionController transactionController;

    private AutoCloseable closeable;

    private User user;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setEmail("user@test.com");

        transaction = new Transaction();
        transaction.setId(10);
        transaction.setSender(user);
        transaction.setAmount(new BigDecimal("100.11"));
    }

    @Test
    void testGetAllTransactions() {
        List<Transaction> transactions = Arrays.asList(transaction);
        when(transactionService.getTransactions()).thenReturn(transactions);

        Iterable<Transaction> result = transactionController.getAllTransactions();

        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
        assertEquals(transaction, result.iterator().next());
        verify(transactionService, times(1)).getTransactions();
    }

    @Test
    void testGetTransactionsBySenderEmail_Found() {
        List<Transaction> transactions = Arrays.asList(transaction);
        when(userService.getUserByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(transactionService.getTransactionBySender(user)).thenReturn(transactions);

        List<Transaction> result = transactionController.getTransactionsBySenderEmail("user@test.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transaction, result.get(0));
        verify(userService, times(1)).getUserByEmail("user@test.com");
        verify(transactionService, times(1)).getTransactionBySender(user);
    }

    @Test
    void testGetTransactionsBySenderEmail_NotFound() {
        when(userService.getUserByEmail("unknown@test.com")).thenReturn(Optional.empty());

        List<Transaction> result = transactionController.getTransactionsBySenderEmail("unknown@test.com");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userService, times(1)).getUserByEmail("unknown@test.com");
        verify(transactionService, never()).getTransactionBySender(any());
    }

    @Test
    void testCreateTransaction() {
        when(transactionService.saveTransaction(transaction)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(transaction, response.getBody());
        verify(transactionService, times(1)).saveTransaction(transaction);
    }

    @Test
    void testUpdateTransaction_Found() {
        Transaction updatedDetails = new Transaction();
        updatedDetails.setAmount(new BigDecimal("200.0"));
        updatedDetails.setSender(user);

        when(transactionService.getTransactionById(10)).thenReturn(Optional.of(transaction));
        when(transactionService.saveTransaction(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Transaction> response = transactionController.updateTransaction(10, updatedDetails);

        assertNotNull(response);
        assertEquals(200.0, response.getStatusCodeValue());
        assertEquals(new BigDecimal("200.0"), response.getBody().getAmount());
        verify(transactionService, times(1)).getTransactionById(10);
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
    }

    @Test
    void testUpdateTransaction_NotFound() {
        when(transactionService.getTransactionById(99)).thenReturn(Optional.empty());

        ResponseEntity<Transaction> response = transactionController.updateTransaction(99, new Transaction());

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(transactionService, times(1)).getTransactionById(99);
        verify(transactionService, never()).saveTransaction(any());
    }
}