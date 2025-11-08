package com.studing;

public class Veterinarian {
    public void treatAnimal(Animal animal) {
        System.out.println("At the appointment: " + animal.name);
        System.out.println("Description: " + animal.getDescription());
        System.out.println("---");
    }
}