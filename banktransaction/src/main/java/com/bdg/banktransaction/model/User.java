package com.bdg.banktransaction.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tatevik Mirzoyan
 * Created on 22-Nov-20
 */

@Data
@NoArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 30)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    // TODO private String confirmPassword;
    @Column(columnDefinition = "varchar(10) default 'USER'")
    private String role = "USER";
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Account> accounts;
    @OneToMany
    private List<Transaction> transactions;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        setEmail(email);
        setPassword(password);
    }

    public void setAccount(Account account) {
        accounts.add(account);
    }

    public void setTransactions(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * The setter method first checks if the given email is valid.
     * <p>
     * The email address must start with “[a-zA-Z0-9_+&*-]” ,
     * optional follow by “.[a-zA-Z0-9_+&*-]“, and end with a “@” symbol.
     * The email’s domain name must start with “[a-zA-Z0-9-]”,
     * follow by first level Tld (.com, .net) “.[a-zA-Z0-9]”
     * and optional follow by a second level Tld (.com.au, .com.my) “\.[A-Za-z]{2,7}”,
     * where second level Tld must start with a dot “.”
     * and length must equal or more than 2 characters and less than 7 characters.
     *
     * @param email - the given email
     * @throws IllegalArgumentException - if the given email is invalid
     */
    public void setEmail(String email) {
        try {
            if (isValidEmail(email)) {
                this.email = email;
            } else throw new IllegalArgumentException("Invalid email");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * The setter method first checks if the given password is valid password.
     * "The password must contain " +
     * "at least 8 characters and at most 20 characters," +
     * "at least one digit," +
     * "at least one upper case letter," +
     * "at least one lower case letter," +
     * "at least one special character which includes !@#$%&*()-+=^," +
     * "it can not contain any white space"
     *
     * @param password - the given password
     * @throws IllegalArgumentException - if the given password is invalid
     */
    public void setPassword(String password) {
        try {
            if (isValidPassword(password)) {
                this.password = Base64.getEncoder().encodeToString(password.getBytes());
            } else throw new IllegalArgumentException("Invalid password");
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public String getPassword() {
        return new String(Base64.getDecoder().decode(password));
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])" +
                "(?=.*[a-z])" +
                "(?=.*[A-Z])" +
                "(?=.*[@#$%^&--_+=()])" +
                "(?=\\S+$).{8,20}";
        Pattern pattern = Pattern.compile(passwordRegex);
        if (password == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+" +
                "(\\.[a-zA-Z0-9_+&*-]+)*@" +
                "[a-zA-Z0-9-]+(\\.[a-zA-Z0-9]+)*(\\.[A-Za-z]{2,7})$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
