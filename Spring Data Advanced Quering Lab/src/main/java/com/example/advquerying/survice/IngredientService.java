package com.example.advquerying.survice;

import java.math.BigDecimal;
import java.util.List;

public interface IngredientService {
    List<String> findAllIngredientsNameStartsWithGivenLetters(String letter);

    List<String> findAllIngredientsWhichContainedGivenList(List<String> ingredients);


    void deleteIngredientName(String name);

    void increasePrice(double percent);

    void updateThePriceWithGivenName(String name);
}
