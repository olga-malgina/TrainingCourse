package com.company;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class CollectorMapOfNumbers implements Collector<Number, Map<Integer, String>, Map<Integer, String>> {

    public static CollectorMapOfNumbers toMapOfNumbers() {
        return new CollectorMapOfNumbers();
    }

    @Override
    public Supplier<Map<Integer, String>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<Integer, String>, Number> accumulator() {
        return (map, num) -> map.put(num.getNum(), num.getName());
    }

    @Override
    public BinaryOperator<Map<Integer, String>> combiner() {
        return (map1, map2) -> {
            map1.putAll(map2);
            return map1;
        };
    }

    @Override
    public Function<Map<Integer, String>, Map<Integer, String>> finisher() {
        return Collections::unmodifiableMap;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }
}
