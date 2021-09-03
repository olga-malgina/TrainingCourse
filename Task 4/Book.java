package com.company;

import java.util.List;

public class Book {

    private String title;
    private List<Author> authors;
    int numberOfPages;

    public Book(String title, int numberOfPages, List<Author> authors) {
        this.title = title;
        this.numberOfPages = numberOfPages;
        this.authors = List.copyOf(authors);
    }

    public String getTitle() {
        return title;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public List<Author> getAuthors() {
        return List.copyOf(authors);
    }
}
