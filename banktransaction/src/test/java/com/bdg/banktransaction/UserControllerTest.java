package com.bdg.banktransaction;

import com.bdg.banktransaction.controller.UserController;
import com.bdg.banktransaction.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Tatevik Mirzoyan
 * Created on 26-Nov-20
 */
@SpringBootTest
public class UserControllerTest {

    @Autowired
    UserController userController;

    @Test
    void createAdminUser_returnAdminUsersId(){
        User admin = new User("Tatev", "Mirzoyan", "tat.mirzoyan@gmail.com", "Tatev123++");
        admin.setRole("ADMIN");
        userController.addUser(admin);
        assertEquals(admin.getRole(),"ADMIN");
    }

    @Test
    void addUser_withDefaultUserRole_user() {
        User user = new User("Tatev", "Atoyan", "tatev-a@gmail.com", "Tatev123++");
        userController.addUser(user);
        assertEquals(user.getRole(), "USER");
        assertEquals(user.getPassword(), "Tatev123++");
    }

    @Test
    void getUserById() {
        User user = new User("Tatev", "Atoyan", "tatev-a@gmail.com", "Tatev123++");
        userController.addUser(user);
        long id = user.getId();
        assertEquals(userController.getById(id).getFirstName(), "Tatev");
    }

    @Test
    void updateUserRoleByAdmin_setAdmin() {
        User admin = new User("Tatev", "Mirzoyan", "tat.mirzoyan@gmail.com", "Tatev123++");
        admin.setRole("ADMIN");
        userController.addUser(admin);
        long adminId = admin.getId();
        User user = new User("Tatev", "Mirzoyan", "tat@gmail.com", "Tatev111++");
        userController.addUser(user);
        long userId = user.getId();
        assertEquals(user.getRole(), "USER");
        userController.setUserRoleAdmin(userId, adminId);
        assertEquals(userController.getById(userId).getRole(), "ADMIN");
    }

    @Test
    void updateUserInfo() {
        User user = new User("Tatev", "Mirzoyan", "tat@gmail.com", "Tatev123++");
        userController.addUser(user);
        long id = user.getId();
        User newUser = new User("Satenik", "Mirzoyan", "Saten@gmail.com", "Satenik123++");
        userController.updateUser(id, newUser);
        assertEquals(userController.getById(id).getFirstName(), "Satenik");
    }

    @Test
    void addUser_withInvalidEmailAndPassword_userWillNotBeCreated() {
        try {
            User user = new User("Tatev", "Atoyan", "Tatev", "Tatev");
            userController.addUser(user);
            assertEquals(user.getPassword(), "Tatev");
            assertEquals(user.getEmail(), "Tatev");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}
