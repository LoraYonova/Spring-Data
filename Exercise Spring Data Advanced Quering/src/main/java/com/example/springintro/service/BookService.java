package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> findAllTitleOfBooks(AgeRestriction ageRestriction);

    List<String> findAllBooksWithEditionTypeGoldAndWithLessThan5000Copies();

    List<String> findAllBooksWithPriceLowerThan5OrHigherThan40();


    List<String> findAllBooksThatAreNotReleasedInGivenYear(int year);

    List<String> findAllBooksBeforeGivenDate(LocalDate date);

    List<String> findAllBooksWitchContainGIvenInput(String letters);

    List<String> findAllAuthorsWhoseLastNameStartWith(String letters);

    int findAllBooksWitchTitleIsLongerThanGivenNumber(int number);


    String findBookByGivenTitle(String title);

    String increaseCopiesByDate(LocalDate localDate, int copies);

    int removeBoosWitchCopiesLowerThan(int numberOfCopies);
}
