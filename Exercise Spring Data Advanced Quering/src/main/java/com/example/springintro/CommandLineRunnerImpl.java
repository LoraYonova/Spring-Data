package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader bufferedReader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

         printAllBooksAfterYear(2000);
         printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
         printAllAuthorsAndNumberOfTheirBooks();
         printALlBooksByAuthorNameOrderByReleaseDate("George", "Powell");

        System.out.println("Enter number of exercise:");
        int exNumber = Integer.parseInt(bufferedReader.readLine());

        switch (exNumber) {
            case 1 -> bookTitlesByAgeRestriction();
            case 2 -> goldenBooks();
            case 3 -> booksByPrice();
            case 4 -> notReleasedBooks();
            case 5 -> booksReleasedBeforeDate();
            case 6 -> authorsSearch();
            case 7 -> bookTitleSearch();
            case 8 -> booksTitlesSearch();
            case 9 -> countBooks();
            case 10 -> totalBookCopies();
            case 11 -> reducedBook();
            case 12 -> increaseBookCopies();
            case 13 -> removeBooks();

        }

    }



    private void removeBooks() throws IOException {
        System.out.println("Enter number:");
        int numberOfCopies = Integer.parseInt(bufferedReader.readLine());

        System.out.println(bookService.removeBoosWitchCopiesLowerThan(numberOfCopies));
    }

    private void increaseBookCopies() throws IOException {
        System.out.println("Enter the date:");

       LocalDate localDate = LocalDate.parse(bufferedReader.readLine(),
               DateTimeFormatter.ofPattern(("dd MMM yyyy")));

        System.out.println("Enter number of book copies:");
        int copies = Integer.parseInt(bufferedReader.readLine());

        System.out.println(bookService.increaseCopiesByDate(localDate, copies));

    }

    private void reducedBook() throws IOException {
        System.out.println("Enter the book title:");
        String title = bufferedReader.readLine();
        String bookByGivenTitle = bookService.findBookByGivenTitle(title);
        System.out.println(bookByGivenTitle);

    }

    private void totalBookCopies() {
        authorService.findAllAuthorsOfBookCopies().forEach(System.out::println);
    }

    private void countBooks() throws IOException {
        System.out.println("Enter number:");
        int number = Integer.parseInt(bufferedReader.readLine());

        int count = bookService.findAllBooksWitchTitleIsLongerThanGivenNumber(number);
        System.out.println(count);

    }

    private void booksTitlesSearch() throws IOException {
        System.out.println("Enter the letters that start the last name:");
        String letters = bufferedReader.readLine();

        bookService.findAllAuthorsWhoseLastNameStartWith(letters + "%").forEach(System.out::println);
    }

    private void bookTitleSearch() throws IOException {
        System.out.println("Enter letters:");
        String letters = bufferedReader.readLine();

        bookService.findAllBooksWitchContainGIvenInput(letters).forEach(System.out::println);
    }

    private void authorsSearch() throws IOException {
        System.out.println("Enter the letters to complete the first name:");
        String letters = bufferedReader.readLine();

        authorService.findAllAuthorsWhoseFirstNameEndsWith(letters).forEach(System.out::println);
    }

    private void booksReleasedBeforeDate() throws IOException {
        System.out.println("Enter the date:");
        String[] input = bufferedReader.readLine().split("-");
        int day = Integer.parseInt(input[0]);
        int month = Integer.parseInt(input[1]);
        int year = Integer.parseInt(input[2]);
        LocalDate date = LocalDate.of(year, month, day);

        bookService.findAllBooksBeforeGivenDate(date).forEach(System.out::println);
    }

    private void notReleasedBooks() throws IOException {
        System.out.println("Enter the year:");
        int year = Integer.parseInt(bufferedReader.readLine());
        bookService.findAllBooksThatAreNotReleasedInGivenYear(year).forEach(System.out::println);
    }

    private void booksByPrice() {
        bookService.findAllBooksWithPriceLowerThan5OrHigherThan40().forEach(System.out::println);
    }

    private void goldenBooks() {

        bookService.findAllBooksWithEditionTypeGoldAndWithLessThan5000Copies().forEach(System.out::println);
    }

    private void bookTitlesByAgeRestriction() throws IOException {
        System.out.println("Enter Age Restriction:");
        AgeRestriction ageRestriction = AgeRestriction.valueOf(bufferedReader.readLine().toUpperCase());

        bookService.findAllTitleOfBooks(ageRestriction).forEach(System.out::println);

    }

    private void printALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
