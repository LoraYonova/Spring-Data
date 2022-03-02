package com.example.springdataintro;

import com.example.springdataintro.exceptions.InsufficientFoundsException;
import com.example.springdataintro.exceptions.UsernameAlreadyExistsException;
import com.example.springdataintro.services.AccountService;
import com.example.springdataintro.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ConsoleRunner implements CommandLineRunner {

   private final UserService userService;
   private final AccountService accountService;

    public ConsoleRunner(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }


    @Override
    public void run(String... args) throws Exception {

        try {

        this.userService.register
                ("Lora", 29, new BigDecimal(1000));
        } catch (UsernameAlreadyExistsException e) {
            System.out.println(e.getClass().getSimpleName());
        }

        this.userService.addAccount(new BigDecimal(500), 1L);

        try {
            this.accountService.withdrawMoney(new BigDecimal(200), 1L);
        } catch (InsufficientFoundsException e) {
            System.out.println(e.getClass().getSimpleName());
        }

        this.accountService.transferMoney(new BigDecimal(200), 2L);

    }
}
