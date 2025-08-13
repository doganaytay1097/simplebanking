package com.eteration.simplebanking;

import com.eteration.simplebanking.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountModelExtendedTest {

    @Test
    void createAccount_defaults() {
        Account a = new Account("Kerem Karaca", "A1");
        assertEquals("Kerem Karaca", a.getOwner());
        assertEquals("A1", a.getAccountNumber());
        assertEquals(0.0, a.getBalance(), 1e-9);
        assertNotNull(a.getCreateDate());
    }

    @Test
    void deposit_positive_increases_balance() {
        Account a = new Account("X", "A2");
        a.deposit(100);
        assertEquals(100, a.getBalance(), 1e-9);
    }

    @Test
    void deposit_zero_no_change() {
        Account a = new Account("X", "A3");
        a.deposit(0);
        assertEquals(0, a.getBalance(), 1e-9);
    }

    @Test
    void withdraw_exact_balance() throws InsufficientBalanceException {
        Account a = new Account("X", "A4");
        a.deposit(50);
        a.withdraw(50);
        assertEquals(0, a.getBalance(), 1e-9);
    }

    @Test
    void withdraw_more_than_balance_throws() {
        Account a = new Account("X", "A5");
        a.deposit(10);
        assertThrows(InsufficientBalanceException.class, () -> a.withdraw(11));
    }

    @Test
    void withdraw_negative_throws() {
        Account a = new Account("X", "A6");
        assertThrows(InsufficientBalanceException.class, () -> a.withdraw(-1));
    }

    @Test
    void post_deposit_and_withdrawal_polymorphism() throws Exception {
        Account a = new Account("X", "A7");
        a.post(new DepositTransaction(1000));
        a.post(new WithdrawalTransaction(200));
        assertEquals(800, a.getBalance(), 1e-9);
        assertEquals(2, a.getTransactions().size());
    }

    @Test
    void post_bill_payment_polymorphism() throws Exception {
        Account a = new Account("X", "A8");
        a.deposit(200);
        a.post(new BillPaymentTransaction("Vodafone", 96.50));
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
        Account a = new Account("X", "A9");
        a.post(new DepositTransaction(1000));
        a.post(new WithdrawalTransaction(200));
        a.post(new BillPaymentTransaction("Payee", 50));
        a.post(new DepositTransaction(25));
        assertEquals(775, a.getBalance(), 1e-9);
        assertEquals(4, a.getTransactions().size());
    }
}
