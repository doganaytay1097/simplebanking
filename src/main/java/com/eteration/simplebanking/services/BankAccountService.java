package com.eteration.simplebanking.services;

import com.eteration.simplebanking.exception.AccountAlreadyExistsException;
import com.eteration.simplebanking.exception.AccountNotFoundException;
import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.eteration.simplebanking.constants.ErrorMessages.*;

@Service
@Transactional
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    public BankAccountService(BankAccountRepository bankAccountRepository) { this.bankAccountRepository = bankAccountRepository; }


    public BankAccount create(String owner, String accountNumber) {
        if (bankAccountRepository.existsById(accountNumber)) {
            throw new AccountAlreadyExistsException(ACCOUNT_ALREADY_EXISTS);
        }
        return bankAccountRepository.save(new BankAccount(owner, accountNumber));
    }


    @Transactional(readOnly = true)
    public BankAccount findAccount(String accountNumber) {
        return bankAccountRepository.findById(accountNumber).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));
    }


    public Transaction billPayment(String accountNumber, String payee, double amount, String phoneNumber)
            throws InsufficientBalanceException {
        BankAccount bankAccount = findAccount(accountNumber);
        BillPaymentTransaction transaction = new BillPaymentTransaction(payee, amount, phoneNumber);
        bankAccount.post(transaction );
        bankAccountRepository.save(bankAccount);
        return transaction ;
    }

}


