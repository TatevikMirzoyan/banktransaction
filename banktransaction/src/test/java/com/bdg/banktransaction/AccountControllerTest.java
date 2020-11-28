package com.bdg.banktransaction;

import com.bdg.banktransaction.controller.AccountController;
import com.bdg.banktransaction.controller.UserController;
import com.bdg.banktransaction.model.Account;
import com.bdg.banktransaction.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Tatevik Mirzoyan
 * Created on 26-Nov-20
 */
@SpringBootTest
public class AccountControllerTest {
    @Autowired
    AccountController accountController;
    @Autowired
    UserController userController;

    @Test
    void createUserAccount_whenAdminUsersRoleIsAdmin_alsoWithDefaultBalance() {
        User admin = new User("Tatev", "Mirzoyan", "tat.mirzoyan@gmail.com", "Tatev123++");
        admin.setRole("ADMIN");
        userController.addUser(admin);
        User user = new User("Tatev", "Atoyan", "tatev-a@gmail.com", "Tatev123++");
        userController.addUser(user);
        long adminId = admin.getId(); //this is my ADMIN users id
        long userId = user.getId();  //this is my USER users id
        Account account = new Account("AMD");
        Account account1 = new Account(1000, "USD");
        accountController.addUserAccount(account, userId, adminId);
        accountController.addUserAccount(account1, userId, adminId);
        assertEquals(account.getBalance(), 0.0);
        assertEquals(account1.getBalance(), 1000);
        assertEquals(account.getCurrency(), "AMD");
        assertEquals(account1.getCurrency(), "USD");
    }

    @Test
    void createAccount_whenAdminUsersRoleIsNotAdmin_thenAccountIsNotCreated() {
        User admin = new User("Tatev", "Mirzoyan", "tat.mirzoyan@gmail.com", "Tatev123++");
        admin.setRole("ADMIN");
        userController.addUser(admin);
        User user = new User("Tatev", "Atoyan", "tatev-a@gmail.com", "Tatev123++");
        userController.addUser(user);
        long adminId = user.getId();//this is not my ADMIN users id, it is USER users id
        long userId = user.getId(); //this is my USER users id
        Account account = new Account(200, "AMD");
        accountController.addUserAccount(account, userId, adminId);
        assertEquals(account.getBalance(), 200);
        assertEquals(account.getCurrency(), "AMD");
    }

    @Test
    void createAccount_withInvalidBalanceAndCurrency_thenCreatedAccountWithDefaultValues() {
        User admin = new User("Tatev", "Mirzoyan", "tat.mirzoyan@gmail.com", "Tatev123++");
        admin.setRole("ADMIN");
        userController.addUser(admin);
        User user = new User("Tatev", "Atoyan", "tatev-a@gmail.com", "Tatev123++");
        userController.addUser(user);
        long adminId = admin.getId(); //this is my ADMIN users id
        long userId = user.getId();  //this is my USER users id
        Account account = new Account(-200, "AAA");
        accountController.addUserAccount(account, userId, adminId);
        assertEquals(account.getBalance(), 0);
        assertNotEquals(-200, account.getBalance());
        assertEquals(account.getCurrency(), "AMD");
        assertNotEquals(account.getCurrency(), "AAA");
    }

    @Test
    void getAccountById() {
        User admin = new User("Tatev", "Mirzoyan", "tat.mirzoyan@gmail.com", "Tatev123++");
        admin.setRole("ADMIN");
        userController.addUser(admin);
        User user = new User("Tatev", "Atoyan", "tatev-a@gmail.com", "Tatev123++");
        userController.addUser(user);
        Account acc = new Account(200, "AMD");
        accountController.addUserAccount(acc, user.getId(), admin.getId());
        long accountId = acc.getId();
        Account account = accountController.getById(accountId);
        assertEquals(account.getId(), accountId);
        assertEquals(account.getCurrency(), acc.getCurrency());
        assertEquals(account.getBalance(), acc.getBalance());
    }

    @Test
    void getUserAccountsByUserId() {
        User admin = new User("Tatev", "Mirzoyan", "tat.mirzoyan@gmail.com", "Tatev123++");
        admin.setRole("ADMIN");
        userController.addUser(admin);
        User user = new User("Tatev", "Atoyan", "tatev-a@gmail.com", "Tatev123++");
        userController.addUser(user);
        long adminId = admin.getId(); //this is my ADMIN users id
        long userId = user.getId();  //this is my USER users id
        Account account = new Account(100, "EUR");
        accountController.addUserAccount(account, userId, adminId);
        long accountId = account.getId();
        List<Account> accounts = userController.getById(userId).getAccounts();
        for (Account acc : accounts) {
            if (acc.getId() == accountId) {
                assertEquals(acc.getBalance(), 100);
                assertEquals(acc.getCurrency(), "EUR");
            }
        }
    }

}
