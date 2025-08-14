package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static com.eteration.simplebanking.constants.ErrorMessages.*;

@Entity
@Table(name = "BANK_ACCOUNT")
public class BankAccount {

    @Id
    @Column(name = "ACCOUNT_NUMBER", length = 32, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private double balance = 0.0;

    @Column(nullable = false)
    private Instant createDate;

    @OneToMany(mappedBy = "BANK_ACCOUNT", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<Transaction> transactions = new ArrayList<>();

    protected BankAccount() { }

    public BankAccount(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.createDate = Instant.now();
    }


    public synchronized void post(Transaction transaction) throws InsufficientBalanceException {
        transaction.setBankAccount(this);
        transaction.apply(this);
        this.transactions.add(transaction);
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
