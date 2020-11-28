package com.bdg.banktransaction.service;

import com.bdg.banktransaction.enums.TransactionStatus;
import com.bdg.banktransaction.enums.TransactionType;
import com.bdg.banktransaction.enums.UserRole;
import com.bdg.banktransaction.model.Account;
import com.bdg.banktransaction.model.Transaction;
import com.bdg.banktransaction.model.User;
import com.bdg.banktransaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Tatevik Mirzoyan
 * Created on 22-Nov-20
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;

    private void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    private void updateTransaction(long id, Transaction transaction) {
        transaction.setId(id);
        saveTransaction(transaction);
    }

    public Transaction getById(long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.orElse(null);
    }

    public User getUserByTransactionId(long transId) {
        return userService.getUserByAccountId(getById(transId).getAccount().getId());
    }

    public List<Transaction> getTransactionsByDate(LocalDate date) {
        List<Transaction> transactions = new ArrayList<>();
        Iterable<Transaction> iterable = transactionRepository.findAll();
        for (Transaction item : iterable) {
            if (item.getDateTime().toLocalDate().equals(date)) {
                transactions.add(item);
            }
        }
        return transactions;
    }

    public List<Transaction> getTransactionsByFilters(long userId, LocalDate date, String status) {
        List<Transaction> transactionsByDate = getTransactionsByDate(date);
        return transactionsByDate.stream()
                .filter(c -> userService.getUserByAccountId(c.getAccount().getId()).getId() == userId
                        && c.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createTransaction(Transaction transaction) {
        User user = userService.getUserByAccountId(transaction.getAccount().getId());
        saveTransaction(transaction);
        user.setTransactions(transaction);
    }

    @Transactional
    public void confirmTransaction(long id, long adminId) {
        try {
            if (userService.getById(adminId).getRole().equals(String.valueOf(UserRole.ADMIN))) {
                Transaction transaction = getById(id);
                Account account = transaction.getAccount();
                if (transaction.getStatus().equalsIgnoreCase(String.valueOf(TransactionStatus.PENDING))) {
                    if (transaction.getType().equalsIgnoreCase(String.valueOf(TransactionType.WITHDRAWAL))) {
                        if (account.getBalance() >= transaction.getAmount()) {
                            account.setBalance(account.getBalance() - transaction.getAmount());
                        } else {
                            cancelTransaction(id);
                        }
                    } else if (transaction.getType().equalsIgnoreCase(String.valueOf(TransactionType.DEPOSIT))) {
                        account.setBalance(account.getBalance() + transaction.getAmount());
                        transaction.setStatus(TransactionStatus.ACCEPTED);
                        updateTransaction(id, transaction);
                    }
                }
            } else throw new Exception("Only ADMIN user can ACCEPT the transaction");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    public void cancelTransaction(long id) {
        Transaction transaction = getById(id);
        if (transaction.getStatus().equalsIgnoreCase(String.valueOf(TransactionStatus.PENDING))) {
            transaction.setStatus(TransactionStatus.CANCELED);
            updateTransaction(transaction.getId(), transaction);
        }
    }
}
