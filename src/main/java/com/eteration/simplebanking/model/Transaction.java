package com.eteration.simplebanking.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant date;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false, length = 64)
    private String approvalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number")
    private Account account;

    protected Transaction() { }

    public Transaction(double amount) {
        this.amount = amount;
        this.date = Instant.now();
        this.approvalCode = UUID.randomUUID().toString();
    }

    public abstract void apply(Account account) throws InsufficientBalanceException;

    // getters/setters
    public Long getId() { return id; }
    public Instant getDate() { return date; }
    public double getAmount() { return amount; }
    public String getApprovalCode() { return approvalCode; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}
