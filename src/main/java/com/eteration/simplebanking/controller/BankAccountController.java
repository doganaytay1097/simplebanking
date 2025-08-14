package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.dto.BillPaymentRequest;
import com.eteration.simplebanking.dto.TransactionResult;
import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.services.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Accounts", description = "Account retrieval and money operations")
@RestController
@RequestMapping("/account/v1")
@Validated

public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Operation(summary = "Get account by number")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<BankAccount> getAccount(@PathVariable String accountNumber) {
        BankAccount bankAccount = bankAccountService.findAccount(accountNumber);
        return ResponseEntity.ok(bankAccount);
    }

    @Operation(summary = "Create a bank account")
    @PostMapping("/create/{accountNumber}")
    public ResponseEntity<BankAccount> create(@PathVariable String accountNumber,
                                              @RequestParam String owner) {
        BankAccount createdBankAccount = bankAccountService.create(owner, accountNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBankAccount);
    }

    @Operation(summary = "Credit (deposit) money to account")
    @PostMapping("/credit/{accountNumber}")
    @Transactional
    public ResponseEntity<TransactionStatus> credit(@PathVariable String accountNumber,
                                                    @RequestBody DepositTransaction depositTransaction)
            throws InsufficientBalanceException {
        BankAccount bankAccount = bankAccountService.findAccount(accountNumber);
        bankAccount.post(depositTransaction);
        return ResponseEntity.ok(new TransactionStatus(TransactionResult.OK, depositTransaction.getApprovalCode()));
    }

    @Operation(summary = "Debit (withdraw) money from account")
    @PostMapping("/debit/{accountNumber}")
    @Transactional
    public ResponseEntity<TransactionStatus> debit(@PathVariable String accountNumber,
                                                   @RequestBody WithdrawalTransaction withdrawalTransaction)
            throws InsufficientBalanceException {
        BankAccount bankAccount = bankAccountService.findAccount(accountNumber);
        bankAccount.post(withdrawalTransaction);
        return ResponseEntity.ok(new TransactionStatus(TransactionResult.OK, withdrawalTransaction.getApprovalCode()));
    }

    @Operation(summary = "Pay a bill (BillPaymentTransaction)")
    @PostMapping("/bill-payment/{accountNumber}")
    @Transactional
    public ResponseEntity<TransactionStatus> billPayment(@PathVariable String accountNumber,
                                                         @Valid @RequestBody BillPaymentRequest billPaymentRequest)
            throws InsufficientBalanceException {

        Transaction transaction= bankAccountService.billPayment(
                accountNumber,
                billPaymentRequest.getPayee(),
                billPaymentRequest.getAmount(),
                billPaymentRequest.getPhoneNumber()
        );

        return ResponseEntity.ok(new TransactionStatus(TransactionResult.OK, transaction.getApprovalCode()));
    }

}





