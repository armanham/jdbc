package com.aca.db.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends AbstractEntity {

    private String name;

    private String surname;

    private String email;

    private Integer age;

    private boolean active;

    public User(ResultSet resultSet) throws SQLException {
        super(resultSet);
        this.name = resultSet.getString("name");
        this.surname = resultSet.getString("surname");
        this.age = resultSet.getInt("age");
        this.email = resultSet.getString("email");
        this.active = resultSet.getBoolean("active");
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}