package com.eteration.simplebanking.services;

import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    public Account create(String owner, String accountNumber) {
        return repo.save(new Account(owner, accountNumber));
    }

    // TESTLER bunu mock'luyor ve çağırıyor
    @Transactional(readOnly = true)
    public Account findAccount(String accountNumber) {
        return repo.findById(accountNumber).orElse(new Account("Unknown", accountNumber));
    }
}
