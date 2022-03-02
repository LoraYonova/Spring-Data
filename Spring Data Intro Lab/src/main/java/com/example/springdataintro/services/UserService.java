package com.example.springdataintro.services;

import com.example.springdataintro.exceptions.UserNotFoundException;
import com.example.springdataintro.exceptions.UsernameAlreadyExistsException;

import java.math.BigDecimal;

public interface UserService {

    void register(String username, int age, BigDecimal initialAmount)
            throws UsernameAlreadyExistsException;

    void addAccount(BigDecimal amount, Long id) throws UserNotFoundException;
}
