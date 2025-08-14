package com.eteration.simplebanking;

import com.eteration.simplebanking.controller.BankAccountController;
import com.eteration.simplebanking.controller.TransactionStatus;
import com.eteration.simplebanking.dto.BillPaymentRequest;
import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.services.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BankBankAccountControllerExtendedTest {

    @Test
    void bill_payment_endpoint_happy_path() throws Exception {
        BankAccountService service = mock(BankAccountService.class);
        BankAccountController controller = new BankAccountController(service);

        BankAccount acc = new BankAccount("Kerem", "X1");
        acc.deposit(200);
        when(service.findAccount("X1")).thenReturn(acc);

        BillPaymentRequest req = new BillPaymentRequest();
        req.setPayee("Vodafone");
        req.setAmount(96.5);

        ResponseEntity<TransactionStatus> resp = controller.billPayment("X1", req);

        assertEquals("OK", resp.getBody().getStatus());
        assertEquals(103.5, acc.getBalance(), 1e-9); // 200 - 96.5
        verify(service, times(1)).findAccount("X1");
        assertEquals(96.5, acc.getTransactions().get(0).getAmount(), 1e-9);
    }

    @Test
    void credit_and_debit_endpoints_flow() throws Exception {
        BankAccountService service = mock(BankAccountService.class);
        BankAccountController controller = new BankAccountController(service);

        BankAccount acc = new BankAccount("Kerem", "X2");
        when(service.findAccount("X2")).thenReturn(acc);

        ResponseEntity<TransactionStatus> c = controller.credit("X2", new DepositTransaction(1000));
        ResponseEntity<TransactionStatus> d = controller.debit("X2", new WithdrawalTransaction(50));

        assertEquals("OK", c.getBody().getStatus());
        assertEquals("OK", d.getBody().getStatus());
        assertEquals(950.0, acc.getBalance(), 1e-9);
        verify(service, times(2)).findAccount("X2");
    }
}
