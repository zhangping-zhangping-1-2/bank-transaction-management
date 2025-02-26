package com.hsbc.banktransaction.service.impl;

import com.hsbc.banktransaction.common.ResultCode;
import com.hsbc.banktransaction.dao.TransactionRepository;
import com.hsbc.banktransaction.entity.Transaction;
import com.hsbc.banktransaction.exception.CustomException;
import com.hsbc.banktransaction.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    @CacheEvict(value = "transactions", allEntries = true)
    public Transaction createTransaction(Transaction transaction) {
        log.info("TransactionServiceImpl.create transaction={}", transaction);
        if (transactionRepository.existsByCode(transaction.getCode())) {
            //throw new RuntimeException("交易已存在 code: " + transaction.getCode());
            throw new CustomException(ResultCode.TRANSACTION_EXIST_NOT_ADD);
        }
        return transactionRepository.save(transaction);
    }

    @Override
    @CacheEvict(value = "transactions", allEntries = true)
    public void deleteTransaction(Long id) {
        log.info("TransactionServiceImpl.delete id={}", id);
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isEmpty()) {
            //throw new RuntimeException("交易不存在，无法删除");
            throw new CustomException(ResultCode.TRANSACTION_NOT_EXIST_DELETE);
        }
        transactionRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = "transactions", allEntries = true)
    public Transaction updateTransaction(Long id, Transaction transaction) {
        log.info("TransactionServiceImpl.update id={},transaction={}", id,transaction);
        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
        if (existingTransaction.isEmpty()) {
            //throw new RuntimeException("交易不存在，无法修改");
            throw new CustomException(ResultCode.TRANSACTION_NOT_EXIST_UPDATE);
        }
        Transaction updatedTransaction = existingTransaction.get();
        updatedTransaction.setTransactionType(transaction.getTransactionType());
        updatedTransaction.setFromAccount(transaction.getFromAccount());
        updatedTransaction.setToAccount(transaction.getToAccount());
        updatedTransaction.setAmount(transaction.getAmount());
        updatedTransaction.setDescription(transaction.getDescription());
        updatedTransaction.setCode(transaction.getCode());
        updatedTransaction.setUpdateTime(java.time.LocalDateTime.now());
        return transactionRepository.save(updatedTransaction);
    }

    @Override
    @Cacheable(value = "transactions")
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions != null ? transactions : Collections.emptyList();
    }

    @Override
    @Cacheable(value = "transactions")
    public Page<Transaction> getTransactionsByPage(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "transactions")
    public Page<Transaction> getPage(String transactionType, int page, int size) {
        log.info("TransactionServiceImpl.getPage transactionType={}, pageIndex={}, pageSize={}", transactionType, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Transaction> result;
        if (StringUtils.isBlank(transactionType)) {
            result = transactionRepository.findAll(pageable);
        } else {
            result = transactionRepository.findByTransactionType(transactionType, pageable);
        }

        // 检查结果是否为 null，如果是则返回一个空的 Page 对象
        if (result == null) {
            List<Transaction> emptyList = Collections.emptyList();
            result = new PageImpl<>(emptyList, pageable, 0);
        }
        return result;
    }
}