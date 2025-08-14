package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static com.eteration.simplebanking.constants.ErrorMessages.*;

@Entity
@Table(name = "bank_account")
@Getter
@Setter
public class BankAccount {

    @Id
    @Column(name = "account_number", length = 32, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private double balance = 0.0;

    @Column(nullable = false)
    private Instant createDate;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Transaction> transactions = new ArrayList<>();

    protected BankAccount() { }

    public BankAccount(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.createDate = Instant.now();
    }


    public synchronized void post(Transaction tx) throws InsufficientBalanceException {
        tx.setAccount(this);
        tx.apply(this);
        this.transactions.add(tx);
    }


    public void credit(double amount) {
        if (amount <= 0) return;
        this.balance += amount;
    }

    public void debit(double amount) throws InsufficientBalanceException {
        if (amount <= 0) throw new InsufficientBalanceException(AMOUNT_MUST_BE_POSITIVE);
        if (this.balance < amount) throw new InsufficientBalanceException(INSUFFICIENT_BALANCE);
        this.balance -= amount;
    }


    public void deposit(double amount) { credit(amount); }
    public void withdraw(double amount) throws InsufficientBalanceException { debit(amount); }

}
