package com.example.advquerying.survice;

import com.example.advquerying.entities.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ShampooService {
    List<String> findAllShampooWithGivenSize(Size size);

    List<String> findAllShampoosWithGivenSizeAndLabelId(Size size, long labelId);

    List<String> findAllShampoosWithHigherGivenPrice(BigDecimal price);


    int findAllShampoosCountWithLowerThanGivenPrice(BigDecimal price);

    Set<String> findAllShampoosWithTheGivenIngredients(Set<String> ingredients);

    List<String> findAllShampoosWithIngredientsLessThanGivenNumber(int ingredientsCount);
}
