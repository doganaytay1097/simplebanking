package com.eteration.simplebanking.repository;

import com.eteration.simplebanking.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> { }
