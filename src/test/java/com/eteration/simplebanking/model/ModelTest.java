package com.eteration.simplebanking.model;



import static org.junit.jupiter.api.Assertions.assertTrue;

import com.eteration.simplebanking.exception.InsufficientBalanceException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ModelTest {
	
	@Test
	public void testCreateAccountAndSetBalance0() {
		BankAccount bankAccount = new BankAccount("Kerem Karaca", "17892");
		assertTrue(bankAccount.getOwner().equals("Kerem Karaca"));
		assertTrue(bankAccount.getAccountNumber().equals("17892"));
		assertTrue(bankAccount.getBalance() == 0);
	}

	@Test
	public void testDepositIntoBankAccount() {
		BankAccount bankAccount = new BankAccount("Demet Demircan", "9834");
		bankAccount.deposit(100);
		assertTrue(bankAccount.getBalance() == 100);
	}

	@Test
	public void testWithdrawFromBankAccount() throws InsufficientBalanceException {
		BankAccount bankAccount = new BankAccount("Demet Demircan", "9834");
		bankAccount.deposit(100);
		assertTrue(bankAccount.getBalance() == 100);
		bankAccount.withdraw(50);
		assertTrue(bankAccount.getBalance() == 50);
	}

	@Test
	public void testWithdrawException() {
		Assertions.assertThrows( InsufficientBalanceException.class, () -> {
			BankAccount bankAccount = new BankAccount("Demet Demircan", "9834");
			bankAccount.deposit(100);
			bankAccount.withdraw(500);
		  });

	}
	
	@Test
	public void testTransactions() throws InsufficientBalanceException {
		// Create account
		BankAccount bankAccount = new BankAccount("Canan Kaya", "1234");
		assertTrue(bankAccount.getTransactions().size() == 0);

		// Deposit Transaction
		DepositTransaction depositTrx = new DepositTransaction(100);
		assertTrue(depositTrx.getDate() != null);
		bankAccount.post(depositTrx);
		assertTrue(bankAccount.getBalance() == 100);
		assertTrue(bankAccount.getTransactions().size() == 1);

		// Withdrawal Transaction
		WithdrawalTransaction withdrawalTrx = new WithdrawalTransaction(60);
		assertTrue(withdrawalTrx.getDate() != null);
		bankAccount.post(withdrawalTrx);
		assertTrue(bankAccount.getBalance() == 40);
		assertTrue(bankAccount.getTransactions().size() == 2);
	}
}
