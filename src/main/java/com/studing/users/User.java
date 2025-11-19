package com.studing.users;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private Long id;
    private String name;
    private String surname;
    private int age;
    private String username;
    private String password;
    private LocalDateTime insertedDateAtUtc;
    private LocalDateTime updatedDateAtUtc;

    public User() {}

    public User(Long id, String name, String surname, int age, String username,
                String password, LocalDateTime insertedDateAtUtc, LocalDateTime updatedDateAtUtc) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.username = username;
        this.password = password;
        this.insertedDateAtUtc = insertedDateAtUtc;
        this.updatedDateAtUtc = updatedDateAtUtc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getInsertedDateAtUtc() {
        return insertedDateAtUtc;
    }

    public void setInsertedDateAtUtc(LocalDateTime insertedDateAtUtc) {
        this.insertedDateAtUtc = insertedDateAtUtc;
    }

    public LocalDateTime getUpdatedDateAtUtc() {
        return updatedDateAtUtc;
    }

    public void setUpdatedDateAtUtc(LocalDateTime updatedDateAtUtc) {
        this.updatedDateAtUtc = updatedDateAtUtc;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(age, user.age) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(insertedDateAtUtc, user.insertedDateAtUtc) && Objects.equals(updatedDateAtUtc, user.updatedDateAtUtc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, age, username, password, insertedDateAtUtc, updatedDateAtUtc);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", insertedDateAtUtc=" + insertedDateAtUtc +
                ", updatedDateAtUtc=" + updatedDateAtUtc +
                '}';
    }
}
