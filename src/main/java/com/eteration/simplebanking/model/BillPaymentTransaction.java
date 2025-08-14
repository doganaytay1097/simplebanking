// src/main/java/com/eteration/simplebanking/model/BillPaymentTransaction.java
package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BillPaymentTransaction")
public class BillPaymentTransaction extends Transaction {


    @Column
    private String payee;


    @Column(length = 20)
    private String phoneNumber;

    protected BillPaymentTransaction() { super(); }

    public BillPaymentTransaction(String payee, double amount, String phoneNumber) {
        super(amount);
        this.payee = payee;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void apply(BankAccount bankAccount) throws InsufficientBalanceException {
        bankAccount.debit(getAmount());
    }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPayee() { return payee; }
    public void setPayee(String payee) { this.payee = payee; }

    @Override
    public String toString() {
        return super.toString().replace("}", ", payee='" + payee + "'}");
    }
}
