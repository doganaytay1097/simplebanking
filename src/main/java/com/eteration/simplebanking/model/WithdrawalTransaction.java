package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("WithdrawalTransaction")
public class WithdrawalTransaction extends Transaction {

    protected WithdrawalTransaction() { }
    public WithdrawalTransaction(double amount) { super(amount); }

    @Override
    public void apply(BankAccount bankAccount) throws InsufficientBalanceException {
        bankAccount.debit(getAmount());
    }
}
