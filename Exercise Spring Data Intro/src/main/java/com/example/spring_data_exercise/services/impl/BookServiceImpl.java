package com.example.spring_data_exercise.services.impl;

import com.example.spring_data_exercise.models.entity.*;
import com.example.spring_data_exercise.repository.BookRepository;
import com.example.spring_data_exercise.services.AuthorService;
import com.example.spring_data_exercise.services.BookService;
import com.example.spring_data_exercise.services.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private static final String BOOK_FILE_PATH = "src/main/resources/files/books.txt";
    private final AuthorService authorService;
    private final CategoryService categoryService;

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

        Files.readAllLines(Path.of(BOOK_FILE_PATH))
                .forEach(books -> {
                    String[] bookInfo = books.split("\\s+");
                    Book book = createBookFromIno(bookInfo);
                    bookRepository.save(book);
                });
    }

    @Override
    public List<Book> findAllBooksAfter2000(int year) {

        return bookRepository.findAllByReleaseDateAfter(LocalDate.of(year, 12, 31));


    }

    @Override
    public List<String> findAllAuthorsNameWithBookWithReleaseDataBeforeYear(int year) {
            return bookRepository.findAllByReleaseDateBefore(LocalDate.of(year, 1,1))
                    .stream()
                    .map(book -> String.format("%s %s",
                            book.getAuthor().getFirstName(),
                            book.getAuthor().getLastName()))
                    .distinct()
                    .collect(Collectors.toList());

    }

    @Override
    public List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName) {
       return bookRepository.findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(firstName, lastName)
                .stream().map(book -> String.format("%S %s %d",
                        book.getTitle(),
                        book.getReleaseDate(),
                        book.getCopies()))
                .collect(Collectors.toList());
    }

    private Book createBookFromIno(String[] bookInfo) {
        EditionType editionType = EditionType.values()[Integer.parseInt(bookInfo[0])];
        LocalDate releaseDAte = LocalDate.parse(bookInfo[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        Integer copies = Integer.parseInt(bookInfo[2]);
        BigDecimal price = new BigDecimal(bookInfo[3]);
        EgeRestriction ageRestriction = EgeRestriction.values()[Integer.parseInt(bookInfo[4])];
        String title = Arrays.stream(bookInfo).skip(5)
                .collect(Collectors.joining(" "));

        Author author = authorService.getRandomAuthor();

        Set<Category> categories = categoryService.getRandomCategories();

        Book book = new Book(editionType, releaseDAte, copies, price, ageRestriction, title, author, categories);

        return book;
    }
}
