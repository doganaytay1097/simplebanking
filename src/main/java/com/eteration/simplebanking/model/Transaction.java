package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;

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
    private BankAccount bankAccount;

    // 🔧 Jackson bu ctor'u kullanır; approvalCode ve date boş kalmasın diye burada üretiyoruz
    protected Transaction() {
        this.date = Instant.now();
        this.approvalCode = UUID.randomUUID().toString();
    }

    public Transaction(double amount) {
        this(amount, Instant.now(), UUID.randomUUID().toString());
    }

    // yardımcı (iç) ctor — test/başka ihtiyaçlar için
    protected Transaction(double amount, Instant date, String approvalCode) {
        this.amount = amount;
        this.date = date;
        this.approvalCode = approvalCode;
    }

    public abstract void apply(BankAccount bankAccount) throws InsufficientBalanceException;

    // ✅ Jackson için gerekli setter
    public void setAmount(double amount) { this.amount = amount; }

    // zaten vardı
    public Long getId() { return id; }
    public Instant getDate() { return date; }
    public double getAmount() { return amount; }
    public String getApprovalCode() { return approvalCode; }
    public BankAccount getAccount() { return bankAccount; }
    public void setAccount(BankAccount bankAccount) { this.bankAccount = bankAccount; }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{date=" + date +
                ", amount=" + amount +
                ", approvalCode='" + approvalCode + '\'' +
                '}';
    }
}
