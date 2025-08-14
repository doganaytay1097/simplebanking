package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.dto.BillPaymentRequest;
import com.eteration.simplebanking.dto.TransactionResult;
import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.services.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Accounts", description = "Account retrieval and money operations")
@RestController
@RequestMapping("/account/v1")
@Validated

public class BankAccountController {

    private final BankAccountService service;

    public BankAccountController(BankAccountService service) {
        this.service = service;
    }

    @Operation(summary = "Get account by number")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<BankAccount> getAccount(@PathVariable String accountNumber) {
        BankAccount bankAccount = service.findAccount(accountNumber);
        return ResponseEntity.ok(bankAccount);
    }

    @Operation(summary = "Credit (deposit) money to account")
    @PostMapping("/credit/{accountNumber}")
    public ResponseEntity<TransactionStatus> credit(@PathVariable String accountNumber,
                                                    @RequestBody DepositTransaction depositTransaction)
            throws InsufficientBalanceException {
        BankAccount bankAccount = service.findAccount(accountNumber);
        bankAccount.post(depositTransaction);
        return ResponseEntity.ok(new TransactionStatus(TransactionResult.OK, depositTransaction.getApprovalCode()));
    }

    @Operation(summary = "Debit (withdraw) money from account")
    @PostMapping("/debit/{accountNumber}")
    public ResponseEntity<TransactionStatus> debit(@PathVariable String accountNumber,
                                                   @RequestBody WithdrawalTransaction withdrawalTransaction)
            throws InsufficientBalanceException {
        BankAccount bankAccount = service.findAccount(accountNumber);
        bankAccount.post(withdrawalTransaction);
        return ResponseEntity.ok(new TransactionStatus(TransactionResult.OK, withdrawalTransaction.getApprovalCode()));
    }

    @Operation(summary = "Pay a bill (BillPaymentTransaction)")
    @PostMapping("/bill-payment/{accountNumber}")
    public ResponseEntity<TransactionStatus> billPayment(@PathVariable String accountNumber,
                                                         @Valid @RequestBody BillPaymentRequest billPaymentRequest)
            throws InsufficientBalanceException {
        BankAccount bankAccount = service.findAccount(accountNumber);
        BillPaymentTransaction billPaymentTransaction =
                new BillPaymentTransaction(billPaymentRequest.getPayee(), billPaymentRequest.getAmount());
        bankAccount.post(billPaymentTransaction);
        return ResponseEntity.ok(new TransactionStatus(TransactionResult.OK, billPaymentTransaction.getApprovalCode()));
    }

    @PostMapping("/create/{accountNumber}")
    public ResponseEntity<BankAccount> create(@PathVariable String accountNumber,
                                              @RequestParam String owner) {
        return ResponseEntity.ok(service.create(owner, accountNumber));
    }
}
