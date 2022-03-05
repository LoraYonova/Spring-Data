package com.example.advquerying.repositories;

import com.example.advquerying.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findAllByNameIsStartingWith(String name);

    List<Ingredient> findAllByNameInOrderByPriceAsc(List<String> name);

    @Transactional
    void deleteIngredientByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Ingredient i " +
            "SET i.price = i.price + (i.price * :percent)")
    void increasePriceByPercent(BigDecimal percent);

    @Modifying
    @Transactional
    @Query("UPDATE Ingredient i " +
            "SET i.price = i.price + (i.price * 0.10) " +
            "WHERE i.name = :name")
    void increasePriceByName(String name);
}
