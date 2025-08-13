package com.eteration.simplebanking.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("WithdrawalTransaction")
public class WithdrawalTransaction extends Transaction {

    protected WithdrawalTransaction() { }
    public WithdrawalTransaction(double amount) { super(amount); }

    @Override
    public void apply(Account account) throws InsufficientBalanceException {
        account.debit(getAmount());
    }
}
