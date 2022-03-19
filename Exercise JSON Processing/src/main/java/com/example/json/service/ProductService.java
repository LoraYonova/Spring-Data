package com.example.json.service;

import com.example.json.model.dto.CategoryStatsDTO;
import com.example.json.model.dto.ProductNamePriceAndSellerDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void seedProducts() throws IOException;

    List<ProductNamePriceAndSellerDTO> findAllProductsInRange(BigDecimal lower, BigDecimal upper);

    List<CategoryStatsDTO> findAllCategoriesWithProductsCount();
}
