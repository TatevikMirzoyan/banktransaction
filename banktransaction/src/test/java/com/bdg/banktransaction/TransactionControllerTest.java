package com.bdg.banktransaction;

import com.bdg.banktransaction.controller.AccountController;
import com.bdg.banktransaction.controller.TransactionController;
import com.bdg.banktransaction.controller.UserController;
import com.bdg.banktransaction.model.Account;
import com.bdg.banktransaction.model.Transaction;
import com.bdg.banktransaction.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Tatevik Mirzoyan
 * Created on 26-Nov-20
 */
@SpringBootTest
public class TransactionControllerTest {
    @Autowired
    TransactionController transactionController;
    @Autowired
    AccountController accountController;
    @Autowired
    UserController userController;

    @Test
    void createDepositTransactionAndGetById() {
        long accountId = 3; //Random number, of course, being sure that account with given id exists
        Account account = accountController.getById(accountId);
        Transaction transaction = new Transaction(account, 100, "deposit");
        transactionController.createTransaction(transaction);
        long transId = transaction.getId();
        assertEquals(transactionController.getById(transId).getType(), "DEPOSIT");
        assertEquals(transactionController.getById(transId).getStatus(), "PENDING");
    }

    @Test
    void createWithdrawalTransactionAndGetById() {
        long accountId = 2; //Random number, of course, being sure that account with given id exists
        Account account = accountController.getById(accountId);
        Transaction transaction = new Transaction(account, 25000, "Withdrawal");
        transactionController.createTransaction(transaction);
        long transId = transaction.getId();
        assertEquals(transactionController.getById(transId).getType(), "WITHDRAWAL");
        assertEquals(transactionController.getById(transId).getStatus(), "PENDING");
    }

    @Test
    void acceptTransactionByAdmin() {
        User admin = new User("Tatev", "Mirzoyan", "tat.mirzoyan@gmail.com", "Tatev123++");
        admin.setRole("ADMIN");
        userController.addUser(admin);
        User user = new User("Tatev", "Atoyan", "tatev-a@gmail.com", "Tatev123++");
        userController.addUser(user);
        long adminId = admin.getId(); //this is my ADMIN users id
        long userId = user.getId();  //this is my USER users id
        Account account = new Account(1000, "USD");
        accountController.addUserAccount(account, userId, adminId);
        Transaction transaction = new Transaction(account, 100, "deposit");
        transactionController.createTransaction(transaction);
        assertEquals(account.getBalance(), 1000);
        assertEquals(transaction.getStatus(), "PENDING");
        long accountId = account.getId();
        long transId = transaction.getId();
        float oldBalance = account.getBalance();
        transactionController.confirmTransaction(transId, adminId);
        float newBalance = accountController.getById(accountId).getBalance();
        assertNotEquals(newBalance, oldBalance);
        assertEquals(newBalance, 1100);
        assertEquals(transactionController.getById(transId).getStatus(), "ACCEPTED");
    }

    @Test
    void cancelTransactionByAdmin() {
        User admin = new User("Tatev", "Mirzoyan", "tat.mirzoyan@gmail.com", "Tatev123++");
        admin.setRole("ADMIN");
        userController.addUser(admin);
        User user = new User("Tatev", "Atoyan", "tatev-a@gmail.com", "Tatev123++");
        userController.addUser(user);
        long adminId = admin.getId(); //this is my ADMIN users id
        long userId = user.getId();  //this is my USER users id
        Account account = new Account(100, "EUR");
        accountController.addUserAccount(account, userId, adminId);
        Transaction transaction = new Transaction(account, 500, "withdrawal");
        transactionController.createTransaction(transaction);
        assertEquals(account.getBalance(), 100);
        assertEquals(transaction.getStatus(), "PENDING");
        long accountId = account.getId();
        long transId = transaction.getId();
        float oldBalance = account.getBalance();
        transactionController.confirmTransaction(transId, adminId);
        float newBalance = accountController.getById(accountId).getBalance();
        assertEquals(newBalance, oldBalance);
        assertEquals(transactionController.getById(transId).getStatus(), "CANCELED");
    }

    @Test
    void cancelTransactionByUser() {
        User admin = new User("Tatev", "Mirzoyan", "tat.mirzoyan@gmail.com", "Tatev123++");
        admin.setRole("ADMIN");
        userController.addUser(admin);
        User user = new User("Tatev", "Atoyan", "tatev-a@gmail.com", "Tatev123++");
        userController.addUser(user);
        long adminId = admin.getId(); //this is my ADMIN users id
        long userId = user.getId();  //this is my USER users id
        Account account = new Account(100, "EUR");
        accountController.addUserAccount(account, userId, adminId);
        Transaction transaction = new Transaction(account, 500, "deposit");
        transactionController.createTransaction(transaction);
        long accountId = account.getId();
        long transId = transaction.getId();
        float oldBalance = account.getBalance();
        assertEquals(account.getBalance(), 100);
        assertEquals(transaction.getStatus(), "PENDING");
        userController.cancelTransaction(transId);
        float newBalance = accountController.getById(accountId).getBalance();
        assertEquals(oldBalance, newBalance);
        assertEquals(transactionController.getById(transId).getStatus(), "CANCELED");
    }

    @Test
    void getTransactionsByDate() {
        User admin = new User("Tatev", "Mirzoyan", "tat.mirzoyan@gmail.com", "Tatev123++");
        admin.setRole("ADMIN");
        userController.addUser(admin);
        User user = new User("Tatev", "Atoyan", "tatev-a@gmail.com", "Tatev123++");
        userController.addUser(user);
        Account account = new Account(100, "EUR");
        accountController.addUserAccount(account, user.getId(), admin.getId());
        Transaction transaction1 = new Transaction(account, 500, "deposit");
        Transaction transaction2 = new Transaction(account, 100, "withdrawal");
        transactionController.createTransaction(transaction1);
        transactionController.createTransaction(transaction2);
        LocalDate date = LocalDate.now();
        List<Transaction> transactionList = transactionController.getTransactionsByDate(date);
        assertFalse(transactionList.isEmpty());
        assertThat(transactionList).isNotEmpty();
    }

    @Test
    void getTransactionsFilteredByDateUserStatus() {
        User admin = new User("Tatev", "Mirzoyan", "tat.mirzoyan@gmail.com", "Tatev123++");
        admin.setRole("ADMIN");
        userController.addUser(admin);
        User user = new User("Tatev", "Atoyan", "tatev-a@gmail.com", "Tatev123++");
        userController.addUser(user);
        Account account = new Account(100, "EUR");
        accountController.addUserAccount(account, user.getId(), admin.getId());
        Transaction transaction1 = new Transaction(account, 500, "deposit");
        transactionController.createTransaction(transaction1);
        LocalDate date = LocalDate.now();
        List<Transaction> transactionList = transactionController.getTransactionsByFilters(user.getId(), date, "PENDING");
        assertFalse(transactionList.isEmpty());
        assertEquals(transactionList.get(0).getStatus(), "PENDING");
        assertEquals(transactionList.get(0).getDateTime().toLocalDate(), date);
        assertEquals(userController.getUserByAccountId(transactionList.stream().findAny().get().getAccount().getId()).getId(), user.getId());
    }

}
