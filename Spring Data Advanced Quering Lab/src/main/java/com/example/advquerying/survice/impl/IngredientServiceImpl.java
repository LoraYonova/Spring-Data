package com.example.advquerying.survice.impl;

import com.example.advquerying.entities.Ingredient;
import com.example.advquerying.repositories.IngredientRepository;
import com.example.advquerying.survice.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {
   private IngredientRepository ingredientRepository;

   @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<String> findAllIngredientsNameStartsWithGivenLetters(String letter) {
        return ingredientRepository.findAllByNameIsStartingWith(letter).stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllIngredientsWhichContainedGivenList(List<String> ingredients) {
        return ingredientRepository.findAllByNameInOrderByPriceAsc(ingredients)
                .stream().map(Ingredient::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteIngredientName(String name) {
        ingredientRepository.deleteIngredientByName(name);
    }

    @Override
    public void increasePrice(double percent) {
       BigDecimal actualPercent = BigDecimal.valueOf(percent);
        ingredientRepository.increasePriceByPercent(actualPercent);
    }

    @Override
    public void updateThePriceWithGivenName(String name) {
        ingredientRepository.increasePriceByName(name);
    }


}
