package com.projet6.PayMyBuddy;

import com.projet6.PayMyBuddy.Model.Transaction;
import com.projet6.PayMyBuddy.Model.User;
import com.projet6.PayMyBuddy.Repository.TransactionRepository;
import com.projet6.PayMyBuddy.Services.TransactionService;
import com.projet6.PayMyBuddy.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionService transactionService;

    private User sender;
    private User receiver;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(1);
        sender.setBalance(new BigDecimal("100.00"));

        receiver = new User();
        receiver.setId(2);
        receiver.setBalance(new BigDecimal("50.00"));

        transaction = new Transaction();
        transaction.setId(1);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(new BigDecimal("20.00"));
        transaction.setDescription("Payment");
    }

    @Test
    void getTransactions_returnsAllTransactions() {
        List<Transaction> expected = Arrays.asList(transaction);
        when(transactionRepository.findAll()).thenReturn(expected);

        Iterable<Transaction> result = transactionService.getTransactions();

        assertNotNull(result);
        assertEquals(expected, result);
        verify(transactionRepository).findAll();
    }

    @Test
    void getTransactionById_returnsTransaction() {
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        Optional<Transaction> result = transactionService.getTransactionById(1);

        assertTrue(result.isPresent());
        assertEquals(transaction, result.get());
        verify(transactionRepository).findById(1);
    }

    @Test
    void getTransactionBySender_returnsTransactions() {
        List<Transaction> expected = Collections.singletonList(transaction);
        when(transactionRepository.findBySender(sender)).thenReturn(expected);

        List<Transaction> result = transactionService.getTransactionBySender(sender);

        assertEquals(expected, result);
        verify(transactionRepository).findBySender(sender);
    }

    @Test
    void saveTransaction_savesAndReturnsTransaction() {
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.saveTransaction(transaction);

        assertNotNull(result);
        assertEquals(transaction, result);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void deleteTransactionById_deletesTransaction() {
        doNothing().when(transactionRepository).deleteById(1);

        transactionService.deleteTransactionById(1);

        verify(transactionRepository).deleteById(1);
    }

    @Test
    void transfer_successfulTransfer_updatesBalancesAndSavesTransaction() {
        BigDecimal amount = new BigDecimal("30.00");
        BigDecimal senderOldBalance = sender.getBalance();
        BigDecimal receiverOldBalance = receiver.getBalance();

        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userService.saveUser(any(User.class))).thenAnswer(i -> i.getArguments()[0]);


        Transaction result = transactionService.transfer(sender, receiver, amount, "Test transfer");

        // Vérifie que les balances ont bien été modifiées
        assertEquals(senderOldBalance.subtract(amount), sender.getBalance());
        assertEquals(receiverOldBalance.add(amount), receiver.getBalance());

        // Vérifie que les utilisateurs ont été sauvegardés
        verify(userService).saveUser(sender);
        verify(userService).saveUser(receiver);

        // Vérifie que la transaction sauvegardée contient les bonnes infos
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction savedTransaction = captor.getValue();

        assertEquals(sender, savedTransaction.getSender());
        assertEquals(receiver, savedTransaction.getReceiver());
        assertEquals(amount, savedTransaction.getAmount());
        assertEquals("Test transfer", savedTransaction.getDescription());

        assertEquals(savedTransaction, result);
    }

    @Test
    void transfer_throwsException_whenInsufficientBalance() {
        BigDecimal amount = new BigDecimal("200.00"); // plus que le solde du sender

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transfer(sender, receiver, amount, "Should fail");
        });

        assertEquals("Solde insuffisant pour le transfert.", exception.getMessage());

        // Aucun appel aux méthodes saveUser ou saveTransaction
        verify(userService, never()).saveUser(any());
        verify(transactionRepository, never()).save(any());
    }
}
