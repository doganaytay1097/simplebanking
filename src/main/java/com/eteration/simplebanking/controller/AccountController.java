package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.InsufficientBalanceException;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Accounts", description = "Account retrieval and money operations")
@RestController
@RequestMapping("/account/v1")
public class AccountController {

    private final AccountService service;
    public AccountController(AccountService service) { this.service = service; }

    @Operation(summary = "Get account by number")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        Account account = service.findAccount(accountNumber);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Credit (deposit) money to account")
    @PostMapping("/credit/{accountNumber}")
    public ResponseEntity<TransactionStatus> credit(@PathVariable String accountNumber,
                                                    @RequestBody DepositTransaction trx)
            throws InsufficientBalanceException {
        Account account = service.findAccount(accountNumber);
        account.post(trx);
        return ResponseEntity.ok(new TransactionStatus("OK", trx.getApprovalCode()));
    }

    @Operation(summary = "Debit (withdraw) money from account")
    @PostMapping("/debit/{accountNumber}")
    public ResponseEntity<TransactionStatus> debit(@PathVariable String accountNumber,
                                                   @RequestBody WithdrawalTransaction trx)
            throws InsufficientBalanceException {
        Account account = service.findAccount(accountNumber);
        account.post(trx);
        return ResponseEntity.ok(new TransactionStatus("OK", trx.getApprovalCode()));
    }
}
