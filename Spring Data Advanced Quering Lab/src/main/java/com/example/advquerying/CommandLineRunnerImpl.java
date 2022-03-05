package com.example.advquerying;

import com.example.advquerying.entities.Size;
import com.example.advquerying.survice.IngredientService;
import com.example.advquerying.survice.ShampooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final BufferedReader bufferedReader;
    private final ShampooService shampooService;
    private final IngredientService ingredientService;

    @Autowired
    public CommandLineRunnerImpl(BufferedReader bufferedReader, ShampooService shampooService, IngredientService ingredientService) {
        this.bufferedReader = bufferedReader;
        this.shampooService = shampooService;
        this.ingredientService = ingredientService;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Enter the task number:");
        int exNumber = Integer.parseInt(bufferedReader.readLine());

        switch (exNumber) {

            case 1 -> selectShampooBySize();
            case 2 -> selectShampoosBySizeOrLabel();
            case 3 -> selectShampoosByPrice();
            case 4 -> selectIngredientsByName();
            case 5 -> selectIngredientsByNames();
            case 6 -> countShampoosByPrice();
            case 7 -> selectShampoosByIngredients();
            case 8 -> selectShampoosByIngredientsCount();
            case 9 -> deleteIngredientsByName();
            case 10 -> updateIngredientsByPrice();
            case 11 -> updateIngredientsByName();
        }

    }

    private void updateIngredientsByName() throws IOException {
        System.out.println("Enter ingredient name:");
        String name = bufferedReader.readLine();

        ingredientService.updateThePriceWithGivenName(name);

    }

    private void updateIngredientsByPrice() {
        double percent = 0.10;
        ingredientService.increasePrice(percent);
    }

    private void deleteIngredientsByName() throws IOException {
        System.out.println("Enter ingredient name:");
        String name = bufferedReader.readLine();

        ingredientService.deleteIngredientName(name);
    }

    private void selectShampoosByIngredientsCount() throws IOException {
        System.out.println("Enter number:");
        int ingredientsCount = Integer.parseInt(bufferedReader.readLine());

        shampooService.findAllShampoosWithIngredientsLessThanGivenNumber(ingredientsCount)
                .forEach(System.out::println);

    }

    private void selectShampoosByIngredients() {
        System.out.println("Enter ingredients:");
        List<String> inputIngredients = bufferedReader.lines().limit(2).toList();
        Set<String> ingredients = new HashSet<>(inputIngredients);

        shampooService.findAllShampoosWithTheGivenIngredients(ingredients)
                .forEach(System.out::println);

    }

    private void countShampoosByPrice() throws IOException {
        System.out.println("Enter price:");
        double price = Double.parseDouble(bufferedReader.readLine());

        System.out.println(shampooService.findAllShampoosCountWithLowerThanGivenPrice(BigDecimal.valueOf(price)));

    }

    private void selectIngredientsByNames() throws IOException {
        System.out.println("Enter ingredients:");
        List<String> ingredients = bufferedReader.lines().limit(3).toList();
        ingredientService.findAllIngredientsWhichContainedGivenList(ingredients)
                .forEach(System.out::println);
    }

    private void selectIngredientsByName() throws IOException {
        System.out.println("Enter the start letter:");
        String letter = bufferedReader.readLine();

        ingredientService.findAllIngredientsNameStartsWithGivenLetters(letter)
                .forEach(System.out::println);
    }

    private void selectShampoosByPrice() throws IOException {
        System.out.println("Enter price:");
        double price = Double.parseDouble(bufferedReader.readLine());

        shampooService.findAllShampoosWithHigherGivenPrice(BigDecimal.valueOf(price))
                .forEach(System.out::println);
    }

    private void selectShampoosBySizeOrLabel() throws IOException {
        System.out.println("Enter the size:");
        Size size = Size.valueOf(bufferedReader.readLine());
        System.out.println("Enter label id:");
        long labelId = Long.parseLong(bufferedReader.readLine());

        shampooService.findAllShampoosWithGivenSizeAndLabelId(size, labelId).forEach(System.out::println);
    }

    private void selectShampooBySize() throws IOException {
        System.out.println("Enter the size:");
        Size size = Size.valueOf(bufferedReader.readLine());
        shampooService.findAllShampooWithGivenSize(size).forEach(System.out::println);
    }
}
