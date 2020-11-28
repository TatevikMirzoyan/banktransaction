package com.bdg.banktransaction.model;

import com.bdg.banktransaction.enums.TransactionStatus;
import com.bdg.banktransaction.enums.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Tatevik Mirzoyan
 * Created on 22-Nov-20
 */
@Data
@NoArgsConstructor
@Entity(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime dateTime;
    @OneToOne
    private Account account;
    private float amount;
    private String type;  //TransactionType
    @Column(columnDefinition = "varchar(20) default 'PENDING'")
    private String status = "PENDING";  //TransactionStatus

    public Transaction(Account account, float amount, String type) {
        this.dateTime = LocalDateTime.now();
        this.account = account;
        this.amount = amount;
        setType(type);
    }

    public void setType(String type) {
        if (type.equalsIgnoreCase(String.valueOf(TransactionType.DEPOSIT))) {
            this.type = TransactionType.DEPOSIT.name();
        }
        if (type.equalsIgnoreCase(String.valueOf(TransactionType.WITHDRAWAL))) {
            this.type = TransactionType.WITHDRAWAL.name();
        }
    }

    public void setStatus(TransactionStatus status) {
        this.status = String.valueOf(status);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", account=" + account.getId() +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
