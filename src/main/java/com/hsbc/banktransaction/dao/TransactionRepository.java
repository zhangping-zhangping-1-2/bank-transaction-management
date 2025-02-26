package com.hsbc.banktransaction.dao;

import com.hsbc.banktransaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    boolean existsByCode(String code);
    Page<Transaction> findByTransactionType(String transactionType, Pageable pageable);

}