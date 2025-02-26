package com.hsbc.banktransaction.service;

import com.hsbc.banktransaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    void deleteTransaction(Long id);
    Transaction updateTransaction(Long id, Transaction transaction);
    List<Transaction> getAllTransactions();
    // 新增分页查询方法
    Page<Transaction> getTransactionsByPage(Pageable pageable);
    Page<Transaction> getPage(String transactionType, int page, int size);
}