package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountModelExtendedTest {

    @Test
    void createAccount_defaults() {
        BankAccount a = new BankAccount("Kerem Karaca", "A1");
        assertEquals("Kerem Karaca", a.getOwner());
        assertEquals("A1", a.getAccountNumber());
        assertEquals(0.0, a.getBalance(), 1e-9);
        assertNotNull(a.getCreateDate());
    }

    @Test
    void deposit_positive_increases_balance() {
        BankAccount a = new BankAccount("X", "A2");
        a.deposit(100);
        assertEquals(100, a.getBalance(), 1e-9);
    }

    @Test
    void deposit_zero_no_change() {
        BankAccount a = new BankAccount("X", "A3");
        a.deposit(0);
        assertEquals(0, a.getBalance(), 1e-9);
    }

    @Test
    void withdraw_exact_balance() throws InsufficientBalanceException {
        BankAccount a = new BankAccount("X", "A4");
        a.deposit(50);
        a.withdraw(50);
        assertEquals(0, a.getBalance(), 1e-9);
    }

    @Test
    void withdraw_more_than_balance_throws() {
        BankAccount a = new BankAccount("X", "A5");
        a.deposit(10);
        assertThrows(InsufficientBalanceException.class, () -> a.withdraw(11));
    }

    @Test
    void withdraw_negative_throws() {
        BankAccount a = new BankAccount("X", "A6");
        assertThrows(InsufficientBalanceException.class, () -> a.withdraw(-1));
    }

    @Test
    void post_deposit_and_withdrawal_polymorphism() throws Exception {
        BankAccount a = new BankAccount("X", "A7");
        a.post(new DepositTransaction(1000));
        a.post(new WithdrawalTransaction(200));
        assertEquals(800, a.getBalance(), 1e-9);
        assertEquals(2, a.getTransactions().size());
    }

    @Test
    void post_bill_payment_polymorphism() throws Exception {
        BankAccount a = new BankAccount("X", "A8");
        a.deposit(200);
        a.post(new BillPaymentTransaction("Vodafone", 96.50,"09585985441"));
        assertEquals(103.5, a.getBalance(), 1e-9);
        assertEquals(1, a.getTransactions().stream().filter(t -> t instanceof BillPaymentTransaction).count());
    }

    @Test
    void transaction_generates_date_and_approvalCode() throws Exception {
        DepositTransaction t = new DepositTransaction(12.3);
        assertNotNull(t.getDate());
        assertNotNull(t.getApprovalCode());
        assertTrue(t.toString().contains("amount=12.3"));
    }

    @Test
    void complex_sequence_balance() throws Exception {
        BankAccount a = new BankAccount("X", "A9");
        a.post(new DepositTransaction(1000));
        a.post(new WithdrawalTransaction(200));
        a.post(new BillPaymentTransaction("Payee", 50,"095697848152"));
        a.post(new DepositTransaction(25));
        assertEquals(775, a.getBalance(), 1e-9);
        assertEquals(4, a.getTransactions().size());
    }
}
