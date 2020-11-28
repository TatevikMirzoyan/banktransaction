package com.bdg.banktransaction.controller;

import com.bdg.banktransaction.model.Transaction;
import com.bdg.banktransaction.model.User;
import com.bdg.banktransaction.service.TransactionService;
import com.bdg.banktransaction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping(path = "/{id}")
    public User getById(@PathVariable long id) {
        return userService.getById(id);
    }

    @PostMapping(path = "/register")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @PutMapping(path = "/{id}/update")
    public void updateUser(@PathVariable long id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

    @PutMapping(path = "/{userId}/role/{adminId}")
    public void setUserRoleAdmin(@PathVariable long userId, @PathVariable long adminId) {
        userService.setUserRoleAdmin(userId, adminId);
    }

    @PatchMapping(path = "/transaction/{id}/cancel")
    public void cancelTransaction(@PathVariable long id) {
        transactionService.cancelTransaction(id);
    }

    @GetMapping(path = "/{id}/transaction")
    public List<Transaction> getUserTransactions(@PathVariable long id) {
        return userService.getUserTransactions(id);
    }

    @GetMapping(path = "/{id}/transaction/filter")
    public List<Transaction> getUserTransactionsByDate(@PathVariable long id, @RequestParam(value = "date") LocalDate date) {
        return userService.getUserTransactionsByDate(id, date);
    }

    @GetMapping(path = "/account/{accountId}")
    public User getUserByAccountId(@PathVariable long accountId) {
        return userService.getUserByAccountId(accountId);
    }


}
