package com.eteration.simplebanking.services;

import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.eteration.simplebanking.constants.ErrorMessages.*;

@Service
@Transactional
public class BankAccountService {

    private final AccountRepository repo;
    public BankAccountService(AccountRepository repo) { this.repo = repo; }

    public BankAccount create(String owner, String accountNumber) {
        return repo.save(new BankAccount(owner, accountNumber));
    }


    @Transactional(readOnly = true)
    public BankAccount findAccount(String accountNumber) {
        return repo.findById(accountNumber).orElseThrow(() -> new IllegalArgumentException(ACCOUNT_NOT_FOUND));
    }


    public Transaction billPayment(String accountNumber, String payee, double amount)
            throws InsufficientBalanceException {
        BankAccount bankAccount = findAccount(accountNumber);
        BillPaymentTransaction billPaymentTransaction = new BillPaymentTransaction(payee, amount);
        bankAccount.post(billPaymentTransaction);
        repo.save(bankAccount);
        return billPaymentTransaction;
    }
}
