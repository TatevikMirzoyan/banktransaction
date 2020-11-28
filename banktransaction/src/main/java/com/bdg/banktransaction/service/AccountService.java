package com.bdg.banktransaction.service;

import com.bdg.banktransaction.enums.UserRole;
import com.bdg.banktransaction.model.Account;
import com.bdg.banktransaction.model.User;
import com.bdg.banktransaction.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Tatevik Mirzoyan
 * Created on 22-Nov-20
 */
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserService userService;

    public Account getById(long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.orElse(null);
    }

    public void addAccount(Account account, long userId, long adminId) {
        User admin = userService.getById(adminId);
        User user = userService.getById(userId);
        try {
            if (admin.getRole().equals(String.valueOf(UserRole.ADMIN))) {
                if (account != null) {
                accountRepository.save(account);
                user.setAccount(account);
                userService.updateUser(userId, user);
                }
            } else throw new Exception("Only the ADMIN user can create account for another USER");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
