package com.example.advquerying.repositories;

import com.example.advquerying.entities.Shampoo;
import com.example.advquerying.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ShampooRepository extends JpaRepository<Shampoo, Long> {

    List<Shampoo> findAllBySizeOrderById(Size size);

    List<Shampoo> findBySizeOrLabelIdOrderByPriceAsc(Size size, Long id);

    List<Shampoo> findAllByPriceIsGreaterThanOrderByPriceDesc(BigDecimal price);

    int countByPriceLessThan(BigDecimal price);

    @Query("SELECT s FROM Shampoo s " +
            "JOIN s.ingredients i " +
            "WHERE i.name IN :ingredientName")
    List<Shampoo> findByIngredientsNames(Set<String> ingredientName);


    @Query("SELECT s FROM Shampoo s " +
            "WHERE size(s.ingredients) < :count")
    List<Shampoo> findByIngredientCountLessThan(int count);
}
