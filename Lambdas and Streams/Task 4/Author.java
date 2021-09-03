package com.company;

import java.util.ArrayList;
import java.util.List;

public class Author {

    private String name;
    private short age;
    private List<Book> books;

    public Author(String name, short age) {
        this.name = name;
        this.age = age;
        this.books = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public short getAge() {
        return age;
    }

    public void setBook(Book book) {
        books.add(book);
    }

    public List<Book> getBooks() {
        return List.copyOf(books);
    }
}
