package com.example.json.service;

import com.example.json.model.dto.CategoryStatsDTO;
import com.example.json.model.entity.Category;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    Set<Category> findRandomCategories();


}
