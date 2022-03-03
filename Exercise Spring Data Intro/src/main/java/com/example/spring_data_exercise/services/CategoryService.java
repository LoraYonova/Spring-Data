package com.example.spring_data_exercise.services;

import com.example.spring_data_exercise.models.entity.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Category> getRandomCategories();
}
