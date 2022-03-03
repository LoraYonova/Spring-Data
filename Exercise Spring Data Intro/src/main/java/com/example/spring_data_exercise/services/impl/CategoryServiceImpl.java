package com.example.spring_data_exercise.services.impl;

import com.example.spring_data_exercise.models.entity.Category;
import com.example.spring_data_exercise.repository.CategoryRepository;
import com.example.spring_data_exercise.services.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final String CATEGORIES_FILE_PATH = "src/main/resources/files/categories.txt";

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedCategories() throws IOException {

        if (categoryRepository.count() > 0) {
            return;
        }

        Files.readAllLines(Path.of(CATEGORIES_FILE_PATH))
                .stream()
                .filter(c -> !c.isEmpty())
                .forEach(categoryName -> {

                    Category category = new Category(categoryName);
            categoryRepository.save(category);
        });
    }

    @Override
    public Set<Category> getRandomCategories() {

        Set<Category> categories = new HashSet<>();
        int randomInt = ThreadLocalRandom.current().nextInt(1, 3);

        long categoryRepositoryCount = categoryRepository.count();

        for (int i = 0; i < randomInt; i++) {
            long randomId = ThreadLocalRandom.current().nextLong(1, categoryRepositoryCount + 1);
            Category category = categoryRepository.findById(randomId).orElse(null);
            categories.add(category);
        }
            return categories;
    }
}
