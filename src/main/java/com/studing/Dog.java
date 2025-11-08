package com.studing;

public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }

    @Override
    public void makeNoise() {
        System.out.println(name + " says: Woof-woof!");
    }

    @Override
    public void eat() {
        System.out.println(name + " eats: bones, meat, dog food");
    }

    @Override
    public String getDescription() {
        return "Dog - domestic animal, human's best friend";
    }
}