package com.example.springdataintro.repositories;

import com.example.springdataintro.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
}
