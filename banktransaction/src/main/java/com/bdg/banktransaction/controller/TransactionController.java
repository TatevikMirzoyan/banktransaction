package com.bdg.banktransaction.controller;

import com.bdg.banktransaction.model.Transaction;
import com.bdg.banktransaction.model.User;
import com.bdg.banktransaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Tatevik Mirzoyan
 * Created on 22-Nov-20
 */
@Transactional
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public void createTransaction(@RequestBody Transaction transaction) {
        transactionService.createTransaction(transaction);
    }

    @PatchMapping(path = "/{id}/confirm/{adminId}")
    public void confirmTransaction(@PathVariable long id, @PathVariable long adminId) {
        transactionService.confirmTransaction(id, adminId);
    }

    @GetMapping(path = "/{id}")
    public Transaction getById(@PathVariable long id) {
        return transactionService.getById(id);
    }

    @GetMapping(path = "/{transId}/user")
    public User getUserByTransactionId(@PathVariable long transId) {
        return transactionService.getUserByTransactionId(transId);
    }

    //TODO get transactions from date1 to date2
    @GetMapping(path = "/filter/date")
    public List<Transaction> getTransactionsByDate(@RequestParam(value = "date")
            @DateTimeFormat(pattern = "dd/MM/yy") LocalDate date) {
        return transactionService.getTransactionsByDate(date);
    }

    @GetMapping(path = "/filter")
    public List<Transaction> getTransactionsByFilters(
            @RequestParam(value = "userId") long userId,
            @RequestParam(value = "date") @DateTimeFormat(pattern = "dd/MM/yy") LocalDate date,
            @RequestParam(value = "status") String status) {
        return transactionService.getTransactionsByFilters(userId, date, status);
    }
}
