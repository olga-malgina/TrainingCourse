package com.company;

public class Dog implements Animal {

    public Dog() {

    }


    @Override
    public void play() {
        System.out.println("Playing with a ball!");
    }

    @Override
    public void voice() {
        System.out.println("Bork");
    }
}
