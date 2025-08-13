package com.eteration.simplebanking.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "account_number", nullable = false, length = 32)
    private String accountNumber;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private double balance = 0.0;

    @Column(nullable = false)
    private Instant createDate;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    protected Account() { }

    public Account(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.createDate = Instant.now();
    }

    // BONUS: polimorfik işle
    public synchronized void post(Transaction tx) throws InsufficientBalanceException {
        tx.setAccount(this);
        tx.apply(this);
        this.transactions.add(tx);
    }

    // Testler bunları doğrudan çağırıyor
    public void deposit(double amount) {
        if (amount <= 0) return;
        this.balance += amount;
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount <= 0) throw new InsufficientBalanceException("Amount must be positive");
        if (this.balance < amount) throw new InsufficientBalanceException("Insufficient balance");
        this.balance -= amount;
    }

    // Polimorfik çağrılar için iç metotlar
    public void credit(double amount) { deposit(amount); }
    public void debit(double amount) throws InsufficientBalanceException { withdraw(amount); }

    // getters
    public String getAccountNumber() { return accountNumber; }
    public String getOwner() { return owner; }
    public double getBalance() { return balance; }
    public Instant getCreateDate() { return createDate; }
    public List<Transaction> getTransactions() { return transactions; }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    public void setOwner(String owner) { this.owner = owner; }
}
