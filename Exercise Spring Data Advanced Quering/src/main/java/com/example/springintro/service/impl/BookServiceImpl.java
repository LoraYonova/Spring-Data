package com.example.springintro.service.impl;

import com.example.springintro.model.entity.*;
import com.example.springintro.repository.BookRepository;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private static final String BOOKS_FILE_PATH = "src/main/resources/files/books.txt";

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (bookRepository.count() > 0) {
            return;
        }

        Files
                .readAllLines(Path.of(BOOKS_FILE_PATH))
                .forEach(row -> {
                    String[] bookInfo = row.split("\\s+");

                    Book book = createBookFromInfo(bookInfo);

                    bookRepository.save(book);
                });
    }

    @Override
    public List<Book> findAllBooksAfterYear(int year) {
        return bookRepository
                .findAllByReleaseDateAfter(LocalDate.of(year, 12, 31));
    }

    @Override
    public List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year) {
        return bookRepository
                .findAllByReleaseDateBefore(LocalDate.of(year, 1, 1))
                .stream()
                .map(book -> String.format("%s %s", book.getAuthor().getFirstName(),
                        book.getAuthor().getLastName()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName) {
       return bookRepository
                .findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(firstName, lastName)
                .stream()
                .map(book -> String.format("%s %s %d",
                        book.getTitle(),
                        book.getReleaseDate(),
                        book.getCopies()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllTitleOfBooks(AgeRestriction ageRestriction) {
        return bookRepository.findAllByAgeRestriction(ageRestriction)
                .stream().map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksWithEditionTypeGoldAndWithLessThan5000Copies() {
        return bookRepository.findAllByEditionTypeAndCopiesLessThan(EditionType.valueOf("GOLD"), 5000)
                .stream().map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksWithPriceLowerThan5OrHigherThan40() {

        return bookRepository.findAllByPriceIsLessThanOrPriceIsGreaterThan(BigDecimal.valueOf(5L), BigDecimal.valueOf(40L))
                .stream().map(book -> String.format("%s - $%.2f",
                        book.getTitle(),
                        book.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksThatAreNotReleasedInGivenYear(int year) {

        LocalDate lower = LocalDate.of(year, 1,1);
        LocalDate upper = LocalDate.of(year, 12, 31);

        return bookRepository.findAllByReleaseDateBeforeOrReleaseDateAfter(lower, upper)
                .stream().map(Book::getTitle)
                .collect(Collectors.toList());

    }

    @Override
    public List<String> findAllBooksBeforeGivenDate(LocalDate date) {
        return bookRepository.findAllByReleaseDateBefore(date)
                .stream().map(book -> String.format("%s %s %.2f",
                        book.getTitle(),
                        book.getEditionType(),
                        book.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksWitchContainGIvenInput(String letters) {
        return bookRepository.findAllByTitleContaining(letters)
                .stream().map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllAuthorsWhoseLastNameStartWith(String letters) {
        return bookRepository.findByTitleWhereAuthorLastNameLike(letters)
                .stream().map(book -> String.format("%s (%s %s)",
                        book.getTitle(),
                        book.getAuthor().getFirstName(),
                        book.getAuthor().getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public int findAllBooksWitchTitleIsLongerThanGivenNumber(int number) {
        return bookRepository.findByNumberOfBooks(number);
    }

    @Override
    public String findBookByGivenTitle(String title) {
        return bookRepository.findAByTitle(title);

    }

    @Override
    @Transactional
    public String increaseCopiesByDate(LocalDate localDate, int copies) {
        int affectedRows = bookRepository.updateCopiesByReleaseDate(copies, localDate);

        return String.format("%d books are released after %s, so total of %d book copies were added", affectedRows, localDate, affectedRows * copies);
    }

    @Override
    @Transactional
    public int removeBoosWitchCopiesLowerThan(int numberOfCopies) {
        return bookRepository.deleteBookByCopiesIsLessThan(numberOfCopies);
    }

    private Book createBookFromInfo(String[] bookInfo) {
        EditionType editionType = EditionType.values()[Integer.parseInt(bookInfo[0])];
        LocalDate releaseDate = LocalDate
                .parse(bookInfo[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        Integer copies = Integer.parseInt(bookInfo[2]);
        BigDecimal price = new BigDecimal(bookInfo[3]);
        AgeRestriction ageRestriction = AgeRestriction
                .values()[Integer.parseInt(bookInfo[4])];
        String title = Arrays.stream(bookInfo)
                .skip(5)
                .collect(Collectors.joining(" "));

        Author author = authorService.getRandomAuthor();
        Set<Category> categories = categoryService
                .getRandomCategories();

        return new Book(editionType, releaseDate, copies, price, ageRestriction, title, author, categories);

    }
}
