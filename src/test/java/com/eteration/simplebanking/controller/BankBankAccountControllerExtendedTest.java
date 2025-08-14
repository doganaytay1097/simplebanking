package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.dto.BillPaymentRequest;
import com.eteration.simplebanking.model.BillPaymentTransaction;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.services.BankAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BankBankAccountControllerExtendedTest {

    @Test
    void bill_payment_endpoint_happy_path() throws Exception {
        // Arrange
        BankAccountService service = mock(BankAccountService.class);
        BankAccountController controller = new BankAccountController(service);

        BillPaymentRequest req = new BillPaymentRequest();
        req.setPayee("Vodafone");
        req.setAmount(96.5);
        req.setPhoneNumber("5551234567"); // <-- null değil

        BillPaymentTransaction tx =
                new BillPaymentTransaction("Vodafone", 96.5, "5551234567");
        when(service.billPayment("X1", "Vodafone", 96.5, "5551234567"))
                .thenReturn(tx);

        // Act
        ResponseEntity<TransactionStatus> resp = controller.billPayment("X1", req);

        // Assert
        assertEquals("OK", resp.getBody().getStatus());
        assertNotNull(resp.getBody().getApprovalCode());
        assertEquals(tx.getApprovalCode(), resp.getBody().getApprovalCode());
        verify(service, times(1))
                .billPayment("X1", "Vodafone", 96.5, "5551234567");
        verifyNoMoreInteractions(service);
    }

    @Test
    void credit_and_debit_endpoints_flow() throws Exception {
        BankAccountService service = mock(BankAccountService.class);
        BankAccountController controller = new BankAccountController(service);

        var acc = new com.eteration.simplebanking.model.BankAccount("Kerem", "X2");
        when(service.findAccount("X2")).thenReturn(acc);

        ResponseEntity<TransactionStatus> c = controller.credit("X2", new DepositTransaction(1000));
        ResponseEntity<TransactionStatus> d = controller.debit("X2", new WithdrawalTransaction(50));

        assertEquals("OK", c.getBody().getStatus());
        assertEquals("OK", d.getBody().getStatus());
        assertEquals(950.0, acc.getBalance(), 1e-9);
        verify(service, times(2)).findAccount("X2");
        verifyNoMoreInteractions(service);
    }
}
