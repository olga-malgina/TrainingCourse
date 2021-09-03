package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

class CollectorMapOfNumbersTest {

    List<Number> numbers = Arrays.asList(new Number(1, "One"), new Number(2, "Two"),
            new Number(3, "Three"), new Number(4, "Four"), new Number(5, "Five"));

    List<Number> primeNumbers = Arrays.asList(new Number(1, "One"), new Number(3, "Three"),
            new Number(5, "Five"), new Number(7, "Seven"), new Number(11, "Eleven"));



    @org.junit.jupiter.api.Test
    void normalBehaviorTest() {
        Map<Integer, String> nums = numbers.stream()
                                            .collect(CollectorMapOfNumbers.toMapOfNumbers());
        assertEquals(5, nums.size());
    }

    @org.junit.jupiter.api.Test
    void zeroElementResultTest() {
        Map<Integer, String> nums = primeNumbers.stream()
                .filter(Number::isEven)
                .collect(CollectorMapOfNumbers.toMapOfNumbers());
        assertEquals(0, nums.size());
    }

    @org.junit.jupiter.api.Test
    void parallelStreamTest() {
        Map<Integer, String> nums = numbers.stream()
                .parallel()
                .collect(CollectorMapOfNumbers.toMapOfNumbers());
        assertEquals(5, nums.size());
    }

}