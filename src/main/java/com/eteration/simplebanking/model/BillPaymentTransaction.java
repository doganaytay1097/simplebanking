package com.eteration.simplebanking.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BillPaymentTransaction")
public class BillPaymentTransaction extends Transaction {

    // UML: - payee
    @Column(nullable = false)
    private String payee;

    protected BillPaymentTransaction() {
    }

    public BillPaymentTransaction(String payee, double amount) {
        super(amount);
        this.payee = payee;
    }

    @Override
    public void apply(Account account) throws InsufficientBalanceException {
        account.debit(getAmount());
    }

    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }

    @Override
    public String toString() {
        return super.toString().replace("}", ", payee='" + payee + "'}");
    }
}
