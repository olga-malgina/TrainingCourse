package com.company;

import java.math.BigInteger;

public class Number {

    private int num;
    private String name;
    private boolean isEven;
    private boolean isPrime;

    public Number(int num, String name) {
        this.num = num;
        this.name = name;
        this.isEven = num % 2 == 0;
        this.isPrime = BigInteger.valueOf(num).isProbablePrime(100);
    }

    public int getNum() {
        return num;
    }

    public String getName() {
        return name;
    }

    public boolean isEven() {
        return isEven;
    }

    public boolean isPrime() {
        return isPrime;
    }
}
