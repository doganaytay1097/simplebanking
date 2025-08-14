package com.eteration.simplebanking.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DepositTransaction")
public class DepositTransaction extends Transaction {

    public DepositTransaction() { super(); }
    public DepositTransaction(double amount) { super(amount); }
    @Override public void apply(BankAccount account) { account.deposit(getAmount()); }
}
