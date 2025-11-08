package com.studing;

public abstract class Animal {
    String name;

    public Animal(String name) {
        this.name = name;
    }

    public abstract void makeNoise();
    public abstract void eat();
    public abstract String getDescription();
}