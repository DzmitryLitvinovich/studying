package com.studing;

public class Bear extends Animal {
    public Bear(String name) {
        super(name);
    }

    @Override
    public void makeNoise() {
        System.out.println(name + " says: Growl-growl!");
    }

    @Override
    public void eat() {
        System.out.println(name + " eats: meat, fish, berries, honey");
    }

    @Override
    public String getDescription() {
        return "Bear - large predatory animal, lives in the forest";
    }
}