package com.eteration.simplebanking.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

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

    protected Account() { }

    public Account(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.createDate = Instant.now();
    }

    // BONUS: polimorfik delege
    public synchronized void post(Transaction tx) throws InsufficientBalanceException {
        tx.setAccount(this);
        tx.apply(this);
        this.transactions.add(tx);
    }

    // UML'deki isimler: credit/debit
    public void credit(double amount) {
        if (amount <= 0) return;
        this.balance += amount;
    }

    public void debit(double amount) throws InsufficientBalanceException {
        if (amount <= 0) throw new InsufficientBalanceException("Amount must be positive");
        if (this.balance < amount) throw new InsufficientBalanceException("Insufficient balance");
        this.balance -= amount;
    }

    // Testlerde kullanılan isimler (köprü)
    public void deposit(double amount) { credit(amount); }
    public void withdraw(double amount) throws InsufficientBalanceException { debit(amount); }

    public String getAccountNumber() { return accountNumber; }
    public String getOwner() { return owner; }
    public double getBalance() { return balance; }
    public Instant getCreateDate() { return createDate; }
    public List<Transaction> getTransactions() { return transactions; }
    public void setOwner(String owner) { this.owner = owner; }
}
