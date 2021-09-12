package com.company;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Logger log = LogManager.getLogger(Main.class);
        log.info("Doing something");

        MyClassLoader myClassLoader = new MyClassLoader();

        ArrayList<Animal> animals = new ArrayList<>();
        animals.add((Animal) myClassLoader.findClass("com.company.Cat").getDeclaredConstructor().newInstance());
        animals.add((Animal) myClassLoader.findClass("com.company.Dog").getDeclaredConstructor().newInstance());

        for (Animal a : animals) {
            a.play();
            a.voice();
        }
    }
}
