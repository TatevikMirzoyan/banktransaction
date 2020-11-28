package com.bdg.banktransaction.service;

import com.bdg.banktransaction.enums.UserRole;
import com.bdg.banktransaction.model.Account;
import com.bdg.banktransaction.model.Transaction;
import com.bdg.banktransaction.model.User;
import com.bdg.banktransaction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Tatevik Mirzoyan
 * Created on 22-Nov-20
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getById(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void setUserRoleAdmin(long userId, long adminId) {
        User user = getById(userId);
        User admin = getById(adminId);
        try {
            if (admin.getRole().equals(UserRole.ADMIN.name())) {
                user.setRole("ADMIN");
                updateUser(userId, user);
            } else
                throw new Exception("Only the Admin user can change the role of another USER");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Transaction> getUserTransactions(long id) {
        User user = getById(id);
        return user.getTransactions();
    }

    public List<Transaction> getUserTransactionsByDate(long id, LocalDate date) {
        List<Transaction> userTransactions = getUserTransactions(id);
        return userTransactions.stream()
                .filter(c -> c.getDateTime().toLocalDate().isEqual(date))
                .collect(Collectors.toList());
    }

    public User getUserByAccountId(long accountId) {
        User user = null;
        Iterable<User> iterable = userRepository.findAll();
        for (User item : iterable) {
            for (Account account : item.getAccounts()) {
                if (account.getId() == accountId) {
                    user = item;
                    break;
                }
            }
        }
        return user;
    }

    public void updateUser(long id, User user) {
        user.setId(id);
        userRepository.save(user);
    }
}
