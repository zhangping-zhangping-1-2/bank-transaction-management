package com.hsbc.banktransaction.controller;

import com.hsbc.banktransaction.entity.Transaction;
import com.hsbc.banktransaction.service.TransactionService;
import com.hsbc.banktransaction.common.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Result<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return Result.success(createdTransaction);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Transaction> updateTransaction(@PathVariable Long id, @Valid @RequestBody Transaction transaction) {
        Transaction updatedTransaction = transactionService.updateTransaction(id, transaction);
        return Result.success(updatedTransaction);
    }

    @GetMapping
    public Result<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return Result.success(transactions);
    }

    // 新增分页查询接口
    @GetMapping("/page")
    public Result<Page<Transaction>> getTransactionsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "transactionType", required = false) String transactionType) {
        Page<Transaction> transactionPage = transactionService.getPage(transactionType, page, size);
        return Result.success(transactionPage);
    }
}