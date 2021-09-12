package com.company;

import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;

public class Main {

    static ClassPool cp = ClassPool.getDefault();

    // a classic faulty factorial function for Task 4 (the one missing the base case)
    static int factorial(int n) {
        return(n * factorial(n-1));
    }

    // add throws CannotCompileException
    public static void main(String[] args) throws CannotCompileException {
        // Subtask 1 - Out of memory error with different data structures
        // The program crashes with Exception in thread "main" java.lang.OutOfMemory: Java heap space
        // If I add printing out of i, the program runs much slower
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < Integer.MAX_VALUE; ++i) {
            list.add(Integer.MAX_VALUE);
        }

        // Subtask 2 - Out of memory error - creating big objects continuously and keeping them in memory
        // Since concatenation is used via "+=" a new String object is created for each iteration
        // Works especially well when heap size is reduced to 5MB

        String s = "";
        for (int i = 0; i < Integer.MAX_VALUE; ++i) {
            s += "VeeeeeerrrrryyyyyyyllllllooooooooonnnnnnggggggSsssssssssttttttttrrrrrrrriiiiiiiiinnnnnnnnggggggg";
        }

        // Subtask 3 - load classes continuously to get Out of Memory - Metaspace error
        // Used javassist library here to generate classes
        for (int i = 0; i < Integer.MAX_VALUE; ++i) {
            Class c = cp.makeClass("com.company.DefaultClass" + i).toClass();
        }

        // Subtask 4 - get StackOverflow Error with recursive methods
        // Throws the StackOverflow exception as the function factorial is called infinite times
        System.out.println(factorial(16));

        // Subtask 5 - get StackOverflow Error without recursion
        // Might have been accomplished via generating a lot of variables of primitive type (allocated on stack)
        // But creating a cyclical dependency between 2 classes is easier :D
        Egg egg = new Egg();
    }
}
