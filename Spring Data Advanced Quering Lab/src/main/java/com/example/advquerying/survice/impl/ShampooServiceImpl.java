package com.example.advquerying.survice.impl;

import com.example.advquerying.entities.Shampoo;
import com.example.advquerying.entities.Size;
import com.example.advquerying.repositories.ShampooRepository;
import com.example.advquerying.survice.ShampooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShampooServiceImpl implements ShampooService {
    private final ShampooRepository shampooRepository;

    @Autowired
    public ShampooServiceImpl(ShampooRepository shampooRepository) {
        this.shampooRepository = shampooRepository;
    }

    @Override
    public List<String> findAllShampooWithGivenSize(Size size) {
        return shampooRepository.findAllBySizeOrderById(size)
                .stream().map(shampoo -> String.format("%s %s %.2flv.",
                        shampoo.getBrand(),
                        shampoo.getSize(),
                        shampoo.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllShampoosWithGivenSizeAndLabelId(Size size, long labelId) {
        return shampooRepository.findBySizeOrLabelIdOrderByPriceAsc(size, labelId)
                .stream().map(shampoo -> String.format("%s %s %.2flv.",
                        shampoo.getBrand(),
                        shampoo.getSize(),
                        shampoo.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllShampoosWithHigherGivenPrice(BigDecimal price) {
        return shampooRepository.findAllByPriceIsGreaterThanOrderByPriceDesc(price)
                .stream().map(shampoo -> String.format("%s %s %.2flv.",
                        shampoo.getBrand(),
                        shampoo.getSize(),
                        shampoo.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public int findAllShampoosCountWithLowerThanGivenPrice(BigDecimal price) {
        return shampooRepository.countByPriceLessThan(price);
    }

    @Override
    public Set<String> findAllShampoosWithTheGivenIngredients(Set<String> ingredients) {
        return shampooRepository.findByIngredientsNames(ingredients)
                .stream().map(Shampoo::getBrand)
                .collect(Collectors.toSet());
    }

    @Override
    public List<String> findAllShampoosWithIngredientsLessThanGivenNumber(int ingredientsCount) {
        return shampooRepository.findByIngredientCountLessThan(ingredientsCount)
                .stream().map(Shampoo::getBrand)
                .collect(Collectors.toList());
    }
}
