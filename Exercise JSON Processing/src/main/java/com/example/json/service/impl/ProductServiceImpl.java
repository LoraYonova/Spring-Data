package com.example.json.service.impl;

import com.example.json.model.dto.CategoryStatsDTO;
import com.example.json.model.dto.ProductNamePriceAndSellerDTO;
import com.example.json.model.dto.ProductSeedDTO;
import com.example.json.model.entity.Product;
import com.example.json.repository.ProductRepository;
import com.example.json.service.CategoryService;
import com.example.json.service.ProductService;
import com.example.json.service.UserService;
import com.example.json.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.json.constants.GlobalConstants.RESOURCE_FILE_PATH;

@Service
public class ProductServiceImpl implements ProductService {

    public static final String PRODUCTS_FILE_PATH = "products.json";

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final CategoryService categoryService;


    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.userService = userService;

        this.categoryService = categoryService;
    }

    @Override
    public void seedProducts() throws IOException {

        if (productRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCE_FILE_PATH + PRODUCTS_FILE_PATH));

        ProductSeedDTO[] productSeedDTOS = gson.fromJson(fileContent, ProductSeedDTO[].class);

        Arrays.stream(productSeedDTOS).filter(validationUtil::isValid)
                .map(productSeedDTO -> {
                    Product product = modelMapper.map(productSeedDTO, Product.class);
                    product.setSeller(userService.findRandomUser());

                    if (product.getPrice().compareTo(BigDecimal.valueOf(900L)) > 0) {
                        product.setBuyer(userService.findRandomUser());
                    }

                    product.setCategories(categoryService.findRandomCategories());

                    return product;
                })

                .forEach(productRepository::save);


    }

    @Override
    public List<CategoryStatsDTO> findAllCategoriesWithProductsCount() {
        return productRepository.findAllCategoryWithProductsCount();
    }


    @Override
    public List<ProductNamePriceAndSellerDTO> findAllProductsInRange(BigDecimal lower, BigDecimal upper) {

        return productRepository.findAllByPriceBetweenAndBuyerIsNullOrderByPrice(lower, upper)
                .stream().map(product -> {
                    ProductNamePriceAndSellerDTO productNameAndPriceDTO = modelMapper
                            .map(product, ProductNamePriceAndSellerDTO.class);

                    productNameAndPriceDTO.setSeller(String.format("%s %s",
                            product.getSeller().getFirstName(),
                            product.getSeller().getLastName()));

                    return productNameAndPriceDTO;
                })
                .collect(Collectors.toList());

    }
}
