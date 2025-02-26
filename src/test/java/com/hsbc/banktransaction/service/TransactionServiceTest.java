package com.hsbc.banktransaction.service;

import com.hsbc.banktransaction.dao.TransactionRepository;
import com.hsbc.banktransaction.entity.Transaction;
import com.hsbc.banktransaction.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    private Transaction sampleTransaction;

    @BeforeEach
    public void setUp() {
        sampleTransaction = new Transaction();
        sampleTransaction.setTransactionType("Transfer");
        sampleTransaction.setFromAccount("123456");
        sampleTransaction.setToAccount("654321");
        sampleTransaction.setAmount(BigDecimal.valueOf(1000));
        sampleTransaction.setDescription("Sample transfer");
        sampleTransaction.setCode("12345");
    }

    @Test
    public void testCreateTransaction() {
        Mockito.when(transactionRepository.existsByCode(sampleTransaction.getCode())).thenReturn(false);
        Mockito.when(transactionRepository.save(sampleTransaction)).thenReturn(sampleTransaction);

        Transaction result = transactionService.createTransaction(sampleTransaction);
        assertEquals(sampleTransaction, result);
    }

    @Test
    public void testCreateTransactionWithExistingCode() {
        Mockito.when(transactionRepository.existsByCode(sampleTransaction.getCode())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> {
            transactionService.createTransaction(sampleTransaction);
        });
        assertEquals("交易存在,不能再添加", exception.getResultCode().getMessage());
    }

    @Test
    public void testDeleteTransaction() {
        Long id = 1L;
        Mockito.when(transactionRepository.findById(id)).thenReturn(Optional.of(sampleTransaction));
        Mockito.doNothing().when(transactionRepository).deleteById(id);

        transactionService.deleteTransaction(id);
        Mockito.verify(transactionRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void testDeleteNonExistingTransaction() {
        Long id = 1L;
        Mockito.when(transactionRepository.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            transactionService.deleteTransaction(id);
        });
        assertEquals("交易不存在，无法删除", exception.getResultCode().getMessage());
    }

    @Test
    public void testUpdateTransaction() {
        Long id = 1L;
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setTransactionType("Withdrawal");
        updatedTransaction.setFromAccount("654321");
        updatedTransaction.setToAccount("123456");
        updatedTransaction.setAmount(BigDecimal.valueOf(500));
        updatedTransaction.setDescription("Sample withdrawal");
        updatedTransaction.setCode("54321");

        Mockito.when(transactionRepository.findById(id)).thenReturn(Optional.of(sampleTransaction));
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(updatedTransaction);

        Transaction result = transactionService.updateTransaction(id, updatedTransaction);
        assertEquals(updatedTransaction, result);
    }

    @Test
    public void testUpdateNonExistingTransaction() {
        Long id = 1L;
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setTransactionType("Withdrawal");
        updatedTransaction.setFromAccount("654321");
        updatedTransaction.setToAccount("123456");
        updatedTransaction.setAmount(BigDecimal.valueOf(500));
        updatedTransaction.setDescription("Sample withdrawal");
        updatedTransaction.setCode("54321");

        Mockito.when(transactionRepository.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            transactionService.updateTransaction(id, updatedTransaction);
        });
        assertEquals("交易不存在，无法修改", exception.getResultCode().getMessage());
    }

    @Test
    public void testGetAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(sampleTransaction);

        Mockito.when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.getAllTransactions();
        assertEquals(transactions, result);
    }

    @Test
    public void testGetTransactionsByPage() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(sampleTransaction);
        Page<Transaction> pageResult = new PageImpl<>(transactions, pageRequest, transactions.size());

        Mockito.when(transactionRepository.findAll(pageRequest)).thenReturn(pageResult);

        Page<Transaction> result = transactionService.getTransactionsByPage(pageRequest);
        assertEquals(pageResult, result);
    }

    @Test
    public void testGetPage() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("id").descending());
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCode("54321");
        transaction.setAmount(BigDecimal.valueOf(500));
        transaction.setDescription("Sample withdrawal");

        Page<Transaction> page = new PageImpl<>(Collections.singletonList(transaction));
        when(transactionRepository.findAll(pageable)).thenReturn(page);

        Page<Transaction> result = transactionService.getPage("", pageable.getPageNumber(), page.getSize());
        assertEquals(page.getTotalElements(), result.getTotalElements());
        verify(transactionRepository).findAll(pageable);
    }

    @Test
    public void testPageByType() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by("id").descending());
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCode("54321");
        transaction.setTransactionType("withdrawal");
        transaction.setAmount(BigDecimal.valueOf(500));
        transaction.setDescription("Sample withdrawal");

        Page<Transaction> page = new PageImpl<>(Collections.singletonList(transaction));
        when(transactionRepository.findByTransactionType("withdrawal", pageable)).thenReturn(page);

        Page<Transaction> result = transactionService.getPage("withdrawal", pageable.getPageNumber(), page.getSize());
        assertEquals(page.getTotalElements(), result.getTotalElements());
        verify(transactionRepository).findByTransactionType("withdrawal", pageable);
    }

    @Test
    public void testGetPageWithNegativePageNumber() {
        int page = -1;
        int size = 10;
        String transactionType = "Transfer";
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getPage(transactionType, page, size);
        });
    }

    @Test
    public void testGetPageWithNegativeSize() {
        int page = 0;
        int size = -1;
        String transactionType = "Transfer";
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getPage(transactionType, page, size);
        });
    }

    @Test
    public void testCreateTransactionWithNull() {
        assertThrows(NullPointerException.class, () -> {
            transactionService.createTransaction(null);
        });
    }

    @Test
    public void testUpdateTransactionWithNull() {
        Long id = 1L;
        assertThrows(CustomException.class, () -> {
            transactionService.updateTransaction(id, null);
        });
        System.out.println();
    }

    @Test
    public void testDeleteTransactionWithNullId() {
        assertThrows(CustomException.class, () -> {
            transactionService.deleteTransaction(null);
        });
    }

    @Test
    public void testGetAllTransactionsWhenRepositoryReturnsNull() {
        when(transactionRepository.findAll()).thenReturn(null);
        List<Transaction> result = transactionService.getAllTransactions();
        assertEquals(0, result.size());
    }

    @Test
    public void testGetPageWhenRepositoryReturnsNull() {
        int page = 0;
        int size = 10;
        String transactionType = "Transfer";
        Pageable pageable = PageRequest.of(page, size);
        when(transactionRepository.findByTransactionType(transactionType, pageable)).thenReturn(null);
        Page<Transaction> result = transactionService.getPage(transactionType, page, size);
        assertEquals(0, result.getTotalElements());
    }
}