package com.company;

public class Main {

    public static void main(String[] args) {

        ThreeFunction summator = (first, second, third) -> first + second + third;
        ThreeFunction max = (first, second, third) -> first > second ? (Math.max(first, third)) :
                (Math.max(second, third));

        System.out.println(summator.calculate(45, 59, 784));
        System.out.println(max.calculate(45, 59, 784));

    }
}
