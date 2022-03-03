package com.example.spring_data_exercise;

import com.example.spring_data_exercise.models.entity.Book;
import com.example.spring_data_exercise.services.AuthorService;
import com.example.spring_data_exercise.services.BookService;
import com.example.spring_data_exercise.services.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConsoleRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    public ConsoleRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }


    @Override
    public void run(String... args) throws Exception {
        seedData();
        printAllBooksAfter2000(2000);
        printAllAuthorsNamesWithBooksWithReleaseDateBefore1990(1990);
        printAllAuthorsNumberOfTheirBooks();
        printAllBooksByAuthorNameOrderByReleaseDate("George", "Powell");


    }

    private void printAllBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService.findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsNumberOfTheirBooks() {
        authorService.getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBefore1990(int year) {
        bookService.findAllAuthorsNameWithBookWithReleaseDataBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfter2000(int year) {
        bookService.findAllBooksAfter2000(year)
                .stream().map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
