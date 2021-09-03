package com.company;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<Person> persons = Arrays.asList(new Person("John", 25),
                new Person("Mary", 21),
                new Person("Jean", 26),
                new Person("Michael", 31),
                new Person("Ness", 13),
                new Person("Duster", 33),
                new Person("Anabelle", 18));

        Comparator<Person> byName = (p1, p2) -> p1.getName().compareTo(p2.getName());
        Comparator<Person> byAge = (p1, p2) -> p1.getAge() - p2.getAge();

        persons.stream()
                .sorted(byName)
                .map(Person::toString)
                .forEach(System.out::println);

        System.out.println("=================");

        persons.stream()
                .sorted(byAge)
                .map(Person::toString)
                .forEach(System.out::println);
    }
}
