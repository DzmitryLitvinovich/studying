package com.studing;

public class StudyingApp {
    public static void main(String[] args) {
        Animal[] animals = new Animal[3];

        animals[0] = new Dog("Buddy");
        animals[1] = new Cat("Whiskers");
        animals[2] = new Bear("Misha");

        Veterinarian vet = new Veterinarian();

        System.out.println("=== Veterinary Appointment ===");

        for (Animal animal : animals) {
            vet.treatAnimal(animal);
        }

        System.out.println("=== Animal Behavior ===");

        for (Animal animal : animals) {
            animal.makeNoise();
            animal.eat();
            System.out.println();
        }
    }
}
