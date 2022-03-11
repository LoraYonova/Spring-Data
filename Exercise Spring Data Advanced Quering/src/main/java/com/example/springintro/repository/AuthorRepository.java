package com.example.springintro.repository;

import com.example.springintro.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a ORDER BY size(a.books) DESC")
    List<Author> findAllByBooksSizeDESC();

    List<Author> findAllByFirstNameEndsWith(String firstName);

//    @Query("SELECT a.firstName, a.lastName, sum (b.copies) AS counts FROM Author a " +
//            "JOIN Book b " +
//            "GROUP BY counts " +
//            "ORDER BY counts DESC ")
//    List<Author> findByBooksCopies();



}
