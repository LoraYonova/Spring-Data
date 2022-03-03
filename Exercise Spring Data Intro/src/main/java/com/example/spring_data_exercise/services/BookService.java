package com.example.spring_data_exercise.services;

import com.example.spring_data_exercise.models.entity.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;
    List<Book> findAllBooksAfter2000(int year);

    List<String> findAllAuthorsNameWithBookWithReleaseDataBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);


}
