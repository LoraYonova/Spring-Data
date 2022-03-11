package com.example.springintro.repository;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Author;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDateAfter);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String author_firstName, String author_lastName);

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, Integer copies);

    List<Book> findAllByPriceIsLessThanOrPriceIsGreaterThan(BigDecimal price, BigDecimal price2);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate releaseDate, LocalDate releaseDate2);

    List<Book> findAllByTitleContaining(String title);

    @Query("SELECT b FROM Book b " +
            "WHERE b.author.lastName like :lastName ")
    List<Book> findByTitleWhereAuthorLastNameLike(String lastName);

    @Query("SELECT count(b) FROM Book b " +
            "WHERE length (b.title) > :count")
    int findByNumberOfBooks(int count);

    @Query("SELECT b.title, b.editionType, b.ageRestriction, b.price FROM Book b " +
            "WHERE b.title = :title")
    String findAByTitle(String title);

    @Modifying
    @Query("UPDATE Book b SET b.copies = b.copies + :copies WHERE b.releaseDate > :date")
    int updateCopiesByReleaseDate(@Param(value = "copies") int copies,
                                  @Param(value = "date") LocalDate date);

    @Modifying
    int deleteBookByCopiesIsLessThan(Integer copies);
}