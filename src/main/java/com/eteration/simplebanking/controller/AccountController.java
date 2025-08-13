package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.InsufficientBalanceException;
import com.eteration.simplebanking.model.Transaction;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account/v1")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    // Testler doğrudan bu metodu çağırıyor
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        Account account = service.findAccount(accountNumber);
        return ResponseEntity.ok(account);
    }

    // Testler doğrudan bu imza ile çağırıyor
    @PostMapping("/credit/{accountNumber}")
    public ResponseEntity<TransactionStatus> credit(@PathVariable String accountNumber,
                                                    @RequestBody DepositTransaction trx)
            throws InsufficientBalanceException {
        Account account = service.findAccount(accountNumber);
        account.post(trx); // polimorfik uygula
        TransactionStatus status = new TransactionStatus("OK", trx.getApprovalCode());
        return ResponseEntity.ok(status);
    }

    @PostMapping("/debit/{accountNumber}")
    public ResponseEntity<TransactionStatus> debit(@PathVariable String accountNumber,
                                                   @RequestBody WithdrawalTransaction trx)
            throws InsufficientBalanceException {
        Account account = service.findAccount(accountNumber);
        account.post(trx);
        TransactionStatus status = new TransactionStatus("OK", trx.getApprovalCode());
        return ResponseEntity.ok(status);
    }
}
