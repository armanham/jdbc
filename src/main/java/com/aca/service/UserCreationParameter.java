package com.aca.service;

public class UserCreationParameter {
    private final String name;

    private final String surname;

    private final String email;

    private final Integer age;

    public UserCreationParameter(String name, String surname, String email, Integer age) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
