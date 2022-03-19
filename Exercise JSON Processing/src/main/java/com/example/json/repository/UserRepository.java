package com.example.json.repository;

import com.example.json.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u " +
            "WHERE (SELECT count (p) FROM Product p WHERE p.buyer IS NOT NULL AND p.seller.id = u.id) > 0 " +
            "ORDER BY u.lastName, u.firstName ")
    List<User> findAllUsersWithMoreThanOneSoldProductsOrderByLastNameAndFirstName();


}
