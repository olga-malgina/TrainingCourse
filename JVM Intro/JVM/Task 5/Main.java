package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        List<String> duplicates = new ArrayList<>();

        // adding the same strings to the list 50 times
        for (int i = 0; i < 50; ++i) {
            duplicates.add(new String("spaghetti"));
        }

        // and one more cycle of the same strings
        for (int i = 0; i < 30; ++i) {
            duplicates.add(new String("noodles"));
        }

        Map<String, Long> map = duplicates.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println(map);

        duplicates.forEach(System.out::println);

        String stop = "stop";
        Scanner sc = new Scanner(System.in);
        String input = "";
        while (!(input.equals(stop))) {
            input = sc.next();
            duplicates.add(input);
        }
    }
}
