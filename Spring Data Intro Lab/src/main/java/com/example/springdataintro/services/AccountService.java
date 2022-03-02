package com.example.springdataintro.services;

import com.example.springdataintro.exceptions.InsufficientFoundsException;

import java.math.BigDecimal;

public interface AccountService {

    void withdrawMoney(BigDecimal amount, Long id) throws InsufficientFoundsException;

    void transferMoney(BigDecimal amount, Long id);
}
