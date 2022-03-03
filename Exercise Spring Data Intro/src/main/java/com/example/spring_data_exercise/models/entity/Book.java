package com.example.spring_data_exercise.models.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book extends BaseEntity {

    @Column(length = 50)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated
    private EditionType editionType;

    @Column
    private BigDecimal price;

    @Column
    private Integer copies;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Enumerated
    private EgeRestriction ageRestriction;

    @ManyToOne
    private Author author;

    @ManyToMany
    private Set<Category> categories;

    public Book() {
    }

    public Book(EditionType editionType, LocalDate releaseDAte, Integer copies, BigDecimal price, EgeRestriction ageRestriction, String title, Author author, Set<Category> categories) {
        this.editionType = editionType;
        this.releaseDate = releaseDAte;
        this.copies = copies;
        this.price = price;
        this.ageRestriction = ageRestriction;
        this.title = title;
        this.author = author;
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EditionType getEditionType() {
        return editionType;
    }

    public void setEditionType(EditionType editionType) {
        this.editionType = editionType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public EgeRestriction getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(EgeRestriction egeRestriction) {
        this.ageRestriction = egeRestriction;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author authors) {
        this.author = authors;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
