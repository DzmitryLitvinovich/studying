package com.studing;

public class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }

    @Override
    public void makeNoise() {
        System.out.println(name + " says: Meow-meow!");
    }

    @Override
    public void eat() {
        System.out.println(name + " eats: fish, milk, cat food");
    }

    @Override
    public String getDescription() {
        return "Cat - domestic animal, independent and graceful";
    }
}