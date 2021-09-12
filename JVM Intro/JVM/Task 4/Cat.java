package com.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cat implements Animal {

    private static final Logger log = LogManager.getLogger();

    public Cat() {

    }

    @Override
    public void play() {
        log.info("Playing method");
        System.out.println("Playing with a ribbon!");
    }

    @Override
    public void voice() {
        log.info("Voice method");
        System.out.println("Meow");
    }
}
