package com.example.springdataintro.services;

import com.example.springdataintro.exceptions.InsufficientFoundsException;
import com.example.springdataintro.models.Account;
import com.example.springdataintro.repositories.AccountRepository;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;


    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void withdrawMoney(BigDecimal amount, Long id) throws InsufficientFoundsException {

        Account account = this.accountRepository.findById(id).orElseThrow();

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFoundsException();
        }

        account.setBalance(account.getBalance().subtract(amount));
        this.accountRepository.save(account);
    }

    @Override
    public void transferMoney(BigDecimal amount, Long id) {

        Account account = this.accountRepository.findById(id).orElseThrow();

        account.setBalance(account.getBalance().add(amount));
        this.accountRepository.save(account);


    }
}
