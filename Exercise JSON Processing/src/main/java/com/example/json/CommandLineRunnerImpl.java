package com.example.json;

import com.example.json.model.dto.CategoryStatsDTO;
import com.example.json.model.dto.ProductNamePriceAndSellerDTO;
import com.example.json.model.dto.UserSoldDTO;
import com.example.json.service.CategoryService;
import com.example.json.service.ProductService;
import com.example.json.service.UserService;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final String OUT_FILE_PATH = "src/main/resources/files/out/";
    private static final String PRODUCTS_IN_RANGE_FILE_NAME = "products-in-range.json";
    private static final String USERS_SOLD_PRODUCTS = "users-sold-products.json";
    private static final String CATEGORIES_PRODUCT_STATS = "categories-product-stats.json";
    public static final String USERS_AND_PRODUCTS = "users-and-products.json";

    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;
    private final BufferedReader bufferedReader;
    private final Gson gson;

    public CommandLineRunnerImpl(CategoryService categoryService, ProductService productService, UserService userService, Gson gson) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
        this.gson = gson;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }


    @Override
    public void run(String... args) throws Exception {

        seedData();

        System.out.println("Enter exercise:");
        int exerciseNumber = Integer.parseInt(bufferedReader.readLine());

        switch (exerciseNumber) {
            case 1 -> productsInRange();
            case 2 -> successfullySoldProducts();
            case 3 -> categoryByproductsCount();

        }

    }




    private void categoryByproductsCount() throws IOException {

        List<CategoryStatsDTO> categoryStatsDTOS = productService.findAllCategoriesWithProductsCount();
        String content = gson.toJson(categoryStatsDTOS);
        writeToFile(OUT_FILE_PATH + CATEGORIES_PRODUCT_STATS, content);
    }

    private void successfullySoldProducts() throws IOException {
        List<UserSoldDTO> userSoldProductsDto = userService.findAllUsersWithMoreThanOneSoldProducts();

        String content = gson.toJson(userSoldProductsDto);
        writeToFile(OUT_FILE_PATH + USERS_SOLD_PRODUCTS, content);

    }

    private void productsInRange() throws IOException {
        List<ProductNamePriceAndSellerDTO> productDto = productService.findAllProductsInRange(BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L));

        String content = gson.toJson(productDto);
        writeToFile(OUT_FILE_PATH + PRODUCTS_IN_RANGE_FILE_NAME, content);
    }

    private void writeToFile(String filePath, String content) throws IOException {
        Files.write(Path.of(filePath), Collections.singleton(content));
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        userService.seedUsers();
        productService.seedProducts();


    }
}
