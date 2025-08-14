package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("WithdrawalTransaction")
public class WithdrawalTransaction extends Transaction {
    public WithdrawalTransaction() { super(); }
    public WithdrawalTransaction(double amount) { super(amount); }
    @Override public void apply(BankAccount account) throws InsufficientBalanceException {
        account.withdraw(getAmount());
    }
}