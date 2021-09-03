package com.company;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Author[] authors = {new Author("Charles Petzold", (short) 56), new Author("Scott Chacon", (short) 43),
                new Author("Ben Straub", (short) 39), new Author("Joshua Bloch", (short) 61),
                new Author("Robert Sedgewick", (short) 72), new Author("Kevin Wayne", (short) 52),
                new Author("Bill Murray", (short) 64), new Author("Stephen King", (short) 60),
                new Author("Neil Gaiman", (short) 52), new Author("Dio Brando", (short) 31)};

        Book[] books = {new Book("Code", 326, Arrays.asList(authors[0])),
                        new Book("ProGit", 519, Arrays.asList(authors[1], authors[2])),
                        new Book("Effective Java", 435, Arrays.asList(authors[3], authors[1])),
                        new Book("Algorithms", 741, Arrays.asList(authors[4], authors[5])),
                        new Book("Short History of Everything", 561, Arrays.asList(authors[6])),
                        new Book("The Stand", 1784, Arrays.asList(authors[7])),
                        new Book("Green Mile", 213, Arrays.asList(authors[7])),
                        new Book("Good omens", 487, Arrays.asList(authors[7], authors[8]))};

        authors[0].setBook(books[0]);
        authors[1].setBook(books[1]);
        authors[2].setBook(books[1]);
        authors[3].setBook(books[2]);
        authors[4].setBook(books[3]);
        authors[5].setBook(books[3]);
        authors[6].setBook(books[4]);
        authors[7].setBook(books[5]);
        authors[7].setBook(books[6]);
        authors[7].setBook(books[7]);
        authors[8].setBook(books[7]);



        // Subtask 1 - filter books with more than 200 pages
        System.out.println("================");
        Arrays.stream(books)
                .filter(book -> book.getNumberOfPages() > 200)
                .forEach(book -> System.out.println(book.getTitle()));

        // Subtask 2 - find the book with max number of pages
        System.out.println("================");
        System.out.println("The thickest book is: " + Arrays.stream(books)
                                                            .max(Comparator.comparingInt(Book::getNumberOfPages))
                                                            .get()
                                                            .getTitle());

        // Subtask 2 - find the book with min number of pages
        System.out.println("================");
        System.out.println("The thinnest book is: " + Arrays.stream(books)
                                                            .min(Comparator.comparingInt(Book::getNumberOfPages))
                                                            .get()
                                                            .getTitle());

        // Subtask 3 - filter books with a single author
        System.out.println("================");
        System.out.println("Books with single author are:");
        Arrays.stream(books)
                .filter(book -> book.getAuthors().size() == 1)
                .forEach(book -> System.out.println(book.getTitle()));

        // Subtask 3 - with parallel streaming. Noticeable difference - the output is in different order
        System.out.println("================");
        System.out.print("Books with single author are: ");
        Arrays.stream(books)
                .parallel()
                .filter(book -> book.getAuthors().size() == 1)
                .forEach(book -> System.out.println(book.getTitle()));

        // Subtask 4 - sort books by number of pages, title
        System.out.println("================");
        System.out.println("Sorted books: ");
        Arrays.stream(books)
                .sorted(Comparator.comparing(Book::getNumberOfPages).thenComparing(Book::getTitle))
                .forEach(book -> System.out.println(book.getTitle() + " " + book.getNumberOfPages() + " pages"));

        // Subtask 5 + 6 - get a list of all titles and print them
        System.out.println("================");
        System.out.println("Printing all the titles: ");
        Arrays.stream(books)
                .map(Book::getTitle)
                .collect(Collectors.toList())
                .forEach(System.out::println);

        // Subtask 7 - get distinct list of all authors
        System.out.println("================");
        System.out.println("Printing the authors: ");
        Arrays.stream(books)
                .map(Book::getAuthors)
                .flatMap(Collection::stream)
                .map(Author::getName)
                .distinct()
                .collect(Collectors.toList())
                .forEach(System.out::println);

        // Task 4 - the title of the biggest book of some author (testing on an author with and without books)
        System.out.println("================");
        String author1 = "Stephen King";
        String author2 = "Dio Brando";

        Optional<Book> largestKingsBook = Arrays.stream(authors)
                                        .filter(author -> author.getName().equals(author1))
                                        .map(Author::getBooks)
                                        .flatMap(Collection::stream)
                                        .max(Comparator.comparingInt(Book::getNumberOfPages));

        System.out.println("Largest book by Stephen King is " + (largestKingsBook.isPresent() ?
                                                                    largestKingsBook.get().getTitle() : "No such book"));


        Optional<Book> largestBrandosBook = Arrays.stream(authors)
                .filter(author -> author.getName().equals(author2))
                .map(Author::getBooks)
                .flatMap(Collection::stream)
                .max(Comparator.comparingInt(Book::getNumberOfPages));

        System.out.println("Largest book by Dio Brando is " + (largestBrandosBook.isPresent() ? largestBrandosBook.get().getTitle() : "No such book"));

    }
}
