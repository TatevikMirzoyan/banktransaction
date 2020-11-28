package com.bdg.banktransaction.controller;

import com.bdg.banktransaction.model.Account;
import com.bdg.banktransaction.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tatevik Mirzoyan
 * Created on 22-Nov-20
 */
@Transactional
@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping(path = "/{id}")
    public Account getById(@PathVariable long id) {
        return accountService.getById(id);
    }

    @PostMapping(path = "/{adminId}/{userId}")
    public void addUserAccount(@RequestBody Account account, @PathVariable long userId, @PathVariable long adminId) {
        accountService.addAccount(account, userId, adminId);
    }

}
