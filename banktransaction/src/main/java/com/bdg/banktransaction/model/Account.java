package com.bdg.banktransaction.model;

import com.bdg.banktransaction.enums.Currency;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.stream.Stream;

/**
 * @author Tatevik Mirzoyan
 * Created on 22-Nov-20
 */
@Data
@NoArgsConstructor
@Entity(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, columnDefinition = "float default '0.0'")
    private float balance;
    @Column(nullable = false, columnDefinition = "varchar(5) default 'AMD'")
    private String currency;

    public Account(String currency) {
        setCurrency(currency);
    }

    public Account(float balance, String currency) {
        setBalance(balance);
        setCurrency(currency);
    }

    public void setCurrency(String currency) {
        Currency[] currencies = Currency.values();
        if (Stream.of(currencies).anyMatch(c -> c.name().equalsIgnoreCase(currency))) {
            this.currency = currency;
        } else try {
            throw new IllegalArgumentException("Invalid currency");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Created account with default currency AMD");
            this.currency = "AMD";
        }
    }

    public void setBalance(float balance) {
        if (balance >= 0) {
            this.balance = balance;
        } else try {
            throw new IllegalArgumentException("Invalid balance");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Created account with default balance 0");
            this.balance = 0;
        }
    }
}
